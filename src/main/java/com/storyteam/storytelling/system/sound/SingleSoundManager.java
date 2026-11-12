package com.storyteam.storytelling.system.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class SingleSoundManager {
   private static SoundInstance currentSound;

   public static void play(SoundEvent soundEvent, float volume, float pitch) {
      Minecraft mc = Minecraft.getInstance();
      mc.getSoundManager().stop(currentSound);
      SimpleSoundInstance newSound = SimpleSoundInstance.forUI(soundEvent, volume, pitch);
      currentSound = newSound;
      mc.getSoundManager().play(newSound);
   }

   public static void stop() {
      if (isPlaying()) {
         Minecraft.getInstance().getSoundManager().stop(currentSound);
         currentSound = null;
      }
   }

   public static boolean isPlaying() {
      return Minecraft.getInstance().getSoundManager().isActive(currentSound);
   }
}
