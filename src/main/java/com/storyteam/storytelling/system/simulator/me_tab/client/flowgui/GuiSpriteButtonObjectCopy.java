package com.storyteam.storytelling.system.simulator.me_tab.client.flowgui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject.OnPress;

public class GuiSpriteButtonObjectCopy extends GuiButtonObject {
   public ResourceLocation texture;
   public Vec3 color = new Vec3(1.0, 1.0, 1.0);
   public Supplier<ShaderInstance> shader = FlowguiShaderRegistry.GUI_SHADER;
   public int shaderTexture = 0;
   Screen mainScreen;

   public GuiSpriteButtonObjectCopy(
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
      Screen mainScreen
   ) {
      super(name, alpha, packedFGColor, enabled, message, callback, pressSound, pressSoundPitch);
      this.texture = customTexture;
      if (color != null) {
         this.color = color;
      }

      this.mainScreen = mainScreen;
   }

   public GuiSpriteButtonObjectCopy shader(Supplier<ShaderInstance> shader) {
      this.shader = shader;
      return this;
   }

   public GuiSpriteButtonObjectCopy shaderTexture(int shaderTexture) {
      this.shaderTexture = shaderTexture;
      return this;
   }

   protected void renderButtonBg(Graphics gfx, MousePos pos) {
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      gfx.setColor(1.0F, 1.0F, 1.0F, this.alpha);
      gfx.drawManaged(
         () -> {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.setShaderTexture(this.shaderTexture, this.texture);
            GuiImageObject.blitWithBlend(
               this.shader,
               gfx.gfx(),
               0.0F,
               (float)(mc.screen == this.mainScreen ? this.getTextureY(pos.isMouseWithin(this)) : 1),
               this.width,
               this.height,
               this.width,
               this.height * 3.0F,
               this.alpha,
               this.color
            );
         }
      );
      gfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   protected int getTextureY(boolean hovered) {
      int state = 1;
      if (!this.enabled) {
         state = 0;
      } else if (hovered) {
         state = 2;
      }

      return (int)((float)state * this.height);
   }

   public static GuiSpriteButtonObjectCopy.SpriteButtonBuilder of(String name, Screen mainScreen) {
      return new GuiSpriteButtonObjectCopy.SpriteButtonBuilder()
         .name(name)
         .alpha(1.0F)
         .packedFGColor(-1)
         .enabled(true)
         .message(Component.empty())
         .callback(OnPress.NONE)
         .pressSound(SoundEvents.UI_BUTTON_CLICK)
         .mainScreen(mainScreen);
   }

   @Generated
   public static GuiSpriteButtonObjectCopy.SpriteButtonBuilder builder() {
      return new GuiSpriteButtonObjectCopy.SpriteButtonBuilder();
   }

   @Generated
   public static class SpriteButtonBuilder {
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
      private Screen mainScreen;

      @Generated
      SpriteButtonBuilder() {
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder name(@NotNull String name) {
         if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
         } else {
            this.name = name;
            return this;
         }
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder alpha(float alpha) {
         this.alpha = alpha;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder packedFGColor(int packedFGColor) {
         this.packedFGColor = packedFGColor;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder enabled(boolean enabled) {
         this.enabled = enabled;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder message(@NotNull Component message) {
         if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
         } else {
            this.message = message;
            return this;
         }
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder callback(@NotNull OnPress callback) {
         if (callback == null) {
            throw new NullPointerException("callback is marked non-null but is null");
         } else {
            this.callback = callback;
            return this;
         }
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder pressSound(Supplier<SoundEvent> pressSound) {
         this.pressSound = pressSound;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder pressSoundPitch(Float pressSoundPitch) {
         this.pressSoundPitch = pressSoundPitch;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder customTexture(@NotNull ResourceLocation customTexture) {
         if (customTexture == null) {
            throw new NullPointerException("customTexture is marked non-null but is null");
         } else {
            this.customTexture = customTexture;
            return this;
         }
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder color(Vec3 color) {
         this.color = color;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy.SpriteButtonBuilder mainScreen(Screen mainScreen) {
         this.mainScreen = mainScreen;
         return this;
      }

      @Generated
      public GuiSpriteButtonObjectCopy build() {
         return new GuiSpriteButtonObjectCopy(
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
            this.mainScreen
         );
      }

      @Generated
      @Override
      public String toString() {
         return "GuiSpriteButtonObjectCopy.SpriteButtonBuilder(name="
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
            + ", mainScreen="
            + this.mainScreen
            + ")";
      }
   }
}
