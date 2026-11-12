package com.storyteam.storytelling.script.story.npc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Method;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import org.lytharalab.gfbs.gltf.api.io.GltfLoader;
import org.lytharalab.gfbs.gltf.api.model.GltfAsset;

/** Confirms that portraits include a skinned head and a complete unskinned Head group. */
public final class NpcPortraitSmokeTest {
   public static void main(String[] args) throws Exception {
      check(Path.of(args[0]), 1);
      check(Path.of(args[1]), 2);
   }

   private static void check(Path file, int minimumMeshes) throws Exception {
      var asset = GltfLoader.load(new ResourceLocation("storytelling", "models/" + file.getFileName()),
         location -> Files.newInputStream(file));
      Class<?> portraits = Class.forName("com.storyteam.storytelling.script.story.npc.NpcPortraits");
      Method select = portraits.getDeclaredMethod("headGeometry", GltfAsset.class, String.class, long.class);
      select.setAccessible(true);
      Object head = select.invoke(null, asset, file.getFileName().toString(), 1L);
      if (head == null) throw new AssertionError("No head geometry in " + file);
      long selected = (int) value(head, "selectedMeshes");
      if (selected < minimumMeshes) {
         throw new AssertionError("Head group is incomplete in " + file + ": " + selected + " meshes");
      }
      if (!(coordinate(head, "maxX") > coordinate(head, "minX")
         && coordinate(head, "maxY") > coordinate(head, "minY"))) {
         throw new AssertionError("Invalid head bounds in " + file);
      }
      Method rasterize = portraits.getDeclaredMethod("rasterize", head.getClass());
      rasterize.setAccessible(true);
      int opaque = 0;
      try (NativeImage image = (NativeImage) rasterize.invoke(null, head)) {
         if (image == null) throw new AssertionError("Empty portrait in " + file);
         for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
               if ((image.getPixelRGBA(x, y) >>> 24) != 0) opaque++;
            }
         }
      }
      if (opaque < 100) throw new AssertionError("Portrait is nearly blank in " + file + ": " + opaque);
      System.out.println(file.getFileName() + ": head meshes=" + selected + ", opaque pixels=" + opaque);
   }

   private static float coordinate(Object head, String name) throws Exception {
      return (float) value(head, name);
   }

   private static Object value(Object head, String name) throws Exception {
      Method method = head.getClass().getDeclaredMethod(name);
      method.setAccessible(true);
      return method.invoke(head);
   }
}
