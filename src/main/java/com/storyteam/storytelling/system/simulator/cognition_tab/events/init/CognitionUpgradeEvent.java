package com.storyteam.storytelling.system.simulator.cognition_tab.events.init;

import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class CognitionUpgradeEvent extends PlayerEvent {
   private final Cognition cognition;
   private final int oldTier;
   private final int newTier;

   public CognitionUpgradeEvent(Player player, Cognition cognition, int oldTier, int newTier) {
      super(player);
      this.cognition = cognition;
      this.oldTier = oldTier;
      this.newTier = newTier;
   }

   public Cognition getCognition() {
      return this.cognition;
   }

   public int getOldTier() {
      return this.oldTier;
   }

   public int getNewTier() {
      return this.newTier;
   }
}
