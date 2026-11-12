package com.storyteam.storytelling.client.particle.controllers;

import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;

public interface ControllerData {
   @Nonnull
   <T extends ControllerData> ControllerType<T> getType();

   void writeToNetwork(FriendlyByteBuf var1);

   String writeToString();
}
