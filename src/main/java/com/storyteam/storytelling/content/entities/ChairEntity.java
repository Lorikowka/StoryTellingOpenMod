package com.storyteam.storytelling.content.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class ChairEntity extends Entity {
   public ChairEntity(EntityType<? extends Entity> entityType, Level level) {
      super(entityType, level);
   }

   public boolean isAlwaysTicking() {
      return true;
   }

   protected void defineSynchedData() {
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         if (this.getY() < -64.0) {
            this.kill();
         }

         if (this.getPassengers().isEmpty()) {
            this.kill();
         }

         if (!this.isAlive()) {
            this.ejectPassengers();
         }
      }
   }

   public boolean canCollideWith(@NotNull Entity pEntity) {
      return false;
   }

   public boolean isControlledByLocalInstance() {
      return false;
   }

   public boolean hurt(@NotNull DamageSource source, float amount) {
      return super.hurt(source, amount);
   }

   public Iterable<ItemStack> getArmorSlots() {
      return null;
   }

   public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
   }

   public void push(double x, double y, double z) {
      this.discard();
   }

   public PushReaction getPistonPushReaction() {
      return PushReaction.IGNORE;
   }

   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
   }

   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
