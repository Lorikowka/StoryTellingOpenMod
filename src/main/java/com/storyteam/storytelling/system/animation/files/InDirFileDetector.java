package com.storyteam.storytelling.system.animation.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.zeith.hammerlib.abstractions.props.KeyMap;

public abstract class InDirFileDetector implements IUnusedFileDetector {
   protected abstract Path scope(ResourceType var1, Path var2, KeyMap var3) throws IOException;

   protected abstract void findUsedFiles(ResourceType var1, Path var2, Path var3, KeyMap var4, Set<Path> var5) throws IOException;

   protected void processUnusedFiles(
      ResourceType type, Path rootDir, Path scopedDir, KeyMap context, Set<Path> usedFiles, List<Path> gatheredUnused, FileDetectionResult res
   ) throws IOException {
      res.unusedPaths().addAll(gatheredUnused);
      res.usedPaths().addAll(usedFiles);
   }

   @Override
   public void detect(ResourceType type, Path rootDir, KeyMap context, FileDetectionResult res) throws IOException {
      Path scoped = this.scope(type, rootDir, context);
      Set<Path> usedFiles = new HashSet<>();
      this.findUsedFiles(type, rootDir, scoped, context, usedFiles);
      ArrayList<Path> gatheredUnused = new ArrayList<>(
         Files.walk(scoped)
            .filter(x$0 -> Files.isRegularFile(x$0))
            .filter(p -> !usedFiles.contains(p))
            .collect(Collectors.toCollection(HashSet::new))
      );
      this.processUnusedFiles(type, rootDir, scoped, context, usedFiles, gatheredUnused, res);
   }
}
