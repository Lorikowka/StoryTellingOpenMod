package com.storyteam.storytelling.util;

import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class AI {
   public static <T extends Entity> Consumer<Entity> $(Class<T> type, Consumer<T> handler) {
      return e -> {
         if (type.isInstance(e)) {
            handler.accept((T)e);
         }
      };
   }

   public static void noAI(Mob entity) {
      entity.setNoAi(true);
   }

   public static void noGoals(Mob entity) {
      entity.goalSelector.getAvailableGoals().clear();
      entity.targetSelector.getAvailableGoals().clear();
   }

   public static void lookClosest(Mob entity) {
      Player nearestPlayer = entity.level().getNearestPlayer(entity, 32.0);
      if (nearestPlayer != null) {
         entity.getLookControl().setLookAt(nearestPlayer, 5.0F, 5.0F);
      }
   }
}
