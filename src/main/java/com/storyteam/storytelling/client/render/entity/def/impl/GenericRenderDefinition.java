package com.storyteam.storytelling.client.render.entity.def.impl;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.client.render.entity.def.RenderContext;
import com.storyteam.storytelling.client.render.entity.def.RenderDefinition;
import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import com.storyteam.storytelling.system.currency.client.render.EidosEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.abstractions.props.Key;
import org.zeith.hammerlib.util.java.Cast;

public class GenericRenderDefinition<T extends Entity> extends RenderDefinition<T> {
   public static final Key<GenericRenderDefinition.Renderer> RENDERER = Key.of(St.id("renderer"), GenericRenderDefinition.Renderer.class);

   public GenericRenderDefinition(RenderContext<T> context) {
      super(context);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public EntityRenderer<T> create(Context pContext) {
      IGeometryContainer model = (IGeometryContainer)this.context.props.get(RenderContext.ENTITY_MODEL);
      ResourceLocation texture = (ResourceLocation)this.context.props.get(RenderContext.DEFAULT_TEXTURE);
      float shadow = (Float)this.context.props.getOrDefault(RenderContext.ENTITY_SHADOW_SCALE, 0.5F);
      float scale = (Float)this.context.props.getOrDefault(RenderContext.ENTITY_SCALE, 1.0F);
      boolean always_renderer = (Boolean)this.context.props.getOrDefault(RenderContext.ENTITY_RENDER_ALWAYS, false);
      RenderKey key = (RenderKey)this.context.props.getOrDefault(RenderKey.KEY, RenderKey.entityCutoutNoCull);
      ModelConfiguration modelConfiguration = new ModelConfiguration(model, key::getNMC);
      switch ((GenericRenderDefinition.Renderer)this.context.props.get(RENDERER)) {
         case EidosRenderer:
            return (EntityRenderer<T>)Cast.cast(new EidosEntityRenderer(pContext, modelConfiguration, shadow, texture, always_renderer));
         default:
            throw new IncompatibleClassChangeError();
      }
   }

   public static enum Renderer {
      EidosRenderer;
   }
}
