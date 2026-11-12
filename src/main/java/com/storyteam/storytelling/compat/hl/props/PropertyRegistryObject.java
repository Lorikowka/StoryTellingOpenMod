package com.storyteam.storytelling.compat.hl.props;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.hammerlib.net.properties.PropertyBase;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyRegistryObject<T> extends PropertyBase<T> {
   protected final IForgeRegistry<T> registry;

   public PropertyRegistryObject(Class<T> type, IForgeRegistry<T> registry, DirectStorage<T> value) {
      super(type, value);
      this.registry = registry;
   }

   public PropertyRegistryObject(Class<T> type, IForgeRegistry<T> registry) {
      super(type);
      this.registry = registry;
   }

   public void write(FriendlyByteBuf buf) {
      T val = (T)this.get();
      ResourceLocation key = val != null ? this.registry.getKey(val) : null;
      buf.writeBoolean(key != null);
      if (key != null) {
         buf.writeResourceLocation(key);
      }
   }

   public void read(FriendlyByteBuf buf) {
      if (buf.readBoolean()) {
         this.set(this.registry.getValue(buf.readResourceLocation()));
      } else {
         this.set(null);
      }
   }
}
