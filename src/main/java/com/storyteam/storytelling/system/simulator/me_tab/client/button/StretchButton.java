package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.client.WordScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.util.RenderTools;
import java.awt.Rectangle;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.zeith.hammerlib.util.math.Point;

public class StretchButton extends AbstractButton {
   ButtonStyle buttonStyle;
   private Vector2f scale = new Vector2f(1.0F, 1.0F);
   private Vector2f scaleAdd = new Vector2f(0.0F, 0.0F);
   private boolean hasHovered = false;
   private final int midWidth;
   private static final int TEXT_PADDING = 8;
   private final int baseWidth;
   private final int baseHeight;
   private int radius;
   boolean animatedBtn = false;
   MeTabScreen mainScreen;
   FragmentInstance fragmentInstance;
   private final Vector2f position = new Vector2f(0.0F, 0.0F);
   public Tween hoverTween;

   public StretchButton(int x, int y, Component text, ButtonStyle style, MeTabScreen mainScreen) {
      super(x, y, 0, style.height, text);
      this.buttonStyle = style;
      StretchButton.ButtonSize size = calcButtonSize(text, style);
      this.width = size.totalWidth;
      this.midWidth = size.midWidth;
      this.radius = this.calcButtonRadius(this.width, this.height);
      this.baseWidth = this.width;
      this.baseHeight = this.height;
      this.mainScreen = mainScreen;
   }

   public StretchButton(int x, int y, Component text, ButtonStyle style, MeTabScreen mainScreen, FragmentInstance fragmentInstance) {
      super(x, y, 0, style.height, text);
      this.buttonStyle = style;
      StretchButton.ButtonSize size = calcButtonSize(text, style);
      this.width = size.totalWidth;
      this.midWidth = size.midWidth;
      this.radius = this.calcButtonRadius(this.width, this.height);
      this.baseWidth = this.width;
      this.baseHeight = this.height;
      this.mainScreen = mainScreen;
      this.fragmentInstance = fragmentInstance;
   }

   public void setX(int x) {
      this.position.x = (float)x;
      super.setX(x);
   }

   public void setY(int y) {
      this.position.y = (float)y;
      super.setY(y);
   }

   public int getX() {
      return (int)this.position.x;
   }

   public int getY() {
      return (int)this.position.y;
   }

   public void applyGlobalScale(float guiScale) {
      this.scaleAdd = new Vector2f(guiScale, guiScale);
      this.width = (int)((float)this.baseWidth * guiScale);
      this.height = (int)((float)this.baseHeight * guiScale);
      this.radius = this.calcButtonRadius(this.width, this.height);
   }

   public int calcButtonRadius(int width, int height) {
      return (int)(Math.sqrt((double)(width * width + height * height)) / 2.0);
   }

   private static StretchButton.ButtonSize calcButtonSize(Component text, ButtonStyle style) {
      int textWidth = Minecraft.getInstance().font.width(text);
      int baseWidthButton = style.getTotalWidth();
      int neededWidthButton = textWidth + 16;
      int extra = Math.max(0, neededWidthButton - baseWidthButton);
      int finalMid = style.midWidth + extra;
      int total = style.leftWidth + finalMid + style.rightWidth;
      return new StretchButton.ButtonSize(total, finalMid);
   }

   public void onPress() {
   }

   public void onRelease(double pMouseX, double pMouseY) {
      super.onRelease(pMouseX, pMouseY);
      Minecraft mc = Minecraft.getInstance();
      mc.pushGuiLayer(
         new WordScreen(this.mainScreen, new Point((float)this.getX(), (float)this.getY()), (float)this.width, (float)this.height, this.fragmentInstance, this)
      );
   }

   public void renderWidget(@NotNull GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      this.isHovered();
      gfx.pose().pushPose();
      float cx = (float)this.getX() + (float)this.getWidth() / 2.0F;
      float cy = (float)this.getY() + (float)this.getHeight() / 2.0F;
      gfx.pose().translate(cx, cy, 0.0F);
      gfx.pose().scale(this.scale.x, this.scale.y, 1.0F);
      gfx.pose().translate(-cx, -cy, 0.0F);
      cx = (float)this.getX();
      cy = (float)this.getY();
      gfx.pose().translate(cx, cy, 0.0F);
      gfx.pose().scale(this.scaleAdd.x, this.scaleAdd.y, 1.0F);
      gfx.pose().translate(-cx, -cy, 0.0F);
      int x = this.getX();
      int y = this.getY();
      RenderSystem.setShaderTexture(0, this.buttonStyle.getLeft());
      RenderTools.blitWithBlend(
         gfx,
         (float)x,
         (float)y,
         0.0F,
         0.0F,
         (float)this.buttonStyle.leftWidth,
         (float)this.baseHeight,
         (float)this.buttonStyle.leftWidth,
         (float)this.baseHeight,
         1.0F
      );
      int midX = x + this.buttonStyle.leftWidth;
      RenderSystem.setShaderTexture(0, this.buttonStyle.getMid());
      RenderTools.blitWithBlend(
         gfx, (float)midX, (float)y, 0.0F, 0.0F, (float)this.midWidth, (float)this.baseHeight, (float)this.midWidth, (float)this.baseHeight, 1.0F
      );
      int rightX = midX + this.midWidth;
      RenderSystem.setShaderTexture(0, this.buttonStyle.getRight());
      RenderTools.blitWithBlend(
         gfx,
         (float)rightX,
         (float)y,
         0.0F,
         0.0F,
         (float)this.buttonStyle.rightWidth,
         (float)this.baseHeight,
         (float)this.buttonStyle.rightWidth,
         (float)this.baseHeight,
         1.0F
      );
      int textWidth = Minecraft.getInstance().font.width(this.getMessage());
      int textX = midX + (this.midWidth - textWidth) / 2;
      int textY = y + (this.baseHeight - 9) / 2;
      gfx.drawString(Minecraft.getInstance().font, this.getMessage(), textX, textY, this.buttonStyle.colorText, false);
      this.renderProgress(gfx);
      gfx.pose().popPose();
   }

