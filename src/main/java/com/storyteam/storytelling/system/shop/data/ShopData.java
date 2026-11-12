package com.storyteam.storytelling.system.shop.data;

import com.storyteam.storytelling.system.shop.init.ShopsST;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class ShopData implements IAutoNBTSerializable {
   @NBTSerializable
   private String id;
   public Map<String, LotData> lots;

   public ShopData(ShopInfo info) {
      this.id = info.getId();
      Map<String, LotData> lots = new LinkedHashMap<>();

      for (LotInfo lot : info.getAllLots().values()) {
         lots.put(lot.getId(), new LotData(lot));
      }

      this.lots = lots;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      CompoundTag lotsTag = new CompoundTag();

      for (Entry<String, LotInfo> entry : ShopsST.SHOPS.get(this.id).getAllLots().entrySet()) {
         String id = entry.getKey();
         lotsTag.put(id, (this.lots.containsKey(id) ? this.lots.get(id) : new LotData(entry.getValue())).serializeNBT());
      }

      tag.put("lots", lotsTag);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      CompoundTag lotsTag = nbt.getCompound("lots");

      for (Entry<String, LotInfo> entry : ShopsST.SHOPS.get(this.id).getAllLots().entrySet()) {
         String id = entry.getKey();
         LotData dummy = new LotData(entry.getValue());
         if (lotsTag.contains(id)) {
            dummy.deserializeNBT(lotsTag.getCompound(id));
         }

         this.lots.put(id, dummy);
      }
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public Map<String, LotData> getLots() {
      return this.lots;
   }

   @Generated
   public void setId(String id) {
      this.id = id;
   }

   @Generated
   public void setLots(Map<String, LotData> lots) {
      this.lots = lots;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ShopData other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$id = this.getId();
         Object other$id = other.getId();
         if (this$id == null ? other$id == null : this$id.equals(other$id)) {
            Object this$lots = this.getLots();
            Object other$lots = other.getLots();
            return this$lots == null ? other$lots == null : this$lots.equals(other$lots);
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof ShopData;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $lots = this.getLots();
      return result * 59 + ($lots == null ? 43 : $lots.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "ShopData(id=" + this.getId() + ", lots=" + this.getLots() + ")";
   }

   @Generated
   public ShopData(String id, Map<String, LotData> lots) {
      this.id = id;
      this.lots = lots;
   }
}
