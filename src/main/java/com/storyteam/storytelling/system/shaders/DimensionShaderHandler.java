package com.storyteam.storytelling.system.shaders;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DimensionShaderHandler {
   public static ShaderModifier defaultShaders;
   private static final HashMap<ResourceKey<Level>, ShaderModifier> shadersByDimension = new HashMap<>();

   public static void setDimensionShader(ResourceKey<Level> dimension, ShaderModifier shader) {
      shadersByDimension.put(dimension, shader);
      if (Minecraft.getInstance().level.dimension() == dimension) {
         ShaderSystem.addShader(shader);
      }
   }

   public static void removeDimensionShader(ResourceKey<Level> dimension) {
      ShaderSystem.removeShader(shadersByDimension.get(dimension));
      shadersByDimension.remove(dimension);
   }

   @SubscribeEvent
   public static void onEntityConstructing(EntityConstructing event) {
      if (event.getEntity() instanceof LocalPlayer) {
         if (shadersByDimension.isEmpty()) {
            DimensionShadersST.init();
         }

         for (ShaderModifier s : shadersByDimension.values()) {
            ShaderSystem.removeShader(s);
         }

         if (shadersByDimension.containsKey(event.getEntity().level().dimension())) {
            ShaderSystem.addShader(shadersByDimension.get(event.getEntity().level().dimension()));
         } else if (defaultShaders != null) {
            ShaderSystem.addShader(defaultShaders);
         }
      }
   }
}
