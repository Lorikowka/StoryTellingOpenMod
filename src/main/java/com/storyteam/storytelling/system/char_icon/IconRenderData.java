package com.storyteam.storytelling.system.char_icon;

import lombok.Generated;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class IconRenderData {
   private String uuid;
   private ItemStack item;
   private Vec3 posOffset;

   public static IconRenderData of(String uuid, ItemStack item, Vec3 offset) {
      return builder().uuid(uuid).item(item).posOffset(offset).build();
   }

   public static IconRenderData of(String uuid, ItemStack item) {
      return builder().uuid(uuid).item(item).build();
   }

   public static IconRenderData empty(String uuid) {
      return builder().uuid(uuid).build();
   }

   @Generated
   private static ItemStack $default$item() {
      return ItemStack.EMPTY;
   }

   @Generated
   private static Vec3 $default$posOffset() {
      return Vec3.ZERO;
   }

   @Generated
   IconRenderData(String uuid, ItemStack item, Vec3 posOffset) {
      this.uuid = uuid;
      this.item = item;
      this.posOffset = posOffset;
   }

   @Generated
   public static IconRenderData.IconRenderDataBuilder builder() {
      return new IconRenderData.IconRenderDataBuilder();
   }

   @Generated
   public String getUuid() {
      return this.uuid;
   }

   @Generated
   public ItemStack getItem() {
      return this.item;
   }

   @Generated
   public Vec3 getPosOffset() {
      return this.posOffset;
   }

   @Generated
   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   @Generated
   public void setItem(ItemStack item) {
      this.item = item;
   }

   @Generated
   public void setPosOffset(Vec3 posOffset) {
      this.posOffset = posOffset;
   }

   @Generated
   public static class IconRenderDataBuilder {
      @Generated
      private String uuid;
      @Generated
      private boolean item$set;
      @Generated
      private ItemStack item$value;
      @Generated
      private boolean posOffset$set;
      @Generated
      private Vec3 posOffset$value;

      @Generated
      IconRenderDataBuilder() {
      }

      @Generated
      public IconRenderData.IconRenderDataBuilder uuid(String uuid) {
         this.uuid = uuid;
         return this;
      }

      @Generated
      public IconRenderData.IconRenderDataBuilder item(ItemStack item) {
         this.item$value = item;
         this.item$set = true;
         return this;
      }

      @Generated
      public IconRenderData.IconRenderDataBuilder posOffset(Vec3 posOffset) {
         this.posOffset$value = posOffset;
         this.posOffset$set = true;
         return this;
      }

      @Generated
      public IconRenderData build() {
         ItemStack item$value = this.item$value;
         if (!this.item$set) {
            item$value = IconRenderData.$default$item();
         }

         Vec3 posOffset$value = this.posOffset$value;
         if (!this.posOffset$set) {
            posOffset$value = IconRenderData.$default$posOffset();
         }

         return new IconRenderData(this.uuid, item$value, posOffset$value);
      }

      @Generated
      @Override
      public String toString() {
         return "IconRenderData.IconRenderDataBuilder(uuid="
            + this.uuid
            + ", item$value="
            + this.item$value
            + ", posOffset$value="
            + this.posOffset$value
            + ")";
      }
   }
}
