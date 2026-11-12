package com.storyteam.storytelling.client.particle.controllers.custom_controller;

import com.storyteam.storytelling.client.particle.ParticleInfo;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface MovementFunction<T> {
   Vec3 apply(ParticleInfo var1, T var2);
}
