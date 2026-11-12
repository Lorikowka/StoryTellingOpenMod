package com.storyteam.storytelling.script.performer.entity.behaviour.performer_pattern_season_utils;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.animations.AnimationsST7;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.client.particle.controllers.ScatterController;
import com.storyteam.storytelling.content.entities.api.CustomFlyingMoveControl;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightStages;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.mobs.SpecialCharacter;
import com.storyteam.storytelling.system.fight.mob_drop.MobDropUtil;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class DronePatternUtils {
   public static AABB aroundBunker = new AABB(-3067.0, 67.0, -120.0, -3137.0, 94.0, -59.0);
   static Random rnd = new Random();

   public static Consumer<ScriptPerformerBuilder> fightPatternDrone(
      Character<BaseMob> ch, IAttackChain<BaseMob> mob, double close_to, String coord, double distance_agr
   ) {
      return b -> b.positionEntity(ch, coord)
            .label("fight", false)
            .thenUseEntity(ch, e -> {
               if (e.getMoveControl() instanceof CustomFlyingMoveControl flyingMoveControl) {
                  flyingMoveControl.setHoversInPlace(true);
               }

               e.setNoGravity(true);
            })
            .thenDelay(2)
            .enableFight(ch, true)
            .thenUseEntity(ch, e -> e.setHealth(e.getMaxHealth()))
            .thenEntityFlyRandomWalk(ch, 0.3, coord, -5, 10, 7, 10)
            .thenAnyPerformer(
               new Performer[]{
                  ScriptPerformerBuilder.builder(b.getPerformer().script).thenWaitUntil(() -> complete(ch, close_to)).build(),
                  ScriptPerformerBuilder.builder(b.getPerformer().script)
                     .thenAwaitEvent(
                        EventType.LIVING_HURT_DIRECT,
                        e -> {
                           if (ch.repositoryEntity() != null
                              && e.getEntity() == ch.repositoryEntity()
                              && ch.repositoryEntity() != null
                              && e.getEntity() == ch.repositoryEntity()
                              && e.getSource().getEntity() instanceof Player p
                              && (double)p.distanceTo(e.getEntity()) <= distance_agr) {
                              return true;
                           } else {
                              if (ch.repositoryEntity() != null && e.getEntity() == ch.repositoryEntity()) {
                                 e.setCanceled(true);
                              }

                              return false;
                           }
                        }
                     )
                     .build()
               }
            )
            .thenClearEntityGoal(ch)
            .entityLookAt(ch, "player")
            .enableFight(ch, true)
            .startFight(
               ch,
               FightStages.createFightStage(
                  mob,
                  context -> {
                     double deltaX = ((BaseMob)context.getEntity()).position().x - (new Location(coord)).x;
                     double deltaZ = ((BaseMob)context.getEntity()).position().z - (new Location(coord)).z;
                     double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                     return ((BaseMob)context.getEntity()).getHealth() <= 1.0F
                        || horizontalDistance > distance_agr
                        || ScriptUtils.getPlayer() != null && ScriptUtils.getPlayer().isSpectator()
                        || ScriptUtils.getPlayer() != null && aroundBunker.contains(ScriptUtils.getPlayer().position());
                  }
               )
            )
            .thenClearEntityGoal(ch)
            .appendBuilder(stopFightReturnOrDeathDrone(ch, coord, 300, 0.3, distance_agr))
            .thenRun(
               () -> {
                  if (ch.repositoryEntity() != null) {
                     double deltaX = ((BaseMob)ch.repositoryEntity()).position().x - (new Location(coord)).x;
                     double deltaZ = ((BaseMob)ch.repositoryEntity()).position().z - (new Location(coord)).z;
                     double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                     System.out.println("d " + ((BaseMob)ch.repositoryEntity()).position().x + " " + (new Location(coord)).x + " " + horizontalDistance);
                     Storytelling.LOG
                        .info(
                           "Файт с "
                              + ch.getReferenceId()
                              + " окончен. Условия: Health: "
                              + ((BaseMob)ch.repositoryEntity()).getHealth()
                              + " Условие: "
                              + prov((BaseMob)ch.repositoryEntity(), coord, distance_agr)
                        );
                  } else {
                     Storytelling.LOG.info("Файт с " + ch.getReferenceId() + " окончен.");
                  }
               }
            )
            .thenWaitForFlag("fight_return" + ch.getReferenceId())
            .thenRun(() -> b.getPerformer().script.flags.remove("fight_return" + ch.getReferenceId()))
            .gotoLabel("fight", true);
   }

   public static Consumer<ScriptPerformerBuilder> stopFightReturnOrDeathDrone(
      Character<BaseMob> character, String pos, int delayTp, double speed, double distance_agr
   ) {
      return b -> b.thenRun(() -> System.out.println("FIGHT_STOP with " + character.getReferenceId()))
            .thenIf(
               () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).getHealth() <= 1.0F,
               ScriptPerformerBuilder.builder(b.getPerformer().script)
                  .action(character, AnimationsST7.DRONE_SHOT_DAMAGE)
                  .thenUseEntity(character, e -> {
                     Player p = ScriptUtils.getPlayer();
                     if (p != null) {
                        Vec3 move = e.position().subtract(p.position()).normalize();
                        e.setDeltaMovement(move.scale(0.5));
                     }
                  })
                  .thenDelay(AnimationsST7.DRONE_SHOT_DAMAGE)
                  .thenUseEntity(character, e -> {
                     if (e.getMoveControl() instanceof CustomFlyingMoveControl flyingMoveControl) {
                        flyingMoveControl.setHoversInPlace(false);
                     }

                     e.setNoGravity(false);
                  })
                  .thenWaitUntil(
                     () -> character.repositoryEntity() != null && ((BaseMob)character.repositoryEntity()).onGround() || character.repositoryEntity() == null
                  )
                  .thenDelay(5)
                  .thenUseEntity(
                     character,
                     e -> {
                        exp_drone(e, 0.4F);
                        ScriptUses.playSoundAt(e.position(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 3.0F, 1.0F).run();
                        ParticleHelper.spawnParticleAABB(
                           e.level(),
                           ParticleTypes.CAMPFIRE_COSY_SMOKE,
                           e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                           20,
                           0.01
                        );
                        ParticleHelper.spawnParticleAABB(
                           e.level(),
                           ParticleTypes.CAMPFIRE_COSY_SMOKE,
                           e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                           20,
                           0.01
                        );
                        ParticleHelper.spawnParticleAABB(
                           e.level(),
                           ParticleTypes.EXPLOSION,
                           e.getBoundingBox().contract(0.0, e.getBoundingBox().getYsize() / 2.0, 0.0).inflate(0.7, 0.0, 0.7),
                           20,
                           0.01
                        );
                     }
                  )
                  .thenRun(() -> {
                     Entity e = IStoryServer.get().getEntityRepository().getEntity(character);
                     if (e instanceof BaseMob baseMob) {
                        if (character instanceof SpecialCharacter<BaseMob> specialCharacter) {
                           MobDropUtil.handleDrops(e.level(), e.position().add(0.0, (double)(e.getBbHeight() / 2.0F), 0.0), specialCharacter.getmobDropList());
                        }

                        baseMob.onDeathByPlayer();
                     }
                  })
                  .freeEntity(character, true)
                  .build(),
               ScriptPerformerBuilder.builder(b.getPerformer().script)
                  .enableFight(character, true)
                  .thenAnyPerformer(
                     new Performer[]{
                        ScriptPerformerBuilder.builder(b.getPerformer().script)
                           .thenEntityPath(character, speed, pos)
                           .entityLookAtFromRot(character, pos)
                           .build(),
                        ScriptPerformerBuilder.builder(b.getPerformer().script).thenDelay(delayTp).positionEntity(character, pos).build(),
                        ScriptPerformerBuilder.builder(b.getPerformer().script).thenAwaitEvent(EventType.LIVING_HURT_DIRECT, e -> {
                           if (character.repositoryEntity() != null
                              && e.getEntity() == character.repositoryEntity()
                              && e.getSource().getEntity() instanceof Player p) {
                              double deltaX = e.getSource().getEntity().position().x - e.getEntity().getX();
                              double deltaZ = e.getSource().getEntity().position().z - e.getEntity().getZ();
                              double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                              if (horizontalDistance <= distance_agr) {
                                 return true;
                              }
                           }

                           if (character.repositoryEntity() != null && e.getEntity() == character.repositoryEntity()) {
                              e.setCanceled(true);
                           }

                           return false;
                        }).build()
                     }
                  )
                  .thenUseEntity(character, e -> e.setHealth(e.getMaxHealth()))
                  .thenRun(() -> b.getPerformer().script.getFlags().set("fight_return" + character.getReferenceId()))
                  .build()
            );
   }

   public static void exp_drone(BaseMob mob, float radius) {
      double d0 = mob.position().x();
      double d1 = mob.position().y() + 0.9;
      double d2 = mob.position().z();
      Random rnd = new Random();

      for (int j = 0; j < 100; j++) {
         double theta = Math.random() * 2.0 * Math.PI;
         double phi = Math.acos(2.0 * Math.random() - 1.0);
         double stretchX = 0.4;
         double stretchY = 0.2;
         double stretchZ = 0.4;
         double x = (double)radius * stretchX * Math.sin(phi) * Math.cos(theta);
         double y = (double)radius * stretchY * Math.sin(phi) * Math.sin(theta);
         double z = (double)radius * stretchZ * Math.cos(phi);
         double d3 = x + (rnd.nextDouble() - rnd.nextDouble()) * 0.1;
         double d4 = y + (rnd.nextDouble() - rnd.nextDouble()) * 0.1;
         double d5 = z + (rnd.nextDouble() - rnd.nextDouble()) * 0.1;
         ParticleHelper.spawnDirectedParticle(
            mob.level(),
            new CircleTintData(generateRedOrangeColor(), 0.35F, 30, -1.0F, false, false, new ScatterController()),
            d0 + d3,
            d1 + d4,
            d2 + d5,
            d0 + d3 - d0,
            d1 + d4 - d1,
            d2 + d5 - d2
         );
         ParticleHelper.spawnDirectedParticle(mob.level(), ParticleTypes.FLAME, d0 + d3, d1 + d4, d2 + d5, d0 + d3 - d0, d1 + d4 - d1, d2 + d5 - d2);
         ParticleHelper.spawnDirectedParticle(mob.level(), ParticleTypes.SMOKE, d0 + d3, d1 + d4, d2 + d5, d0 + d3 - d0, d1 + d4 - d1, d2 + d5 - d2);
         if (j % 10 == 0) {
            ParticleHelper.spawnDirectedParticle(mob.level(), ParticleTypes.LAVA, d0 + d3, d1 + d4, d2 + d5, d0 - (d0 + d3), d1 + d4 - d1, d2 - (d2 + d5));
         }
      }
   }

   public static Color generateRedOrangeColor() {
      return new Color(245 + rnd.nextInt(10), 20 + rnd.nextInt(90), 12 + rnd.nextInt(30));
   }

   public static boolean complete(Character<BaseMob> character, double distance) {
      Player player = ScriptUtils.getPlayer();
      if (player == null) {
         return false;
      } else {
         BaseMob entity = (BaseMob)character.repositoryEntity();
         return entity == null
            ? false
            : !player.isSpectator()
               && entity.level().dimension().location().equals(player.level().dimension().location())
               && ScriptUtils.getAngle(entity, player) < 75.0
               && (double)entity.distanceTo(player) < distance + 10.0
               && isClearForAttack(entity, player, false);
      }
   }

   private static boolean isClearForAttack(Mob pMob, LivingEntity target, boolean pAllowSwimming) {
      Vec3[] pointsToCheck = new Vec3[]{
         target.position().add(0.0, (double)target.getBbHeight(), 0.0), target.position().add(0.0, (double)target.getBbHeight() * 0.5, 0.0), target.position()
      };

      for (Vec3 point : pointsToCheck) {
         boolean isClear = pMob.level()
               .clip(
                  new ClipContext(
                     pMob.position().add(0.0, (double)pMob.getBbHeight() * 0.5, 0.0), point, Block.COLLIDER, pAllowSwimming ? Fluid.ANY : Fluid.NONE, pMob
                  )
               )
               .getType()
            == Type.MISS;
         if (isClear) {
            return true;
         }
      }

      return false;
   }

   public static boolean prov(EntityContext<BaseMob> context, String pos, double distance_agr) {
      return ScriptUtils.getPlayer() != null && (double)((BaseMob)context.getEntity()).distanceTo(ScriptUtils.getPlayer()) > distance_agr;
   }

   public static boolean prov(BaseMob context, String pos, double distance_agr) {
      return context.position().distanceTo(new Location(pos)) > distance_agr
         || ScriptUtils.getPlayer() != null && (double)context.distanceTo(ScriptUtils.getPlayer()) > distance_agr;
   }
}
