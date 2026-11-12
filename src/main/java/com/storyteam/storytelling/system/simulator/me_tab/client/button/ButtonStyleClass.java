package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;

public class ButtonStyleClass {
   private final MeTabSystem sys = MeTabSystem.INSTANCE;
   public final String baseName;
   public final int colorText;
   public final int leftWidth;
   public final int midWidth;
   public final int rightWidth;
   public final int height;

   public ButtonStyleClass(String baseName, int colorText, int leftWidth, int midWidth, int rightWidth, int height) {
      this.baseName = baseName;
      this.colorText = colorText;
      this.leftWidth = leftWidth;
      this.midWidth = midWidth;
      this.rightWidth = rightWidth;
      this.height = height;
   }

   public int getTotalWidth() {
      return this.leftWidth + this.midWidth + this.rightWidth;
   }
}
