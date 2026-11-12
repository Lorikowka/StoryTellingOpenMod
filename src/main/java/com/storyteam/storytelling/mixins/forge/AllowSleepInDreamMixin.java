package com.storyteam.storytelling.mixins.forge;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ForgeEventFactory.class})
public class AllowSleepInDreamMixin {
   @Inject(
      method = {"fireSleepingTimeCheck"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void fireSleepingTimeCheck(Player player, Optional<BlockPos> sleepingLocation, CallbackInfoReturnable<Boolean> cir) {
      SleepingTimeCheckEvent evt = new SleepingTimeCheckEvent(player, sleepingLocation);
      MinecraftForge.EVENT_BUS.post(evt);
      Result canContinueSleep = evt.getResult();
      if (canContinueSleep == Result.DEFAULT) {
         cir.setReturnValue(!player.level().isDay() || !player.level().dimension().equals(Level.OVERWORLD));
      } else {
         cir.setReturnValue(canContinueSleep == Result.ALLOW);
      }
   }
}
