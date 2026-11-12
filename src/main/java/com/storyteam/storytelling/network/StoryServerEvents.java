package com.storyteam.storytelling.network;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StoryServerEvents {
   @SubscribeEvent
   public static void sendNotification(StoryServerConnectedEvent event) {
   }
}
