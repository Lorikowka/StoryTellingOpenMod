package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentState;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

public class ButtonManager {
   private int centerX;
   private int centerY;
   private final int baseRadius;
   private final List<StretchButton> allButtons = new ArrayList<>();
   private final List<List<StretchButton>> pages = new ArrayList<>();
   private int currentPage = 0;
   private float globalRotation = 0.0F;
   private boolean animating = false;
   Tween tweenAnimButs;

   public ButtonManager(int centerX, int centerY, int radius) {
      this.centerX = centerX;
      this.centerY = centerY;
      this.baseRadius = radius;
   }

   public void setCenter(int x, int y) {
      this.setCenterX(x);
      this.setCenterY(y);
   }

   public void createButtons(int screenWidth, int screenHeight, MeTabScreen mainScreen) {
      this.allButtons.clear();
      new Random();
      List<FragmentInstance> fragmentInstances = FragmentInstanceManager.allActive().stream().toList();

      for (int i = 0; i < fragmentInstances.size(); i++) {
         FragmentInstance instance = fragmentInstances.get(i);
         if (instance.getState() != FragmentState.TRIGGERED) {
            FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(instance.getData().getId());
            if (data != null) {
               StretchButton btn = new StretchButton(0, 0, data.getWord(), ButtonStyle.values()[i % ButtonStyle.values().length], mainScreen, instance);
               this.allButtons.add(btn);
            }
         }
      }

      this.calculateScales();
      this.splitIntoPages();
      this.layoutCurrentPage();
      this.gatherButtons(screenWidth, screenHeight);
   }

   public void createTestButtons(int screenWidth, int screenHeight, MeTabScreen mainScreen) {
      this.allButtons.clear();
      new Random();

      for (int i = 0; i < 20; i++) {
         StretchButton btn = new StretchButton(0, 0, Component.literal("Button " + i), ButtonStyle.values()[i % ButtonStyle.values().length], mainScreen);
         this.allButtons.add(btn);
      }

      this.calculateScales();
      this.splitIntoPages();
      this.layoutCurrentPage();
      this.gatherButtons(screenWidth, screenHeight);
   }

   private void splitIntoPages() {
      this.pages.clear();
      List<StretchButton> current = new ArrayList<>();
      double angle = 0.0;

      for (int i = 0; i < this.allButtons.size(); i++) {
         StretchButton btn = this.allButtons.get(i);
         StretchButton next = this.allButtons.get((i + 1) % this.allButtons.size());
         int r = btn.getRadius();
         int nextR = next.getRadius();
         double Ri = (double)(this.baseRadius + r);
         double Rj = (double)(this.baseRadius + nextR);
         double deltaTheta = Math.acos((Ri * Ri + Rj * Rj - (double)((r + nextR) * (r + nextR))) / (2.0 * Ri * Rj));
         if (angle + deltaTheta > Math.PI * 2) {
            this.pages.add(current);
            current = new ArrayList<>();
            angle = 0.0;
         }

         current.add(btn);
         angle += deltaTheta;
      }

      if (!current.isEmpty()) {
         this.pages.add(current);
      }
   }

   public void layoutCurrentPage() {
      if (!this.pages.isEmpty() && !this.animating) {
         List<StretchButton> page = this.pages.get(this.currentPage);
         double angle = (double)this.globalRotation;

         for (int i = 0; i < page.size(); i++) {
            StretchButton btn = page.get(i);
            StretchButton next = page.get((i + 1) % page.size());
            int r = btn.getRadius();
            int nextR = next.getRadius();
            double Ri = (double)(this.baseRadius + r);
            double Rj = (double)(this.baseRadius + nextR);
            double deltaTheta = Math.acos((Ri * Ri + Rj * Rj - (double)((r + nextR) * (r + nextR))) / (2.0 * Ri * Rj));
            double bx = (double)this.centerX + Ri * Math.cos(angle);
            double by = (double)this.centerY + Ri * Math.sin(angle);
            if (!btn.isAnimatedBtn()) {
               btn.setX((int)(bx - (double)btn.getWidth() / 2.0));
               btn.setY((int)(by - (double)btn.getHeight() / 2.0));
            }

            angle += deltaTheta;
         }
      }
   }

