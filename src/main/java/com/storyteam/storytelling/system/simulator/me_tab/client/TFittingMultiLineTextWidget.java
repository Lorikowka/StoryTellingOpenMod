package com.storyteam.storytelling.system.simulator.me_tab.client;

import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiFittingMultiLineTextWidget;
import lombok.Generated;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammerlib.client.flowgui.GuiObject;

public class TFittingMultiLineTextWidget extends AbstractScrollWidget {
   private final Font font;
   private MultiLineTextWidgetCopy multilineWidget;
   private boolean scrolling;
   int scroll_color1;
   int scroll_color2;
   boolean useScrollBar = true;
   GuiFittingMultiLineTextWidget guiFittingMultiLineTextWidget;

   public TFittingMultiLineTextWidget(
      int pX, int pY, int pWidth, int pHeight, Component pMessage, Font pFont, GuiFittingMultiLineTextWidget guiFittingMultiLineTextWidget
   ) {
      super(pX, pY, pWidth, pHeight, pMessage);
      this.font = pFont;
      this.multilineWidget = new MultiLineTextWidgetCopy(0, 0, pMessage, pFont, 0).setMaxWidth(this.getWidth());
      this.guiFittingMultiLineTextWidget = guiFittingMultiLineTextWidget;
   }

   public TFittingMultiLineTextWidget setColor(int pColor) {
      this.multilineWidget.setColor(pColor);
      return this;
   }

   public TFittingMultiLineTextWidget setScrollColor(int color1, int color2) {
      if ((color1 & 0xFF000000) == 0) {
         color1 |= -16777216;
      }

      if ((color2 & 0xFF000000) == 0) {
         color2 |= -16777216;
      }

      this.scroll_color1 = color1;
      this.scroll_color2 = color2;
      return this;
   }

   public void setWidth(int pWidth) {
      super.setWidth(pWidth);
      this.multilineWidget.setMaxWidth(this.getWidth());
   }

   public void setHeight(int value) {
      super.setHeight(value);
   }

   protected int getInnerHeight() {
      return this.multilineWidget.getHeight();
   }

   protected double scrollRate() {
      return 9.0;
   }

   public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
      return this.isMouseOver(pMouseX, pMouseY) ? super.mouseScrolled(pMouseX, pMouseY, pDelta) : false;
   }

   protected void setScrollAmount(double pScrollAmount) {
      int max_scroll = this.getMaxScrollAmount();
      double scroll = Mth.clamp(pScrollAmount, 0.0, (double)max_scroll);
      super.setScrollAmount(scroll);
   }

   protected int getMaxScrollAmount() {
      int content_height = this.getContentHeight();
      return Math.max(0, content_height - (this.height - 4));
   }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      if (this.useScrollBar) {
         if (this.visible && this.isFocused() && this.scrolling) {
            if (pMouseY < (double)this.getY()) {
               this.setScrollAmount(0.0);
            } else if (pMouseY > (double)(this.getY() + this.height)) {
               this.setScrollAmount((double)this.getMaxScrollAmount());
            } else {
               int $$5 = this.getScrollBarHeight();
               double $$6 = (double)Math.max(1, this.getMaxScrollAmount() / (this.height - $$5));
               this.setScrollAmount(this.scrollAmount() + pDragY * $$6);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private int getScrollBarHeight() {
      return Mth.clamp((int)((float)(this.height * this.height) / (float)this.getContentHeight()), 8, this.height);
   }

   protected void renderBackground(GuiGraphics pGuiGraphics) {
      if (this.scrollbarVisible()) {
      }
   }

   public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      if (this.visible) {
         if (!this.scrollbarVisible()) {
            this.renderBackground(pGuiGraphics);
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate((float)this.getX(), (float)this.getY(), 0.0F);
            this.multilineWidget.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.pose().popPose();
         } else if (this.visible) {
            this.renderBackground(pGuiGraphics);
            Vector3f vector3f = pGuiGraphics.pose().last().pose().transformPosition(new Vector3f());
            GuiObject obj = this.guiFittingMultiLineTextWidget.getParent();
            Vec3 scale = new Vec3(1.0, 1.0, 1.0);
            if (obj != null) {
               scale = (Vec3)obj.elementScale.get();
            }

            pGuiGraphics.enableScissor(
               (int)vector3f.x - 1,
               (int)vector3f.y,
               (int)((double)vector3f.x + (double)this.width * scale.x()) + 1,
               (int)((double)vector3f.y + (double)this.height * scale.y())
            );
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0, -this.scrollAmount(), 0.0);
            this.renderContents(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.pose().popPose();
            pGuiGraphics.disableScissor();
            this.renderDecorations(pGuiGraphics);
         }
      }
   }

   protected void renderContents(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      pGuiGraphics.pose().pushPose();
      pGuiGraphics.pose().translate((float)this.getX(), (float)this.getY(), 0.0F);
      this.multilineWidget.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      pGuiGraphics.pose().popPose();
   }

   protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
      pNarrationElementOutput.add(NarratedElementType.TITLE, this.getMessage());
   }

   protected void renderDecorations(GuiGraphics pGuiGraphics) {
      if (this.scrollbarVisible() && this.useScrollBar) {
         this.renderScrollBar(pGuiGraphics);
      }
   }

   private void renderScrollBar(GuiGraphics pGuiGraphics) {
      int $$1 = this.getScrollBarHeight();
      int $$2 = this.getX() + this.width;
      int $$3 = this.getX() + this.width + 5;
      int $$4 = Math.max(this.getY(), (int)this.scrollAmount() * (this.height - $$1) / this.getMaxScrollAmount() + this.getY());
      int $$5 = $$4 + $$1;
      pGuiGraphics.fill($$2, $$4, $$3, $$5, this.scroll_color1);
      pGuiGraphics.fill($$2, $$4, $$3 - 1, $$5 - 1, this.scroll_color2);
   }

   private int getContentHeight() {
      return this.getInnerHeight() + 4;
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (this.tmouseClicked(pMouseX, pMouseY, pButton)) {
         this.setFocused(true);
         return true;
      } else {
         return false;
      }
   }

   public boolean tmouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (!this.visible) {
         return false;
      } else {
         boolean $$3 = this.withinContentAreaPoint(pMouseX, pMouseY);
         boolean $$4 = this.scrollbarVisible()
            && pMouseX >= (double)this.width
            && pMouseX <= (double)(this.width + 8)
            && pMouseY >= 0.0
            && pMouseY < (double)this.height;
         if ($$4 && pButton == 0) {
            this.scrolling = true;
            return true;
         } else {
            return $$3 || $$4;
         }
      }
   }

   public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
      if (pButton == 0 && this.scrolling) {
         this.scrolling = false;
         return false;
      } else {
         return false;
      }
   }

   @Generated
   public void setMultilineWidget(MultiLineTextWidgetCopy multilineWidget) {
      this.multilineWidget = multilineWidget;
   }

   @Generated
   public MultiLineTextWidgetCopy getMultilineWidget() {
      return this.multilineWidget;
   }

   @Generated
   public void setUseScrollBar(boolean useScrollBar) {
      this.useScrollBar = useScrollBar;
   }
}
