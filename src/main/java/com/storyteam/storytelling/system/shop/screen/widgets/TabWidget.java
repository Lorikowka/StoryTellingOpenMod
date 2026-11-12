package com.storyteam.storytelling.system.shop.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.shop.data.TabInfo;
import com.storyteam.storytelling.system.shop.screen.ShopScreen;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.interpolation.EaseOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TabWidget extends AbstractWidget {
   private TabInfo tabInfo;
   private ShopScreen screen;

   public TabWidget(int pX, int pY, int pWidth, int pHeight, TabInfo tabInfo, ShopScreen screen) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.tabInfo = tabInfo;
      this.screen = screen;
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int i, int i1, float v) {
      Interpolation interpolation = new EaseOutQuadInterpolation();
      float alpha = interpolation.interpolate(Mth.clamp(this.screen.ticker, 0.0F, 0.5F) * 2.0F);
      PoseStack poseStack = pGuiGraphics.pose();
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/tab.png"));
      int offset = 0;
      if (this.screen.getSelectedTab() == this.tabInfo) {
         RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/tab_on.png"));
         offset++;
      }

      RenderTools.blitWithBlend(
         pGuiGraphics,
         (float)this.getX(),
         (float)(this.getY() + offset),
         0.0F,
         0.0F,
         (float)this.width,
         (float)this.height,
         (float)this.width,
         (float)this.height,
         1.0F
      );
      if (this.tabInfo.getIcon() != null) {
         pGuiGraphics.renderFakeItem(this.tabInfo.getIcon(), this.getX() + 4, this.getY() + 6);
      }
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
         this.screen.setTab(this.tabInfo);
         return true;
      } else {
         return false;
      }
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public TabInfo getTabInfo() {
      return this.tabInfo;
   }

   @Generated
   public void setTabInfo(TabInfo tabInfo) {
      this.tabInfo = tabInfo;
   }

   @Generated
   public ShopScreen getScreen() {
      return this.screen;
   }

   @Generated
   public void setScreen(ShopScreen screen) {
      this.screen = screen;
   }
}
