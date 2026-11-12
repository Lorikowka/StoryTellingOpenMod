package com.storyteam.storytelling.system.simulator.me_tab.client;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.octospizdel.particle.ScalingParticle;
import com.storyteam.storytelling.system.octospizdel.particle.UIParticle;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.ButtonStyleClass;
import com.storyteam.storytelling.system.simulator.me_tab.client.button.StretchButton;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiButtonHighLightWidget;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiFilledWidget;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiFittingMultiLineTextWidget;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiImageObjectWithTooltip;
import com.storyteam.storytelling.system.simulator.me_tab.client.flowgui.GuiStretchWidget;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.DescribedCondition;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentState;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.net.CheckConditionsFragmentPacket;
import com.storyteam.storytelling.util.Color;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ScreenEvent.BackgroundRendered;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Vector2f;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.math.Point;

public class WordScreen extends Screen {
   protected GuiRootObject scene;
   protected GuiObject gui;
   protected GuiObject image;
   private final MeTabSystem sys = MeTabSystem.INSTANCE;
   MeTabScreen mainScreen;
   Tween tween;
   private boolean animating = false;
   Point startPos;
   float widthElement;
   float heightElement;
   FragmentInstance fragmentInstance;
   StretchButton button;
   int checkTimer = 0;

   public WordScreen(MeTabScreen mainScreen, Point startPos, float widthElement, float heightElement, FragmentInstance fragmentInstance, StretchButton button) {
      super(Component.empty());
      this.mainScreen = mainScreen;
      this.startPos = startPos;
      this.widthElement = widthElement;
      this.heightElement = heightElement;
      this.fragmentInstance = fragmentInstance;
      this.button = button;
   }

