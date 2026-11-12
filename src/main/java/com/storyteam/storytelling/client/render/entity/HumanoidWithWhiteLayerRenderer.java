package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.FadeableGlowingLayer;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class HumanoidWithWhiteLayerRenderer extends HumanoidRenderer<HumanoidEntity> {
   public HumanoidWithWhiteLayerRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }
}
