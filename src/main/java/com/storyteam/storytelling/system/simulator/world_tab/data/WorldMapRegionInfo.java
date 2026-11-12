package com.storyteam.storytelling.system.simulator.world_tab.data;

import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WorldMapRegionInfo {
   private Component name;
   private String description;
   private ResourceLocation image;
   private List<WMRButton> buttons;
   private Location teleportLocation;

   public WorldMapRegionInfo(Component name, String description, ResourceLocation image, List<WMRButton> buttons, Location teleportLocation) {
      this.name = name;
      this.description = description;
      this.image = image;
      this.buttons = buttons.stream().map(WMRButton::copy).toList();
      this.teleportLocation = teleportLocation;
   }

   @Generated
   public static WorldMapRegionInfo.WorldMapRegionInfoBuilder builder() {
      return new WorldMapRegionInfo.WorldMapRegionInfoBuilder();
   }

   @Generated
   public Component getName() {
      return this.name;
   }

   @Generated
   public String getDescription() {
      return this.description;
   }

   @Generated
   public ResourceLocation getImage() {
      return this.image;
   }

   @Generated
   public List<WMRButton> getButtons() {
      return this.buttons;
   }

   @Generated
   public Location getTeleportLocation() {
      return this.teleportLocation;
   }

   @Generated
   public void setButtons(List<WMRButton> buttons) {
      this.buttons = buttons;
   }

   @Generated
   public static class WorldMapRegionInfoBuilder {
      @Generated
      private Component name;
      @Generated
      private String description;
      @Generated
      private ResourceLocation image;
      @Generated
      private List<WMRButton> buttons;
      @Generated
      private Location teleportLocation;

      @Generated
      WorldMapRegionInfoBuilder() {
      }

      @Generated
      public WorldMapRegionInfo.WorldMapRegionInfoBuilder name(Component name) {
         this.name = name;
         return this;
      }

      @Generated
      public WorldMapRegionInfo.WorldMapRegionInfoBuilder description(String description) {
         this.description = description;
         return this;
      }

      @Generated
      public WorldMapRegionInfo.WorldMapRegionInfoBuilder image(ResourceLocation image) {
         this.image = image;
         return this;
      }

      @Generated
      public WorldMapRegionInfo.WorldMapRegionInfoBuilder buttons(List<WMRButton> buttons) {
         this.buttons = buttons;
         return this;
      }

      @Generated
      public WorldMapRegionInfo.WorldMapRegionInfoBuilder teleportLocation(Location teleportLocation) {
         this.teleportLocation = teleportLocation;
         return this;
      }

      @Generated
      public WorldMapRegionInfo build() {
         return new WorldMapRegionInfo(this.name, this.description, this.image, this.buttons, this.teleportLocation);
      }

      @Generated
      @Override
      public String toString() {
         return "WorldMapRegionInfo.WorldMapRegionInfoBuilder(name="
            + this.name
            + ", description="
            + this.description
            + ", image="
            + this.image
            + ", buttons="
            + this.buttons
            + ", teleportLocation="
            + this.teleportLocation
            + ")";
      }
   }
}