   protected void init() {
      super.init();
      this.scene = (GuiRootObject)this.addRenderableWidget(this.createRoot());
      this.openScreen();
      Network.sendToServer(new CheckConditionsFragmentPacket(this.fragmentInstance.getData().getId()));
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

   public void tick() {
      super.tick();
      if (this.scene != null) {
         this.scene.sendUpdate();
      }

      if (this.mainScreen != null) {
         this.mainScreen.tick();
      }

      if (++this.checkTimer >= 100) {
         Network.sendToServer(new CheckConditionsFragmentPacket(this.fragmentInstance.getData().getId()));
         this.checkTimer = 0;
      }
   }

   public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      this.renderBackground(pGuiGraphics);
      super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      CursorParticles.spawnAtCursor((float)pMouseX, (float)pMouseY, this);
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
      Font font = Minecraft.getInstance().font;
      GuiRootObject $root = GuiRootObject.root();
      $root.size((float)this.width, (float)this.height);
      float scale = 0.0F;
      float cx = this.startPos.x() + this.widthElement / 2.0F;
      float cy = this.startPos.y() + this.heightElement / 2.0F;
      this.gui = GuiObject.create("gui")
         .image(this.sys.guiTex("word/background_1"), 0.0F, 0.0F, 214.0F, 235.0F, 214.0F, 235.0F)
         .pivotAtCenter()
         .pos(cx, cy)
         .scale(scale);
      if (this.fragmentInstance != null) {
         FragmentData data = this.fragmentInstance.getData();
         GuiObject word = new GuiStretchWidget("background_word", data.getWord(), new ButtonStyleClass("wordplace", 0, 22, 2, 22, 42)).pos(0.0F, 0.0F);
         word.pos((Float)this.gui.elementWidth.get() / 2.0F - (Float)word.elementWidth.get() / 2.0F, 10.0F);
         int widthIm = 164;
         GuiImageObjectWithTooltip progressBar = new GuiImageObjectWithTooltip(
               "progress_bar", () -> this.sys.guiTex("word/progress_bar"), 0.0F, 0.0F, 0.0F, 14.0F, (float)widthIm, 14.0F
            )
            .tooltip((Supplier<Tooltip>)(() -> {
               MutableComponent component = Component.empty();
               if (this.fragmentInstance.isConditionsPassed()) {
                  int percent = (int)(this.fragmentInstance.progress() * 100.0);
                  component.append(percent + "%");
               } else {
                  component.append("Для начала обдумывания:\n");

                  for (DescribedCondition condition : data.getDescribedConditions()) {
                     component.append(condition.getDescription());
                  }
               }

               return Tooltip.create(component);
            }));
         progressBar.onPreRender((partialTime, mouse) -> {
            double progress = this.fragmentInstance.progress();
            float drawWidth = (float)((double)widthIm * progress);
            progressBar.elementWidth.set(drawWidth);
         }).pos(25.0F, 63.0F);
         this.gui.addChild(word);
         this.gui.addChild(progressBar);
         this.image = GuiObject.create("image")
            .image(this.sys.guiTex("word/image/" + data.getId()), 0.0F, 0.0F, 114.0F, 95.0F, 114.0F, 95.0F)
            .pos(50.0F, 88.0F);
         this.gui.addChild(this.image);
         if (this.fragmentInstance.getState() == FragmentState.READY) {
            int widthBtn = 81;
            int heightBtn = 29;
            int padding = 17;
            GuiButtonHighLightWidget accept = new GuiButtonHighLightWidget(
                  "accept", Component.literal("Принять"), new Color(-16777216), this.sys.guiTex("word/button_accept"), b -> {
                     int lifetime = 20;
                     Tween tween = Tween.create().setTransitionType(TransitionType.LINEAR).setEase(EaseType.EASE_OUT);
                     tween.tweenRunnable(() -> {
                        this.closeScreen();
                        this.button.setAnimatedBtn(true);
                     });
                     tween.tweenInterval(0.6);
                     Vector2f target = this.mainScreen.entityWidget.getCurrentPosition();
                     Vector2f scaleAdd = this.button.getScaleAdd();
                     tween.tweenProperty(this.button, "position", target, 0.5);
                     tween.parallel().tweenMethod(s -> this.button.setScaleAdd(s), scaleAdd, new Vector2f(0.0F, 0.0F), 0.5);
                     tween.tweenInterval((double)((float)lifetime / 20.0F / 2.0F));
                     tween.tweenRunnable(() -> this.button.visible = false);
                     tween.tweenInterval((double)((float)lifetime / 20.0F / 2.0F));
                     tween.tweenRunnable(() -> {
                        this.button.setAnimatedBtn(false);
                        accept(data);
                        this.mainScreen.rebuildButtons();
                     });
                     tween.start();
                  }, Tooltip.create(data.getAcceptAction().getDescription())
               )
               .size((float)widthBtn, (float)heightBtn)
               .pos((float)padding, (Float)this.gui.elementHeight.get() - (float)heightBtn - (float)padding);
            GuiButtonHighLightWidget cancel = new GuiButtonHighLightWidget(
                  "cancel",
                  Component.literal("Отклонить"),
                  new Color(1280255),
                  this.sys.guiTex("word/button_cancel"),
                  b -> {
                     int lifetime = 20;
                     Tween tween = Tween.create();
                     tween.tweenRunnable(() -> {
                        this.closeScreen();
                        this.button.setAnimatedBtn(true);
                     });
                     tween.tweenInterval(0.6);
                     tween.tweenRunnable(
                        () -> {
                           UIParticle uiParticle = new ScalingParticle(
                              0.0F,
                              lifetime,
                              (float)this.button.getX() + (float)this.button.getWidth() / 2.0F,
                              (float)this.button.getY() + (float)this.button.getHeight() / 2.0F,
                              UIParticle.Layer.SCREEN,
                              1.0F,
                              0.0F,
                              (float)this.button.getRadius() * 0.035F,
                              0.0F
                           );
                           uiParticle.setScreen(this.mainScreen);
                           uiParticle.instantiate();
                        }
                     );
                     tween.tweenInterval((double)((float)lifetime / 20.0F / 2.0F));
                     tween.tweenRunnable(() -> this.button.visible = false);
                     tween.tweenInterval((double)((float)lifetime / 20.0F / 2.0F));
                     tween.tweenRunnable(() -> {
                        this.button.setAnimatedBtn(false);
                        reject(data);
                        this.mainScreen.rebuildButtons();
                     });
                     tween.start();
                  },
                  Tooltip.create(data.getRejectAction().getDescription())
               )
               .size((float)widthBtn, (float)heightBtn)
               .pos(
                  (Float)this.gui.elementWidth.get() - (float)widthBtn - (float)padding,
                  (Float)this.gui.elementHeight.get() - (float)heightBtn - (float)padding
               );
            this.gui.addChild(accept);
            this.gui.addChild(cancel);
            GuiObject backgroundText = new GuiFilledWidget("back_text", -1072689136, -804253680)
               .size((Float)this.image.elementWidth.get(), (Float)this.image.elementHeight.get());
            this.image.addChild(backgroundText);
            int widthText = ((Float)this.image.elementWidth.get()).intValue();
            int heightText = ((Float)this.image.elementHeight.get()).intValue();
            GuiObject text = new GuiFittingMultiLineTextWidget(
                  "description_module",
                  multiLineTextWidget -> {
                     multiLineTextWidget.setMultilineWidget(
                        new MultiLineTextWidgetCopy(0, 0, data.getThought(), font, 5).setCentered(true).setMaxWidth(widthText - 16)
                     );
                     multiLineTextWidget.setWidth(widthText - 16);
                     multiLineTextWidget.setHeight(heightText);
                     multiLineTextWidget.setColor(-1);
                     multiLineTextWidget.setScrollColor(554230, 1280255);
                  }
               )
               .pos(7.0F, 0.0F);
            this.image.addChild(text);
         }
      }

      $root.add(this.gui);
      $root.debugBoundaries = false;
      return $root;
   }

   private static void accept(FragmentData data) {
      FragmentInstance inst = FragmentInstanceManager.getActive(data);
      if (inst != null) {
         inst.accept();
      }
   }

   private static void reject(FragmentData data) {
      FragmentInstance inst = FragmentInstanceManager.getActive(data);
      if (inst != null) {
         inst.reject();
      }
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
