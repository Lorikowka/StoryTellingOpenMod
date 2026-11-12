package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.Arrays;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.core.init.DefaultsHA;

public class EntitySetAnimationAction<E extends AnimatedEntity> extends EntityPerformerAction<E> {
   private final String layer;
   private final Random random;
   private final ConfiguredAnimation[] animations;
   private boolean started;

   public EntitySetAnimationAction(ScriptPerformer performer, @NotNull Character<E> character, String layer, ConfiguredAnimation... animations) {
      this(performer, character, layer, null, animations);
   }

   public EntitySetAnimationAction(
      ScriptPerformer performer, @NotNull Character<E> character, String layer, @Nullable Random random, ConfiguredAnimation... animations
   ) {
      super(performer, character);
      this.layer = layer;
      this.animations = animations;
      this.random = random == null ? DEFALT_RANDOM : random;
   }

   @Override
   public void tick() {
      if (!this.started) {
         if (this.character == CharactersST8.SKY_EYE) {
            E e = (E)this.character.repositoryEntity();
            if (e != null) {
               e.startAnimation(this.layer, this.animations[this.random.nextInt(this.animations.length)]);
            }
         } else {
            this.getEntity().setAnimation(this.layer, this.animations[this.random.nextInt(this.animations.length)]);
         }

         this.started = true;
      }
   }

   @Override
   public void onInit() {
      this.started = false;
   }

   @Override
   public void reset() {
      this.started = false;
   }

   @Override
   public void fastForwardToStage() {
      if (this.character == CharactersST8.SKY_EYE) {
         E e = (E)this.character.repositoryEntity();
         if (e != null) {
            e.startAnimation(this.layer, this.animations[this.random.nextInt(this.animations.length)]);
         }
      } else {
         this.getEntity().setAnimation(this.layer, this.animations[this.random.nextInt(this.animations.length)]);
      }
   }

   @Override
   public void rewindToStage() {
      if (this.character == CharactersST8.SKY_EYE) {
         E e = (E)this.character.repositoryEntity();
         if (e != null) {
            e.startAnimation(this.layer, DefaultsHA.NULL_ANIM.configure());
         }
      } else {
         this.getEntity().setAnimation(this.layer, DefaultsHA.NULL_ANIM.configure());
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append(
         "Запуск одной из анимаций анимации %s на персонаже [%s] в слое %s.",
         Arrays.stream(this.animations).map(ConfiguredAnimation::getLocation).toArray(),
         this.character.getName(),
         this.layer
      );
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("animations", this.animations).build();
   }
}
