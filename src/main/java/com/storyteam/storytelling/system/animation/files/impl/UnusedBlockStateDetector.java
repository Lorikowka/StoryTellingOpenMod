package com.storyteam.storytelling.system.animation.files.impl;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.animation.files.DetectUnusedFiles;
import com.storyteam.storytelling.system.animation.files.FileDetectionResult;
import com.storyteam.storytelling.system.animation.files.IUnusedFileDetector;
import com.storyteam.storytelling.system.animation.files.InDirFileDetector;
import com.storyteam.storytelling.system.animation.files.ResourceType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

@DetectUnusedFiles(ResourceType.ASSETS)
public class UnusedBlockStateDetector extends InDirFileDetector {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(UnusedBlockStateDetector.class);

   @Override
   protected Path scope(ResourceType type, Path rootDir, KeyMap context) {
      return rootDir.resolve("storytelling").resolve("blockstates");
   }

   @Override
   protected void findUsedFiles(ResourceType type, Path rootDir, Path scopedDir, KeyMap context, final Set<Path> used) throws IOException {
      ModelVisitor visitor = new ModelVisitor(new Path[]{rootDir}) {
         @Override
         protected void onPathAccessed(Path path) {
            used.add(path);
         }
      };
      Iterator<Entry<ResourceLocation, Block>> it = IUnusedFileDetector.<Block>stObjects(BuiltInRegistries.BLOCK).iterator();

      while (it.hasNext()) {
         Entry<ResourceLocation, Block> e = it.next();
         Block blk = e.getValue();
         ResourceLocation path = e.getKey();
         if (!blk.getStateDefinition().getPossibleStates().stream().noneMatch(b -> b.getRenderShape() == RenderShape.MODEL)) {
            visitor.visitBlockState(path);
         }
      }
   }

   @Override
   protected void processUnusedFiles(
      ResourceType type, Path rootDir, Path scopedDir, KeyMap context, Set<Path> usedFiles, List<Path> gatheredUnused, final FileDetectionResult res
   ) throws IOException {
      ModelVisitor visitor = new ModelVisitor(new Path[]{rootDir}) {
         @Override
         protected void onPathAccessed(Path path) {
            res.unusedPaths().add(path);
         }
      };

      for (Path path : gatheredUnused) {
         visitor.visitBlockStateVariantRaw(
            St.id(scopedDir.relativize(path).toString().toLowerCase(Locale.ROOT).replace(File.separatorChar, '/')),
            (JSONObject)new JSONTokener(Files.readString(path)).nextValue()
         );
      }

      super.processUnusedFiles(type, rootDir, scopedDir, context, usedFiles, gatheredUnused, res);
   }
}
