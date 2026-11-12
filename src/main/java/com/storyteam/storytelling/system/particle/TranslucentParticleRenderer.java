package com.storyteam.storytelling.system.particle;

import java.util.Set;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;

public class TranslucentParticleRenderer implements IParticleRenderer<TranslucentParticle, RenderLevelStageEvent> {
   @OnlyIn(Dist.CLIENT)
   public void renderParticles(RenderLevelStageEvent event, Set<TranslucentParticle> particles) {
      if (event.getStage() == Stage.AFTER_BLOCK_ENTITIES) {
         for (TranslucentParticle particle : particles) {
            particle.render(event.getPoseStack());
         }
      }
   }
}
