package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.AttackAction;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.EntityFollowEntityBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.TargetSelectorHelper;
import java.util.List;
import java.util.Random;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class AttackUtils {
   private static final Random RANDOM = new Random();

   public static <T extends BaseMob, U extends LivingEntity> AttackAction<T> createFollowAction(
      TargetSelector<T, U> targetSelector, float distance_attack, float speed
   ) {
      return (manager, context) -> {
         manager.tick();
         U nearest = TargetSelectorHelper.nearest(context, targetSelector);
         if (nearest == null) {
            context.getState().getData().setOverrideAnimationAll(null);
            ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
            context.getState().stopBehaviour(BehaviourType.MOVEMENT);
            ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
            return true;
         } else {
            manager.doOnce(
               "follow",
               () -> {
                  context.getState().getData().setLookTarget(new EntityTarget(nearest));
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
                        ((BaseMob)context.getEntity()).startAnimation("action", movement);
                     }
                  }

                  context.getState()
                     .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
               }
            );
            if (((BaseMob)context.getEntity()).distanceTo(nearest) <= 3.0F
               && ((BaseMob)context.getEntity()).distanceTo(nearest) >= distance_attack
               && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               ((BaseMob)context.getEntity()).lookAt(nearest, 360.0F, 360.0F);
               ((BaseMob)context.getEntity())
                  .move(MoverType.SELF, nearest.position().subtract(((BaseMob)context.getEntity()).position()).normalize().multiply(0.08, 0.08, 0.08));
            } else if (((BaseMob)context.getEntity()).distanceTo(nearest) > 3.0F && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               context.getState()
                  .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
            }

            if (nearest.distanceTo(context.getEntity()) <= distance_attack) {
               context.getState().getData().setOverrideAnimationAll(null);
               ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
               return true;
            } else {
               return false;
            }
         }
      };
   }

   public static <T extends BaseMob, U extends LivingEntity> AttackAction<T> createFollowAction1(TargetSelector<T, U> targetSelector, float speed) {
      return (manager, context) -> {
         manager.tick();
         U nearest = TargetSelectorHelper.nearest(context, targetSelector);
         if (nearest == null) {
            context.getState().getData().setOverrideAnimationAll(null);
            ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
            context.getState().stopBehaviour(BehaviourType.MOVEMENT);
            ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
            return true;
         } else {
            manager.doOnce("follow", () -> {
               context.getState().getData().setLookTarget(new EntityTarget(nearest));
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
                     ((BaseMob)context.getEntity()).startAnimation("action", movement);
                  }
               }
            });
            if (nearest.distanceTo(context.getEntity()) < 3.0F + ((BaseMob)context.getEntity()).getBbWidth() / 2.0F + nearest.getBbWidth() / 2.0F) {
               double speed1 = (double)speed / 2.5 * 1.2;
               Vec3 dir = nearest.position().subtract(((BaseMob)context.getEntity()).position());
               if (dir.normalize().dot(((BaseMob)context.getEntity()).getDeltaMovement()) < speed1) {
                  Vec3 d = dir.multiply(1.0, 0.0, 1.0)
                     .normalize()
                     .scale(speed1)
                     .subtract(((BaseMob)context.getEntity()).getDeltaMovement().multiply(1.0, 0.0, 1.0));
                  double l = d.length();
                  ((BaseMob)context.getEntity())
                     .addDeltaMovement(
                        d.normalize()
                           .scale(
                              Math.min(
                                 l,
                                 0.2
                                    + 0.1
                                       * (double)(
                                          1.0F
                                             - nearest.distanceTo(context.getEntity())
                                                / (3.0F + ((BaseMob)context.getEntity()).getBbWidth() / 2.0F + nearest.getBbWidth() / 2.0F)
                                       )
                              )
                           )
                     );
                  context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                  return false;
               }
            }

            if (context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               context.getState()
                  .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, false));
            }

            return false;
         }
      };
   }

   public static <T extends BaseMob, U extends LivingEntity> AttackAction<T> createFollowActionWithoutHands(
      TargetSelector<T, U> targetSelector, float distance_attack, float speed
   ) {
      return (manager, context) -> {
         manager.tick();
         U nearest = TargetSelectorHelper.nearest(context, targetSelector);
         if (nearest == null) {
            context.getState().getData().setOverrideAnimationAll(null);
            ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
            context.getState().stopBehaviour(BehaviourType.MOVEMENT);
            ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
            return true;
         } else {
            manager.doOnce(
               "follow",
               () -> {
                  context.getState().getData().setLookTarget(new EntityTarget(nearest));
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
                        ((BaseMob)context.getEntity()).startAnimation("action", movement.mask(ScriptUses.maskNoArms()));
                     }
                  }

                  context.getState()
                     .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
               }
            );
            if (((BaseMob)context.getEntity()).distanceTo(nearest) <= 3.0F
               && ((BaseMob)context.getEntity()).distanceTo(nearest) >= distance_attack
               && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               ((BaseMob)context.getEntity()).lookAt(nearest, 360.0F, 360.0F);
               ((BaseMob)context.getEntity())
                  .move(MoverType.SELF, nearest.position().subtract(((BaseMob)context.getEntity()).position()).normalize().multiply(0.08, 0.08, 0.08));
            } else if (((BaseMob)context.getEntity()).distanceTo(nearest) > 3.0F && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               context.getState()
                  .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
            }

            if (nearest.distanceTo(context.getEntity()) <= distance_attack) {
               context.getState().getData().setOverrideAnimationAll(null);
               ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
               return true;
            } else {
               return false;
            }
         }
      };
   }

   public static <T extends BaseMob, U extends LivingEntity> AttackAction<T> createFollowActionWithDrop(
      TargetSelector<T, U> targetSelector, float distance_attack, float speed, int time_try_complete
   ) {
      return (manager, context) -> {
         manager.tick();
         U nearest = TargetSelectorHelper.nearest(context, targetSelector);
         if (nearest == null) {
            return true;
         } else {
            manager.doOnce(
               "follow",
               () -> {
                  context.getState().getData().setLookTarget(new EntityTarget(nearest));
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
                        ((BaseMob)context.getEntity()).startAnimation("action", movement);
                     }
                  }

                  context.getState()
                     .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
               }
            );
            if (((BaseMob)context.getEntity()).distanceTo(nearest) <= 3.0F
               && ((BaseMob)context.getEntity()).distanceTo(nearest) >= distance_attack
               && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               ((BaseMob)context.getEntity()).lookAt(nearest, 360.0F, 360.0F);
               ((BaseMob)context.getEntity())
                  .move(MoverType.SELF, nearest.position().subtract(((BaseMob)context.getEntity()).position()).normalize().multiply(0.08, 0.08, 0.08));
            } else if (((BaseMob)context.getEntity()).distanceTo(nearest) > 3.0F && context.getState().getBehaviour(BehaviourType.MOVEMENT) == null) {
               context.getState()
                  .addBehaviour(new EntityFollowEntityBehaviour((double)speed, TargetSelectorHelper.nearest(context, targetSelector), 0.0, true));
            }

            if (nearest.distanceTo(context.getEntity()) <= distance_attack) {
               context.getState().getData().setOverrideAnimationAll(null);
               ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
               context.getState().stopBehaviour(BehaviourType.MOVEMENT);
               ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
               return true;
            } else {
               manager.addTimer("end", time_try_complete);
               if (manager.containsCompleted("end")) {
                  context.getState().getData().setOverrideAnimationAll(null);
                  ((BaseMob)context.getEntity()).getAnimationSystem().stopAnimation("action");
                  context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                  ((BaseMob)context.getEntity()).setDeltaMovement(Vec3.ZERO);
                  return true;
               } else {
                  return false;
               }
            }
         }
      };
   }
}
