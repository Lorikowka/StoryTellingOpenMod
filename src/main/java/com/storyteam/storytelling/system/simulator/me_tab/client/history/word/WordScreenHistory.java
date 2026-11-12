package com.storyteam.storytelling.system.simulator.me_tab.client.history.word;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.client.CursorParticles;
import com.storyteam.storytelling.system.simulator.me_tab.client.MultiLineTextWidgetCopy;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiFittingMultiLineTextWidget;
import com.storyteam.storytelling.system.simulator.me_tab.client.history.HistoryScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ScreenEvent.BackgroundRendered;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.util.math.Point;

public class WordScreenHistory extends Screen {
   protected GuiRootObject scene;
   protected final MeTabSystem sys = MeTabSystem.INSTANCE;
   protected GuiObject gui;
   HistoryScreen mainScreen;
   FragmentInstance fragmentInstance;
   Tween tween;
   private boolean animating = false;
   Point startPos;
   float widthElement;
   float heightElement;

   public WordScreenHistory(HistoryScreen mainScreen, Point startPos, float widthElement, float heightElement, FragmentInstance fragmentInstance) {
      super(Component.empty());
      this.mainScreen = mainScreen;
      this.startPos = startPos;
      this.widthElement = widthElement;
      this.heightElement = heightElement;
      this.fragmentInstance = fragmentInstance;
   }

   protected void init() {
      super.init();
      Minecraft MC = Minecraft.getInstance();
      Level level = MC.level;
      if (level != null) {
         this.scene = (GuiRootObject)this.addRenderableWidget(this.createRoot());
         this.openScreen();
      }
   }

   public void tick() {
      super.tick();
      if (this.scene != null) {
         this.scene.sendUpdate();
      }
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(graphics);
      super.render(graphics, mouseX, mouseY, partialTick);
      CursorParticles.spawnAtCursor((float)mouseX, (float)mouseY, this);
   }

   public void renderBackground(GuiGraphics pGuiGraphics) {
      assert this.minecraft != null;

      if (this.minecraft.level != null) {
         float progress = (float)((Vec3)this.gui.elementScale.get()).x();
         progress = Math.max(0.0F, Math.min(1.0F, progress));
         int maxAlphaTop = 192;
         int maxAlphaBottom = 208;
         int alphaTop = (int)(192.0F * progress + 0.5F);
         int alphaBottom = (int)(208.0F * progress + 0.5F);
         int rgb = 1052688;
         int topColor = alphaTop << 24 | 1052688;
         int bottomColor = alphaBottom << 24 | 1052688;
         pGuiGraphics.fillGradient(0, 0, this.width, this.height, topColor, bottomColor);
         MinecraftForge.EVENT_BUS.post(new BackgroundRendered(this, pGuiGraphics));
      }
   }

   public GuiRootObject createRoot() {
      Font font = this.minecraft.font;
      GuiRootObject $root = GuiRootObject.root();
      $root.size((float)this.width, (float)this.height);
      float scale = 0.0F;
      float cx = this.startPos.x() + this.widthElement / 2.0F;
      float cy = this.startPos.y() + this.heightElement / 2.0F;
      this.gui = GuiObject.create("gui")
         .image(this.sys.guiTex("history/history_word_base2"), 0.0F, 0.0F, 214.0F, 235.0F, 214.0F, 235.0F)
         .pos(cx, cy)
         .pivotAtCenter()
         .scale(scale);
      $root.add(this.gui);
      GuiObject image = GuiObject.create("image")
         .image(this.sys.guiTex("word/image/" + this.fragmentInstance.getData().getId()), 0.0F, 0.0F, 114.0F, 95.0F, 114.0F, 95.0F)
         .pos(50.0F, 114.0F);
      this.gui.addChild(image);
      int widthText = 150;
      int heightText = 108;
      GuiObject text = new GuiFittingMultiLineTextWidget(
            "description_module",
            multiLineTextWidget -> {
               multiLineTextWidget.setMultilineWidget(
                  new MultiLineTextWidgetCopy(0, 0, this.fragmentInstance.getData().getThought(), font, 10).setCentered(true).setMaxWidth(widthText)
               );
               multiLineTextWidget.setWidth(widthText);
               multiLineTextWidget.setHeight(heightText);
               multiLineTextWidget.setColor(-1);
               multiLineTextWidget.setScrollColor(554230, 1280255);
               multiLineTextWidget.setUseScrollBar(false);
            }
         )
         .pos(30.0F, 4.0F);
      this.gui.addChild(text);
      $root.debugBoundaries = false;
      return $root;
   }

   public void openScreen() {
      this.setAnimating(true);
      if (this.tween != null) {
         this.tween.kill();
      }

      this.tween = Tween.create().setTransitionType(TransitionType.BACK).setEase(EaseType.EASE_OUT);
      Point startPos = (Point)this.gui.elementPosition.get();
      Vec3 startScale = (Vec3)this.gui.elementScale.get();
      Point endPos = new Point(((float)this.width - this.gui.getUnscaledWidth()) / 2.0F, ((float)this.height - this.gui.getUnscaledHeight()) / 2.0F);
      Vec3 endScale = new Vec3(1.0, 1.0, 1.0);
      this.tween.parallel().tweenMethod(p -> this.gui.elementPosition.set(p), startPos, endPos, 0.6);
      this.tween.parallel().tweenMethod(scale -> this.gui.elementScale.set(scale), startScale, endScale, 0.6);
      this.tween.tweenRunnable(() -> this.setAnimating(false));
      this.tween.start();
   }

   public void closeScreen() {
      this.setAnimating(true);
      if (this.tween != null) {
         this.tween.kill();
      }

      this.tween = Tween.create().setTransitionType(TransitionType.BACK).setEase(EaseType.EASE_IN);
      Point startPos = (Point)this.gui.elementPosition.get();
      Vec3 startScale = (Vec3)this.gui.elementScale.get();
      Vec3 endScale = new Vec3(0.0, 0.0, 1.0);
      float cx = (float)((double)(this.startPos.x() + this.widthElement / 2.0F) - 107.0 * endScale.x());
      float cy = (float)((double)(this.startPos.y() + this.heightElement / 2.0F) - 117.5 * endScale.y());
      Point endPos = new Point(cx, cy);
      this.tween.parallel().tweenMethod(p -> this.gui.elementPosition.set(p), startPos, endPos, 0.5);
      this.tween.parallel().tweenMethod(scale -> this.gui.elementScale.set(scale), startScale, endScale, 0.5);
      this.tween.tweenRunnable(() -> {
         this.setAnimating(false);

         assert this.minecraft != null;

         this.minecraft.popGuiLayer();
      });
      this.tween.start();
   }

   public void onClose() {
      if (!this.animating) {
         this.closeScreen();
      }
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      return super.mouseClicked(pMouseX, pMouseY, pButton);
   }

   public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
      if (!this.animating) {
         float x = ((Point)this.gui.elementPosition.get()).x();
         float y = ((Point)this.gui.elementPosition.get()).y();
         float width = (Float)this.gui.elementWidth.get();
         float height = (Float)this.gui.elementHeight.get();
         boolean mouseIsScreen = pMouseX >= (double)x && pMouseY >= (double)y && pMouseX < (double)(x + width) && pMouseY < (double)(y + height);
         if (!mouseIsScreen) {
            this.closeScreen();
         }
      }

      return super.mouseReleased(pMouseX, pMouseY, pButton);
   }

   public boolean isPauseScreen() {
      return false;
   }

   @Generated
   public void setAnimating(boolean animating) {
      this.animating = animating;
   }
}
