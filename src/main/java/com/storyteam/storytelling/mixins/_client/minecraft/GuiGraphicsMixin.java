package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent.Color;
import net.minecraftforge.client.event.RenderTooltipEvent.Pre;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiGraphics.class})
public abstract class GuiGraphicsMixin {
   @Shadow
   private ItemStack tooltipStack;
   @Final
   @Shadow
   private PoseStack pose;
   @Final
   @Shadow
   private BufferSource bufferSource;

   @Redirect(
      method = {"renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/item/ItemStack;getBarWidth()I"
      )
   )
   private int renderItemDecorationsWidth(ItemStack instance) {
      return Math.round(13.0F - (float)instance.getDamageValue() * 13.0F / (float)instance.getMaxDamage());
   }

   @Inject(
      method = {"renderTooltipInternal"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderTooltipInternal(
      Font pFont, List<ClientTooltipComponent> pComponents, int pMouseX, int pMouseY, ClientTooltipPositioner pTooltipPositioner, CallbackInfo ci
   ) {
      if (!pComponents.isEmpty()) {
         Pre preEvent = ForgeHooksClient.onRenderTooltipPre(
            this.tooltipStack, (GuiGraphics)(Object)this, pMouseX, pMouseY, this.guiWidth(), this.guiHeight(), pComponents, pFont, pTooltipPositioner
         );
         if (preEvent.isCanceled()) {
            return;
         }

         int i = 0;
         int j = pComponents.size() == 1 ? -2 : 0;

         for (ClientTooltipComponent clienttooltipcomponent : pComponents) {
            int k = clienttooltipcomponent.getWidth(preEvent.getFont());
            if (k > i) {
               i = k;
            }

            j += clienttooltipcomponent.getHeight();
         }

         int i2 = i;
         int j2 = j;
         Vector2ic vector2ic = pTooltipPositioner.positionTooltip(this.guiWidth(), this.guiHeight(), preEvent.getX(), preEvent.getY(), i2, j2);
         int l = vector2ic.x();
         int i1 = vector2ic.y();
         this.pose.pushPose();
         int j1 = 400;
         this.drawManaged(
            () -> {
               Color colorEvent = ForgeHooksClient.onRenderTooltipColor(this.tooltipStack, (GuiGraphics)(Object)this, l, i1, preEvent.getFont(), pComponents);
               TooltipRenderUtil.renderTooltipBackground(
                  (GuiGraphics)(Object)this,
                  l,
                  i1,
                  i2,
                  j2,
                  400,
                  colorEvent.getBackgroundStart(),
                  colorEvent.getBackgroundEnd(),
                  colorEvent.getBorderStart(),
                  colorEvent.getBorderEnd()
               );
            }
         );
         this.pose.translate(0.0F, 0.0F, 400.0F);
         int k1 = i1;
         int maxWidth = pComponents.stream().mapToInt(c -> c.getWidth(pFont)).max().orElse(0);

         for (int l1 = 0; l1 < pComponents.size(); l1++) {
            ClientTooltipComponent clienttooltipcomponent1 = pComponents.get(l1);
            if (l1 == 0 && maxWidth < 76) {
               clienttooltipcomponent1.renderText(preEvent.getFont(), l, k1, this.pose.last().pose(), this.bufferSource);
            } else {
               clienttooltipcomponent1.renderText(preEvent.getFont(), l, k1, this.pose.last().pose(), this.bufferSource);
            }

            k1 += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
         }

         k1 = i1;

         for (int k2 = 0; k2 < pComponents.size(); k2++) {
            ClientTooltipComponent clienttooltipcomponent2 = pComponents.get(k2);
            clienttooltipcomponent2.renderImage(preEvent.getFont(), l, k1, (GuiGraphics)(Object)this);
            k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
         }

         this.pose.popPose();
      }

      ci.cancel();
   }

   @Shadow
   public abstract void drawManaged(Runnable var1);

   @Shadow
   public abstract int guiHeight();

   @Shadow
   public abstract int guiWidth();
}


