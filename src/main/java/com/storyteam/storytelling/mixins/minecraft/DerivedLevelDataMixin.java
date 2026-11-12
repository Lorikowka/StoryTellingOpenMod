package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({DerivedLevelData.class})
public class DerivedLevelDataMixin {
   @Unique
   private int st$clearWeatherTime = 0;
   @Unique
   private boolean st$raining = false;
   @Unique
   private int st$rainTime = 0;
   @Unique
   private boolean st$thundering = false;
   @Unique
   private int st$thunderTime = 0;
   @Final
   @Shadow
   private ServerLevelData wrapped;

   @Overwrite
   public int getClearWeatherTime() {
      return this.st$clearWeatherTime;
   }

   @Overwrite
   public void setClearWeatherTime(int pTime) {
      this.st$clearWeatherTime = pTime;
   }

   @Overwrite
   public boolean isThundering() {
      return this.st$thundering;
   }

   @Overwrite
   public int getThunderTime() {
      return this.st$thunderTime;
   }

   @Overwrite
   public boolean isRaining() {
      return this.st$raining;
   }

   @Overwrite
   public int getRainTime() {
      return this.st$rainTime;
   }

   @Overwrite
   public void setThundering(boolean pThundering) {
      this.st$thundering = pThundering;
   }

   @Overwrite
   public void setThunderTime(int pTime) {
      this.st$thunderTime = pTime;
   }

   @Overwrite
   public void setRaining(boolean pIsRaining) {
      this.st$raining = pIsRaining;
   }

   @Overwrite
   public void setRainTime(int pTime) {
      this.st$rainTime = pTime;
   }
}
