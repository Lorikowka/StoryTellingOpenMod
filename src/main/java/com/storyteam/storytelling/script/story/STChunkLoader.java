package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.common.world.ForgeChunkManager.LoadingValidationCallback;
import net.minecraftforge.common.world.ForgeChunkManager.TicketHelper;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringSection;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class STChunkLoader implements LoadingValidationCallback {
   private final HashMap<String, Entity> map = new HashMap<>();

   public STChunkLoader() {
      MinecraftForge.EVENT_BUS.register(this);
      ForgeChunkManager.setForcedChunkLoadingCallback("storytelling", this);
   }

   public void validateTickets(@NotNull ServerLevel level, @NotNull TicketHelper ticketHelper) {
      for (UUID uuid : ticketHelper.getEntityTickets().keySet()) {
         Entity entity = level.getEntity(uuid);
         if (entity == null || IStoryEntity.of(entity).isStorytellingEntity()) {
            ticketHelper.removeAllTickets(uuid);
         }
      }
   }

   public void entityStartTracking(ServerLevel level, String refId, Entity entity) {
      if (!this.map.containsKey(refId)) {
         this.map.put(refId, entity);
         ChunkPos chunkPos = entity.chunkPosition();
         ForgeChunkManager.forceChunk(level, "storytelling", entity, chunkPos.x, chunkPos.z, true, true);
      }
   }

   public void entityStopTracking(ServerLevel level, String refId) {
      if (this.map.containsKey(refId)) {
         Entity entity = this.map.get(refId);
         ChunkPos chunkPos = entity.chunkPosition();
         ForgeChunkManager.forceChunk(level, "storytelling", entity, chunkPos.x, chunkPos.z, false, true);
         this.map.remove(refId);
      }
   }

   @SubscribeEvent
   public void onEnteringJoin(EntityJoinLevelEvent e) {
      if (!e.getLevel().isClientSide) {
         IStoryEntity storyEntity = IStoryEntity.of(e.getEntity());
         if (storyEntity.isStorytellingEntity() && this.map.containsKey(storyEntity.getStoryName())) {
            this.entityStartTracking((ServerLevel)e.getLevel(), storyEntity.getStoryName(), e.getEntity());
         }
      }
   }

   @SubscribeEvent
   public void onEnteringChunk(EnteringSection e) {
      if (!e.getEntity().level().isClientSide && e.didChunkChange()) {
         IStoryEntity storyEntity = IStoryEntity.of(e.getEntity());
         if (storyEntity.isStorytellingEntity() && this.map.containsKey(storyEntity.getStoryName())) {
            ServerLevel level = (ServerLevel)e.getEntity().level();
            ChunkPos lastPos = e.getOldPos().chunk();
            ForgeChunkManager.forceChunk(level, "storytelling", e.getEntity(), lastPos.x, lastPos.z, false, true);
            ChunkPos newPos = e.getNewPos().chunk();
            ForgeChunkManager.forceChunk(level, "storytelling", e.getEntity(), newPos.x, newPos.z, true, true);
         }
      }
   }
}
