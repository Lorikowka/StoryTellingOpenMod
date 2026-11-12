package com.storyteam.storytelling.events.base;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class STDeathEvent extends Event {
   public Player player;
   public boolean dropInv;

   public STDeathEvent(Player player, boolean dropInv) {
      this.player = player;
      this.dropInv = dropInv;
   }
}
