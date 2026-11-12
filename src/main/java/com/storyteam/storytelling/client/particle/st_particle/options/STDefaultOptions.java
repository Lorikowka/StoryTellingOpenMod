package com.storyteam.storytelling.client.particle.st_particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.storyteam.storytelling.client.particle.st_particle.STParticleData;
import com.storyteam.storytelling.util.Color;
import java.io.StringReader;
import lombok.Generated;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class STDefaultOptions extends STParticleOptions<STDefaultOptions> {
   public float size;
   public int lifetime;
   public float r;
   public float g;
   public float b;
   public float alpha;
   public float friction;
   public boolean hasPhysics;
   public boolean standardBlending;
   public boolean glowing;
   public boolean blur;
   public static final Codec<STDefaultOptions> CODEC = RecordCodecBuilder.create(
      p -> p.group(
               Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
               Codec.INT.fieldOf("lifetime").forGetter(o -> o.lifetime),
               Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
               Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
               Codec.FLOAT.fieldOf("b").forGetter(o -> o.b),
               Codec.FLOAT.fieldOf("a").forGetter(o -> o.alpha),
               Codec.FLOAT.fieldOf("friction").forGetter(o -> o.friction),
               Codec.BOOL.fieldOf("hasPhysics").forGetter(o -> o.hasPhysics),
               Codec.BOOL.fieldOf("standardBlending").forGetter(o -> o.standardBlending),
               Codec.BOOL.fieldOf("glowing").forGetter(o -> o.glowing),
               Codec.BOOL.fieldOf("blur").forGetter(o -> o.blur)
            )
            .apply(p, STDefaultOptions::new)
   );

   public STDefaultOptions(FriendlyByteBuf buf) {
      this.size = buf.readFloat();
      this.lifetime = buf.readInt();
      this.r = buf.readFloat();
      this.g = buf.readFloat();
      this.b = buf.readFloat();
      this.alpha = buf.readFloat();
      this.friction = buf.readFloat();
      this.hasPhysics = buf.readBoolean();
      this.standardBlending = buf.readBoolean();
      this.glowing = buf.readBoolean();
      this.blur = buf.readBoolean();
   }

   public STDefaultOptions(ParticleType<STParticleData> type, StringReader reader) {
   }

   public STDefaultOptions(
      float size, int lifetime, float r, float g, float b, float a, float friction, boolean hasPhysics, boolean standardBlending, boolean glowing, boolean blur
   ) {
      this.size = size;
      this.lifetime = lifetime;
      this.r = r;
      this.g = g;
      this.b = b;
      this.alpha = a;
      this.friction = friction;
      this.hasPhysics = hasPhysics;
      this.standardBlending = standardBlending;
      this.glowing = glowing;
      this.blur = blur;
   }

   public STDefaultOptions(float size, int lifetime, Color color, float friction, boolean hasPhysics, boolean standardBlending, boolean glowing, boolean blur) {
      this.size = size;
      this.lifetime = lifetime;
      this.r = (float)color.getRed() / 255.0F;
      this.g = (float)color.getGreen() / 255.0F;
      this.b = (float)color.getBlue() / 255.0F;
      this.alpha = (float)color.getAlpha() / 255.0F;
      this.friction = friction;
      this.hasPhysics = hasPhysics;
      this.standardBlending = standardBlending;
      this.glowing = glowing;
      this.blur = blur;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeFloat(this.size);
      buf.writeInt(this.lifetime);
      buf.writeFloat(this.r);
      buf.writeFloat(this.g);
      buf.writeFloat(this.b);
      buf.writeFloat(this.alpha);
      buf.writeFloat(this.friction);
      buf.writeBoolean(this.hasPhysics);
      buf.writeBoolean(this.standardBlending);
      buf.writeBoolean(this.glowing);
      buf.writeBoolean(this.blur);
   }

   @Override
   public String writeToString() {
      return "";
   }

   @Override
   public Codec<STDefaultOptions> codec() {
      return CODEC;
   }

   @Generated
   private static float $default$size() {
      return 0.25F;
   }

   @Generated
   private static int $default$lifetime() {
      return 60;
   }

   @Generated
   private static float $default$r() {
      return 1.0F;
   }

   @Generated
   private static float $default$g() {
      return 1.0F;
   }

   @Generated
   private static float $default$b() {
      return 1.0F;
   }

   @Generated
   private static float $default$alpha() {
      return 1.0F;
   }

   @Generated
   private static float $default$friction() {
      return 1.0F;
   }

   @Generated
   private static boolean $default$hasPhysics() {
      return false;
   }

   @Generated
   private static boolean $default$standardBlending() {
      return false;
   }

   @Generated
   private static boolean $default$glowing() {
      return false;
   }

   @Generated
   private static boolean $default$blur() {
      return false;
   }

   @Generated
   public static STDefaultOptions.DefaultOptionsBuilder builder() {
      return new STDefaultOptions.DefaultOptionsBuilder();
   }

   public static class DefaultOptionsBuilder {
      @Generated
      private boolean size$set;
      @Generated
      private float size$value;
      @Generated
      private boolean lifetime$set;
      @Generated
      private int lifetime$value;
      @Generated
      private boolean r$set;
      @Generated
      private float r$value;
      @Generated
      private boolean g$set;
      @Generated
      private float g$value;
      @Generated
      private boolean b$set;
      @Generated
      private float b$value;
      @Generated
      private boolean alpha$set;
      @Generated
      private float alpha$value;
      @Generated
      private boolean friction$set;
      @Generated
      private float friction$value;
      @Generated
      private boolean hasPhysics$set;
      @Generated
      private boolean hasPhysics$value;
      @Generated
      private boolean standardBlending$set;
      @Generated
      private boolean standardBlending$value;
      @Generated
      private boolean glowing$set;
      @Generated
      private boolean glowing$value;
      @Generated
      private boolean blur$set;
      @Generated
      private boolean blur$value;

      public STDefaultOptions.DefaultOptionsBuilder color(Color color) {
         this.r((float)color.getRed() / 255.0F);
         this.g((float)color.getGreen() / 255.0F);
         this.b((float)color.getBlue() / 255.0F);
         return this;
      }

      @Generated
      DefaultOptionsBuilder() {
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder size(float size) {
         this.size$value = size;
         this.size$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder lifetime(int lifetime) {
         this.lifetime$value = lifetime;
         this.lifetime$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder r(float r) {
         this.r$value = r;
         this.r$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder g(float g) {
         this.g$value = g;
         this.g$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder b(float b) {
         this.b$value = b;
         this.b$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder alpha(float alpha) {
         this.alpha$value = alpha;
         this.alpha$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder friction(float friction) {
         this.friction$value = friction;
         this.friction$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder hasPhysics(boolean hasPhysics) {
         this.hasPhysics$value = hasPhysics;
         this.hasPhysics$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder standardBlending(boolean standardBlending) {
         this.standardBlending$value = standardBlending;
         this.standardBlending$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder glowing(boolean glowing) {
         this.glowing$value = glowing;
         this.glowing$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions.DefaultOptionsBuilder blur(boolean blur) {
         this.blur$value = blur;
         this.blur$set = true;
         return this;
      }

      @Generated
      public STDefaultOptions build() {
         float size$value = this.size$value;
         if (!this.size$set) {
            size$value = STDefaultOptions.$default$size();
         }

         int lifetime$value = this.lifetime$value;
         if (!this.lifetime$set) {
            lifetime$value = STDefaultOptions.$default$lifetime();
         }

         float r$value = this.r$value;
         if (!this.r$set) {
            r$value = STDefaultOptions.$default$r();
         }

         float g$value = this.g$value;
         if (!this.g$set) {
            g$value = STDefaultOptions.$default$g();
         }

         float b$value = this.b$value;
         if (!this.b$set) {
            b$value = STDefaultOptions.$default$b();
         }

         float alpha$value = this.alpha$value;
         if (!this.alpha$set) {
            alpha$value = STDefaultOptions.$default$alpha();
         }

         float friction$value = this.friction$value;
         if (!this.friction$set) {
            friction$value = STDefaultOptions.$default$friction();
         }

         boolean hasPhysics$value = this.hasPhysics$value;
         if (!this.hasPhysics$set) {
            hasPhysics$value = STDefaultOptions.$default$hasPhysics();
         }

         boolean standardBlending$value = this.standardBlending$value;
         if (!this.standardBlending$set) {
            standardBlending$value = STDefaultOptions.$default$standardBlending();
         }

         boolean glowing$value = this.glowing$value;
         if (!this.glowing$set) {
            glowing$value = STDefaultOptions.$default$glowing();
         }

         boolean blur$value = this.blur$value;
         if (!this.blur$set) {
            blur$value = STDefaultOptions.$default$blur();
         }

         return new STDefaultOptions(
            size$value,
            lifetime$value,
            r$value,
            g$value,
            b$value,
            alpha$value,
            friction$value,
            hasPhysics$value,
            standardBlending$value,
            glowing$value,
            blur$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "STDefaultOptions.DefaultOptionsBuilder(size$value="
            + this.size$value
            + ", lifetime$value="
            + this.lifetime$value
            + ", r$value="
            + this.r$value
            + ", g$value="
            + this.g$value
            + ", b$value="
            + this.b$value
            + ", alpha$value="
            + this.alpha$value
            + ", friction$value="
            + this.friction$value
            + ", hasPhysics$value="
            + this.hasPhysics$value
            + ", standardBlending$value="
            + this.standardBlending$value
            + ", glowing$value="
            + this.glowing$value
            + ", blur$value="
            + this.blur$value
            + ")";
      }
   }
}
