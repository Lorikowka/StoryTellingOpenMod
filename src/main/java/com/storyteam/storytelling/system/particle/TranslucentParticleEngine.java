package com.storyteam.storytelling.system.particle;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.MinecraftForge;

public class TranslucentParticleEngine extends ParticleManager<TranslucentParticle, RenderLevelStageEvent> {
   public static final TranslucentParticleEngine instance = new TranslucentParticleEngine();

   public TranslucentParticleEngine() {
      super(TranslucentParticleRenderer::new);
   }

   @Override
   void initRenderManager(ParticleRenderManager<TranslucentParticle, RenderLevelStageEvent> manager) {
      MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
         if (event.getStage() == Stage.AFTER_LEVEL) {
            manager.render(event);
         }
      });
   }
}
