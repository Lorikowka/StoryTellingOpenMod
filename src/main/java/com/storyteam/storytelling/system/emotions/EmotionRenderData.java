package com.storyteam.storytelling.system.emotions;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import java.util.Arrays;
import java.util.Random;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.zeith.hammerlib.util.mcf.Resources;

public class EmotionRenderData {
   public static final Random randomOfQueniros = new Random();
   private String uuid;
   private float yOffset;
   private long appearTick;
   private float renderScale;
   private Tween tween;
   private boolean hiddenOnWorld;
   private boolean disappearing;
   private float[] x;
   private float[] liftSpeed;
   private float[] y;
   private float scale;
   private BubbleType bubbleType;
   private Emotion emotion;
   private int ticks;
   private Vec3 posOffset;

   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      float alpha = Mth.clamp((float)this.ticks / 100.0F, 0.0F, 1.0F);
      ResourceLocation textureLocation = Resources.location("storytelling", "textures/system/emotions/" + this.emotion.getPath() + ".png");

      for (int i = 0; i < this.x.length; i++) {
         float lx = this.x[i];
         float fs = this.liftSpeed[i];
         this.y[i] = this.y[i] - fs * partialTick;
         float y = this.y[i];
         guiGraphics.pose().pushPose();
         RenderSystem.enableBlend();
         guiGraphics.pose().translate(lx, y, 0.0F);
         guiGraphics.pose().scale(this.scale, this.scale, 1.0F);
         RenderSystem.setShaderTexture(0, textureLocation);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
         Minecraft.getInstance().getTextureManager().getTexture(textureLocation).bind();
         guiGraphics.blit(textureLocation, -8, -6, 0.0F, 0.0F, 17, 13, 17, 13);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         guiGraphics.pose().popPose();
      }
   }

   public static EmotionRenderData of(String uuid, Emotion emotion, int ticks, Vec3 offset, BubbleType bubbleType) {
      return builder().uuid(uuid).emotion(emotion).ticks(ticks).posOffset(offset).bubbleType(bubbleType).build();
   }

   public static EmotionRenderData of(String uuid, Emotion emotion, int ticks, Vec3 offset) {
      return builder().uuid(uuid).emotion(emotion).ticks(ticks).posOffset(offset).build();
   }

   public static EmotionRenderData of(String uuid, Emotion emotion, int ticks) {
      return builder().uuid(uuid).emotion(emotion).ticks(ticks).build();
   }

   public static EmotionRenderData of(String uuid, Emotion emotion, Vec3 offset, BubbleType bubbleType) {
      return builder().uuid(uuid).emotion(emotion).posOffset(offset).bubbleType(bubbleType).build();
   }

   public static EmotionRenderData of(String uuid, Emotion item, Vec3 offset) {
      return builder().uuid(uuid).emotion(item).posOffset(offset).build();
   }

   public static EmotionRenderData of(String uuid, Emotion item) {
      return builder().uuid(uuid).emotion(item).build();
   }

   public static EmotionRenderData empty(String uuid) {
      return builder().uuid(uuid).build();
   }

   @Generated
   private static boolean $default$hiddenOnWorld() {
      return false;
   }

   @Generated
   private static boolean $default$disappearing() {
      return false;
   }

   @Generated
   private static float[] $default$x() {
      return new float[]{randomOfQueniros.nextFloat() * 125.0F, randomOfQueniros.nextFloat() * 125.0F, randomOfQueniros.nextFloat() * 125.0F};
   }

   @Generated
   private static float[] $default$liftSpeed() {
      return new float[]{randomOfQueniros.nextFloat() * 0.5F, randomOfQueniros.nextFloat() * 0.5F, randomOfQueniros.nextFloat() * 0.5F};
   }

   @Generated
   private static float[] $default$y() {
      return new float[0];
   }

   @Generated
   private static float $default$scale() {
      return randomOfQueniros.nextFloat() + 1.25F;
   }

   @Generated
   private static BubbleType $default$bubbleType() {
      return BubbleType.SPEECH;
   }

   @Generated
   private static Emotion $default$emotion() {
      return null;
   }

   @Generated
   private static int $default$ticks() {
      return 200;
   }

   @Generated
   private static Vec3 $default$posOffset() {
      return Vec3.ZERO;
   }

   @Generated
   EmotionRenderData(
      String uuid,
      float yOffset,
      long appearTick,
      float renderScale,
      Tween tween,
      boolean hiddenOnWorld,
      boolean disappearing,
      float[] x,
      float[] liftSpeed,
      float[] y,
      float scale,
      BubbleType bubbleType,
      Emotion emotion,
      int ticks,
      Vec3 posOffset
   ) {
      this.uuid = uuid;
      this.yOffset = yOffset;
      this.appearTick = appearTick;
      this.renderScale = renderScale;
      this.tween = tween;
      this.hiddenOnWorld = hiddenOnWorld;
      this.disappearing = disappearing;
      this.x = x;
      this.liftSpeed = liftSpeed;
      this.y = y;
      this.scale = scale;
      this.bubbleType = bubbleType;
      this.emotion = emotion;
      this.ticks = ticks;
      this.posOffset = posOffset;
   }

   @Generated
   public static EmotionRenderData.EmotionRenderDataBuilder builder() {
      return new EmotionRenderData.EmotionRenderDataBuilder();
   }

   @Generated
   public String getUuid() {
      return this.uuid;
   }

   @Generated
   public float getYOffset() {
      return this.yOffset;
   }

   @Generated
   public long getAppearTick() {
      return this.appearTick;
   }

   @Generated
   public float getRenderScale() {
      return this.renderScale;
   }

   @Generated
   public Tween getTween() {
      return this.tween;
   }

   @Generated
   public boolean isHiddenOnWorld() {
      return this.hiddenOnWorld;
   }

   @Generated
   public boolean isDisappearing() {
      return this.disappearing;
   }

   @Generated
   public float[] getX() {
      return this.x;
   }

   @Generated
   public float[] getLiftSpeed() {
      return this.liftSpeed;
   }

   @Generated
   public float[] getY() {
      return this.y;
   }

   @Generated
   public float getScale() {
      return this.scale;
   }

   @Generated
   public BubbleType getBubbleType() {
      return this.bubbleType;
   }

   @Generated
   public Emotion getEmotion() {
      return this.emotion;
   }

   @Generated
   public int getTicks() {
      return this.ticks;
   }

   @Generated
   public Vec3 getPosOffset() {
      return this.posOffset;
   }

   @Generated
   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   @Generated
   public void setYOffset(float yOffset) {
      this.yOffset = yOffset;
   }

   @Generated
   public void setAppearTick(long appearTick) {
      this.appearTick = appearTick;
   }

   @Generated
   public void setRenderScale(float renderScale) {
      this.renderScale = renderScale;
   }

   @Generated
   public void setTween(Tween tween) {
      this.tween = tween;
   }

   @Generated
   public void setHiddenOnWorld(boolean hiddenOnWorld) {
      this.hiddenOnWorld = hiddenOnWorld;
   }

   @Generated
   public void setDisappearing(boolean disappearing) {
      this.disappearing = disappearing;
   }

   @Generated
   public void setX(float[] x) {
      this.x = x;
   }

   @Generated
   public void setLiftSpeed(float[] liftSpeed) {
      this.liftSpeed = liftSpeed;
   }

   @Generated
   public void setY(float[] y) {
      this.y = y;
   }

   @Generated
   public void setScale(float scale) {
      this.scale = scale;
   }

   @Generated
   public void setBubbleType(BubbleType bubbleType) {
      this.bubbleType = bubbleType;
   }

   @Generated
   public void setEmotion(Emotion emotion) {
      this.emotion = emotion;
   }

   @Generated
   public void setTicks(int ticks) {
      this.ticks = ticks;
   }

   @Generated
   public void setPosOffset(Vec3 posOffset) {
      this.posOffset = posOffset;
   }

   @Generated
   public static class EmotionRenderDataBuilder {
      @Generated
      private String uuid;
      @Generated
      private float yOffset;
      @Generated
      private long appearTick;
      @Generated
      private float renderScale;
      @Generated
      private Tween tween;
      @Generated
      private boolean hiddenOnWorld$set;
      @Generated
      private boolean hiddenOnWorld$value;
      @Generated
      private boolean disappearing$set;
      @Generated
      private boolean disappearing$value;
      @Generated
      private boolean x$set;
      @Generated
      private float[] x$value;
      @Generated
      private boolean liftSpeed$set;
      @Generated
      private float[] liftSpeed$value;
      @Generated
      private boolean y$set;
      @Generated
      private float[] y$value;
      @Generated
      private boolean scale$set;
      @Generated
      private float scale$value;
      @Generated
      private boolean bubbleType$set;
      @Generated
      private BubbleType bubbleType$value;
      @Generated
      private boolean emotion$set;
      @Generated
      private Emotion emotion$value;
      @Generated
      private boolean ticks$set;
      @Generated
      private int ticks$value;
      @Generated
      private boolean posOffset$set;
      @Generated
      private Vec3 posOffset$value;

      @Generated
      EmotionRenderDataBuilder() {
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder uuid(String uuid) {
         this.uuid = uuid;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder yOffset(float yOffset) {
         this.yOffset = yOffset;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder appearTick(long appearTick) {
         this.appearTick = appearTick;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder renderScale(float renderScale) {
         this.renderScale = renderScale;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder tween(Tween tween) {
         this.tween = tween;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder hiddenOnWorld(boolean hiddenOnWorld) {
         this.hiddenOnWorld$value = hiddenOnWorld;
         this.hiddenOnWorld$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder disappearing(boolean disappearing) {
         this.disappearing$value = disappearing;
         this.disappearing$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder x(float[] x) {
         this.x$value = x;
         this.x$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder liftSpeed(float[] liftSpeed) {
         this.liftSpeed$value = liftSpeed;
         this.liftSpeed$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder y(float[] y) {
         this.y$value = y;
         this.y$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder scale(float scale) {
         this.scale$value = scale;
         this.scale$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder bubbleType(BubbleType bubbleType) {
         this.bubbleType$value = bubbleType;
         this.bubbleType$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder emotion(Emotion emotion) {
         this.emotion$value = emotion;
         this.emotion$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder ticks(int ticks) {
         this.ticks$value = ticks;
         this.ticks$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData.EmotionRenderDataBuilder posOffset(Vec3 posOffset) {
         this.posOffset$value = posOffset;
         this.posOffset$set = true;
         return this;
      }

      @Generated
      public EmotionRenderData build() {
         boolean hiddenOnWorld$value = this.hiddenOnWorld$value;
         if (!this.hiddenOnWorld$set) {
            hiddenOnWorld$value = EmotionRenderData.$default$hiddenOnWorld();
         }

         boolean disappearing$value = this.disappearing$value;
         if (!this.disappearing$set) {
            disappearing$value = EmotionRenderData.$default$disappearing();
         }

         float[] x$value = this.x$value;
         if (!this.x$set) {
            x$value = EmotionRenderData.$default$x();
         }

         float[] liftSpeed$value = this.liftSpeed$value;
         if (!this.liftSpeed$set) {
            liftSpeed$value = EmotionRenderData.$default$liftSpeed();
         }

         float[] y$value = this.y$value;
         if (!this.y$set) {
            y$value = EmotionRenderData.$default$y();
         }

         float scale$value = this.scale$value;
         if (!this.scale$set) {
            scale$value = EmotionRenderData.$default$scale();
         }

         BubbleType bubbleType$value = this.bubbleType$value;
         if (!this.bubbleType$set) {
            bubbleType$value = EmotionRenderData.$default$bubbleType();
         }

         Emotion emotion$value = this.emotion$value;
         if (!this.emotion$set) {
            emotion$value = EmotionRenderData.$default$emotion();
         }

         int ticks$value = this.ticks$value;
         if (!this.ticks$set) {
            ticks$value = EmotionRenderData.$default$ticks();
         }

         Vec3 posOffset$value = this.posOffset$value;
         if (!this.posOffset$set) {
            posOffset$value = EmotionRenderData.$default$posOffset();
         }

         return new EmotionRenderData(
            this.uuid,
            this.yOffset,
            this.appearTick,
            this.renderScale,
            this.tween,
            hiddenOnWorld$value,
            disappearing$value,
            x$value,
            liftSpeed$value,
            y$value,
            scale$value,
            bubbleType$value,
            emotion$value,
            ticks$value,
            posOffset$value
         );
      }

      @Generated
      @Override
      public String toString() {
         return "EmotionRenderData.EmotionRenderDataBuilder(uuid="
            + this.uuid
            + ", yOffset="
            + this.yOffset
            + ", appearTick="
            + this.appearTick
            + ", renderScale="
            + this.renderScale
            + ", tween="
            + this.tween
            + ", hiddenOnWorld$value="
            + this.hiddenOnWorld$value
            + ", disappearing$value="
            + this.disappearing$value
            + ", x$value="
            + Arrays.toString(this.x$value)
            + ", liftSpeed$value="
            + Arrays.toString(this.liftSpeed$value)
            + ", y$value="
            + Arrays.toString(this.y$value)
            + ", scale$value="
            + this.scale$value
            + ", bubbleType$value="
            + this.bubbleType$value
            + ", emotion$value="
            + this.emotion$value
            + ", ticks$value="
            + this.ticks$value
            + ", posOffset$value="
            + this.posOffset$value
            + ")";
      }
   }
}
