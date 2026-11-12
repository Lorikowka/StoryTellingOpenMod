package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.init.ItemsST;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.java.Cast;

@Mixin({ItemFrame.class})
public abstract class ItemFrameMixin extends HangingEntity {
   @Shadow
   private boolean fixed;

   protected ItemFrameMixin(EntityType<? extends HangingEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Shadow
   protected abstract void dropItem(Entity var1, boolean var2);

   @Inject(
      method = {"hurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
      if (pSource.getEntity() instanceof Player p && !p.isCreative()) {
         cir.setReturnValue(false);
         cir.cancel();
         return;
      }

      if (!this.fixed && !this.level().isClientSide()) {
         ItemFrame frame = (ItemFrame)Cast.cast(this);
         if (frame.getItem().isEmpty()) {
            frame.discard();
         } else {
            this.dropItem(pSource.getEntity(), false);
            frame.discard();
         }
      } else {
         cir.setReturnValue(false);
         cir.cancel();
      }
   }

   @Inject(
      method = {"interact"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void interact(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
      if (pPlayer.isCreative() && pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).is(ItemsST.STORYTELLING)) {
         this.fixed = !this.fixed;
         cir.setReturnValue(InteractionResult.PASS);
         cir.cancel();
      }
   }
}
