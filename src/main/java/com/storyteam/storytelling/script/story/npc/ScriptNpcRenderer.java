package com.storyteam.storytelling.script.story.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.story.StoryWorkspace;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.lytharalab.gfbs.gltf.api.animation.AnimationController;
import org.lytharalab.gfbs.gltf.api.animation.LoopMode;
import org.lytharalab.gfbs.gltf.api.animation.PlaybackOptions;
import org.lytharalab.gfbs.gltf.api.client.GltfInstance;
import org.lytharalab.gfbs.gltf.api.client.GltfRenderer;
import org.lytharalab.gfbs.gltf.api.io.GltfLoader;
import org.lytharalab.gfbs.gltf.api.model.GltfAsset;

/** Renders author supplied glTF models. The model and its named clips live in one file. */
public final class ScriptNpcRenderer extends EntityRenderer<ScriptNpcEntity> {
   private static final ResourceLocation UNUSED_TEXTURE = new ResourceLocation("storytelling", "textures/entity/script_npc.png");
   private final Map<String, CachedAsset> assets = new HashMap<>();
   private final Map<String, ModelStamp> failed = new HashMap<>();
   private final Map<ScriptNpcEntity, State> instances = new WeakHashMap<>();

   public ScriptNpcRenderer(EntityRendererProvider.Context context) {
      super(context);
      shadowRadius = 0.35f;
   }

   @Override
   public ResourceLocation getTextureLocation(ScriptNpcEntity entity) {
      return UNUSED_TEXTURE;
   }

   @Override
   public void render(ScriptNpcEntity entity, float yaw, float partialTick, PoseStack pose,
      MultiBufferSource buffers, int packedLight) {
      String model = entity.model();
      if (!model.isEmpty()) {
         GltfAsset asset = getAsset(model);
         if (asset != null) {
            State state = instances.get(entity);
            if (state == null || state.instance.asset() != asset) {
               if (state != null) state.instance.close();
               state = new State(new GltfInstance(asset));
               instances.put(entity, state);
            }
            updateAnimation(entity, state, partialTick);
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(180f - yaw));
            GltfRenderer.render(state.instance, pose, buffers, packedLight, OverlayTexture.NO_OVERLAY);
            pose.popPose();
         }
      }
      super.render(entity, yaw, partialTick, pose, buffers, packedLight);
   }

   private GltfAsset getAsset(String model) {
      ModelStamp stamp;
      try {
         stamp = modelStamp(model);
      } catch (IOException | RuntimeException error) {
         Storytelling.LOG.error("Unable to check story NPC model ST/assets/{}", model, error);
         CachedAsset cached = assets.get(model);
         return cached == null ? null : cached.asset;
      }

      CachedAsset cached = assets.get(model);
      if (cached != null && cached.stamp.equals(stamp)) {
         return cached.asset;
      }
      if (stamp.equals(failed.get(model))) {
         return cached == null ? null : cached.asset;
      }
      try {
         GltfAsset asset = loadModel(model);
         assets.put(model, new CachedAsset(asset, stamp));
         failed.remove(model);
         return asset;
      } catch (IOException | RuntimeException error) {
         failed.put(model, stamp);
         Storytelling.LOG.error("Unable to load story NPC model ST/assets/{}", model, error);
         return cached == null ? null : cached.asset;
      }
   }

   private static ModelStamp modelStamp(String model) throws IOException {
      Path root = StoryWorkspace.assets().toAbsolutePath().normalize();
      Path file = root.resolve(model).normalize();
      if (!file.startsWith(root)) {
         throw new IOException("Model path escapes ST/assets: " + model);
      }
      if (!Files.isRegularFile(file)) {
         return new ModelStamp(false, FileTime.fromMillis(0), 0);
      }
      return new ModelStamp(true, Files.getLastModifiedTime(file), Files.size(file));
   }

   static GltfAsset loadModel(String model) throws IOException {
      if (!model.matches("models/[a-z0-9_/-]+\\.(?:glb|gltf)")) {
         throw new IOException("Invalid model path: " + model);
      }
      ResourceLocation id = new ResourceLocation("storytelling", model);
      Path root = StoryWorkspace.assets().toAbsolutePath().normalize();
      return GltfLoader.load(id, location -> {
         if (!"storytelling".equals(location.getNamespace())) {
            throw new IOException("External namespace in glTF asset: " + location);
         }
         Path file = root.resolve(location.getPath()).normalize();
         if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            throw new IOException("Missing or invalid glTF resource: " + location);
         }
         return Files.newInputStream(file);
      });
   }

   private void updateAnimation(ScriptNpcEntity entity, State state, float partialTick) {
      double time = entity.tickCount + partialTick;
      if (state.lastTime >= 0) {
         state.instance.update(Math.max(0, Math.min((time - state.lastTime) / 20.0, 0.25)));
      }
      state.lastTime = time;
      AnimationController controller = state.instance.animations();
      if ("once".equals(state.playingMode) && !state.onceFinished && !controller.isPlaying()) {
         controller.stopLayer(AnimationController.BASE_LAYER, 0);
         state.onceFinished = true;
      }
      String requested = entity.animation();
      if ("auto".equals(requested)) {
         boolean walking = entity.walking();
         requested = walking && state.instance.asset().animation("walk").isPresent() ? "walk" : "idle";
      }
      String mode = entity.animationMode();
      if (!requested.equals(state.playing) || !mode.equals(state.playingMode)) {
         if (state.instance.asset().animation(requested).isPresent()) {
            PlaybackOptions options = switch (mode) {
               case "once" -> PlaybackOptions.once();
               case "hold" -> new PlaybackOptions(1f, LoopMode.HOLD, 0, 0);
               default -> PlaybackOptions.loop();
            };
            controller.play(requested, options);
            state.playing = requested;
            state.playingMode = mode;
            state.onceFinished = false;
         } else {
            controller.stopLayer(AnimationController.BASE_LAYER, 0);
            state.playing = requested;
            state.playingMode = mode;
            state.onceFinished = false;
         }
      }
   }

   private static final class State {
      final GltfInstance instance;
      String playing = "";
      String playingMode = "";
      boolean onceFinished;
      double lastTime = -1;

      State(GltfInstance instance) {
         this.instance = instance;
      }
   }

   private record CachedAsset(GltfAsset asset, ModelStamp stamp) { }
   private record ModelStamp(boolean present, FileTime modified, long size) { }
}
