package com.storyteam.storytelling.system.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.St;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.opengl.GL46;

public class OptimizedParticleType extends AbstractParticleType {
   public static OptimizedParticleType CIRCLE_TINT = builder()
      .texture(St.tx("particle/circle_tint"))
      .sfactor(770)
      .dfactor(1)
      .texture_mag_filter(9729)
      .texture_min_filter(9729)
      .build();
   public ResourceLocation texture;
   public int sfactor;
   public int dfactor;
   public int texture_min_filter;
   public int texture_mag_filter;
   public Vec2 uv0;
   public Vec2 uv1;
   public Vec2 uv2;
   public Vec2 uv3;

   public OptimizedParticleType(ResourceLocation texture) {
      this.texture = texture;
   }

   @Override
   public void setup() {
      RenderSystem.setShaderTexture(0, this.texture);
      GL46.glBlendFunc(this.sfactor, this.dfactor);
      GL46.glTexParameteri(3553, 10241, this.texture_min_filter);
      GL46.glTexParameteri(3553, 10240, this.texture_mag_filter);
      int shaderProgram = GL46.glGetInteger(35725);
      GL46.glUniform2f(GL46.glGetUniformLocation(shaderProgram, "uv0"), this.uv0.x, this.uv0.y);
      GL46.glUniform2f(GL46.glGetUniformLocation(shaderProgram, "uv1"), this.uv1.x, this.uv1.y);
      GL46.glUniform2f(GL46.glGetUniformLocation(shaderProgram, "uv2"), this.uv2.x, this.uv2.y);
      GL46.glUniform2f(GL46.glGetUniformLocation(shaderProgram, "uv3"), this.uv3.x, this.uv3.y);
   }

   @Override
   public boolean equals(AbstractParticleType other) {
      if (other instanceof OptimizedParticleType type
         && this.texture.equals(type.texture)
         && this.sfactor == type.sfactor
         && this.dfactor == type.dfactor
         && this.texture_min_filter == type.texture_min_filter
         && this.texture_mag_filter == type.texture_mag_filter
         && this.uv0.equals(type.uv0)
         && this.uv1.equals(type.uv1)
         && this.uv2.equals(type.uv2)
         && this.uv3.equals(type.uv3)) {
         return true;
      }

      return false;
   }

   @Generated
   private static int $default$sfactor() {
      return 770;
   }

   @Generated
   private static int $default$dfactor() {
      return 771;
   }

   @Generated
   private static int $default$texture_min_filter() {
      return 9728;
   }

   @Generated
   private static int $default$texture_mag_filter() {
      return 9728;
   }

   @Generated
   private static Vec2 $default$uv0() {
      return new Vec2(1.0F, 1.0F);
   }

   @Generated
   private static Vec2 $default$uv1() {
      return new Vec2(1.0F, 0.0F);
   }

   @Generated
   private static Vec2 $default$uv2() {
      return new Vec2(0.0F, 1.0F);
   }

   @Generated
   private static Vec2 $default$uv3() {
      return new Vec2(0.0F, 0.0F);
   }

   @Generated
   public static OptimizedParticleType.OptimizedParticleTypeBuilder builder() {
      return new OptimizedParticleType.OptimizedParticleTypeBuilder();
   }

   @Generated
   public OptimizedParticleType(
      ResourceLocation texture, int sfactor, int dfactor, int texture_min_filter, int texture_mag_filter, Vec2 uv0, Vec2 uv1, Vec2 uv2, Vec2 uv3
   ) {
      this.texture = texture;
      this.sfactor = sfactor;
      this.dfactor = dfactor;
      this.texture_min_filter = texture_min_filter;
      this.texture_mag_filter = texture_mag_filter;
      this.uv0 = uv0;
      this.uv1 = uv1;
      this.uv2 = uv2;
      this.uv3 = uv3;
   }

