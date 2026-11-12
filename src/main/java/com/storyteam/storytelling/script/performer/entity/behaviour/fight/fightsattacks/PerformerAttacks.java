package com.storyteam.storytelling.script.performer.entity.behaviour.fight.fightsattacks;

import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.util.TargetSelectorHelper;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;

public class PerformerAttacks {
   public static <T extends BaseMob, U extends LivingEntity> Attack<T> test(
      int cooldown, TargetSelector<T, U> targetSelector, Function<U, ScriptPerformer> performer
   ) {
      AtomicReference<ScriptPerformer> reference = new AtomicReference<>();
      return Attack.<T>builder("test", cooldown, (manager, context) -> {
         U e = TargetSelectorHelper.nearest(context, targetSelector);
         if (reference.get() == null) {
            reference.set(performer.apply(e));
         }

         reference.get().tick();
         if (reference.get().isCompleted()) {
            System.out.println(reference.get().isCompleted() + " " + reference.get().getActions());
            System.out.println("КОНЕЦ");
            return true;
         } else {
            return false;
         }
      }).build();
   }
}
