package com.storyteam.storytelling.system.reputation.gift;

import lombok.Generated;

public class GiftInfo {
   private GiftCategory category;

   @Generated
   public GiftCategory getCategory() {
      return this.category;
   }

   @Generated
   public void setCategory(GiftCategory category) {
      this.category = category;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GiftInfo other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$category = this.getCategory();
         Object other$category = other.getCategory();
         return this$category == null ? other$category == null : this$category.equals(other$category);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof GiftInfo;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $category = this.getCategory();
      return result * 59 + ($category == null ? 43 : $category.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "GiftInfo(category=" + this.getCategory() + ")";
   }

   @Generated
   public GiftInfo(GiftCategory category) {
      this.category = category;
   }
}
