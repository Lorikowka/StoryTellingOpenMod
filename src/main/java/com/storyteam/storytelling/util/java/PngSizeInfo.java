package com.storyteam.storytelling.util.java;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.util.mcf.RunnableReloader;

@EventBusSubscriber({Dist.CLIENT})
public final class PngSizeInfo {
   private static final Map<ResourceLocation, PngSizeInfo> CACHE = new HashMap<>();
   private final int pngWidth;
   private final int pngHeight;

   public PngSizeInfo(int pngWidth, int pngHeight) {
      this.pngWidth = pngWidth;
      this.pngHeight = pngHeight;
   }

   public PngSizeInfo(InputStream stream) throws IOException {
      try (DataInputStream in = new DataInputStream(stream)) {
         if (in.readLong() != -8552249625308161526L) {
            throw new IOException("Bad PNG Signature");
         }

         if (in.readInt() != 13) {
            throw new IOException("Bad length for IHDR chunk!");
         }

         if (in.readInt() != 1229472850) {
            throw new IOException("Bad type for IHDR chunk!");
         }

         this.pngWidth = in.readInt();
         this.pngHeight = in.readInt();
      }
   }

   public static PngSizeInfo get(ResourceLocation resource) throws IOException {
      PngSizeInfo inf = CACHE.get(resource);
      if (inf != null) {
         return inf;
      } else {
         try (InputStream input = ((Resource)Minecraft.getInstance().getResourceManager().getResource(resource).orElseThrow(FileNotFoundException::new)).open()) {
            inf = new PngSizeInfo(input);
         }

         CACHE.put(resource, inf);
         return inf;
      }
   }

   private static void resetCache() {
      CACHE.clear();
   }

   @SubscribeEvent
   public static void reloadListeners(AddReloadListenerEvent e) {
      e.addListener(RunnableReloader.of(PngSizeInfo::resetCache));
   }

   @Generated
   public int getPngWidth() {
      return this.pngWidth;
   }

   @Generated
   public int getPngHeight() {
      return this.pngHeight;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PngSizeInfo other)) {
         return false;
      } else {
         return this.getPngWidth() != other.getPngWidth() ? false : this.getPngHeight() == other.getPngHeight();
      }
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + this.getPngWidth();
      return result * 59 + this.getPngHeight();
   }

   @Generated
   @Override
   public String toString() {
      return "PngSizeInfo(pngWidth=" + this.getPngWidth() + ", pngHeight=" + this.getPngHeight() + ")";
   }
}
