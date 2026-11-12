package com.storyteam.storytelling.system.currency.handler;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.currency.type.EidosCurrency;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EidosAnimationTickHandler {
   @SubscribeEvent
   public static void tickTime(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         AnimatedEntity eidos = EidosCurrency.getLivingEntity();
         if (eidos != null) {
            eidos.getAnimationSystem().tick();
         }
      }
   }
}
