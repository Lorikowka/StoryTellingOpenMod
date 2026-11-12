package com.storyteam.storytelling.system.animation.backend;

import com.google.common.base.Suppliers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.Since;
import com.storyteam.storytelling.api.animations.SpecialAnim;
import com.storyteam.storytelling.client.AnimationWithMetadata;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.script.story.characters.RenderCharacter;
import com.storyteam.storytelling.script.story.characters.TexturedGeometryContainer;
import com.storyteam.storytelling.system.animation.AnimationsDescription;
import com.storyteam.storytelling.system.animation.DevWorkspaceAccessor;
import com.storyteam.storytelling.system.animation.files.DetectUnusedFiles;
import com.storyteam.storytelling.system.animation.files.FileDetectionResult;
import com.storyteam.storytelling.system.animation.files.IUnusedFileDetector;
import com.storyteam.storytelling.system.animation.files.ResourceType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammeranims.api.HammerAnimationsApi;
import org.zeith.hammeranims.api.animation.AnimationLocation;
import org.zeith.hammeranims.api.animation.IAnimationContainer;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animation.LoopMode;
import org.zeith.hammeranims.api.animation.data.IAnimationData;
import org.zeith.hammeranims.api.animation.event.DecodeAnimationEvent;
import org.zeith.hammeranims.api.event.ReloadHammerAnimationsEvent.EnqueueReloads;
import org.zeith.hammeranims.api.event.ReloadHammerAnimationsEvent.Post;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.event.DecodeGeometryEvent;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;
import org.zeith.hammerlib.util.shaded.json.JSONArray;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

public class AnimationRenderBackend {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(AnimationRenderBackend.class);
   private static AnimationRenderBackend.BackendConfigs configs;
   public static boolean hashesLoaded = false;
   private static final Map<AnimationLocation, AnimationRenderBackend.StringWithHash> ANIM_HASHES = new ConcurrentHashMap<>();
   private static final Map<ResourceLocation, AnimationRenderBackend.StringWithHash> GEO_HASHES = new ConcurrentHashMap<>();
   private static final Supplier<Int2ObjectMap<List<IUnusedFileDetector>>> DETECTORS = Suppliers.memoize(() -> {
      Int2ObjectMap<List<IUnusedFileDetector>> entries = new Int2ObjectOpenHashMap();
      ScanDataHelper.lookupAnnotatedTypes(DetectUnusedFiles.class, IUnusedFileDetector.class).stream().map(c -> {
         DetectUnusedFiles duf = c.getAnnotation(DetectUnusedFiles.class);
         if (duf == null) {
            return null;
         } else {
            try {
               return ((IUnusedFileDetector)c.getDeclaredConstructor().newInstance()).in(duf.value());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var3) {
               throw new RuntimeException(var3);
            }
         }
      }).filter(Objects::nonNull).forEach(det -> ((List)entries.computeIfAbsent(det.order(), io -> new ArrayList())).add(det));
      return entries;
   });
   private static CompletableFuture<?> DUMP_FUTURE;

   private static void decodeAnimation(DecodeAnimationEvent e) {
      String str = e.asString();
      ANIM_HASHES.put(new AnimationLocation(e.container.getRegistryKey(), e.key), new AnimationRenderBackend.StringWithHash(str, Hashers.SHA1.hashify(str)));
   }

   private static void decodeGeometry(DecodeGeometryEvent e) {
      String str = ((JSONObject)e.rootJson.get()).toString();
      GEO_HASHES.put(e.container.getRegistryKey(), new AnimationRenderBackend.StringWithHash(str, Hashers.SHA1.hashify(str)));
   }

   private static void startHLReload(EnqueueReloads e) {
      hashesLoaded = false;
      ANIM_HASHES.clear();
      GEO_HASHES.clear();
   }

   private static void finishHLReload(Post e) {
      hashesLoaded = true;
   }

