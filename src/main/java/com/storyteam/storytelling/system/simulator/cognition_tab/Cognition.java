package com.storyteam.storytelling.system.simulator.cognition_tab;

import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public record Cognition(
   String name, String title, String description, Set<Cognition> dependencies, Set<Cognition> allDependencies, Map<Integer, CognitionTierInfo> levels
) {
   public static final Cognition EMPTY = new Cognition("dev_null", "", "", new HashSet<>(), new HashSet<>(), new HashMap<>());

   public boolean isEmpty() {
      return this == EMPTY;
   }

   public ResourceLocation id() {
      return CognitionSystem.INSTANCE.id(this.name);
   }

   public Cognition.Pos pos() {
      return CognitionPoseConfig.getPos(this.id());
   }

   public ResourceLocation icon() {
      return CognitionSystem.INSTANCE.guiTex("icons/" + this.name);
   }

   public static Cognition.Builder builder(String name) {
      return new Cognition.Builder(name);
   }

   public static class Builder {
      private final String name;
      private String title = "";
      private String description = "";
      private final Set<Cognition> dependencies = new HashSet<>();
      private final Map<Integer, CognitionTierInfo> levels = new HashMap<>();

      private Builder(String name) {
         this.name = name;
      }

      public Cognition.Builder title(String title) {
         this.title = title;
         return this;
      }

      public Cognition.Builder description(String description) {
         this.description = description;
         return this;
      }

      public Cognition.Builder dependencies(Cognition... dependencies) {
         this.dependencies.addAll(Arrays.asList(dependencies));
         return this;
      }

      public Cognition.Builder level(int tier, CognitionTierInfo upgrade) {
         this.levels.put(tier, upgrade);
         return this;
      }

      private Set<Cognition> handleDependencies(Set<Cognition> dependencies) {
         Set<Cognition> deps = new HashSet<>();

         for (Cognition depRes : dependencies) {
            deps.add(depRes);

            for (Cognition depDep : depRes.dependencies()) {
               deps.addAll(this.handleDependencies(depDep.dependencies()));
            }
         }

         return deps;
      }

      public Cognition build() {
         Set<Cognition> allDeps = this.handleDependencies(this.dependencies);
         Cognition cognition = new Cognition(this.name, this.title, this.description, this.dependencies, allDeps, this.levels);
         CognitionHolder.INSTANCE.register(cognition);
         return cognition;
      }
   }

   public static record Pos(float x, float y) {
   }
}
