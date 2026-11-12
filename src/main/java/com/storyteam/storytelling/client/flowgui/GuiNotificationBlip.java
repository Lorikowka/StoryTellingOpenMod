package com.storyteam.storytelling.client.flowgui;

import java.time.Duration;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;

public class GuiNotificationBlip extends GuiImageObject {
   private final int steps;
   private final Duration stepDuration;

   public GuiNotificationBlip(String name, ResourceLocation tx, int imgWidth, int imgHeight, int steps, Duration stepDuration) {
      super(name, () -> tx, 0.0F, 0.0F, (float)imgWidth, (float)imgHeight, (float)imgWidth, (float)(imgHeight * steps));
      this.steps = steps;
      this.stepDuration = stepDuration;
   }

   private int getStep() {
      return (int)(System.currentTimeMillis() / this.stepDuration.toMillis() % (long)this.steps);
   }

   private void updateTexture() {
      int step = this.getStep();
      this.vOffset = this.imgHeight * (float)step;
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.updateTexture();
      super.render(gfx, pos);
   }
}
