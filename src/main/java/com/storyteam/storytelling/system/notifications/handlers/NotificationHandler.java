package com.storyteam.storytelling.system.notifications.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.notifications.data.NotificationData;
import com.storyteam.storytelling.system.notifications.storage.NotificationStorage;
import com.storyteam.storytelling.util.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent.Post;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class NotificationHandler {
   @SubscribeEvent
   public static void onOverlayRender(Post event) {
      if (event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type()) {
         render(event.getGuiGraphics(), event.getPartialTick());
      }
   }

   private static void render(GuiGraphics guiGraphics, float partialTicks) {
      Minecraft MC = Minecraft.getInstance();

      for (NotificationData data : NotificationStorage.notifications) {
         if (data.getProgress() >= 0) {
            float time = !MC.isPaused() && (MC.screen == null || data.getProgress() != 0) ? partialTicks : 0.0F;
            guiGraphics.pose().pushPose();
            int progress = data.getProgress();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, progress > 80 ? 1.0F - (float)(progress - 80) * 0.05F : 1.0F);
            RenderSystem.setShaderTexture(0, data.getIcon());
            guiGraphics.pose().translate(0.0F, -16.0F + ((float)progress + time) * 1.5F, 0.0F);
            guiGraphics.blit(data.getIcon(), 4, 0, 16, 16, 0.0F, 0.0F, 23, 23, 23, 23);
            int colorValue = progress > 80 ? 255 - Math.min(255, (progress - 80) * 5) : 255;
            Color color = new Color(colorValue, colorValue, colorValue, 0);
            guiGraphics.drawString(MC.font, data.getTitle(), 23, 4, color.getRGB());
            guiGraphics.pose().popPose();
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END && event.side == LogicalSide.CLIENT) {
         NotificationStorage.processQueue();
         List<NotificationData> toClear = new ArrayList<>();

         for (NotificationData data : NotificationStorage.notifications) {
            int progress = data.getProgress();
            if (progress >= 100) {
               toClear.add(data);
            } else {
               if (progress == 0) {
                  if (Minecraft.getInstance().screen != null) {
                     continue;
                  }

                  event.player.level().playSound(null, event.player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.MASTER, 1.0F, 2.0F);
               }

               data.setProgress(progress + 1);
            }
         }

         if (!toClear.isEmpty()) {
            NotificationStorage.notifications.removeAll(toClear);
         }
      }
   }
}
