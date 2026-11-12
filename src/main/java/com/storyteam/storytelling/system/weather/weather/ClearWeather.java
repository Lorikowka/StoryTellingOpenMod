package com.storyteam.storytelling.system.weather.weather;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.system.weather.Weather;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

public class ClearWeather extends Weather {
   public ClearWeather() {
      MinecraftForge.EVENT_BUS.addListener(this::tick);
   }

   public void tick(PlayerTickEvent event) {
      if (this.enabled) {
         IStoryServer.get().toMinecraftServer().overworld().setWeatherParameters(100000000, 0, false, false);
      }
   }
}
