package com.storyteam.storytelling.system.particle;

import java.util.Set;

public interface IParticleRenderer<P extends IParticle, RENDER_CONTEXT> {
   void renderParticles(RENDER_CONTEXT var1, Set<P> var2);
}
