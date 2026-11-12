package com.storyteam.storytelling.system.simulator.me_tab.fragment.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.MouseButton.Pre;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientFragmentHandler {
   @SubscribeEvent
   public static void tickClient(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         ;
      }
   }

   @SubscribeEvent
   public static void onMouseInput(Pre event) {
      if (event.getButton() == 1 && event.getAction() == 1) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.player != null) {
         }
      }
   }
}
