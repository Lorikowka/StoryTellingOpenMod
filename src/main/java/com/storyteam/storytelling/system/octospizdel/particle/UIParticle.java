package com.storyteam.storytelling.system.octospizdel.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.system.octospizdel.util.OctoColor;
import com.storyteam.storytelling.system.octospizdel.util.RenderUtils;
import java.util.ArrayList;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import oshi.util.tuples.Pair;

public class UIParticle {
   private final UIParticle.Texture2D texture;
   private final float maxSpeed;
   private final int lifetime;
   private final UIParticle.Layer layer;
   private Screen screen;
   private final Transform transform;
   private Vector2f direction = new Vector2f(0.0F, 1.0F);
   private float rollVelocity;
   private float speed;
   private int time;
   private float zOffset;
   private OctoColor[] colors;
   private Pair<Integer, Integer> blendFunc;
   private boolean enableBlend;

   public UIParticle(UIParticle.Texture2D texture, float maxSpeed, int lifetime, float xStart, float yStart, UIParticle.Layer layer, float zOffset) {
      this.texture = texture;
      this.maxSpeed = maxSpeed;
      this.lifetime = lifetime;
      this.speed = maxSpeed;
      this.layer = layer;
      this.zOffset = zOffset;
      this.transform = new Transform(new Vector2f(xStart, yStart), 0.0F, new Vector2f(1.0F, 1.0F));
      this.colors = new OctoColor[]{OctoColor.WHITE};
      this.blendFunc = new Pair(770, 1);
      this.enableBlend = true;
   }

   public void enableBlend(boolean value) {
      this.enableBlend = value;
   }

   public void setBlendFunc(int source, int destination) {
      this.blendFunc = new Pair(source, destination);
      this.enableBlend = true;
   }

   public void setColors(OctoColor... colors) {
      this.colors = colors;
   }

   public void setDirection(float x, float y) {
      this.direction = new Vector2f(x, y).normalize();
   }

   public void setDirection(Vector2f direction) {
      this.direction = new Vector2f(direction).normalize();
   }

   public boolean isExpired() {
      return this.time >= this.lifetime;
   }

   public boolean isScreen() {
      return this.layer == UIParticle.Layer.SCREEN;
   }

   public boolean isGui() {
      return this.layer == UIParticle.Layer.GUI;
   }

   public void tick() {
      if (!this.isExpired()) {
         this.transform.updateOldValues();
         this.time = Mth.clamp(this.time + 1, 0, this.lifetime);
         if (Mth.abs(this.speed) > 0.01F) {
            this.transform.getPosition().add(new Vector2f(this.direction).mul(this.speed));
         }

         if (Mth.abs(this.rollVelocity) > 0.01F) {
            this.transform.setRoll(this.transform.getRoll() + this.rollVelocity);
         }
      }
   }

   public void render(GuiGraphics guiGraphics, float partialTicks) {
      if (!this.isExpired()) {
         UIParticle.Texture2D tex = this.getTexture();
         PoseStack pose = guiGraphics.pose();
         pose.pushPose();
         this.transformPose(pose, partialTicks);
         float lifePercentage = this.getTimeRatio(partialTicks);
         OctoColor color = this.getColor(partialTicks);
         RenderSystem.setShaderColor(color.r(), color.g(), color.b(), color.a());
         RenderSystem.setShaderTexture(0, tex.rl);
         RenderSystem.enableBlend();
         if (this.enableBlend) {
            RenderSystem.blendFunc((Integer)this.blendFunc.getA(), (Integer)this.blendFunc.getB());
         }

         RenderUtils.renderTextureFromCenter(
            pose, 0.0F, 0.0F, tex.texOffX, tex.texOffY, tex.texWidth, tex.texHeight, tex.width, tex.height, 1.0F, this.getZOffset()
         );
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableBlend();
         pose.popPose();
      }
   }

   private void transformPose(PoseStack pose, float partialTicks) {
      Vector2f interpPos = this.transform.getInterpolatedPosition(partialTicks);
      float interpRot = this.transform.getInterpolatedRoll(partialTicks);
      Vector2f interpSize = this.transform.getInterpolatedSize(partialTicks);
      pose.translate(interpPos.x, interpPos.y, this.zOffset);
      pose.scale(interpSize.x, interpSize.y, 1.0F);
      pose.mulPose(Axis.ZP.rotationDegrees(interpRot));
   }

   public float getTimeRatio(float partialTicks) {
      return ((float)this.time + partialTicks) / (float)this.lifetime;
   }

   public OctoColor getColor(float partialTicks) {
      return OctoColor.lerp((double)this.getTimeRatio(partialTicks), this.colors);
   }

   public void instantiate() {
      if (this.isScreen()) {
         if (this.screen != null) {
            ParticleSystem.SCREEN_PARTICLES.putIfAbsent(this.screen, new ArrayList<>());
            ParticleSystem.SCREEN_PARTICLES.get(this.screen).add(this);
         }
      } else {
         ParticleSystem.GUI_PARTICLES.add(this);
      }
   }

   @Generated
   public UIParticle.Texture2D getTexture() {
      return this.texture;
   }

   @Generated
   public float getMaxSpeed() {
      return this.maxSpeed;
   }

   @Generated
   public int getLifetime() {
      return this.lifetime;
   }

   @Generated
   public UIParticle.Layer getLayer() {
      return this.layer;
   }

   @Generated
   public Screen getScreen() {
      return this.screen;
   }

