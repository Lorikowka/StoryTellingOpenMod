package com.storyteam.storytelling.client.particle;

import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import net.minecraft.core.particles.ParticleOptions;

public interface StoryParticleData<T extends ParticleOptions> extends ParticleOptions {
   ControllerData getController();

   T setController(ControllerData var1);
}
