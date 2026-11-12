package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import com.storyteam.storytelling.system.simulator.me_tab.client.button.ButtonStyleClass;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.StretchWidget;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiStretchWidget extends GuiObject {
   public final StretchWidget wrapped;

   public GuiStretchWidget(String name, Component message, ButtonStyleClass buttonStyleClass) {
      super(name);
      this.wrapped = new StretchWidget(0, 0, message, buttonStyleClass);
      this.width = (float)this.wrapped.getWidth();
      this.height = (float)this.wrapped.getHeight();
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setWidth((int)this.width);
      this.wrapped.setHeight((int)this.height);
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return this.wrapped.mouseClicked((double)pos.relX(), (double)pos.relY(), button);
   }

   public GuiStretchWidget size(float width, float height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public GuiStretchWidget pos(float x, float y) {
      this.pos = new Point(x, y);
      return this;
   }

   public GuiStretchWidget centered(float width, float height) {
      this.pos((width - this.width) / 2.0F, (height - this.height) / 2.0F);
      return this;
   }

   public GuiStretchWidget centered(int width, int height) {
      this.pos((float)((int)(((float)width - this.width) / 2.0F)), (float)((int)(((float)height - this.height) / 2.0F)));
      return this;
   }

   public GuiStretchWidget addPos(int x, int y) {
      this.pos = this.pos.offset((float)x, (float)y);
      return this;
   }
}
