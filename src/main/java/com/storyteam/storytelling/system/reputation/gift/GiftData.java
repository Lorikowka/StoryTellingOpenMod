package com.storyteam.storytelling.system.reputation.gift;

import lombok.Generated;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class GiftData implements IAutoNBTSerializable {
   @NBTSerializable
   private String id;

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public void setId(String id) {
      this.id = id;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GiftData other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$id = this.getId();
         Object other$id = other.getId();
         return this$id == null ? other$id == null : this$id.equals(other$id);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof GiftData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $id = this.getId();
      return result * 59 + ($id == null ? 43 : $id.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "GiftData(id=" + this.getId() + ")";
   }

   @Generated
   public GiftData() {
   }

   @Generated
   public GiftData(String id) {
      this.id = id;
   }
}
