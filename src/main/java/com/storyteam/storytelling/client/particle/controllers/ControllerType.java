package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public interface ControllerType<T extends ControllerData> {
   @Nonnull
   Vec3 handleMovement(ParticleInfo var1, T var2);

   T fromCommand(StringReader var1) throws CommandSyntaxException;

   T fromNetwork(FriendlyByteBuf var1);
}