   @Generated
   public static class OptimizedParticleTypeBuilder {
      @Generated
      private ResourceLocation texture;
      @Generated
      private boolean sfactor$set;
      @Generated
      private int sfactor$value;
      @Generated
      private boolean dfactor$set;
      @Generated
      private int dfactor$value;
      @Generated
      private boolean texture_min_filter$set;
      @Generated
      private int texture_min_filter$value;
      @Generated
      private boolean texture_mag_filter$set;
      @Generated
      private int texture_mag_filter$value;
      @Generated
      private boolean uv0$set;
      @Generated
      private Vec2 uv0$value;
      @Generated
      private boolean uv1$set;
      @Generated
      private Vec2 uv1$value;
      @Generated
      private boolean uv2$set;
      @Generated
      private Vec2 uv2$value;
      @Generated
      private boolean uv3$set;
      @Generated
      private Vec2 uv3$value;

      @Generated
      OptimizedParticleTypeBuilder() {
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder texture(ResourceLocation texture) {
         this.texture = texture;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder sfactor(int sfactor) {
         this.sfactor$value = sfactor;
         this.sfactor$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder dfactor(int dfactor) {
         this.dfactor$value = dfactor;
         this.dfactor$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder texture_min_filter(int texture_min_filter) {
         this.texture_min_filter$value = texture_min_filter;
         this.texture_min_filter$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder texture_mag_filter(int texture_mag_filter) {
         this.texture_mag_filter$value = texture_mag_filter;
         this.texture_mag_filter$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder uv0(Vec2 uv0) {
         this.uv0$value = uv0;
         this.uv0$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder uv1(Vec2 uv1) {
         this.uv1$value = uv1;
         this.uv1$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder uv2(Vec2 uv2) {
         this.uv2$value = uv2;
         this.uv2$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType.OptimizedParticleTypeBuilder uv3(Vec2 uv3) {
         this.uv3$value = uv3;
         this.uv3$set = true;
         return this;
      }

      @Generated
      public OptimizedParticleType build() {
         int sfactor$value = this.sfactor$value;
         if (!this.sfactor$set) {
            sfactor$value = OptimizedParticleType.$default$sfactor();
         }

         int dfactor$value = this.dfactor$value;
         if (!this.dfactor$set) {
            dfactor$value = OptimizedParticleType.$default$dfactor();
         }

         int texture_min_filter$value = this.texture_min_filter$value;
         if (!this.texture_min_filter$set) {
            texture_min_filter$value = OptimizedParticleType.$default$texture_min_filter();
         }

         int texture_mag_filter$value = this.texture_mag_filter$value;
         if (!this.texture_mag_filter$set) {
            texture_mag_filter$value = OptimizedParticleType.$default$texture_mag_filter();
         }

         Vec2 uv0$value = this.uv0$value;
         if (!this.uv0$set) {
            uv0$value = OptimizedParticleType.$default$uv0();
         }

         Vec2 uv1$value = this.uv1$value;
         if (!this.uv1$set) {
            uv1$value = OptimizedParticleType.$default$uv1();
         }

         Vec2 uv2$value = this.uv2$value;
         if (!this.uv2$set) {
            uv2$value = OptimizedParticleType.$default$uv2();
         }

         Vec2 uv3$value = this.uv3$value;
         if (!this.uv3$set) {
            uv3$value = OptimizedParticleType.$default$uv3();
         }

         return new OptimizedParticleType(
            this.texture, sfactor$value, dfactor$value, texture_min_filter$value, texture_mag_filter$value, uv0$value, uv1$value, uv2$value, uv3$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "OptimizedParticleType.OptimizedParticleTypeBuilder(texture="
            + this.texture
            + ", sfactor$value="
            + this.sfactor$value
            + ", dfactor$value="
            + this.dfactor$value
            + ", texture_min_filter$value="
            + this.texture_min_filter$value
            + ", texture_mag_filter$value="
            + this.texture_mag_filter$value
            + ", uv0$value="
            + this.uv0$value
            + ", uv1$value="
            + this.uv1$value
            + ", uv2$value="
            + this.uv2$value
            + ", uv3$value="
            + this.uv3$value
            + ")";
      }
   }
}
