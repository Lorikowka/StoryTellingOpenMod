package com.storyteam.storytelling.system.notifications.data;

import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NotificationData implements Cloneable {
   private ResourceLocation icon;
   private Component title;
   private int progress;

   public NotificationData clone() {
      try {
         return (NotificationData)super.clone();
      } catch (Throwable var2) {
         if (var2 instanceof RuntimeException runtime) throw runtime;
         if (var2 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var2);
      }
   }

   @Generated
   public ResourceLocation getIcon() {
      return this.icon;
   }

   @Generated
   public Component getTitle() {
      return this.title;
   }

   @Generated
   public int getProgress() {
      return this.progress;
   }

   @Generated
   public void setIcon(ResourceLocation icon) {
      this.icon = icon;
   }

   @Generated
   public void setTitle(Component title) {
      this.title = title;
   }

   @Generated
   public void setProgress(int progress) {
      this.progress = progress;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NotificationData other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (this.getProgress() != other.getProgress()) {
         return false;
      } else {
         Object this$icon = this.getIcon();
         Object other$icon = other.getIcon();
         if (this$icon == null ? other$icon == null : this$icon.equals(other$icon)) {
            Object this$title = this.getTitle();
            Object other$title = other.getTitle();
            return this$title == null ? other$title == null : this$title.equals(other$title);
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof NotificationData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getProgress();
      Object $icon = this.getIcon();
      result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
      Object $title = this.getTitle();
      return result * 59 + ($title == null ? 43 : $title.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "NotificationData(icon=" + this.getIcon() + ", title=" + this.getTitle() + ", progress=" + this.getProgress() + ")";
   }

   @Generated
   public NotificationData(ResourceLocation icon, Component title, int progress) {
      this.icon = icon;
      this.title = title;
      this.progress = progress;
   }
}
