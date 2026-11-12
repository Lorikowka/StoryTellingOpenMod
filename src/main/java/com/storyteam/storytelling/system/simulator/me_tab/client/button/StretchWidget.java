package com.storyteam.storytelling.system.simulator.me_tab.client.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.utils.RenderUtils;
import com.storyteam.storytelling.util.RenderTools;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class StretchWidget extends AbstractWidget {
   private final int midWidth;
   private static final int TEXT_PADDING = 12;
   private final int baseWidth;
   private final int baseHeight;
   private final MeTabSystem sys = MeTabSystem.INSTANCE;
   ButtonStyleClass buttonStyle;
   private final Vector2f position = new Vector2f(0.0F, 0.0F);

   public StretchWidget(int x, int y, Component text, ButtonStyleClass style) {
      super(x, y, 0, style.height, text);
      StretchWidget.ButtonSize size = calcButtonSize(text, style);
      this.width = size.totalWidth;
      this.midWidth = size.midWidth;
      this.baseWidth = this.width;
      this.baseHeight = this.height;
      this.buttonStyle = style;
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

   private static StretchWidget.ButtonSize calcButtonSize(Component text, ButtonStyleClass style) {
      int textWidth = Minecraft.getInstance().font.width(text);
      int baseWidthButton = style.getTotalWidth();
      int neededWidthButton = textWidth + 24;
      int extra = Math.max(0, neededWidthButton - baseWidthButton);
      int finalMid = style.midWidth + extra;
      int total = style.leftWidth + finalMid + style.rightWidth;
      return new StretchWidget.ButtonSize(total, finalMid);
   }

   public void renderWidget(@NotNull GuiGraphics gfx, int pMouseX, int pMouseY, float pPartialTick) {
      this.isHovered();
      gfx.pose().pushPose();
      int x = this.getX();
      int y = this.getY();
      RenderSystem.setShaderTexture(0, this.sys.guiTex("word/buttons/wordplace_left"));
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
      RenderSystem.setShaderTexture(0, this.sys.guiTex("word/buttons/wordplace_mid"));
      RenderTools.blitWithBlend(
         gfx, (float)midX, (float)y, 0.0F, 0.0F, (float)this.midWidth, (float)this.baseHeight, (float)this.midWidth, (float)this.baseHeight, 1.0F
      );
      int rightX = midX + this.midWidth;
      RenderSystem.setShaderTexture(0, this.sys.guiTex("word/buttons/wordplace_right"));
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
      RenderUtils.drawStringWithOutline(gfx, Minecraft.getInstance().font, this.getMessage(), textX, textY, -1, -16777216);
      gfx.pose().popPose();
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public int getBaseWidth() {
      return this.baseWidth;
   }

   @Generated
   public int getBaseHeight() {
      return this.baseHeight;
   }

   public static record ButtonSize(int totalWidth, int midWidth) {
   }
}
