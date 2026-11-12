package com.storyteam.storytelling.script.story.characters;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public record TexturedGeometryContainer(IGeometryContainer model, ResourceLocation texture, ResourceLocation emissive) {
}
