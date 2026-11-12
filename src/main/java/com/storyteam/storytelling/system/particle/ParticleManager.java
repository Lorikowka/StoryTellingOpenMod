package com.storyteam.storytelling.system.particle;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;

public abstract class ParticleManager<P extends IParticle, R> implements ISystem {
   final Set<P> particles = new HashSet<>();

   public ParticleManager(Supplier<IParticleRenderer<P, R>> rendererFactory) {
      this.initRenderManager(new ParticleRenderManager<>(this, rendererFactory.get()));
      MinecraftForge.EVENT_BUS.addListener(this::clientTick);
   }

   public final P addParticle(P particle) {
      synchronized (this.particles) {
         this.particles.add(particle);
         return particle;
      }
   }

   public void clearParticles() {
      synchronized (this.particles) {
         this.particles.clear();
      }
   }

   protected void clientTick(ClientTickEvent event) {
      synchronized (this.particles) {
         this.particles.removeIf(IParticle::tick);
      }
   }

   public int getParticlesCount() {
      synchronized (this.particles) {
         return this.particles.size();
      }
   }

   abstract void initRenderManager(ParticleRenderManager<P, R> var1);
}
