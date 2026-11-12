package com.storyteam.storytelling.init;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.Generated;

public class ScriptCategory {
   public static final Map<String, ScriptCategory> SCRIPTS = new LinkedHashMap<>();
   public static final ScriptCategory MECHANICS = register(
      "mechanics",
      new int[]{-100, -101, -103, -104, -105, -106, -109, -110, -111, -112, -113, -1301, -114, -117, -118, -119, -120, -501, -502, -503, -504, -505}
   );
   public static final ScriptCategory DRONES = register("drones", new int[]{-20000, -20001, -20002, -20003, -20004, -20005, -20007, -20008});
   public static final ScriptCategory CHARACTERS = register("characters", new int[]{-3011, -3013});
   public static final ScriptCategory TRAIN_WALKS = register(
      "train", new int[]{-30104, -30401, -30601, -30604, -30606, -30702, -30901, -31004, -30202, -30802, -30302, -30502, -31102, -31202, -31302, -31402}
   );
   int[] scripts;

   public static ScriptCategory register(String category, int[] ids) {
      ScriptCategory scriptCategory = new ScriptCategory(ids);
      SCRIPTS.put(category, scriptCategory);
      return scriptCategory;
   }

   public ScriptCategory exclude(int... values) {
      int[] filteredScripts = Arrays.stream(this.scripts).filter(i -> IntStream.of(values).noneMatch(val -> val == i)).toArray();
      return new ScriptCategory(filteredScripts);
   }

   public ScriptCategory include(int... values) {
      int[] combinedScripts = Arrays.copyOf(this.scripts, this.scripts.length + values.length);
      System.arraycopy(values, 0, combinedScripts, this.scripts.length, values.length);
      return new ScriptCategory(combinedScripts);
   }

   public static ScriptCategory of(int... values) {
      return new ScriptCategory(values);
   }

   public static ScriptCategory all() {
      int[] allScripts = SCRIPTS.values().stream().flatMapToInt(category -> Arrays.stream(category.getScripts())).toArray();
      return new ScriptCategory(allScripts);
   }

   public static ScriptCategory allWithoutMechanics() {
      int[] allScripts = SCRIPTS.entrySet()
         .stream()
         .flatMapToInt(entry -> !entry.getKey().equals("mechanism_new") ? Arrays.stream(entry.getValue().getScripts()) : IntStream.empty())
         .toArray();
      return new ScriptCategory(allScripts);
   }

   @Generated
   public ScriptCategory(int[] scripts) {
      this.scripts = scripts;
   }

   @Generated
   public int[] getScripts() {
      return this.scripts;
   }
}
