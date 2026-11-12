package com.storyteam.storytelling.system.weather.net;

import com.storyteam.storytelling.system.weather.Weather;
import com.storyteam.storytelling.system.weather.WeatherSystem;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class STWeatherPacket implements IPacket {
   public Weather weather;

   public STWeatherPacket(Weather weather) {
      this.weather = weather;
   }

   public STWeatherPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.weather.name());
   }

   public void read(FriendlyByteBuf buf) {
      this.weather = WeatherSystem.REGISTRY.get(buf.readUtf());
   }

   public void execute(PacketContext ctx) {
      WeatherSystem.setWeather(this.weather);
   }
}
