package com.storyteam.storytelling.compat.hl.io;

import com.storyteam.storytelling.system.clothes.entity.VanityInventory;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;

@NBTSerializer({VanityInventory.class})
public class VanityInventorySerializer implements INBTSerializer<VanityInventory> {
   public void serialize(CompoundTag nbt, String key, @NotNull VanityInventory value) {
      nbt.put(key, value.serializeNBT());
   }

   @Nullable
   public VanityInventory deserialize(CompoundTag nbt, String key) {
      return VanityInventory.deserializeNBT(nbt.getList(key, 10));
   }
}
