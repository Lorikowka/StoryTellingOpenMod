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
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

public class CircleTintData implements StoryParticleData<CircleTintData> {
   private final Color tint;
   private final float diameter;
   private final int lifeTime;
   private final int fadeInTime;
   private final float resizeSpeed;
   private final boolean shouldCollide;
   private final boolean standardBlending;
   @Nonnull
   private ControllerData controllerData = new StandartController();
   public static final Codec<CircleTintData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               Codec.INT.fieldOf("tint").forGetter(d -> d.tint.getRGB()),
               Codec.FLOAT.fieldOf("diameter").forGetter(d -> d.diameter),
               Codec.INT.fieldOf("life_time").forGetter(d -> d.lifeTime),
               Codec.FLOAT.fieldOf("resize_speed").forGetter(d -> d.resizeSpeed),
               Codec.BOOL.fieldOf("should_collide").forGetter(d -> d.shouldCollide)
            )
            .apply(
               instance,
               (tintRGB, diameter1, lifeTime1, resizeSpeed1, shouldCollide1) -> new CircleTintData(
                     tintRGB, diameter1, lifeTime1, resizeSpeed1, shouldCollide1, new StandartController()
                  )
            )
   );
   public static final Deserializer<CircleTintData> DESERIALIZER = new Deserializer<CircleTintData>() {
      @Nonnull
      public CircleTintData fromCommand(@Nonnull ParticleType<CircleTintData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
         reader.expect(' ');
         int red = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int green = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int blue = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         float diameter = CircleTintData.validateDiameter(reader.readFloat());
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
         return new CircleTintData(new Color(red, green, blue), diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromCommand(reader));
      }

      public CircleTintData fromNetwork(@Nonnull ParticleType<CircleTintData> type, FriendlyByteBuf buf) {
         int red = Mth.clamp(buf.readInt(), 0, 255);
         int green = Mth.clamp(buf.readInt(), 0, 255);
         int blue = Mth.clamp(buf.readInt(), 0, 255);
         Color color = new Color(red, green, blue);
         float diameter = CircleTintData.validateDiameter(buf.readFloat());
         int fadeIn = buf.readInt();
         int lifeTime = buf.readInt();
         float resizeSpeed = buf.readFloat();
         boolean shouldCollide = buf.readBoolean();
         boolean standardBlending = buf.readBoolean();
         int id = buf.readInt();
         ControllerType<? extends ControllerData> ctype = (ControllerType<? extends ControllerData>)ParticleTypesST.ParticleBehaviorRegistry.byId(id);
         return new CircleTintData(color, diameter, fadeIn, lifeTime, resizeSpeed, shouldCollide, standardBlending, ctype.fromNetwork(buf));
      }
   };

   public CircleTintData(
      Color tint, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending, ControllerData controllerData
   ) {
      this.tint = tint;
      this.lifeTime = lifeTime;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = standardBlending;
      this.controllerData = (ControllerData)(controllerData == null ? new StandartController() : controllerData);
      this.fadeInTime = 0;
   }

   public CircleTintData(Color tint, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, boolean standardBlending) {
      this.tint = tint;
      this.lifeTime = lifeTime;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = standardBlending;
      this.fadeInTime = 0;
   }

   public CircleTintData(
      Color tint,
      float diameter,
      int fadeInTime,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      ControllerData controllerData
   ) {
      this.tint = tint;
      this.fadeInTime = fadeInTime;
      this.lifeTime = lifeTime;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = standardBlending;
      this.controllerData = (ControllerData)(controllerData == null ? new StandartController() : controllerData);
   }

   public Color getTint() {
      return this.tint;
   }

   public int getLifeTime() {
      return this.lifeTime;
   }

   public int getFadeInTime() {
      return this.fadeInTime;
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
   public ParticleType<? extends CircleTintData> getType() {
      return ParticleTypesST.CIRCLE_TINT_TYPE;
   }

   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeInt(this.tint.getRed());
      buf.writeInt(this.tint.getGreen());
      buf.writeInt(this.tint.getBlue());
      buf.writeFloat(this.diameter);
      buf.writeInt(this.fadeInTime);
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
      return (float)Mth.clamp((double)diameter, 0.002, 95.0);
   }

   private CircleTintData(int tintRGB, float diameter, int lifeTime, float resizeSpeed, boolean shouldCollide, ControllerData controllerData) {
      this.tint = new Color(tintRGB);
      this.lifeTime = lifeTime;
      this.diameter = validateDiameter(diameter);
      this.resizeSpeed = resizeSpeed;
      this.shouldCollide = shouldCollide;
      this.standardBlending = false;
      this.controllerData = controllerData;
      this.fadeInTime = 0;
   }

   @Override
   public ControllerData getController() {
      return this.controllerData;
   }

   public CircleTintData setController(ControllerData data) {
      this.controllerData = (ControllerData)(data == null ? new StandartController() : data);
      return this;
   }
}
