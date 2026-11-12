package com.storyteam.storytelling.system.simulator.me_tab.client.history.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentResult;
import com.storyteam.storytelling.util.RenderTools;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WordButton extends AbstractButton {
   protected final MeTabSystem sys = MeTabSystem.INSTANCE;
   FragmentInstance instance;
   protected final WordButton.OnPress onPress;
   float alpha = 0.0F;

   public WordButton(int pX, int pY, int pWidth, int pHeight, FragmentInstance instance, WordButton.OnPress onPress) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.instance = instance;
      this.onPress = onPress;
   }

   public void onPress() {
      this.onPress.onPress(this);
   }

   public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, boolean isHovered) {
      if (this.visible) {
         this.isHovered = isHovered;
         this.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      }
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      int x = this.getX();
      int y = this.getY();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
      ResourceLocation img;
      if (this.isHovered()) {
         img = this.sys.guiTex("history/" + (this.instance.getResult() == FragmentResult.ACCEPTED ? "history_word_card_blue_on" : "history_word_card_pink_on"));
      } else {
         img = this.sys.guiTex("history/" + (this.instance.getResult() == FragmentResult.ACCEPTED ? "history_word_card_blue" : "history_word_card_pink"));
      }

      RenderSystem.setShaderTexture(0, img);
      RenderTools.blitWithBlend(pGuiGraphics, (float)x, (float)y, 0.0F, 0.0F, 72.0F, 94.0F, 72.0F, 94.0F, 1.0F);
      RenderSystem.setShaderTexture(0, this.sys.guiTex("word/image/" + this.instance.getData().getId()));
      RenderTools.blitWithBlend(pGuiGraphics, (float)(x + 5), (float)(y + 5), 0.0F, 0.0F, 60.0F, 58.0F, 60.0F, 58.0F, 1.0F);
      Component word = this.instance.getData().getWord();
      if (this.instance.getResult() == FragmentResult.REJECTED) {
         word = word.copy().withStyle(Style.EMPTY.withStrikethrough(true));
      }

      int textWidth = Minecraft.getInstance().font.width(word);
      int maxWidth = 60;
      float scale = 1.0F;
      if (textWidth > maxWidth) {
         scale = (float)maxWidth / (float)textWidth;
      }

      pGuiGraphics.pose().pushPose();
      float centerX = (float)(x + 5) + (float)maxWidth / 2.0F;
      float centerY = (float)(y + 72);
      pGuiGraphics.pose().translate(centerX, centerY, 0.0F);
      pGuiGraphics.pose().scale(scale, scale, 1.0F);
      pGuiGraphics.drawString(Minecraft.getInstance().font, word, -textWidth / 2, 0, -1, false);
      pGuiGraphics.pose().popPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }

   @Generated
   public float getAlpha() {
      return this.alpha;
   }

   @OnlyIn(Dist.CLIENT)
   public interface OnPress {
      void onPress(WordButton var1);
   }
}
