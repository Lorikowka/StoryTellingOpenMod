package com.storyteam.storytelling.system.clothes.options;

import com.storyteam.storytelling.system.clothes.VanityModels;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public class VanityModelOptions {
   private final VanityParts parts;
   private final Map<String, VanityOverride> modelOverrides;

   @Generated
   private static VanityParts $default$parts() {
      return VanityParts.ALL;
   }

   @Generated
   private static Map<String, VanityOverride> $default$modelOverrides() {
      return new HashMap<>();
   }

   @Generated
   VanityModelOptions(VanityParts parts, Map<String, VanityOverride> modelOverrides) {
      this.parts = parts;
      this.modelOverrides = modelOverrides;
   }

   @Generated
   public static VanityModelOptions.VanityModelOptionsBuilder builder() {
      return new VanityModelOptions.VanityModelOptionsBuilder();
   }

   @Generated
   public VanityParts getParts() {
      return this.parts;
   }

   @Generated
   public Map<String, VanityOverride> getModelOverrides() {
      return this.modelOverrides;
   }

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VanityModelOptions other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$parts = this.getParts();
         Object other$parts = other.getParts();
         if (this$parts == null ? other$parts == null : this$parts.equals(other$parts)) {
            Object this$modelOverrides = this.getModelOverrides();
            Object other$modelOverrides = other.getModelOverrides();
            return this$modelOverrides == null ? other$modelOverrides == null : this$modelOverrides.equals(other$modelOverrides);
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof VanityModelOptions;
   }

   @Generated
   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $parts = this.getParts();
      result = result * 59 + ($parts == null ? 43 : $parts.hashCode());
      Object $modelOverrides = this.getModelOverrides();
      return result * 59 + ($modelOverrides == null ? 43 : $modelOverrides.hashCode());
   }

   @Generated
   @Override
   public String toString() {
      return "VanityModelOptions(parts=" + this.getParts() + ", modelOverrides=" + this.getModelOverrides() + ")";
   }

   public static class VanityModelOptionsBuilder {
      @Generated
      private boolean parts$set;
      @Generated
      private VanityParts parts$value;
      @Generated
      private boolean modelOverrides$set;
      @Generated
      private Map<String, VanityOverride> modelOverrides$value;

      public VanityModelOptions.VanityModelOptionsBuilder overrideBaseTex(IGeometryContainer entityModel, String overrideName) {
         return this.override(entityModel, overrideName, IVanityTextureProvider.INHERIT_BASE_TEXTURES);
      }

      public VanityModelOptions.VanityModelOptionsBuilder overrideUnifiedTex(IGeometryContainer entityModel, String overrideName) {
         return this.override(entityModel, overrideName, IVanityTextureProvider.UNIFIED_TEXTURE);
      }

      public VanityModelOptions.VanityModelOptionsBuilder overrideSplitTex(IGeometryContainer entityModel, String overrideName) {
         return this.override(entityModel, overrideName, IVanityTextureProvider.SPLIT_TEXTURES);
      }

      public VanityModelOptions.VanityModelOptionsBuilder override(IGeometryContainer entityModel, String overrideName, IVanityTextureProvider textures) {
         return this.override(VanityOverride.builder(overrideName).entityModel(entityModel).textures(textures).build());
      }

      public VanityModelOptions.VanityModelOptionsBuilder override(VanityOverride override) {
         if (this.modelOverrides$value == null || !this.modelOverrides$set) {
            this.modelOverrides$value = new HashMap<>();
            this.modelOverrides$set = true;
         }

         this.modelOverrides$value.put(override.getOverrideName(), override);
         return this;
      }

      public VanityModelOptions.VanityModelOptionsBuilder addParts(VanitySlot... slots) {
         this.parts$value = this.parts$value.or(VanityParts.of(slots));
         this.parts$set = true;
         return this;
      }

      public VanityModelOptions.VanityModelOptionsBuilder setParts(VanitySlot... slots) {
         Set<VanitySlot> es = Set.of(slots);
         this.parts$value = VanityParts.ALL.filter(es::contains);
         this.parts$set = true;
         return this;
      }

      public VanityModels create() {
         return new VanityModels(this.build());
      }

      @Generated
      VanityModelOptionsBuilder() {
      }

      @Generated
      public VanityModelOptions.VanityModelOptionsBuilder parts(VanityParts parts) {
         this.parts$value = parts;
         this.parts$set = true;
         return this;
      }

      @Generated
      public VanityModelOptions.VanityModelOptionsBuilder modelOverrides(Map<String, VanityOverride> modelOverrides) {
         this.modelOverrides$value = modelOverrides;
         this.modelOverrides$set = true;
         return this;
      }

      @Generated
      public VanityModelOptions build() {
         VanityParts parts$value = this.parts$value;
         if (!this.parts$set) {
            parts$value = VanityModelOptions.$default$parts();
         }

         Map<String, VanityOverride> modelOverrides$value = this.modelOverrides$value;
         if (!this.modelOverrides$set) {
            modelOverrides$value = VanityModelOptions.$default$modelOverrides();
         }

         return new VanityModelOptions(parts$value, modelOverrides$value);
      }

      @Generated
      @Override
      public String toString() {
         return "VanityModelOptions.VanityModelOptionsBuilder(parts$value=" + this.parts$value + ", modelOverrides$value=" + this.modelOverrides$value + ")";
      }
   }
}
