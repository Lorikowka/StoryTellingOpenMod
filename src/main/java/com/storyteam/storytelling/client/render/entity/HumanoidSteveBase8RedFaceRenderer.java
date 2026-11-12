package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.RedFaceSteveBase8Layer;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class HumanoidSteveBase8RedFaceRenderer extends HumanoidRenderer<HumanoidEntity> {
   public HumanoidSteveBase8RedFaceRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, false);
      this.addLayer(new RedFaceSteveBase8Layer(this, St.tx("entity/city_steve_base8_red_face_layer")));
   }
}
