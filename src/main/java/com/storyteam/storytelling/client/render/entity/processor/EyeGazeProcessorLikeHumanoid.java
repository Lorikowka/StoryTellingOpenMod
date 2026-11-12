package com.storyteam.storytelling.client.render.entity.processor;

import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.system.animation.EyeGazeSystem;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

public class EyeGazeProcessorLikeHumanoid<T extends AnimatedEntityLikeHumanoid> implements IProcessor<T> {
   public void process(T t, IGeometricModel model, float partialTick) {
      EyeGazeSystem gaze = t.getGazeSystem();
      gaze.preApply(t.getAnimationSystem(), partialTick);
   }
}
