package com.storyteam.storytelling.system.particle;

public class ParticleRenderManager<P extends IParticle, RENDER_CONTEXT> {
   private final IParticleRenderer<P, RENDER_CONTEXT> renderer;
   private final ParticleManager<P, RENDER_CONTEXT> manager;
   private long lastRenderTime;

   public ParticleRenderManager(ParticleManager<P, RENDER_CONTEXT> manager, IParticleRenderer<P, RENDER_CONTEXT> renderer) {
      this.manager = manager;
      this.renderer = renderer;
   }

   public final void render(RENDER_CONTEXT renderContext) {
      synchronized (this.manager.particles) {
         if (this.lastRenderTime - System.nanoTime() > 100000000L) {
            this.lastRenderTime = System.nanoTime();
         }

         float dt = (float)(System.nanoTime() - this.lastRenderTime) / 1.0E9F;
         this.lastRenderTime = System.nanoTime();

         for (P particle : this.manager.particles) {
            particle.renderTick(dt);
         }

         this.renderer.renderParticles(renderContext, this.manager.particles);
      }
   }
}
