package com.storyteam.storytelling.content.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.tile.IAnimatedTile;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class BaseAnimatedTile<T extends TileSyncableTickable> extends TileSyncableTickable implements IAnimatedTile {
   public final AnimationSystem animations = AnimationSystem.create(this);

   public BaseAnimatedTile(BlockEntityType<T> tile, BlockPos pos, BlockState state) {
      super(tile, pos, state);
   }

   public void update() {
      assert this.level != null;

      this.animations.tick();
   }

   public void startAnimation(IAnimationSource animationSource) {
      this.animations.startAnimationAt("action", animationSource);
   }

   public void startAnimation(ConfiguredAnimation configuredAnimation) {
      this.animations.startAnimationAt("action", configuredAnimation);
   }

   public void stopAnimation() {
      this.animations.stopAnimation("action");
   }

   public IPacket createSyncPacket() {
      return this.animations.createSyncPacket();
   }

   public static void startAnimation(Level level, BlockPos pos, IAnimationSource animationSource) {
      if (level.getBlockEntity(pos) instanceof BaseAnimatedTile tile) {
         tile.startAnimation(animationSource);
      }
   }

   public static void startAnimation(Level level, BlockPos pos, ConfiguredAnimation configuredAnimation) {
      if (level.getBlockEntity(pos) instanceof BaseAnimatedTile tile) {
         tile.startAnimation(configuredAnimation);
      }
   }

   public static void stopAnimation(Level level, BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof BaseAnimatedTile tile) {
         tile.stopAnimation();
      }
   }

   public static IPacket createSyncPacket(Level level, BlockPos pos) {
      return level.getBlockEntity(pos) instanceof BaseAnimatedTile tile ? tile.createSyncPacket() : null;
   }

   public void setupSystem(Builder builder) {
      builder.addLayers(new org.zeith.hammeranims.api.animsys.layer.AnimationLayer.Builder[]{AnimationLayer.builder("action")}).disableSync();
   }

   public AnimationSystem getAnimationSystem() {
      return this.animations;
   }
}
