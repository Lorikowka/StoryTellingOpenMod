package com.storyteam.storytelling.system.simulator.me_tab.client.history;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.client.CursorParticles;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.client.history.button.WordButton;
import com.storyteam.storytelling.system.simulator.me_tab.client.history.word.WordScreenHistory;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.util.math.Point;

public class HistoryScreen extends Screen {
   MeTabScreen screen;
   protected GuiRootObject scene;
   protected GuiObject gui;
   protected final MeTabSystem sys = MeTabSystem.INSTANCE;
   HistoryScrollableWidgetList historyScrollableWidgetList = null;
   private boolean animating = false;
   Tween tween;
   Point startPos;
   float widthElement;
   float heightElement;

   public HistoryScreen(MeTabScreen screen, Point startPos, float widthElement, float heightElement) {
      super(Component.empty());
      this.screen = screen;
      this.startPos = startPos;
      this.widthElement = widthElement;
      this.heightElement = heightElement;
   }

   protected void init() {
      super.init();
      Minecraft MC = Minecraft.getInstance();
      Level level = MC.level;
      if (level != null) {
         this.scene = (GuiRootObject)this.addRenderableWidget(this.createRoot());
         this.historyScrollableWidgetList = (HistoryScrollableWidgetList)this.addRenderableWidget(this.createList());
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
      super.render(graphics, mouseX, mouseY, partialTick);
      CursorParticles.spawnAtCursor((float)mouseX, (float)mouseY, this);
   }

   public GuiRootObject createRoot() {
      Font font = this.minecraft.font;
      GuiRootObject $root = GuiRootObject.root();
      $root.size((float)this.width, (float)this.height);
      float scale = 0.0F;
      float cx = this.startPos.x() + this.widthElement / 2.0F;
      float cy = this.startPos.y() + this.heightElement / 2.0F;
      this.gui = GuiObject.create("gui")
         .image(this.sys.guiTex("history/history_word_base1"), 0.0F, 0.0F, 448.0F, 235.0F, 448.0F, 235.0F)
         .pos(cx, cy)
         .pivotAtCenter()
         .scale(scale);
      $root.add(this.gui);
      $root.debugBoundaries = false;
      return $root;
   }

   public HistoryScrollableWidgetList createList() {
      int cx = (this.width - 448) / 2;
      int cy = (this.height - 235) / 2;
      List<FragmentInstance> instances = FragmentInstanceManager.allHistory().stream().toList();
      List<WordButton> buttons = new ArrayList<>();

      for (FragmentInstance instance : instances) {
         WordButton wordButton = new WordButton(
            0,
            0,
            70,
            92,
            instance,
            b -> {
               assert this.minecraft != null;

               this.minecraft
                  .pushGuiLayer(new WordScreenHistory(this, new Point((float)b.getX(), (float)b.getY()), (float)b.getWidth(), (float)b.getHeight(), instance));
            }
         );
         buttons.add(wordButton);
      }

      HistoryScrollableWidgetList historyScrollableWidgetList = new HistoryScrollableWidgetList(
         cx + 6,
         cy + 4,
         ((Float)this.gui.elementWidth.get()).intValue() - 12,
         ((Float)this.gui.elementHeight.get()).intValue() - 8,
         Component.literal("list"),
         5,
         33,
         20,
         false
      );

      for (WordButton button : buttons) {
         historyScrollableWidgetList.addWidget(button);
      }

      return historyScrollableWidgetList;
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

   public void onClose() {
      if (!this.animating) {
         this.closeScreen();
      }
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
      this.tween.tweenInterval(0.1F);

      for (WordButton button : this.historyScrollableWidgetList.getWidgets()) {
         float alpha_start = button.getAlpha();
         float alpha_end = 1.0F;
         this.tween.parallel().tweenMethod(button::setAlpha, alpha_start, alpha_end, 0.3);
      }

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

      for (WordButton button : this.historyScrollableWidgetList.getWidgets()) {
         float alpha_start = button.getAlpha();
         float alpha_end = 0.0F;
         this.tween.parallel().tweenMethod(button::setAlpha, alpha_start, alpha_end, 0.2);
      }

      this.tween.tweenInterval(0.1F);
      this.tween.parallel().tweenMethod(p -> this.gui.elementPosition.set(p), startPos, endPos, 0.5);
      this.tween.parallel().tweenMethod(scale -> this.gui.elementScale.set(scale), startScale, endScale, 0.5);
      this.tween.tweenRunnable(() -> {
         this.setAnimating(false);

         assert this.minecraft != null;

         this.minecraft.popGuiLayer();
      });
      this.tween.start();
   }

   @Generated
   public void setAnimating(boolean animating) {
      this.animating = animating;
   }
}
