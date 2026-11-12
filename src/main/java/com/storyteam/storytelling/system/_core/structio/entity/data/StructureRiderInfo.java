package com.storyteam.storytelling.system._core.structio.entity.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record StructureRiderInfo(Map<UUID, Vec3> offsets) {
   public static final StructureRiderInfo EMPTY = new StructureRiderInfo(Map.of());

   public StructureRiderInfo copy() {
      return new StructureRiderInfo(Map.copyOf(this.offsets));
   }

   public StructureRiderInfo withOffset(Entity entity, Vec3 offset) {
      UUID id = entity.getUUID();
      return new StructureRiderInfo(Map.copyOf((Map<? extends UUID, ? extends Vec3>)Util.make(new HashMap<>(this.offsets), m -> m.put(id, offset))));
   }

   public Vec3 getOffset(Entity entity) {
      return this.offsets.getOrDefault(entity.getUUID(), Vec3.ZERO);
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      ListTag lst = new ListTag();

      for (Entry<UUID, Vec3> e : this.offsets.entrySet()) {
         CompoundTag sub = new CompoundTag();
         sub.putUUID("Id", e.getKey());
         Vec3 v = e.getValue();
         sub.putDouble("x", v.x());
         sub.putDouble("y", v.y());
         sub.putDouble("z", v.z());
         lst.add(sub);
      }

      tag.put("offsets", lst);
      return tag;
   }

   public static StructureRiderInfo deserializeNBT(CompoundTag nbt) {
      Map<UUID, Vec3> offsets = new HashMap<>();
      ListTag lst = nbt.getList("offsets", 10);

      for (int i = 0; i < lst.size(); i++) {
         CompoundTag sub = lst.getCompound(i);
         UUID id = sub.getUUID("Id");
         Vec3 off = new Vec3(sub.getDouble("x"), sub.getDouble("y"), sub.getDouble("z"));
         offsets.put(id, off);
      }

      return new StructureRiderInfo(Map.copyOf(offsets));
   }

   public static void write(FriendlyByteBuf buf, StructureRiderInfo inf) {
      buf.writeBoolean(inf != null);
      if (inf != null) {
         buf.writeVarInt(inf.offsets.size());

         for (Entry<UUID, Vec3> e : inf.offsets.entrySet()) {
            buf.writeUUID(e.getKey());
            Vec3 v = e.getValue();
            buf.writeDouble(v.x());
            buf.writeDouble(v.y());
            buf.writeDouble(v.z());
         }
      }
   }

   public static StructureRiderInfo read(FriendlyByteBuf buf) {
      if (!buf.readBoolean()) {
         return null;
      } else {
         Map<UUID, Vec3> offsets = new HashMap<>();
         int offsetCount = buf.readVarInt();

         for (int i = 0; i < offsetCount; i++) {
            UUID id = buf.readUUID();
            Vec3 off = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            offsets.put(id, off);
         }

         return new StructureRiderInfo(Map.copyOf(offsets));
      }
   }
}
