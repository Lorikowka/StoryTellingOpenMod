package com.storyteam.storytelling.events.base;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class F8PressEvent extends PlayerEvent {
   public F8PressEvent(Player player) {
      super(player);
   }
}
