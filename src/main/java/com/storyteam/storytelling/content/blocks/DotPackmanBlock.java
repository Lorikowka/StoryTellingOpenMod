package com.storyteam.storytelling.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DotPackmanBlock extends Block {
   public DotPackmanBlock() {
      super(Properties.copy(Blocks.STONE).noOcclusion().noCollission().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return Block.box(5.25, 0.0, 5.25, 10.75, 5.6, 10.75);
   }

   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (!level.isClientSide) {
         Vec3 entityCenter = entity.position();
         Vec3 blockCenter = Vec3.atCenterOf(pos);
         double tolerance = 0.8;
         boolean isPlayer = entity instanceof Player;
         boolean colliding = Math.abs(entityCenter.x - blockCenter.x) < tolerance && Math.abs(entityCenter.z - blockCenter.z) < tolerance;
         if (colliding && (!isPlayer || !((Player)entity).isCreative())) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         }

         super.entityInside(state, level, pos, entity);
      }
   }
}
