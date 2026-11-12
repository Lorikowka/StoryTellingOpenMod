package com.storyteam.storytelling.client.particle.st_particle;

import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.client.particle.st_particle.options.STAlphaInOutOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STGradientOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STScalingInOutOptions;
import com.storyteam.storytelling.util.Color;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleType;

public class STParticleDataBuilder {
   @Nonnull
   private ControllerData controllerData = new StandartController();
   private final STDefaultOptions defaultOptions;
   private final STScalingInOutOptions scalingOptions = new STScalingInOutOptions(0, 0);
   private final STAlphaInOutOptions alphaOptions = new STAlphaInOutOptions(0, 0);
   private final STGradientOptions gradientOptions = new STGradientOptions(new ArrayList<>());
   private final ParticleType<? extends STParticleData> particleData;

   public STParticleDataBuilder(STDefaultOptions defaultOptions, ParticleType<STParticleData> particleData) {
      this.defaultOptions = defaultOptions;
      this.particleData = particleData;
   }

   public STParticleDataBuilder setScalingOptions(int in, int out) {
      this.scalingOptions.in = in;
      this.scalingOptions.out = out;
      return this;
   }

   public STParticleDataBuilder setAlphaOptions(int in, int out) {
      this.alphaOptions.in = in;
      this.alphaOptions.out = out;
      return this;
   }

   public STParticleDataBuilder addGradientPoint(int time, Color color) {
      this.gradientOptions.gradients.add(new STGradientOptions.GradientPoint(time, color));
      return this;
   }

   public STParticleDataBuilder addGradientPoint(STGradientOptions.GradientPoint point) {
      this.gradientOptions.gradients.add(point);
      return this;
   }

   public STParticleDataBuilder addGradientPoints(List<STGradientOptions.GradientPoint> points) {
      this.gradientOptions.gradients.addAll(points);
      return this;
   }

   public STParticleDataBuilder setControllerData(ControllerData controllerData) {
      this.controllerData = controllerData;
      return this;
   }

   public STParticleData build() {
      return new STParticleData(this.particleData, this.controllerData, this.defaultOptions, this.scalingOptions, this.alphaOptions, this.gradientOptions);
   }
}
