package com.storyteam.storytelling.client.screen.widgets;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.utils.Scissors;

public class ScrollWidget extends AbstractWidget {
   private List<AbstractWidget> widgets = new ArrayList<>();
   private double scroll = 0.0;

   public ScrollWidget(int pX, int pY, int pWidth, int pHeight) {
      super(pX, pY, pWidth, pHeight, Component.empty());
   }

   public void add(AbstractWidget widget) {
      this.widgets.add(widget);
   }

   protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
      int yPos = 0;
      Scissors.begin(this.getX(), this.getY(), this.getWidth(), this.getHeight());

      for (AbstractWidget widget : this.widgets) {
         widget.setY((int)((double)(this.getY() + yPos) + this.scroll));
         yPos += widget.getHeight() + 10;
         widget.render(guiGraphics, i, i1, v);
      }

      Scissors.end();
   }

   public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
      if (pMouseX > (double)this.getX()
         && pMouseX < (double)(this.getX() + this.getWidth())
         && pMouseY > (double)this.getY()
         && pMouseY < (double)(this.getY() + this.getHeight())) {
         this.scroll = this.scroll + Mth.clamp(pDelta * 5.0, -10.0, 10.0);
         int lowestPoint = 0;

         for (AbstractWidget widget : this.widgets) {
            lowestPoint += widget.getHeight() + 10;
         }

         lowestPoint -= this.getHeight();
         this.scroll = Math.max(this.scroll, (double)(-lowestPoint));
         this.scroll = Math.min(this.scroll, 0.0);
         return true;
      } else {
         return true;
      }
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }
}
