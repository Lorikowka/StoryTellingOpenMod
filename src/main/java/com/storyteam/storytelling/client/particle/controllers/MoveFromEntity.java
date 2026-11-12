package com.storyteam.storytelling.client.particle.controllers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.client.particle.ParticleInfo;
import com.storyteam.storytelling.init.ParticleTypesST;
import java.util.Random;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MoveFromEntity implements ControllerData, ControllerType<MoveFromEntity> {
   @NotNull
   @Override
   public <T extends ControllerData> ControllerType<T> getType() {
      return (ControllerType<T>)ParticleTypesST.ParticleBehaviorRegistry.MOVE_FROM_ENTITY;
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf pBuffer) {
   }

   @Override
   public String writeToString() {
      return "";
   }

   @NotNull
   public Vec3 handleMovement(ParticleInfo info, MoveFromEntity data) {
      for (LivingEntity e : info.level()
         .getEntitiesOfClass(
            LivingEntity.class,
            new AABB(
               info.pos().x,
               info.pos().y,
               info.pos().z,
               info.pos().x + (double)(info.width() * 2.0F),
               info.pos().y + (double)(info.height() * 2.0F),
               info.pos().z + (double)(info.width() * 2.0F)
            )
         )) {
         if (e != null && this.isMoving(e)) {
            Vec3 particlePos = new Vec3(info.pos().x, info.pos().y, info.pos().z);
            Vec3 entityPos = e.position();
            Vec3 direction = particlePos.subtract(entityPos.add(0.0, -0.1, 0.0)).normalize();
            Random rnd = new Random();
            double randomFactor = (double)rnd.nextFloat(0.005F, 0.03F);
            return info.movement().scale(0.5).add(direction.scale(randomFactor));
         }
      }

      return info.movement();
   }

   private boolean isMoving(LivingEntity mob) {
      double d0 = mob.getX() - mob.xo;
      double d1 = mob.getZ() - mob.zo;
      return d0 * d0 + d1 * d1 > 2.5000003E-7F;
   }

   public MoveFromEntity fromCommand(StringReader pReader) throws CommandSyntaxException {
      return this;
   }

   public MoveFromEntity fromNetwork(FriendlyByteBuf pBuffer) {
      return this;
   }
}
