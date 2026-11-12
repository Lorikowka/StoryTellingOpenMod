package com.storyteam.storytelling.script.performer.entity.behaviour.fight.fightsattacks;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.TargetSelectorHelper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class CommonAttacks {
   private static final Random RANDOM = new Random();

   public static <T extends AnimatedEntity, U extends LivingEntity> Attack<T> stun(int cooldown, TargetSelector<T, U> targetSelector, IAnimationSource... anim) {
      return Attack.<T>builder(
            "stun",
            cooldown,
            (manager, context) -> {
               LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
               if (e == null) {
                  return true;
               } else {
                  if (context.getEntity() instanceof BaseMob baseMob) {
                     baseMob.setImpact(baseMob.getMax_impact());
                  }

                  Random random = new Random();
                  IAnimationSource animation = anim[random.nextInt(anim.length)];
                  manager.tick();
                  manager.addFlag("default_flags$STUN");
                  manager.doOnce("stun", () -> {
                     context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                     ((AnimatedEntity)context.getEntity()).startAnimation("ANIMATION_9", animation);
                  });
                  manager.addTimer(
                     "end",
                     (int)(
                           (float)animation.configure()
                                 .animation
                                 .getData()
                                 .getLength()
                                 .minus(Duration.ofMillis((long)Mth.floor(animation.configure().startTime * 1000.0F)))
                                 .toMillis()
                              / 50.0F
                              / animation.configure().speed
                        )
                        + 1
                  );
                  if (manager.containsCompleted("end")) {
                     ((AnimatedEntity)context.getEntity()).stopAnimation("ANIMATION_9");
                     manager.remove("default_flags$STUN");
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         )
         .build();
   }

   public static <T extends BaseMob, U extends LivingEntity> Attack<T> block(int cooldown, TargetSelector<T, U> targetSelector, IAnimationSource anim) {
      return Attack.<T>builder("block", cooldown, (manager, context) -> {
         LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
         if (e == null) {
            return true;
         } else {
            manager.tick();
            manager.addFlag("block");
            manager.doOnce("animation1", () -> {
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).stopAnimation("action");
               context.getState().getData().setOverrideAnimationAll(null);
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).startAnimation("ANIMATION_9", anim);
            });
            manager.addTimer("end", 25);
            if (manager.containsCompleted("end")) {
               ((BaseMob)context.getEntity()).stopAnimation("action");
               context.getState().getData().setOverrideAnimationAll(null);
               manager.remove("block");
               return true;
            } else {
               return false;
            }
         }
      }).addHandler(EventType.LIVING_HURT_DIRECT, (evt, ctx) -> {
         if (evt.getEntity() == ctx.getEntity() && evt.getEntity() != ScriptUtils.getPlayer()) {
            evt.setCanceled(true);
         }
      }).build();
   }

   public static <T extends BaseMob, U extends LivingEntity> Attack<T> actionBackWard(
      int cooldown, TargetSelector<T, U> targetSelector, int max_time_backward, float speed, float distance_active_action, float distance_difference_for_stop
   ) {
      return Attack.<T>builder(
            "actionBackWard",
            cooldown,
            (manager, context) -> {
               LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
               if (e == null) {
                  return true;
               } else {
                  manager.tick();
                  manager.addFlag("default_flags$ATTACK");
                  manager.doOnce("actionBackWard_anim", () -> {
                     context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                     AnimConfigurationWrapper animConfigurationWrapper = ((BaseMob)context.getEntity()).getWrapper();
                     IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
                     if (iDefaultAnimConfiguration != null) {
                        List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                        if (!idleList.isEmpty()) {
                           ConfiguredAnimation idle = idleList.get(RANDOM.nextInt(idleList.size()));
                           context.getState().getData().setOverrideAnimationAll(idle);
                        }

                        List<ConfiguredAnimation> movementList = iDefaultAnimConfiguration.getAnimations(MovementActionType.getBySpeed((double)speed));
                        if (!movementList.isEmpty()) {
                           ConfiguredAnimation movement = movementList.get(RANDOM.nextInt(movementList.size()));
                           ((BaseMob)context.getEntity()).startAnimation("action", movement.reversed());
                        }
                     }

                     context.getState().getData().setLookTarget(new EntityTarget(e));
                  });
                  if (((BaseMob)context.getEntity()).distanceTo(e) < distance_difference_for_stop) {
                     ((BaseMob)context.getEntity()).lookAt(e, 360.0F, 360.0F);
                     ((BaseMob)context.getEntity())
                        .move(
                           MoverType.SELF, ((BaseMob)context.getEntity()).position().subtract(e.position()).normalize().scale(0.08).scale((double)speed / 0.3)
                        );
                     manager.addTimer("end", max_time_backward);
                     if (manager.containsCompleted("end")) {
                        context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                        ((BaseMob)context.getEntity()).stopAnimation("ANIMATION_9");
                        context.getState().getData().setOverrideAnimationAll(null);
                        ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
                        ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
                        manager.remove("default_flags$ATTACK");
                        return true;
                     } else {
                        return false;
                     }
                  } else {
                     context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                     ((BaseMob)context.getEntity()).stopAnimation("ANIMATION_9");
                     manager.remove("default_flags$ATTACK");
                     context.getState().getData().setOverrideAnimationAll(null);
                     ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
                     ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
                     return true;
                  }
               }
            }
         )
         .addCondition(context -> TargetSelectorHelper.hasBetween(context, targetSelector, (double)distance_active_action, 0.0))
         .build();
   }

   public static <T extends BaseMob, U extends LivingEntity> Attack<T> dodgeRollBack(
      int cooldown, TargetSelector<T, U> targetSelector, float distance_active_action
   ) {
      return Attack.<T>builder("actionBackWard", cooldown, (manager, context) -> {
         LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
         if (e == null) {
            return true;
         } else {
            manager.tick();
            manager.addFlag("default_flags$ATTACK");
            manager.doOnce("actionBackWard_anim", () -> {
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               AnimConfigurationWrapper animConfigurationWrapper = ((BaseMob)context.getEntity()).getWrapper();
               IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
               context.getState().getData().setLookTarget(null);
               if (iDefaultAnimConfiguration != null) {
                  List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                  if (!idleList.isEmpty()) {
                     ConfiguredAnimation idle = idleList.get(RANDOM.nextInt(idleList.size()));
                     context.getState().getData().setOverrideAnimationAll(idle);
                  }

                  ((BaseMob)context.getEntity()).startAnimation("action", Animations.DODGE_ROLL_BACK);
                  manager.addTimer("end", 22);
               }

               context.getState().getData().setLookTarget(new EntityTarget(e));
               Vec3 move = ((BaseMob)context.getEntity()).position().subtract(e.position()).normalize().multiply(0.9, 0.0, 0.9).add(0.0, 0.2, 0.0);
               ((BaseMob)context.getEntity()).setDeltaMovement(move);
            });
            if (manager.containsCompleted("end")) {
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).stopAnimation("ANIMATION_9");
               context.getState().getData().setOverrideAnimationAll(null);
               ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action", 0.0F);
               ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
               manager.remove("default_flags$ATTACK");
               return true;
            } else {
               return false;
            }
         }
      }).addCondition(context -> TargetSelectorHelper.hasBetween(context, targetSelector, (double)distance_active_action, 0.0)).build();
   }
}
