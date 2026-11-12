package com.storyteam.storytelling.system.exp.entities;

import com.storyteam.storytelling.system.exp.ExpStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class ExpEntity extends Entity {
   private static final EntityDataAccessor<Integer> EXP_AMOUNT = SynchedEntityData.defineId(ExpEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> UNPICKABLE = SynchedEntityData.defineId(ExpEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> FIXED_AMOUNT = SynchedEntityData.defineId(ExpEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> DELAY_UNPICKABLE = SynchedEntityData.defineId(ExpEntity.class, EntityDataSerializers.INT);
   public static final int[] STAGES = new int[]{10, 50, 200, 1000, 10000};

   public ExpEntity(EntityType<? extends ExpEntity> type, Level level) {
      super(type, level);
      this.setNoGravity(true);
   }

   public int getDelayUnpickable() {
      return (Integer)this.getEntityData().get(DELAY_UNPICKABLE);
   }

   public void setDelayUnpickable(int delayUnpickable) {
      this.getEntityData().set(DELAY_UNPICKABLE, delayUnpickable);
   }

   public int getExp() {
      return (Integer)this.getEntityData().get(EXP_AMOUNT);
   }

   public void setExp(int sparks) {
      this.getEntityData().set(EXP_AMOUNT, sparks);
   }

   public boolean isUnpickable() {
      return (Boolean)this.getEntityData().get(UNPICKABLE);
   }

   public void setUnpickable(boolean unpickable) {
      this.getEntityData().set(UNPICKABLE, unpickable);
   }

   public boolean isFixedAmount() {
      return (Boolean)this.getEntityData().get(FIXED_AMOUNT);
   }

   public void setFixedAmount(boolean fixed) {
      this.getEntityData().set(FIXED_AMOUNT, fixed);
   }

   public int getStage() {
      for (int st = STAGES.length - 1; st >= 0; st--) {
         if (this.getExp() > STAGES[st]) {
            return st + 1;
         }
      }

      return 0;
   }

   public void tick() {
      super.tick();
      this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
      if (!this.level().noCollision(this.getBoundingBox())) {
         this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
      }

      if (this.tickCount >= this.getDelayUnpickable()) {
         double maxDistance = 10.0;
         Player player = this.isUnpickable() ? null : this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), maxDistance, entity -> {
            Player entry = (Player)entity;
            return !entry.isSpectator();
         });
         if (player != null && !this.level().isClientSide()) {
            this.setDeltaMovement(
               this.getDeltaMovement()
                  .add(
                     player.position()
                        .add(0.0, (double)(player.getBbHeight() / 2.0F), 0.0)
                        .subtract(this.position())
                        .normalize()
                        .scale((maxDistance - this.position().distanceTo(player.position())) / (maxDistance * 10.0))
                  )
            );
            if (this.position().distanceTo(player.position()) <= (double)(player.getBbWidth() * 1.25F)) {
               ExpStorage.addAmount(this.getExp(), this.isFixedAmount());
               this.level()
                  .playSound(
                     null,
                     this.blockPosition(),
                     SoundEvents.EXPERIENCE_ORB_PICKUP,
                     SoundSource.MASTER,
                     0.5F,
                     1.25F + this.level().getRandom().nextFloat() * 0.75F
                  );
               this.discard();
            }
         }
      }

      this.move(MoverType.SELF, this.getDeltaMovement());
      float friction = 0.98F;
      if (this.onGround()) {
         BlockPos pos = this.getBlockPosBelowThatAffectsMyMovement();
         friction = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
      }

      this.setDeltaMovement(this.getDeltaMovement().multiply((double)friction, 0.98, (double)friction));
      if (this.onGround()) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
      }

      if (this.tickCount >= 1000 && !this.isUnpickable()) {
         this.discard();
      }
   }

   protected void readAdditionalSaveData(CompoundTag tag) {
      this.setExp(tag.getInt("sparks"));
      this.setUnpickable(tag.getBoolean("unpickable"));
      this.setFixedAmount(tag.getBoolean("fixed"));
      this.setDelayUnpickable(tag.getInt("delay_unpickable"));
   }

   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putInt("sparks", this.getExp());
      tag.putBoolean("unpickable", this.isUnpickable());
      tag.putBoolean("fixed", this.isFixedAmount());
      tag.putInt("delay_unpickable", this.getDelayUnpickable());
   }

   protected void defineSynchedData() {
      this.entityData.define(EXP_AMOUNT, 0);
      this.entityData.define(UNPICKABLE, false);
      this.entityData.define(FIXED_AMOUNT, false);
      this.entityData.define(DELAY_UNPICKABLE, 15);
   }

   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
      return this.getOnPos(0.999F);
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.NONE;
   }

   public boolean isAttackable() {
      return false;
   }

   public SoundSource getSoundSource() {
      return SoundSource.AMBIENT;
   }

   public boolean isPushedByFluid(FluidType type) {
      return false;
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
      if (EXP_AMOUNT.equals(pKey)) {
         this.refreshDimensions();
      }

      super.onSyncedDataUpdated(pKey);
   }

   public EntityDimensions getDimensions(Pose pPose) {
      float scale = 0.15F + (float)this.getStage() * 0.05F;
      return EntityDimensions.scalable(scale, scale);
   }

   public boolean shouldRenderAtSqrDistance(double pDistance) {
      double d0 = this.getBoundingBox().inflate(0.2F).getSize();
      if (Double.isNaN(d0)) {
         d0 = 1.0;
      }

      d0 *= 64.0;
      return pDistance < d0 * d0;
   }
}
