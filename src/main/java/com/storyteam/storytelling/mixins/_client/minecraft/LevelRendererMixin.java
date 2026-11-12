package com.storyteam.storytelling.mixins._client.minecraft;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.system._core.structio.entity.client.StructuresInVRAM;
import com.storyteam.storytelling.util.Necr0manthUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;

@Mixin({LevelRenderer.class})
public abstract class LevelRendererMixin {
   @Shadow
   protected abstract void renderEntity(Entity var1, double var2, double var4, double var6, float var8, PoseStack var9, MultiBufferSource var10);

   @Shadow
   @Nullable
   public abstract RenderTarget entityTarget();

   @Inject(
      method = {"allChanged"},
      at = {@At("HEAD")}
   )
   private void ST_allChanged(CallbackInfo ci) {
      StructuresInVRAM.reset();
   }

   @Inject(
      method = {"renderEntity"},
      at = {@At("HEAD")}
   )
   private void renderEntity(
      Entity e, double pCamX, double pCamY, double pCamZ, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, CallbackInfo ci
   ) {
      if (Necr0manthUtils.locations.containsKey(e)) {
         for (Entry<String, ConfiguredAnimation> anim : Necr0manthUtils.anims.get(e).entrySet()) {
            ((IAnimatedEntity)e).getAnimationSystem().startAnimationAt(anim.getKey(), anim.getValue());
         }

         Location location = Necr0manthUtils.locations.get(e);
         e.setPos(location);
         e.xo = e.getX();
         e.yo = e.getY();
         e.zo = e.getZ();
         e.xOld = e.getX();
         e.yOld = e.getY();
         e.zOld = e.getZ();
         e.lerpTo(e.getX(), e.getY(), e.getZ(), e.getYRot(), e.getXRot(), 0, false);
         e.setYRot(location.getYaw());
         e.yRotO = location.getYaw();
         e.setYBodyRot(location.getYaw());
         e.setXRot(location.getPitch());
         e.xRotO = location.getPitch();
         if (e instanceof HumanoidEntity he) {
            he.yBody = he.yBodyRot = he.yBodyRotO = location.getYaw();
            he.yHeadRot = he.yHeadRotO = location.getYaw();
         }

         Necr0manthUtils.noMoveTime.put(e, Necr0manthUtils.noMoveTime.get(e) - pPartialTick * 0.05F);
         if (Necr0manthUtils.noMoveTime.get(e) < 0.0F) {
            Necr0manthUtils.noMoveTime.remove(e);
            Necr0manthUtils.locations.remove(e);
            Necr0manthUtils.anims.remove(e);
         }

         ((HumanoidEntity)e).recalcEyeHeight();
      }
   }
}
