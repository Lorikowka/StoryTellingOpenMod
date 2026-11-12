package com.storyteam.storytelling.mixins._client.hammeranims;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.zeith.hammeranims.core.client.model.ModelCubeF;
import org.zeith.hammeranims.core.client.model.TexturedQuadF;

@Mixin({ModelCubeF.class})
public interface ModelCubeFAccessor {
   @Accessor
   TexturedQuadF[] getQuads();
}
