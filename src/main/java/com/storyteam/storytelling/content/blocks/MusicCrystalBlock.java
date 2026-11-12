package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.RotateBlockWithCustomShape;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class MusicCrystalBlock extends RotateBlockWithCustomShape {
   private static final SoundEvent[] NOTE_SOUNDS = new SoundEvent[]{
      (SoundEvent)SoundEvents.NOTE_BLOCK_HARP.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_BELL.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_FLUTE.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_PLING.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_XYLOPHONE.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_COW_BELL.get(),
      (SoundEvent)SoundEvents.NOTE_BLOCK_BIT.get()
   };

   public MusicCrystalBlock(Properties pProperties) {
      super(pProperties, makeShape());
   }

   @NotNull
   public InteractionResult use(
      @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit
   ) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         RandomSource random = level.getRandom();
         SoundEvent sound = NOTE_SOUNDS[random.nextInt(NOTE_SOUNDS.length)];
         int note = random.nextInt(25);
         float pitch = (float)Math.pow(2.0, (double)(note - 12) / 12.0);
         level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
         return InteractionResult.SUCCESS;
      }
   }

   private static VoxelShape makeShape() {
      return Block.box(4.0, 0.0, 6.0, 10.0, 14.0, 12.0);
   }
}
