package com.storyteam.storytelling.client.particle.st_particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.storyteam.storytelling.util.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

public class STGradientOptions extends STParticleOptions<STGradientOptions> {
   public List<STGradientOptions.GradientPoint> gradients;
   public static final Codec<STGradientOptions.GradientPoint> GRADIENT_POINT_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               Codec.INT.fieldOf("time").forGetter(g -> g.time),
               Codec.FLOAT.fieldOf("r").forGetter(g -> g.r),
               Codec.FLOAT.fieldOf("g").forGetter(g -> g.g),
               Codec.FLOAT.fieldOf("b").forGetter(g -> g.b)
            )
            .apply(instance, STGradientOptions.GradientPoint::new)
   );
   public static final Codec<STGradientOptions> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(GRADIENT_POINT_CODEC.listOf().fieldOf("gradients").forGetter(o -> o.gradients)).apply(instance, STGradientOptions::new)
   );

   public STGradientOptions(List<STGradientOptions.GradientPoint> gradients) {
      this.gradients = gradients;
   }

   public STGradientOptions(FriendlyByteBuf buf) {
      int size = buf.readInt();
      this.gradients = new ArrayList<>();

      for (int i = 0; i < size; i++) {
         int time = buf.readInt();
         float r = buf.readFloat();
         float g = buf.readFloat();
         float b = buf.readFloat();
         this.gradients.add(new STGradientOptions.GradientPoint(time, r, g, b));
      }
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeInt(this.gradients.size());

      for (STGradientOptions.GradientPoint gradient : this.gradients) {
         buf.writeInt(gradient.time);
         buf.writeFloat(gradient.r);
         buf.writeFloat(gradient.g);
         buf.writeFloat(gradient.b);
      }
   }

   @Override
   public String writeToString() {
      return this.gradients.toString();
   }

   @Override
   public Codec<STGradientOptions> codec() {
      return CODEC;
   }

   public static class GradientPoint {
      public final int time;
      public final float r;
      public final float g;
      public final float b;

      public GradientPoint(int time, float r, float g, float b) {
         this.time = time;
         this.r = r;
         this.g = g;
         this.b = b;
      }

      public GradientPoint(int time, Color color) {
         this.time = time;
         this.r = (float)color.getRed() / 255.0F;
         this.g = (float)color.getGreen() / 255.0F;
         this.b = (float)color.getBlue() / 255.0F;
      }
   }
}
