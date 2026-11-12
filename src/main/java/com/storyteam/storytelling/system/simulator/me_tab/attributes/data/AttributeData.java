package com.storyteam.storytelling.system.simulator.me_tab.attributes.data;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.net.MeTabScreenAnimatedAttributePacket;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.zeith.hammerlib.net.Network;

public class AttributeData {
   private final AttributeType type;
   private int value;
   private int parts;
   private int pendingParts;
   private final int PARTS_PER_VALUE = 3;

   public AttributeData(AttributeType type) {
      this.type = type;
      this.value = 0;
      this.parts = 0;
      this.pendingParts = 0;
   }

   public void tick(ServerPlayer player) {
      if (this.canConvertParts()) {
         if (PlayerStats.isGuiWithAttributeScreenOpen) {
            Network.sendTo(player, new MeTabScreenAnimatedAttributePacket(this.type));
         } else {
            this.convertAllPartsToValue();
         }
      }
   }

   public void add(int value, int parts) {
      this.addValue(value);
      this.addParts(parts);
   }

   public void addValue(int amount) {
      this.value = Math.max(0, this.value + amount);
   }

   public void addParts(int amount) {
      if (amount != 0) {
         if (amount > 0) {
            if (!this.canConvertParts()) {
               this.parts += amount;
               if (this.parts > 3) {
                  int overflow = this.parts - 3;
                  this.parts = 3;
                  this.pendingParts += overflow;
               }
            } else {
               this.pendingParts += amount;
            }
         } else {
            int remaining = -amount;

            while (remaining > 0) {
               if (this.parts > 0) {
                  int deduction = Math.min(this.parts, remaining);
                  this.parts -= deduction;
                  remaining -= deduction;
               } else if (this.pendingParts > 0) {
                  int deduction = Math.min(this.pendingParts, remaining);
                  this.pendingParts -= deduction;
                  remaining -= deduction;
               } else {
                  if (this.value <= 0) {
                     break;
                  }

                  this.value--;
                  this.parts = 2;
                  remaining--;
               }
            }
         }
      }
   }

   public boolean canConvertParts() {
      return this.parts >= 3;
   }

   public void convertPartsToValue() {
      if (this.canConvertParts()) {
         this.value++;
         this.parts -= 3;
         if (this.parts < 0) {
            this.parts = 0;
         }

         if (this.pendingParts > 0) {
            int transfer = Math.min(3, this.pendingParts);
            this.pendingParts -= transfer;
            this.parts += transfer;
         }
      }
   }

   protected void convertAllPartsToValue() {
      int totalParts = this.parts + this.pendingParts;
      this.pendingParts = 0;
      int addedValues = totalParts / 3;
      int remainder = totalParts % 3;
      this.value += addedValues;
      this.parts = remainder;
   }

   public void set(int newValue, int newParts) {
      this.value = Math.max(0, newValue);
      this.parts = Math.min(Math.max(0, newParts), 3);
   }

   public void setValue(int newValue) {
      this.set(newValue, this.parts);
   }

   public void setParts(int newParts) {
      this.set(this.value, newParts);
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("value", this.value);
      tag.putInt("parts", this.parts);
      tag.putInt("pending", this.pendingParts);
      return tag;
   }

   public void load(CompoundTag tag) {
      this.value = tag.getInt("value");
      this.parts = tag.getInt("parts");
      this.pendingParts = tag.getInt("pending");
   }

   @Generated
   public AttributeType getType() {
      return this.type;
   }

   @Generated
   public int getValue() {
      return this.value;
   }

   @Generated
   public int getParts() {
      return this.parts;
   }

   @Generated
   public int getPARTS_PER_VALUE() {
      return 3;
   }
}
