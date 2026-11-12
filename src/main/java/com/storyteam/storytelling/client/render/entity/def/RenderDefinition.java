package com.storyteam.storytelling.client.render.entity.def;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class RenderDefinition<T extends Entity> {
   protected final RenderContext<T> context;

   public RenderDefinition(RenderContext<T> context) {
      this.context = context;
   }

   @OnlyIn(Dist.CLIENT)
   public abstract EntityRenderer<T> create(Context var1);
}
