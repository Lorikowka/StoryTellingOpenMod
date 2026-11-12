package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.content.entities.ChairEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.state.PlayerScriptState;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerSitAction extends ScriptPerformerAction {
   @Nullable
   private final Supplier<Location> location;
   private final boolean lockUnmount;
   private boolean isDone;

   public PlayerSitAction(ScriptPerformer performer, @Nullable Supplier<Location> location, boolean lockUnmount) {
      super(performer);
      this.location = location;
      this.lockUnmount = lockUnmount;
      this.isDone = false;
   }

   @Override
   public void tick() {
      PlayerScriptState state = this.performer.getState(PlayerScriptState.TYPE);
      ServerPlayer player = ScriptUtils.getPlayer();
      boolean playerIsPassenger = player.isPassenger();
      if (this.lockUnmount) {
         state.sitLocation = this.location == null ? new Location(player) : this.location.get();
         this.isDone = true;
      } else if (state.sitLocation != null) {
         state.sitLocation = null;
         this.isDone = true;
      } else {
         if (!playerIsPassenger) {
            ChairEntity chair = (ChairEntity)EntitiesST.CHAIR.create(player.level());
            if (this.location == null) {
               chair.setPos(player.getX(), player.getY(), player.getZ());
            } else {
               chair.setPos(this.location.get().x, this.location.get().y, this.location.get().z);
            }

            ResourceKey<Level> dimensionToSit = this.location != null ? this.location.get().getDimension() : player.level().dimension();
            ServerLevel level = ScriptUtils.server().getLevel(dimensionToSit);
            if (this.location != null) {
               player.teleportTo(
                  level, this.location.get().x, this.location.get().y, this.location.get().z, this.location.get().getYaw(), this.location.get().getPitch()
               );
            }

            level.addFreshEntity(chair);
            player.startRiding(chair);
            if (this.location != null) {
               player.setYRot(this.location.get().getYaw());
               player.setXRot(this.location.get().getPitch());
            }

            this.isDone = true;
         }

         this.isDone = true;
      }
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("location", this.location).append("lockUnmount", this.lockUnmount).build();
   }
}
