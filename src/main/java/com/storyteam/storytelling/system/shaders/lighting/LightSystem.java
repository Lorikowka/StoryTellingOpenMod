package com.storyteam.storytelling.system.shaders.lighting;

import com.storyteam.storytelling.system.shaders.ShaderSystem;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.level.LevelEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

@EventBusSubscriber({Dist.CLIENT})
public class LightSystem {
   public static final Map<BlockPos, LightSource> lightSources = new ConcurrentHashMap<>();

   @SubscribeEvent
   public static void renderTick(RenderTickEvent event) {
      ClientLevel level = Minecraft.getInstance().level;
      synchronized (lightSources) {
         int size = 0;

         for (LightSource lightSource : lightSources.values()) {
            int h = lightSource.maxDistance * 2 + 1;
            size += h * h * h * 4;
         }

         ByteBuffer buffer1 = BufferUtils.createByteBuffer(32 * lightSources.size());
         ByteBuffer buffer2 = BufferUtils.createByteBuffer(size);
         Iterator<Entry<BlockPos, LightSource>> it = lightSources.entrySet().iterator();

         while (it.hasNext()) {
            LightSource lightSource = it.next().getValue();
            lightSource.writeToBuffer(buffer1);
            lightSource.writeLightMap(buffer2);
            if (!lightSource.isInitialized()) {
               lightSource.initLightEngine();
            }

            if (!lightSource.condition.test(level)) {
               it.remove();
            }
         }


      }
   }

   public static void setLight(BlockPos pos, int maxDistance, Vector3f color, Predicate<Level> condition) {
      synchronized (lightSources) {
         if (condition.test(Minecraft.getInstance().level)) {
            if (!lightSources.containsKey(pos)) {
               LightSource newLightSource = new LightSource(Minecraft.getInstance().level, pos, maxDistance);
               newLightSource.initLightEngine();
               lightSources.put(pos, newLightSource);
            }

            LightSource lightSource = lightSources.get(pos);
            lightSource.color = color;
            lightSource.condition = condition;
         } else {
            lightSources.remove(pos);
         }
      }
   }

   public static void blockChanged(BlockPos pos, BlockState state) {
      synchronized (lightSources) {
         for (LightSource light : lightSources.values()) {
            light.updateBlock(pos, state);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void onLoadLevel(Load event) {
      if (event.getLevel() instanceof ClientLevel) {
         synchronized (lightSources) {
            for (LightSource light : lightSources.values()) {
               light.initLightEngine();
            }
         }
      }
   }

   public static void refreshLighting() {
      for (LightSource lightSource : lightSources.values()) {
         lightSource.initLightEngine();
      }
   }
}