   public void renderProgress(GuiGraphics gfx) {
      if (this.fragmentInstance != null) {
         double prog = this.fragmentInstance.progress();
         int x = this.getX() - 6;
         int y = this.getY() - 6;
         int leftW = this.buttonStyle.getOnWidth();
         int midW = this.midWidth;
         int rightW = this.buttonStyle.getOnWidth();
         int h = this.buttonStyle.getOnheight();
         int totalWidth = leftW + midW + rightW;
         int filledWidth = (int)((double)totalWidth * prog);
         if (filledWidth > 0) {
            int renderW = Math.min(leftW, filledWidth);
            RenderSystem.setShaderTexture(0, this.buttonStyle.getLeftOn());
            RenderTools.blitWithBlend(gfx, (float)x, (float)y, 0.0F, 0.0F, (float)renderW, (float)h, (float)leftW, (float)h, 1.0F);
         }

         if (filledWidth > leftW) {
            int renderW = Math.min(midW, filledWidth - leftW);
            RenderSystem.setShaderTexture(0, this.buttonStyle.getMidOn());
            RenderTools.blitWithBlend(gfx, (float)(x + leftW), (float)y, 0.0F, 0.0F, (float)renderW, (float)h, (float)midW, (float)h, 1.0F);
         }

         if (filledWidth > leftW + midW) {
            int renderW = Math.min(rightW, filledWidth - leftW - midW);
            RenderSystem.setShaderTexture(0, this.buttonStyle.getRightOn());
            RenderTools.blitWithBlend(gfx, (float)(x + leftW + midW), (float)y, 0.0F, 0.0F, (float)renderW, (float)h, (float)rightW, (float)h, 1.0F);
         }
      }
   }

   public boolean isHovered() {
      boolean hovered = super.isHovered();
      if (hovered && !this.hasHovered) {
         this.hasHovered = true;
         if (this.hoverTween != null) {
            this.hoverTween.kill();
         }

         this.hoverTween = Tween.create().setTransitionType(TransitionType.QUART).setEase(EaseType.EASE_OUT);
         this.hoverTween.tweenProperty(this, "scale", new Vector2f(1.25F, 1.25F), 0.2);
         this.hoverTween.start();
         if (!this.mainScreen.getEntityWidget().getEntity().getAnimationSystem().isActiveAnimation("emotion", this.fragmentInstance.getData().getAnimation())) {
            this.mainScreen
               .getEntityWidget()
               .getEntity()
               .getAnimationSystem()
               .startAnimationAt("emotion", this.fragmentInstance.getData().getAnimation().configure().transitionTime(0.34F));
         }
      } else if (!hovered && this.hasHovered) {
         this.hasHovered = false;
         if (this.hoverTween != null) {
            this.hoverTween.kill();
         }

         if (this.mainScreen.getEntityWidget().getEntity().getAnimationSystem().isActiveAnimation("emotion", this.fragmentInstance.getData().getAnimation())) {
            this.mainScreen.getEntityWidget().getEntity().getAnimationSystem().stopAnimation("emotion", 0.34F);
         }

         this.hoverTween = Tween.create().setTransitionType(TransitionType.QUART).setEase(EaseType.EASE_OUT);
         this.hoverTween.tweenProperty(this, "scale", new Vector2f(1.0F, 1.0F), 0.2);
         this.hoverTween.start();
      }

      return hovered;
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   public Rectangle getBounds() {
      return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
   }

   public Rectangle getScreenBounds(Vector2f offset, float zoom) {
      int x = (int)((float)this.getX() * zoom + offset.x);
      int y = (int)((float)this.getY() * zoom + offset.y);
      int w = (int)((float)this.getWidth() * zoom);
      int h = (int)((float)this.getHeight() * zoom);
      return new Rectangle(x, y, w, h);
   }

   @Generated
   public void setScale(Vector2f scale) {
      this.scale = scale;
   }

   @Generated
   public Vector2f getScale() {
      return this.scale;
   }

   @Generated
   public void setScaleAdd(Vector2f scaleAdd) {
      this.scaleAdd = scaleAdd;
   }

   @Generated
   public Vector2f getScaleAdd() {
      return this.scaleAdd;
   }

   @Generated
   public int getBaseWidth() {
      return this.baseWidth;
   }

   @Generated
   public int getBaseHeight() {
      return this.baseHeight;
   }

   @Generated
   public void setRadius(int radius) {
      this.radius = radius;
   }

   @Generated
   public int getRadius() {
      return this.radius;
   }

   @Generated
   public void setAnimatedBtn(boolean animatedBtn) {
      this.animatedBtn = animatedBtn;
   }

   @Generated
   public boolean isAnimatedBtn() {
      return this.animatedBtn;
   }

   public static record ButtonSize(int totalWidth, int midWidth) {
   }
}
