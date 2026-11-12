package com.storyteam.storytelling.mixins._client.iris;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import net.irisshaders.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Iris.class})
public abstract class IrisMixin {
   @Inject(
      method = {"isValidToShowPack"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void isValidToShowPack(Path pack, CallbackInfoReturnable<Boolean> cir) {
      if (!Files.isDirectory(pack) && pack.toString().endsWith(".txt")) {
         try {
            String firstLine = Files.newBufferedReader(pack).readLine();
            if (!firstLine.startsWith("shaderpack: ")) {
               return;
            }

            Path shaderPack = pack.getParent().resolve(firstLine.split("shaderpack: ", 2)[1]);
            cir.setReturnValue(Files.isDirectory(shaderPack) || shaderPack.toString().endsWith(".zip"));
         } catch (Exception var4) {
         }
      }
   }

   @Inject(
      method = {"loadExternalShaderpack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/irisshaders/iris/Iris;isValidShaderpack(Ljava/nio/file/Path;)Z",
         shift = Shift.BEFORE
      )},
      remap = false
   )
   private static void loadExternalShaderpack(
      String name,
      CallbackInfoReturnable<Boolean> cir,
      @Local(name = {"shaderPackRoot"}) LocalRef<Path> shaderPackRoot,
      @Local(name = {"shaderPackConfigTxt"}) LocalRef<Path> shaderPackConfigTxt
   ) {
      try {
         if (!name.endsWith(".txt")) {
            return;
         }

         String line = Files.newBufferedReader((Path)shaderPackRoot.get()).readLine();
         if (!line.startsWith("shaderpack: ")) {
            cir.setReturnValue(false);
         }

         shaderPackConfigTxt.set((Path)shaderPackRoot.get());
         shaderPackRoot.set(((Path)shaderPackRoot.get()).getParent().resolve(line.split("shaderpack: ", 2)[1]));
      } catch (Exception var5) {
      }
   }

   @Inject(
      method = {"tryReadConfigProperties"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/Properties;load(Ljava/io/InputStream;)V"
      )},
      remap = false
   )
   private static void tryReadConfigProperties(Path path, CallbackInfoReturnable<Optional<Properties>> cir, @Local InputStream is) {
      try {
         String firstLine = Files.newBufferedReader(path).readLine();
         if (firstLine.startsWith("shaderpack: ")) {
            is.skip((long)(firstLine.length() + 1));
         }
      } catch (Exception var4) {
      }
   }

   @Overwrite(
      remap = false
   )
   private static void tryUpdateConfigPropertiesFile(Path path, Properties properties) {
      try {
         String firstLine = Files.newBufferedReader(path).readLine();
         OutputStream out = Files.newOutputStream(path);
         if (firstLine != null && firstLine.startsWith("shaderpack: ")) {
            out.write((firstLine + "\n").getBytes());
         } else if (properties.isEmpty()) {
            if (Files.exists(path)) {
               Files.delete(path);
            }

            return;
         }

         try {
            properties.store(out, null);
         } catch (Throwable var7) {
            if (out != null) {
               try {
                  out.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (out != null) {
            out.close();
         }
      } catch (IOException var8) {
      }
   }
}
