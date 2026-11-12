package com.storyteam.storytelling.system.emotions.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.system.emotions.EmotionRenderData;
import com.storyteam.storytelling.system.emotions.RenderHolder;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Matrix4f;
import org.zeith.hammerlib.util.mcf.Resources;

@EventBusSubscriber({Dist.CLIENT})
public class RenderLevelEvent {
   private static IStoryServer storyServer;
   private static final float ICON_SCALE = 0.5F;
   private static final float ICON_WIDTH = 1.0F;
   private static final float ICON_HEIGHT = 0.8F;
   private static final float BUBBLE_WIDTH = 1.5F;
   private static final float BUBBLE_HEIGHT = 1.4F;
   private static final float ICON_X_OFFSET = -0.5F;
   private static final float ICON_VERTICAL_PADDING = 0.5F;
   private static final float ICON_OPACITY = 1.0F;
   public static final int SHOW_TIME = 200;

   @SubscribeEvent
   public static void onTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         if (storyServer == null) {
            IStoryServer tmpStoryServer = IStoryServer.get();
            if (tmpStoryServer == null) {
               return;
            }

            storyServer = tmpStoryServer;
         }

         for (EmotionRenderData emotionRenderData : RenderHolder.getRenderedMap()) {
            emotionRenderData.setTicks(emotionRenderData.getTicks() - 1);
         }

