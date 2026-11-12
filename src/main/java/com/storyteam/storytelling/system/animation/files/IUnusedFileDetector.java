package com.storyteam.storytelling.system.animation.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.hammerlib.abstractions.props.KeyMap;

public interface IUnusedFileDetector {
   void detect(ResourceType var1, Path var2, KeyMap var3, FileDetectionResult var4) throws IOException;

   default int order() {
      return 0;
   }

   default IUnusedFileDetector in(final ResourceType type) {
      final IUnusedFileDetector parent = this;
      return new IUnusedFileDetector() {
         @Override
         public void detect(ResourceType type1, Path rootDir, KeyMap context, FileDetectionResult res) throws IOException {
            if (type == type1) {
               parent.detect(type1, rootDir, context, res);
            }
         }

         @Override
         public int order() {
            return parent.order();
         }
      };
   }

   static <E> Stream<Entry<ResourceLocation, E>> stObjects(Registry<E> registry) {
      return registry.entrySet()
         .stream()
         .filter(e -> ((ResourceKey)e.getKey()).location().getNamespace().equals("storytelling"))
         .map(e -> Map.entry(((ResourceKey)e.getKey()).location(), (E)e.getValue()));
   }

   static <E> Stream<Entry<ResourceLocation, E>> stObjects(IForgeRegistry<E> registry) {
      return registry.getEntries()
         .stream()
         .filter(e -> ((ResourceKey)e.getKey()).location().getNamespace().equals("storytelling"))
         .map(e -> Map.entry(((ResourceKey)e.getKey()).location(), (E)e.getValue()));
   }
}
