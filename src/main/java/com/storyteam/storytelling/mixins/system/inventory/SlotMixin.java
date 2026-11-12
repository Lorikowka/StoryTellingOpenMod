package com.storyteam.storytelling.mixins.system.inventory;

import com.storyteam.storytelling.system.inventory.InventorySystem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Slot.class})
public class SlotMixin {
   @Shadow
   @Final
   public Container container;
   @Shadow
   public int index;
   @Shadow
   @Final
   public int slot;

   @Inject(
      method = {"isActive"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void isActive(CallbackInfoReturnable<Boolean> cir) {
      if (this.container instanceof Inventory inv) {
         if (!InventorySystem.isOverrideLogicDisabled(inv.player)) {
            if ((this.slot < 36 || this.slot > 39) && (this.slot < 0 || this.slot >= InventorySystem.getHotBarSize(inv.player)) && this.slot != 40) {
               cir.setReturnValue(false);
            }
         }
      }
   }

   @Inject(
      method = {"mayPickup"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void mayPickup(CallbackInfoReturnable<Boolean> cir) {
      if (this.container instanceof Inventory inv) {
         if (!InventorySystem.isOverrideLogicDisabled(inv.player)) {
            if ((this.slot < 36 || this.slot > 39) && (this.slot < 0 || this.slot >= InventorySystem.getHotBarSize(inv.player)) && this.slot != 40) {
               cir.setReturnValue(false);
            }
         }
      }
   }

   @Inject(
      method = {"mayPlace"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void mayPlace(CallbackInfoReturnable<Boolean> cir) {
      if (this.container instanceof Inventory inv) {
         if (!InventorySystem.isOverrideLogicDisabled(inv.player)) {
            if ((this.slot < 36 || this.slot > 39) && (this.slot < 0 || this.slot >= InventorySystem.getHotBarSize(inv.player)) && this.slot != 40) {
               cir.setReturnValue(false);
            }
         }
      }
   }
}
