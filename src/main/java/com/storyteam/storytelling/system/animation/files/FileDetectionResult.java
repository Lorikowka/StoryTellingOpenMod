package com.storyteam.storytelling.system.animation.files;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public record FileDetectionResult(Set<Path> usedPaths, Set<Path> unusedPaths) {
   public static final FileDetectionResult EMPTY = new FileDetectionResult(Set.of(), Set.of());

   public void addFrom(FileDetectionResult result) {
      this.usedPaths().addAll(result.usedPaths());
      this.unusedPaths().addAll(result.unusedPaths());
   }

   public FileDetectionResult joinIntoMutable(FileDetectionResult other) {
      FileDetectionResult res = mutable();
      res.addFrom(this);
      res.addFrom(other);
      return res;
   }

   public FileDetectionResult bake() {
      Set<Path> unusedPaths = new HashSet<>(this.unusedPaths());
      unusedPaths.removeAll(this.usedPaths);
      return new FileDetectionResult(Set.copyOf(this.usedPaths), Set.copyOf(unusedPaths));
   }

   public static FileDetectionResult mutable() {
      return new FileDetectionResult(new HashSet<>(), new HashSet<>());
   }
}
