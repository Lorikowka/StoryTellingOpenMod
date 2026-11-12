package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class BunkerEnterExitEvent extends Event {
   private final Player player;
   private final boolean isEnter;

   public BunkerEnterExitEvent(Player player, boolean isEnter) {
      this.player = player;
      this.isEnter = isEnter;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public boolean isEnter() {
      return this.isEnter;
   }
}
