package com.storyteam.storytelling.events.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MinimapTPEvent extends Event {
   @Cancelable
   public static class ToLocation extends MinimapTPEvent {
      public final Player player;
      public final ResourceLocation dimension;
      public final double x;
      public final double y;
      public final double z;

      public ToLocation(Player player, ResourceLocation dimension, double x, double y, double z) {
         this.player = player;
         this.dimension = dimension;
         this.x = x;
         this.y = y;
         this.z = z;
      }
   }
}
