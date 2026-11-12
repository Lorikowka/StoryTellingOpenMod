package com.storyteam.storytelling.client.render.entity.def;

import com.storyteam.storytelling.St;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.abstractions.props.Key;

public enum RenderKey {
   entityCutoutNoCull,
   entityTranslucent,
   entityTranslucentEmissive,
   armorCutoutNoCull,
   entityTranslucentCull;

   public static final Key<RenderKey> KEY = Key.of(St.id("render_type"), RenderKey.class);

   @OnlyIn(Dist.CLIENT)
   public RenderType getNMC(ResourceLocation texture) {
      return switch (this) {
         case entityTranslucentCull -> RenderType.entityTranslucentCull(texture);
         case entityCutoutNoCull -> RenderType.entityCutoutNoCull(texture);
         case entityTranslucent -> RenderType.entityTranslucent(texture);
         case armorCutoutNoCull -> RenderType.armorCutoutNoCull(texture);
         case entityTranslucentEmissive -> RenderType.entityTranslucentEmissive(texture);
      };
   }

   @OnlyIn(Dist.CLIENT)
   public RenderType getWhiteVariant() {
      ResourceLocation white = new ResourceLocation("minecraft", "textures/misc/white.png");

      return switch (this) {
         case entityTranslucentCull -> RenderType.entityTranslucentCull(white);
         case entityCutoutNoCull -> RenderType.entityCutoutNoCull(white);
         case entityTranslucent -> RenderType.entityTranslucent(white);
         case armorCutoutNoCull -> RenderType.armorCutoutNoCull(white);
         case entityTranslucentEmissive -> RenderType.entityTranslucentEmissive(white);
      };
   }
}
