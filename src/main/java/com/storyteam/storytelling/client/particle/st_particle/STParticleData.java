package com.storyteam.storytelling.client.particle.st_particle;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.storyteam.storytelling.client.particle.StoryParticleData;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.client.particle.controllers.ControllerType;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.client.particle.st_particle.options.STAlphaInOutOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STGradientOptions;
import com.storyteam.storytelling.client.particle.st_particle.options.STScalingInOutOptions;
import com.storyteam.storytelling.init.ParticleTypesST;
import java.util.Locale;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class STParticleData implements StoryParticleData<STParticleData> {
   private final ParticleType<? extends STParticleData> particleType;
   @Nonnull
   private ControllerData controllerData;
   protected STDefaultOptions defaultOptions;
   protected STScalingInOutOptions scalingOptions;
   protected STAlphaInOutOptions alphaOptions;
   protected STGradientOptions gradientInOutOptions;
   public static final Codec<STParticleData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               STDefaultOptions.CODEC.fieldOf("default_options").forGetter(STParticleData::getDefaultOptions),
               STScalingInOutOptions.CODEC.fieldOf("scaling_options").forGetter(STParticleData::getScalingOptions),
               STAlphaInOutOptions.CODEC.fieldOf("alpha_options").forGetter(STParticleData::getAlphaOptions),
               STGradientOptions.CODEC.fieldOf("gradientInOutOptions").forGetter(STParticleData::getGradientInOutOptions)
            )
            .apply(
               instance,
               (default_options1, scalingOptions1, alphaOptions1, gradientInOutOptions1) -> new STParticleData(
                     ParticleTypesST.QUAD_TINT_U, new StandartController(), default_options1, scalingOptions1, alphaOptions1, gradientInOutOptions1
                  )
            )
   );
   public static final Deserializer<STParticleData> DESERIALIZER = new Deserializer<STParticleData>() {
      public STParticleData fromCommand(@Nonnull ParticleType<STParticleData> type, @Nonnull StringReader reader) {
         return null;
      }

      @NotNull
      public STParticleData fromNetwork(@Nonnull ParticleType<STParticleData> type, FriendlyByteBuf buf) {
         ResourceLocation typeId = buf.readResourceLocation();
         ParticleType<? extends STParticleData> particleType = (ParticleType<? extends STParticleData>)ForgeRegistries.PARTICLE_TYPES.getValue(typeId);
         int id = buf.readInt();
         ControllerType<? extends ControllerData> ctype = (ControllerType<? extends ControllerData>)ParticleTypesST.ParticleBehaviorRegistry.byId(id);
         ControllerData data = ctype.fromNetwork(buf);
         STDefaultOptions defaultOptions1 = new STDefaultOptions(buf);
         STScalingInOutOptions scalingOptions = new STScalingInOutOptions(buf);
         STAlphaInOutOptions alphaOptions = new STAlphaInOutOptions(buf);
         STGradientOptions gradientInOutOptions = new STGradientOptions(buf);
         return new STParticleData(particleType, data, defaultOptions1, scalingOptions, alphaOptions, gradientInOutOptions);
      }
   };

   public STParticleData(
      ParticleType<? extends STParticleData> particleType,
      ControllerData controllerData,
      STDefaultOptions defaultOptions,
      STScalingInOutOptions scalingOptions,
      STAlphaInOutOptions alphaOptions,
      STGradientOptions gradientInOutOptions
   ) {
      this.particleType = particleType;
      this.controllerData = controllerData;
      this.defaultOptions = defaultOptions;
      this.scalingOptions = scalingOptions;
      this.alphaOptions = alphaOptions;
      this.gradientInOutOptions = gradientInOutOptions;
   }

   @Nonnull
   public ParticleType<? extends STParticleData> getType() {
      return this.particleType;
   }

   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeResourceLocation(ForgeRegistries.PARTICLE_TYPES.getKey(this.particleType));
      buf.writeInt(ParticleTypesST.ParticleBehaviorRegistry.getId(this.controllerData.getType()));
      this.controllerData.writeToNetwork(buf);
      this.defaultOptions.writeToNetwork(buf);
      this.scalingOptions.writeToNetwork(buf);
      this.alphaOptions.writeToNetwork(buf);
      this.gradientInOutOptions.writeToNetwork(buf);
   }

   public String writeToString() {
      return String.format(
         Locale.ROOT,
         "%s %s %s %s %s %s %s",
         ForgeRegistries.PARTICLE_TYPES.getKey(this.particleType),
         this.controllerData.writeToString(),
         this.defaultOptions.writeToString(),
         this.scalingOptions.writeToString(),
         this.alphaOptions.writeToString()
      );
   }

   @Override
   public ControllerData getController() {
      return this.controllerData;
   }

   public STParticleData setController(ControllerData data) {
      this.controllerData = (ControllerData)(data == null ? new StandartController() : data);
      return this;
   }

   @Generated
   public ParticleType<? extends STParticleData> getParticleType() {
      return this.particleType;
   }

   @Nonnull
   @Generated
   public ControllerData getControllerData() {
      return this.controllerData;
   }

   @Generated
   public STDefaultOptions getDefaultOptions() {
      return this.defaultOptions;
   }

   @Generated
   public STScalingInOutOptions getScalingOptions() {
      return this.scalingOptions;
   }

   @Generated
   public STAlphaInOutOptions getAlphaOptions() {
      return this.alphaOptions;
   }

   @Generated
   public STGradientOptions getGradientInOutOptions() {
      return this.gradientInOutOptions;
   }
}
