package com.storyteam.storytelling.system.simulator.cognition_tab.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class CognitionKeybinding {
   public static final KeyMapping OPEN_COGNITION_GUI = new KeyMapping("key.open_cognition_gui", 71, "key.categories.main");

   @SubscribeEvent
   public static void onKeyMappingRegister(RegisterKeyMappingsEvent event) {
      event.register(OPEN_COGNITION_GUI);
   }
}
