package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.script.story.STChunkLoader;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import java.nio.file.Path;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({MinecraftServer.class})
@Implements({@Interface(
      iface = IStoryServer.class,
      prefix = "ISS$"
   )})
public abstract class MinecraftServerMixin {
   @Unique
   private static final LevelResource STORYTELLING_DIR = new LevelResource(".storytelling");
   @Unique
   private final EntityRepository storytelling_5$entityRepository = new EntityRepository((IStoryServer)this);
   @Unique
   private final ScriptEnvironment storytelling_5$scriptEnvironment = new ScriptEnvironment((IStoryServer)this);
   @Unique
   private final STChunkLoader storytelling_5$chunkLoader = new STChunkLoader();
   @Shadow
   @Final
   protected LevelStorageAccess storageSource;

   @Nonnull
   public Path ISS$getStorytellingDir() {
      return this.storageSource.getLevelPath(STORYTELLING_DIR);
   }

   @Nonnull
   public ScriptEnvironment ISS$getScriptEnv() {
      return this.storytelling_5$scriptEnvironment;
   }

   @Nonnull
   public EntityRepository ISS$getEntityRepository() {
      return this.storytelling_5$entityRepository;
   }

   @Nonnull
   public STChunkLoader ISS$getChunkLoader() {
      return this.storytelling_5$chunkLoader;
   }

   public <E> void ISS$postStoryEvent(@Nonnull EventType<E> type, @Nonnull E event) {
      this.ISS$getEntityRepository().postEventToControllers(type, event);
      this.ISS$getScriptEnv().postEventToInstances(type, event);
   }

   public <E> boolean ISS$postCancellableStoryEvent(@Nonnull EventType<E> type, @Nonnull E event, Predicate<E> breakCondition) {
      return this.ISS$getEntityRepository().postCancellableEventToControllers(type, event, breakCondition)
         || this.ISS$getScriptEnv().postCancellableEventToInstances(type, event, breakCondition);
   }
}
