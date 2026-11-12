package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class HumanoidAlwaysRenderer extends HumanoidRenderer<HumanoidEntity> {
   public HumanoidAlwaysRenderer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, float scale) {
      super(manager, modelConfiguration, shadowSize, texture, scale, true);
   }

   public boolean shouldRender(HumanoidEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
      return true;
   }

   @Nullable
   protected RenderType getRenderType(HumanoidEntity pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
      return RenderType.entityTranslucent(this.getTextureLocation(pLivingEntity));
   }
}
