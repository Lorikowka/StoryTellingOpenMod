package com.storyteam.storytelling.content.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class STCameraEntity extends Entity {
   public STCameraEntity(EntityType<?> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.noPhysics = true;
   }

   public void tick() {
      super.tick();
   }

   public void setPos(double x, double y, double z) {
      this.xo = this.getX();
      this.yo = this.getY();
      this.zo = this.getZ();
      super.setPos(x, y, z);
   }

   protected float getEyeHeight(Pose pPose, EntityDimensions pDimensions) {
      return Minecraft.getInstance().player.getEyeHeight();
   }

   public double getEyeY() {
      return Minecraft.getInstance().player.getEyeY();
   }

   protected void defineSynchedData() {
   }

   protected void readAdditionalSaveData(CompoundTag pCompound) {
   }

   protected void addAdditionalSaveData(CompoundTag pCompound) {
   }

   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   public boolean fireImmune() {
      return true;
   }

   public void push(Entity pEntity) {
   }

   public void push(double pX, double pY, double pZ) {
   }
}
