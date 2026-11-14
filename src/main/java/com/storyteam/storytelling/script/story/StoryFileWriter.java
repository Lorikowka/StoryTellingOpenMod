package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Keeps the previous author file and never exposes a partially written replacement. */
public final class StoryFileWriter {
   private StoryFileWriter() { }

   public static void write(Path target, String content, boolean backup) throws IOException {
      Path directory = target.getParent().toRealPath();
      if (!target.normalize().getParent().equals(directory))
         throw new IOException("File must be directly inside the story directory");
      if (Files.exists(target, LinkOption.NOFOLLOW_LINKS) && Files.isSymbolicLink(target))
         throw new IOException("Symbolic links are not allowed for story files");
      Path temp = Files.createTempFile(directory, ".storytelling-", ".tmp");
      try {
         Files.writeString(temp, content, StandardCharsets.UTF_8);
         if (backup && Files.exists(target)) {
            Path previous = directory.resolve(target.getFileName().toString() + ".bak");
            Files.copy(target, previous, StandardCopyOption.REPLACE_EXISTING);
         }
         try { Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
         catch (AtomicMoveNotSupportedException ignored) { Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING); }
      } finally { Files.deleteIfExists(temp); }
   }
}
