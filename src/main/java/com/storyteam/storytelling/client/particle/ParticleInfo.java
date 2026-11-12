package com.storyteam.storytelling.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record ParticleInfo(
   StoryTexturedSheetParticle particle,
   ClientLevel level,
   Vec3 movement,
   Vec3 pos,
   Vec3 prevPos,
   RandomSource random,
   boolean onGround,
   float width,
   float height,
   float lifetime,
   float age,
   float gravity,
   float friction,
   AABB bb
) {
}
