package com.storyteam.storytelling.events.base;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class LeftClickEvent extends PlayerEvent {
   public LeftClickEvent(Player player) {
      super(player);
   }
}
