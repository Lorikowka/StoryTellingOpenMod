package com.storyteam.storytelling.util.data.buffered;

import net.minecraft.nbt.CompoundTag;

public class BooleanBuffer extends BufferedData<Boolean> {
   public BooleanBuffer() {
      super(false);
   }

   public BooleanBuffer(Boolean initialValue) {
      super(initialValue);
   }

   public void handeIfChanged(Runnable ifTrue, Runnable ifFalse) {
      if (this.isChange()) {
         if (this.value != null && this.value) {
            ifTrue.run();
         } else {
            ifFalse.run();
         }

         this.isChange = false;
      }
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("value", this.value);
      return super.serializeNBT();
   }

   @Override
   public void deserializeManual(CompoundTag nbt) {
      this.value = nbt.getBoolean("value");
   }
}
