package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Mob.class})
public abstract class MobMixin2 extends EntityMixin {
   @Shadow
   @Nullable
   public abstract LivingEntity getTarget();

   @Inject(
      method = {"shouldDespawnInPeaceful"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void shouldDespawnInPeacefulRedirect(CallbackInfoReturnable<Boolean> cir) {
      if (IStoryEntity.of(this.getTarget()) != null
         && (IStoryEntity.of(this.getTarget()).isStorytellingEntity() || IStoryEntity.of(this.getTarget()).isUndespawnable())) {
         cir.setReturnValue(true);
      }
   }
}