   public static void initialize(Path backendCfgFile) {
      log.info("Initialize configs from {}", backendCfgFile.toAbsolutePath());
      Gson gson = new GsonBuilder()
         .registerTypeAdapter(Path.class, (JsonDeserializer)(json, typeOfT, context) -> Path.of(json.getAsString()).toAbsolutePath())
         .create();

      try (FileReader fr = new FileReader(backendCfgFile.toFile(), StandardCharsets.UTF_8)) {
         configs = (AnimationRenderBackend.BackendConfigs)gson.fromJson(fr, AnimationRenderBackend.BackendConfigs.class);
         log.info("Loaded: {}", configs);
      } catch (Throwable var7) {
         log.error("Failed to load backend render configs", var7);
      }

      if (configs != null) {
         HammerAnimationsApi.EVENT_BUS.addListener(AnimationRenderBackend::startHLReload);
         HammerAnimationsApi.EVENT_BUS.addListener(EventPriority.HIGHEST, true, AnimationRenderBackend::decodeAnimation);
         HammerAnimationsApi.EVENT_BUS.addListener(EventPriority.HIGHEST, true, AnimationRenderBackend::decodeGeometry);
         HammerAnimationsApi.EVENT_BUS.addListener(AnimationRenderBackend::finishHLReload);
      }
   }

   private static JSONObject createListingInfo(AnimationWithMetadata anim) {
      String key = anim.name();
      IAnimationSource a = anim.animation();
      if (a == Animations.NULL) {
         return null;
      } else {
         JSONObject o = new JSONObject();
         o.put("desc", AnimationsDescription.forAnimation(anim.referenceField()));
         SpecialAnim sa = anim.referenceField().getAnnotation(SpecialAnim.class);
         if (sa != null && !sa.value().isBlank()) {
            if (!RenderCharacterBackend.CHARACTERS.containsKey(sa.value())) {
               STAnimsRenderBackend.LOG.error("Unable to find the render character {}", sa.value());
            } else {
               o.put("char", sa.value());
            }
         } else {
            o.put("char", RenderCharacterBackend.DEFAULT_HUMAN);
         }

         IAnimationData data = a.configure().getAnimation().getData();
         JSONArray tags = new JSONArray();
         if (key.contains("EMOTION") || key.contains("EMOC")) {
            tags.put("эмоция");
         }

         if (key.contains("HUMANOID")) {
            tags.put("действие");
         }

         if (key.contains("HANDS") && !key.contains("NOHANDS")) {
            tags.put("руки");
         }

         if (key.contains("PLACEHOLDER")) {
            tags.put("плейсхолдер");
         }

         if (data.getLengthSeconds() <= 1.0E-7) {
            tags.put("поза");
         } else if (key.contains("CYCLE") || data.getLoopMode() == LoopMode.LOOP) {
            tags.put("цикл");
         }

         Since since = anim.since();
         if (since != null) {
            tags.put(since.value().getHash());
         }

         o.put("tags", tags);
         o.put("length", data.getLengthSeconds());
         return o;
      }
   }

   private static Set<String> gatherUnusedResources(ResourceType subfolder) {
      try {
         Path resources = DevWorkspaceAccessor.resourcesDir().resolve(subfolder.getDir());
         FileDetectionResult globResult = FileDetectionResult.mutable();
         KeyMap ctx = KeyMap.createConcurrentHash();
         Int2ObjectMap<List<IUnusedFileDetector>> dets = DETECTORS.get();

         try {
            for (int group : dets.keySet().intStream().sorted().toArray()) {
               globResult.addFrom(
                  dets.getOrDefault(group, List.of())
                     .stream()
                     .map(det -> CompletableFuture.supplyAsync(() -> {
                           FileDetectionResult res = FileDetectionResult.mutable();

                           try {
                              det.detect(subfolder, resources, ctx, res);
                              return res;
                           } catch (IOException var6) {
                              throw new CompletionException(var6);
                           }
                        }, STAnimsRenderBackend.BACKGROUND_CHECKER))
                     .toList()
                     .stream()
                     .map(CompletableFuture::join)
                     .reduce(FileDetectionResult.EMPTY, FileDetectionResult::joinIntoMutable)
               );
            }
         } catch (CompletionException var9) {
            throw var9.getCause();
         }

         return globResult.bake()
            .unusedPaths()
            .stream()
            .filter(x$0 -> Files.isRegularFile(x$0))
            .map(resources::relativize)
            .map(Path::toString)
            .map(s -> s.replace(File.separatorChar, '/'))
            .sorted()
            .collect(Collectors.toCollection(LinkedHashSet::new));
      } catch (Throwable var10) {
         if (var10 instanceof RuntimeException runtime) throw runtime;
         if (var10 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var10);
      }
   }

