package com.storyteam.storytelling.system.simulator.me_tab.fragment.data;

import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DescribedActionRollback {
   private final Consumer<ServerPlayer> action;
   private final Consumer<ServerPlayer> rollback;
   private final Component description;

   public DescribedActionRollback(Consumer<ServerPlayer> action, Consumer<ServerPlayer> rollback, Component description) {
      this.action = action;
      this.rollback = rollback;
      this.description = description;
   }

   public void applyAction(ServerPlayer player) {
      if (this.action != null) {
         this.action.accept(player);
      }
   }

   public void rollbackAction(ServerPlayer player) {
      if (this.rollback != null) {
         this.rollback.accept(player);
      }
   }

   @Generated
   public Component getDescription() {
      return this.description;
   }
}
