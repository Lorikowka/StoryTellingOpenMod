package com.storyteam.storytelling.content.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.api.tile.IAnimatedTile;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class TileSimpleAnimated extends TileSyncableTickable implements IAnimatedTile {
   @NBTSerializable
   protected final AnimationSystem animations = AnimationSystem.create(this);

   public TileSimpleAnimated(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public void update() {
      super.update();
      this.animations.tick();
   }

   public void setupSystem(Builder builder) {
      builder.addLayers(new String[]{"action"});
   }

   public AnimationSystem getAnimationSystem() {
      return this.animations;
   }
}
