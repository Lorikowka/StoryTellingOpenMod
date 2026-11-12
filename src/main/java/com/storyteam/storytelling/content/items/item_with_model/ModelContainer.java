package com.storyteam.storytelling.content.items.item_with_model;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.util.UtilST;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public record ModelContainer(SimpleBedrockModel<?> model, ResourceLocation texture) {
   public static ModelContainer create(IGeometryContainer container, String tex) {
      return new ModelContainer(UtilST.model(container), St.tx(tex));
   }
}
