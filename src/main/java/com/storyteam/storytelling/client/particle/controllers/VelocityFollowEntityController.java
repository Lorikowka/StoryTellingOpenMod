package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VelocityFollowEntityController implements ControllerData, ControllerType<VelocityFollowEntityController> {
   private Entity entity = null;
   private int entityId = -1;
   private float multiplier = 1.0F;
   private Vec3 startMove = Vec3.ZERO;

   @Nonnull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.VELOCITY_FOLLOW_ENTITY_CONTROLLER;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeFloat(this.multiplier);
   }

   public VelocityFollowEntityController fromNetwork(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.multiplier = buf.readFloat();
      return this;
   }

   public VelocityFollowEntityController entityId(int id) {
      this.entityId = id;
      return this;
   }

   public VelocityFollowEntityController multiplier(float multiplier) {
      this.multiplier = multiplier;
      return this;
   }

   public void resolveEntity(Level level, VelocityFollowEntityController data) {
      if (data.entity == null && data.entityId >= 0) {
         data.entity = level.getEntity(data.entityId);
      }
   }

   @Nonnull
   public Vec3 handleMovement(ParticleInfo info, VelocityFollowEntityController data) {
      this.resolveEntity(info.level(), data);
      if (data.entity == null) {
         return info.movement();
      } else {
         Vec3 moveEntity = data.entity.position().subtract(data.entity.xOld, data.entity.yOld, data.entity.zOld);
         if (data.entity.onGround()) {
            moveEntity = new Vec3(moveEntity.x, 0.0, moveEntity.z);
         }

         return moveEntity.equals(Vec3.ZERO) ? info.movement() : moveEntity.scale((double)data.multiplier);
      }
   }

   @Override
   public String writeToString() {
      return String.format("%d%f", this.entityId, this.multiplier);
   }

   public VelocityFollowEntityController fromCommand(StringReader pReader) throws CommandSyntaxException {
      this.entityId = pReader.readInt();
      this.multiplier = pReader.readFloat();
      return this;
   }
}
