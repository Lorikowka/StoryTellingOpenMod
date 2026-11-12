package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({TextureAtlas.class})
public interface TextureAtlasAccessor {
   @Accessor("width")
   int st$getWidth();

   @Accessor("height")
   int st$getHeight();
}
