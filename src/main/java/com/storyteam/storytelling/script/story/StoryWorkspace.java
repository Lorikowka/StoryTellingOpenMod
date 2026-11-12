package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import net.minecraftforge.fml.loading.FMLPaths;

/** Files owned by the story author, separate from the mod JAR and world saves. */
public final class StoryWorkspace {
   private StoryWorkspace() { }

   public static Path root() {
      return FMLPaths.GAMEDIR.get().resolve("ST");
   }

   public static Path scripts() {
      return root().resolve("scripts");
   }

   public static Path assets() {
      return root().resolve("assets");
   }

   public static void initialize() throws IOException {
      Files.createDirectories(scripts());
      Files.createDirectories(root().resolve("logs"));
      Files.createDirectories(assets().resolve("models"));
      Files.createDirectories(assets().resolve("textures"));
      // Older versions used config/storytelling/stories. Keep user edits when upgrading.
      Path oldScripts = FMLPaths.CONFIGDIR.get().resolve("storytelling/stories");
      Path migrationMarker = root().resolve(".scripts_migrated");
      if (!Files.exists(migrationMarker) && Files.isDirectory(oldScripts) && !oldScripts.equals(scripts())) {
         try (Stream<Path> files = Files.list(oldScripts)) {
            for (Path source : files.filter(Files::isRegularFile)
               .filter(path -> path.getFileName().toString().matches("[a-zA-Z0-9_-]+\\.lua")).toList()) {
               try {
                  Files.copy(source, scripts().resolve(source.getFileName()));
               } catch (FileAlreadyExistsException ignored) {
                  // The author's version in ST/scripts takes precedence.
               }
            }
         }
      }
      if (!Files.exists(migrationMarker)) {
         Files.createFile(migrationMarker);
      }
   }
}
