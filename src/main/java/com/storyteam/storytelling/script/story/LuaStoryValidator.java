package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/** Reads author scripts without registering or starting them in the current world. */
public final class LuaStoryValidator {
   private LuaStoryValidator() { }

   public static Report validate(Path directory, Set<Integer> javaScriptIds) throws IOException {
      if (!Files.isDirectory(directory)) throw new IOException("Lua script directory does not exist: " + directory);
      List<Issue> issues = new ArrayList<>();
      Map<Integer, Path> ids = new HashMap<>();
      int checked = 0;
      try (Stream<Path> files = Files.list(directory)) {
         for (Path file : files.filter(Files::isRegularFile)
            .filter(path -> path.getFileName().toString().endsWith(".lua")).sorted().toList()) {
            checked++;
            try {
               LuaStory story = LuaStory.load(file);
               String name = file.getFileName().toString();
               if (name.matches("[0-9]+\\.lua")) {
                  String expected = story.id() + ".lua";
                  if (!name.equals(expected)) {
                     issues.add(new Issue(name, "story(" + story.id() + ") requires filename " + expected));
                  }
                  if (javaScriptIds.contains(story.id())) {
                     issues.add(new Issue(name, "ID " + story.id() + " is already used by a Java script"));
                  }
               }
               Path earlier = ids.putIfAbsent(story.id(), file);
               if (earlier != null) {
                  issues.add(new Issue(name, "ID " + story.id() + " is also declared in " + earlier.getFileName()));
               }
            } catch (RuntimeException error) {
               String message = error.getMessage();
               String prefix = "Invalid Lua story " + file + ": ";
               if (message != null && message.startsWith(prefix)) message = message.substring(prefix.length());
               issues.add(new Issue(file.getFileName().toString(), message == null ? error.toString() : message));
            }
         }
      }
      return new Report(checked, List.copyOf(issues));
   }

   public record Issue(String file, String message) { }
   public record Report(int checked, List<Issue> issues) {
      public boolean valid() { return issues.isEmpty(); }
   }
}
