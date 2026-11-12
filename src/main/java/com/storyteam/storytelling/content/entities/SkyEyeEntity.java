package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.init.EntityDataSerializersST;
import com.storyteam.storytelling.system.animation.EyeGazeSystem;
import com.storyteam.storytelling.system.animation.EyeGazeSystemSkyEye;
import com.storyteam.storytelling.util.LayersList;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.world.ForgeChunkManager;

public class SkyEyeEntity extends AnimatedEntity implements IGazingEntity {
   private static final EntityDataAccessor<Vec3> SPAWN_POS = SynchedEntityData.defineId(SkyEyeEntity.class, EntityDataSerializersST.VEC3);
   private UUID ownerUUID;
   protected EyeGazeSystem eyeGaze;
   private ChunkPos forcedChunk;

   public SkyEyeEntity(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
   }

   @Override
   public void addLayers(LayersList layers) {
      this.eyeGaze = new EyeGazeSystemSkyEye(this, "EyeGaze");
      this.eyeGaze.setupLayers(layers);
   }

   protected void manageEyeGaze() {
      if (this.level().isClientSide) {
         StoryData sd = StoryData.of(this);
         float yRot = this.yHeadRot;
         float xRot = this.getXRot();
         if (sd != null) {
            if (sd.lockHead.getBoolean()) {
               yRot = this.yBodyRot;
            }

            if (sd.blockBody.getBoolean()) {
               if (sd.lockHead.getBoolean()) {
                  yRot = this.yBodyRot;
               } else {
                  yRot = this.yHeadRot;
               }
            }
         }

         this.eyeGaze.updateClient(this.getEyePosition(1.0F).add(0.0, 0.18, 0.0), xRot, yRot, this.animationSystem);
      }
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(SPAWN_POS, new Vec3(0.0, 0.0, 0.0));
   }

   public Player getOwner() {
      return this.ownerUUID != null && !this.level().isClientSide ? this.level().getPlayerByUUID(this.ownerUUID) : null;
   }

   public void setSpawnPoint(Vec3 vec3) {
      this.entityData.set(SPAWN_POS, vec3);
   }

   public Vec3 getSpawnPos() {
      return (Vec3)this.entityData.get(SPAWN_POS);
   }

   public Vec3 getRenderPos() {
      Vec3 spawnPos = (Vec3)this.entityData.get(SPAWN_POS);
      Player owner = this.getOwner();
      double y = spawnPos.y;
      if (owner != null) {
         y = owner.getY() + 0.0;
      }

      return new Vec3(spawnPos.x, spawnPos.y, spawnPos.z);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         Player owner = this.getOwner();
         if (owner != null) {
            Vec3 ownerPos = owner.position();
            Vec3 spawnPos = this.getSpawnPos();
            Vec3 dir = spawnPos.subtract(ownerPos).normalize();
            Vec3 scale = dir.scale(32.0);
            Vec3 target = ownerPos.add(scale);
            this.setPos(target.x, target.y, target.z);
         }
      }

      this.manageEyeGaze();
   }

   public void setPos(double x, double y, double z) {
      super.setPos(x, y, z);
      if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
         ChunkPos current = this.chunkPosition();
         if (this.forcedChunk == null || !this.forcedChunk.equals(current)) {
            if (this.forcedChunk != null) {
               ForgeChunkManager.forceChunk(serverLevel, "storytelling", this, this.forcedChunk.x, this.forcedChunk.z, false, true);
            }

            ForgeChunkManager.forceChunk(serverLevel, "storytelling", this, current.x, current.z, true, true);
            this.forcedChunk = current;
         }
      }
   }

   public int getMaxHeadYRot() {
      return Integer.MAX_VALUE;
   }

   public int getMaxHeadXRot() {
      return Integer.MAX_VALUE;
   }

   public boolean shouldRender(double pX, double pY, double pZ) {
      return true;
   }

   public boolean shouldRenderAtSqrDistance(double pDistance) {
      return true;
   }

   protected AABB getBoundingBoxForPose(Pose pPose) {
      return new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   }

   public AABB getBoundingBoxForCulling() {
      return super.getBoundingBoxForCulling();
   }

   public boolean shouldBeSaved() {
      return true;
   }

   public boolean isPersistenceRequired() {
      return true;
   }

   public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
      return false;
   }

   public boolean isNoGravity() {
      return true;
   }

   public void onAddedToWorld() {
      super.onAddedToWorld();
      if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel && this.ownerUUID == null && !serverLevel.players().isEmpty()) {
         Player first = (Player)serverLevel.players().get(0);
         this.ownerUUID = first.getUUID();
         this.entityData.set(SPAWN_POS, this.position());
      }
   }

   public void remove(RemovalReason reason) {
      super.remove(reason);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      Vec3 spawn = this.getSpawnPos();
      tag.putDouble("spawnX", spawn.x());
      tag.putDouble("spawnY", spawn.y());
      tag.putDouble("spawnZ", spawn.z());
      if (this.ownerUUID != null) {
         tag.putUUID("Owner", this.ownerUUID);
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("spawnX") && tag.contains("spawnY") && tag.contains("spawnZ")) {
         this.entityData.set(SPAWN_POS, new Vec3(tag.getDouble("spawnX"), tag.getDouble("spawnY"), tag.getDouble("spawnZ")));
      }

      if (tag.hasUUID("Owner")) {
         this.ownerUUID = tag.getUUID("Owner");
      }
   }

   @Override
   public EyeGazeSystem getGazeSystem() {
      return this.eyeGaze;
   }
}
