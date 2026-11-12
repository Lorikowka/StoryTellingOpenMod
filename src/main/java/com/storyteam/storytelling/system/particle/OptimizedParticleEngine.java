package com.storyteam.storytelling.system.particle;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;

public class OptimizedParticleEngine extends ParticleManager<OptimizedParticle, RenderLevelStageEvent> {
   public static OptimizedParticleEngine instance = new OptimizedParticleEngine();

   public OptimizedParticleEngine() {
      super(OptimizedParticleRenderer::new);
   }

   @Override
   void initRenderManager(ParticleRenderManager<OptimizedParticle, RenderLevelStageEvent> manager) {
      MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
         if (event.getStage() == Stage.AFTER_LEVEL) {
            manager.render(event);
         }
      });
   }
}
