package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.RenderUtils;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class GameTipOverlay implements IGuiOverlay {
   public static int ticker;
   private static Component text;
   private static int inTime;
   private static int time;
   private static int outTime;

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (ticker > 1) {
         guiGraphics.pose().pushPose();
         float alpha = this.getAlpha();
         RenderSystem.enableBlend();
         ResourceLocation bgRL = St.id("textures/gui/overlay/screen_title_base.png");
         RenderSystem.setShaderTexture(0, bgRL);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         Minecraft.getInstance().getTextureManager().getTexture(bgRL).bind();
         int BGHEIGHT = 160;
         int centerX = screenWidth / 2;
         int centerY = screenHeight / 4;
         guiGraphics.blit(bgRL, 0, centerY - BGHEIGHT / 2, 0.0F, 0.0F, screenWidth, BGHEIGHT, screenWidth, BGHEIGHT);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         List<FormattedCharSequence> lines = gui.getFont().split(text, (int)((float)screenWidth * 0.8F));
         int maxCurrentLineWidth = 0;

         for (FormattedCharSequence line : lines) {
            maxCurrentLineWidth = Math.max(maxCurrentLineWidth, gui.getFont().width(line));
         }

         int height = 9 * lines.size();
         int x = centerX - maxCurrentLineWidth / 2;
         int y = centerY - height / 2;

         for (int i = 0; i < lines.size(); i++) {
            RenderUtils.drawCenteredString(
               guiGraphics,
               gui.getFont(),
               lines.get(i),
               centerX,
               y + height / 2 - 9 * lines.size() / 2 + 9 * i - 9 / 4,
               new Color(1.0F, 0.99F, 0.9F, alpha).getRGB(),
               false
            );
         }

         RenderSystem.disableBlend();
         guiGraphics.pose().popPose();
      }
   }

   @SubscribeEvent
   public static void tick(PlayerTickEvent event) {
      if (event.phase == Phase.END && event.player.level().isClientSide) {
         ticker = Mth.clamp(ticker - 1, 0, Integer.MAX_VALUE);
      }
   }

   private float getAlpha() {
      int t = inTime + time + outTime - ticker;
      float tick = Mth.lerp(Minecraft.getInstance().getDeltaFrameTime(), (float)t, (float)(t + 1));
      if (tick < (float)inTime && inTime != 0) {
         return tick / (float)inTime;
      } else {
         return t - inTime < time ? 1.0F : 1.0F - (tick - (float)inTime - (float)time) / (float)outTime;
      }
   }

   public static void setTitleOverlay(Component component, int inTime, int time, int outTime) {
      text = component;
      GameTipOverlay.inTime = inTime;
      GameTipOverlay.time = time;
      GameTipOverlay.outTime = outTime;
      ticker = inTime + time + outTime;
   }
}
