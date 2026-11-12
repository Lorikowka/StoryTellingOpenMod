package com.storyteam.storytelling.script.story;

import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import org.lytharalab.gfbs.gltf.api.io.GltfLoader;

/** Quick decoder check for an actual author supplied glTF model, without starting Minecraft. */
public final class GltfModelSmokeTest {
   public static void main(String[] args) throws Exception {
      Path model = Path.of(args[0]);
      String modelName = model.getFileName().toString();
      var asset = GltfLoader.load(new ResourceLocation("storytelling", "models/" + modelName),
         location -> Files.newInputStream(model));
      if (asset.scenes().isEmpty()) throw new AssertionError("glTF has no scene");
      if (asset.animationNames().isEmpty()) throw new AssertionError("glTF has no animations");
      System.out.println("Loaded glTF animations: " + asset.animationNames());
   }
}
