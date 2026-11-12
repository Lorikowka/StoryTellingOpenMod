package com.storyteam.storytelling.system.char_icon.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.system.char_icon.IconRenderData;
import com.storyteam.storytelling.system.char_icon.RenderHolder;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class RenderLevelEvent {
   private static IStoryServer storyServer;

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

         Camera camera = event.getCamera();
         Vec3 cameraPos = camera.getPosition();
         PoseStack poseStack = event.getPoseStack();
         Minecraft mc = Minecraft.getInstance();
         ItemRenderer itemRenderer = mc.getItemRenderer();
         BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
         EntityRepository entityRepository = storyServer.getEntityRepository();

         for (IconRenderData iconRenderData : RenderHolder.getRenderedMap()) {
            String regId = iconRenderData.getUuid();
            ItemStack stack = iconRenderData.getItem();
            Vec3 offset = iconRenderData.getPosOffset();
            Optional<Entity> optional = entityRepository.optional(regId);
            optional.ifPresent(entity -> {
               Vec3 pos = entity.position();
               poseStack.pushPose();
               poseStack.translate(pos.x - cameraPos.x, pos.y - cameraPos.y, pos.z - cameraPos.z);
               poseStack.translate(0.0 + offset.x, (double)(entity.getBbHeight() + 0.5F) + offset.y, 0.0 + offset.z);
               poseStack.mulPose(camera.rotation());
               poseStack.scale(0.5F, 0.5F, 0.5F);
               itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
               poseStack.popPose();
            });
         }
      }
   }
}
