package com.storyteam.storytelling.client.flowgui;

import com.storyteam.storytelling.events.ClientModEvents;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;

public class GuiFadingImage extends GuiImageObject {
   protected float lastTickAlpha = 1.0F;
   protected float tickAlpha = 1.0F;
   public float targetAlpha = 1.0F;
   public float alphaLerpSpeed = 0.1F;

   public GuiFadingImage(
      String name, Supplier<ResourceLocation> tex, float uOffset, float vOffset, float imgWidth, float imgHeight, float txWidth, float txHeight
   ) {
      super(name, tex, uOffset, vOffset, imgWidth, imgHeight, txWidth, txHeight);
      this.shader(ClientModEvents::noDiscardPositionColorTex);
   }

   public GuiFadingImage alpha(float alpha) {
      this.lastTickAlpha = this.tickAlpha = this.targetAlpha = alpha;
      return this;
   }

   protected void render(Graphics gfx, MousePos pos) {
      this.alpha = Mth.lerp(gfx.partialTime(), this.lastTickAlpha, this.tickAlpha);
      super.render(gfx, pos);
   }

   protected void update() {
      this.lastTickAlpha = this.tickAlpha;
      this.tickAlpha = this.tickAlpha + Mth.clamp(this.targetAlpha - this.tickAlpha, -this.alphaLerpSpeed, this.alphaLerpSpeed);
   }
}
