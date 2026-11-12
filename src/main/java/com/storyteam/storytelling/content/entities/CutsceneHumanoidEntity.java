package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.mixins.minecraft.EntityAccessor;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.util.java.Cast;

public class CutsceneHumanoidEntity extends HumanoidEntity {
   public CutsceneHumanoidEntity(EntityType<? extends HumanoidEntity> type, Level world) {
      super(type, world);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   public void recalcEyeHeight() {
      if (this.isAlive() && (this.level().isClientSide() || ((ServerLevel)this.level()).isPositionEntityTicking(this.blockPosition()))) {
         if (this.tickCount % 5 == 0 && this.getType() instanceof StoryEntityType<?> stt) {
            IGeometryContainer model = ((Function<Object, IGeometryContainer>)stt.getProperties().model).apply((Entity)Cast.cast(this));
            Matrix4d tfm;
            if (model != null && (tfm = this.getTransforms(model, "camera", false).orElse(null)) != null) {
               this.eyeHeight = (float)((this.eyePos = tfm.transformPosition(new Vector3d(0.0, 0.125, 0.0))).y - this.position().y);
               ((EntityAccessor)this).setEyeHeight(this.eyeHeight);
            }
         } else {
            this.eyeHeight = Float.NEGATIVE_INFINITY;
         }
      }
   }

   @Override
   public boolean canCollideWith(@NotNull Entity entity) {
      return false;
   }

   public boolean isColliding(BlockPos pPos, BlockState pState) {
      return false;
   }

   public void push(Entity pEntity) {
   }

   public void push(double pX, double pY, double pZ) {
   }

   public boolean isPushable() {
      return true;
   }

   protected void pushEntities() {
   }
}
