package com.storyteam.storytelling.client.particle.controllers.custom_controller;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.util.java.tuples.Tuple3;

public class MovementFunctions {
   private static final Map<String, MovementFunctionWithParams<?>> FUNCTIONS = new HashMap<>();

   public static <T> void register(String id, MovementFunction<T> function, T params) {
      FUNCTIONS.put(id, new MovementFunctionWithParams<>(function, params));
   }

   public static <T> MovementFunctionWithParams<T> get(String id) {
      return (MovementFunctionWithParams<T>)FUNCTIONS.get(id);
   }

   static {
      register("default", (info, params) -> info.movement(), 0);
      register("spiral", (info, params) -> {
         double angle = (Double)params.b() * (double)info.age();
         double x = (Double)params.a() * Math.cos(angle);
         double z = (Double)params.a() * Math.sin(angle);
         double y = info.movement().y - (Double)params.c();
         return new Vec3(x, y, z);
      }, new Tuple3(2.0, 0.1, 0.05));
      register("gravity", (info, gravity) -> new Vec3(info.movement().x, info.movement().y - gravity, info.movement().z), 0.05);
      register("gravity", (info, gravity) -> new Vec3(info.movement().x, info.movement().y - gravity, info.movement().z), 0.05);
   }
}
