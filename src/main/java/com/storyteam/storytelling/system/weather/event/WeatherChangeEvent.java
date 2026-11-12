package com.storyteam.storytelling.system.weather.event;

import com.storyteam.storytelling.system.weather.Weather;
import lombok.Generated;
import net.minecraftforge.eventbus.api.Event;

public class WeatherChangeEvent extends Event {
   public final Weather weather;

   @Generated
   public WeatherChangeEvent(Weather weather) {
      this.weather = weather;
   }

   @Generated
   public Weather getWeather() {
      return this.weather;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WeatherChangeEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else {
         Object this$weather = this.getWeather();
         Object other$weather = other.getWeather();
         return this$weather == null ? other$weather == null : this$weather.equals(other$weather);
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof WeatherChangeEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      Object $weather = this.getWeather();
      return result * 59 + ($weather == null ? 43 : $weather.hashCode());
   }

   @Generated
   public String toString() {
      return "WeatherChangeEvent(weather=" + this.getWeather() + ")";
   }
}
