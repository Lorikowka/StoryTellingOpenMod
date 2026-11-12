package com.storyteam.storytelling.content.entities;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammeranims.api.geometry.model.IPositionalModel;
import org.zeith.hammerlib.util.java.Cast;

public class AnimatedItemEntity extends AnimatedEntityNoCollision {
   protected final Matrix4f tfmat = new Matrix4f();
   protected final Matrix4d tfmatd = new Matrix4d();

   public AnimatedItemEntity(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
   }

   public Optional<Matrix4d> getTransforms(String bone, boolean useBodyRotation) {
      StoryEntityType<AnimatedItemEntity> stt = (StoryEntityType<AnimatedItemEntity>)Cast.cast(this.getType());
      if (stt == null) {
         return Optional.empty();
      } else {
         IGeometryContainer model = stt.properties.model.apply(this);
         return this.getTransforms(model, bone, useBodyRotation);
      }
   }

   public Optional<Matrix4d> getTransforms(IGeometryContainer model, String bone, boolean useBodyRotation) {
      AnimationSystem asys = this.getAnimationSystem();
      if (model != null && asys != null) {
         IPositionalModel pmod = model.getPositionalModel();
         pmod.applySystem(1.0F, asys, layer -> !Objects.equals(layer.activeAnimationLocation(), this.getIdleAnimation().getLocation()));
         Vec3 pos = this.position();
         if (pmod.applyBoneTransforms(this.tfmat.identity(), bone)) {
            float scale = 1.0F;
            if (this.getType() instanceof StoryEntityType<?> stt) {
               scale = stt.getProperties().scale;
            }

            this.tfmatd
               .identity()
               .translate((double)((float)pos.x), (double)((float)pos.y), (double)((float)pos.z))
               .rotateY((double)((float) (Math.PI / 180.0) * (180.0F - (useBodyRotation ? this.yBodyRot : this.yHeadRot))))
               .scale((double)scale)
               .mul(this.tfmat);
            return Optional.of(this.tfmatd);
         } else {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }
}
