package com.storyteam.storytelling.api.serialization;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public interface IHandledAutoNBTSerializable extends IAutoNBTSerializable {
   @Nullable
   default CompoundTag serializeManual() {
      return null;
   }

   default void deserializeManual(CompoundTag nbt) {
   }

   default void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt.getCompound("Auto"));
      if (nbt.contains("Manual")) {
         this.deserializeManual(nbt.getCompound("Manual"));
      }
   }

   default CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      CompoundTag all = new CompoundTag();
      all.put("Auto", tag);
      CompoundTag manual = this.serializeManual();
      if (manual != null) {
         all.put("Manual", manual);
      }

      return all;
   }
}
