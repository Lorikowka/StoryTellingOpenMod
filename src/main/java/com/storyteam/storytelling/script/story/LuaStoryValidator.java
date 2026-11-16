package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Reads author scripts without registering or starting them in the current world. */
public final class LuaStoryValidator {
   private LuaStoryValidator() { }

   public static Report validate(Path directory, Set<Integer> javaScriptIds) throws IOException {
      if (!Files.isDirectory(directory)) throw new IOException("Lua script directory does not exist: " + directory);
      List<Issue> issues = new ArrayList<>();
      Map<Integer, Path> ids = new HashMap<>();
      int checked = 0;
      StoryPackCatalog.Scan catalog = StoryPackCatalog.scan(directory);
      catalog.issues().forEach(issue -> issues.add(new Issue(issue.file(), issue.message())));
      for (Path file : catalog.files()) {
         checked++;
         try {
            LuaStory story = LuaStory.load(LuaStoryPaths.checkedStoryFile(file.getParent(), file));
            String name = file.getFileName().toString();
            String expected = story.id() + ".lua";
            if (!name.equals(expected)) {
               issues.add(new Issue(name, "story(" + story.id() + ") requires filename " + expected));
            }
            if (javaScriptIds.contains(story.id())) {
               issues.add(new Issue(name, "ID " + story.id() + " is already used by a Java script"));
            }
            Path earlier = ids.putIfAbsent(story.id(), file);
            if (earlier != null) {
               issues.add(new Issue(name, "ID " + story.id() + " is also declared in " + earlier.getFileName()));
            }
         } catch (IOException | RuntimeException error) {
            String message = error.getMessage();
            String prefix = "Invalid Lua story " + file + ": ";
            if (message != null && message.startsWith(prefix)) message = message.substring(prefix.length());
            issues.add(new Issue(directory.relativize(file).toString(), message == null ? error.toString() : message));
         }
      }
      return new Report(checked, List.copyOf(issues));
   }

   public record Issue(String file, String message) { }
   public record Report(int checked, List<Issue> issues) {
      public boolean valid() { return issues.isEmpty(); }
   }
}

