package com.storyteam.storytelling.system.shop.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.screen.GUIColor;
import com.storyteam.storytelling.system.shop.net.ShopExchangePacket;
import com.storyteam.storytelling.system.shop.screen.ShopScreen;
import com.storyteam.storytelling.util.RenderTools;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.zeith.hammerlib.net.Network;

public class ExchangeWidget extends AbstractButton {
   private static Minecraft MC = Minecraft.getInstance();
   private ShopScreen screen;

   public ExchangeWidget(int pX, int pY, int pWidth, int pHeight, ShopScreen screen) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.screen = screen;
   }

   public void onPress() {
      if (this.screen.getSelectedLot() != null) {
         boolean isLocked = this.screen.getSelectedLot().getLockCondition().get();
         boolean isOutOfStock = this.screen.getSelectedLot().getData().isOutOfStock();
         boolean canAfford = this.screen.canAfford(this.screen.getSelectedLot(), this.screen.getCount());
         if (!isLocked && !isOutOfStock && canAfford) {
            Network.sendToServer(
               new ShopExchangePacket(
                  this.screen.getShop().getId(), this.screen.getSelectedLot().getTabId(), this.screen.getSelectedLot().getId(), this.screen.getCount()
               )
            );
         }
      }
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      PoseStack poseStack = pGuiGraphics.pose();
      boolean isRlyHovered = this.screen.getSelectedLot() != null && this.isHoveredOrFocused();
      RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/buy_button" + (isRlyHovered ? "_light" : "") + ".png"));
      RenderTools.blitWithBlend(
         pGuiGraphics, (float)this.getX(), (float)this.getY(), 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.width, (float)this.height, 1.0F
      );
      MutableComponent action = Component.literal(
         (this.screen.getSelectedLot() == null ? "§lВыберите товар" : "§l" + this.screen.getSelectedLot().getType().getName().toUpperCase(Locale.ROOT))
            .toUpperCase(Locale.ROOT)
      );
      float scale = 0.75F;
      poseStack.pushPose();
      poseStack.scale(scale, scale, scale);
      pGuiGraphics.drawString(
         MC.font,
         action,
         (int)(((float)this.getX() + (float)this.width / 2.0F) / scale - (float)MC.font.width(action) / 2.0F),
         (int)((float)(this.getY() + 4) / scale) + 1,
         GUIColor.WHITE.getHex(),
         false
      );
      poseStack.popPose();
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public ShopScreen getScreen() {
      return this.screen;
   }
}
