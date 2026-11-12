package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.weather.Weather;
import com.storyteam.storytelling.system.weather.weather.ClearWeather;
import com.storyteam.storytelling.system.weather.weather.RainWeather;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface WeathersST {
   @RegistryName("clear")
   ClearWeather CLEAR = new ClearWeather();
   @RegistryName("rain")
   Weather RAIN = new RainWeather();
}
