package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({RenderLayer.class})
public interface RenderLayerAccessor {
   @Accessor
   <T extends Entity, M extends EntityModel<T>> RenderLayerParent<T, M> getRenderer();
}
