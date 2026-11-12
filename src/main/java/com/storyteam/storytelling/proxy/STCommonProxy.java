package com.storyteam.storytelling.proxy;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.zeith.hammerlib.api.proxy.IProxy;

public class STCommonProxy implements IProxy {
   public String getIngameName() {
      return "DedicatedServer@" + System.getProperty("user.name");
   }

   public void renderDebugPath(Mob mob_, @Nullable Path path_, float maxDistanceToWaypoint_) {
   }

   public void setup() {
   }

   public void enqueueToRenderThread(Runnable task) {
   }

   public void enqueueToServerThread(Runnable task) {
      MinecraftServer serv = ServerLifecycleHooks.getCurrentServer();
      if (serv != null) {
         serv.execute(task);
      }
   }
}
