package com.storyteam.storytelling.client.particle.st_particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

public class STAlphaInOutOptions extends STParticleOptions<STAlphaInOutOptions> {
   public int in;
   public int out;
   public static final Codec<STAlphaInOutOptions> CODEC = RecordCodecBuilder.create(
      p -> p.group(Codec.INT.fieldOf("in").forGetter(i -> i.in), Codec.INT.fieldOf("out").forGetter(i -> i.out)).apply(p, STAlphaInOutOptions::new)
   );

   public STAlphaInOutOptions(int in, int out) {
      this.in = in;
      this.out = out;
   }

   public STAlphaInOutOptions(FriendlyByteBuf buf) {
      this.in = buf.readInt();
      this.out = buf.readInt();
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeInt(this.in);
      buf.writeInt(this.out);
   }

   @Override
   public String writeToString() {
      return "";
   }

   @Override
   public Codec<STAlphaInOutOptions> codec() {
      return CODEC;
   }
}
