package com.storyteam.storytelling.script.data;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ScriptFlagStorage implements INBTSerializable<CompoundTag> {
   private final HashMap<String, String> flags = Maps.newHashMap();

   public void set(String id, String value) {
      this.flags.put(id, value);
   }

   public void set(String id) {
      this.flags.put(id, "");
   }

   public String get(String id) {
      return this.flags.get(id);
   }

   public boolean has(String id) {
      return this.flags.containsKey(id);
   }

   public void remove(String id) {
      this.flags.remove(id);
   }

   public HashMap<String, String> getFlags() {
      return this.flags;
   }

   public boolean tryRemove(String id) {
      return this.flags.remove(id) != null;
   }

   public boolean equal(String flag, String value) {
      return this.has(flag) && this.get(flag).equals(value);
   }

   public Optional<String> with(String id) {
      return Optional.ofNullable(this.flags.get(id));
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();

      for (Entry<String, String> entry : this.flags.entrySet()) {
         nbt.putString(entry.getKey(), entry.getValue());
      }

      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.flags.clear();
      if (nbt != null) {
         for (String key : nbt.getAllKeys()) {
            if (nbt.contains(key, 8)) {
               this.flags.put(key, nbt.getString(key));
            }
         }
      }
   }

   @Override
   public String toString() {
      return this.flags.toString();
   }
}
