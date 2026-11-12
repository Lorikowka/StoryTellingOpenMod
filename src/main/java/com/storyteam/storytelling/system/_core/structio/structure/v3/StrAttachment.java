package com.storyteam.storytelling.system._core.structio.structure.v3;

import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.zeith.hammerlib.api.io.ICompoundSerializable;

public class StrAttachment implements ICompoundSerializable {
   public CompoundTag data;

   public StrAttachment(CompoundTag data) {
      this.data = data;
   }

   public StrAttachment() {
      this.data = new CompoundTag();
   }

   public StrAttachment sub() {
      return new StrAttachment();
   }

   public StrAttachment sub(CompoundTag tag) {
      return new StrAttachment(tag);
   }

   public void setSubs(String key, StrAttachment... subs) {
      ListTag lst = new ListTag();

      for (StrAttachment sub : subs) {
         lst.add(sub.serializeNBT());
      }

      this.data.put(key, lst);
   }

   public void setTag(String key, Tag tag) {
      this.data.put(key, tag);
   }

   public void setUUID(String key, UUID uuid) {
      this.data.putUUID(key, uuid);
   }

   public void getBool(String key, boolean bool) {
      this.data.putBoolean(key, bool);
   }

   public void setString(String key, String str) {
      this.data.putString(key, str);
   }

   public void setByte(String key, byte num) {
      this.data.putByte(key, num);
   }

   public void setShort(String key, short num) {
      this.data.putShort(key, num);
   }

   public void setInt(String key, int num) {
      this.data.putInt(key, num);
   }

   public void setFloat(String key, float num) {
      this.data.putFloat(key, num);
   }

   public void setLong(String key, long num) {
      this.data.putLong(key, num);
   }

   public void setDouble(String key, double num) {
      this.data.putDouble(key, num);
   }

   public void setBytes(String key, byte... bytes) {
      this.data.putByteArray(key, bytes);
   }

   public void setInts(String key, int... ints) {
      this.data.putIntArray(key, ints);
   }

   public void setLongs(String key, long... longs) {
      this.data.putLongArray(key, longs);
   }

   public List<StrAttachment> getSubs(String key) {
      ListTag tags = this.data.getList(key, 10);
      return tags.stream().map(CompoundTag.class::cast).map(StrAttachment::new).toList();
   }

   public Tag getTag(String key) {
      return this.data.get(key);
   }

   public UUID getUUID(String key) {
      return this.data.getUUID(key);
   }

   public boolean getBool(String key) {
      return this.data.getBoolean(key);
   }

   public String getString(String key) {
      return this.data.getString(key);
   }

   public byte getByte(String key) {
      return this.data.getByte(key);
   }

   public short getShort(String key) {
      return this.data.getShort(key);
   }

   public int getInt(String key) {
      return this.data.getInt(key);
   }

   public float getFloat(String key) {
      return this.data.getFloat(key);
   }

   public long getLong(String key) {
      return this.data.getLong(key);
   }

   public double getDouble(String key) {
      return this.data.getDouble(key);
   }

   public byte[] getBytes(String key) {
      return this.data.getByteArray(key);
   }

   public int[] getInts(String key) {
      return this.data.getIntArray(key);
   }

   public long[] getLongs(String key) {
      return this.data.getLongArray(key);
   }

   public CompoundTag serializeNBT() {
      return this.data.copy();
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.data = nbt.copy();
   }
}
