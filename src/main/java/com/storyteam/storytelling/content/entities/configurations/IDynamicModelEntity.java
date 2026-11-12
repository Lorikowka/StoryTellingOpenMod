package com.storyteam.storytelling.content.entities.configurations;

import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

public interface IDynamicModelEntity<T extends Entity & IAnimatedEntity> {
   ResourceLocation getTexture();

   SimpleBedrockModel<T> getModel(Function<IGeometryContainer, SimpleBedrockModel<T>> var1);
}
