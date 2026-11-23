package com.storyteam.storytelling.logging;

import com.storyteam.storytelling.net.PacketLeftClick;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.net.Network;

@EventBusSubscriber
public class EventsHooks {
   @SubscribeEvent
   public static void clientLeftClick(LeftClickEmpty e) {
      Network.sendToServer(new PacketLeftClick());
   }

}
