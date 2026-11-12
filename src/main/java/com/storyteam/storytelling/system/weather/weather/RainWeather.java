package com.storyteam.storytelling.system.weather.weather;

import com.storyteam.storytelling.system.shaders.ShaderModifier;
import com.storyteam.storytelling.system.shaders.ShaderSystem;
import com.storyteam.storytelling.system.weather.Weather;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class RainWeather extends Weather {
   public RainWeather() {
      MinecraftForge.EVENT_BUS.addListener(this::tick);
   }

   @Override
   public void enable() {
      super.enable();
      if (FMLEnvironment.dist.isClient()) {
         ShaderSystem.addShader(new ShaderModifier("rain").afterModifier("overworld").beforeModifier("bunker"));
      }

      if (FMLEnvironment.dist.isClient()) {
         ShaderSystem.addShader(new ShaderModifier("rain", "thunder").afterModifier("overworld").beforeModifier("bunker"));
      }
   }

   @Override
   public void disable() {
      super.disable();
      if (FMLEnvironment.dist.isClient()) {
         ShaderSystem.removeShader("rain");
      }
   }

   public void tick(ServerTickEvent event) {
      if (this.enabled && event.phase == Phase.START) {
         event.getServer().overworld().setWeatherParameters(0, 100000000, true, false);
      }
   }
}
