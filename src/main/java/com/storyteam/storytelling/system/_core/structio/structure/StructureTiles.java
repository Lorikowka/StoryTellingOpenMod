package com.storyteam.storytelling.system._core.structio.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

public class StructureTiles implements INBTSerializable<ListTag> {
   public final Map<BlockPos, CompoundTag> entries = new HashMap<>();

   public Set<BlockPos> positions() {
      return this.entries.keySet();
   }

   public String getTypeAt(BlockPos pos) {
      CompoundTag e = this.entries.get(pos);
      if (e == null) {
         return null;
      } else {
         String data = e.getString("id");
         return data.isBlank() ? null : data;
      }
   }

   public void set(BlockEntity be) {
      if (be != null) {
         this.entries.put(be.getBlockPos(), be.saveWithFullMetadata());
      }
   }

   public ListTag serializeNBT() {
      ListTag lst = new ListTag();
      lst.addAll(this.entries.values());
      return lst;
   }

   public void deserializeNBT(ListTag nbt) {
      this.entries.clear();

      for (int i = 0; i < nbt.size(); i++) {
         CompoundTag tag = nbt.getCompound(i);
         if (!tag.isEmpty()) {
            BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
            this.entries.put(pos, tag);
         }
      }
   }

   public void clear() {
      this.entries.clear();
   }
}
