package com.storyteam.storytelling.client.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class AnimatedWidget extends AbstractWidget {
   public float fx;
   public float fy;
   public float start_ticker = 0.0F;
   public float ticker = this.start_ticker;
   Interpolation interpolation;
   public float falpha = 0.0F;
   AnimatedData data;

   public AnimatedWidget(int pX, int pY, int width, int height, AnimatedData data) {
      super(pX, pY, width, height, Component.empty());
      this.fx = (float)pX;
      this.fy = (float)pY;
      this.data = data;
      this.interpolation = this.data.type().interpolationFactory.get();
   }

   public abstract void animatedRender(GuiGraphics var1, int var2, int var3, float var4);

   protected void renderWidget(GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      int x = this.getX();
      int y = this.getY();
      this.fx = InterpolationType.lerp((float)(x + this.data.dX()), (float)x, this.interpolation.interpolate(this.start_ticker));
      this.fy = InterpolationType.lerp((float)(y + this.data.dY()), (float)y, this.interpolation.interpolate(this.start_ticker));
      this.falpha = InterpolationType.EASE_OUT_QUAD.interpolationFactory.get().interpolate(this.start_ticker);
      PoseStack pose = gfx.pose();
      pose.pushPose();
      pose.translate(this.fx - (float)x, this.fy - (float)y, 0.0F);
      this.animatedRender(gfx, pMouseX, pMouseY, pPartialTick);
      pose.popPose();
      if (this.ticker < 1.0F + this.data.delay()) {
         this.ticker = this.ticker + pPartialTick / 20.0F * this.data.speed();
      }

      this.start_ticker = Mth.clamp(this.ticker - this.data.delay(), 0.0F, 1.0F);
   }

   protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
   }
}
