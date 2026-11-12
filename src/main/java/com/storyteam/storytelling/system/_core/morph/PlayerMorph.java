package com.storyteam.storytelling.system._core.morph;

import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.api.animations.LivingAnimationSet;
import com.storyteam.storytelling.client.texture.Material;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import lombok.Generated;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.util.java.functions.Function2;

public class PlayerMorph {
   private final IGeometryContainer model;
   private final LivingAnimationSet<Player> animations;
   private final Material material;
   private final Predicate<Player> disableVanillaAnimationsWhen;
   private final Function2<Player, Pose, Float> eyeHeight;
   private final MorphSizeHandler entitySize;
   private final boolean renderLayers;

   public void update(Player player, AnimationSystem system) {
      if (this.animations != null) {
         if (this.entitySize != null) {
            this.entitySize.update(player);
         }

         for (Entry<String, AnimationWithCondition<Player>> entry : this.animations.getAnimations().entrySet()) {
            AnimationLayer layer = system.getLayer(entry.getKey());
            if (layer != null) {
               AnimationWithCondition<Player> val = entry.getValue();
               if (layer.activeAnimation() == DefaultsHA.NULL_ANIMATION_SYNTETIC) {
                  if (val.canStart(player)) {
                     val.onStart(player);
                     layer.startAnimation(val.getAnimation(player));
                  }
               } else if (val.canContinue(player)) {
                  layer.startAnimation(val.getAnimation(player));
               } else {
                  val.onEnd(player);
                  layer.stopAnimation();
               }
            }
         }
      }
   }

   public void setupPlayerAnimationSystem(Builder builder) {
      if (this.animations != null) {
         builder.addLayers(this.animations.getAnimations().keySet().toArray(new String[0]));
      }
   }

   public boolean disableVanillaAnimations(Player player) {
      return this.disableVanillaAnimationsWhen != null && this.disableVanillaAnimationsWhen.test(player);
   }

   @Generated
   private static Function2<Player, Pose, Float> $default$eyeHeight() {
      return (player, pose) -> {
         return switch (pose) {
            case SWIMMING, FALL_FLYING, SPIN_ATTACK -> 0.4F;
            case CROUCHING -> 1.27F;
            default -> 1.62F;
         };
      };
   }

   @Generated
   private static boolean $default$renderLayers() {
      return true;
   }

   @Generated
   PlayerMorph(
      IGeometryContainer model,
      LivingAnimationSet<Player> animations,
      Material material,
      Predicate<Player> disableVanillaAnimationsWhen,
      Function2<Player, Pose, Float> eyeHeight,
      MorphSizeHandler entitySize,
      boolean renderLayers
   ) {
      this.model = model;
      this.animations = animations;
      this.material = material;
      this.disableVanillaAnimationsWhen = disableVanillaAnimationsWhen;
      this.eyeHeight = eyeHeight;
      this.entitySize = entitySize;
      this.renderLayers = renderLayers;
   }

   @Generated
   public static PlayerMorph.PlayerMorphBuilder builder() {
      return new PlayerMorph.PlayerMorphBuilder();
   }

   @Generated
   public IGeometryContainer getModel() {
      return this.model;
   }

   @Generated
   public LivingAnimationSet<Player> getAnimations() {
      return this.animations;
   }

   @Generated
   public Material getMaterial() {
      return this.material;
   }

   @Generated
   public Predicate<Player> getDisableVanillaAnimationsWhen() {
      return this.disableVanillaAnimationsWhen;
   }

   @Generated
   public Function2<Player, Pose, Float> getEyeHeight() {
      return this.eyeHeight;
   }

   @Generated
   public MorphSizeHandler getEntitySize() {
      return this.entitySize;
   }

   @Generated
   public boolean isRenderLayers() {
      return this.renderLayers;
   }

   public static class PlayerMorphBuilder {
      @Generated
      private IGeometryContainer model;
      @Generated
      private LivingAnimationSet<Player> animations;
      @Generated
      private Material material;
      @Generated
      private Predicate<Player> disableVanillaAnimationsWhen;
      @Generated
      private boolean eyeHeight$set;
      @Generated
      private Function2<Player, Pose, Float> eyeHeight$value;
      @Generated
      private MorphSizeHandler entitySize;
      @Generated
      private boolean renderLayers$set;
      @Generated
      private boolean renderLayers$value;

      public PlayerMorph.PlayerMorphBuilder disableVanillaAnimations() {
         return this.disableVanillaAnimationsWhen(Objects::nonNull);
      }

      public PlayerMorph.PlayerMorphBuilder withAnimations(UnaryOperator<LivingAnimationSet.LivingAnimationSetBuilder<Player>> builder) {
         return this.animations(builder.apply(LivingAnimationSet.builder()).build());
      }

      @Generated
      PlayerMorphBuilder() {
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder model(IGeometryContainer model) {
         this.model = model;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder animations(LivingAnimationSet<Player> animations) {
         this.animations = animations;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder material(Material material) {
         this.material = material;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder disableVanillaAnimationsWhen(Predicate<Player> disableVanillaAnimationsWhen) {
         this.disableVanillaAnimationsWhen = disableVanillaAnimationsWhen;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder eyeHeight(Function2<Player, Pose, Float> eyeHeight) {
         this.eyeHeight$value = eyeHeight;
         this.eyeHeight$set = true;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder entitySize(MorphSizeHandler entitySize) {
         this.entitySize = entitySize;
         return this;
      }

      @Generated
      public PlayerMorph.PlayerMorphBuilder renderLayers(boolean renderLayers) {
         this.renderLayers$value = renderLayers;
         this.renderLayers$set = true;
         return this;
      }

      @Generated
      public PlayerMorph build() {
         Function2<Player, Pose, Float> eyeHeight$value = this.eyeHeight$value;
         if (!this.eyeHeight$set) {
            eyeHeight$value = PlayerMorph.$default$eyeHeight();
         }

         boolean renderLayers$value = this.renderLayers$value;
         if (!this.renderLayers$set) {
            renderLayers$value = PlayerMorph.$default$renderLayers();
         }

         return new PlayerMorph(
            this.model, this.animations, this.material, this.disableVanillaAnimationsWhen, eyeHeight$value, this.entitySize, renderLayers$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "PlayerMorph.PlayerMorphBuilder(model="
            + this.model
            + ", animations="
            + this.animations
            + ", material="
            + this.material
            + ", disableVanillaAnimationsWhen="
            + this.disableVanillaAnimationsWhen
            + ", eyeHeight$value="
            + this.eyeHeight$value
            + ", entitySize="
            + this.entitySize
            + ", renderLayers$value="
            + this.renderLayers$value
            + ")";
      }
   }
}
