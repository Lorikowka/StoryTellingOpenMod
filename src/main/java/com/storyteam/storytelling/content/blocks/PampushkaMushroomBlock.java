package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.blocks.base.RotateBlockEntityWithShape;
import com.storyteam.storytelling.content.items.base.CommonCustomBlockItem;
import com.storyteam.storytelling.content.tiles.PampushkaMushroomTile;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;
import org.zeith.hammerlib.net.Network;

public class PampushkaMushroomBlock extends RotateBlockEntityWithShape<PampushkaMushroomTile> implements ICustomBlockItem {
   public static final ResourceLocation COLORED_TEXTURE = St.tx("tile/pampushka_mushroom");
   public static final ResourceLocation GRAY_TEXTURE = St.tx("tile/pampushka_mushroom_gray");
   private final PampushkaMushroomBlock.BlockType type;

   public PampushkaMushroomBlock(Properties pProperties, PampushkaMushroomBlock.BlockType type) {
      super(makeShape(), pProperties, true, () -> TilesST.PAMPUSHKA_MUSHROOM);
      this.type = type;
   }

   public BlockItem createBlockItem() {
      return (BlockItem)CreativeTabAdapter.bindTab(
         new CommonCustomBlockItem(
            this,
            BlockEntityModels.PAMPUSHKA_MUSHROOM,
            this.type == PampushkaMushroomBlock.BlockType.COLORED ? COLORED_TEXTURE : GRAY_TEXTURE,
            0.45F,
            new Vec3(0.0, -0.5, 0.0),
            true
         ),
         new CreativeTab[]{TabsST.BLOCKITEMS}
      );
   }

   public void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float pFallDistance) {
      super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
      if (!pLevel.isClientSide) {
         PampushkaMushroomTile.startAnimation(pLevel, pPos, AnimationsST8.PAMPUSHKA_MUSHROOM_JUMP.configure().important());
         Network.sendToAll(PampushkaMushroomTile.createSyncPacket(pLevel, pPos));
         this.spawnParticlesAndSound(pLevel, pPos);
      }
   }

   @NotNull
   public RenderShape getRenderShape(@NotNull BlockState pState) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   private void spawnParticlesAndSound(Level level, BlockPos pos) {
      RandomSource random = level.getRandom();

      for (int i = 0; i < 30; i++) {
         double x = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5);
         double y = (double)pos.getY() + 0.7 + random.nextDouble() * 0.6;
         double z = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5);
         double moveX = (random.nextDouble() - 0.5) * 0.02;
         double moveY = random.nextDouble() * 0.05 + 0.05;
         double moveZ = (random.nextDouble() - 0.5) * 0.02;
         float r = 0.5F + random.nextFloat() * 0.5F;
         float g = 0.5F + random.nextFloat() * 0.5F;
         float b = 0.5F + random.nextFloat() * 0.5F;
         Color color = new Color(r, g, b);
         float alpha = 0.8F + random.nextFloat() * 0.2F;
         float scale = 1.0F + random.nextFloat() * 0.8F;
         int lifetime = 20 + random.nextInt(15);
         ParticleHelper.spawnEnginedParticle(
            level, new DustParticleOptions(new Vector3f(r, g, b), 1.0F), new Vec3(x, y, z), moveX, moveY, moveZ, scale, lifetime, color, alpha
         );
      }

      SoundEvent[] sounds = new SoundEvent[]{
         SoundEvents.SLIME_BLOCK_PLACE,
         SoundEvents.HONEY_BLOCK_SLIDE,
         SoundEvents.SLIME_BLOCK_STEP,
         SoundEvents.HONEY_BLOCK_STEP,
         SoundEvents.BUBBLE_COLUMN_BUBBLE_POP
      };
      SoundEvent sound = sounds[random.nextInt(sounds.length)];
      SoundUtils.playVolumedSound(level, pos, sound);
   }

   private static VoxelShape makeShape() {
      return Shapes.join(Block.box(4.5, 0.0, 4.5, 11.5, 8.0, 11.5), Block.box(1.0, 7.7, 1.0, 15.0, 12.25, 15.0), BooleanOp.OR);
   }

   @Generated
   public PampushkaMushroomBlock.BlockType getType() {
      return this.type;
   }

   public static enum BlockType {
      COLORED,
      GRAY;
   }
}
