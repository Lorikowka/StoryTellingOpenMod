package com.storyteam.storytelling.script.story;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

/** Loads author modules from one story directory without exposing LuaJ's package loader. */
final class LuaStoryModules extends OneArgFunction {
   private static final int MAX_MODULES = 64;
   private static final long MAX_BYTES = 256 * 1024;
   private static final String MODULE_NAME = "[A-Za-z_][A-Za-z0-9_]*(\\.[A-Za-z_][A-Za-z0-9_]*)*";

   private final Path root;
   private final Globals globals;
   private final Map<String, LuaValue> loaded = new HashMap<>();
   private final Set<String> loading = new HashSet<>();

   LuaStoryModules(Path root, Globals globals) {
      try {
         this.root = root.toRealPath();
      } catch (IOException e) {
         throw new IllegalArgumentException("Cannot open Lua module directory: " + root, e);
      }
      this.globals = globals;
   }

   @Override
   public LuaValue call(LuaValue argument) {
      String name = argument.checkjstring();
      if (!name.matches(MODULE_NAME)) {
         throw new IllegalArgumentException("Invalid Lua module name: " + name);
      }
      LuaValue cached = loaded.get(name);
      if (cached != null) return cached;
      if (loading.contains(name)) throw new IllegalArgumentException("Cyclic Lua module import: " + name);
      if (loaded.size() + loading.size() >= MAX_MODULES) {
         throw new IllegalArgumentException("Too many Lua modules (maximum " + MAX_MODULES + ")");
      }

      Path candidate = root.resolve(name.replace('.', '/') + ".lua").normalize();
      if (!candidate.startsWith(root)) throw new IllegalArgumentException("Lua module escapes story directory: " + name);
      try {
         Path file = candidate.toRealPath();
         if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Lua module escapes story directory: " + name);
         }
         if (Files.size(file) > MAX_BYTES) {
            throw new IllegalArgumentException("Lua module exceeds " + MAX_BYTES + " bytes: " + name);
         }
         loading.add(name);
         try {
            LuaValue result = globals.load(Files.readString(file, StandardCharsets.UTF_8), file.toString()).call();
            if (result.isnil()) result = LuaValue.TRUE;
            loaded.put(name, result);
            return result;
         } finally {
            loading.remove(name);
         }
      } catch (IOException e) {
         throw new IllegalArgumentException("Cannot read Lua module " + name + " inside " + root, e);
      }
   }
}

