package com.storyteam.storytelling.system.sound.handler;

import com.storyteam.storytelling.system.sound.SingleSoundManager;
import com.storyteam.storytelling.system.sound.init.UnitSoundsST;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundClientHandler {
   @SubscribeEvent
   public static void onKeyPress(Key event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
         int key = event.getKey();
         if (event.getAction() == 1 && key == 69) {
            SingleSoundManager.play(UnitSoundsST.SPECIAL_SOUND, 1.0F, 1.0F);
         }
      }
   }
}
