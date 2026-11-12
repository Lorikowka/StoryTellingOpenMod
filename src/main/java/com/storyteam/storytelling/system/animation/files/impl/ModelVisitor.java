package com.storyteam.storytelling.system.animation.files.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

class ModelVisitor {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(ModelVisitor.class);
   protected final List<Path> assetsDirs;
   protected final Stack<ResourceLocation> activePaths = new Stack<>();

   public ModelVisitor(Path... assetsDirs) {
      this(List.of(assetsDirs));
   }

   public ModelVisitor(List<Path> assetsDirs) {
      this.assetsDirs = assetsDirs;
   }

   protected void onPathAccessed(Path path) {
   }

   protected final Path touchFile(ResourceLocation path) {
      for (Path assets : this.assetsDirs) {
         Path pth = assets.resolve(path.getNamespace()).resolve(path.getPath());
         if (!Files.notExists(pth)) {
            this.onPathAccessed(pth);
            return pth;
         }
      }

      return null;
   }

   @Nullable
   protected final JSONObject readJson(ResourceLocation path) throws IOException {
      Path pth = this.touchFile(path.withSuffix(".json"));
      return pth == null ? null : (JSONObject)new JSONTokener(Files.readString(pth)).nextValue();
   }

   void visitModelTextures(JSONObject textures) {
      for (String s : textures.keySet()) {
         String v = textures.getString(s);
         if (!v.startsWith("#")) {
            ResourceLocation texture = Resources.location(v + ".png").withPrefix("textures/");
            this.touchFile(texture);
            this.touchFile(texture.withSuffix(".mcmeta"));
         }
      }
   }

   void visitBlockModelRaw(ResourceLocation path, JSONObject model) throws IOException {
      if (model != null) {
         if (this.activePaths.contains(path)) {
            throw new FileSystemLoopException(path.toString());
         } else {
            this.activePaths.push(path);

            try {
               String parentStr = model.optString("parent");
               if (parentStr != null && !parentStr.isBlank()) {
                  ResourceLocation modelPath = Resources.location(parentStr).withPrefix("models/");

                  JSONObject o;
                  try {
                     o = this.readJson(modelPath);
                  } catch (FileNotFoundException var10) {
                     return;
                  }

                  this.visitBlockModelRaw(modelPath, o);
               }

               if (model.has("textures")) {
                  this.visitModelTextures(model.getJSONObject("textures"));
               }
            } finally {
               this.activePaths.pop();
            }
         }
      }
   }

   void visitBlockStateVariantRaw(ResourceLocation path, JSONObject variant) throws IOException {
      if (!variant.has("model")) {
         log.error("Unable to find model in variant map of file {}: {}", path.getPath(), variant);
      } else {
         ResourceLocation modelPath = Resources.location(variant.getString("model"));
         this.visitBlockModelRaw(modelPath, this.readJson(modelPath.withPrefix("models/")));
      }
   }

   void visitBlockStateRaw(ResourceLocation path, JSONObject blockstate) throws IOException {
      if (blockstate != null) {
         if (blockstate.has("multipart")) {
            JSONArray selectors = blockstate.getJSONArray("multipart");

            for (int i = 0; i < selectors.length(); i++) {
               this.applyStateVariant(path, selectors.getJSONObject(i).get("apply"));
            }
         }

         if (blockstate.has("variants")) {
            JSONObject vars = blockstate.getJSONObject("variants");

            for (String v : vars.keySet()) {
               this.applyStateVariant(path, vars.get(v));
            }
         }
      }
   }

   void applyStateVariant(ResourceLocation loc, Object in) throws IOException {
      if (in instanceof JSONArray a) {
         for (int i = 0; i < a.length(); i++) {
            this.visitBlockStateVariantRaw(loc, a.getJSONObject(i));
         }
      } else if (in instanceof JSONObject o) {
         this.visitBlockStateVariantRaw(loc, o);
      } else {
         log.error("Unable to apply state variant {}", in);
      }
   }

   public final void visitBlockState(ResourceLocation path) throws IOException {
      this.visitBlockStateRaw(path, this.readJson(path.withPrefix("blockstates/")));
   }

   public final void visitItemModel(ResourceLocation path) throws IOException {
      this.visitBlockModelRaw(path, this.readJson(path.withPrefix("models/item/")));
   }
}
