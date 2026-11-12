package com.storyteam.storytelling.events.base;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class ServerEmptyRightClickEvent extends Event {
   public final ServerPlayer player;

   public ServerEmptyRightClickEvent(ServerPlayer player) {
      this.player = player;
   }
}
