package com.storyteam.storytelling.events.base;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class DismountEvent extends PlayerEvent {
   public DismountEvent(Player player) {
      super(player);
   }
}
