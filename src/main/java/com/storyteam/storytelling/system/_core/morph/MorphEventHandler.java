package com.storyteam.storytelling.system._core.morph;

import com.storyteam.storytelling.init.seasonal.MorphsST8;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MorphEventHandler {
   @SubscribeEvent
   public static void breakSpeed(BreakSpeed event) {
      IAnimatedPlayer anime = (IAnimatedPlayer)event.getEntity();
      if (anime != null) {
         if (anime.getMorph().equals(MorphsST8.SPOODER) || anime.getMorph().equals(MorphsST8.HEAD)) {
            event.setNewSpeed(0.0F);
         }
      }
   }

   @SubscribeEvent
   public static void attack(LivingHurtEvent event) {
      if (event.getSource().getEntity() instanceof IAnimatedPlayer anime
         && (anime.getMorph().equals(MorphsST8.SPOODER) || anime.getMorph().equals(MorphsST8.HEAD))) {
         event.setAmount(event.getAmount() * 0.3F);
      }
   }
}
