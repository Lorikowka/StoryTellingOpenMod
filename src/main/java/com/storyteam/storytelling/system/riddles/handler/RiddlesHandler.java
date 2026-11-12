package com.storyteam.storytelling.system.riddles.handler;

import com.storyteam.storytelling.content.entities.RiddleEntity;
import com.storyteam.storytelling.system.currency.CurrencySystem;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.persona.Persona;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.riddles.data.RiddlesDef;
import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RiddlesHandler {
   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public static void onChatMessage(ServerChatEvent event) {
      Player player = event.getPlayer();
      String message = event.getMessage().getString().trim().toLowerCase();

      for (Entity entity : player.level().getEntitiesOfClass(RiddleEntity.class, player.getBoundingBox().inflate(5.0))) {
         RiddleEntity riddleEntity = (RiddleEntity)entity;
         if (riddleEntity.shouldShowText() && riddleEntity.canReadText()) {
            RiddlesDef riddle = riddleEntity.getRiddle();
            Persona persona = PersonaHandler.getCurrentPersona();
            if (riddle != null
               && riddle.isVisible()
               && !RiddlesStorage.isSolvedByPersona(persona, riddle.getId())
               && message.equalsIgnoreCase(riddle.getAnswer())) {
               RiddlesStorage.setSolvedForPersona(persona, riddle.getId(), true);
               CurrencyStorage.addCurrency(CurrencySystem.EIDOS_CURRENCY, riddle.getReward());
               break;
            }
         }
      }
   }
}
