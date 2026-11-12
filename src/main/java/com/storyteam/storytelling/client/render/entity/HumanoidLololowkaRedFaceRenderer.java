package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.BlushLololowkaLayer;
import com.storyteam.storytelling.client.render.entity.layer.RedFaceLololowkaLayer;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class HumanoidLololowkaRedFaceRenderer extends HumanoidRenderer<HumanoidEntity> {
   public HumanoidLololowkaRedFaceRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
      this.addLayer(new RedFaceLololowkaLayer(this, St.tx("entity/lololowka_red_face_layer")));
      this.addLayer(new BlushLololowkaLayer(this, St.tx("entity/lololowka_blush_layer")));
   }
}
