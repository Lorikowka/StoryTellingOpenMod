package com.storyteam.storytelling.client.render.entity.processor;

import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

public class WithScale<T extends Entity & IAnimatedEntity> implements IProcessor<T> {
   private final Vector3f scale;

   public WithScale(Vector3f scale) {
      this.scale = scale;
   }

   public WithScale(float scale) {
      this.scale = new Vector3f(scale, scale, scale);
   }

   public void process(T object, IGeometricModel model, float partialTick) {
      model.getRoot().getScale().mul(this.scale.x(), this.scale.y(), this.scale.z());
   }

   public Vector3f getScale() {
      return this.scale;
   }
}
