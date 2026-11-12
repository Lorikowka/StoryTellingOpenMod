package com.storyteam.storytelling.mixins.system.rts;

import com.storyteam.storytelling.system.rts.init.RTSAttributesST;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public abstract class PlayerMixin extends LivingEntity {
   protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Inject(
      method = {"createAttributes"},
      at = {@At("RETURN")}
   )
   private static void createAttributes(CallbackInfoReturnable<Builder> cir) {
      Builder builder = (Builder)cir.getReturnValue();
      RTSAttributesST.ATTRIBUTES.forEach(builder::add);
   }
}
