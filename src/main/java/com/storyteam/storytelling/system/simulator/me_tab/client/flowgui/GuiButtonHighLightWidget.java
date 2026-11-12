package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import com.storyteam.storytelling.system.simulator.me_tab.client.button.ButtonHighlight;
import com.storyteam.storytelling.util.Color;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiButtonHighLightWidget extends GuiObject {
   public final ButtonHighlight wrapped;

   public GuiButtonHighLightWidget(String name, Component message, Color color, ResourceLocation btn, ButtonHighlight.OnPress onPress, Tooltip tooltip) {
      super(name);
      this.wrapped = new ButtonHighlight(0, 0, 0, 0, message, color, btn, onPress, tooltip);
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setWidth((int)this.width);
      this.wrapped.setHeight((int)this.height);
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return this.wrapped.mouseClicked((double)pos.relX(), (double)pos.relY(), button);
   }

   public GuiButtonHighLightWidget size(float width, float height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public GuiButtonHighLightWidget pos(float x, float y) {
      this.pos = new Point(x, y);
      return this;
   }

   public GuiButtonHighLightWidget centered(float width, float height) {
      this.pos((width - this.width) / 2.0F, (height - this.height) / 2.0F);
      return this;
   }

   public GuiButtonHighLightWidget centered(int width, int height) {
      this.pos((float)((int)(((float)width - this.width) / 2.0F)), (float)((int)(((float)height - this.height) / 2.0F)));
      return this;
   }

   public GuiButtonHighLightWidget addPos(int x, int y) {
      this.pos = this.pos.offset((float)x, (float)y);
      return this;
   }
}
