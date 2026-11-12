package com.storyteam.storytelling.system.shop.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.shop.data.LotData;
import com.storyteam.storytelling.system.shop.screen.ShopScreen;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.interpolation.EaseOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class CountWidget extends AbstractButton {
   private static Minecraft MC = Minecraft.getInstance();
   private ShopScreen screen;
   private int step;

   public CountWidget(int pX, int pY, ShopScreen screen, int step) {
      super(pX, pY, Math.abs(step) == 1 ? 9 : 12, Math.abs(step) == 1 ? 13 : 15, Component.empty());
      this.screen = screen;
      this.step = step;
   }

   public void onPress() {
      if (this.screen.getSelectedLot() != null) {
         LotData data = this.screen.getSelectedLot().getData();
         this.screen.setCount(Mth.clamp(this.screen.getCount() + this.step, 1, Mth.clamp(data.getStock() == -1 ? 99 : data.getStock(), 1, 99)));
      }
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      Interpolation interpolation = new EaseOutQuadInterpolation();
      float alpha = interpolation.interpolate(Mth.clamp(this.screen.ticker, 0.0F, 0.5F) * 2.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/arrow_" + this.step + (this.isHoveredOrFocused() ? "_glow" : "") + ".png"));
      PoseStack poseStack = pGuiGraphics.pose();
      RenderTools.blitWithBlend(
         pGuiGraphics, (float)this.getX(), (float)this.getY(), 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.width, (float)this.height, 1.0F
      );
      String type = (this.step > 0 ? "§a+" : "§c") + "§l" + this.step;
      poseStack.pushPose();
      float renderX = (float)this.getX() + (float)this.width / 2.0F - (float)MC.font.width(type) / 2.0F;
      if (this.step == -5) {
         renderX += 3.0F;
      }

      if (this.step == 5) {
         renderX -= 3.0F;
      }

      poseStack.popPose();
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public ShopScreen getScreen() {
      return this.screen;
   }

   @Generated
   public int getStep() {
      return this.step;
   }
}
