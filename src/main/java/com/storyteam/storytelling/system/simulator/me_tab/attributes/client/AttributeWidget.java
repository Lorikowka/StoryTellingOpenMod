package com.storyteam.storytelling.system.simulator.me_tab.attributes.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.system.octospizdel.client.animation.Tween;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.EaseType;
import com.storyteam.storytelling.system.octospizdel.client.animation.easing.TransitionType;
import com.storyteam.storytelling.system.simulator.me_tab.MeTabSystem;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.net.AttributeConvertPartsToValuePacket;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.utils.RenderUtils;
import com.storyteam.storytelling.util.RenderTools;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.net.Network;

public class AttributeWidget extends AbstractWidget {
   private final MeTabSystem sys = MeTabSystem.INSTANCE;
   AttributeData attributeData;
   public boolean animatedStat;
   private float animatedFill = 1.0F;
   private Tween fillTween = null;

   public AttributeWidget(AttributeData attributeData, int pX, int pY, int pWidth, int pHeight) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.attributeData = attributeData;
   }

   public void updateAttributeAnimation() {
      if (this.attributeData.getParts() > 0 && this.isAnimatedStat() && (this.fillTween == null || !this.fillTween.isRunning())) {
         this.animatedFill = 1.0F;
         this.fillTween = Tween.create().setTransitionType(TransitionType.CIRC).setEase(EaseType.EASE_IN_OUT).setLoops(1);
         this.fillTween
            .tweenMethod(this::setAnimatedFill, 1.0F, 0.0F, 0.5)
            .setDelay(1.0)
            .setEaseType(EaseType.EASE_IN_OUT)
            .setTransitionType(TransitionType.LINEAR);
         this.fillTween.tweenRunnable(() -> Network.sendToServer(new AttributeConvertPartsToValuePacket(this.attributeData.getType())));
         this.fillTween.tweenInterval(0.3F);
         this.fillTween.tweenRunnable(() -> this.setAnimatedStat(false));
         this.fillTween.start();
      }
   }

   protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      if (this.isAnimatedStat()) {
         this.updateAttributeAnimation();
      }

      Minecraft MC = Minecraft.getInstance();
      Font font = MC.font;
      float w = (float)this.width;
      float h = (float)this.height;
      RenderSystem.setShaderTexture(0, this.attributeData.getType().getBaseTexture());
      RenderTools.blitWithBlend(guiGraphics, (float)this.getX(), (float)this.getY(), 0.0F, 0.0F, w, h, w, h, 1.0F);
      if (this.attributeData.getParts() > 0) {
         float fillRatio = (float)this.attributeData.getParts() / (float)this.attributeData.getPARTS_PER_VALUE();
         if (this.isAnimatedStat()) {
            fillRatio *= this.animatedFill;
         }

         float filledHeight = h * fillRatio;
         RenderSystem.setShaderTexture(0, this.attributeData.getType().getFilledTexture());
         float vStart = h - filledHeight;
         RenderTools.blitWithBlend(guiGraphics, (float)this.getX(), (float)this.getY() + (h - filledHeight), 0.0F, vStart, w, filledHeight, w, h, 1.0F);
      }

      if (this.isAnimatedStat()) {
         RenderSystem.setShaderTexture(0, this.sys.guiTex("attribute/" + this.attributeData.getType().name().toLowerCase() + "_on"));
         RenderTools.blitWithBlend(guiGraphics, (float)this.getX(), (float)this.getY(), 0.0F, 0.0F, w, h, w, h, 1.0F);
      }

      String text = String.valueOf(this.attributeData.getValue());
      int centerX = this.getX() + (int)(w / 2.0F) - font.width(text) / 2;
      int centerY = this.getY() + (int)(h / 2.0F) - 9 / 2;
      if (this.attributeData.getType() == AttributeType.BODY) {
         centerY += 5;
      }

      RenderUtils.drawStringWithOutline(guiGraphics, font, text, centerX, centerY, -1, -16777216);
   }

   protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public void setAnimatedStat(boolean animatedStat) {
      this.animatedStat = animatedStat;
   }

   @Generated
   public boolean isAnimatedStat() {
      return this.animatedStat;
   }

   @Generated
   public void setAnimatedFill(float animatedFill) {
      this.animatedFill = animatedFill;
   }
}
