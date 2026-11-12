package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.FadeableGlowingLayer;
import com.storyteam.storytelling.client.render.entity.layer.GlowingLayerSaturation;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.mcf.Resources;

public class HumanoidWithEmissiveAndWhiteLayerRenderer extends HumanoidRenderer<HumanoidEntity> {
   public HumanoidWithEmissiveAndWhiteLayerRenderer(
      Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale
   ) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
      this.addLayer(
         new GlowingLayerSaturation(
            this, Resources.location(texture.getNamespace(), texture.getPath().replace(".png", "_emissive.png")), 0.7F, 0.7F, 0.7F, 1.0F
         )
      );
   }
}
