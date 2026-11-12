package com.storyteam.storytelling.system._core.structio.structure;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

public class StructureTagList implements INBTSerializable<ListTag> {
   public final ArrayList<CompoundTag> entries = new ArrayList<>();

   public void prepare(int extra) {
      this.entries.ensureCapacity(this.entries.size() + extra);
   }

   public void add(CompoundTag tag) {
      if (tag != null) {
         this.entries.add(tag);
      }
   }

   public void add(INBTSerializable<CompoundTag> entry) {
      if (entry != null) {
         this.entries.add((CompoundTag)entry.serializeNBT());
      }
   }

   public ListTag serializeNBT() {
      ListTag lst = new ListTag();
      lst.addAll(this.entries);
      return lst;
   }

   public void deserializeNBT(ListTag nbt) {
      this.entries.clear();

      for (int i = 0; i < nbt.size(); i++) {
         CompoundTag tag = nbt.getCompound(i);
         if (!tag.isEmpty()) {
            this.entries.add(tag);
         }
      }
   }

   public void clear() {
      this.entries.clear();
   }

   public int size() {
      return this.entries.size();
   }

   public CompoundTag get(int i) {
      return this.entries.get(i);
   }
}
