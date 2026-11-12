package com.storyteam.storytelling.system.particle;

import net.minecraft.world.phys.Vec3;

public interface ParticleFactory<T extends IParticle> {
   T create(Vec3 var1, Vec3 var2);
}
