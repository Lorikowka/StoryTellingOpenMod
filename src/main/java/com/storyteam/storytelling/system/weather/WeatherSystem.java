package com.storyteam.storytelling.system.weather;

import com.storyteam.storytelling.init.system.WeathersST;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.weather.event.WeatherChangeEvent;
import com.storyteam.storytelling.util.Necr0manthUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("weather")
public class WeatherSystem implements ISystem {
   public static final Map<String, Weather> REGISTRY = new HashMap<>();
   private static Weather currentWeather = WeathersST.CLEAR;

   public static Weather currentWeather() {
      return currentWeather;
   }

   public static void setWeather(Weather weather) {
      if (weather != currentWeather) {
         currentWeather().disable();
         currentWeather = weather;
         weather.enable();
         MinecraftForge.EVENT_BUS.post(new WeatherChangeEvent(weather));
         Necr0manthUtils.saveStConfig("lastWeather.txt", weather.name());
      }
   }

   private static void playerLoggedIn(PlayerLoggedInEvent event) {
      if (currentWeather != WeathersST.CLEAR) {
         setWeather(WeathersST.CLEAR);
      } else {
         REGISTRY.getOrDefault(Necr0manthUtils.readStConfig("lastWeather.txt"), WeathersST.CLEAR).disable();
      }

      Necr0manthUtils.saveStConfig("lastWeather.txt", WeathersST.CLEAR.name());
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.addListener(WeatherSystem::playerLoggedIn);
      gameBus.register(WeatherCommand.class);
   }
}
