package com.storyteam.storytelling.system.simulator.world_tab.ui.elements;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.world_tab.data.WMRButton;
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

public class GuiRegionButtonObject extends GuiSpriteButtonObject {
   public Tween tweenHower;
   public Tween tweenRelease;
   private Point defaultPos;
   private WMRButton button;

   public GuiRegionButtonObject(
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
      Point defaultPos,
      WMRButton button
   ) {
      super(name, alpha, packedFGColor, enabled, message, callback, pressSound, pressSoundPitch, customTexture, color);
      this.defaultPos = defaultPos;
      this.button = button;
   }

   protected void render(Graphics gfx, MousePos pos) {
      if (this.defaultPos == null) {
         this.defaultPos = this.pos;
      }

      if (pos.isMouseWithin(this) && this.tweenHower == null) {
         this.tweenHower = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_OUT).setLoops(1);
         if (this.tweenRelease != null) {
            this.tweenHower.tweenRunnable(this.tweenRelease::kill);
            this.tweenRelease = null;
         }

         this.tweenHower.parallel().tweenMethod(x -> this.scale = new Vec3(x, x, x), this.scale.x(), 1.25, 0.1);
         this.tweenHower.parallel().tweenMethod(x -> this.pos(x, this.pos.y()), this.pos.x(), this.defaultPos.x() - this.getUnscaledWidth() * 0.125F, 0.1);
         this.tweenHower.parallel().tweenMethod(y -> this.pos(this.pos.x(), y), this.pos.y(), this.defaultPos.y() - this.getUnscaledHeight() * 0.125F, 0.1);
         this.tweenHower
            .parallel()
            .tweenMethod(x -> this.color = x, this.color, this.button.hasEnoughEidos() ? new Vec3(0.4F, 1.0, 0.4F) : new Vec3(1.0, 0.4F, 0.4F), 0.2);
         this.tweenHower.start();
      } else if (!pos.isMouseWithin(this) && this.tweenRelease == null) {
         this.tweenRelease = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_IN).setLoops(1);
         if (this.tweenHower != null) {
            this.tweenRelease.tweenRunnable(this.tweenHower::kill);
            this.tweenHower = null;
         }

         this.tweenRelease.parallel().tweenMethod(x -> this.scale = new Vec3(x, x, x), this.scale.x(), 1.0, 0.1);
         this.tweenRelease.parallel().tweenMethod(x -> this.pos(x, this.pos.y()), this.pos.x(), this.defaultPos.x(), 0.1);
         this.tweenRelease.parallel().tweenMethod(y -> this.pos(this.pos.x(), y), this.pos.y(), this.defaultPos.y(), 0.1);
         this.tweenRelease.parallel().tweenMethod(x -> this.color = x, this.color, new Vec3(1.0, 1.0, 1.0), 0.2);
         this.tweenRelease.start();
      }

      super.render(gfx, pos);
   }

   @Generated
   public static GuiRegionButtonObject.GuiRegionButtonObjectBuilder regionButtonBuilder() {
      return new GuiRegionButtonObject.GuiRegionButtonObjectBuilder();
   }

   @Generated
   public static class GuiRegionButtonObjectBuilder {
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
      private WMRButton button;

      @Generated
      GuiRegionButtonObjectBuilder() {
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder name(@NotNull String name) {
         if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
         } else {
            this.name = name;
            return this;
         }
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder alpha(float alpha) {
         this.alpha = alpha;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder packedFGColor(int packedFGColor) {
         this.packedFGColor = packedFGColor;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder message(@NotNull Component message) {
         if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
         } else {
            this.message = message;
            return this;
         }
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder callback(@NotNull OnPress callback) {
         if (callback == null) {
            throw new NullPointerException("callback is marked non-null but is null");
         } else {
            this.callback = callback;
            return this;
         }
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder pressSound(Supplier<SoundEvent> pressSound) {
         this.pressSound = pressSound;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder pressSoundPitch(Float pressSoundPitch) {
         this.pressSoundPitch = pressSoundPitch;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder customTexture(@NotNull ResourceLocation customTexture) {
         if (customTexture == null) {
            throw new NullPointerException("customTexture is marked non-null but is null");
         } else {
            this.customTexture = customTexture;
            return this;
         }
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder color(Vec3 color) {
         this.color = color;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder defaultPos(Point defaultPos) {
         this.defaultPos = defaultPos;
         return this;
      }

      @Generated
      public GuiRegionButtonObject.GuiRegionButtonObjectBuilder button(WMRButton button) {
         this.button = button;
         return this;
      }

      @Generated
      public GuiRegionButtonObject build() {
         return new GuiRegionButtonObject(
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
            this.defaultPos,
            this.button
         );
      }

      @Generated
      @Override
      public String toString() {
         return "GuiRegionButtonObject.GuiRegionButtonObjectBuilder(name="
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
            + ", button="
            + this.button
            + ")";
      }
   }
}
