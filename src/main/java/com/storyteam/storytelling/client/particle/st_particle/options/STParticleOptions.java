package com.storyteam.storytelling.client.particle.st_particle.options;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;

public abstract class STParticleOptions<T extends STParticleOptions<?>> {
   public abstract void writeToNetwork(FriendlyByteBuf var1);

   public abstract String writeToString();

   public abstract Codec<T> codec();
}
