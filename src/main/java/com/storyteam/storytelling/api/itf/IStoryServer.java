package com.storyteam.storytelling.api.itf;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.script.story.STChunkLoader;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

public interface IStoryServer {
   static IStoryServer get(MinecraftServer server) {
      return (IStoryServer)server;
   }

   static Optional<IStoryServer> optionally(MinecraftServer server) {
      return Optional.ofNullable(get(server));
   }

   static IStoryServer get() {
      return get(ServerLifecycleHooks.getCurrentServer());
   }

   static Optional<IStoryServer> optionally() {
      return optionally(ServerLifecycleHooks.getCurrentServer());
   }

   @Nonnull
   Path getStorytellingDir();

   @Nonnull
   ScriptEnvironment getScriptEnv();

   @Nonnull
   EntityRepository getEntityRepository();

   @Nonnull
   STChunkLoader getChunkLoader();

   <E> void postStoryEvent(@Nonnull EventType<E> var1, @Nonnull E var2);

   <E> boolean postCancellableStoryEvent(@Nonnull EventType<E> var1, @Nonnull E var2, Predicate<E> var3);

   default ServerLevel getOrLoadDimension(ResourceLocation id) {
      return ((MinecraftServer)this).getLevel(ResourceKey.create(Registries.DIMENSION, id));
   }

   default ServerLevel getOrLoadDimension(ResourceKey<Level> id) {
      return ((MinecraftServer)this).getLevel(id);
   }

   default long getTotalTime() {
      return this.getEntityRepository().ticks;
   }

   default MinecraftServer toMinecraftServer() {
      return (MinecraftServer)this;
   }

   default void tickStory() {
      this.getEntityRepository().tick();
      this.getScriptEnv().tick();
   }
}
