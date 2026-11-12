package com.storyteam.storytelling.mixins._client.minecraft;

import java.util.Map;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ParticleEngine.class})
public interface ParticleEngineAccessor {
   @Accessor("textureAtlas")
   TextureAtlas storytelling$getTextureAtlas();

   @Accessor("spriteSets")
   Map<ResourceLocation, SpriteSet> storytelling$getSpriteSets();
}
