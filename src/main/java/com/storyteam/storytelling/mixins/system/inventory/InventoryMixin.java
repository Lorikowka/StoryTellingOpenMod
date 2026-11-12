package com.storyteam.storytelling.mixins.system.inventory;

import com.storyteam.storytelling.system.inventory.InventorySystem;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.core.NonNullList;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net.minecraft.world.entity.player.Inventory"}
)
public class InventoryMixin {
   @Shadow
   public int selected;
   @Shadow
   @Final
   public Player player;

   @Inject(
      method = {"getSelectionSize"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getSelectionSize(CallbackInfoReturnable<Integer> cir) {
      cir.setReturnValue(InventorySystem.getHotBarSize(ScriptUtils.getPlayer()));
   }

   @Inject(
      method = {"getSelected"},
      at = {@At("HEAD")}
   )
   public void getSelected(CallbackInfoReturnable<ItemStack> cir) {
      int size = InventorySystem.getHotBarSize(this.player);
      if (this.selected >= size) {
         this.selected = 0;
      }
   }

   @ModifyConstant(
      method = {"swapPaint"},
      constant = {@Constant(
         intValue = 9
      )}
   )
   private int slots(int value) {
      return InventorySystem.getHotBarSize(this.player);
   }

   @ModifyConstant(
      method = {"getSuitableHotbarSlot"},
      constant = {@Constant(
         intValue = 9
      )}
   )
   private int getSuitableHotbarSlot(int value) {
      return InventorySystem.getHotBarSize(this.player);
   }

   @Redirect(
      method = {"getFreeSlot"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/core/NonNullList;size()I"
      )
   )
   private int getFreeSlotSize(NonNullList<ItemStack> list) {
      return InventorySystem.getHotBarSize(this.player);
   }
}
