package com.storyteam.storytelling.client.texture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import lombok.Generated;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Material {
   private final ResourceLocation albedo;
   private final ResourceLocation emission;

   @OnlyIn(Dist.CLIENT)
   public VertexConsumer get(MultiBufferSource source, boolean translucent) {
      if (this.albedo == null) {
         return VertexMultiConsumer.create(new VertexConsumer[0]);
      } else if (this.emission == null) {
         return source.getBuffer(translucent ? RenderType.entityTranslucent(this.albedo) : RenderType.entityCutoutNoCull(this.albedo));
      } else {
         VertexConsumer out1 = source.getBuffer(translucent ? RenderType.entityTranslucent(this.albedo) : RenderType.entityCutoutNoCull(this.albedo));
         VertexConsumer out2 = source.getBuffer(RenderType.entityTranslucentEmissive(this.emission));
         return out1 == out2 ? out1 : VertexMultiConsumer.create(out1, out2);
      }
   }

   @Generated
   Material(ResourceLocation albedo, ResourceLocation emission) {
      this.albedo = albedo;
      this.emission = emission;
   }

   @Generated
   public static Material.MaterialBuilder builder() {
      return new Material.MaterialBuilder();
   }

   @Generated
   public static class MaterialBuilder {
      @Generated
      private ResourceLocation albedo;
      @Generated
      private ResourceLocation emission;

      @Generated
      MaterialBuilder() {
      }

      @Generated
      public Material.MaterialBuilder albedo(ResourceLocation albedo) {
         this.albedo = albedo;
         return this;
      }

      @Generated
      public Material.MaterialBuilder emission(ResourceLocation emission) {
         this.emission = emission;
         return this;
      }

      @Generated
      public Material build() {
         return new Material(this.albedo, this.emission);
      }

      @Generated
      @Override
      public String toString() {
         return "Material.MaterialBuilder(albedo=" + this.albedo + ", emission=" + this.emission + ")";
      }
   }
}
