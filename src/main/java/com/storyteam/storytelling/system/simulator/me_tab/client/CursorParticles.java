package com.storyteam.storytelling.system.simulator.me_tab.client;

import com.storyteam.storytelling.system.octospizdel.particle.ExtendedUIParticle;
import com.storyteam.storytelling.system.octospizdel.particle.UIParticle;
import com.storyteam.storytelling.system.octospizdel.util.OctoColor;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

public class CursorParticles {
   private static final Random RANDOM = new Random();
   private static final UIParticle.Texture2D TEXTURE = new UIParticle.Texture2D(new ResourceLocation("storytelling:textures/particle/circle_tint.png"), 64, 64);

   public static void spawnAtCursor(float mouseX, float mouseY, Screen screen) {
      Minecraft mc = Minecraft.getInstance();
      ExtendedUIParticle p = new ExtendedUIParticle(
         TEXTURE,
         0.4F + RANDOM.nextFloat() * 0.3F,
         20 + RANDOM.nextInt(10),
         mouseX + RANDOM.nextFloat() * 4.0F - 2.0F,
         mouseY + RANDOM.nextFloat() * 4.0F - 2.0F,
         UIParticle.Layer.SCREEN,
         0.0F
      );
      float angle = (float)((double)RANDOM.nextFloat() * Math.PI * 2.0);
      p.setDirection((float)Math.cos((double)angle), (float)Math.sin((double)angle));
      p.setGravity(0.01F + RANDOM.nextFloat() * 0.02F);
      p.setFriction(0.05F);
      p.setAngularVelocity((RANDOM.nextFloat() - 0.5F) * 4.0F);
      p.setColors(
         new OctoColor[]{
            new OctoColor(0.8F, 0.3F, 1.0F, 0.9F),
            new OctoColor(0.3F, 0.8F, 1.0F, 0.7F),
            new OctoColor(1.0F, 0.5F, 0.8F, 0.3F),
            new OctoColor(1.0F, 1.0F, 1.0F, 0.0F)
         }
      );
      p.getTransform().setSize(new Vector2f(0.03F + RANDOM.nextFloat() * 0.04F));
      p.setBlendFunc(770, 1);
      p.setScreen(screen);
      p.getTransform().updateOldValues();
      p.instantiate();
   }
}
