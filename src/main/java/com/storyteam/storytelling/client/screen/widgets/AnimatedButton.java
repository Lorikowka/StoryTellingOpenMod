package com.storyteam.storytelling.client.screen.widgets;

import net.minecraft.client.Minecraft;

public abstract class AnimatedButton extends AnimatedWidget {
   public AnimatedButton(int pX, int pY, int width, int height, AnimatedData data) {
      super(pX, pY, width, height, data);
   }

   public abstract void onPress();

   public void onClick(double pMouseX, double pMouseY) {
      this.onPress();
   }

   public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
      if (!this.active || !this.visible) {
         return false;
      } else if (pKeyCode != 257 && pKeyCode != 32 && pKeyCode != 335) {
         return false;
      } else {
         this.playDownSound(Minecraft.getInstance().getSoundManager());
         this.onPress();
         return true;
      }
   }
}
