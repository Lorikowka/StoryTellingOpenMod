package com.storyteam.storytelling.client.content.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.St;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber({Dist.CLIENT})
public class TitleOverlay implements IGuiOverlay {
   public static int ticker;
   private static String text;
   private static int inTime;
   private static int time;
   private static int outTime;

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (ticker != 0) {
         guiGraphics.pose().pushPose();
         float alpha = this.getAlpha();
         guiGraphics.pose().pushPose();
         RenderSystem.enableBlend();
         ResourceLocation bgRL = St.id("textures/gui/overlay/screen_title_base.png");
         RenderSystem.setShaderTexture(0, bgRL);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         Minecraft.getInstance().getTextureManager().getTexture(bgRL).bind();
         int BGHEIGHT = GL11.glGetTexLevelParameteri(3553, 0, 4097);
         guiGraphics.blit(bgRL, 0, (screenHeight - BGHEIGHT) / 2, 0.0F, 0.0F, screenWidth, BGHEIGHT, screenWidth, BGHEIGHT);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         ResourceLocation textRL = St.id("textures/gui/overlay/" + text + ".png");
         RenderSystem.setShaderTexture(0, textRL);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha * 0.3F);
         Minecraft.getInstance().getTextureManager().getTexture(textRL).bind();
         int TEXTWIDTHBASE = GL11.glGetTexLevelParameteri(3553, 0, 4096);
         int TEXTHEIGHTBASE = GL11.glGetTexLevelParameteri(3553, 0, 4097);
         int TEXTWIDTH1 = (int)Math.round((double)TEXTWIDTHBASE + 30.0 * Math.atan((double)((float)(inTime + time + outTime - ticker - 5) / 20.0F)));
         int TEXTHEIGHT1 = Math.round((float)TEXTHEIGHTBASE);
         guiGraphics.blit(
            textRL, (screenWidth - TEXTWIDTH1) / 2, (screenHeight - TEXTHEIGHT1) / 2, 0.0F, 0.0F, TEXTWIDTH1, TEXTHEIGHT1, TEXTWIDTH1, TEXTHEIGHT1
         );
         RenderSystem.setShaderTexture(0, textRL);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         Minecraft.getInstance().getTextureManager().getTexture(textRL).bind();
         int TEXTWIDTH2 = Math.round((float)(TEXTWIDTHBASE - 20) + (float)(100 - ticker) / 4.0F);
         int TEXTHEIGHT2 = Math.round((float)TEXTHEIGHTBASE);
         guiGraphics.blit(
            textRL, (screenWidth - TEXTWIDTH2) / 2, (screenHeight - TEXTHEIGHT2) / 2, 0.0F, 0.0F, TEXTWIDTH2, TEXTHEIGHT2, TEXTWIDTH2, TEXTHEIGHT2
         );
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

   public static void setTitleOverlay(String textFileName, int inTime, int time, int outTime) {
      text = textFileName;
      TitleOverlay.inTime = inTime;
      TitleOverlay.time = time;
      TitleOverlay.outTime = outTime;
      ticker = inTime + time + outTime;
   }
}
