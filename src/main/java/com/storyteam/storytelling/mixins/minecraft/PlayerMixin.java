package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.CheckHurtEvent;
import com.storyteam.storytelling.events.base.DismountEvent;
import com.storyteam.storytelling.init.AttributesST;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.java.Cast;

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
      builder.add(AttributesST.LEVELING_VIGOR);
      builder.add(AttributesST.LEVELING_ENDURANCE);
      builder.add(AttributesST.LEVELING_AGILITY);
      builder.add(AttributesST.LEVELING_INTELLIGENCE);
      builder.add(AttributesST.LEVELING_STRENGTH);
      builder.add(AttributesST.LEVELING_LUCK);
      builder.add(AttributesST.MAX_STAMINA);
      builder.add(AttributesST.MAX_MANA);
      builder.add(AttributesST.STAMINA_REGEN);
      builder.add(AttributesST.POISE);
      builder.add(AttributesST.AFFINITY);
   }

   @Inject(
      method = {"getHurtSound"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getHurtSound(DamageSource pSource, CallbackInfoReturnable<SoundEvent> cir) {
      if (pSource != null && Storytelling.BUS.post(new CheckHurtEvent((LivingEntity)Cast.cast(this), CheckHurtEvent.TestCase.HURT_SOUND))) {
         cir.setReturnValue(null);
         cir.cancel();
      }
   }

   @Inject(
      method = {"wantsToStopRiding"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void wantsToDismount(CallbackInfoReturnable<Boolean> cir) {
      if (MinecraftForge.EVENT_BUS.post(new DismountEvent((Player)(Object)this))) {
         cir.setReturnValue(false);
      }
   }
}
