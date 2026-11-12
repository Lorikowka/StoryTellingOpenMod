package com.storyteam.storytelling.system.clothes.client;

import com.storyteam.storytelling.client.render.entity.def.RenderKey;
import lombok.Generated;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record VanityRenderOptions(ResourceLocation texture, RenderKey renderer, boolean emissive) {
   @OnlyIn(Dist.CLIENT)
   public RenderType renderType() {
      return this.renderer.getNMC(this.texture);
   }

   @OnlyIn(Dist.CLIENT)
   public RenderType renderTypeWhite() {
      return this.renderer.getWhiteVariant();
   }

   @Generated
   public static VanityRenderOptions.Builder builder() {
      return new VanityRenderOptions.Builder();
   }

   @Generated
   public static class Builder {
      @Generated
      private ResourceLocation texture;
      @Generated
      private RenderKey renderer;
      @Generated
      private boolean emissive;

      @Generated
      Builder() {
      }

      @Generated
      public VanityRenderOptions.Builder texture(ResourceLocation texture) {
         this.texture = texture;
         return this;
      }

      @Generated
      public VanityRenderOptions.Builder renderer(RenderKey renderer) {
         this.renderer = renderer;
         return this;
      }

      @Generated
      public VanityRenderOptions.Builder emissive(boolean emissive) {
         this.emissive = emissive;
         return this;
      }

      @Generated
      public VanityRenderOptions build() {
         return new VanityRenderOptions(this.texture, this.renderer, this.emissive);
      }

      @Generated
      @Override
      public String toString() {
         return "VanityRenderOptions.Builder(texture=" + this.texture + ", renderer=" + this.renderer + ", emissive=" + this.emissive + ")";
      }
   }
}
