package com.storyteam.storytelling.mixins.system._core.morph;

import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({IForgeLivingEntity.class})
public interface IForgeLivingEntityMixin {
   @Shadow
   LivingEntity self();

   @Overwrite(
      remap = false
   )
   default void jumpInFluid(FluidType type) {
      if (this.self() instanceof Player pl && ((IAnimatedPlayer)pl).getMorph().equals(MorphsST8.SPOODER) && !pl.isCreative() && !pl.isSpectator()) {
         return;
      }

      this.self().setDeltaMovement(this.self().getDeltaMovement().add(0.0, 0.04F * this.self().getAttributeValue((Attribute)ForgeMod.SWIM_SPEED.get()), 0.0));
   }
}
