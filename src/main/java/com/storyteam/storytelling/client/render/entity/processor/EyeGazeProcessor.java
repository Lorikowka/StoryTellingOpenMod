package com.storyteam.storytelling.client.render.entity.processor;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.animation.EyeGazeSystem;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

public class EyeGazeProcessor<T extends AnimatedEntity & IGazingEntity> implements IProcessor<T> {
   public void process(T t, IGeometricModel model, float partialTick) {
      EyeGazeSystem gaze = t.getGazeSystem();
      gaze.preApply(t.getAnimationSystem(), partialTick);
   }
}
