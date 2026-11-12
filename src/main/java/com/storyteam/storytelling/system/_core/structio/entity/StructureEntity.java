package com.storyteam.storytelling.system._core.structio.entity;

import com.storyteam.storytelling.api.data.AABBWithShape;
import com.storyteam.storytelling.system._core.structio.entity.client.StructuresInVRAM;
import com.storyteam.storytelling.system._core.structio.entity.data.StructureRiderInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class StructureEntity extends Mob {
   private static final EntityDataAccessor<StructureRiderInfo> RIDER_INFO = SynchedEntityData.defineId(
      StructureEntity.class, StructioContent.STRUCTURE_RIDER_INFO
   );
   private static final EntityDataAccessor<String> STRUCTURE = SynchedEntityData.defineId(StructureEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Boolean> SHOULD_GENERATE_BOX = SynchedEntityData.defineId(StructureEntity.class, EntityDataSerializers.BOOLEAN);
   private EntityDimensions cachedDims = null;

   public StructureEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   public void addRider(Entity entity, Vec3 relativePos) {
      if (!this.level().isClientSide) {
         entity.startRiding(entity, true);
         this.setRiderInfo(this.getRiderInfo().withOffset(entity, relativePos));
      }
   }

   public void tick() {
      super.tick();
      float sine = Mth.sin((float)this.tickCount / 100.0F);
      float cosine = Mth.cos((float)this.tickCount / 100.0F);
      this.getLookControl().setLookAt(this.position().add(0.0, 1.0, 1.0));
   }

   @NotNull
   protected AABB makeBoundingBox() {
      if (this.isShouldGenerateBox()) {
         VoxelShape shape = StructuresInVRAM.getComputerShape(this.getStructure());
         if (shape != null) {
            Vec3 p = this.position();
            AABBWithShape box = AABBWithShape.of(shape.move(p.x, p.y, p.z));
            this.cachedDims = new EntityDimensions((float)Math.max(box.maxX - box.minX, box.maxZ - box.minZ), (float)(box.maxY - box.minY), false);
            return box;
         }
      }

      return this.getDimensions(this.getPose()).makeBoundingBox(this.position());
   }

   public EntityDimensions getDimensions(Pose pPose) {
      return super.getDimensions(pPose);
   }

   public boolean isInvulnerableTo(@NotNull DamageSource source) {
      return !source.is(DamageTypes.FELL_OUT_OF_WORLD);
   }

   public Component getName() {
      return Component.empty();
   }

   protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
      if (this.hasPassenger(pPassenger)) {
         Vec3 p = this.position().add(this.transformOffset(this.getRiderInfo().getOffset(pPassenger)));
         pCallback.accept(pPassenger, p.x, p.y, p.z);
      }
   }

   public Vec3 transformOffset(Vec3 offset) {
      return offset.xRot(this.getXRot()).yRot(this.getYRot());
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public void push(Entity pEntity) {
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(RIDER_INFO, StructureRiderInfo.EMPTY);
      this.entityData.define(STRUCTURE, "");
      this.entityData.define(SHOULD_GENERATE_BOX, false);
   }

   public final boolean isShouldGenerateBox() {
      return (Boolean)this.entityData.get(SHOULD_GENERATE_BOX);
   }

   public final void setShouldGenerateBox(boolean shouldGenerateBox) {
      this.entityData.set(SHOULD_GENERATE_BOX, shouldGenerateBox);
   }

   public final String getStructure() {
      return (String)this.entityData.get(STRUCTURE);
   }

   public final void setStructure(String struct) {
      this.entityData.set(STRUCTURE, struct);
   }

   @NotNull
   public final StructureRiderInfo getRiderInfo() {
      return (StructureRiderInfo)this.entityData.get(RIDER_INFO);
   }

   public final void setRiderInfo(StructureRiderInfo info) {
      this.entityData.set(RIDER_INFO, info);
   }

   public boolean canCollideWith(Entity pEntity) {
      return false;
   }

   public boolean isColliding(BlockPos pPos, BlockState pState) {
      return false;
   }

   public void push(double pX, double pY, double pZ) {
   }

   public boolean isPushable() {
      return true;
   }

   protected void pushEntities() {
   }

   public void addAdditionalSaveData(CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      pCompound.put("RiderInfo", this.getRiderInfo().serializeNBT());
      pCompound.putString("Structure", this.getStructure());
      pCompound.putBoolean("ShouldGenerateBox", this.isShouldGenerateBox());
   }

   public void readAdditionalSaveData(CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      this.setRiderInfo(StructureRiderInfo.deserializeNBT(pCompound.getCompound("RiderInfo")));
      this.setStructure(pCompound.getString("Structure"));
      this.setShouldGenerateBox(pCompound.getBoolean("ShouldGenerateBox"));
   }

   public boolean shouldRender(double pX, double pY, double pZ) {
      return true;
   }
}
