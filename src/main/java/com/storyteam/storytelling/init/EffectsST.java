package com.storyteam.storytelling.init;

import com.storyteam.storytelling.content.effects.SimpleMobEffect;
import com.storyteam.storytelling.util.Color;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface EffectsST {
   @RegistryName("earthquake")
   MobEffect EARTHQUAKE = new SimpleMobEffect(MobEffectCategory.HARMFUL, -1206189);
   @RegistryName("camera_shake")
   MobEffect CAMERA_SHAKE = new SimpleMobEffect(MobEffectCategory.NEUTRAL, -1206189);
   @RegistryName("timer")
   MobEffect TIMER = new SimpleMobEffect(MobEffectCategory.HARMFUL, -1206189);
   @RegistryName("dying_spark")
   MobEffect DYING_SPARK = new SimpleMobEffect(MobEffectCategory.NEUTRAL, -16777216);
   @RegistryName("spark_anomaly")
   MobEffect SPARK_ANOMALY = new SimpleMobEffect(MobEffectCategory.NEUTRAL, -16777216);
   @RegistryName("spell_enhancement")
   MobEffect SPELL_ENHANCEMENT = new SimpleMobEffect(MobEffectCategory.BENEFICIAL, new Color(65, 127, 209).getRGB());
   @RegistryName("hunger_slow")
   MobEffect HUNGER_SLOW = new SimpleMobEffect(MobEffectCategory.HARMFUL, -16777216)
      .addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15F, Operation.MULTIPLY_TOTAL);
   @RegistryName("screen_blur")
   MobEffect SCREEN_BLUR = new SimpleMobEffect(MobEffectCategory.HARMFUL, -16777216);
   @RegistryName("immunity")
   MobEffect IMMUNITY = new SimpleMobEffect(MobEffectCategory.BENEFICIAL, -15022852);
   @RegistryName("space_slow")
   MobEffect SPACE_SLOW = new SimpleMobEffect(MobEffectCategory.HARMFUL, -16777216)
      .addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15F, Operation.MULTIPLY_TOTAL);
}
