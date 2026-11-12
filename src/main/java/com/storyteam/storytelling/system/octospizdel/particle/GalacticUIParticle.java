package com.storyteam.storytelling.system.octospizdel.particle;

import com.storyteam.storytelling.system.octospizdel.util.OctoColor;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

public class GalacticUIParticle extends ExtendedUIParticle {
   private static final Random RANDOM = new Random();
   private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

   public GalacticUIParticle(float maxSpeed, int maxLifetime, float xStart, float yStart, UIParticle.Layer layer, float zOffset) {
      super(
         new UIParticle.Texture2D(
            new ResourceLocation("minecraft:textures/particle/sga_").withSuffix(ALPHABET[RANDOM.nextInt(ALPHABET.length)] + ".png"), 0, 0, 8, 8, 5, 7
         ),
         maxSpeed,
         maxLifetime,
         xStart,
         yStart,
         layer,
         zOffset
      );
      this.setSpeed(RANDOM.nextFloat() * 2.0F * maxSpeed);
      this.setAngularVelocity(this.getSpeed() * 3.0F);
      this.setColors(new OctoColor[]{new OctoColor(1.0F, 1.0F, 1.0F, 1.0F), new OctoColor(0.0F, 1.0F, 1.0F, 0.0F)});
      this.setDirection(new Vector2f(RANDOM.nextFloat() - 0.5F, RANDOM.nextFloat() - 0.5F).normalize());
      this.setGravity(0.01F);
      this.setGravityDirection(0.0F, 1.0F);
      this.setFriction(0.02F);
   }
}