   private static void dump() {
      try {
         try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(configs.dump), 65536))) {
            zos.setComment(
               "Dump created on %s.\nContains animations and geometries of ST-%s\n"
                  .formatted(DateTimeFormatter.ISO_INSTANT.format(Instant.now()), Since.Season.CURRENT.getSt())
            );
            Set<IGeometryContainer> geometries = new HashSet<>();
            Set<ResourceLocation> textures = new HashSet<>();
            List<String> problems = new ArrayList<>();
            Map<String, String> hashes = new HashMap<>();
            JSONObject annotations = new JSONObject();
            JSONObject listing = new JSONObject();
            Iterator<AnimationWithMetadata> it = Animations.animations().iterator();

            while (it.hasNext()) {
               AnimationWithMetadata anim = it.next();
               String key = anim.name();
               IAnimationSource a = anim.animation();
               if (a != Animations.NULL) {
                  if (a.configure().getAnimation() == DefaultsHA.NULL_ANIMATION_SYNTETIC) {
                     problems.add("a.configure() is null inside " + a + " [" + key + "]");
                  } else {
                     ResourceLocation registryName = a instanceof IAnimationContainer ctr ? ctr.getRegistryKey() : a.getLocation().container;
                     if (registryName == null) {
                        problems.add("a.getLocation().container is null(?) inside " + a + " [" + key + "]");
                     } else {
                        AnimationRenderBackend.StringWithHash contents = ANIM_HASHES.get(a.getLocation());
                        if (contents == null) {
                           problems.add("Missing animation contents for " + key);
                        } else if (hashes.containsKey(key)) {
                           problems.add("Duplicate animation key: " + key);
                        } else {
                           hashes.put(key, contents.hash());
                           listing.put(key, createListingInfo(anim));
                           JSONObject store = new JSONObject();

                           for (Annotation an : anim.referenceField().getDeclaredAnnotations()) {
                              JSONObject props = new JSONObject();

                              for (Method m : an.annotationType().getDeclaredMethods()) {
                                 if (m.getParameterCount() == 0 && Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers())) {
                                    props.put(m.getName(), m.invoke(an));
                                 }
                              }

                              store.put(an.annotationType().getSimpleName(), props);
                           }

                           if (!store.isEmpty()) {
                              annotations.put(key, store);
                           }

                           zos.putNextEntry(new ZipEntry("animations/" + key + ".json"));
                           zos.write(contents.content().getBytes(StandardCharsets.UTF_8));
                        }
                     }
                  }
               }
            }

            zos.putNextEntry(new ZipEntry("animation-hashes.json"));
            zos.write(new JSONObject(hashes).toString().getBytes(StandardCharsets.UTF_8));
            zos.putNextEntry(new ZipEntry("animation-annotations.json"));
            zos.write(annotations.toString().getBytes(StandardCharsets.UTF_8));
            zos.putNextEntry(new ZipEntry("animation-listing.json"));
            zos.write(listing.toString().getBytes(StandardCharsets.UTF_8));
            zos.putNextEntry(new ZipEntry("unused-resources.json"));
            zos.write(
               new JSONObject()
                  .put("assets", gatherUnusedResources(ResourceType.ASSETS))
                  .put("data", gatherUnusedResources(ResourceType.DATA))
                  .toString()
                  .getBytes(StandardCharsets.UTF_8)
            );

            for (Entry<String, RenderCharacter> ce : RenderCharacterBackend.CHARACTERS.entrySet()) {
               RenderCharacter rc = ce.getValue();

               for (TexturedGeometryContainer model : rc.models()) {
                  textures.add(model.texture());
                  geometries.add(model.model());
               }

               geometries.add(rc.getModel());
               textures.add(rc.getTexture());
               if (rc.getEmissiveTexture() != null) {
                  textures.add(rc.getEmissiveTexture());
               }
            }

            hashes = new HashMap<>();

            for (IGeometryContainer geo : geometries) {
               AnimationRenderBackend.StringWithHash mod = GEO_HASHES.get(geo.getRegistryKey());
               if (mod == null) {
                  problems.add("Unable to find geometry " + geo.getRegistryKey());
               } else {
                  String key = geo.getRegistryKey().toString();
                  hashes.put(key, mod.hash());
                  zos.putNextEntry(new ZipEntry("geo/" + key.replace(':', '/') + ".json"));
                  zos.write(mod.content().getBytes(StandardCharsets.UTF_8));
               }
            }

            zos.putNextEntry(new ZipEntry("geo-hashes.json"));
            zos.write(new JSONObject(hashes).toString().getBytes(StandardCharsets.UTF_8));
            Map<String, String> geoHashes = new HashMap<>(hashes);
            Map<String, String> var29 = new HashMap();

            for (ResourceLocation texture : textures) {
               try (InputStream input = Storytelling.class.getResourceAsStream("/assets/" + texture.getNamespace() + "/" + texture.getPath())) {
                  if (input != null) {
                     byte[] data = input.readAllBytes();
                     var29.put(texture.toString(), Hashers.SHA1.hashify(data));
                     zos.putNextEntry(new ZipEntry("textures/" + texture.toString().replace(':', '/')));
                     zos.write(data);
                  } else {
                     problems.add("Missing texture file: " + texture);
                  }
               }
            }

            zos.putNextEntry(new ZipEntry("texture-hashes.json"));
            zos.write(new JSONObject(var29).toString().getBytes(StandardCharsets.UTF_8));
            Map<String, String> txHashes = new HashMap<>(var29);
            Map<String, String> var30 = new HashMap();

            for (Entry<String, RenderCharacter> ce : RenderCharacterBackend.CHARACTERS.entrySet()) {
               String id = ce.getKey();
               RenderCharacter rc = ce.getValue();
               JSONObject ch = new JSONObject();
               ch.put("id", id);
               ch.put("title", rc.getName());
               JSONArray meshes = new JSONArray();

               for (TexturedGeometryContainer model : rc.models()) {
                  JSONObject mesh = new JSONObject();
                  mesh.put("geo", model.model().getRegistryKey().toString());
                  if (model.texture() != null) {
                     mesh.put("tex", model.texture().toString());
                  }

                  if (model.emissive() != null) {
                     mesh.put("emissive", model.emissive().toString());
                  }

                  meshes.put(mesh);
                  textures.add(model.texture());
                  geometries.add(model.model());
               }

               ch.put("meshes", meshes);
               geometries.add(rc.getModel());
               textures.add(rc.getTexture());
               zos.putNextEntry(new ZipEntry("characters/" + id + ".json"));
               byte[] d = ch.toString().getBytes(StandardCharsets.UTF_8);
               zos.write(d);
               var30.put(
                  id,
                  Hashers.SHA256
                     .hashify(
                        Hashers.SHA1.hashify(d)
                           + "+"
                           + String.join(
                              "+",
                              geometries.stream()
                                 .map(IGeometryContainer::getRegistryKey)
                                 .<Object>map(ResourceLocation::toString)
                                 .sorted()
                                 .map(geoHashes::get)
                                 .filter(Objects::nonNull)
                                 .toList()
                           )
                           + "+"
                           + String.join(
                              "+", textures.stream().<Object>map(ResourceLocation::toString).sorted().map(txHashes::get).filter(Objects::nonNull).toList()
                           )
                     )
               );
            }

            zos.putNextEntry(new ZipEntry("character-hashes.json"));
            zos.write(new JSONObject(var30).toString().getBytes(StandardCharsets.UTF_8));
            if (!problems.isEmpty()) {
               zos.putNextEntry(new ZipEntry("problems.txt"));
               zos.write(String.join("\n", problems).getBytes(StandardCharsets.UTF_8));
            }
         }
      } catch (Throwable var27) {
         if (var27 instanceof RuntimeException runtime) throw runtime;
         if (var27 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var27);
      }
   }

   @SubscribeEvent
   public static void serverStarted(ServerStartedEvent e) {
      log.info("Animation render backend dump started.");
      DUMP_FUTURE = CompletableFuture.runAsync(new AnimationRenderBackend.SafeRunnable("Dump", AnimationRenderBackend::dump), STAnimsRenderBackend.IO_SAVE);
   }

   @SubscribeEvent
   public static void serverStopping(ServerStoppingEvent e) {
      if (DUMP_FUTURE != null && !DUMP_FUTURE.isDone()) {
         log.info("Waiting for render backend dump to finish...");
         DUMP_FUTURE.join();
         log.info("Animation render backend dump finished.");
      }
   }

   public static record BackendCfgDirs(Path images, Path metadata, Path queue, Path renderdata) {
   }

   public static record BackendConfigs(Path dump, AnimationRenderBackend.BackendCfgDirs animations, AnimationRenderBackend.BackendCfgDirs characters) {
   }

   public static record SafeRunnable(String name, Runnable wrap) implements Runnable {
      @Override
      public void run() {
         try {
            this.wrap.run();
         } catch (Throwable var2) {
            AnimationRenderBackend.log.error("Failed to execute task {}", this.name, var2);
         }
      }
   }

   private static record StringWithHash(String content, String hash) {
   }
}
