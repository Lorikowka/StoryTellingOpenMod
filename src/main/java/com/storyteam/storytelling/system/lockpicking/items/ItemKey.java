package com.storyteam.storytelling.system.lockpicking.items;

import com.storyteam.storytelling.system.lockpicking.InitLockPicking;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import java.util.Objects;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class ItemKey extends Item {
   public ItemKey(Properties pProperties) {
      super(pProperties);
   }

   @Nullable
   protected ItemKey.KeyData modifyKeyData(ItemStack stack, @Nullable ItemKey.KeyData data) {
      return data;
   }

   public ItemKey.KeyData getData(ItemStack stack) {
      if (stack.getItem() != this) {
         return null;
      } else {
         CompoundTag key = stack.getTagElement("Key");
         if (key != null && !key.isEmpty()) {
            ItemKey.KeyData data = new ItemKey.KeyData();
            data.deserializeNBT(key);
            return this.modifyKeyData(stack, data);
         } else {
            return this.modifyKeyData(stack, null);
         }
      }
   }

   @Nullable
   public static ItemKey.KeyData getKeyData(ItemStack stack) {
      return !stack.isEmpty() && stack.getItem() instanceof ItemKey key ? key.getData(stack) : null;
   }

   public static void setKeyData(ItemStack stack, ItemKey.KeyData keys) {
      if (keys == null) {
         stack.removeTagKey("Key");
      } else {
         stack.getOrCreateTag().put("Key", keys.serializeNBT());
      }
   }

   public static ItemStack createKey(boolean consume, ResourceLocation sound, GlobalPos... positions) {
      ItemKey.KeyData kd = new ItemKey.KeyData();
      kd.consume = consume;
      if (sound != null) {
         kd.sound = sound;
      }

      kd.pos = positions;
      ItemStack stack = InitLockPicking.KEY.getDefaultInstance();
      setKeyData(stack, kd);
      return stack;
   }

   public static class KeyData implements IAutoNBTSerializable {
      @NBTSerializable
      public GlobalPos[] pos = new GlobalPos[0];
      @NBTSerializable
      public boolean consume = true;
      @NBTSerializable
      public ResourceLocation sound = SoundEvents.ARMOR_EQUIP_NETHERITE.getLocation();

      public SoundEvent getSound() {
         return SoundEvent.createVariableRangeEvent(this.sound);
      }

      public boolean matches(LockRegistryEntry entry) {
         return entry.positions().anyMatch(pos -> {
            for (GlobalPos v : this.pos) {
               if (Objects.equals(v, pos)) {
                  return true;
               }
            }

            return false;
         });
      }
   }
}
