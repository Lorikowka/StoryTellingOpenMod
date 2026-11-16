package com.storyteam.storytelling.script.story;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/** Discovers legacy stories and versioned packs using one set of path rules. */
final class StoryPackCatalog {
   private static final String PACK_ID = "[a-z][a-z0-9_-]{0,63}";
   private static final String STORY_FILE = "[1-9][0-9]{0,8}\\.lua";
   private static final long MAX_MANIFEST_BYTES = 64 * 1024;

   private StoryPackCatalog() { }

   static Scan scan(Path scripts) throws IOException {
      List<Path> files = new ArrayList<>();
      List<Issue> issues = new ArrayList<>();
      try (Stream<Path> entries = Files.list(scripts)) {
         for (Path file : entries.filter(path -> path.getFileName().toString().matches("[0-9]+\\.lua")).sorted().toList()) {
            files.add(file);
         }
      }
      Path packs = scripts.resolve("packs");
      if (!Files.exists(packs)) return new Scan(List.copyOf(files), List.copyOf(issues));
      if (!Files.isDirectory(packs) || !packs.toRealPath().startsWith(scripts.toRealPath())) {
         issues.add(new Issue("packs", "Pack directory escapes ST/scripts or is not a directory"));
         return new Scan(List.copyOf(files), List.copyOf(issues));
      }
      try (Stream<Path> entries = Files.list(packs)) {
         for (Path directory : entries.sorted().toList()) {
            String id = directory.getFileName().toString();
            if (!Files.isDirectory(directory)) {
               issues.add(new Issue("packs/" + id, "Expected a pack directory"));
               continue;
            }
            try {
               files.addAll(readPack(packs, directory));
            } catch (IOException | RuntimeException error) {
               issues.add(new Issue("packs/" + id + "/storypack.json", error.getMessage()));
            }
         }
      }
      return new Scan(List.copyOf(files), List.copyOf(issues));
   }

   static Path resolveStoryFile(Path scripts, String name) throws IOException {
      if (name.matches(STORY_FILE)) return LuaStoryPaths.checkedStoryFile(scripts, scripts.resolve(name));
      if (!name.matches("packs/" + PACK_ID + "/" + STORY_FILE)) {
         throw new IllegalArgumentException("Укажите 900030.lua или packs/<id>/900030.lua");
      }
      Scan scan = scan(scripts);
      Path candidate = scripts.resolve(name);
      String packPath = name.substring(0, name.lastIndexOf('/')) + "/storypack.json";
      for (Issue issue : scan.issues()) {
         if (issue.file().equals(packPath)) throw new IllegalArgumentException(issue.message());
      }
      if (scan.files().stream().noneMatch(file -> file.toAbsolutePath().normalize().equals(candidate.toAbsolutePath().normalize()))) {
         throw new IllegalArgumentException("Сценарий не указан в действующем манифесте: " + name);
      }
      return LuaStoryPaths.checkedStoryFile(candidate.getParent(), candidate);
   }

   private static List<Path> readPack(Path packs, Path directory) throws IOException {
      String id = directory.getFileName().toString();
      if (!id.matches(PACK_ID)) throw new IllegalArgumentException("Invalid pack ID: " + id);
      if (!directory.toRealPath().getParent().equals(packs.toRealPath())) {
         throw new IllegalArgumentException("Pack directory escapes ST/scripts: " + id);
      }
      Path manifest = directory.resolve("storypack.json");
      if (!Files.isRegularFile(manifest) || !manifest.toRealPath().getParent().equals(directory.toRealPath())) {
         throw new IllegalArgumentException("Missing or unsafe storypack.json");
      }
      if (Files.size(manifest) > MAX_MANIFEST_BYTES) throw new IllegalArgumentException("Manifest exceeds 64 KiB");
      JsonObject json;
      try (Reader reader = Files.newBufferedReader(manifest)) {
         json = JsonParser.parseReader(reader).getAsJsonObject();
      }
      if (!id.equals(string(json, "id"))) throw new IllegalArgumentException("Manifest id must match directory " + id);
      if (!string(json, "version").matches("[0-9]+\\.[0-9]+\\.[0-9]+")) {
         throw new IllegalArgumentException("version must be major.minor.patch");
      }
      if (!json.has("api") || json.get("api").getAsInt() != 1) {
         throw new IllegalArgumentException("Unsupported Lua API; expected api: 1");
      }
      if (json.has("dependencies") && (!json.get("dependencies").isJsonArray()
         || !json.getAsJsonArray("dependencies").isEmpty())) {
         throw new IllegalArgumentException("Pack dependencies are not supported by API 1");
      }
      JsonArray stories = json.getAsJsonArray("stories");
      if (stories == null || stories.isEmpty()) throw new IllegalArgumentException("stories must list at least one Lua file");
      List<Path> files = new ArrayList<>();
      Set<String> names = new HashSet<>();
      for (JsonElement element : stories) {
         String name = element.getAsString();
         if (!name.matches(STORY_FILE) || !names.add(name)) {
            throw new IllegalArgumentException("Invalid or duplicate story filename: " + name);
         }
         files.add(LuaStoryPaths.checkedStoryFile(directory, directory.resolve(name)));
      }
      return files;
   }

   private static String string(JsonObject json, String key) {
      if (!json.has(key) || !json.get(key).isJsonPrimitive()) throw new IllegalArgumentException("Missing " + key);
      return json.get(key).getAsString();
   }

   record Issue(String file, String message) { }
   record Scan(List<Path> files, List<Issue> issues) { }
}

