package com.storyteam.storytelling.system.clothes.options;

import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface IVanityTextureProvider {
   IVanityTextureProvider UNIFIED_TEXTURE = (id, slot) -> id.getNamespace() + ":textures/" + id.getPath();
   IVanityTextureProvider SPLIT_TEXTURES = (id, slot) -> id.getNamespace() + ":textures/" + id.getPath() + "/" + slot.getSerializedName();
   IVanityTextureProvider INHERIT_BASE_TEXTURES = (itemId, slot) -> null;

   String provideTexture(ResourceLocation var1, VanitySlot var2);

   static IVanityTextureProvider stable(ResourceLocation texture) {
      return (itemId, slot) -> texture.toString();
   }
}
