package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.google.common.collect.Lists;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.StoryFightEvent;
import com.storyteam.storytelling.events.StoryFightTickEvent;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.AttackManager;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.data.EntityContext;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class EntityFightBehaviour<T extends AnimatedEntity> implements IStoryBehaviour<T> {
   private final IAttackChain<T> chain;
   private Attack<T> activeAttack;
   private Attack<T> potentianAttack;
   public final AttackManager<T> manager = new AttackManager<>();

   public EntityFightBehaviour(IAttackChain<T> chain) {
      this.chain = chain;
   }

   @Override
   public void tickEntity(ScriptEnvironment env, EntityContext<T> packet) {
      env.postEventToInstances(EventType.STORY_FIGHT_TICK_EVENT_EVENT_PRE, new StoryFightTickEvent());
      IStoryServer.get().postStoryEvent(EventType.STORY_FIGHT_TICK_EVENT_EVENT_PRE, new StoryFightTickEvent());
      if (!((AnimatedEntity)packet.getEntity()).getPersistentData().getCompound("Storytelling").getBoolean("Vulnerable")) {
         ((AnimatedEntity)packet.getEntity()).getPersistentData().getCompound("Storytelling").putBoolean("Vulnerable", true);
      }

      if (this.canStillUse(env, packet)) {
         if (this.activeAttack != null) {
            if (this.manager.isCancelled() || this.activeAttack.getAction().doAttack(this.manager, packet)) {
               StoryFightEvent<T> event = new StoryFightEvent<>(this.activeAttack, packet);
               env.postEventToInstances(EventType.ATTACK_FINISH, event);
               IStoryServer.get().postStoryEvent(EventType.ATTACK_FINISH, event);
               MinecraftForge.EVENT_BUS.post(event);
               this.manager.reset();
               this.activeAttack = null;
               this.manager.activeAttack = null;
            }
         } else if (this.potentianAttack != null) {
            if (this.manager.isCancelled() || this.potentianAttack.tryCompleteConditions(this.manager, packet)) {
               this.manager.reset();
               this.potentianAttack = null;
            }
         } else {
            Attack<T> offered = this.manager.nextOffered();
            if (offered != null && this.chain.onStartOfferedAttack(offered)) {
               this.activeAttack = offered;
               this.manager.activeAttack = this.activeAttack;
            } else {
               this.activeAttack = this.chain.getAttackAndTick(packet);
               this.manager.activeAttack = this.activeAttack;
            }

            if (this.activeAttack != null) {
               this.manager.reset();
               env.postEventToInstances(EventType.ATTACK_START, new StoryFightEvent<>(this.activeAttack, packet));
            } else {
               this.potentianAttack = this.chain.getPotentialAttack(packet);
               if (this.potentianAttack == null) {
                  env.postEventToInstances(EventType.ATTACK_NOT_FOUND, packet);
               } else {
                  this.manager.reset();
               }
            }
         }

         env.postEventToInstances(EventType.STORY_FIGHT_TICK_EVENT_EVENT_POST, new StoryFightTickEvent());
         IStoryServer.get().postStoryEvent(EventType.STORY_FIGHT_TICK_EVENT_EVENT_POST, new StoryFightTickEvent());
      }
   }

   @Override
   public void onStart(ScriptEnvironment env, EntityContext<T> packet) {
   }

   @Override
   public void onStop(ScriptEnvironment env, EntityContext<T> packet) {
      if (this.activeAttack != null) {
         StoryFightEvent<T> event = new StoryFightEvent<>(this.activeAttack, packet);
         env.postEventToInstances(EventType.ATTACK_FINISH, event);
         IStoryServer.get().postStoryEvent(EventType.ATTACK_FINISH, event);
         MinecraftForge.EVENT_BUS.post(event);
      }

      for (String layer : Lists.newArrayList(((AnimatedEntity)packet.getEntity()).getAnimationSystem().getLayerNames())) {
         ((AnimatedEntity)packet.getEntity()).getAnimationSystem().stopAnimation(layer);
      }

      packet.getState().getData().setLookTarget(null);
      packet.getState().getData().setOverrideAnimationAll(null);
   }

   @Override
   public boolean canStillUse(ScriptEnvironment env, EntityContext<T> packet) {
      return this.chain.isFightActive(packet);
   }

   @NotNull
   @Override
   public BehaviourType getType() {
      return BehaviourType.FIGHT;
   }

   @Override
   public <E> void handleEvent(EventType<E> type, E event, EntityContext<T> content) {
      this.chain.handleEvent(type, event, new FightContext<>(content, this.manager));
      if (this.activeAttack != null) {
         this.activeAttack.handleEvent(type, event, new FightContext<>(content, this.manager));
      }
   }
}
