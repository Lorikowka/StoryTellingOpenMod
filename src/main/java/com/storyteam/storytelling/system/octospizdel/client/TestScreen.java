package com.storyteam.storytelling.system.octospizdel.client;

import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;

public class TestScreen extends Screen {
   private int ticker;
   private boolean down;
   public Vector2d squeeze = new Vector2d(1.0, 1.0);

   public TestScreen() {
      super(Component.empty());
   }

   protected void init() {
      int x = 8;
      int y = 16;

      for (TransitionType transitionType : TransitionType.values()) {
         for (EaseType easeType : EaseType.values()) {
            this.addRenderableWidget(new TestWidget(x, y, transitionType, easeType, this));
            y += 30;
            if (y + 30 > this.height) {
               x += 120;
               y = 16;
            }
         }
      }
   }

   public void tick() {
      super.tick();
   }

   public void render(GuiGraphics guiGraphics, int i, int j, float f) {
      guiGraphics.pose().pushPose();
      guiGraphics.pose().translate((float)this.width / 2.0F, (float)this.height / 2.0F, 0.0F);
      guiGraphics.pose().scale((float)this.squeeze.x, (float)this.squeeze.y, 1.0F);
      guiGraphics.pose().translate((float)(-this.width) / 2.0F, (float)(-this.height) / 2.0F, 0.0F);
      super.render(guiGraphics, i, j, f);
      guiGraphics.drawString(
         Minecraft.getInstance().font,
         String.valueOf(this.ticker),
         this.width - Minecraft.getInstance().font.width(String.valueOf(this.ticker)) - 4,
         4,
         -1,
         true
      );
      guiGraphics.drawString(
         Minecraft.getInstance().font,
         String.valueOf(f),
         this.width - Minecraft.getInstance().font.width(String.valueOf(f)) - 4,
         this.height - 10,
         872415231,
         true
      );
      guiGraphics.pose().popPose();
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean result = super.mouseClicked(mouseX, mouseY, button);
      if (!result && button == 1) {
         for (GuiEventListener g : this.children()) {
            if (g instanceof TestWidget widget) {
               widget.onClick(mouseX, mouseY);
            }
         }
      } else if (!result && button == 0) {
         Tween tween = Tween.create().setTransitionType(TransitionType.SINE).setParallel(true);
         tween.tweenProperty(this, "squeeze.y", 0.9, 0.15).setEaseType(EaseType.EASE_OUT);
         tween.tweenProperty(this, "squeeze.x", 0.9, 0.15).setEaseType(EaseType.EASE_IN);
         tween.tweenProperty(this, "squeeze.y", 1, 0.15).setDelay(0.15).setEaseType(EaseType.EASE_OUT);
         tween.tweenProperty(this, "squeeze.x", 1, 0.15).setDelay(0.15).setEaseType(EaseType.EASE_IN);
      }

      return result;
   }

   public boolean isPauseScreen() {
      return true;
   }

   public void onClose() {
      super.onClose();
   }
}
