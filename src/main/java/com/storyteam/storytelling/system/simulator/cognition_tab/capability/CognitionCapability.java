package com.storyteam.storytelling.system.simulator.cognition_tab.capability;

import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.events.init.CognitionUpgradeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

@AutoRegisterCapability
public class CognitionCapability implements IAutoNBTSerializable {
   private final Player player;
   private final Map<ResourceLocation, Integer> cogTier = new HashMap<>();

   public CognitionCapability(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public int getCognitionTier(Cognition cognition) {
      return this.cogTier.getOrDefault(cognition.id(), 0);
   }

   public void upgrade(Cognition cognition) {
      ResourceLocation id = cognition.id();
      if (!this.cogTier.containsKey(id)) {
         this.cogTier.put(id, 1);
      } else {
         int currTier = this.cogTier.get(id);
         int nextTier = currTier + 1;
         if (nextTier > 4) {
            return;
         }

         CognitionUpgradeEvent event = new CognitionUpgradeEvent(this.player, cognition, currTier, nextTier);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCanceled()) {
            return;
         }

         this.cogTier.put(id, nextTier);
      }
   }

   public void downgrade(Cognition cognition) {
      ResourceLocation id = cognition.id();
      if (this.cogTier.containsKey(id)) {
         int currTier = this.cogTier.get(id);
         int prevTier = currTier - 1;
         if (prevTier <= 0) {
            this.remove(cognition);
         } else {
            this.cogTier.put(id, prevTier);
         }
      }
   }

   public void remove(Cognition cognition) {
      this.cogTier.remove(cognition.id());
   }

   public void copyFrom(CognitionCapability cap) {
      this.cogTier.clear();
      this.cogTier.putAll(cap.cogTier);
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();

      for (Entry<ResourceLocation, Integer> entry : this.cogTier.entrySet()) {
         tag.putInt(entry.getKey().toString(), entry.getValue());
      }

      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.cogTier.clear();

      for (String key : nbt.getAllKeys()) {
         ResourceLocation id = ResourceLocation.tryParse(key);
         if (id != null) {
            int tier = nbt.getInt(key);
            this.cogTier.put(id, tier);
         }
      }
   }
}
