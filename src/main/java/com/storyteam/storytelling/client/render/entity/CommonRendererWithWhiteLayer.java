package com.storyteam.storytelling.client.render.entity;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.layer.FadeableGlowingLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

@OnlyIn(Dist.CLIENT)
public class CommonRendererWithWhiteLayer<T extends LivingEntity & IAnimatedEntity> extends CommonRenderer<T> {
   public <M extends SimpleBedrockModel<T>> CommonRendererWithWhiteLayer(
      Context manager, M model, float shadowSize, ResourceLocation texture, boolean alwaysRenderer
   ) {
      super(manager, model, shadowSize, texture, alwaysRenderer);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }

   public <M extends SimpleBedrockModel<T>> CommonRendererWithWhiteLayer(Context manager, M model, float shadowSize, ResourceLocation texture) {
      super(manager, model, shadowSize, texture);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }

   public CommonRendererWithWhiteLayer(
      Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture, boolean alwaysRenderer
   ) {
      super(manager, modelConfiguration, shadowSize, texture, alwaysRenderer);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }

   public CommonRendererWithWhiteLayer(Context manager, ModelConfiguration modelConfiguration, float shadowSize, ResourceLocation texture) {
      super(manager, modelConfiguration, shadowSize, texture);
      this.addLayer(new FadeableGlowingLayer(this, St.tx("entity/walter_white")));
   }
}