   private void calculateScales() {
      float maxSpace = (float)(this.centerX - this.baseRadius);
      float buttonMaxRadius = maxSpace / 2.0F;
      float hoverScale = 1.25F;
      float padding = buttonMaxRadius * (hoverScale - 1.0F);
      buttonMaxRadius -= padding / 2.0F;

      for (StretchButton btn : this.allButtons) {
         float scale = buttonMaxRadius / (float)btn.getRadius();
         if (scale > 1.0F) {
            scale = 1.0F;
         }

         btn.applyGlobalScale(scale);
      }
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      if (!this.pages.isEmpty()) {
         if (!this.animating) {
            this.layoutCurrentPage();
         }

         for (StretchButton b : this.pages.get(this.currentPage)) {
            b.render(graphics, mouseX, mouseY, partialTick);
            if (b.visible) {
               this.renderOffscreenIndicators(graphics, graphics.guiWidth(), graphics.guiHeight());
            }
         }
      }
   }

   private void renderOffscreenIndicators(GuiGraphics gfx, int screenWidth, int screenHeight) {
      if (!this.animating) {
         for (StretchButton btn : this.pages.get(this.currentPage)) {
            int centerXBtn = btn.getX() + btn.getWidth() / 2;
            int centerYBtn = btn.getY() + btn.getHeight() / 2;
            Rectangle buttonRect = btn.getBounds();
            Rectangle screenRect = new Rectangle(0, 0, screenWidth, screenHeight);
            Rectangle intersection = buttonRect.intersection(screenRect);
            float visibleFraction = intersection.isEmpty()
               ? 0.0F
               : (float)(intersection.width * intersection.height) / (float)(btn.getWidth() * btn.getHeight());
            if (!(visibleFraction >= 1.0F)) {
               int maxAlpha = 15;
               int alpha = (int)((1.0F - visibleFraction) * (float)maxAlpha);
               float dx = (float)(centerXBtn - this.centerX);
               float dy = (float)(centerYBtn - this.centerY);
               float angle = (float)Math.atan2((double)dy, (double)dx);
               float radius = (float)Math.min(screenWidth, screenHeight) / 2.0F - 10.0F;
               int indicatorX = (int)((double)this.centerX + (double)radius * Math.cos((double)angle));
               int indicatorY = (int)((double)this.centerY + (double)radius * Math.sin((double)angle));
               int arrowColor = btn.buttonStyle.colorText;
               this.drawTriangle(gfx, (float)indicatorX, (float)indicatorY, 12.0F, angle, arrowColor, alpha);
            }
         }
      }
   }

   private void drawTriangle(GuiGraphics gfx, float x, float y, float size, float angle, int color, int alpha) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      float halfSize = size / 2.0F;
      float[] xs = new float[]{halfSize, -halfSize, -halfSize};
      float[] ys = new float[]{0.0F, -halfSize, halfSize};

      for (int i = 0; i < 3; i++) {
         float px = xs[i];
         float py = ys[i];
         xs[i] = (float)((double)px * Math.cos((double)angle) - (double)py * Math.sin((double)angle)) + x;
         ys[i] = (float)((double)px * Math.sin((double)angle) + (double)py * Math.cos((double)angle)) + y;
      }

      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      Tesselator tess = Tesselator.getInstance();
      BufferBuilder buffer = tess.getBuilder();
      buffer.begin(Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

      for (int i = 0; i < 3; i++) {
         buffer.vertex((double)xs[i], (double)ys[i], 0.0).color(r, g, b, alpha).endVertex();
      }

      tess.end();
      RenderSystem.disableBlend();
   }

   public void nextPage() {
      if (!this.pages.isEmpty()) {
         this.currentPage = (this.currentPage + 1) % this.pages.size();
      }
   }

   public void rotate(float delta) {
      if (!this.getAllButtons().isEmpty()) {
         if (!this.animating && !this.isAnimAnyBtn()) {
            this.globalRotation += delta;
         }
      }
   }

   public boolean isAnimAnyBtn() {
      if (!this.pages.isEmpty()) {
         for (StretchButton btn : this.pages.get(this.currentPage)) {
            if (btn.isAnimatedBtn()) {
               return true;
            }
         }
      }

      return false;
   }

