package com.storyteam.storytelling.client.render.entity.def.impl;

import com.storyteam.storytelling.client.render.entity.EmptyRenderer;
import com.storyteam.storytelling.client.render.entity.def.RenderContext;
import com.storyteam.storytelling.client.render.entity.def.RenderDefinition;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EmptyRenderDefinition<T extends Entity> extends RenderDefinition<T> {
   public EmptyRenderDefinition(RenderContext<T> context) {
      super(context);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public EntityRenderer<T> create(Context pContext) {
      return new EmptyRenderer<>(pContext);
   }
}
