package com.storyteam.storytelling.system.clothes.options;

import com.storyteam.storytelling.system.clothes.client.VanityRenderOptions;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.function.BiFunction;
import lombok.Generated;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public class VanityOverride {
   private final IGeometryContainer entityModel;
   private final String overrideName;
   private final IVanityTextureProvider textures;
   private final BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions;

   public static VanityOverride.VanityOverrideBuilder builder(String overrideName) {
      return new VanityOverride.VanityOverrideBuilder().overrideName(overrideName);
   }

   @Generated
   private static BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> $default$renderOptions() {
      return (s, b) -> b;
   }

   @Generated
   VanityOverride(
      IGeometryContainer entityModel,
      String overrideName,
      IVanityTextureProvider textures,
      BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions
   ) {
      this.entityModel = entityModel;
      this.overrideName = overrideName;
      this.textures = textures;
      this.renderOptions = renderOptions;
   }

   @Generated
   public IGeometryContainer getEntityModel() {
      return this.entityModel;
   }

   @Generated
   public String getOverrideName() {
      return this.overrideName;
   }

   @Generated
   public IVanityTextureProvider getTextures() {
      return this.textures;
   }

   @Generated
   public BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> getRenderOptions() {
      return this.renderOptions;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VanityOverride other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$entityModel = this.getEntityModel();
         Object other$entityModel = other.getEntityModel();
         if (this$entityModel == null ? other$entityModel == null : this$entityModel.equals(other$entityModel)) {
            Object this$overrideName = this.getOverrideName();
            Object other$overrideName = other.getOverrideName();
            if (this$overrideName == null ? other$overrideName == null : this$overrideName.equals(other$overrideName)) {
               Object this$textures = this.getTextures();
               Object other$textures = other.getTextures();
               if (this$textures == null ? other$textures == null : this$textures.equals(other$textures)) {
                  Object this$renderOptions = this.getRenderOptions();
                  Object other$renderOptions = other.getRenderOptions();
                  return this$renderOptions == null ? other$renderOptions == null : this$renderOptions.equals(other$renderOptions);
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof VanityOverride;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $entityModel = this.getEntityModel();
      result = result * 59 + ($entityModel == null ? 43 : $entityModel.hashCode());
      Object $overrideName = this.getOverrideName();
      result = result * 59 + ($overrideName == null ? 43 : $overrideName.hashCode());
      Object $textures = this.getTextures();
      result = result * 59 + ($textures == null ? 43 : $textures.hashCode());
      Object $renderOptions = this.getRenderOptions();
      return result * 59 + ($renderOptions == null ? 43 : $renderOptions.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "VanityOverride(entityModel="
         + this.getEntityModel()
         + ", overrideName="
         + this.getOverrideName()
         + ", textures="
         + this.getTextures()
         + ", renderOptions="
         + this.getRenderOptions()
         + ")";
   }

   @Generated
   public static class VanityOverrideBuilder {
      @Generated
      private IGeometryContainer entityModel;
      @Generated
      private String overrideName;
      @Generated
      private IVanityTextureProvider textures;
      @Generated
      private boolean renderOptions$set;
      @Generated
      private BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions$value;

      @Generated
      VanityOverrideBuilder() {
      }

      @Generated
      public VanityOverride.VanityOverrideBuilder entityModel(IGeometryContainer entityModel) {
         this.entityModel = entityModel;
         return this;
      }

      @Generated
      public VanityOverride.VanityOverrideBuilder overrideName(String overrideName) {
         this.overrideName = overrideName;
         return this;
      }

      @Generated
      public VanityOverride.VanityOverrideBuilder textures(IVanityTextureProvider textures) {
         this.textures = textures;
         return this;
      }

      @Generated
      public VanityOverride.VanityOverrideBuilder renderOptions(BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions) {
         this.renderOptions$value = renderOptions;
         this.renderOptions$set = true;
         return this;
      }

      @Generated
      public VanityOverride build() {
         BiFunction<VanitySlot, VanityRenderOptions.Builder, VanityRenderOptions.Builder> renderOptions$value = this.renderOptions$value;
         if (!this.renderOptions$set) {
            renderOptions$value = VanityOverride.$default$renderOptions();
         }

         return new VanityOverride(this.entityModel, this.overrideName, this.textures, renderOptions$value);
      }

      @Generated
      @Override
      public String toString() {
         return "VanityOverride.VanityOverrideBuilder(entityModel="
            + this.entityModel
            + ", overrideName="
            + this.overrideName
            + ", textures="
            + this.textures
            + ", renderOptions$value="
            + this.renderOptions$value
            + ")";
      }
   }
}
