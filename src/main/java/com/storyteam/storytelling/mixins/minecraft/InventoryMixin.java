package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Inventory.class})
public class InventoryMixin {
   @Final
   @Shadow
   public NonNullList<ItemStack> items;

   @Inject(
      method = {"getName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getName(CallbackInfoReturnable<Component> cir) {
      cir.setReturnValue(Component.empty());
   }
}
