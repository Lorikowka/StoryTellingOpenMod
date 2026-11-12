package com.storyteam.storytelling.system.shop.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.screen.GUIColor;
import com.storyteam.storytelling.client.screen.base.IAutoScaled;
import com.storyteam.storytelling.client.screen.base.IHoverableWidget;
import com.storyteam.storytelling.system.shop.data.LotData;
import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.ShopData;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import com.storyteam.storytelling.system.shop.data.TabInfo;
import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.system.shop.screen.widgets.CountWidget;
import com.storyteam.storytelling.system.shop.screen.widgets.ExchangeWidget;
import com.storyteam.storytelling.system.shop.screen.widgets.LotWidget;
import com.storyteam.storytelling.system.shop.screen.widgets.LotsWidget;
import com.storyteam.storytelling.system.shop.screen.widgets.TabWidget;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import com.storyteam.storytelling.system.shop.utils.ShopUtils;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.interpolation.EaseOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ShopScreen extends Screen implements IAutoScaled {
   private static Minecraft MC = Minecraft.getInstance();
   public static boolean NO_SPARKS = false;
   public float ticker = 0.0F;
   public float coolerTicker = 0.0F;
   private final ShopInfo shop;
   public final int backgroundWidth = 445;
   public final int backgroundHeight = 262;
   public int x;
   public int y;
   private double lotOffset;
   private TabInfo selectedTab;
   private LotInfo selectedLot;
   private int count = 1;
   private LotsWidget lotsWidget;

   public ShopScreen(ShopInfo shop) {
      super(Component.empty());
      this.shop = shop;
      this.selectedTab = shop.getTabs().get("main");
      this.selectedLot = this.selectedTab.getLots().values().stream().toList().get(0);
   }

   protected void init() {
      super.init();
      Minecraft.getInstance().options.hideGui = true;
      this.x = (this.width - 445) / 2;
      this.y = (this.height - 262) / 2 + 10;
      int yOff = 0;
      int xOff = 0;
      int step = 0;
      ShopData data = ShopStorage.SHOPS.get(this.shop.getId());
      if (data != null) {
         this.addRenderableWidget(this.lotsWidget = new LotsWidget(this.x + 40, this.y + 20 + 46, 445, 140));
         int tabOffset = 0;
         int xOffTab = 5;

         for (Entry<String, TabInfo> entry : this.getShop().getTabs().entrySet()) {
            int yOffTab = 12;
            this.addRenderableWidget(new TabWidget(this.x + 22 + xOffTab, this.y + 2 + yOffTab, 24, 26, entry.getValue(), this));
            tabOffset++;
            xOffTab += 25;
         }

         for (LotInfo lot : this.selectedTab.getLots().values().stream().filter(entry -> entry.getVisibilityCondition().get()).toList()) {
            LotData d = ShopStorage.SHOPS.get(this.getShop().getId()).getLots().get(lot.getId());
            if (d == null) {
               return;
            }

            this.lotsWidget.getLots().add(new LotWidget(this.x + 40 + xOff, (int)((double)(this.y + 25 + 46 + yOff) + this.lotOffset), 30, 43, lot, this));
            if (++step % 5 == 0) {
               yOff += 50;
               xOff = 0;
            } else {
               xOff += 36;
            }
         }
      }

      this.addRenderableWidget(new ExchangeWidget(this.x + 313, this.y + 195, 88, 14, this));
      this.addRenderableWidget(new CountWidget(this.x + 357 - 40, this.y + 102, this, -5));
      this.addRenderableWidget(new CountWidget(this.x + 357 - 15, this.y + 103, this, -1));
      this.addRenderableWidget(new CountWidget(this.x + 357 + 15 - 7, this.y + 103, this, 1));
      this.addRenderableWidget(new CountWidget(this.x + 357 + 40 - 10, this.y + 102, this, 5));
   }

   public void setTab(TabInfo tab) {
      this.selectedTab = tab;
      this.lotOffset = 0.0;
      this.rebuildWidgets();
   }

   public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      Interpolation interpolation = new EaseOutQuadInterpolation();
      this.ticker = Mth.clamp(this.ticker + pPartialTick / 20.0F, 0.0F, 1.0F);
      this.coolerTicker += pPartialTick / 20.0F;
      float iTicker = interpolation.interpolate((Mth.clamp(this.ticker, 0.0F, 0.5F) * 2.0F + 0.7F) / 1.7F);
      float alpha = interpolation.interpolate(Mth.clamp(this.ticker, 0.0F, 0.5F) * 2.0F);
      pGuiGraphics.fill(
         0,
         0,
         this.width,
         this.height,
         new Color(0.09803922F, 0.08235294F, 0.07450981F, interpolation.interpolate(Mth.clamp(this.ticker, 0.0F, 0.5F) * 2.0F) * 0.65F).getRGB()
      );
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/base_1.png"));
      PoseStack poseStack = pGuiGraphics.pose();
      poseStack.pushPose();
      float tl = -(1.0F - iTicker) * 30.0F;
      poseStack.translate(0.0F, tl, 0.0F);
      this.rebuildWidgets();
      RenderTools.blitWithBlend(pGuiGraphics, (float)this.x, (float)(this.y + 25), 0.0F, 0.0F, 254.0F, 212.0F, 254.0F, 212.0F, 1.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/base_2.png"));
      RenderTools.blitWithBlend(pGuiGraphics, (float)(this.x + 254 + 15), (float)this.y, 0.0F, 0.0F, 176.0F, 262.0F, 176.0F, 262.0F, 1.0F);
      if (this.selectedLot != null && this.selectedLot.getVisibilityCondition().get()) {
         ItemStack stack = this.selectedLot.getStack();
         poseStack.pushPose();
         float scale = 2.0F;
         poseStack.scale(scale, scale, 1.0F);
         int stackX = this.x + 357 - 16;
         int stackY = this.y + 71 - 16;
         pGuiGraphics.renderFakeItem(stack, (int)((float)(this.x + 357) / scale) - 8, (int)((float)(this.y + 71) / scale) - 8);
         poseStack.popPose();
         poseStack.pushPose();
         poseStack.scale(2.0F, 2.0F, 2.0F);
         poseStack.translate(0.0F, 0.0F, 100.0F);
         String count = "x" + this.selectedLot.getCount();
         poseStack.popPose();
         poseStack.pushPose();
         poseStack.scale(0.75F, 0.75F, 0.75F);
         int yOff = 10;
         if (this.getSelectedLot().getData() != null) {
            int stock = this.getSelectedLot().getData().getStock();
            String s0 = this.selectedLot.getStack().getDisplayName().getString();
            String s1 = "Стоимость товара: ";
            String s2 = "Количество: " + this.count + " по " + this.selectedLot.getCount() + " (" + this.count * this.selectedLot.getCount() + " шт.)";
            String s3 = "Итоговая стоимость: ";
            String s4 = this.selectedLot.getLockCondition().get()
               ? "§n§lТовар заблокирован!"
               : (
                  this.selectedLot.getType() == LotType.SELL
                     ? (
                        stock == 0
                           ? "§n§lТовар не принимается!"
                           : (ShopUtils.hasAll(this.getSelectedLot(), this.count, MC.player) ? "§n§lГотово к продаже!" : "§n§lНедостаточно товара!")
                     )
                     : (
                        stock == 0
                           ? "§n§lТовар закончился!"
                           : (this.canAfford(this.selectedLot, this.count) ? "§n§lГотово к покупке!" : "§n§lНедостаточно средств!")
                     )
               );
            int c4 = this.selectedLot.getLockCondition().get()
               ? GUIColor.RED.getHex()
               : (
                  this.selectedLot.getType() == LotType.SELL
                     ? (
                        stock == 0
                           ? GUIColor.RED.getHex()
                           : (ShopUtils.hasAll(this.getSelectedLot(), this.count, MC.player) ? GUIColor.GREEN.getHex() : GUIColor.RED.getHex())
                     )
                     : (stock == 0 ? GUIColor.RED.getHex() : (this.canAfford(this.selectedLot, this.count) ? GUIColor.GREEN.getHex() : GUIColor.RED.getHex()))
               );
            int textWidth = this.font.width(s0);
            float maxTextWidth = 155.0F;
            float textScale = 1.0F;
            if ((float)textWidth > maxTextWidth) {
               textScale = maxTextWidth / (float)textWidth;
               textScale = Mth.clamp(textScale, 0.5F, 1.0F);
            }

            float add = 16.0F;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(textScale, textScale, 0.0F);
            pGuiGraphics.drawString(
               MC.font, s0, (float)(this.x + 296) / textScale / 0.75F, (float)(this.y + yOff + 108) / textScale / 0.75F, GUIColor.WHITE.getHex(), false
            );
            pGuiGraphics.pose().popPose();
            pGuiGraphics.drawString(MC.font, s1, (float)(this.x + 296) / 0.75F, (float)(this.y + yOff + 117) / 0.75F, GUIColor.WHITE.getHex(), false);
            pGuiGraphics.drawString(MC.font, s2, (float)(this.x + 296) / 0.75F, ((float)(this.y + yOff + 126) + add) / 0.75F, GUIColor.WHITE.getHex(), false);
            pGuiGraphics.drawString(MC.font, s3, (float)(this.x + 296) / 0.75F, ((float)(this.y + yOff + 135) + add) / 0.75F, GUIColor.WHITE.getHex(), false);
            pGuiGraphics.drawString(MC.font, s4, (float)(this.x + 296) / 0.75F, (float)(this.y + yOff + 175) / 0.75F, c4, false);
            int cost = this.selectedLot.getCost();
            float a = 0.0F;
            float b = 0.0F;
            if (!NO_SPARKS) {
               a = this.drawSparks(pGuiGraphics, (float)(this.x + 294) / 0.75F, (float)(this.y + 140) / 0.75F, 1.0F, cost, true);
               b = this.drawSparks(
                  pGuiGraphics, (float)(this.x + 294) / 0.75F, (float)(this.y + 174) / 0.75F, 1.0F, this.countSumCost(this.selectedLot, this.getCount()), true
               );
               a -= (float)(this.x + 294) / 0.75F + 7.0F;
               b -= (float)(this.x + 294) / 0.75F + 7.0F;
            }

            if (this.selectedLot.getTradeStack().length > 0) {
               float barterOffset = a;

               for (int i = 0; i < this.selectedLot.getTradeStack().length; i++) {
                  ItemStack st = this.selectedLot.getTradeStack()[i].copy();
                  pGuiGraphics.renderFakeItem(st, (int)(((float)(this.x + 296) + barterOffset) / 0.75F), (int)((float)(this.y + 136) / 0.75F));
                  pGuiGraphics.drawString(
                     MC.font,
                     "§lx" + st.getCount(),
                     ((float)(this.x + 310) + barterOffset) / 0.75F,
                     (float)(this.y + 140) / 0.75F,
                     GUIColor.WHITE.getHex(),
                     true
                  );
                  float w = (float)MC.font.width("§lx" + st.getCount()) * 0.75F;
                  barterOffset += w + 16.0F;
               }
            }

            if (this.selectedLot.getTradeStack().length > 0) {
               float barterOffset = b;

               for (int i = 0; i < this.selectedLot.getTradeStack().length; i++) {
                  ItemStack st = this.selectedLot.getTradeStack()[i].copy();
                  pGuiGraphics.renderFakeItem(st, (int)(((float)(this.x + 296) + barterOffset) / 0.75F), (int)((float)(this.y + 170) / 0.75F));
                  pGuiGraphics.drawString(
                     MC.font,
                     "§lx" + st.getCount() * this.getCount(),
                     ((float)(this.x + 310) + barterOffset) / 0.75F,
                     (float)(this.y + 174) / 0.75F,
                     GUIColor.WHITE.getHex(),
                     true
                  );
                  float w = (float)MC.font.width("§lx" + st.getCount() * this.getCount()) * 0.75F;
                  barterOffset += w + 16.0F;
               }
            }

            poseStack.popPose();
            poseStack.pushPose();
            poseStack.scale(1.5F, 1.5F, 1.5F);
            count = "§lx" + this.getCount();
            pGuiGraphics.drawString(MC.font, count, (float)(this.x + 360) / 1.5F, (float)(this.y + 75) / 1.5F, GUIColor.WHITE.getHex(), true);
            poseStack.popPose();
         }

         pGuiGraphics.pose().pushPose();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         if (pMouseX >= stackX && pMouseX <= stackX + 32 && pMouseY >= stackY && pMouseY <= stackY + 32) {
            pGuiGraphics.renderComponentTooltip(MC.font, stack.getTooltipLines(null, TooltipFlag.NORMAL), pMouseX, pMouseY);
         }

         pGuiGraphics.pose().popPose();
      }

      pGuiGraphics.pose().pushPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/shop_bracket_left.png"));
      float bx = this.sparkWidth(0.75F, this.getCount(), false);
      RenderTools.blitWithBlend(
         pGuiGraphics, (float)(this.x + 345) - bx / 2.0F - 6.0F, (float)(this.y + 222), 0.0F, 0.0F, 5.0F, 16.0F, 5.0F, 16.0F, NO_SPARKS ? 0.0F : 1.0F
      );
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/shop_bracket_right.png"));
      pGuiGraphics.pose().popPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
      poseStack.popPose();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      for (GuiEventListener listener : this.children()) {
         if (listener instanceof AbstractButton) {
            AbstractButton button = (AbstractButton)listener;
            if (button instanceof IHoverableWidget) {
               IHoverableWidget widget = (IHoverableWidget)button;
               if (button.isHoveredOrFocused()) {
                  widget.onHovered(poseStack, pMouseX, pMouseY);
               }
            }
         }
      }
   }

   public float drawSparks(GuiGraphics pGuiGraphics, float x, float y, float scale, int count, boolean reversed) {
      if (reversed) {
         PoseStack poseStack = pGuiGraphics.pose();
         poseStack.pushPose();
         poseStack.translate((double)x, (double)(y - 4.0F * scale) + Math.sin((double)(this.coolerTicker * 2.0F)) * 0.75, 0.0);
         poseStack.scale(scale, scale, 0.0F);
         poseStack.mulPose(Axis.ZP.rotationDegrees((float)Math.sin((double)this.coolerTicker * 1.4) * 5.0F));
         RenderSystem.setShaderTexture(0, St.id("textures/gui/sinclit/sincoin.png"));
         RenderTools.blitWithBlend(pGuiGraphics, 0.0F, 0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F, 12.0F, 1.0F);
         poseStack.popPose();
         poseStack.pushPose();
         poseStack.scale(scale, scale, 1.0F);
         pGuiGraphics.drawString(MC.font, "x" + count, (x + 13.0F) / scale, y / scale, GUIColor.WHITE.getHex(), false);
         poseStack.popPose();
         return (x + 13.0F) / scale + (float)MC.font.width("x" + count) / scale;
      } else {
         float sparks = (float)MC.font.width(String.valueOf(count));
         PoseStack poseStack = pGuiGraphics.pose();
         poseStack.pushPose();
         poseStack.translate((double)(x + (sparks + 8.0F) * scale), (double)(y + 2.0F * scale) + Math.sin((double)(this.coolerTicker * 2.0F)) * 0.75, 0.0);
         poseStack.scale(scale, scale, 0.0F);
         poseStack.mulPose(Axis.ZP.rotationDegrees((float)Math.sin((double)this.coolerTicker * 1.4) * 5.0F));
         RenderSystem.setShaderTexture(0, St.id("textures/gui/sinclit/sincoin.png"));
         RenderTools.blitWithBlend(pGuiGraphics, -6.0F, -6.0F, 0.0F, 0.0F, 12.0F, 12.0F, 12.0F, 12.0F, 1.0F);
         poseStack.popPose();
         poseStack.pushPose();
         poseStack.scale(scale, scale, 1.0F);
         pGuiGraphics.drawString(MC.font, String.valueOf(count), x / scale, y / scale, GUIColor.WHITE.getHex(), false);
         poseStack.popPose();
         return x / scale + sparks / scale;
      }
   }

   public float sparkWidth(float scale, int count, boolean reversed) {
      return reversed ? 13.0F / scale + (float)MC.font.width("x" + count) / scale : (float)(MC.font.width(String.valueOf(count)) + 8) / scale;
   }

   public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
      if ((float)this.selectedTab.getLots().size() / 5.0F <= 2.0F) {
         return false;
      } else {
         this.lotOffset = Mth.clamp(
            this.lotOffset + pDelta * 20.0, (double)Math.min(-(Mth.ceil((float)this.selectedTab.getLots().size() / 5.0F) * 50 - 140), 0), 0.0
         );
         this.rebuildWidgets();
         return super.mouseScrolled(pMouseX, pMouseY, pDelta);
      }
   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      int cornerX = this.x + 11;
      int cornerY = this.y;
      int cornerWidth = 434;
      int cornerHeight = 233;
      return !(pMouseX <= (double)cornerX)
         && !(pMouseX >= (double)(cornerX + cornerWidth))
         && !(pMouseY <= (double)cornerY)
         && !(pMouseY >= (double)(cornerY + cornerHeight))
         && super.mouseClicked(pMouseX, pMouseY, pButton);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean canAfford(LotInfo lot, int count) {
      if (lot.getType() == LotType.SELL) {
         return true;
      } else {
         int cost = this.countSumCost(lot, count);
         Inventory inventory = Minecraft.getInstance().player.getInventory();

         for (ItemStack stack : lot.getTradeStack()) {
            int countInInventory = 0;

            for (int i = 0; i < inventory.getContainerSize(); i++) {
               if (inventory.getItem(i).is(stack.getItem())) {
                  countInInventory += inventory.getItem(i).getCount();
               }
            }

            if (countInInventory < stack.getCount() * count) {
               return false;
            }
         }

         return false;
      }
   }

   public int countSumCost(LotInfo lot, int count) {
      return lot == null ? 0 : count * lot.getFinalCost();
   }

   @Override
   public double defaultScale() {
      return 4.0;
   }

   public void onClose() {
      super.onClose();
      Minecraft.getInstance().options.hideGui = false;
   }

   @Generated
   public ShopInfo getShop() {
      return this.shop;
   }

   @Generated
   public TabInfo getSelectedTab() {
      return this.selectedTab;
   }

   @Generated
   public void setSelectedTab(TabInfo selectedTab) {
      this.selectedTab = selectedTab;
   }

   @Generated
   public void setSelectedLot(LotInfo selectedLot) {
      this.selectedLot = selectedLot;
   }

   @Generated
   public LotInfo getSelectedLot() {
      return this.selectedLot;
   }

   @Generated
   public int getCount() {
      return this.count;
   }

   @Generated
   public void setCount(int count) {
      this.count = count;
   }

   @Generated
   public LotsWidget getLotsWidget() {
      return this.lotsWidget;
   }

   @Generated
   public void setLotsWidget(LotsWidget lotsWidget) {
      this.lotsWidget = lotsWidget;
   }
}
