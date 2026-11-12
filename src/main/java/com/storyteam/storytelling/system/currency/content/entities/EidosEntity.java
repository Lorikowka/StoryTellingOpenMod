package com.storyteam.storytelling.system.currency.content.entities;

import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidType;

public class EidosEntity extends AnimatedEntity {
   public static final int[] STAGES = new int[]{10, 50, 200, 1000, 10000};
   private static final EntityDataAccessor<Integer> AMOUNT = SynchedEntityData.defineId(EidosEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> UNPICKABLE = SynchedEntityData.defineId(EidosEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> FIXED_AMOUNT = SynchedEntityData.defineId(EidosEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> DELAY_UNPICKABLE = SynchedEntityData.defineId(EidosEntity.class, EntityDataSerializers.INT);
   private static int lifetime = 1000;

   public EidosEntity(EntityType<? extends EidosEntity> type, Level level) {
      super(type, level);
      this.setNoGravity(true);
   }

   public int getDelayUnpickable() {
      return (Integer)this.getEntityData().get(DELAY_UNPICKABLE);
   }

   public void setDelayUnpickable(int delayUnpickable) {
      this.getEntityData().set(DELAY_UNPICKABLE, delayUnpickable);
   }

   public int getAmount() {
      return (Integer)this.getEntityData().get(AMOUNT);
   }

   public void setAmount(int amount) {
      this.getEntityData().set(AMOUNT, amount);
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
         if (this.getAmount() > STAGES[st]) {
            return st + 1;
         }
      }

      return 0;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         this.spawnTrailParticles();
      }

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
               CurrencyStorage.addEidos(this.getAmount());
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

      if (this.tickCount >= lifetime && !this.isUnpickable()) {
         this.discard();
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void spawnTrailParticles() {
      if (this.tickCount % 4 == 0) {
         int[] colors = new int[]{65535, 16711935, 16776960, 4835660, 255, 16711680};
         int count = 2;

         for (int i = 0; i < count; i++) {
            int color = colors[this.random.nextInt(colors.length)];
            double px = this.getX();
            double py = this.getY() + (double)this.getBbHeight() / 2.0 + 0.1F;
            double pz = this.getZ();
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double speed = 0.005;
            double speedX = Math.cos(angle) * speed;
            double speedZ = Math.sin(angle) * speed;
            double speedY = (this.random.nextDouble() - 0.5) * 0.01;
            CircleTintData circleTintData = new CircleTintData(new Color(color), 0.03F + this.random.nextFloat() * 0.02F, 80, -1.0F, false, false);
            ParticleHelper.spawnDirectedParticle(this.level(), circleTintData, px, py, pz, speedX, speedY, speedZ);
         }
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.setAmount(tag.getInt("amount"));
      this.setUnpickable(tag.getBoolean("unpickable"));
      this.setFixedAmount(tag.getBoolean("fixed"));
      this.setDelayUnpickable(tag.getInt("delay_unpickable"));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("amount", this.getAmount());
      tag.putBoolean("unpickable", this.isUnpickable());
      tag.putBoolean("fixed", this.isFixedAmount());
      tag.putInt("delay_unpickable", this.getDelayUnpickable());
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(AMOUNT, 0);
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
      if (AMOUNT.equals(pKey)) {
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

   @Generated
   public static int getLifetime() {
      return lifetime;
   }

   @Generated
   public static void setLifetime(int lifetime) {
      EidosEntity.lifetime = lifetime;
   }
}
