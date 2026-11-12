package com.storyteam.storytelling.system._core.cloud.images;

import com.storyteam.storytelling.St;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.client.texture.HttpTextureWithHeaders;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.net.HttpRequest;

public class UrlImageRepository {
   private static final Map<String, CompletableFuture<ResourceLocation>> TEXTURES = new HashMap<>();

   public static CompletableFuture<ResourceLocation> requestImage(String url) {
      throw new IllegalStateException("Storytelling offline build: remote service disabled; use local files and /script commands.");
   }

   private static CompletableFuture<ResourceLocation> uploadTexture(File file, String url, ResourceLocation texture) {
      return Minecraft.getInstance().submit(() -> {
         HttpTextureWithHeaders tex = new HttpTextureWithHeaders(file, url, texture, false, null);
         Minecraft.getInstance().textureManager.register(texture, tex);
         return texture;
      });
   }
}
