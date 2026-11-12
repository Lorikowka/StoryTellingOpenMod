package com.storyteam.storytelling.system.shop.screen.widgets;

import java.util.ArrayList;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.utils.Scissors;

public class LotsWidget extends AbstractWidget {
   private ArrayList<LotWidget> lots = new ArrayList<>();

   public LotsWidget(int pX, int pY, int pWidth, int pHeight) {
      super(pX, pY, pWidth, pHeight, Component.empty());
   }

   protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
      Scissors.begin(this.getX(), this.getY(), this.getWidth(), this.getHeight());

      for (LotWidget lot : this.lots) {
         lot.renderWidget(guiGraphics, i, i1, v);
      }

      Scissors.end();
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
         for (LotWidget lot : this.lots) {
            if (lot.mouseClicked(pMouseX, pMouseY, pButton)) {
               return true;
            }
         }
      }

      return false;
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public ArrayList<LotWidget> getLots() {
      return this.lots;
   }

   @Generated
   public void setLots(ArrayList<LotWidget> lots) {
      this.lots = lots;
   }
}
