package com.storyteam.storytelling.system.shop.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.client.screen.base.IHoverableWidget;
import com.storyteam.storytelling.system.shop.data.LotData;
import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.system.shop.screen.ShopScreen;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import com.storyteam.storytelling.system.shop.utils.ShopUtils;
import com.storyteam.storytelling.util.RenderTools;
import com.storyteam.storytelling.util.interpolation.EaseOutQuadInterpolation;
import com.storyteam.storytelling.util.interpolation.Interpolation;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class LotWidget extends AbstractButton implements IHoverableWidget {
   private static Minecraft MC = Minecraft.getInstance();
   private LotInfo info;
   private ShopScreen screen;

   public LotWidget(int pX, int pY, int pWidth, int pHeight, LotInfo info, ShopScreen screen) {
      super(pX, pY, pWidth, pHeight, Component.empty());
      this.info = info;
      this.screen = screen;
   }

   public void onPress() {
      this.screen.setSelectedLot(this.info);
      this.screen.setCount(1);
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      Interpolation interpolation = new EaseOutQuadInterpolation();
      float alpha = interpolation.interpolate(Mth.clamp(this.screen.ticker, 0.0F, 0.5F) * 2.0F);
      PoseStack poseStack = pGuiGraphics.pose();
      LotData data = ShopStorage.SHOPS.get(this.screen.getShop().getId()).getLots().get(this.info.getId());
      if (data != null) {
         boolean isLocked = this.info.getLockCondition().get();
         boolean isOutOfStock = data.isOutOfStock();
         boolean canAfford = this.info.getType() == LotType.BUY
            ? this.screen.canAfford(this.info, 1)
            : ShopUtils.hasAll(this.info, this.screen.getCount(), MC.player);
         if (isLocked) {
            RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/lot_locked.png"));
         } else if (isOutOfStock) {
            RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/lot_sold.png"));
         } else if (!canAfford) {
            RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/product_cell.png"));
            if (this.screen.getSelectedLot() == this.info) {
               RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/product_cell_glow.png"));
            }
         } else {
            RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/product_cell.png"));
            if (this.screen.getSelectedLot() == this.info) {
               RenderSystem.setShaderTexture(0, St.id("textures/gui/shop/product_cell_glow.png"));
            }
         }

         RenderTools.blitWithBlend(
            pGuiGraphics,
            (float)this.getX(),
            (float)this.getY(),
            0.0F,
            0.0F,
            (float)this.width,
            (float)this.height,
            (float)this.width,
            (float)this.height,
            1.0F
         );
         String type = "§l" + this.info.getType().getName().toUpperCase(Locale.ROOT);
         poseStack.pushPose();
         float s1 = 0.5F;
         poseStack.scale(s1, s1, s1);
         pGuiGraphics.drawString(
            MC.font, type, (float)(this.getX() + 15) / s1 + 1.0F - (float)MC.font.width(type) / 2.0F, (float)(this.getY() + 38) / s1, 14866628, true
         );
         poseStack.popPose();
         poseStack.pushPose();
         pGuiGraphics.renderFakeItem(this.info.getStack(), this.getX() + 7, this.getY() + 8);
         poseStack.popPose();
      }
   }

   @Override
   public void onHovered(PoseStack poseStack, int mouseX, int mouseY) {
      int cornerX = this.screen.x + 11;
      int cornerY = this.screen.y + 15;
      int cornerWidth = 445 - 11;
      int cornerHeight = 262 - 29;
      if (mouseX <= cornerX || mouseX >= cornerX + cornerWidth || mouseY <= cornerY || mouseY >= cornerY + cornerHeight) {
         ;
      }
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
   }

   @Generated
   public LotInfo getInfo() {
      return this.info;
   }

   @Generated
   public ShopScreen getScreen() {
      return this.screen;
   }
}
