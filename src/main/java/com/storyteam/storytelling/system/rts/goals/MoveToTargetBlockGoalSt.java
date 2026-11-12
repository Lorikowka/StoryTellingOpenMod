package com.storyteam.storytelling.system.rts.goals;

import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import net.minecraft.world.entity.Mob;

public class MoveToTargetBlockGoalSt extends MoveToTargetBlockGoal {
   public MoveToTargetBlockGoalSt(Mob mob, boolean persistent, int reachRange) {
      super(mob, persistent, reachRange);
   }
}
