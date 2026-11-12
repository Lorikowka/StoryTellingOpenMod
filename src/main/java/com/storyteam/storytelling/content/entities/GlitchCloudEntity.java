package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GlitchCloudEntity extends AnimatedEntity {
   private static final Random random = new Random();
   private float width = 2.0F;
   private float depth = 5.0F;
   private float height = 2.5F;
   private int density = 25;
   private final Color cloudColor = new Color(143, 143, 143);
   private final float alpha = 0.9F;
   private boolean raining = true;
   private final int rainRate = 3;
   private boolean radialBuffMode = false;
   private double buffRadius = 12.0;
   private final List<GlitchCloudEntity.ColorChance> dropColors = List.of(
      new GlitchCloudEntity.ColorChance(new Color(255, 100, 200), 90.0F),
      new GlitchCloudEntity.ColorChance(new Color(100, 150, 255), 3.0F),
      new GlitchCloudEntity.ColorChance(new Color(255, 60, 60), 3.0F),
      new GlitchCloudEntity.ColorChance(new Color(255, 220, 50), 3.0F)
   );

   public GlitchCloudEntity(EntityType<? extends AnimatedEntity> type, Level level) {
      super(type, level);
      this.noPhysics = false;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         this.spawnCloudParticles();
         if (this.raining) {
            this.spawnRainParticles();
         }
      }

      int buffInterval = this.radialBuffMode ? 60 : 20;
      if (!this.level().isClientSide() && this.raining && this.tickCount % buffInterval == 0) {
         this.applyBuffToPlayers();
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.raining = tag.getBoolean("Raining");
      this.radialBuffMode = tag.getBoolean("RadialBuffMode");
      if (tag.contains("BuffRadius")) {
         this.buffRadius = tag.getDouble("BuffRadius");
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putBoolean("Raining", this.raining);
      tag.putBoolean("RadialBuffMode", this.radialBuffMode);
      tag.putDouble("BuffRadius", this.buffRadius);
   }

   private void spawnCloudParticles() {
      Vec3 center = this.position();

      for (int i = 0; i < this.density; i++) {
         double x;
         double y;
         double z;
         do {
            x = (random.nextDouble() * 2.0 - 1.0) * (double)this.width / 2.0;
            y = (random.nextDouble() * 2.0 - 1.0) * (double)this.height / 2.0;
            z = (random.nextDouble() * 2.0 - 1.0) * (double)this.depth / 2.0;
         } while (
            x * x / Math.pow((double)this.width / 2.0, 2.0)
                  + y * y / Math.pow((double)this.height / 2.0, 2.0)
                  + z * z / Math.pow((double)this.depth / 2.0, 2.0)
               > 1.0
         );

         Vec3 pos = center.add(x, y, z);
         double motionX = (random.nextDouble() - 0.5) * 0.005;
         double motionY = (random.nextDouble() - 0.5) * 0.002;
         double motionZ = (random.nextDouble() - 0.5) * 0.005;
         ParticleHelper.spawnEnginedParticle(
            this.level(),
            ParticleTypes.CAMPFIRE_COSY_SMOKE,
            pos,
            motionX,
            motionY,
            motionZ,
            1.5F + random.nextFloat() * 0.5F,
            10 + random.nextInt(10),
            this.cloudColor,
            0.9F
         );
      }

      if (this.tickCount % 8 == 0) {
         for (int i = 0; i < 3; i++) {
            Vec3 pos = center.add(
               (random.nextDouble() - 0.5) * (double)this.width,
               (random.nextDouble() - 0.5) * (double)this.height,
               (random.nextDouble() - 0.5) * (double)this.depth
            );
            ParticleHelper.spawnEnginedParticle(
               this.level(),
               ParticleTypesST.GLITCH,
               pos,
               0.0,
               0.003,
               0.0,
               1.0F + random.nextFloat() * 0.4F,
               20 + random.nextInt(20),
               new Color(150, 200, 255),
               0.9F
            );
         }
      }
   }

   private void spawnRainParticles() {
      if (this.tickCount % 3 == 0) {
         Vec3 center = this.position();

         for (int i = 0; i < 6; i++) {
            double x = (random.nextDouble() - 0.5) * (double)this.width;
            double z = (random.nextDouble() - 0.5) * (double)this.depth;
            Vec3 pos = center.add(x, (double)(-this.height / 2.0F) - 0.1, z);
            Color dropColor = this.chooseDropColor();
            ParticleHelper.spawnEnginedParticle(
               this.level(), ParticleTypesST.COLORED_DROP, pos, 0.0, -0.05, 0.0, 0.7F + random.nextFloat() * 0.3F, 30 + random.nextInt(10), dropColor, 0.95F
            );
         }
      }
   }

   private void applyBuffToPlayers() {
      Vec3 center = this.position();
      double effectiveRadius = this.radialBuffMode ? this.buffRadius : (double)(Math.max(this.width, this.depth) / 2.0F) + 1.5;
      double yRange = (double)this.height + 4.0;
      AABB area = new AABB(
         center.x - effectiveRadius,
         center.y - yRange,
         center.z - effectiveRadius,
         center.x + effectiveRadius,
         center.y + (this.radialBuffMode ? yRange : 0.0),
         center.z + effectiveRadius
      );

      for (Player player : this.level().getEntitiesOfClass(Player.class, area)) {
         if (!player.isSpectator()) {
            if (!this.radialBuffMode) {
               if (player.position().y > center.y - (double)(this.height / 2.0F)) {
                  continue;
               }

               double dx = player.getX() - center.x;
               double dz = player.getZ() - center.z;
               if (Math.sqrt(dx * dx + dz * dz) > (double)(Math.max(this.width, this.depth) / 2.0F) + 1.5) {
                  continue;
               }
            }

            CurrencyStorage.addEidos(1);
         }
      }
   }

   private Color chooseDropColor() {
      float total = 0.0F;

      for (GlitchCloudEntity.ColorChance c : this.dropColors) {
         total += c.chance;
      }

      float r = random.nextFloat() * total;
      float cumulative = 0.0F;

      for (GlitchCloudEntity.ColorChance c : this.dropColors) {
         cumulative += c.chance;
         if (r <= cumulative) {
            return c.color;
         }
      }

      return this.dropColors.get(this.dropColors.size() - 1).color;
   }

   @NotNull
   public EntityDimensions getDimensions(@NotNull Pose pose) {
      return EntityDimensions.fixed(this.width, this.height);
   }

   @Generated
   public float getWidth() {
      return this.width;
   }

   @Generated
   public float getDepth() {
      return this.depth;
   }

   @Generated
   public float getHeight() {
      return this.height;
   }

   @Generated
   public int getDensity() {
      return this.density;
   }

   @Generated
   public Color getCloudColor() {
      return this.cloudColor;
   }

   @Generated
   public float getAlpha() {
      return 0.9F;
   }

   @Generated
   public boolean isRaining() {
      return this.raining;
   }

   @Generated
   public int getRainRate() {
      return 3;
   }

   @Generated
   public boolean isRadialBuffMode() {
      return this.radialBuffMode;
   }

   @Generated
   public double getBuffRadius() {
      return this.buffRadius;
   }

   @Generated
   public List<GlitchCloudEntity.ColorChance> getDropColors() {
      return this.dropColors;
   }

   @Generated
   public void setWidth(float width) {
      this.width = width;
   }

   @Generated
   public void setDepth(float depth) {
      this.depth = depth;
   }

   @Generated
   public void setHeight(float height) {
      this.height = height;
   }

   @Generated
   public void setDensity(int density) {
      this.density = density;
   }

   @Generated
   public void setRaining(boolean raining) {
      this.raining = raining;
   }

   @Generated
   public void setRadialBuffMode(boolean radialBuffMode) {
      this.radialBuffMode = radialBuffMode;
   }

   @Generated
   public void setBuffRadius(double buffRadius) {
      this.buffRadius = buffRadius;
   }

   private static record ColorChance(Color color, float chance) {
   }
}
