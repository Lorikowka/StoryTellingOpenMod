package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class GooseEatEvent extends Event {
   private final Player player;

   public GooseEatEvent(Player player) {
      this.player = player;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }
}