         RenderHolder.getRenderedMap().removeIf(erd -> erd.getTicks() <= 0);
      }
   }

   @SubscribeEvent
   public static void onRenderLevelEvent(RenderLevelStageEvent event) {
      if (event.getStage() == Stage.AFTER_ENTITIES) {
         if (storyServer == null) {
            IStoryServer tmpStoryServer = IStoryServer.get();
            if (tmpStoryServer == null) {
               return;
            }

            storyServer = tmpStoryServer;
         }

         Minecraft mc = Minecraft.getInstance();
         Camera camera = event.getCamera();
         Vec3 cameraPos = camera.getPosition();
         PoseStack poseStack = event.getPoseStack();
         EntityRepository entityRepository = storyServer.getEntityRepository();
         float partialTick = event.getPartialTick();
         HashMap<String, List<EmotionRenderData>> map = new HashMap<>();

         for (EmotionRenderData data : RenderHolder.getRenderedMap()) {
            if (!data.isHiddenOnWorld()) {
               map.computeIfAbsent(data.getUuid(), k -> new ArrayList<>()).add(data);
            }
         }

         for (Entry<String, List<EmotionRenderData>> entry : map.entrySet()) {
            String entityId = entry.getKey();
            if (entityId.equals("player")) {
               if (!mc.options.getCameraType().isFirstPerson()) {
                  renderEmotionIcon(mc.player, entry.getValue(), camera, cameraPos, poseStack, partialTick);
               }
            } else {
               entityRepository.<Entity>optional(entityId)
                  .ifPresent(entity -> renderEmotionIcon(mc.level.getEntity(entity.getId()), entry.getValue(), camera, cameraPos, poseStack, partialTick));
            }
         }
      }
   }

   private static void renderEmotionIcon(Entity entity, List<EmotionRenderData> data, Camera camera, Vec3 cameraPos, PoseStack poseStack, float partialTick) {
      if (entity != null) {
         Vec3 entityPos = new Vec3(
            entity.xOld + (entity.getX() - entity.xOld) * (double)partialTick,
            entity.yOld + (entity.getY() - entity.yOld) * (double)partialTick,
            entity.zOld + (entity.getZ() - entity.zOld) * (double)partialTick
         );
         Vec3 offset = data.get(0).getPosOffset();
         int FADE_TICKS = 20;
         int maxTicks = data.stream().mapToInt(EmotionRenderData::getTicks).max().orElse(0);
         EmotionRenderData mainData = data.get(0);
         if (mainData.getTween() == null && maxTicks > 20) {
            float appearScaleFrom = 0.5F;
            float appearScaleTo = 1.0F;
            float yOffsetFrom = 0.4F;
            float yOffsetTo = 0.0F;
            double durationSec = 1.0;
            Tween appearTween = Tween.create().setTransitionType(TransitionType.ELASTIC).setEase(EaseType.EASE_OUT).setLoops(1);
            appearTween.tweenMethod(x -> mainData.setRenderScale(x), 0.5F, 1.0F, 1.0);
            appearTween.parallel().tweenMethod(y -> mainData.setYOffset(y), 0.4F, 0.0F, 1.0);
            mainData.setRenderScale(0.5F);
            mainData.setYOffset(0.4F);
            appearTween.start();
            mainData.setTween(appearTween);
            mainData.setDisappearing(false);
         }

         if (maxTicks <= 20 && mainData.getTween() != null && !mainData.isDisappearing()) {
            float disappearScaleFrom = 1.0F;
            float disappearScaleTo = 0.5F;
            float yOffsetFrom = 0.0F;
            float yOffsetTo = 0.4F;
            double durationSec = 1.0;
            Tween disappearTween = Tween.create().setTransitionType(TransitionType.ELASTIC).setEase(EaseType.EASE_IN).setLoops(1);
            disappearTween.tweenMethod(x -> mainData.setRenderScale(x), 1.0F, 0.5F, 1.0);
            disappearTween.parallel().tweenMethod(y -> mainData.setYOffset(y), 0.0F, 0.4F, 1.0);
            disappearTween.start();
            mainData.setTween(disappearTween);
            mainData.setDisappearing(true);
         }

         float appearScale = mainData.getRenderScale();
         float yAppearOffset = mainData.getYOffset();
         poseStack.pushPose();
         poseStack.translate(entityPos.x - cameraPos.x, entityPos.y - cameraPos.y, entityPos.z - cameraPos.z);
         poseStack.translate(offset.x, (double)(entity.getBbHeight() + 0.5F) + offset.y - (double)yAppearOffset, offset.z);
         poseStack.mulPose(camera.rotation());
         poseStack.scale(0.5F * appearScale, 0.5F * appearScale, 0.5F * appearScale);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         if (data.isEmpty()) {
            renderTexturedQuad(poseStack, offset.add(-0.25, -0.35F, 0.0), 1.4F, 1.5F, data.get(0).getBubbleType().FULL, 1);
         } else {
            float segmentWidth = 1.1F;
            float bubbleHeight = 1.1F;
            float totalWidth = (float)data.size() * segmentWidth;
            float startX = totalWidth / 2.0F;
            float rightX = startX - (float)data.size() * segmentWidth;
            renderTexturedQuad(poseStack, offset.add((double)rightX, -0.1F, 0.0), bubbleHeight, segmentWidth - 0.25F, data.get(0).getBubbleType().RIGHT, 1);
            float midOffset = 0.25F;
            renderTexturedQuad(
               poseStack,
               offset.add((double)(startX - (float)(data.size() - 1) * segmentWidth - midOffset), -0.1F, 0.0),
               bubbleHeight,
               (float)(data.size() - 1) * segmentWidth + midOffset,
               data.get(0).getBubbleType().MID,
               1
            );
            renderTexturedQuad(
               poseStack,
               offset.add((double)(startX - (float)data.size() / 2.0F * segmentWidth + 0.1F), -0.29F, 0.0),
               0.32F,
               0.576F,
               data.get(0).getBubbleType().TAIL,
               0
            );
            renderTexturedQuad(poseStack, offset.add((double)startX, -0.1F, 0.0), bubbleHeight, segmentWidth - 0.25F, data.get(0).getBubbleType().LEFT, 1);
         }

         for (int i = 0; i < data.size(); i++) {
            EmotionRenderData renderData = data.get(i);
            ResourceLocation textureLocation = Resources.location("storytelling", "textures/system/emotions/" + renderData.getEmotion().getPath() + ".png");
            float totalIconsWidth = (float)data.size() * 1.0F + (float)i * 0.1F;
            float startIconX = totalIconsWidth / 2.0F;
            float iconX = startIconX - (float)i * 1.0F - (float)i * 0.1F - 0.5F;
            renderTexturedQuad(poseStack, offset.add((double)(iconX - 0.15F), 0.05F, 0.0), 0.8F, 1.0F, textureLocation, 0);
         }

         poseStack.popPose();
      }
   }

   private static void renderTexturedQuad(PoseStack poseStack, Vec3 offset, float ICON_HEIGHT, float ICON_WIDTH, ResourceLocation textureLocation, int layer) {
      RenderSystem.setShaderTexture(0, textureLocation);
      BufferBuilder builder = Tesselator.getInstance().getBuilder();
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      float x = (float)(offset.x + -0.5);
      float y = (float)offset.y;
      float z = (float)(offset.z + (double)((float)layer / 1000.0F));
      Matrix4f matrix = poseStack.last().pose();
      builder.vertex(matrix, x, y, z).uv(0.0F, 0.0F).endVertex();
      builder.vertex(matrix, x, y + ICON_HEIGHT, z).uv(0.0F, -1.0F).endVertex();
      builder.vertex(matrix, x + ICON_WIDTH, y + ICON_HEIGHT, z).uv(-1.0F, -1.0F).endVertex();
      builder.vertex(matrix, x + ICON_WIDTH, y, z).uv(-1.0F, 0.0F).endVertex();
      BufferUploader.drawWithShader(builder.end());
   }
}
