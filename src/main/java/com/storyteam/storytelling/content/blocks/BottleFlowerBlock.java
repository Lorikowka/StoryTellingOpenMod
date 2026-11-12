package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.blocks.base.RotateBlockEntityWithShape;
import com.storyteam.storytelling.content.items.base.CommonCustomBlockItem;
import com.storyteam.storytelling.content.tiles.BottleFlowerTile;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import java.util.Map;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

public class BottleFlowerBlock extends RotateBlockEntityWithShape<BottleFlowerTile> implements ICustomBlockItem {
   public static String EMISSIVE_TEXTURE_PATH = "tile/bottleflower/emissive/bottleflower_%s_emissive";
   public static String BASE_COLORED_TEXTURE_PATH = "tile/bottleflower/bottleflower_base";
   public static String BASE_GRAY_TEXTURE_PATH = "tile/bottleflower/bottleflower_base_gray";
   private final BottleFlowerBlock.FlowerColor color;
   private final BottleFlowerBlock.FlowerType type;
   private static final Map<BottleFlowerBlock.FlowerColor, MobEffectInstance> COLOR_EFFECTS = Map.of(
      BottleFlowerBlock.FlowerColor.RED,
      new MobEffectInstance(MobEffects.HEAL, 1, 0),
      BottleFlowerBlock.FlowerColor.YELLOW,
      new MobEffectInstance(MobEffects.DIG_SPEED, 200, 0),
      BottleFlowerBlock.FlowerColor.GREEN,
      new MobEffectInstance(MobEffects.POISON, 200, 0),
      BottleFlowerBlock.FlowerColor.BLACK,
      new MobEffectInstance(MobEffects.WITHER, 200, 0),
      BottleFlowerBlock.FlowerColor.BLUE,
      new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0),
      BottleFlowerBlock.FlowerColor.VIOLET,
      new MobEffectInstance(MobEffects.HARM, 1, 0),
      BottleFlowerBlock.FlowerColor.CYAN,
      new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0)
   );
   private static final Map<BottleFlowerBlock.FlowerColor, Integer> EFFECT_COOLDOWNS = Map.of(
      BottleFlowerBlock.FlowerColor.RED,
      12000,
      BottleFlowerBlock.FlowerColor.YELLOW,
      12000,
      BottleFlowerBlock.FlowerColor.BLUE,
      12000,
      BottleFlowerBlock.FlowerColor.CYAN,
      12000,
      BottleFlowerBlock.FlowerColor.GREEN,
      1200,
      BottleFlowerBlock.FlowerColor.BLACK,
      1200,
      BottleFlowerBlock.FlowerColor.VIOLET,
      1200
   );

   public BottleFlowerBlock(BottleFlowerBlock.FlowerColor color) {
      this(Properties.copy(Blocks.GRASS), color, BottleFlowerBlock.FlowerType.COLORED);
   }

   public BottleFlowerBlock(BottleFlowerBlock.FlowerColor color, BottleFlowerBlock.FlowerType type) {
      this(Properties.copy(Blocks.GRASS), color, type);
   }

   public BottleFlowerBlock(Properties pProperties, BottleFlowerBlock.FlowerColor color, BottleFlowerBlock.FlowerType type) {
      super(makeShape(), pProperties, true, () -> TilesST.BOTTLEFLOWER);
      this.color = color;
      this.type = type;
   }

   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      if (entity instanceof LivingEntity living) {
         MobEffectInstance effect = COLOR_EFFECTS.get(this.color);
         int cooldown = EFFECT_COOLDOWNS.get(this.color);
         if (effect != null) {
            if (!level.isClientSide) {
               String key = "flower_cooldown_" + this.color.name();
               CompoundTag persistent = living.getPersistentData();
               long last = persistent.getLong(key);
               long now = level.getGameTime();
               if (now - last >= (long)cooldown) {
                  living.addEffect(new MobEffectInstance(effect));
                  persistent.putLong(key, now);
                  Vec3 spawnPos = new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                  this.spawnPollenParticles(level, spawnPos.add(0.0, -0.3F, 0.0), this.color);
               }
            }
         }
      }
   }

   private void spawnPollenParticles(Level level, Vec3 pos, BottleFlowerBlock.FlowerColor color) {
      RandomSource random = level.getRandom();
      Vector3f baseColor = this.getColorVector(color);

      for (int i = 0; i < 30; i++) {
         double x = pos.x() + 0.5 + (random.nextDouble() - 0.5);
         double y = pos.y() + 0.7 + random.nextDouble() * 0.6;
         double z = pos.z() + 0.5 + (random.nextDouble() - 0.5);
         double moveX = (random.nextDouble() - 0.5) * 0.02;
         double moveY = random.nextDouble() * 0.05 + 0.03;
         double moveZ = (random.nextDouble() - 0.5) * 0.02;
         float r = clamp(baseColor.x() + (random.nextFloat() - 0.5F) * 0.3F, 0.0F, 1.0F);
         float g = clamp(baseColor.y() + (random.nextFloat() - 0.5F) * 0.3F, 0.0F, 1.0F);
         float b = clamp(baseColor.z() + (random.nextFloat() - 0.5F) * 0.3F, 0.0F, 1.0F);
         float alpha = 0.7F + random.nextFloat() * 0.3F;
         float scale = 0.7F + random.nextFloat() * 0.8F;
         int lifetime = 25 + random.nextInt(20);
         Color particleColor = new Color(r, g, b);
         ParticleHelper.spawnEnginedParticle(
            level, new DustParticleOptions(new Vector3f(r, g, b), 1.0F), new Vec3(x, y, z), moveX, moveY, moveZ, scale, lifetime, particleColor, alpha
         );
      }

      if (random.nextFloat() < 0.2F) {
         SoundEvent sound = SoundEvents.GRASS_STEP;
         SoundUtils.playVolumedSound(level, new BlockPos((int)pos.x(), (int)pos.y(), (int)pos.z()), sound);
      }
   }

   private static float clamp(float val, float min, float max) {
      return Math.max(min, Math.min(max, val));
   }

   private Vector3f getColorVector(BottleFlowerBlock.FlowerColor color) {
      return switch (color) {
         case RED -> new Vector3f(1.0F, 0.2F, 0.2F);
         case YELLOW -> new Vector3f(1.0F, 1.0F, 0.3F);
         case GREEN -> new Vector3f(0.2F, 1.0F, 0.2F);
         case BLACK -> new Vector3f(0.1F, 0.1F, 0.1F);
         case BLUE -> new Vector3f(0.3F, 0.7F, 1.0F);
         case CYAN -> new Vector3f(0.5F, 1.0F, 1.0F);
         case VIOLET -> new Vector3f(0.7F, 0.3F, 1.0F);
         case DEFAULT -> new Vector3f(0.0F, 0.0F, 0.0F);
      };
   }

   public BlockItem createBlockItem() {
      return (BlockItem)CreativeTabAdapter.bindTab(
         new CommonCustomBlockItem(this, BlockEntityModels.BOTTLEFLOWER, getBaseTexture(this.type), 0.45F, new Vec3(0.0, -0.5, 0.0), true),
         new CreativeTab[]{TabsST.BLOCKITEMS}
      );
   }

   private static VoxelShape makeShape() {
      return Shapes.join(Block.box(6.0, 0.0, 7.0, 10.0, 10.0, 9.0), Block.box(4.0, 9.0, 4.0, 12.0, 11.0, 12.0), BooleanOp.OR);
   }

   public static BottleFlowerBlock.FlowerColor getColor(BlockState blockState) {
      return blockState.getBlock() instanceof BottleFlowerBlock bottleFlowerBlock ? bottleFlowerBlock.getColor() : BottleFlowerBlock.FlowerColor.DEFAULT;
   }

   public static BottleFlowerBlock.FlowerType getType(BlockState blockState) {
      return blockState.getBlock() instanceof BottleFlowerBlock bottleFlowerBlock ? bottleFlowerBlock.getType() : BottleFlowerBlock.FlowerType.COLORED;
   }

   public static ResourceLocation getEmissiveTexture(BottleFlowerBlock.FlowerColor color) {
      return St.tx(String.format(EMISSIVE_TEXTURE_PATH, color.name().toLowerCase()));
   }

   public static ResourceLocation getBaseTexture(BottleFlowerBlock.FlowerType type) {
      return type == BottleFlowerBlock.FlowerType.COLORED ? St.tx(BASE_COLORED_TEXTURE_PATH) : St.tx(BASE_GRAY_TEXTURE_PATH);
   }

   @Generated
   public BottleFlowerBlock.FlowerColor getColor() {
      return this.color;
   }

   @Generated
   public BottleFlowerBlock.FlowerType getType() {
      return this.type;
   }

   public static enum FlowerColor {
      BLACK,
      BLUE,
      CYAN,
      GREEN,
      RED,
      VIOLET,
      YELLOW,
      DEFAULT;
   }

   public static enum FlowerType {
      COLORED,
      GRAY;
   }
}
