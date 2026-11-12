package com.storyteam.storytelling.system.sounds;

import com.storyteam.storytelling.events.listeners.ScriptIO;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SoundUtils {
   public static float getSoundVolume(SoundEvent sound) {
      String key = sound.getLocation().toLanguageKey();
      CompoundTag volumes = ScriptIO.getSubTag("Volumes");
      return volumes.contains(key) ? volumes.getFloat(key) : 1.0F;
   }

   public static void setSoundVolume(SoundEvent sound, float volume) {
      CompoundTag volumes = ScriptIO.getSubTag("Volumes");
      volumes.putFloat(sound.getLocation().toLanguageKey(), volume);
   }

   public static void playVolumedSound(Player player, SoundEvent sound) {
      if (player != null) {
         float volume = getSoundVolume(sound);
         if (player.level().isClientSide()) {
            for (float i = 0.0F; i < volume; i++) {
               player.level().playSound(player, player.blockPosition(), sound, SoundSource.HOSTILE, volume, 1.0F);
            }
         } else {
            playVolumedSound(player.level(), player.blockPosition(), sound);
         }
      }
   }

   public static void playVolumedSound(Level level, BlockPos pos, SoundEvent sound) {
      float volume = getSoundVolume(sound);

      for (float i = 0.0F; i < volume; i++) {
         level.playSound(null, pos, sound, SoundSource.HOSTILE, volume, 1.0F);
      }
   }
}
