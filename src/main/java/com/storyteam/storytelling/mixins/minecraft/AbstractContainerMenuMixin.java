package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.events.base.ContainerSlotClickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractContainerMenu.class})
public abstract class AbstractContainerMenuMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"doClick"},
      cancellable = true
   )
   protected void onClick(int index, int action, ClickType clickType, Player player, CallbackInfo ci) {
      if (index >= 0) {
         AbstractContainerMenu menu = (AbstractContainerMenu)(Object)this;
         Slot slot = (Slot)menu.slots.get(index);
         if (clickType.equals(ClickType.SWAP) && action == 8) {
            ci.cancel();
         } else {
            ContainerSlotClickEvent event = new ContainerSlotClickEvent(
               player, menu, slot, action == 8 ? ClickAction.PRIMARY : ClickAction.SECONDARY, clickType, menu.getCarried(), slot.getItem()
            );
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
               ci.cancel();
            }
         }
      }
   }
}
