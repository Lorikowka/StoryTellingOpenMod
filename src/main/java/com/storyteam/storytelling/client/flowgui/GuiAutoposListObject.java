package com.storyteam.storytelling.client.flowgui;

import java.util.Collection;
import java.util.List;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;

public class GuiAutoposListObject extends GuiObject {
   protected int scroll;

   public GuiAutoposListObject(String name) {
      super(name);
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return fake ? true : super.onMouseClicked(globalMousePos, pos, button, fake);
   }

   protected boolean scroll(double y) {
      int cs = ((Collection)this.getChildren()).size();
      if (cs < 5) {
         return false;
      } else {
         int prev = this.scroll;
         this.scroll = Mth.clamp(this.scroll + (int)Math.round(-Math.signum(y)), 0, cs - 4);
         return prev != this.scroll;
      }
   }

   protected boolean onMouseScrolled(Point globalMousePos, MousePos pos, ScrollData delta) {
      return this.enabled && pos.isMouseWithin(this) && this.scroll(delta.scrollY()) || super.onMouseScrolled(globalMousePos, pos, delta);
   }

   protected void render(Graphics gfx, MousePos pos) {
      List<GuiObject> ch = (List<GuiObject>)this.getChildren();
      float y = 2.0F;
      y -= (float)(22 * this.scroll);

      for (GuiObject child : ch) {
         child.pos(((Point)child.elementPosition.get()).x(), y);
         y += child.getScaledHeight() + 2.0F;
      }
   }
}
