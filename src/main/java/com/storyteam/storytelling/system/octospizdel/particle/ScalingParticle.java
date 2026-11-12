package com.storyteam.storytelling.system.octospizdel.particle;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.octospizdel.util.OctoColor;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public class ScalingParticle extends UIParticle {
   private final float startScale;
   private final float peakScale;
   private final float endScale;

   public ScalingParticle(
      float maxSpeed, int lifetime, float xStart, float yStart, UIParticle.Layer layer, float zOffset, float startScale, float peakScale, float endScale
   ) {
      super(new UIParticle.Texture2D(St.tx("particle/circle_full"), 64, 64), maxSpeed, lifetime, xStart, yStart, layer, zOffset);
      this.startScale = startScale;
      this.peakScale = peakScale;
      this.endScale = endScale;
      this.getTransform().setSize(new Vector2f(startScale, startScale));
      this.getTransform().updateOldValues();
      this.setColors(new OctoColor[]{new OctoColor(0.0F, 0.0F, 0.0F, 1.0F)});
      this.enableBlend(false);
   }

   @Override
   public void tick() {
      super.tick();
      float lifeRatio = this.getTimeRatio(0.0F);
      float scale;
      if (lifeRatio < 0.5F) {
         scale = Mth.lerp(lifeRatio / 0.5F, this.startScale, this.peakScale);
      } else {
         scale = Mth.lerp((lifeRatio - 0.5F) / 0.5F, this.peakScale, this.endScale);
      }

      this.getTransform().setSize(new Vector2f(scale, scale));
   }
}
