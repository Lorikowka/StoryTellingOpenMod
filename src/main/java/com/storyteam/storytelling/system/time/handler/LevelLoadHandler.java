package com.storyteam.storytelling.system.time.handler;

import com.storyteam.storytelling.system.time.core.TimeController;
import lombok.Generated;
import net.minecraftforge.event.level.LevelEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LevelLoadHandler {
   private static boolean paused = false;
   private static double prevRatio = 1.0;

   @SubscribeEvent
   public static void load(Load event) {
   }

   public static void setPaused(boolean paused) {
      LevelLoadHandler.paused = paused;
      if (paused) {
         prevRatio = TimeController.get().getTaskSpeedFactor();
         TimeController.get().setTaskSpeedFactor(0.0);
      } else {
         TimeController.get().setTaskSpeedFactor(prevRatio);
      }
   }

   @Generated
   public static boolean isPaused() {
      return paused;
   }

   @Generated
   public static double getPrevRatio() {
      return prevRatio;
   }
}
