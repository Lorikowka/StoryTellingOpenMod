package com.storyteam.storytelling.system.simulator.me_tab.client.history;

import com.storyteam.storytelling.system.simulator.me_tab.client.history.button.WordButton;
import com.storyteam.storytelling.util.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HistoryScrollableWidgetList extends AbstractScrollWidget {
   private final List<WordButton> widgets = new ArrayList<>();
   private final int widgetSpacing;
   int paddingX;
   int paddingY;
   Color scroll_main;
   Color scroll_add;
   boolean useScrollBar;

   public HistoryScrollableWidgetList(
      int x, int y, int width, int height, Component message, int widgetSpacing, int paddingX, int paddingY, boolean useScrollBar
   ) {
      super(x, y, width, height, message);
      this.widgetSpacing = widgetSpacing;
      this.paddingX = paddingX;
      this.paddingY = paddingY;
      this.useScrollBar = useScrollBar;
   }

   public void addWidget(WordButton widget) {
      this.widgets.add(widget);
   }

   protected int getInnerHeight() {
      if (this.widgets.isEmpty()) {
         return 0;
      } else {
         int currentRowWidth = 0;
         int currentRowHeight = 0;
         int totalHeight = this.paddingY;

         for (int i = 0; i < this.widgets.size(); i++) {
            WordButton w = this.widgets.get(i);
            int nextWidth = currentRowWidth == 0 ? w.getWidth() : currentRowWidth + this.widgetSpacing + w.getWidth();
            if (nextWidth > this.width) {
               totalHeight += currentRowHeight + this.widgetSpacing;
               currentRowWidth = w.getWidth();
               currentRowHeight = w.getHeight();
            } else {
               currentRowWidth = nextWidth;
               currentRowHeight = Math.max(currentRowHeight, w.getHeight());
            }
         }

         return totalHeight + currentRowHeight;
      }
   }

   protected double scrollRate() {
      return 9.0;
   }

   public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      if (this.visible) {
         this.renderContents(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
         this.renderDecorations(pGuiGraphics);
      }
   }

   protected void renderDecorations(@NotNull GuiGraphics pGuiGraphics) {
      if (this.scrollbarVisible() && this.useScrollBar) {
         this.renderScrollBar(pGuiGraphics);
      }
   }

   private void renderScrollBar(GuiGraphics pGuiGraphics) {
      int barHeight = this.getScrollBarHeight();
      int barLeft = this.getX() + this.width;
      int barRight = this.getX() + this.width + 4;
      int barTop = Math.max(this.getY(), (int)this.scrollAmount() * (this.height - barHeight) / this.getMaxScrollAmount() + this.getY());
      int barBottom = barTop + barHeight;
      pGuiGraphics.fill(barLeft, barTop, barRight, barBottom, new Color(0, 163, 53).getRGB());
      pGuiGraphics.fill(barLeft, barTop, barRight - 1, barBottom - 1, new Color(0, 211, 81).getRGB());
   }

   private int getScrollBarHeight() {
      return Mth.clamp((int)((float)(this.height * this.height) / (float)this.getContentHeight()), 32, this.height);
   }

   private int getContentHeight() {
      return this.getInnerHeight() + 4;
   }

   protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      int startY = (int)((double)this.getY() - this.scrollAmount());
      int yOffset = startY + this.paddingY;
      int currentRowWidth = this.paddingX;
      int rowHeight = 0;
      Rectangle clipRect = new Rectangle(this.getX(), this.getY(), this.width, this.height);
      guiGraphics.enableScissor(clipRect.x, clipRect.y, clipRect.x + clipRect.width, clipRect.y + clipRect.height);

      for (WordButton widget : this.widgets) {
         int nextWidth = currentRowWidth == 0 ? widget.getWidth() : currentRowWidth + this.widgetSpacing + widget.getWidth();
         if (nextWidth > this.width) {
            currentRowWidth = this.paddingX;
            yOffset += rowHeight + this.widgetSpacing;
            rowHeight = 0;
         }

         int widgetX = this.getX() + currentRowWidth;
         widget.setPosition(widgetX, yOffset);
         Rectangle widgetRect = new Rectangle(widgetX, yOffset, widget.getWidth(), widget.getHeight());
         Rectangle visibleRect = clipRect.intersection(widgetRect);
         if (!visibleRect.isEmpty()) {
            boolean mouseOverVisible = visibleRect.contains(mouseX, mouseY);
            widget.render(guiGraphics, mouseX, mouseY, partialTick, mouseOverVisible);
         }

         currentRowWidth += widget.getWidth() + this.widgetSpacing;
         rowHeight = Math.max(rowHeight, widget.getHeight());
      }

      guiGraphics.disableScissor();
   }

   protected boolean withinContentAreaTopBottom(int pTop, int pBottom) {
      return (double)pBottom > (double)this.getY() && (double)pTop < (double)(this.getY() + this.height);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      Rectangle clipRect = new Rectangle(this.getX(), this.getY(), this.width, this.height);

      for (WordButton widget : this.widgets) {
         Rectangle widgetRect = new Rectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
         Rectangle visible = clipRect.intersection(widgetRect);
         if (!visible.isEmpty() && visible.contains(mouseX, mouseY) && widget.mouseClicked(mouseX, mouseY, button)) {
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      return this.useScrollBar ? super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY) : true;
   }

   public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
      return super.mouseReleased(pMouseX, pMouseY, pButton);
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public List<WordButton> getWidgets() {
      return this.widgets;
   }

   @Generated
   public void setScroll_main(Color scroll_main) {
      this.scroll_main = scroll_main;
   }

   @Generated
   public void setScroll_add(Color scroll_add) {
      this.scroll_add = scroll_add;
   }
}
