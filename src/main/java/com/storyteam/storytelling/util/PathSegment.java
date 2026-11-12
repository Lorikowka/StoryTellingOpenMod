package com.storyteam.storytelling.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.zeith.hammerlib.util.java.ArrayHelper;

public record PathSegment(List<String> path, List<PathSegment> children) {
   public static PathSegment create(String... paths) {
      return new PathSegment(Arrays.stream(paths).flatMap(String::lines).filter(s -> !s.isBlank()).toList(), List.of());
   }

   public PathSegment branch(PathSegment... children) {
      return new PathSegment(this.path(), Stream.concat(this.children().stream(), Arrays.stream(children)).toList());
   }

   public String[] generatePath(Random rng) {
      if (this.children.isEmpty()) {
         return this.path.toArray(String[]::new);
      } else {
         String[] childBranch = this.children.get(rng.nextInt(this.children.size())).generatePath(rng);
         return (String[])ArrayHelper.merge(this.path().toArray(String[]::new), childBranch);
      }
   }
}
