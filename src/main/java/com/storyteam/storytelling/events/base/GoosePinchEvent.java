package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class GoosePinchEvent extends Event {
   private final Player player;

   public GoosePinchEvent(Player player) {
      this.player = player;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }
}
