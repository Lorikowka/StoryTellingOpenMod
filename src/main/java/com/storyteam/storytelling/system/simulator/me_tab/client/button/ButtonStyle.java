package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import net.minecraft.resources.ResourceLocation;

public enum ButtonStyle {
   PINK1("gui_ya_cloud1_pink", 16564223, 39, 2, 39, 55),
   BLUE1("gui_ya_cloud2_blue", 12578559, 39, 2, 39, 55),
   PINK2("gui_ya_cloud3_pink", 16564223, 39, 2, 39, 55),
   BLUE2("gui_ya_cloud4_blue", 12578559, 39, 2, 39, 55),
   PINK3("gui_ya_cloud5_pink", 16564223, 39, 2, 39, 55),
   BLUE3("gui_ya_cloud6_blue", 12578559, 39, 2, 39, 55);

   private final MeTabSystem sys = MeTabSystem.INSTANCE;
   public final String baseName;
   public final int colorText;
   public final int leftWidth;
   public final int midWidth;
   public final int rightWidth;
   public final int height;

   private ButtonStyle(String baseName, int colorText, int leftWidth, int midWidth, int rightWidth, int height) {
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

   public ResourceLocation getLeft() {
      return this.sys.guiTex("buttons/" + this.baseName + "_left");
   }

   public ResourceLocation getMid() {
      return this.sys.guiTex("buttons/" + this.baseName + "_mid");
   }

   public ResourceLocation getRight() {
      return this.sys.guiTex("buttons/" + this.baseName + "_right");
   }

   public ResourceLocation getLeftOn() {
      return this.sys.guiTex("buttons/on/" + this.baseName + "_left_on");
   }

   public ResourceLocation getMidOn() {
      return this.sys.guiTex("buttons/on/" + this.baseName + "_mid_on");
   }

   public ResourceLocation getRightOn() {
      return this.sys.guiTex("buttons/on/" + this.baseName + "_right_on");
   }

   public int getOnWidth() {
      return 45;
   }

   public int getOnheight() {
      return 67;
   }
}
