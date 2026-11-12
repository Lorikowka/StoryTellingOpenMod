package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiFilledWidget extends GuiObject {
   int colorFrom;
   int colorTo;

   public GuiFilledWidget(String name, int colorFrom, int colorTo) {
      super(name);
      this.colorFrom = colorFrom;
      this.colorTo = colorTo;
   }

   protected void render(Graphics gfx, MousePos pos) {
      gfx.fillGradient(
         (int)((Point)this.elementPosition.get()).x(),
         (int)((Point)this.elementPosition.get()).y(),
         (int)(((Point)this.elementPosition.get()).x() + this.width),
         (int)(((Point)this.elementPosition.get()).y() + this.height),
         this.colorFrom,
         this.colorTo
      );
   }

   public GuiFilledWidget size(float width, float height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public GuiFilledWidget pos(float x, float y) {
      this.pos = new Point(x, y);
      return this;
   }

   public GuiFilledWidget centered(float width, float height) {
      this.pos((width - this.width) / 2.0F, (height - this.height) / 2.0F);
      return this;
   }

   public GuiFilledWidget centered(int width, int height) {
      this.pos((float)((int)(((float)width - this.width) / 2.0F)), (float)((int)(((float)height - this.height) / 2.0F)));
      return this;
   }
}
