package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.storyteam.storytelling.client.particle.StoryParticleData;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.client.particle.controllers.ControllerType;
import com.storyteam.storytelling.client.particle.controllers.StandartController;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.util.Color;
import java.util.Locale;
import javax.annotation.Nonnull;
import lombok.Generated;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

public class CircleTintWithTrailData implements StoryParticleData<CircleTintWithTrailData> {
   private final Color tint;
   private final float diameter;
   private final int lifeTime;
   private final float resizeSpeed;
   private final boolean shouldCollide;
   private final boolean standardBlending;
   private float trailLifetimeModifier;
   @Nonnull
   private ControllerData controllerData = new StandartController();
   public static final Codec<CircleTintWithTrailData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               Codec.INT.fieldOf("tint").forGetter(d -> d.tint.getRGB()),
               Codec.FLOAT.fieldOf("diameter").forGetter(d -> d.diameter),
               Codec.FLOAT.fieldOf("trailLifetimeModifier").forGetter(d -> d.trailLifetimeModifier),
               Codec.INT.fieldOf("life_time").forGetter(d -> d.lifeTime),
               Codec.FLOAT.fieldOf("resize_speed").forGetter(d -> d.resizeSpeed),
               Codec.BOOL.fieldOf("should_collide").forGetter(d -> d.shouldCollide)
            )
            .apply(
               instance,
               (tintRGB, diameter1, trailMod, lifeTime1, resizeSpeed1, shouldCollide1) -> new CircleTintWithTrailData(
                     tintRGB, diameter1, trailMod, lifeTime1, resizeSpeed1, shouldCollide1, new StandartController()
                  )
            )
   );
   public static final Deserializer<CircleTintWithTrailData> DESERIALIZER = new Deserializer<CircleTintWithTrailData>() {
      @Nonnull
      public CircleTintWithTrailData fromCommand(@Nonnull ParticleType<CircleTintWithTrailData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
         reader.expect(' ');
         int red = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int green = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int blue = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         float diameter = CircleTintWithTrailData.validateDiameter(reader.readFloat());
         reader.expect(' ');
         float trailLifetimeModifier = reader.readFloat();
         reader.expect(' ');
         int lifeTime = reader.readInt();
         reader.expect(' ');
         float resizeSpeed = reader.readFloat();
         reader.expect(' ');
         boolean shouldCollide = reader.readBoolean();
         reader.expect(' ');
         boolean standardBlending = reader.readBoolean();
         reader.expect(' ');
         int id = reader.readInt();
         ControllerType<? extends ControllerData> ctype = (ControllerType<? extends ControllerData>)ParticleTypesST.ParticleBehaviorRegistry.byId(id);
         return new CircleTintWithTrailData(
            new Color(red, green, blue), diameter, trailLifetimeModifier, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromCommand(reader)
         );
      }

      public CircleTintWithTrailData fromNetwork(@Nonnull ParticleType<CircleTintWithTrailData> type, FriendlyByteBuf buf) {
         int red = Mth.clamp(buf.readInt(), 0, 255);
         int green = Mth.clamp(buf.readInt(), 0, 255);
         int blue = Mth.clamp(buf.readInt(), 0, 255);
         Color color = new Color(red, green, blue);
         float diameter = CircleTintWithTrailData.validateDiameter(buf.readFloat());
         float trailLifetimeModifier = buf.readFloat();
         int lifeTime = buf.readInt();
         float resizeSpeed = buf.readFloat();
         boolean shouldCollide = buf.readBoolean();
         boolean standardBlending = buf.readBoolean();
         int id = buf.readInt();
         ControllerType<? extends ControllerData> ctype = (ControllerType<? extends ControllerData>)ParticleTypesST.ParticleBehaviorRegistry.byId(id);
         return new CircleTintWithTrailData(
            color, diameter, trailLifetimeModifier, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromNetwork(buf)
         );
      }
   };

   public CircleTintWithTrailData(
      Color tint,
      float trailLifetimeModifier,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      ControllerData controllerData
   ) {
      this.tint = tint;
      this.lifeTime = lifeTime;
      this.trailLifetimeModifier = trailLifetimeModifier;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = standardBlending;
      this.controllerData = (ControllerData)(controllerData == null ? new StandartController() : controllerData);
   }

   public CircleTintWithTrailData(
      Color tint, float trailLifetimeModifier, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending
   ) {
      this.tint = tint;
      this.lifeTime = lifeTime;
      this.trailLifetimeModifier = trailLifetimeModifier;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = standardBlending;
   }

   public Color getTint() {
      return this.tint;
   }

   public int getLifeTime() {
      return this.lifeTime;
   }

   public float getDiameter() {
      return this.diameter;
   }

   public float getResizeSpeed() {
      return this.resizeSpeed;
   }

   public boolean shouldCollide() {
      return this.shouldCollide;
   }

   public boolean standardBlending() {
      return this.standardBlending;
   }

   @Nonnull
   public ParticleType<CircleTintWithTrailData> getType() {
      return ParticleTypesST.CIRCLE_TINT_TRAIL_TYPE;
   }

   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeInt(this.tint.getRed());
      buf.writeInt(this.tint.getGreen());
      buf.writeInt(this.tint.getBlue());
      buf.writeFloat(this.diameter);
      buf.writeFloat(this.trailLifetimeModifier);
      buf.writeInt(this.lifeTime);
      buf.writeFloat(this.resizeSpeed);
      buf.writeBoolean(this.shouldCollide);
      buf.writeBoolean(this.standardBlending);
      buf.writeInt(ParticleTypesST.ParticleBehaviorRegistry.getId(this.controllerData.getType()));
      this.controllerData.writeToNetwork(buf);
   }

   @Nonnull
   public String writeToString() {
      return String.format(
         Locale.ROOT,
         "%s %d %d %d %.2f %d %f %b %s",
         ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
         this.tint.getRed(),
         this.tint.getGreen(),
         this.tint.getBlue(),
         this.diameter,
         this.lifeTime,
         this.resizeSpeed,
         this.shouldCollide,
         this.controllerData.writeToString()
      );
   }

   private static float validateDiameter(float diameter) {
      return (float)Mth.clamp((double)diameter, 0.05, 5.0);
   }

   private CircleTintWithTrailData(
      int tintRGB, float trailLifetimeModifier, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, ControllerData controllerData
   ) {
      this.tint = new Color(tintRGB);
      this.lifeTime = lifeTime;
      this.diameter = validateDiameter(diameter);
      this.trailLifetimeModifier = trailLifetimeModifier;
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = false;
      this.controllerData = controllerData;
   }

   @Override
   public ControllerData getController() {
      return this.controllerData;
   }

   public CircleTintWithTrailData setController(ControllerData data) {
      this.controllerData = (ControllerData)(data == null ? new StandartController() : data);
      return this;
   }

   @Generated
   public float getTrailLifetimeModifier() {
      return this.trailLifetimeModifier;
   }
}