   public void gatherButtons(int screenWidth, int screenHeight) {
      if (!this.pages.isEmpty()) {
         this.setAnimating(true);
         List<StretchButton> current = this.pages.get(this.currentPage);
         List<Vector2f> targetPositions = new ArrayList<>();

         for (StretchButton btn : current) {
            targetPositions.add(new Vector2f((float)btn.getX(), (float)btn.getY()));
         }

         for (StretchButton btn : current) {
            float dx = (float)(btn.getX() - this.centerX);
            float dy = (float)(btn.getY() - this.centerY);
            Vector2f direction = new Vector2f(dx, dy).normalize();
            float scaleX = ((float)btn.getWidth() / 2.0F + (float)Math.max(this.centerX, screenWidth - this.centerX)) / Math.abs(direction.x);
            float scaleY = ((float)btn.getHeight() / 2.0F + (float)Math.max(this.centerY, screenHeight - this.centerY)) / Math.abs(direction.y);
            btn.setX((int)((float)btn.getX() + direction.x * scaleX));
            btn.setY((int)((float)btn.getY() + direction.y * scaleY));
         }

         if (this.tweenAnimButs != null) {
            this.tweenAnimButs.kill();
         }

         this.tweenAnimButs = Tween.create().setTransitionType(TransitionType.SINE).setEase(EaseType.EASE_OUT);

         for (int i = 0; i < current.size(); i++) {
            StretchButton btn = current.get(i);
            Vector2f target = targetPositions.get(i);
            this.tweenAnimButs.parallel().tweenProperty(btn, "position", target, 0.5);
         }

         this.tweenAnimButs.tweenRunnable(() -> this.setAnimating(false));
         this.tweenAnimButs.start();
      }
   }

   public void animatePageSwitch(int screenWidth, int screenHeight) {
      if (!this.pages.isEmpty() && !this.animating) {
         this.setAnimating(true);
         List<StretchButton> current = this.pages.get(this.currentPage);
         if (this.tweenAnimButs != null) {
            this.tweenAnimButs.kill();
         }

         this.tweenAnimButs = Tween.create().setTransitionType(TransitionType.BACK).setEase(EaseType.EASE_IN);

         for (int i = 0; i < current.size(); i++) {
            StretchButton btn = current.get(i);
            float dx = (float)(btn.getX() - this.centerX);
            float dy = (float)(btn.getY() - this.centerY);
            Vector2f direction = new Vector2f(dx, dy).normalize();
            float scaleX = ((float)btn.getWidth() / 2.0F + (float)Math.max(this.centerX, screenWidth - this.centerX)) / Math.abs(direction.x);
            float scaleY = ((float)btn.getHeight() / 2.0F + (float)Math.max(this.centerY, screenHeight - this.centerY)) / Math.abs(direction.y);
            Vector2f target = new Vector2f((float)btn.getX() + direction.x * scaleX, (float)btn.getY() + direction.y * scaleY);
            this.tweenAnimButs.parallel().tweenProperty(btn, "position", target, 0.5);
         }

         this.tweenAnimButs.tweenRunnable(() -> {
            this.setAnimating(false);
            this.nextPage();
            this.layoutCurrentPage();
            this.tweenAnimButs.kill();
            this.gatherButtons(screenWidth, screenHeight);
         });
         this.tweenAnimButs.start();
      }
   }

   public void rebuildButtons(int screenWidth, int screenHeight, MeTabScreen screen) {
      if (!this.pages.isEmpty() && !this.animating) {
         this.setAnimating(true);
         List<StretchButton> current = this.pages.get(this.currentPage);
         if (this.tweenAnimButs != null) {
            this.tweenAnimButs.kill();
         }

         this.tweenAnimButs = Tween.create().setTransitionType(TransitionType.BACK).setEase(EaseType.EASE_IN);

         for (int i = 0; i < current.size(); i++) {
            StretchButton btn = current.get(i);
            float dx = (float)(btn.getX() - this.centerX);
            float dy = (float)(btn.getY() - this.centerY);
            Vector2f direction = new Vector2f(dx, dy).normalize();
            float scaleX = ((float)btn.getWidth() / 2.0F + (float)Math.max(this.centerX, screenWidth - this.centerX)) / Math.abs(direction.x);
            float scaleY = ((float)btn.getHeight() / 2.0F + (float)Math.max(this.centerY, screenHeight - this.centerY)) / Math.abs(direction.y);
            Vector2f target = new Vector2f((float)btn.getX() + direction.x * scaleX, (float)btn.getY() + direction.y * scaleY);
            this.tweenAnimButs.parallel().tweenProperty(btn, "position", target, 0.5);
         }

         this.tweenAnimButs.tweenRunnable(() -> {
            this.setAnimating(false);
            this.createButtons(screenWidth, screenHeight, screen);
         });
         this.tweenAnimButs.start();
      }
   }

   @Generated
   public void setCenterX(int centerX) {
      this.centerX = centerX;
   }

   @Generated
   public void setCenterY(int centerY) {
      this.centerY = centerY;
   }

   @Generated
   public List<StretchButton> getAllButtons() {
      return this.allButtons;
   }

   @Generated
   public List<List<StretchButton>> getPages() {
      return this.pages;
   }

   @Generated
   public int getCurrentPage() {
      return this.currentPage;
   }

   @Generated
   public void setAnimating(boolean animating) {
      this.animating = animating;
   }
}
