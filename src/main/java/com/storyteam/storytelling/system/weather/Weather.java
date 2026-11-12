package com.storyteam.storytelling.system.weather;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system.weather.net.STWeatherPacket;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.net.IPacket;

public abstract class Weather implements ICustomRegistrar {
   protected boolean enabled = false;
   private String name;

   public static ResourceLocation tx(String id) {
      return St.tx("system/weather/" + id);
   }

   public String name() {
      return this.name;
   }

   public void enable() {
      this.enabled = true;
   }

   public void disable() {
      this.enabled = false;
   }

   public IPacket createPacket() {
      return new STWeatherPacket(this);
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      WeatherSystem.REGISTRY.put(this.name = id.getPath(), this);
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }
}
