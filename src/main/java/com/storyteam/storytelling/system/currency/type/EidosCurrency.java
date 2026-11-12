package com.storyteam.storytelling.system.currency.type;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.content.overlays.BlackoutOverlay;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.system.currency.Currency;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EidosCurrency extends Currency {
   public static final int ICON_SIZE = 18;
   public static final int PADDING = 5;
   public static final int COLOR_NORMAL = 15649534;
   public static final int COLOR_WARNING = 16746118;
   private static final int MARGIN = 10;
   private static final float MAX_TEXT_WIDTH = 80.0F;
   private static boolean isAnimationStarted = false;
   private static AnimatedEntity livingEntity;
   private static boolean isDebug = false;

   public static void renderTexture(GuiGraphics guiGraphics, int x, int y) {
      renderTexture(guiGraphics, x, y, 18);
   }

   public static void renderTexture(GuiGraphics guiGraphics, int x, int y, int scale) {
      if (isDebug) {
         guiGraphics.fill(x, y, x + 4, y + 4, -65536);
      }

      boolean hasBlackout = BlackoutOverlay.getAlpha() > 0.99F;
      if (!hasBlackout) {
         renderEntity(guiGraphics, x + scale / 2, y + scale, (float)scale);
      }
   }

   public static void renderText(
      GuiGraphics guiGraphics, int currentCountRaw, int requiredCountRaw, String currentCount, String requiredCount, float x, float y
   ) {
      renderText(guiGraphics, currentCountRaw, requiredCountRaw, currentCount, requiredCount, x, y, true);
   }

   @OnlyIn(Dist.CLIENT)
   public static void renderText(
      GuiGraphics guiGraphics, int currentCountRaw, int requiredCountRaw, String currentCount, String requiredCount, float x, float y, boolean shadow
   ) {
      boolean hasBlackout = BlackoutOverlay.getAlpha() > 0.99F;
      if (!hasBlackout) {
         Minecraft mc = Minecraft.getInstance();
         Font font = mc.font;
         int currentCountColor = currentCountRaw < requiredCountRaw ? 16746118 : 15649534;
         guiGraphics.drawString(font, currentCount, (int)x, (int)y, currentCountColor, shadow);
         float currentCountWidth = (float)font.width(currentCount);
         guiGraphics.drawString(font, requiredCount, (int)(x + currentCountWidth), (int)y, 15649534, shadow);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private static void renderEntity(GuiGraphics guiGraphics, int x, int y, float scale) {
      Minecraft mc = Minecraft.getInstance();
      if (livingEntity == null) {
         livingEntity = (AnimatedEntity)EntitiesST.EIDOS.create(mc.level);
      }

      if (livingEntity != null) {
         if (!isAnimationStarted) {
            livingEntity.getAnimationSystem().startAnimationAt("action", AnimationsST8.EIDOS_IDLE.configure().important());
            isAnimationStarted = true;
         }

         drawEntity(guiGraphics, x, y, scale);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private static void drawEntity(GuiGraphics gui, int x, int y, float scale) {
      AnimatedEntity entity = livingEntity;
      float entityX = (float)x;
      float entityY = (float)y;
      InventoryScreen.renderEntityInInventoryFollowsAngle(gui, Math.round(entityX), Math.round(entityY), (int)scale, 180.0F, 0.0F, entity);
   }

   @Override
   public String getId() {
      return "eidos";
   }

   @Override
   public String getDisplayName() {
      return "Эйдос";
   }

   @Override
   public String getFormatted(int count) {
      return String.valueOf(count);
   }

   @Override
   public void render(GuiGraphics guiGraphics, float partialTick) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      if (mc.screen == null && mc.player != null && !mc.player.isSpectator()) {
         int screenWidth = mc.getWindow().getGuiScaledWidth();
         int screenHeight = mc.getWindow().getGuiScaledHeight();
         TributeOffer tributeOffer = TributeOfferStorage.getOffer();
         if (tributeOffer != null) {
            int currentCountRaw = CurrencyStorage.getEidos();
            int requiredCountRaw = tributeOffer.getRequiredCount();
            String currentCount = this.getFormatted(currentCountRaw);
            String requiredCount = "/" + this.getFormatted(requiredCountRaw);
            String fullText = currentCount + requiredCount;
            float textWidth = (float)font.width(fullText);
            float scale = 1.0F;
            if (textWidth > 80.0F) {
               scale = 80.0F / textWidth;
               textWidth = 80.0F;
            }

            float totalWidth = 23.0F + textWidth * scale;
            int blockX = (int)((float)screenWidth - totalWidth - 10.0F) - 5;
            int blockY = screenHeight - 18 - 10;
            renderTexture(guiGraphics, blockX, blockY);
            float textX = (float)(blockX + 18 + 5) / scale;
            float textY = ((float)blockY + 9.0F - (float)((int)(9.0F * scale / 2.0F))) / scale;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, 1.0F);
            renderText(guiGraphics, currentCountRaw, requiredCountRaw, currentCount, requiredCount, textX, textY);
            guiGraphics.pose().popPose();
         }
      }
   }

   @Generated
   public static AnimatedEntity getLivingEntity() {
      return livingEntity;
   }
}
