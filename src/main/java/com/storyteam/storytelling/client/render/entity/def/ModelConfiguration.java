package com.storyteam.storytelling.client.render.entity.def;

import java.util.function.Function;
import lombok.Generated;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public record ModelConfiguration(IGeometryContainer model, Function<ResourceLocation, RenderType> renderType) {
   public ModelConfiguration(IGeometryContainer model) {
      this(model, RenderType::entityCutoutNoCull);
   }

   @Generated
   public static ModelConfiguration.ModelConfigurationBuilder builder() {
      return new ModelConfiguration.ModelConfigurationBuilder();
   }

   @Generated
   public static class ModelConfigurationBuilder {
      @Generated
      private IGeometryContainer model;
      @Generated
      private Function<ResourceLocation, RenderType> renderType;

      @Generated
      ModelConfigurationBuilder() {
      }

      @Generated
      public ModelConfiguration.ModelConfigurationBuilder model(IGeometryContainer model) {
         this.model = model;
         return this;
      }

      @Generated
      public ModelConfiguration.ModelConfigurationBuilder renderType(Function<ResourceLocation, RenderType> renderType) {
         this.renderType = renderType;
         return this;
      }

      @Generated
      public ModelConfiguration build() {
         return new ModelConfiguration(this.model, this.renderType);
      }

      @Generated
      @Override
      public String toString() {
         return "ModelConfiguration.ModelConfigurationBuilder(model=" + this.model + ", renderType=" + this.renderType + ")";
      }
   }
}
