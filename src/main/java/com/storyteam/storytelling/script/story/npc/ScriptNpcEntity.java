package com.storyteam.storytelling.script.story.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/** An ordinary pathfinding mob whose appearance is selected by the Lua story. */
public final class ScriptNpcEntity extends PathfinderMob {
   private static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> ANIMATION_MODE = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Boolean> WALKING = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> HITBOX_WIDTH = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> HITBOX_HEIGHT = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);

   public ScriptNpcEntity(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      entityData.define(MODEL, "");
      entityData.define(ANIMATION, "auto");
      entityData.define(ANIMATION_MODE, "loop");
      entityData.define(WALKING, false);
      entityData.define(HITBOX_WIDTH, 0.6f);
      entityData.define(HITBOX_HEIGHT, 1.8f);
   }

   public String model() {
      return entityData.get(MODEL);
   }

   public void model(String model) {
      entityData.set(MODEL, model);
   }

   public String animation() {
      return entityData.get(ANIMATION);
   }

   public void animation(String animation) {
      animation(animation, "loop");
   }

   public String animationMode() {
      return entityData.get(ANIMATION_MODE);
   }

   public void animation(String animation, String mode) {
      entityData.set(ANIMATION, animation);
      entityData.set(ANIMATION_MODE, mode);
   }

   public boolean walking() {
      return entityData.get(WALKING);
   }

   public void walking(boolean walking) {
      entityData.set(WALKING, walking);
   }

   public void hitbox(float width, float height) {
      if (!Float.isFinite(width) || !Float.isFinite(height) || width < 0.1f || width > 8f || height < 0.1f || height > 16f) {
         throw new IllegalArgumentException("NPC hitbox must have width 0.1–8 and height 0.1–16 blocks");
      }
      entityData.set(HITBOX_WIDTH, width);
      entityData.set(HITBOX_HEIGHT, height);
      refreshDimensions();
   }

   @Override
   public EntityDimensions getDimensions(Pose pose) {
      return EntityDimensions.scalable(entityData.get(HITBOX_WIDTH), entityData.get(HITBOX_HEIGHT));
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (HITBOX_WIDTH.equals(accessor) || HITBOX_HEIGHT.equals(accessor)) refreshDimensions();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putString("StoryModel", model());
      tag.putString("StoryAnimation", animation());
      tag.putString("StoryAnimationMode", animationMode());
      tag.putFloat("StoryHitboxWidth", entityData.get(HITBOX_WIDTH));
      tag.putFloat("StoryHitboxHeight", entityData.get(HITBOX_HEIGHT));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      model(tag.getString("StoryModel"));
      if (tag.contains("StoryAnimation")) animation(tag.getString("StoryAnimation"));
      if (tag.contains("StoryAnimationMode")) animation(animation(), tag.getString("StoryAnimationMode"));
      if (tag.contains("StoryHitboxWidth") && tag.contains("StoryHitboxHeight")) {
         hitbox(tag.getFloat("StoryHitboxWidth"), tag.getFloat("StoryHitboxHeight"));
      }
   }
}
