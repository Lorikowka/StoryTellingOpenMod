package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.RotateBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RandomGlitchBlock extends RotateBlock {
   public RandomGlitchBlock(Properties pProperties) {
      super(pProperties);
   }

   public void stepOn(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Entity pEntity) {
      super.stepOn(pLevel, pPos, pState, pEntity);
      if (!pLevel.isClientSide) {
         if (pEntity instanceof Player player) {
            this.randomTeleport(pLevel, pPos, player);
         }
      }
   }

   private void randomTeleport(Level level, BlockPos center, Player player) {
      RandomSource random = level.getRandom();
      int radius = 32;

      for (int attempt = 0; attempt < 30; attempt++) {
         int dx = random.nextInt(65) - 32;
         int dz = random.nextInt(65) - 32;
         int x = center.getX() + dx;
         int z = center.getZ() + dz;
         int y = level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, x, z);
         BlockPos targetPos = new BlockPos(x, y, z);
         if (level.getBlockState(targetPos).isAir() && level.getBlockState(targetPos.above()).isAir()) {
            Vec3 target = new Vec3((double)x + 0.5, (double)y, (double)z + 0.5);
            if (player instanceof ServerPlayer serverPlayer) {
               serverPlayer.teleportTo(target.x, target.y, target.z);
            } else {
               player.setPos(target);
            }

            return;
         }
      }
   }
}
