package com.storyteam.storytelling.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmptyRenderer<T extends Entity> extends EntityRenderer<T> {
   public EmptyRenderer(Context manager) {
      super(manager);
   }

   public ResourceLocation getTextureLocation(T pEntity) {
      return InventoryMenu.BLOCK_ATLAS;
   }

   public static class RenderFactory<T extends Entity> implements EntityRendererProvider<T> {
      public EntityRenderer<T> create(Context manager) {
         return new EmptyRenderer<>(manager);
      }
   }
}
