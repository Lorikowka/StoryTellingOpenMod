package com.storyteam.storytelling.client.render.entity.processor;

import com.storyteam.storytelling.content.caps.StoryData;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.geometry.model.IGeometricModel;
import org.zeith.hammeranims.api.geometry.model.IRenderableBone;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.client.render.entity.proc.IProcessor;

public class ViewDirectionProcessor<T extends LivingEntity & IAnimatedEntity> implements IProcessor<T> {
   private final String headBoneName;
   protected BooleanSupplier doNotApplyHead;

   public ViewDirectionProcessor(String headBoneName, BooleanSupplier doNotApplyHead) {
      this.headBoneName = headBoneName;
      this.doNotApplyHead = doNotApplyHead;
   }

   public void process(T entity, IGeometricModel model, float partialTick) {
      Optional<StoryData> optional = StoryData.optionalOf(entity);
      if (optional.isPresent()) {
         StoryData data = optional.get();
         IRenderableBone bone = model.getBone(this.headBoneName);
         if (!data.lockHead.getBoolean() && bone != null) {
            if (!this.doNotApplyHead.getAsBoolean()) {
               boolean falling = entity.getFallFlyingTicks() > 4;
               boolean swimming = entity.isVisuallySwimming();
               Vector3f headRot = bone.getRotation();
               float swimAmount = entity.getSwimAmount(partialTick);
               float headPitch = -Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
               boolean shouldSit = entity.isPassenger() && entity.getVehicle() != null && entity.getVehicle().shouldRiderSit();
               float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
               float headYaw = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
               float netHeadYaw = headYaw - bodyYaw;
               if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
                  LivingEntity livingentity = (LivingEntity)entity.getVehicle();
                  bodyYaw = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
                  netHeadYaw = headYaw - bodyYaw;
                  float f3 = Mth.wrapDegrees(netHeadYaw);
                  if (f3 < -85.0F) {
                     f3 = -85.0F;
                  }

                  if (f3 >= 85.0F) {
                     f3 = 85.0F;
                  }

                  bodyYaw = headYaw - f3;
                  if (f3 * f3 > 2500.0F) {
                     bodyYaw += f3 * 0.2F;
                  }

                  netHeadYaw = headYaw - bodyYaw;
               }

               headRot.add(0.0F, (float) (Math.PI / 180.0) * netHeadYaw, 0.0F);
               if (falling) {
                  headRot.add((float) (Math.PI / 4), 0.0F, 0.0F);
               } else if (swimAmount > 0.0F) {
                  if (swimming) {
                     headRot.add(this.rotlerpRad(swimAmount, headRot.x(), (float) (Math.PI / 4)), 0.0F, 0.0F);
                  } else {
                     headRot.add(this.rotlerpRad(swimAmount, headRot.x(), (float) (Math.PI / 180.0) * headPitch), 0.0F, 0.0F);
                  }
               } else {
                  headRot.add((float) (Math.PI / 180.0) * headPitch, 0.0F, 0.0F);
               }
            }
         }
      }
   }

   private float rotlerpRad(float angleIn, float maxAngleIn, float mulIn) {
      float f = (mulIn - maxAngleIn) % (float) (Math.PI * 2);
      if (f < (float) -Math.PI) {
         f += (float) (Math.PI * 2);
      }

      if (f >= (float) Math.PI) {
         f -= (float) (Math.PI * 2);
      }

      return maxAngleIn + angleIn * f;
   }
}
