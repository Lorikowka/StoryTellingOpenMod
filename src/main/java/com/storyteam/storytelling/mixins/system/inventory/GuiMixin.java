package com.storyteam.storytelling.mixins.system.inventory;

import com.storyteam.storytelling.system.inventory.InventorySystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.util.mcf.Resources;

@Mixin({Gui.class})
public abstract class GuiMixin {
   @Shadow
   protected int screenWidth;
   @Shadow
   protected int screenHeight;
   @Shadow
   @Final
   protected Minecraft minecraft;
   private static final ResourceLocation NEW_HOTBAR = Resources.location("storytelling", "textures/system/inventory/hotbar.png");
   private static final ResourceLocation NEW_SELECTION = Resources.location("storytelling", "textures/system/inventory/selection.png");
   private static final ResourceLocation NEW_LOCK = Resources.location("storytelling", "textures/system/inventory/lock.png");
   private static final ResourceLocation LEFT_HAND = Resources.location("storytelling", "textures/system/inventory/left_hand.png");

   @Shadow
   protected abstract void renderSlot(GuiGraphics var1, int var2, int var3, float var4, Player var5, ItemStack var6, int var7);

   @ModifyConstant(
      method = {"renderHotbar"},
      constant = {@Constant(
         intValue = 9
      )}
   )
   private int slots(int value) {
      return InventorySystem.getHotBarSize(this.minecraft.player);
   }

   @Redirect(
      method = {"renderHotbar"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
      )
   )
   private void blitRedirect(GuiGraphics instance, ResourceLocation pAtlasLocation, int pX, int pY, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
      switch (pUWidth) {
         case 24:
            int i = this.screenWidth / 2;

            for (int i1 = InventorySystem.getHotBarSize(this.minecraft.player); i1 < 9; i1++) {
               int j1 = i - 90 + i1 * 20 + 2;
               int k1 = this.screenHeight - 16 - 3;
               instance.blit(NEW_LOCK, j1, k1 - 2, 0.0F, 0.0F, 20, 20, 20, 20);
            }

            instance.blit(NEW_SELECTION, pX + 2, pY, 0.0F, 0.0F, 24, 23, 24, 23);
            break;
         case 29:
            instance.blit(LEFT_HAND, pX + 2, pY + 1, 0.0F, 0.0F, 22, 22, 22, 22);
            break;
         case 182:
            instance.blit(NEW_HOTBAR, pX - 2, pY - 4, 0.0F, 0.0F, 186, 26, 186, 26);
      }
   }

   @Redirect(
      method = {"renderHotbar"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"
      )
   )
   private void renderSlotRedirect(Gui instance, GuiGraphics guiGraphics, int pX, int pY, float pPartialTick, Player pPlayer, ItemStack pStack, int pSeed) {
      this.renderSlot(guiGraphics, pX + 2, pY, pPartialTick, pPlayer, pStack, pSeed);
   }

   @Inject(
      method = {"renderHotbar"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
         shift = Shift.BEFORE
      )}
   )
   private void renderLock(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci) {
   }
}
