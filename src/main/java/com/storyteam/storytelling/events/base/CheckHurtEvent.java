package com.storyteam.storytelling.events.base;

import lombok.Generated;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Cancelable
public class CheckHurtEvent extends LivingEvent {
   private final CheckHurtEvent.TestCase testCase;

   public CheckHurtEvent(LivingEntity entity, CheckHurtEvent.TestCase testCase) {
      super(entity);
      this.testCase = testCase;
   }

   @Generated
   public CheckHurtEvent.TestCase getTestCase() {
      return this.testCase;
   }

   @EventBusSubscriber
   public static class Defaults {
      @SubscribeEvent
      public static void noBobDamageSource(CheckHurtEvent e) {
         if (noBobDamageSource(e.getEntity().getLastDamageSource())) {
            e.setCanceled(true);
         }
      }

      public static boolean noBobDamageSource(DamageSource source) {
         if (source == null) {
            return false;
         } else {
            try {
               return source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.LAVA) || "poison".equals(source.type().msgId());
            } catch (NoSuchFieldError var2) {
               return source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.LAVA);
            }
         }
      }
   }

   public static enum TestCase {
      BOB_HURT,
      HURT_OVERLAY,
      HURT_SOUND;
   }
}
