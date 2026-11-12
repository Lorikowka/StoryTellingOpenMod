package com.storyteam.storytelling.script.performer.entity.behaviour.phrases;

import com.google.common.collect.Lists;
import com.storyteam.storytelling.api.data.EntityInteractionData;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.ILookTarget;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.RoundRobinList;

public class PhrasesBehaviour<T extends AnimatedEntity> implements IStoryBehaviour<T> {
   private final RoundRobinList<Phrase<T>> phrases;
   private final String entity;
   @Nullable
   Phrase<T> active;
   int innerCounter;
   ILookTarget<T> tempData;
   private boolean isInterrupter;
   private int lookCounter;
   private final PhraseOrder order;
   private static final RandomSource SOURCE = RandomSource.create();

   @SafeVarargs
   public PhrasesBehaviour(String entityName, Phrase<T>... phrases) {
      this(entityName, PhraseOrder.LOOP, phrases);
   }

   @SafeVarargs
   public PhrasesBehaviour(String entityName, PhraseOrder order, Phrase<T>... phrases) {
      this.phrases = new RoundRobinList(Lists.newArrayList(phrases));
      this.entity = entityName;
      this.order = order;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> context) {
      if (this.active != null) {
         if (this.lookCounter > 0) {
            if (--this.lookCounter == 0) {
               this.startAnimations(this.active, context);
            }
         } else {
            if (this.active.getDelay() >= 0 && this.innerCounter > 0) {
               this.innerCounter--;
               if (this.innerCounter == 0) {
                  if (this.active.hasNext()) {
                     this.activePhrase(this.active, context, null);
                  } else {
                     this.onPhraseEnd(context);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> context) {
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> context) {
      this.onPhraseEnd(context);
   }

   @Override
   public <E> void handleEvent(EventType<E> type, E event, EntityContext<T> context) {
      if (this.canStillUse(IStoryServer.get().getScriptEnv(), context)) {
         if (type == EventType.ENTITY_CLICK_ONE_HAND && !((EntityInteractionData)event).player.isCrouching()) {
            this.onEntityClick((EntityInteractionData)event, context);
         } else if (type == EventType.F8_PRESS) {
            this.onF8Pressed(context);
         }
      }
   }

   private void onF8Pressed(EntityContext<T> context) {
      if (this.active != null) {
         if (this.active.getDelay() < 0 && this.innerCounter > 0) {
            this.innerCounter = 0;
            if (this.active.hasNext()) {
               this.activePhrase(this.active.getNext(), context, ScriptUtils.getPlayer());
            } else {
               this.onPhraseEnd(context);
            }
         }
      }
   }

   private void onEntityClick(EntityInteractionData event, EntityContext<T> context) {
      if (this.innerCounter <= 0 && event.target == context.getEntity()) {
         this.activePhrase(this.choosePhrase(), context, event.player);
      }
   }

   private Phrase<T> choosePhrase() {
      if (this.phrases.isEmpty()) {
         throw new IllegalStateException("Список фраз не должен использоваться, когда он пустой.");
      } else {
         return switch (this.order) {
            case LOOP -> (Phrase)this.phrases.next();
            case RANDOM -> (Phrase)this.phrases.get(SOURCE.nextInt(this.phrases.size()));
            case ONLY_ONCE -> {
               int num = this.phrases.getPos();
               Phrase<T> next = (Phrase<T>)this.phrases.next();
               this.phrases.remove(num);
               yield next;
            }
         };
      }
   }

   private void startAnimations(Phrase<T> phrase, EntityContext<T> context) {
      if (phrase.getEmotion() != null) {
         ((AnimatedEntity)context.getEntity()).startAnimation("emotion", phrase.getEmotion());
      }

      if (phrase.getAction() != null) {
         ((AnimatedEntity)context.getEntity()).startAnimation("action", phrase.getAction());
      }

      phrase.getOthers().forEach(((AnimatedEntity)context.getEntity())::startAnimation);
   }

   private void activePhrase(Phrase<T> phrase, EntityContext<T> context, @Nullable Player target) {
      if (phrase.isLookOnPlayer() && target != null) {
         if (!this.isInterrupter) {
            this.tempData = context.getState().getData().getLookTarget();
            context.getState().getData().setLookTarget(new EntityTarget(target));
            this.isInterrupter = true;
            context.getState().interruptBehaviours(this);
            this.lookCounter = 40;
         } else {
            this.startAnimations(phrase, context);
         }
      } else {
         if (this.isInterrupter) {
            context.getState().getData().setLookTarget(this.tempData);
            this.tempData = null;
            this.isInterrupter = false;
            context.getState().continueBehaviours(this);
         }

         this.startAnimations(phrase, context);
      }

      this.active = phrase;
      ScriptUtils.sendMessageToPlayers(this.entity, this.active.getPhrase());
      this.innerCounter = phrase.getDelay() >= 0 ? phrase.getDelay() : 1;
   }

   private void onPhraseEnd(EntityContext<T> context) {
      if (this.active != null) {
         this.active.getRunnable().accept(context);
         if (this.active.getEmotion() != null) {
            ((AnimatedEntity)context.getEntity()).stopAnimation("emotion");
         }

         if (this.active.getAction() != null) {
            ((AnimatedEntity)context.getEntity()).stopAnimation("action");
         }

         this.active.getOthers().keySet().forEach(((AnimatedEntity)context.getEntity())::stopAnimation);
         if (this.isInterrupter) {
            this.isInterrupter = false;
            context.getState().continueBehaviours(this);
            context.getState().getData().setLookTarget(this.tempData);
            this.tempData = null;
         }
      }
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> context) {
      return !this.phrases.isEmpty() || this.active != null && this.active.hasNext();
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.PHRASE;
   }
}
