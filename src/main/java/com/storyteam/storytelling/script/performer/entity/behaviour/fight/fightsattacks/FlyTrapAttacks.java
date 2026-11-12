package com.storyteam.storytelling.script.performer.entity.behaviour.fight.fightsattacks;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.system.fight.AttackAnimActionBuilder;
import com.storyteam.storytelling.system.fight.Collider;
import com.storyteam.storytelling.util.TargetSelectorHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animation.IAnimationSource;

public class FlyTrapAttacks {
   public static <T extends BaseMob, U extends LivingEntity> Attack<T> flyTrap1PunchAttack(
      int cooldown, TargetSelector<T, U> targetSelector, float distanceAttack, float damage
   ) {
      return Attack.<T>builder(
            "flyTrap1PunchAttack",
            cooldown,
            (manager, context) -> {
               LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
               if (e == null) {
                  return true;
               } else {
                  manager.tick();
                  manager.addFlag("default_flags$ATTACK");
                  manager.doOnce(
                     "fly_trap_punch_attack",
                     () -> {
                        context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                        IAnimationSource punchAnim = AnimationsST8.FLYTRAP_ATTACK;
                        String punchBone = "JawUp";
                        ((BaseMob)context.getEntity()).startAnimation("ANIMATION_9", punchAnim);
                        context.getState().getData().setLookTarget(new EntityTarget(e));
                        ((BaseMob)context.getEntity())
                           .setAttackAnimAction(
                              new AttackAnimActionBuilder(punchAnim.configure(), punchBone, (AnimatedEntityLikeHumanoid)context.getEntity())
                                 .withInflateV3(new Vec3(2.0, 2.0, 2.0))
                                 .build()
                           );
                     }
                  );
                  ((BaseMob)context.getEntity()).lookAt(Anchor.EYES, e.position());
                  ((BaseMob)context.getEntity()).lookAt(Anchor.FEET, e.position());
                  if (((BaseMob)context.getEntity()).getAttackAnimAction() != null && ((BaseMob)context.getEntity()).getAttackAnimAction().is_active()) {
                     if (((BaseMob)context.getEntity()).getAttackAnimAction().getTime_anim() >= 6.0F
                        && ((BaseMob)context.getEntity()).getAttackAnimAction().getTime_anim() < 18.0F
                        && !manager.contains("Damage")
                        && Collider.isCollidingBox(context.getEntity(), e)) {
                        DamageSource damageSource = new DamageSource(
                           ((BaseMob)context.getEntity()).level().damageSources().mobAttack((LivingEntity)context.getEntity()).typeHolder(),
                           context.getEntity(),
                           context.getEntity(),
                           e.position()
                        );
                        e.hurt(damageSource, damage);
                        Vec3 vec3 = e.position().subtract(((BaseMob)context.getEntity()).position()).normalize().scale(0.6);
                        e.setDeltaMovement(new Vec3(vec3.x(), 0.2F, vec3.z()));
                        manager.addFlag("Damage");
                     }

                     return false;
                  } else {
                     context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                     ((BaseMob)context.getEntity()).stopAnimation("ANIMATION_9");
                     manager.remove("default_flags$ATTACK");
                     return true;
                  }
               }
            }
         )
         .addCondition(context -> TargetSelectorHelper.hasBetween(context, targetSelector, (double)distanceAttack, 0.0))
         .build();
   }
}
