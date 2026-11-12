package com.storyteam.storytelling.system.simulator.world_tab.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class WorldMapRegion implements ICustomRegistrar, IAutoNBTSerializable {
   private ResourceLocation id;
   private final ResourceLocation texture;
   private final int width;
   private final int height;
   private final int XPos;
   private final int YPos;
   @NBTSerializable
   private byte state;
   private final WorldMapRegionIcon worldMapRegionIcon;
   private final WorldMapRegionInfo worldMapRegionInfo;

   public WorldMapRegion(
      ResourceLocation mainTexture, int width, int height, int XPos, int YPos, byte state, WorldMapRegionIcon worldMapRegionIcon, WorldMapRegionInfo regionInfo
   ) {
      this.texture = mainTexture;
      this.width = width;
      this.height = height;
      this.XPos = XPos;
      this.YPos = YPos;
      this.state = state;
      this.worldMapRegionIcon = worldMapRegionIcon;
      this.worldMapRegionInfo = regionInfo;
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      this.id = id;
      event.register(Registries.ITEM, h -> WorldMapRegionsStorage.addWMRegion(this));
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      nbt.putByte("state", this.state);
      CompoundTag buttons = new CompoundTag();
      if (this.worldMapRegionInfo.getButtons() != null && !this.worldMapRegionInfo.getButtons().isEmpty()) {
         for (WMRButton button : this.worldMapRegionInfo.getButtons()) {
            if (button.getName() != null) {
               buttons.putBoolean(button.getName(), button.isEnabled());
            }
         }
      }

      nbt.put("buttons", buttons);
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.state = nbt.getByte("state");
      CompoundTag buttons = nbt.getCompound("buttons");
      List<WMRButton> buttonsList = new ArrayList<>();

      for (String key : WorldMapRegionsStorage.WORLD_MAP_BUTTONS.keySet()) {
         if (buttons.contains(key)) {
            WMRButton button = WorldMapRegionsStorage.WORLD_MAP_BUTTONS.get(key).copy(buttons.getBoolean(key));
            buttonsList.add(button);
         }
      }

      this.worldMapRegionInfo.setButtons(buttonsList);
      IAutoNBTSerializable.super.deserializeNBT(nbt);
   }

   public Runnable setState(int state) {
      return () -> this.setState((byte)state);
   }

   public void setState(byte state) {
      this.state = state;
   }

   @Generated
   public static WorldMapRegion.WorldMapRegionBuilder builder() {
      return new WorldMapRegion.WorldMapRegionBuilder();
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public ResourceLocation getTexture() {
      return this.texture;
   }

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getHeight() {
      return this.height;
   }

   @Generated
   public int getXPos() {
      return this.XPos;
   }

   @Generated
   public int getYPos() {
      return this.YPos;
   }

   @Generated
   public byte getState() {
      return this.state;
   }

   @Generated
   public WorldMapRegionIcon getWorldMapRegionIcon() {
      return this.worldMapRegionIcon;
   }

   @Generated
   public WorldMapRegionInfo getWorldMapRegionInfo() {
      return this.worldMapRegionInfo;
   }

   @Generated
   public static class WorldMapRegionBuilder {
      @Generated
      private ResourceLocation mainTexture;
      @Generated
      private int width;
      @Generated
      private int height;
      @Generated
      private int XPos;
      @Generated
      private int YPos;
      @Generated
      private byte state;
      @Generated
      private WorldMapRegionIcon worldMapRegionIcon;
      @Generated
      private WorldMapRegionInfo regionInfo;

      @Generated
      WorldMapRegionBuilder() {
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder mainTexture(ResourceLocation mainTexture) {
         this.mainTexture = mainTexture;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder width(int width) {
         this.width = width;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder height(int height) {
         this.height = height;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder XPos(int XPos) {
         this.XPos = XPos;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder YPos(int YPos) {
         this.YPos = YPos;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder state(byte state) {
         this.state = state;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder worldMapRegionIcon(WorldMapRegionIcon worldMapRegionIcon) {
         this.worldMapRegionIcon = worldMapRegionIcon;
         return this;
      }

      @Generated
      public WorldMapRegion.WorldMapRegionBuilder regionInfo(WorldMapRegionInfo regionInfo) {
         this.regionInfo = regionInfo;
         return this;
      }

      @Generated
      public WorldMapRegion build() {
         return new WorldMapRegion(this.mainTexture, this.width, this.height, this.XPos, this.YPos, this.state, this.worldMapRegionIcon, this.regionInfo);
      }

      @Generated
      @Override
      public String toString() {
         return "WorldMapRegion.WorldMapRegionBuilder(mainTexture="
            + this.mainTexture
            + ", width="
            + this.width
            + ", height="
            + this.height
            + ", XPos="
            + this.XPos
            + ", YPos="
            + this.YPos
            + ", state="
            + this.state
            + ", worldMapRegionIcon="
            + this.worldMapRegionIcon
            + ", regionInfo="
            + this.regionInfo
            + ")";
      }
   }
}
