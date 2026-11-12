package com.storyteam.storytelling.proxy;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.client.model.ArmorModelHolder;
import com.storyteam.storytelling.logging.LoggingController;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.proxy.IClientProxy;

public class STClientProxy extends STCommonProxy implements IClientProxy {
   private static boolean freezeParticles = false;
   private static boolean freezeTextures = false;
   public static final Set<Class<? extends Particle>> UNFREEZE_PARTICLES = new HashSet<>(10);

   @Override
   public void enqueueToRenderThread(Runnable task) {
      Minecraft.getInstance().execute(task);
   }

   @Override
   public String getIngameName() {
      return Minecraft.getInstance().getUser().getGameProfile().getName();
   }

   @Override
   public void renderDebugPath(Mob mob_, @Nullable Path path_, float maxDistanceToWaypoint_) {
      if (path_ != null) {
         Minecraft mc = Minecraft.getInstance();
         if (!Storytelling.renderDebugPaths || IStoryEntity.of(mob_).isStorytellingEntity()) {
            mc.execute(() -> mc.debugRenderer.pathfindingRenderer.addPath(mob_.getId(), path_, maxDistanceToWaypoint_));
         }
      }
   }

   @Override
   public void setup() {
      super.setup();
      LoggingController.uuid = Minecraft.getInstance().getUser().getGameProfile().getId();
      LoggingController.nickname = Minecraft.getInstance().getUser().getGameProfile().getName();
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
   }

   private void clientSetup(FMLClientSetupEvent t) {
      ((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener(ArmorModelHolder.RELOADER);
   }

   @Generated
   public static boolean isFreezeParticles() {
      return freezeParticles;
   }

   @Generated
   public static void setFreezeParticles(boolean freezeParticles) {
      STClientProxy.freezeParticles = freezeParticles;
   }

   @Generated
   public static boolean isFreezeTextures() {
      return freezeTextures;
   }

   @Generated
   public static void setFreezeTextures(boolean freezeTextures) {
      STClientProxy.freezeTextures = freezeTextures;
   }
}
