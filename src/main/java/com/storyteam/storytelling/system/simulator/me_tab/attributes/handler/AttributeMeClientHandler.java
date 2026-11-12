package com.storyteam.storytelling.system.simulator.me_tab.attributes.handler;

import com.storyteam.storytelling.system.inventory.client.screen.FakeInventoryMenuScreen;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.net.MeTabScreenStatusPacket;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.net.Network;

public class AttributeMeClientHandler {
   private static boolean lastScreenWithAttributeState = false;

   @SubscribeEvent
   public static void tickClient(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft MC = Minecraft.getInstance();
         if (MC.getConnection() != null) {
            boolean open = MC.screen instanceof MeTabScreen || MC.screen instanceof FakeInventoryMenuScreen;
            if (open != lastScreenWithAttributeState) {
               Network.sendToServer(new MeTabScreenStatusPacket(open));
               lastScreenWithAttributeState = open;
            }
         }
      }
   }
}
