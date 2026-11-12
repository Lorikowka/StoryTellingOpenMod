package com.storyteam.storytelling.events.listeners;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class SoundMuffler {
   public static boolean muteBlockSounds = false;

   @SubscribeEvent
   public static void playSound(PlaySoundEvent e) {
      SoundInstance rs = e.getSound();
      if (rs != null) {
         SoundSource cat = rs.getSource();
         if (cat == SoundSource.BLOCKS && muteBlockSounds) {
            e.setSound(null);
         }
      }
   }
}
