package com.storyteam.storytelling.system.animation;

import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLPaths;

public class DevWorkspaceAccessor {
   private static Path mainResourceSet() {
      return Path.of(FMLPaths.GAMEDIR.get().toFile().getAbsolutePath(), "../../src/main").normalize();
   }

   public static Path resourcesDir() {
      return mainResourceSet().resolve("resources");
   }

   public static Path javaDir() {
      return mainResourceSet().resolve("java");
   }

   public static Path resolveClassPath(Class<?> type) {
      while (type.getDeclaringClass() != null) {
         type = type.getDeclaringClass();
      }

      return javaDir().resolve("%s.java".formatted(type.getName().replace('.', '/')));
   }
}
