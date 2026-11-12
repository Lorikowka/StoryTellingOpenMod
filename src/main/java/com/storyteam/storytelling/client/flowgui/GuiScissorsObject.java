package com.storyteam.storytelling.client.flowgui;

import org.joml.Vector3f;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.utils.Scissors;

public class GuiScissorsObject extends GuiObject {
   public boolean enableScissors = true;

   public GuiScissorsObject(String name) {
      super(name);
      this.onPostRender((p, m) -> Scissors.end());
   }

   protected void render(Graphics gfx, MousePos pos) {
      if (this.enableScissors) {
         Vector3f p0 = gfx.pose().last().pose().transformPosition(new Vector3f());
         Vector3f size = gfx.pose().last().pose().transformPosition(new Vector3f(this.width, this.height, 0.0F)).sub(p0);
         Scissors.begin((int)p0.x, (int)p0.y, (int)size.x, (int)size.y);
      }
   }
}
