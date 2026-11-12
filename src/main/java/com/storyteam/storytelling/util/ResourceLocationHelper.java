package com.storyteam.storytelling.util;

import lombok.Generated;
import net.minecraft.resources.ResourceLocation;

public final class ResourceLocationHelper {
   public static ResourceLocation replacePath(ResourceLocation resourceLocation, String target, String replacement) {
      return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath().replace(target, replacement));
   }

   @Generated
   private ResourceLocationHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
