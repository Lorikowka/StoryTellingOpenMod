package com.storyteam.storytelling.client.particle.circleTint;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.storyteam.storytelling.client.particle.controllers.ControllerData;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.util.Color;
import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.util.java.Cast;

public class CircleGradientData extends CircleTintData {
   protected final Color repaint;
   protected boolean isGlowing;
   protected int timeStartChangeColor;
   protected float alpha;
   public static final Codec<CircleGradientData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               Codec.INT.fieldOf("tint").forGetter(d -> d.getTint().getRGB()),
               Codec.INT.fieldOf("repaint").forGetter(d -> d.getRepaint().getRGB()),
               Codec.FLOAT.fieldOf("diameter").forGetter(CircleTintData::getDiameter),
               Codec.INT.fieldOf("life_time").forGetter(CircleTintData::getLifeTime),
               Codec.FLOAT.fieldOf("resize_speed").forGetter(CircleTintData::getResizeSpeed),
               Codec.BOOL.fieldOf("should_collide").forGetter(CircleTintData::shouldCollide),
               Codec.BOOL.fieldOf("standardBlending").forGetter(CircleTintData::standardBlending),
               Codec.BOOL.fieldOf("glow").forGetter(CircleGradientData::glowing),
               Codec.INT.fieldOf("timeStartChangeColor").forGetter(CircleGradientData::timeStartChangeColor),
               Codec.FLOAT.fieldOf("alpha").forGetter(CircleGradientData::alpha)
            )
            .apply(instance, CircleGradientData::new)
   );
   public static final Deserializer<CircleGradientData> DESERIALIZER = new Deserializer<CircleGradientData>() {
      @Nonnull
      public CircleGradientData fromCommand(@Nonnull ParticleType<CircleGradientData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
         CircleTintData circleTintData = (CircleTintData)CircleTintData.DESERIALIZER.fromCommand((ParticleType)Cast.cast(type), reader);
         reader.expect(' ');
         int r = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int g = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         int b = Mth.clamp(reader.readInt(), 0, 255);
         reader.expect(' ');
         boolean glowing = reader.readBoolean();
         reader.expect(' ');
         int timeStartChangeColor = reader.readInt();
         reader.expect(' ');
         float alpha = reader.readFloat();
         return new CircleGradientData(circleTintData, new Color(r, g, b), glowing, timeStartChangeColor, alpha);
      }

      public CircleGradientData fromNetwork(@Nonnull ParticleType<CircleGradientData> type, FriendlyByteBuf buf) {
         CircleTintData circleTintData = (CircleTintData)CircleTintData.DESERIALIZER.fromNetwork((ParticleType)Cast.cast(type), buf);
         Color color = new Color(buf.readInt());
         boolean isGlowing = buf.readBoolean();
         int timeStartChangeColor = buf.readInt();
         float alpha = buf.readFloat();
         return new CircleGradientData(circleTintData, color, isGlowing, timeStartChangeColor, alpha);
      }
   };

   public CircleGradientData(
      Color tint,
      Color repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor
   ) {
      super(tint, diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = 1.0F;
   }

   public CircleGradientData(
      Color tint,
      Color repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor,
      ControllerData controller
   ) {
      super(tint, diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending, controller);
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = 1.0F;
   }

   public CircleGradientData(
      Color tint,
      Color repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor,
      float alpha
   ) {
      super(tint, diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = alpha;
   }

   public CircleGradientData(
      Color tint,
      Color repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor,
      float alpha,
      ControllerData controller
   ) {
      super(tint, diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending, controller);
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = alpha;
   }

   public Color getRepaint() {
      return this.repaint;
   }

   public boolean glowing() {
      return this.isGlowing;
   }

   public int timeStartChangeColor() {
      return this.timeStartChangeColor;
   }

   public float alpha() {
      return this.alpha;
   }

   @Nonnull
   @Override
   public ParticleType<? extends CircleTintData> getType() {
      return ParticleTypesST.CIRCLE_TINT_GRADIENT_TYPE;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      super.writeToNetwork(buf);
      buf.writeInt(this.repaint.getRGB());
      buf.writeBoolean(this.isGlowing);
      buf.writeInt(this.timeStartChangeColor);
      buf.writeFloat(this.alpha);
   }

   @Nonnull
   @Override
   public String writeToString() {
      return super.writeToString()
         + String.format(
            " %d %d %d %b %d %.2f",
            this.repaint.getRed(),
            this.repaint.getBlue(),
            this.repaint.getGreen(),
            this.isGlowing,
            this.timeStartChangeColor,
            this.alpha
         );
   }

   private CircleGradientData(
      int tintRGB,
      int repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor
   ) {
      super(new Color(tintRGB), diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.repaint = new Color(repaint);
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = 1.0F;
   }

   public CircleGradientData(CircleTintData data, Color repaint, boolean isGlowing, int timeStartChangeColor) {
      super(data.getTint(), data.getDiameter(), data.getLifeTime(), data.getResizeSpeed(), data.shouldCollide(), data.standardBlending(), data.getController());
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = 1.0F;
   }

   private CircleGradientData(
      int tintRGB,
      int repaint,
      float diameter,
      int lifeTime,
      float resizeSpeed,
      boolean shouldCollide,
      boolean standardBlending,
      boolean isGlowing,
      int timeStartChangeColor,
      float alpha
   ) {
      super(new Color(tintRGB), diameter, lifeTime, resizeSpeed, shouldCollide, standardBlending);
      this.repaint = new Color(repaint);
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = alpha;
   }

   public CircleGradientData(CircleTintData data, Color repaint, boolean isGlowing, int timeStartChangeColor, float alpha) {
      super(data.getTint(), data.getDiameter(), data.getLifeTime(), data.getResizeSpeed(), data.shouldCollide(), data.standardBlending(), data.getController());
      this.repaint = repaint;
      this.isGlowing = isGlowing;
      this.timeStartChangeColor = timeStartChangeColor;
      this.alpha = alpha;
   }
}
