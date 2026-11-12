package com.storyteam.storytelling.system.tribute.client.screen;

import com.mojang.blaze3d.platform.Window;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.currency.type.EidosCurrency;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.system.tribute.TributeSystem;
import com.storyteam.storytelling.system.tribute.client.screen.object.TributeButtonObject;
import com.storyteam.storytelling.system.tribute.data.medallion.MedallionStage;
import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import com.storyteam.storytelling.system.tribute.overlay.MedallionOverlay;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiTextObject;

public class TributeScreen extends Screen {
   private static final float MEDALLION_SCALE = 0.8F;
   private static final float DAY_TEXT_SCALE = 1.25F;
   private static final int BUTTON_OFFSET_Y = 25;
   private static final int DAY_TEXT_OFFSET_Y = 32;
   private static final int MEDALLION_OFFSET_Y = -20;
   protected GuiRootObject root;
   protected GuiTextObject dayText;
   private int cachedMedallionCenterY;
   private TributeScreen.GuiFlyingObject flyingObject;
   private Tween tween;
   private static boolean isButtonHide = true;

   public TributeScreen() {
      super(Component.empty());
   }

   public GuiRootObject createRoot() {
      GuiRootObject root = GuiObject.root();
      Minecraft mc = Minecraft.getInstance();
      Window window = mc.getWindow();
      int width = window.getGuiScaledWidth();
      int height = window.getGuiScaledHeight();
      GuiObject content = GuiObject.create("content").empty().size((float)width, (float)height);
      this.createContent(content);
      root.add(content);
      return root;
   }

   private void createContent(GuiObject parent) {
      this.layoutContent(parent);
   }

   private void createDay(GuiObject parent, int width, int y) {
      int offerDay = TributeOfferStorage.getOfferDay();
      int currentDay = TimeController.get().getCurrentDay();
      int remainingDay = Math.max(offerDay - currentDay, 0);
      GuiTextObject day = GuiTextObject.create("day")
         .text(Component.literal(String.valueOf(remainingDay)).withStyle(Style.EMPTY.withColor(8905722)))
         .setColor(8905722)
         .setShadow(false);
      day.centered(width, this.height).offset(0.0F, (float)y).scale(1.25F);
      parent.removeChild("day");
      parent.addChild(day);
      this.dayText = day;
   }

   private void layoutContent(GuiObject parent) {
      Window window = Minecraft.getInstance().getWindow();
      int width = (int)parent.getUnscaledWidth();
      int height = window.getGuiScaledHeight();
      int medallionSize = 216;
      int medallionHeight = (int)((float)medallionSize * 0.8F);
      int medallionCenterYAddition = isButtonHide ? 20 : 0;
      int medallionCenterY = height / 2 + -20 + medallionCenterYAddition;
      this.cachedMedallionCenterY = medallionCenterY;
      this.createDay(parent, width, 32 + medallionCenterYAddition);
      int buttonTopY = medallionCenterY + medallionHeight / 2 + 25;
      if (!isButtonHide) {
         this.createButton(parent, buttonTopY, width);
      }
   }

   private void createButton(GuiObject parent, int topY, int width) {
      GuiObject tributeConfirmButton = TributeButtonObject.of("tribute_confirm_button", TributeSystem.guiTx("confirm_offer_button"), button -> {
         TributeOffer tributeOffer = TributeOfferStorage.getOffer();
         if (tributeOffer != null && !tributeOffer.isEmpty()) {
            int offerDay = TributeOfferStorage.getOfferDay();
            int currentDay = TimeController.get().getCurrentDay();
            if (currentDay >= offerDay) {
               int requiredCount = tributeOffer.getRequiredCount();
               int currentCount = CurrencyStorage.getEidos();
               if (currentCount >= requiredCount) {
                  CurrencyStorage.removeEidos(requiredCount);
                  TributeOfferStorage.setNextOffer();
                  if (TributeOfferStorage.getOffer().isEmpty()) {
                     Minecraft.getInstance().setScreen(null);
                  }
               }
            }
         }
      }).size(157.0F, 32.0F).centered(width, 0).offset(0.0F, (float)topY);
      parent.removeChild("tribute_confirm_button");
      parent.addChild(tributeConfirmButton);
   }

   protected void init() {
      super.init();
      this.root = (GuiRootObject)this.addRenderableWidget(this.createRoot());
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
      this.setFocused(this.root);
      this.renderBackground(guiGraphics);
      int centerX = this.width / 2;
      MedallionOverlay.renderMedallion(MedallionStage.TextureSize.LARGE, guiGraphics, centerX, this.cachedMedallionCenterY, 0.8F);
      if (this.tween != null && this.tween.isRunning()) {
         this.flyingObject.render(guiGraphics);
      }

      super.render(guiGraphics, mouseX, mouseY, partialTicks);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private static class GuiFlyingObject {
      private float x;
      private float y;
      private float scale;

      GuiFlyingObject(float x, float y, float scale) {
         this.x = x;
         this.y = y;
         this.scale = scale;
      }

      void setX(float x) {
         this.x = x;
      }

      void setY(float y) {
         this.y = y;
      }

      void setScale(float scale) {
         this.scale = scale;
      }

      void render(GuiGraphics guiGraphics) {
         EidosCurrency.renderTexture(guiGraphics, (int)this.x, (int)this.y, (int)this.scale);
      }
   }
}
