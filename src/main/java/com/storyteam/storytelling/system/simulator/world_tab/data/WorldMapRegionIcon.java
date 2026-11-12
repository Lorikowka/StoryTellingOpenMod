package com.storyteam.storytelling.system.simulator.world_tab.data;

import lombok.Generated;
import net.minecraft.resources.ResourceLocation;

public record WorldMapRegionIcon(ResourceLocation iconTexture, int XPos, int YPos, int width, int height) {
   @Generated
   public static WorldMapRegionIcon.WorldMapRegionIconBuilder builder() {
      return new WorldMapRegionIcon.WorldMapRegionIconBuilder();
   }

   @Generated
   public static class WorldMapRegionIconBuilder {
      @Generated
      private ResourceLocation iconTexture;
      @Generated
      private int XPos;
      @Generated
      private int YPos;
      @Generated
      private int width;
      @Generated
      private int height;

      @Generated
      WorldMapRegionIconBuilder() {
      }

      @Generated
      public WorldMapRegionIcon.WorldMapRegionIconBuilder iconTexture(ResourceLocation iconTexture) {
         this.iconTexture = iconTexture;
         return this;
      }

      @Generated
      public WorldMapRegionIcon.WorldMapRegionIconBuilder XPos(int XPos) {
         this.XPos = XPos;
         return this;
      }

      @Generated
      public WorldMapRegionIcon.WorldMapRegionIconBuilder YPos(int YPos) {
         this.YPos = YPos;
         return this;
      }

      @Generated
      public WorldMapRegionIcon.WorldMapRegionIconBuilder width(int width) {
         this.width = width;
         return this;
      }

      @Generated
      public WorldMapRegionIcon.WorldMapRegionIconBuilder height(int height) {
         this.height = height;
         return this;
      }

      @Generated
      public WorldMapRegionIcon build() {
         return new WorldMapRegionIcon(this.iconTexture, this.XPos, this.YPos, this.width, this.height);
      }

      @Generated
      @Override
      public String toString() {
         return "WorldMapRegionIcon.WorldMapRegionIconBuilder(iconTexture="
            + this.iconTexture
            + ", XPos="
            + this.XPos
            + ", YPos="
            + this.YPos
            + ", width="
            + this.width
            + ", height="
            + this.height
            + ")";
      }
   }
}
