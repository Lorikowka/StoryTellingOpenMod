package com.storyteam.storytelling.system.clothes.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.util.java.Cast;

@SimplyRegister(
   prefix = "system/clothes/"
)
public class VanityInvDataSerializer implements EntityDataSerializer<VanityInventory>, ICustomRegistrar {
   @RegistryName("vanity_inventory")
   public static final VanityInvDataSerializer INSTANCE = new VanityInvDataSerializer();

   private VanityInvDataSerializer() {
   }

   public void write(FriendlyByteBuf buf, VanityInventory inv) {
      if (inv == null) {
         buf.writeNbt(null);
      } else {
         CompoundTag tag = new CompoundTag();
         tag.put("Items", inv.serializeNBT());
         buf.writeNbt(tag);
      }
   }

   public VanityInventory read(FriendlyByteBuf buf) {
      CompoundTag tag = buf.readNbt();
      if (tag == null) {
         return null;
      } else {
         VanityInventory inv = new VanityInventory();
         VanityInventory.deserializeNBT(tag.getList("Items", 10));
         return inv;
      }
   }

   public VanityInventory copy(VanityInventory inv) {
      return inv != null ? inv.copy() : null;
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(Keys.ENTITY_DATA_SERIALIZERS, id, Cast.constant(this));
   }
}
