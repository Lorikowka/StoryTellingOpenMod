package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class FlowerOtherMinigameEndEvent extends Event {
   private final Player player;
   private final boolean isPost;

   public FlowerOtherMinigameEndEvent(Player player, boolean isPost) {
      this.player = player;
      this.isPost = isPost;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public boolean isPost() {
      return this.isPost;
   }
}
