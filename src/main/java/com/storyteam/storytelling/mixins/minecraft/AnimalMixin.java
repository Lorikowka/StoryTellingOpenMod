package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.events.FeedAnimalEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Animal.class})
public class AnimalMixin {
   @Inject(
      method = {"mobInteract"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/animal/Animal;usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"
      )}
   )
   public void mobInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
      MinecraftForge.EVENT_BUS.post(new FeedAnimalEvent((Animal)(Object)this, pPlayer, pPlayer.getItemInHand(pHand)));
   }
}
