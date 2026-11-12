package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.api.itf.IMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Mob.class})
@Implements({@Interface(
      iface = IMob.class,
      prefix = "IME$"
   )})
public abstract class MobMixin extends LivingEntity {
   @Shadow
   @Final
   public GoalSelector goalSelector;
   @Shadow
   @Final
   public GoalSelector targetSelector;

   @Shadow
   protected abstract void registerGoals();

   protected MobMixin(EntityType<? extends LivingEntity> entityType_, Level level_) {
      super(entityType_, level_);
   }

   public void IME$clearGoalsIME() {
      this.goalSelector.getAvailableGoals().clear();
      this.targetSelector.getAvailableGoals().clear();
   }

   public void IME$registerGoalsIME() {
      this.registerGoals();
   }
}
