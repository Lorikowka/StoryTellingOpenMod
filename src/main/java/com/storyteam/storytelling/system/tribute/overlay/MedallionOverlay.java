package com.storyteam.storytelling.system.tribute.overlay;

import com.mojang.math.Axis;
import com.storyteam.storytelling.system.currency.CurrencySystem;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.tribute.data.medallion.MedallionStage;
import com.storyteam.storytelling.system.tribute.storage.MedallionStageStorage;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class MedallionOverlay implements IGuiOverlay {
   public static final int LARGE_TEXTURE_SIZE = 216;
   public static final int SMALL_TEXTURE_SIZE = 62;
   private static final float OVERLAY_SCALE = 1.0F;
   private static final int OFFSET_Y = 5;
   private static final int ICON_SIZE = 18;
   private static final int PADDING = 15;
   private static final int MARGIN = 10;
   private static final float MAX_TEXT_WIDTH = 80.0F;

   public static void renderMedallion(MedallionStage.TextureSize size, GuiGraphics guiGraphics, int centerX, int centerY, float scale) {
      MedallionStage currentStage = MedallionStageStorage.getStage();
      if (currentStage != null) {
         int textureSize = size == MedallionStage.TextureSize.SMALL ? 62 : 216;
         ResourceLocation wheelTexture = currentStage.getWheelTexture(size);
         ResourceLocation frameTexture = currentStage.getFrameTexture(size);
         long time = TimeController.get().getTime() % 24000L;
         float shifted = (float)((time - 9000L + 24000L) % 24000L);
         float angle = 360.0F - shifted / 24000.0F * 360.0F;
         float scaledSize = (float)textureSize * scale;
         guiGraphics.pose().pushPose();
         guiGraphics.pose().translate((float)centerX, (float)centerY, 0.0F);
         guiGraphics.pose().scale(scale, scale, 1.0F);
         guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(angle));
         guiGraphics.blit(wheelTexture, -textureSize / 2, -textureSize / 2, 0.0F, 0.0F, textureSize, textureSize, textureSize, textureSize);
         guiGraphics.pose().popPose();
         guiGraphics.pose().pushPose();
         guiGraphics.pose().scale(scale, scale, 1.0F);
         guiGraphics.blit(
            frameTexture,
            (int)(((float)centerX - scaledSize / 2.0F) / scale),
            (int)(((float)centerY - scaledSize / 2.0F) / scale),
            0.0F,
            0.0F,
            textureSize,
            textureSize,
            textureSize,
            textureSize
         );
         guiGraphics.pose().popPose();
      }
   }

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      if (mc.screen == null && mc.player != null && !mc.player.isSpectator()) {
         int currentCountRaw = CurrencyStorage.getEidos();
         int requiredCountRaw = TributeOfferStorage.getOffer().getRequiredCount();
         String currentCount = CurrencySystem.EIDOS_CURRENCY.getFormatted(currentCountRaw);
         String requiredCount = "/" + CurrencySystem.EIDOS_CURRENCY.getFormatted(requiredCountRaw);
         String fullText = currentCount + requiredCount;
         float textWidth = (float)font.width(fullText);
         float scale = 1.0F;
         if (textWidth > 80.0F) {
            scale = 80.0F / textWidth;
            textWidth = 80.0F;
         }

         float totalWidth = 33.0F + textWidth * scale;
         int blockX = (int)((float)screenWidth - totalWidth - 10.0F);
         int blockY = screenHeight - 18 - 10;
         int centerX = (int)((float)blockX + totalWidth / 2.0F);
         float scaledSize = 62.0F;
         int centerY = (int)((float)blockY - scaledSize / 2.0F - 5.0F);
         renderMedallion(MedallionStage.TextureSize.SMALL, guiGraphics, centerX, centerY, 1.0F);
      }
   }
}
