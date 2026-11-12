package com.storyteam.storytelling.system.simulator.me_tab.client;

import java.util.Objects;
import java.util.OptionalInt;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.SingleKeyCache;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MultiLineTextWidgetCopy extends AbstractStringWidget {
   private OptionalInt maxWidth = OptionalInt.empty();
   private OptionalInt maxRows = OptionalInt.empty();
   private final SingleKeyCache<MultiLineTextWidgetCopy.CacheKey, MultiLineLabel> cache;
   private boolean centered = false;
   int paddingY;

   public MultiLineTextWidgetCopy(Component pMessage, Font pFont, int paddingY) {
      this(0, 0, pMessage, pFont, paddingY);
   }

   public MultiLineTextWidgetCopy(int pX, int pY, Component pMessage, Font pFont, int paddingY) {
      super(pX, pY, 0, 0, pMessage, pFont);
      this.cache = Util.singleKeyCache(
         p_270516_ -> p_270516_.maxRows.isPresent()
               ? MultiLineLabel.create(pFont, p_270516_.message, p_270516_.maxWidth, p_270516_.maxRows.getAsInt())
               : MultiLineLabel.create(pFont, p_270516_.message, p_270516_.maxWidth)
      );
      this.active = false;
      this.paddingY = paddingY;
   }

   public MultiLineTextWidgetCopy setColor(int pColor) {
      super.setColor(pColor);
      return this;
   }

   public MultiLineTextWidgetCopy setMaxWidth(int pMaxWidth) {
      this.maxWidth = OptionalInt.of(pMaxWidth);
      return this;
   }

   public MultiLineTextWidgetCopy setMaxRows(int pMaxRows) {
      this.maxRows = OptionalInt.of(pMaxRows);
      return this;
   }

   public MultiLineTextWidgetCopy setCentered(boolean pCentered) {
      this.centered = pCentered;
      return this;
   }

   public int getWidth() {
      return ((MultiLineLabel)this.cache.getValue(this.getFreshCacheKey())).getWidth();
   }

   public int getHeight() {
      int var10000 = ((MultiLineLabel)this.cache.getValue(this.getFreshCacheKey())).getLineCount();
      Objects.requireNonNull(this.getFont());
      return var10000 * 9;
   }

   public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      MultiLineLabel $$4 = (MultiLineLabel)this.cache.getValue(this.getFreshCacheKey());
      int $$5 = this.getX();
      int $$6 = this.getY();
      int $$7 = 9;
      int $$8 = this.getColor();
      if (this.centered) {
         $$4.renderCentered(pGuiGraphics, $$5 + this.maxWidth.orElse(this.getWidth()) / 2, $$6 + this.paddingY, $$7, $$8);
      } else {
         $$4.renderLeftAligned(pGuiGraphics, $$5, $$6 + this.paddingY, $$7, $$8);
      }
   }

   private MultiLineTextWidgetCopy.CacheKey getFreshCacheKey() {
      return new MultiLineTextWidgetCopy.CacheKey(this.getMessage(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
   }

   @OnlyIn(Dist.CLIENT)
   static record CacheKey(Component message, int maxWidth, OptionalInt maxRows) {
   }
}
