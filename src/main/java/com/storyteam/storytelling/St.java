package com.storyteam.storytelling;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.mcf.Resources;

public class St {
   public static ResourceLocation id(String path) {
      return Resources.location("storytelling", path);
   }

   public static ResourceLocation tx(String path) {
      return Resources.location("storytelling", "textures/" + path + ".png");
   }
}
