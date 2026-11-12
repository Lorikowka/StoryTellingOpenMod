package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.renderer.block.model.ItemOverrides.BakedOverride;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BakedOverride.class})
public interface BakedOverrideAccessor {
   @Accessor
   BakedModel getModel();
}
