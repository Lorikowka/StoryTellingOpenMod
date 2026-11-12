package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.events.base.AdvancementEvent;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerAdvancements.class})
public class PlayerAdvancementsMixin {
   @Inject(
      method = {"award"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void award(Advancement pAdvancement, String pCriterionKey, CallbackInfoReturnable<Boolean> cir) {
      if (MinecraftForge.EVENT_BUS.post(new AdvancementEvent())) {
         cir.setReturnValue(false);
         cir.cancel();
      }
   }
}
