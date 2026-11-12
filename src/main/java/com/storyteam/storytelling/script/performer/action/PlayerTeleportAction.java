package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.events.listeners.BaseHooks;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ServerScheduler;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;

public class PlayerTeleportAction extends ScriptPerformerAction {
   private final Supplier<Location> location;
   private int ticks;

   public PlayerTeleportAction(ScriptPerformer performer, Location location) {
      super(performer);
      this.location = () -> location;
   }

   public PlayerTeleportAction(ScriptPerformer performer, Supplier<Location> location) {
      super(performer);
      this.location = location;
   }

   @Override
   public void tick() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (this.ticks == 0) {
         BaseHooks.teleportUnlock = true;
         if (player.isPassenger()) {
            player.stopRiding();
         }

         this.location.get().teleport(player);
         player.fallDistance = 0.0F;
         ServerScheduler.schedule(10, () -> BaseHooks.teleportUnlock = false);
      }

      this.ticks++;
   }

   @Override
   public void reset() {
      this.ticks = 0;
   }

   @Override
   public void rewindToStage() {
      this.ticks = 0;
   }

   @Override
   public boolean isDone() {
      ServerPlayer player = ScriptUtils.getPlayer();
      return this.ticks > 10 && player.level().isAreaLoaded(player.blockPosition(), 2);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("location", this.location.get()).build();
   }
}
