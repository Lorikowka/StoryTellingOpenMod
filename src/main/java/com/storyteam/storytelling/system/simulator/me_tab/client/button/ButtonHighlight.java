package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.RenderTools;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.util.mcf.Resources;

public class ButtonHighlight extends AbstractButton {
   ResourceLocation btn;
   ResourceLocation btn_highlight;
   protected final ButtonHighlight.OnPress onPress;
   boolean animating;
   protected int baseColor = 16564223;
   float progress;
   Tween tween;

   public ButtonHighlight(
      int pX, int pY, int pWidth, int pHeight, Component pMessage, Color color, ResourceLocation btn, ButtonHighlight.OnPress onPress, Tooltip tooltip
   ) {
      super(pX, pY, pWidth, pHeight, pMessage);
      this.btn = btn;
      this.btn_highlight = Resources.location(btn.getNamespace(), btn.getPath().replace(".png", "_highlighted.png"));
      this.onPress = onPress;
      this.setTooltip(tooltip);
      this.baseColor = color.getRGB();
      this.progress = 0.0F;
      this.startAnim();
   }

   public void onPress() {
      this.onPress.onPress(this);
   }

   protected void renderWidget(GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      this.isHovered();
      RenderSystem.setShaderTexture(0, this.btn);
      RenderTools.blitWithBlend(
         gfx,
         (float)this.getX(),
         (float)this.getY(),
         0.0F,
         0.0F,
         (float)this.width,
         (float)this.height,
         (float)this.width,
         (float)this.height,
         this.animating ? this.progress : 1.0F
      );
      if (this.isHovered()) {
         RenderSystem.setShaderTexture(0, this.btn_highlight);
         RenderTools.blitWithBlend(
            gfx,
            (float)this.getX(),
            (float)this.getY(),
            0.0F,
            0.0F,
            (float)this.width,
            (float)this.height,
            (float)this.width,
            (float)this.height,
            this.animating ? this.progress : 1.0F
         );
      }

      int textWidth = Minecraft.getInstance().font.width(this.getMessage());
      int textX = this.getX() + (this.width - textWidth) / 2;
      int textY = this.getY() + (this.height - 9) / 2;
      int alpha = (int)((this.animating ? Math.min(this.progress + 0.1F, 1.0F) : 1.0F) * 255.0F);
      int color = alpha << 24 | this.baseColor & 16777215;
      int animatedY = textY + (int)((1.0F - (this.animating ? this.progress : 1.0F)) * 5.0F);
      gfx.drawString(Minecraft.getInstance().font, this.getMessage(), textX, animatedY, color, false);
   }

   public void startAnim() {
      this.animating = true;
      this.tween = Tween.create().setTransitionType(TransitionType.LINEAR).setEase(EaseType.EASE_OUT);
      this.tween.tweenMethod(this::setProgress, 0.0F, 1.0F, 0.4F).setDelay(0.3F);
      this.tween.tweenRunnable(() -> this.animating = false);
      this.tween.start();
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public void setBaseColor(int baseColor) {
      this.baseColor = baseColor;
   }

   @Generated
   public void setProgress(float progress) {
      this.progress = progress;
   }

   @OnlyIn(Dist.CLIENT)
   public interface OnPress {
      void onPress(ButtonHighlight var1);
   }
}
