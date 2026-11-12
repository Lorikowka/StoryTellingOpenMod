package com.storyteam.storytelling.mixins.system._core.morph;

import com.google.common.base.MoreObjects;
import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.init.BindsST;
import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.init.system.MorphsST;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.MorphSystem;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import com.storyteam.storytelling.system._core.morph.data.PlayerPoseMap;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

@Mixin({Player.class})
public abstract class PlayerMixin extends LivingEntity implements IAnimatedPlayer {
   @Unique
   private static final EntityDataAccessor<String> ST$DATA_MORPH = SynchedEntityData.defineId(Player.class, EntityDataSerializers.STRING);
   @Unique
   private static final EntityDataAccessor<Boolean> ST$DATA_IS_FROZEN = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
   @Unique
   protected AnimationSystem st$animations = null;
   @Unique
   protected PlayerMorph st$trackedTf = null;
   @Unique
   protected final KeyMap st$keyMap = KeyMap.createHash();

   @Shadow
   public abstract boolean isCreative();

   @Shadow
   public abstract boolean isSpectator();

   protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Inject(
      method = {"defineSynchedData"},
      at = {@At("TAIL")}
   )
   protected void ST_defineSynchedData(CallbackInfo ci) {
      this.st$trackedTf = MorphsST.NONE;
      this.entityData.define(ST$DATA_MORPH, "");
      this.entityData.define(ST$DATA_IS_FROZEN, false);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   protected void ST_tick(CallbackInfo ci) {
      String tfs = (String)this.entityData.get(ST$DATA_MORPH);
      PlayerMorph tf = tfs != null && !tfs.isBlank() ? (PlayerMorph)MorphSystem.playerMorphs().getValue(Resources.location(tfs)) : MorphsST.NONE;
      if (tf != this.st$trackedTf) {
         this.st$trackedTf = tf;
         this.st$animations = AnimationSystem.create(this);
         this.refreshDimensions();
      }

      if (this.st$trackedTf != null) {
         this.st$trackedTf.update((Player)Cast.cast(this), this.st$animations);
      }

      this.getAnimationSystem().tick();
   }

   public void setupSystem(Builder builder) {
      builder.disableSync();
      if (this.st$trackedTf != null) {
         this.st$trackedTf.setupPlayerAnimationSystem(builder);
      }
   }

   public AnimationSystem getAnimationSystem() {
      if (this.st$animations == null) {
         this.st$animations = AnimationSystem.create(this);
      }

      return this.st$animations;
   }

   @Override
   public KeyMap getUnsavedProperties() {
      return this.st$keyMap;
   }

   @Inject(
      method = {"getDimensions"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void st$getDimensions(Pose pPose, CallbackInfoReturnable<EntityDimensions> cir) {
      PlayerPoseMap poses = this.st$keyMap != null ? (PlayerPoseMap)this.st$keyMap.get(PlayerPoseMap.POSES) : null;
      if (poses != null) {
         EntityDimensions dim = poses.getPose(pPose);
         if (dim != null) {
            cir.setReturnValue(dim);
         }
      }
   }

   @Override
   public void morphInto(PlayerMorph transformation) {
      if (transformation == null) {
         transformation = MorphsST.NONE;
      }

      this.entityData.set(ST$DATA_MORPH, Objects.toString(MorphSystem.playerMorphs().getKey(transformation)));
      this.refreshDimensions();
   }

   @Override
   public PlayerMorph getMorph() {
      return (PlayerMorph)MoreObjects.firstNonNull(this.st$trackedTf, MorphsST.NONE);
   }

   @Override
   public boolean isPlayerMotionBlocked() {
      return this.getMorph().equals(MorphsST8.HEAD) && !this.isCreative() && !this.isSpectator()
         || this.getUnsavedProperties().opt(AnimationWithCondition.DEAD).orElse(false)
         || (Boolean)this.entityData.get(ST$DATA_IS_FROZEN);
   }

   @Override
   public void blockPlayerMotion(boolean blocked) {
      this.entityData.set(ST$DATA_IS_FROZEN, blocked);
   }

   @Override
   public boolean isPlayerJumping() {
      return this.jumping;
   }

   @Inject(
      method = {"addAdditionalSaveData"},
      at = {@At("HEAD")}
   )
   private void ST$saveMorph(CompoundTag pCompound, CallbackInfo ci) {
      pCompound.putString("StorytellingMorph", Objects.toString(MorphSystem.playerMorphs().getKey(this.getMorph())));
   }

   @Inject(
      method = {"readAdditionalSaveData"},
      at = {@At("HEAD")}
   )
   private void ST$readMorph(CompoundTag pCompound, CallbackInfo ci) {
      if (pCompound.contains("StorytellingMorph", 8)) {
         this.entityData.set(ST$DATA_MORPH, pCompound.getString("StorytellingMorph"));
      }
   }

   @Inject(
      method = {"getStandingEyeHeight"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ST$modifyEyeHeight(Pose pPose, EntityDimensions pSize, CallbackInfoReturnable<Float> cir) {
      PlayerMorph m = this.getMorph();
      if (m != null) {
         Float ret = (Float)m.getEyeHeight().apply((Player)Cast.cast(this), pPose);
         if (ret == null) {
            return;
         }

         cir.setReturnValue(ret);
      }
   }

   @Inject(
      method = {"getSpeed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSpeed(CallbackInfoReturnable<Float> cir) {
      Player deez = (Player)(Object)this;
      float speed = Optional.ofNullable(BindsST.overrideSpeed).orElse((float)deez.getAttributeValue(Attributes.MOVEMENT_SPEED));
      if (((IAnimatedPlayer)deez).getMorph().equals(MorphsST8.SPOODER) && !deez.isCreative() && !deez.isSpectator()) {
         speed *= deez.isSprinting() ? 0.7F : 0.5F;
      }

      cir.setReturnValue(speed);
   }

   @Inject(
      method = {"isSwimming"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isSwimming(CallbackInfoReturnable<Boolean> cir) {
      if (this.getMorph().equals(MorphsST8.SPOODER)) {
         cir.setReturnValue(false);
      }
   }
}
