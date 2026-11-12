package com.storyteam.storytelling.system.simulator.me_tab.attributes.client.flowgui;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.client.AttributeWidget;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.util.math.Point;

public class GuiAttributeWidget extends GuiObject {
   public final AttributeWidget wrapped;

   public GuiAttributeWidget(String name, AttributeData data) {
      super(name);
      this.wrapped = new AttributeWidget(data, 0, 0, (int)this.width, (int)this.height);
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.wrapped.setWidth((int)this.width);
      this.wrapped.setHeight((int)this.height);
      this.wrapped.render(gfx.gfx(), (int)pos.relX(), (int)pos.relY(), gfx.partialTime());
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return this.wrapped.mouseClicked((double)pos.relX(), (double)pos.relY(), button);
   }

   public GuiAttributeWidget size(float width, float height) {
      this.width = width;
      this.height = height;
      return this;
   }

   public GuiAttributeWidget pos(float x, float y) {
      this.pos = new Point(x, y);
      return this;
   }

   public GuiAttributeWidget centered(float width, float height) {
      this.pos((width - this.width) / 2.0F, (height - this.height) / 2.0F);
      return this;
   }

   public GuiAttributeWidget centered(int width, int height) {
      this.pos((float)((int)(((float)width - this.width) / 2.0F)), (float)((int)(((float)height - this.height) / 2.0F)));
      return this;
   }

   public GuiAttributeWidget addPos(int x, int y) {
      this.pos = this.pos.offset((float)x, (float)y);
      return this;
   }
}
