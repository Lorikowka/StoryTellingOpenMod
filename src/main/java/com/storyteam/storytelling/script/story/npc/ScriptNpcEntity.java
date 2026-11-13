package com.storyteam.storytelling.script.story.npc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.level.Level;

/** An ordinary pathfinding mob whose appearance is selected by the Lua story. */
public final class ScriptNpcEntity extends PathfinderMob {
   private static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<String> ANIMATION_MODE = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Boolean> WALKING = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> HITBOX_WIDTH = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> HITBOX_HEIGHT = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<String> LOOK_PLAYER = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Boolean> LOOK_POINT = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> LOOK_X = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> LOOK_Y = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> LOOK_Z = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> LOOK_YAW = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> LOOK_PITCH = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<String> LOOK_MODE = SynchedEntityData.defineId(ScriptNpcEntity.class, EntityDataSerializers.STRING);
   public static final float MAX_HEAD_YAW = 60f;
   private boolean turningBody;

   public ScriptNpcEntity(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   @Override
   protected BodyRotationControl createBodyControl() {
      return new BodyRotationControl(this) {
         @Override public void clientTick() {
            if (entityData.get(LOOK_PLAYER).isEmpty() && !entityData.get(LOOK_POINT)) super.clientTick();
         }
      };
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
      entityData.define(LOOK_PLAYER, "");
      entityData.define(LOOK_POINT, false);
      entityData.define(LOOK_X, 0f);
      entityData.define(LOOK_Y, 0f);
      entityData.define(LOOK_Z, 0f);
      entityData.define(LOOK_YAW, 0f);
      entityData.define(LOOK_PITCH, 0f);
      entityData.define(LOOK_MODE, "auto");
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

   public void lookAt(double x, double y, double z) {
      if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
         || Math.abs(x) > 30000000 || Math.abs(y) > 30000000 || Math.abs(z) > 30000000) {
         throw new IllegalArgumentException("Invalid NPC look coordinates");
      }
      entityData.set(LOOK_PLAYER, "");
      entityData.set(LOOK_X, (float) x);
      entityData.set(LOOK_Y, (float) y);
      entityData.set(LOOK_Z, (float) z);
      entityData.set(LOOK_POINT, true);
   }

   public void lookAt(ServerPlayer player) {
      entityData.set(LOOK_POINT, false);
      entityData.set(LOOK_PLAYER, player.getUUID().toString());
   }

   public void clearLook() {
      entityData.set(LOOK_PLAYER, "");
      entityData.set(LOOK_POINT, false);
      entityData.set(LOOK_YAW, 0f);
      entityData.set(LOOK_PITCH, 0f);
      setYHeadRot(yBodyRot);
      turningBody = false;
   }

   public void lookMode(String mode) {
      if (!mode.equals("auto") && !mode.equals("head") && !mode.equals("body")) {
         throw new IllegalArgumentException("NPC look mode must be auto, head or body");
      }
      entityData.set(LOOK_MODE, mode);
      turningBody = false;
   }

   public float lookYaw() { return entityData.get(LOOK_YAW); }
   public float lookPitch() { return entityData.get(LOOK_PITCH); }

   public Vec3 lookTarget() {
      String playerId = entityData.get(LOOK_PLAYER);
      if (!playerId.isEmpty()) {
         try {
            Player player = level().getPlayerByUUID(UUID.fromString(playerId));
            return player == null ? null : player.getEyePosition();
         } catch (IllegalArgumentException ignored) {
            return null;
         }
      }
      return entityData.get(LOOK_POINT) ? new Vec3(entityData.get(LOOK_X), entityData.get(LOOK_Y), entityData.get(LOOK_Z)) : null;
   }

   @Override
   public void tick() {
      Vec3 target = level().isClientSide ? null : lookTarget();
      if (target != null) {
         getLookControl().setLookAt(target.x, target.y, target.z, 30f, 40f);
      }
      super.tick();
      if (level().isClientSide) return;
      if (target == null) return;
      double dx = target.x - getX();
      double dy = target.y - getEyeY();
      double dz = target.z - getZ();
      if (dx * dx + dz * dz < 0.0001) return;
      float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90f;
      String mode = entityData.get(LOOK_MODE);
      float difference = Mth.wrapDegrees(targetYaw - getYRot());
      if (mode.equals("auto") && Math.abs(difference) > MAX_HEAD_YAW) turningBody = true;
      if (mode.equals("body") || (mode.equals("auto") && turningBody)) {
         float turn = Mth.clamp(difference, -5f, 5f);
         float nextYaw = getYRot() + turn;
         setYRot(nextYaw);
         yBodyRot = nextYaw;
         yBodyRotO = nextYaw;
         if (Math.abs(Mth.wrapDegrees(targetYaw - nextYaw)) < 3f) turningBody = false;
      }
      float headYaw = mode.equals("body") ? 0f : Mth.clamp(Mth.wrapDegrees(targetYaw - getYRot()), -MAX_HEAD_YAW, MAX_HEAD_YAW);
      float headPitch = mode.equals("body") ? 0f : Mth.clamp((float) Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))), -40f, 40f);
      entityData.set(LOOK_YAW, headYaw);
      entityData.set(LOOK_PITCH, headPitch);
      setYHeadRot(getYRot() + headYaw);
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
      tag.putString("StoryLookPlayer", entityData.get(LOOK_PLAYER));
      tag.putBoolean("StoryLookPoint", entityData.get(LOOK_POINT));
      tag.putFloat("StoryLookX", entityData.get(LOOK_X));
      tag.putFloat("StoryLookY", entityData.get(LOOK_Y));
      tag.putFloat("StoryLookZ", entityData.get(LOOK_Z));
      tag.putString("StoryLookMode", entityData.get(LOOK_MODE));
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
      entityData.set(LOOK_PLAYER, tag.getString("StoryLookPlayer"));
      entityData.set(LOOK_POINT, tag.getBoolean("StoryLookPoint"));
      entityData.set(LOOK_X, tag.getFloat("StoryLookX"));
      entityData.set(LOOK_Y, tag.getFloat("StoryLookY"));
      entityData.set(LOOK_Z, tag.getFloat("StoryLookZ"));
      if (tag.contains("StoryLookMode")) lookMode(tag.getString("StoryLookMode"));
   }
}
