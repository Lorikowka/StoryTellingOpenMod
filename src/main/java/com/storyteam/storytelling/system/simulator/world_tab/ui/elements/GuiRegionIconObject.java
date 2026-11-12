package com.storyteam.storytelling.system.simulator.world_tab.ui.elements;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiSpriteButtonObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject.OnPress;
import org.zeith.hammerlib.util.math.Point;

public class GuiRegionIconObject extends GuiSpriteButtonObject {
   private final Point defaultPos;
   public Tween tweenHower;
   public Tween tweenRelease;

   public GuiRegionIconObject(
      @NotNull String name,
      float alpha,
      int packedFGColor,
      boolean enabled,
      @NotNull Component message,
      @NotNull OnPress callback,
      Supplier<SoundEvent> pressSound,
      Float pressSoundPitch,
      @NotNull ResourceLocation customTexture,
      Vec3 color,
      Point defaultPos
   ) {
      super(name, alpha, packedFGColor, enabled, message, callback, pressSound, pressSoundPitch, customTexture, color);
      this.defaultPos = defaultPos;
   }

   protected void render(Graphics gfx, MousePos pos) {
      if (pos.isMouseWithin(this) && this.tweenHower == null && this.getParent().getParent().getParent().getChild("info") == null) {
         this.tweenHower = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_OUT).setLoops(1);
         if (this.tweenRelease != null) {
            this.tweenHower.tweenRunnable(this.tweenRelease::kill);
            this.tweenRelease = null;
         }

         this.tweenHower.tweenMethod(x -> this.scale = new Vec3(x, x, x), this.scale.x(), 2.0, 0.15);
         this.tweenHower.parallel().tweenMethod(x -> this.pos(x, this.pos.y()), this.pos.x(), this.defaultPos.x() - this.getUnscaledWidth() * 0.5F, 0.15);
         this.tweenHower.parallel().tweenMethod(y -> this.pos(this.pos.x(), y), this.pos.y(), this.defaultPos.y() - this.getUnscaledHeight() * 0.5F, 0.15);
         this.tweenHower.start();
      } else if (!pos.isMouseWithin(this) && this.tweenRelease == null) {
         this.tweenRelease = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_IN).setLoops(1);
         if (this.tweenHower != null) {
            this.tweenRelease.tweenRunnable(this.tweenHower::kill);
            this.tweenHower = null;
         }

         this.tweenRelease.tweenMethod(x -> this.scale = new Vec3(x, x, x), this.scale.x(), 1.0, 0.1);
         this.tweenRelease.parallel().tweenMethod(x -> this.pos(x, this.pos.y()), this.pos.x(), this.defaultPos.x(), 0.1);
         this.tweenRelease.parallel().tweenMethod(y -> this.pos(this.pos.x(), y), this.pos.y(), this.defaultPos.y(), 0.1);
         this.tweenRelease.start();
      }

      super.render(gfx, pos);
   }

   protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake) {
      return this.getParent().getParent().getParent().getChild("info") != null ? false : super.onMouseClicked(globalMousePos, pos, button, fake);
   }

   protected int getTextureY(boolean hovered) {
      return this.getParent().getParent().getParent().getChild("info") != null ? (int)(1.0F * this.getUnscaledHeight()) : super.getTextureY(hovered);
   }

   @Generated
   public static GuiRegionIconObject.GuiRegionIconObjectBuilder regionIconBuilder() {
      return new GuiRegionIconObject.GuiRegionIconObjectBuilder();
   }

   @Generated
   public static class GuiRegionIconObjectBuilder {
      @Generated
      private String name;
      @Generated
      private float alpha;
      @Generated
      private int packedFGColor;
      @Generated
      private boolean enabled;
      @Generated
      private Component message;
      @Generated
      private OnPress callback;
      @Generated
      private Supplier<SoundEvent> pressSound;
      @Generated
      private Float pressSoundPitch;
      @Generated
      private ResourceLocation customTexture;
      @Generated
      private Vec3 color;
      @Generated
      private Point defaultPos;

      @Generated
      GuiRegionIconObjectBuilder() {
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder name(@NotNull String name) {
         if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
         } else {
            this.name = name;
            return this;
         }
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder alpha(float alpha) {
         this.alpha = alpha;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder packedFGColor(int packedFGColor) {
         this.packedFGColor = packedFGColor;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder message(@NotNull Component message) {
         if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
         } else {
            this.message = message;
            return this;
         }
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder callback(@NotNull OnPress callback) {
         if (callback == null) {
            throw new NullPointerException("callback is marked non-null but is null");
         } else {
            this.callback = callback;
            return this;
         }
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder pressSound(Supplier<SoundEvent> pressSound) {
         this.pressSound = pressSound;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder pressSoundPitch(Float pressSoundPitch) {
         this.pressSoundPitch = pressSoundPitch;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder customTexture(@NotNull ResourceLocation customTexture) {
         if (customTexture == null) {
            throw new NullPointerException("customTexture is marked non-null but is null");
         } else {
            this.customTexture = customTexture;
            return this;
         }
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder color(Vec3 color) {
         this.color = color;
         return this;
      }

      @Generated
      public GuiRegionIconObject.GuiRegionIconObjectBuilder defaultPos(Point defaultPos) {
         this.defaultPos = defaultPos;
         return this;
      }

      @Generated
      public GuiRegionIconObject build() {
         return new GuiRegionIconObject(
            this.name,
            this.alpha,
            this.packedFGColor,
            this.enabled,
            this.message,
            this.callback,
            this.pressSound,
            this.pressSoundPitch,
            this.customTexture,
            this.color,
            this.defaultPos
         );
      }

      @Generated
      @Override
      public String toString() {
         return "GuiRegionIconObject.GuiRegionIconObjectBuilder(name="
            + this.name
            + ", alpha="
            + this.alpha
            + ", packedFGColor="
            + this.packedFGColor
            + ", enabled="
            + this.enabled
            + ", message="
            + this.message
            + ", callback="
            + this.callback
            + ", pressSound="
            + this.pressSound
            + ", pressSoundPitch="
            + this.pressSoundPitch
            + ", customTexture="
            + this.customTexture
            + ", color="
            + this.color
            + ", defaultPos="
            + this.defaultPos
            + ")";
      }
   }
}
