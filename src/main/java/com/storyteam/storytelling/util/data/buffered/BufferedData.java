package com.storyteam.storytelling.util.data.buffered;

import com.storyteam.storytelling.api.serialization.IHandledAutoNBTSerializable;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

public class BufferedData<T> extends DirectStorage<T> implements IHandledAutoNBTSerializable {
   @NBTSerializable
   T value;
   @NBTSerializable
   boolean isChange;

   public BufferedData() {
      super(null, null);
   }

   public BufferedData(T initialValue) {
      super(null, null);
      this.value = initialValue;
   }

   public T get() {
      return this.value;
   }

   public void set(T value) {
      this.value = value;
      this.isChange = true;
   }

   public void markChanged(boolean changed) {
      this.isChange = changed;
   }

   public void ifChanged(Consumer<T> function) {
      function.accept(this.value);
      this.isChange = false;
   }

   @Nullable
   @Override
   public CompoundTag serializeManual() {
      CompoundTag compoundTag = new CompoundTag();
      if (this.value instanceof INBTSerializable<?> serializable) {
         compoundTag.put("value", serializable.serializeNBT());
      }

      return compoundTag;
   }

   @Override
   public void deserializeManual(CompoundTag nbt) {
      if (this.value instanceof INBTSerializable<?> deserialize) {
         deserializeValue(deserialize, nbt.get("value"));
      }
   }

   @SuppressWarnings({"rawtypes", "unchecked"})
   private static void deserializeValue(INBTSerializable value, Tag tag) {
      value.deserializeNBT(tag);
   }

   @Generated
   public boolean isChange() {
      return this.isChange;
   }
}
