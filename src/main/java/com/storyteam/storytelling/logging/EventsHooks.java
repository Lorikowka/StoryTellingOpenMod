package com.storyteam.storytelling.logging;

import com.storyteam.storytelling.net.PacketLeftClick;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.level.LevelEvent.Unload;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber
public class EventsHooks {
   @SubscribeEvent
   public static void commandInput(ClientChatEvent event) {
      if (event.getOriginalMessage().startsWith("/")) {
         LoggingController.captureCommand(event.getOriginalMessage());
      } else {
         LoggingController.captureChat(event.getOriginalMessage());
      }
   }

   @SubscribeEvent
   public static void clientLeftClick(LeftClickEmpty e) {
      Network.sendToServer(new PacketLeftClick());
   }

   @SubscribeEvent
   public static void leave(Unload event) {
      if (event.getLevel() instanceof ServerLevel && ((ServerLevel)event.getLevel()).dimension() == Level.OVERWORLD) {
         LoggingController.captureUnload();
      }
   }
}