   @Generated
   public void setScreen(Screen screen) {
      this.screen = screen;
   }

   @Generated
   public Transform getTransform() {
      return this.transform;
   }

   @Generated
   public Vector2f getDirection() {
      return this.direction;
   }

   @Generated
   public float getRollVelocity() {
      return this.rollVelocity;
   }

   @Generated
   public void setRollVelocity(float rollVelocity) {
      this.rollVelocity = rollVelocity;
   }

   @Generated
   public float getSpeed() {
      return this.speed;
   }

   @Generated
   public void setSpeed(float speed) {
      this.speed = speed;
   }

   @Generated
   public int getTime() {
      return this.time;
   }

   @Generated
   public void setTime(int time) {
      this.time = time;
   }

   @Generated
   public float getZOffset() {
      return this.zOffset;
   }

   @Generated
   public void setZOffset(float zOffset) {
      this.zOffset = zOffset;
   }

   @Generated
   public OctoColor[] getColors() {
      return this.colors;
   }

   @Generated
   public Pair<Integer, Integer> getBlendFunc() {
      return this.blendFunc;
   }

   @Generated
   public boolean isEnableBlend() {
      return this.enableBlend;
   }

   @Generated
   public void setEnableBlend(boolean enableBlend) {
      this.enableBlend = enableBlend;
   }

   public static enum Layer {
      GUI,
      SCREEN;
   }

   public static class Texture2D {
      private ResourceLocation rl;
      private float width;
      private float height;
      private float texOffX;
      private float texOffY;
      private float texWidth;
      private float texHeight;

      public Texture2D(ResourceLocation texture, int texOffX, int texOffY, int texWidth, int texHeight, int width, int height) {
         this.rl = texture;
         this.texOffX = (float)texOffX;
         this.texOffY = (float)texOffY;
         this.texWidth = (float)texWidth;
         this.texHeight = (float)texHeight;
         this.width = (float)width;
         this.height = (float)height;
      }

      public Texture2D(ResourceLocation texture, int width, int height) {
         this(texture, 0, 0, width, height, width, height);
      }

      @Generated
      public ResourceLocation getRl() {
         return this.rl;
      }

      @Generated
      public float getWidth() {
         return this.width;
      }

      @Generated
      public float getHeight() {
         return this.height;
      }

      @Generated
      public float getTexOffX() {
         return this.texOffX;
      }

      @Generated
      public float getTexOffY() {
         return this.texOffY;
      }

      @Generated
      public float getTexWidth() {
         return this.texWidth;
      }

      @Generated
      public float getTexHeight() {
         return this.texHeight;
      }

      @Generated
      public void setRl(ResourceLocation rl) {
         this.rl = rl;
      }

      @Generated
      public void setWidth(float width) {
         this.width = width;
      }

      @Generated
      public void setHeight(float height) {
         this.height = height;
      }

      @Generated
      public void setTexOffX(float texOffX) {
         this.texOffX = texOffX;
      }

      @Generated
      public void setTexOffY(float texOffY) {
         this.texOffY = texOffY;
      }

      @Generated
      public void setTexWidth(float texWidth) {
         this.texWidth = texWidth;
      }

      @Generated
      public void setTexHeight(float texHeight) {
         this.texHeight = texHeight;
      }

      @Generated
      @Override
      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof UIParticle.Texture2D other)) {
            return false;
         } else if (!other.canEqual(this)) {
            return false;
         } else if (Float.compare(this.getWidth(), other.getWidth()) != 0) {
            return false;
         } else if (Float.compare(this.getHeight(), other.getHeight()) != 0) {
            return false;
         } else if (Float.compare(this.getTexOffX(), other.getTexOffX()) != 0) {
            return false;
         } else if (Float.compare(this.getTexOffY(), other.getTexOffY()) != 0) {
            return false;
         } else if (Float.compare(this.getTexWidth(), other.getTexWidth()) != 0) {
            return false;
         } else if (Float.compare(this.getTexHeight(), other.getTexHeight()) != 0) {
            return false;
         } else {
            Object this$rl = this.getRl();
            Object other$rl = other.getRl();
            return this$rl == null ? other$rl == null : this$rl.equals(other$rl);
         }
      }

      @Generated
      protected boolean canEqual(Object other) {
         return other instanceof UIParticle.Texture2D;
      }

      @Generated
      @Override
      public int hashCode() {
         int PRIME = 59;
         int result = 1;
         result = result * 59 + Float.floatToIntBits(this.getWidth());
         result = result * 59 + Float.floatToIntBits(this.getHeight());
         result = result * 59 + Float.floatToIntBits(this.getTexOffX());
         result = result * 59 + Float.floatToIntBits(this.getTexOffY());
         result = result * 59 + Float.floatToIntBits(this.getTexWidth());
         result = result * 59 + Float.floatToIntBits(this.getTexHeight());
         Object $rl = this.getRl();
         return result * 59 + ($rl == null ? 43 : $rl.hashCode());
      }

      @Generated
      @Override
      public String toString() {
         return "UIParticle.Texture2D(rl="
            + this.getRl()
            + ", width="
            + this.getWidth()
            + ", height="
            + this.getHeight()
            + ", texOffX="
            + this.getTexOffX()
            + ", texOffY="
            + this.getTexOffY()
            + ", texWidth="
            + this.getTexWidth()
            + ", texHeight="
            + this.getTexHeight()
            + ")";
      }
   }
}
