package com.storyteam.storytelling.system.reputation;

import com.storyteam.storytelling.system._core.character_data.CharacterDataAttachment;
import com.storyteam.storytelling.system.reputation.gift.GiftData;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntMap.FastEntrySet;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class CharacterReputation extends CharacterDataAttachment {
   @NBTSerializable("baseline")
   private int baseline = 0;
   @NBTSerializable
   private int giftDay;
   private Object2IntArrayMap<String> reputationModifiers = new Object2IntArrayMap();
   private Map<String, GiftData> presents = new HashMap<>();
   private int total;
   private boolean totalDirty = true;

   CharacterReputation(String name) {
      super(name);
   }

   public void addGift(Item item) {
      this.addGift(ForgeRegistries.ITEMS.getKey(item).toString());
   }

   public void addGift(String id) {
      this.presents.put(id, new GiftData(id));
   }

   public void removeGift(Item item) {
      this.removeGift(ForgeRegistries.ITEMS.getKey(item).toString());
   }

   public void removeGift(String id) {
      this.presents.remove(id);
   }

   public boolean hasGift(Item item) {
      return this.hasGift(ForgeRegistries.ITEMS.getKey(item).toString());
   }

   public boolean hasGift(String id) {
      return this.presents.containsKey(id);
   }

   public int getReputation() {
      if (this.totalDirty) {
         this.total = Mth.clamp(this.baseline + this.reputationModifiers.values().intStream().sum(), 0, 100);
         this.totalDirty = false;
      }

      return this.total;
   }

   public void setBaseline(int baseline) {
      this.baseline = baseline;
      this.totalDirty = true;
   }

   public int getTotal() {
      return this.getReputation();
   }

   public FastEntrySet<String> modifiers() {
      return this.reputationModifiers.object2IntEntrySet();
   }

   public CompoundTag serializeNBT() {
      CompoundTag data = super.serializeNBT();
      ListTag list = new ListTag();

      for (GiftData present : this.presents.values()) {
         list.add(present.serializeNBT());
      }

      data.put("presents", list);
      CompoundTag tag = new CompoundTag();
      ObjectIterator var7 = this.reputationModifiers.object2IntEntrySet().iterator();

      while (var7.hasNext()) {
         Entry<String> mod = (Entry<String>)var7.next();
         tag.putInt((String)mod.getKey(), mod.getIntValue());
      }

      data.put("modifiers", tag);
      return data;
   }

   public void deserializeNBT(CompoundTag nbt) {
      Map<String, GiftData> presentsResult = new HashMap<>();
      Object2IntArrayMap<String> repMods = new Object2IntArrayMap();
      ListTag list = nbt.getList("presents", 10);

      for (int i = 0; i < list.size(); i++) {
         GiftData pd = new GiftData();
         pd.deserializeNBT(list.getCompound(i));
         presentsResult.put(pd.getId(), pd);
      }

      CompoundTag tag = nbt.getCompound("modifiers");

      for (String key : tag.getAllKeys()) {
         repMods.put(key, tag.getInt(key));
      }

      this.presents = presentsResult;
      this.reputationModifiers = repMods;
      this.totalDirty = true;
      super.deserializeNBT(nbt);
   }

   @Generated
   public int getBaseline() {
      return this.baseline;
   }

   @Generated
   public int getGiftDay() {
      return this.giftDay;
   }

   @Generated
   public Object2IntArrayMap<String> getReputationModifiers() {
      return this.reputationModifiers;
   }

   @Generated
   public Map<String, GiftData> getPresents() {
      return this.presents;
   }

   @Generated
   public boolean isTotalDirty() {
      return this.totalDirty;
   }

   @Generated
   public void setGiftDay(int giftDay) {
      this.giftDay = giftDay;
   }

   @Generated
   public void setReputationModifiers(Object2IntArrayMap<String> reputationModifiers) {
      this.reputationModifiers = reputationModifiers;
   }

   @Generated
   public void setPresents(Map<String, GiftData> presents) {
      this.presents = presents;
   }

   @Generated
   public void setTotal(int total) {
      this.total = total;
   }

   @Generated
   public void setTotalDirty(boolean totalDirty) {
      this.totalDirty = totalDirty;
   }
}
