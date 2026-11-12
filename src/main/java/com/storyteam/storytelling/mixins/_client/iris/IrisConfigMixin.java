package com.storyteam.storytelling.mixins._client.iris;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import java.nio.file.Files;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.config.IrisConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({IrisConfig.class})
public class IrisConfigMixin {
   @Inject(
      method = {"setShaderPackName"},
      at = {@At("HEAD")},
      remap = false
   )
   public void setShaderPackName(String name, CallbackInfo ci, @Local(argsOnly = true) LocalRef<String> name1) {
      if (name != null) {
         if (!Files.exists(Iris.getShaderpacksDirectory().resolve(name))) {
            if (Files.exists(Iris.getShaderpacksDirectory().resolve(name + ".txt"))) {
               try {
                  if (Files.newBufferedReader(Iris.getShaderpacksDirectory().resolve(name + ".txt")).readLine().startsWith("shaderpack: ")) {
                     name1.set(name + ".txt");
                     return;
                  }
               } catch (Exception var5) {
               }
            }

            if (Files.exists(Iris.getShaderpacksDirectory().resolve(name + ".zip"))) {
               name1.set(name + ".zip");
            }
         }
      }
   }
}
