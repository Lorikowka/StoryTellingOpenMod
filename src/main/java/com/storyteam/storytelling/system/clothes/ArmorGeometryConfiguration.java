package com.storyteam.storytelling.system.clothes;

import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public record ArmorGeometryConfiguration(
   IGeometryContainer container,
   VanityRenderOptions renderOptions,
   Map<IGeometryContainer, IGeometryContainer> modelOverrides,
   Map<IGeometryContainer, VanityRenderOptions> textureOverrides
) {
   public VanityRenderOptions getOptionsFor(IGeometryContainer ctr) {
      return this.textureOverrides.getOrDefault(ctr, this.renderOptions);
   }

   @Nullable
   public IGeometryContainer getModelContainerFor(IGeometryContainer ctr) {
      return this.modelOverrides.getOrDefault(ctr, this.container);
   }
}
