package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Applies the same root check to initial registration, validation and single-file reload. */
final class LuaStoryPaths {
   private LuaStoryPaths() { }

   static Path checkedStoryFile(Path directory, Path file) throws IOException {
      Path lexicalRoot = directory.toAbsolutePath().normalize();
      Path lexicalFile = file.toAbsolutePath().normalize();
      if (!lexicalRoot.equals(lexicalFile.getParent())) {
         throw new IllegalArgumentException("Lua story must be directly inside " + directory + ": " + file);
      }
      Path realRoot = lexicalRoot.toRealPath();
      Path realFile = lexicalFile.toRealPath();
      if (!realRoot.equals(realFile.getParent()) || !Files.isRegularFile(realFile)) {
         throw new IllegalArgumentException("Lua story escapes " + directory + ": " + file);
      }
      return realFile;
   }
}
