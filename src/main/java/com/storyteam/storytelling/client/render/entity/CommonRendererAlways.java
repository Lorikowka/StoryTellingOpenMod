package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class CommonRendererAlways<T extends AnimatedEntity> extends CommonRenderer<T> {
   public CommonRendererAlways(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture, false);
   }

   public boolean shouldRender(AnimatedEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return true;
   }
}
