package com.storyteam.storytelling.mixins.minecraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {HangingEntity.class},
   priority = 1010
)
public abstract class HangingEntityMixin extends Entity implements IStoryEntity {
   public HangingEntityMixin(EntityType<?> pType, Level pLevel) {
      super(pType, pLevel);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tick(CallbackInfo ci) {
      this.storytellingTick();
   }

   @WrapOperation(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/decoration/HangingEntity;dropItem(Lnet/minecraft/world/entity/Entity;)V"
      )}
   )
   public void tickDropItem(HangingEntity instance, Entity entity, Operation<Void> original) {
      if (!(instance instanceof ItemFrame)) {
         original.call(new Object[]{instance, entity});
      }
   }

   @WrapOperation(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/decoration/HangingEntity;dropItem(Lnet/minecraft/world/entity/Entity;)V"
      )}
   )
   public void moveDropItem(HangingEntity instance, Entity entity, Operation<Void> original) {
      if (!(instance instanceof ItemFrame)) {
         original.call(new Object[]{instance, entity});
      }
   }

   @WrapOperation(
      method = {"push"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/decoration/HangingEntity;dropItem(Lnet/minecraft/world/entity/Entity;)V"
      )}
   )
   public void pushDropItem(HangingEntity instance, Entity entity, Operation<Void> original) {
      if (!(instance instanceof ItemFrame)) {
         original.call(new Object[]{instance, entity});
      }
   }

   @Inject(
      method = {"survives"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void survivesST(CallbackInfoReturnable<Boolean> cir) {
      if (this.isStorytellingEntity()) {
         cir.setReturnValue(true);
      }
   }
}
