package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Key;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRules.class})
public class GameRulesMixin {
   @Inject(
      method = {"getBoolean"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void naturalRegeneration(Key<BooleanValue> pKey, CallbackInfoReturnable<Boolean> cir) {
      if (pKey == GameRules.RULE_NATURAL_REGENERATION) {
         cir.setReturnValue(false);
         cir.cancel();
      }
   }
}
