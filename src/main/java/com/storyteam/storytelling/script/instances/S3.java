package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.ForgetOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.entities.AnimatedEntityLikeHumanoid;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.init.system.RiddlesST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.FightAttacks;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.Attack;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.attacks.AttackAction;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.IAttackChain;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.fightsattacks.CommonAttacks;
import com.storyteam.storytelling.script.performer.entity.look.targets.EntityTarget;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system._core.structio.entity.StructioContent;
import com.storyteam.storytelling.system._core.structio.entity.StructureEntity;
import com.storyteam.storytelling.system.currency.CurrencySystem;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.fight.AttackAnimActionBuilder;
import com.storyteam.storytelling.system.fight.Collider;
import com.storyteam.storytelling.system.persona.PersonaHandler;
import com.storyteam.storytelling.system.riddles.storage.RiddlesStorage;
import com.storyteam.storytelling.system.simulator.main_page.client.gui.MainTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.TargetSelectorHelper;
import com.storyteam.storytelling.util.Title;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animation.LoopMode;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.util.java.Cast;

@ForgetOnClose
@Script(3)
public class S3 extends ScriptInstance {
   private static final Random rng = new Random((long)"S3".hashCode());
   private static int tickPlayerOnBlock = 0;
   private static final CharacterList<AntEntity> ANTS = Characters.ANT_FAC.createCharacterList("s3_ants", 10);
   private static final Character<StructureEntity> TRIANGLE_STATUE = (Character<StructureEntity>)Cast.cast(
      Character.builder("s3_triangle_statue", (Supplier<net.minecraft.world.entity.EntityType<StructureEntity>>)(() -> StructioContent.STRUCTURE_ENTITY_ENTITY_TYPE)).name("Cтатуя").onCreatedFresh(e -> {
         e.setStructure("s3_triangle_statue_just_triangle");
         e.setNoGravity(true);
         e.noPhysics = true;
      }).build()
   );
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S3 inst) {
            this.saveState("0");
            this.ensureScriptsSet(new int[]{-100, -102, -301, -302, -303, -304, -305});
            this.thenPerform(() -> {
               RiddlesStorage.get(RiddlesST.S3_HEAD.getId()).setVisible(false);
               RiddlesStorage.setSolvedForPersona(PersonaHandler.getCurrentPersona(), RiddlesST.S3_HEAD.getId(), false);
               RiddlesStorage.setCanReadForPersona(PersonaHandler.getCurrentPersona(), false);
            });
            this.thenSetSpeechEntity(0, "???", "sky_eye");
            this.thenSetSpeechEntity(1, "???", "blue_man");
            this.clearChat();
            this.clearPlayerEmotion();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -29.42 103.00 -481.20 10.11 -1.05");
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.thenRun(() -> TimeController.get().lockTime());
            this.setTime(1, "5:59");
            this.setSkyEyeLookTarget("player");
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_CLOSE});
            this.sendWebWarning("Начало по F8");
            this.thenWaitForF8();
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         private void part1(S3 inst) {
            this.saveState("1");
            this.clearPlayerEmotion();
            this.timeMarker("5:59", true);
            this.thenDelay(20);
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_OPEN});
            this.thenDelay(Animations.SKYEYE_OPEN);
            this.addPlayerEmotion(Emotion.FACE_EXHALING);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Доброе утро.");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "Ты рад меня видеть?");
            this.addPlayerEmotion(Emotion.SLIGHTLY_SMILING_FACE);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Как прошла твоя первая ночь?");
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_TEAR);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Я знаю, что маленькие иногда боятся ночи.");
            this.thenSpeechMessage(0, "Но это зря.");
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Ночью красиво.");
            this.stopAction(Characters.SKY_EYE);
            this.addPlayerEmotion(Emotion.EYES);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "А ещё ночью хорошо отдыхается.");
            this.thenSpeechMessage(0, "Тебе удалось отдохнуть?");
            this.addPlayerEmotion(Emotion.PENSIVE_FACE);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Нет?");
            this.thenSpeechMessage(0, "Жаль.");
            this.thenSpeechMessage(0, "Значит, ты слишком уставший, чтобы изучать новое…");
            this.addPlayerEmotion(Emotion.FOLDED_HANDS);
            this.thenDelay(20);
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SURPRISE});
            this.thenSpeechMessage(0, "Всё равно хочешь?");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "Да, рвения тебе не занимать.");
            this.thenSpeechMessage(0, "Хорошо, тогда докажи мне, что ты готов перейти на следующий этап.");
            this.addPlayerEmotion(Emotion.SALUTING_FACE);
            this.thenDelay(20);
            this.saveState("2");
            this.clearPlayerEmotion();
            this.importBuilding("s3_pit_stage_1");
            this.sendWebWarningRed(
               "Поищи блок с партиклами и встань ровно на него (/execute in minecraft:overworld run tp @s -49.50 103.00 -449.50 0.00 0.00), после чего ты провалишься вниз (не вылезай), а потом тебя поднимет вверх"
            );
            this.thenRun(() -> S3.tickPlayerOnBlock = 0);
            this.createTicker("ground_particle", p -> {
               Location loc = new Location("/execute in minecraft:overworld run tp @s -49.50 103.00 -449.50 0.00 0.00");
               Location pLoc = ScriptUses.playerPos().get();
               if (S3.tickPlayerOnBlock < 40) {
                  S3.this.spawnEndRodParticles(p, loc.add(0.0, 0.7, 0.0));
               }

               boolean withinRange = Math.abs(pLoc.x() - loc.x()) <= 0.1 && Math.abs(pLoc.y() - loc.y()) <= 0.1 && Math.abs(pLoc.z() - loc.z()) <= 0.1;
               if (withinRange) {
                  if (++S3.tickPlayerOnBlock >= 60) {
                     S3.this.flags.set("player_on_block");
                  }
               } else {
                  S3.tickPlayerOnBlock = 0;
               }
            });
            this.thenWaitForFlag("player_on_block");
            this.deleteTicker("ground_particle");
            this.importBuilding("s3_pit_stage_2");
            this.thenUsePlayer(p -> ScriptUses.playSoundAt(p.position(), SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 3.0F, 1.0F).run());
            this.thenDelay(20);
            this.importBuilding("s3_pit_stage_3");
            this.thenUsePlayer(p -> ScriptUses.playSoundAt(p.position(), SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 3.0F, 1.0F).run());
            this.thenDelay(20);
            this.sendWebWarning("Сверху будет босс, пока не подходи к нему близко");
            this.thenAnyPerformer(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(30)
                     .importBuilding("s3_pit_stage_2")
                     .teleportPlayer(() -> ScriptUses.playerPos().get().add(0.0, 1.1, 0.0))
                     .thenUsePlayer(p -> ScriptUses.playSoundAt(p.position(), SoundEvents.ROOTED_DIRT_PLACE, SoundSource.BLOCKS, 3.0F, 1.0F).run())
                     .thenDelay(30)
                     .importBuilding("s3_pit_stage_1")
                     .teleportPlayer(() -> ScriptUses.playerPos().get().add(0.0, 1.1, 0.0))
                     .thenUsePlayer(p -> ScriptUses.playSoundAt(p.position(), SoundEvents.ROOTED_DIRT_PLACE, SoundSource.BLOCKS, 3.0F, 1.0F).run())
                     .thenDelay(30)
                     .action(Characters.SKY_EYE, Animations.SKYEYE_INTEREST)
                     .thenSpeechMessage(0, "Интересно, а это кто?")
                     .stopAction(Characters.SKY_EYE)
                     .thenSpeechMessage(0, "Как ты думаешь, это друг или враг?")
                     .addPlayerEmotion(Emotion.THINKING_FACE)
                     .thenDelay(20)
                     .thenSpeechMessage(0, "Хочешь подойти поближе и познакомиться?")
                     .thenSpeechMessage(0, "Или хочешь сначала понаблюдать издалека?")
                     .addPlayerEmotion(Emotion.FACE_WITH_PEEKING_EYE)
                     .thenDelay(20)
                     .thenSpeechMessage(0, "Ты прав: быть осторожным — это хорошая стратегия.")
                     .thenSpeechMessage(0, "Не зря ты уже второго уровня.")
                     .thenSpeechMessage(0, "Уже начинаешь что-то понимать.", false)
                     .sendWebWarning("Подойди к боссу на 5 блоков, но не слишком близко")
                     .thenWhenPlayerCloseTo(Characters.MUD_BOSS, 5.0)
                     .thenSpeechMessage(0, "Не волнуйся, даже если он тебе не друг, то мы с формиками — точно твои друзья.")
                     .clearChat()
                     .sendWebWarning("Теперь можешь заагрить его на себя, подойди на 3 блока к нему")
                     .thenWhenPlayerCloseTo(Characters.MUD_BOSS, 3.0)
                     .clearChat()
                     .thenDelay(10)
                     .addPlayerEmotion(Emotion.FEARFUL_FACE)
                     .thenDelay(20)
                     .thenSpeechMessage(0, "Значит, он всё-таки недружелюбный?")
                     .action(Characters.SKY_EYE, Animations.SKYEYE_SLIGHT_ANGRY)
                     .thenSpeechMessage(0, "Как грубо с его стороны так тебя преследовать.")
                     .stopAction(Characters.SKY_EYE)
                     .thenSpeechMessage(0, "Что будешь делать?", false)
                     .thenDelay(200)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .positionEntity(Characters.MUD_BOSS, "/execute in minecraft:overworld run tp @s -42.15 103.00 -455.63 9.83 -2.55")
                     .entityLookAt(Characters.MUD_BOSS, "")
                     .startFight(Characters.MUD_BOSS, S3.MUD_BOSS_PATTERN())
                     .build()
               }
            );
            this.saveState("3");
            this.blackout(30);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntity(Characters.MUD_BOSS, true);
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -37.43 103.32 -458.15 223.73 8.85");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -37.43 103.32 -458.15 223.73 8.85");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -36.16 103.00 -459.11 -144.00 3.60");
            this.positionEntity(Characters.MUD_BOSS, "/execute in minecraft:overworld run tp @s -38.84 103.00 -455.47 -144.75 -0.15");
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.entityLookAt(Characters.MUD_BOSS, Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.blackout(20);
            this.thenDelay(15);
            this.tdCutsceneMoveCamera(
               120,
               SplineType.CATMULLROM,
               InterpolationType.LINEAR,
               new String[]{"/execute in minecraft:overworld run tp @s -34.37 103.32 -462.04 -127.72 12.30"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -32.74 103.00 -463.30 220.13 4.05"}
            );
            this.tdCutscene("/execute in minecraft:overworld run tp @s -38.74 103.32 -457.55 1.13 -4.50");
            this.tdCutsceneMoveCamera(
               60, SplineType.CATMULLROM, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -35.50 103.51 -461.53 -12.97 5.70"}
            );
            this.thenEntityPath(Characters.MUD_BOSS, new String[]{"/execute in minecraft:overworld run tp @s -35.24 103.00 -460.18 -141.07 0.60"});
            this.action(Characters.MUD_BOSS, new ConfiguredAnimation[]{Animations.FALL_FORWARD.configure().transitionTime(0.6F)});
            this.tdCutsceneMoveCamera(
               20,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -34.57 102.85 -461.65 9.53 -1.50"}
            );
            this.thenDelay(25);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -33.18 103.04 -461.42 186.98 -7.65");
            this.entityLookAt(Characters.SPIDER_HEAD_LOLOLOSHKA, Characters.MUD_BOSS, true);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_WITH_TEARS_OF_JOY);
            this.thenDelay(25);
            this.tdCutsceneMoveCamera(
               30,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -33.29 103.04 -461.15 -297.82 -15.90"}
            );
            this.action(Characters.MUD_BOSS, new IAnimationSource[]{Animations.DIZZY_STAND_UP_FROM_HANDS});
            this.thenDelay(Animations.DIZZY_STAND_UP_FROM_HANDS);
            this.stopAction(Characters.MUD_BOSS);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.action(Characters.SPIDER_HEAD_LOLOLOSHKA, new IAnimationSource[]{Animations.SPIDER_HEAD_10_DEGREE_HEAD});
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -32.51 103.12 -461.03 129.68 -7.95"}
            );
            this.thenEntityPath(Characters.MUD_BOSS, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -34.11 103.00 -461.44 -145.72 11.55"});
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.ANGUISHED_FACE);
            this.thenDelay(10);
            this.action(Characters.MUD_BOSS, new ConfiguredAnimation[]{Animations.RUBBING_HANDS_LOOP.configure().transitionTime(0.6F)});
            this.thenDelay(30);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -33.29 103.41 -461.90 21.23 -23.40"}
            );
            this.thenDelay(45);
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SLIGHT_ANGRY});
            this.thenSpeechMessage(0, "Так, это мне уже  совсем не нравится.");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "Формики, не медлите — вы же видите, что нашего друга надо спасать.");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -34.46 103.50 -461.99 233.63 12.75");
            this.clearChat();
            this.thenDelay(15);
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(Characters.SPIDER_HEAD_LOLOLOSHKA).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(Characters.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -33.00 103.50 -463.00 395.63 -7.50")
                     .setSkyEyeLookTarget(Characters.HEAD_LOLOLOSHKA)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .action(Characters.CUTSCENE_MERGE_ANT, Animations.CUTSCENE_MERGE_ANT_JUMP.configure().transitionTime(0.0F).freezeAt(0.01F))
                     .createTelepornGlitchEffect(Characters.CUTSCENE_MERGE_ANT, "/execute in minecraft:overworld run tp @s -32.99 103.00 -461.97 427.73 -4.50")
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(10)
                     .tdCutsceneMoveCamera(
                        20,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -34.46 103.50 -461.99 265.43 16.80"
                     )
                     .thenDelay(10)
                     .build()
               }
            );
            this.addCharacterEmotion(Characters.HEAD_LOLOLOSHKA, Emotion.FLUSHED_FACE);
            this.thenDelay(15);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -34.46 103.50 -461.99 -92.17 0.30"}
            );
            this.action(Characters.CUTSCENE_MERGE_ANT, new IAnimationSource[]{Animations.CUTSCENE_MERGE_ANT_JUMP});
            this.thenDelay(Animations.CUTSCENE_MERGE_ANT_JUMP);
            this.action(Characters.CUTSCENE_MERGE_ANT, new IAnimationSource[]{Animations.CUTSCENE_MERGE_ANT_BIGGER});
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(Characters.CUTSCENE_MERGE_ANT).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(15)
                     .positionEntity(Characters.UNIT_BASE_BIGGER, "/execute in minecraft:overworld run tp @s -32.11 103.00 -462.08 425.78 -2.10")
                     .thenDelay(15)
                     .build()
               }
            );
            this.tdCutsceneMoveCamera(
               30,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -33.32 103.36 -461.90 -98.47 -8.70"}
            );
            this.entityLookAt(Characters.MUD_BOSS, Characters.UNIT_BASE_BIGGER);
            this.thenSpeechMessage(0, "Удивительно, правда?", false);
            this.thenDelay(20);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.AMAZING));
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -32.90 103.73 -461.43 438.38 -3.90");
            this.clearChat();
            this.clearCharacterEmotion(Characters.HEAD_LOLOLOSHKA);
            this.action(Characters.MUD_BOSS, new IAnimationSource[]{Animations.BODY_POSE_SHOCK_ONLY_FACE});
            this.entityLookAt(Characters.UNIT_BASE_BIGGER, Characters.MUD_BOSS);
            this.thenDelay(15);
            this.tdCutsceneMoveCamera(
               25,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -32.87 103.73 -460.74 118.13 3.90"}
            );
            this.thenDelay(5);
            this.thenEntityPath(Characters.UNIT_BASE_BIGGER, 0.4, new String[]{"/execute in minecraft:overworld run tp @s -33.54 103.00 -461.61 58.13 -1.50"});
            this.action(Characters.UNIT_BASE_BIGGER, new IAnimationSource[]{Animations.UNIT_BASE_BIGGER_STRONG_PUNCH});
            this.action(Characters.MUD_BOSS, new IAnimationSource[]{Animations.MUD_MAN_FALL_FROM_PUNCH});
            this.entityLookAt(Characters.MUD_BOSS, "");
            this.thenDelay(10);
            this.tdCutsceneMoveCamera(
               15,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -35.32 103.64 -459.57 74.03 18.30"}
            );
            this.thenEntityMove(Characters.MUD_BOSS, 0.37, "/execute in minecraft:overworld run tp @s -35.98 103.00 -459.75 233.63 -1.50", false);
            this.thenDelay(10);
            this.createTicker(
               "mud_boss_died",
               p -> {
                  BaseMob e = (BaseMob)Characters.MUD_BOSS.repositoryEntity();
                  if (e != null) {
                     Level level = e.level();
                     Vec3 center = ScriptUtils.vec3FromBone("Body", e, Vec3.ZERO);
                     double speed = 0.15 + S3.rng.nextDouble() * 0.15;
                     Vec3 start = center.add((S3.rng.nextDouble() - 0.5) * 0.3, (S3.rng.nextDouble() - 0.3) * 0.3, (S3.rng.nextDouble() - 0.5) * 0.3);
                     ParticleHelper.spawnParticles(
                        level,
                        new DustColorTransitionOptions(new Vector3f(0.25F, 0.15F, 0.05F), new Vector3f(0.5F, 0.35F, 0.2F), 1.5F + S3.rng.nextFloat() * 0.5F),
                        start,
                        5,
                        0.5,
                        0.1,
                        0.5,
                        speed
                     );
                  }
               }
            );
            this.thenDelay(20);
            this.deleteTicker("mud_boss_died");
            this.thenRun(() -> {
               Entity e = Characters.MUD_BOSS.repositoryEntity();
               if (e != null) {
                  UtilST.dropEidos(e.level(), e.position().add(-2.0, 0.0, 2.0), 10, false);
               }
            });
            this.freeEntity(Characters.MUD_BOSS, true);
            this.thenSpeechMessage(0, "Отлично.", false);
            this.thenDelay(40);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -35.37 103.30 -461.86 267.53 -4.05");
            this.thenSpeechMessage(0, "Я ведь говорил тебе, что формики многое умеют.", false);
            this.entitySit(Characters.HEAD_LOLOLOSHKA);
            this.addCharacterEmotion(Characters.HEAD_LOLOLOSHKA, Emotion.CLAPPING_HANDS);
            this.addCharacterEmotion(Characters.HEAD_LOLOLOSHKA, Emotion.STAR_STRUCK);
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(Characters.UNIT_BASE_BIGGER).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(15)
                     .positionEntity(S3.ANTS.get(0), "/execute in minecraft:overworld run tp @s -33.64 103.00 -461.04 44.93 -2.40")
                     .positionEntity(S3.ANTS.get(1), "/execute in minecraft:overworld run tp @s -32.86 103.00 -461.05 54.83 -2.70")
                     .positionEntity(S3.ANTS.get(2), "/execute in minecraft:overworld run tp @s -32.88 103.00 -461.46 51.53 -1.95")
                     .positionEntity(S3.ANTS.get(3), "/execute in minecraft:overworld run tp @s -33.92 103.00 -461.37 411.68 -0.60")
                     .positionEntity(S3.ANTS.get(4), "/execute in minecraft:overworld run tp @s -33.62 103.00 -461.91 412.13 0.00")
                     .positionEntity(S3.ANTS.get(5), "/execute in minecraft:overworld run tp @s -33.29 103.00 -462.00 49.28 0.15")
                     .thenDelay(15)
                     .build()
               }
            );
            this.tdCutsceneMoveCamera(
               30,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -34.33 103.41 -461.44 219.83 15.00"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .thenEntityPath(S3.ANTS.get(0), 0.35, "/execute in minecraft:overworld run tp @s -33.27 103.00 -462.07 -527.01 10.20\n")
                     .entityLookAt(S3.ANTS.get(0), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(0), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .thenEntityPath(S3.ANTS.get(1), 0.35, "/execute in minecraft:overworld run tp @s -32.27 103.00 -462.25 -222.97 4.35\n")
                     .entityLookAt(S3.ANTS.get(1), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(1), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(S3.ANTS.get(2), 0.35, "/execute in minecraft:overworld run tp @s -32.03 103.00 -463.02 -272.02 3.90\n")
                     .entityLookAt(S3.ANTS.get(2), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(2), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .thenEntityPath(S3.ANTS.get(3), 0.35, "/execute in minecraft:overworld run tp @s -33.89 103.00 -462.55 -476.76 10.35\n")
                     .entityLookAt(S3.ANTS.get(3), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(3), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .thenEntityPath(S3.ANTS.get(4), 0.35, "/execute in minecraft:overworld run tp @s -33.91 103.00 -463.40 -426.90 6.15\n")
                     .entityLookAt(S3.ANTS.get(4), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(4), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(
                        S3.ANTS.get(5),
                        0.35,
                        "/execute in minecraft:overworld run tp @s -33.99 103.00 -462.63 -568.35 0.75\n/execute in minecraft:overworld run tp @s -33.96 103.00 -463.65 -465.45 5.10\n/execute in minecraft:overworld run tp @s -33.26 103.00 -464.01 -375.60 3.00\n"
                     )
                     .entityLookAt(S3.ANTS.get(5), Characters.HEAD_LOLOLOSHKA)
                     .action(
                        S3.ANTS.get(5), Animations.ANT_HAPPY_JUMP_CYCLE.configure().speed(S3.rng.nextFloat(1.0F, 1.2F)).weight(S3.rng.nextFloat(0.7F, 1.0F))
                     )
                     .build()
               }
            );
            this.thenDelay(20);
            this.clearCharacterEmotion(Characters.HEAD_LOLOLOSHKA);
            this.addCharacterEmotion(Characters.HEAD_LOLOLOSHKA, Emotion.PARTYING_FACE);
            this.thenDelay(40);
            this.thenSpeechMessage(0, "Кроме того, несмотря на свой малый размер, они довольно сильны.");
            this.clearCharacterEmotion(Characters.HEAD_LOLOLOSHKA);
            this.clearChat();
            this.tdCutsceneMoveCamera(
               50,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -33.72 103.49 -460.83 196.28 6.90"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst).thenDelay(15).deleteTelepornGlitchEffect(Characters.HEAD_LOLOLOSHKA).build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(30)
                     .freeEntities(true, S3.ANTS.get(0), S3.ANTS.get(1), S3.ANTS.get(2), S3.ANTS.get(3), S3.ANTS.get(4), S3.ANTS.get(5))
                     .positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -33.00 103.00 -463.00 395.63 -7.50")
                     .setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(0))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(0), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(0))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(1))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(1), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(1))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(2))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(2), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(2))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(3))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(3), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(3))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(4))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(4), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(4))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .stopAction(S3.ANTS.get(5))
                     .thenDelay(10)
                     .thenEntityFollowEntity(S3.ANTS.get(5), Characters.HEAD_LOLOLOSHKA, 0.3, 0.0)
                     .deleteTelepornGlitchEffect(S3.ANTS.get(5))
                     .build()
               }
            );
            this.thenDelay(25);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         private void part2(S3 inst) {
            this.saveState("4");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntities(true, new Character[]{Characters.SPIDER_HEAD_LOLOLOSHKA, Characters.SKY_EYE});
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -33.00 103.00 -463.06 45.08 -0.45");
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.setSkyEyeLookTarget("player");
            this.thenDelay(70);
            this.thenSpeechMessage(0, "Да, пока ты сам не можешь справиться с недружелюбными существами…");
            this.addPlayerEmotion(Emotion.CONFOUNDED_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Конечно, мы с формиками тебя защитим, не волнуйся.");
            this.thenSpeechMessage(0, "Однако формики будут гораздо эффективнее, если ты сможешь их правильно организовать.");
            this.thenSpeechMessage(0, "Сам.");
            this.addPlayerEmotion(Emotion.THINKING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "А для этого нужно двигаться вперёд.");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.FORWARD));
            this.thenSpeechMessage(0, "И раз уж хранитель этого региона был побеждён…");
            this.thenSpeechMessage(0, "Ты можешь безраздельно пользоваться благами этого участка земли.");
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_TEAR);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Ты не понимаешь, о чём я?");
            this.thenSpeechMessage(0, "Сейчас увидишь.");
            this.saveState("5");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -46.34 106.16 -482.13 -966.54 -5.70");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -46.34 106.16 -482.13 -966.54 -5.70");
            this.sendWebWarningRed(
               "Сейчас будет показана катсцена, в которой происходит разблокировка первой локации. Сначала поочередно демонстрируются четыре участка в сером цвете. Затем на их месте появляются датники с зелёной травой. После этого те же четыре участка показываются снова - уже обновлённые. На монтаже это нужно оформить так: сначала показывается первый участок в сером цвете, затем - тот же участок с зелёной травой; после этого второй участок в сером и зелёном, и так далее."
            );
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.blackout(20);
            this.thenDelay(80);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -24.91 105.30 -468.49 -837.07 -0.60");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 13.53 112.85 -520.94 -855.23 -1.05");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -5.45 106.81 -395.32 -815.18 10.95");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -24.84 202.89 -439.54 185.57 38.85");
            this.sendWebWarningRed("Этот кадр вставлять не нужно, жди пока у тебя вставятся датники, а после нажми F8");
            this.importBuilding("s3_color1");
            this.importBuilding("s3_color2");
            this.importBuilding("s3_lololowka_head_remove_from_color");
            this.importBuilding("s3_lololowka_head_after_color");
            this.importBuilding("s3_simulyator_after_color");
            this.thenWaitForF8();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -46.34 106.16 -482.13 -966.54 -5.70");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -24.91 105.30 -468.49 -837.07 -0.60");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 13.53 112.85 -520.94 -855.23 -1.05");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -5.45 106.81 -395.32 -815.18 10.95");
            this.thenDelay(60);
            this.saveState("6");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -25.07 103.00 -460.25 12.38 -10.35");
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.setSkyEyeLookTarget("player");
            this.thenPerform(() -> RiddlesStorage.get(RiddlesST.S3_HEAD.getId()).setVisible(true));
            this.thenDelay(60);
            this.addPlayerEmotion(Emotion.EXPLODING_HEAD);
            this.addPlayerEmotion(Emotion.EXPLODING_HEAD);
            this.addPlayerEmotion(Emotion.EXPLODING_HEAD);
            this.thenDelay(30);
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_LAUGH});
            this.thenDelay(Animations.SKYEYE_LAUGH);
            this.sendWebWarning("Голос добродушно смеётся");
            this.thenSpeechMessage(0, "Невероятно, правда?");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "Посмотри, как красиво стало у тебя в мире.");
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_HEART_EYES);
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_HEART_EYES);
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_HEART_EYES);
            this.thenSpeechMessage(0, "Уверен, тебе не терпится всё разглядеть поближе.");
            this.thenSpeechMessage(0, "Иди, иди.");
            this.sendWebWarning("Походи, поисследуй локацию");
            this.sendWebWarning("После того, как выйдешь, например, из марио, пропиши - /script trigger 3");
            this.thenWaitForTrigger();
            this.saveState("7");
            this.blackout(30);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -11.76 104.56 -440.45 254.40 2.85");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -11.76 104.56 -440.45 254.40 2.85");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -34.65 103.00 -459.80 315.45 -0.90");
            this.setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.blackout(20);
            this.thenDelay(50);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -8.23 104.56 -442.54 350.55 2.70"}
            );
            this.createTelepornGlitchEffect(Characters.BLUE_MAN, "/execute in minecraft:overworld run tp @s -7.60 103.00 -438.90 208.80 0.30");
            this.action(
               Characters.BLUE_MAN,
               new ConfiguredAnimation[]{
                  Animations.MADMAN_LOOKING_AROUND.configure().transitionTime(0.4F).mask(ScriptUses.maskNoFace()).loopMode(LoopMode.ONCE)
               }
            );
            this.thenDelay(Animations.MADMAN_LOOKING_AROUND);
            this.action(Characters.BLUE_MAN, new IAnimationSource[]{Animations.BLUE_MAN_SCRATCHHEAD.right()});
            this.thenDelay(50);
            this.entityLookAtFromRot(Characters.BLUE_MAN, "/execute in minecraft:overworld run tp @s -7.60 103.00 -438.90 37.50 -1.20", true);
            this.thenDelay(5);
            this.thenEntityPathParallel(Characters.BLUE_MAN, new String[]{"/execute in minecraft:overworld run tp @s -22.79 103.00 -417.52 35.85 -3.90"});
            this.thenDelay(30);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -32.95 103.17 -459.35 463.50 7.80");
            this.freeEntity(Characters.BLUE_MAN, true);
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               20, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -32.95 103.17 -459.35 103.95 -11.55"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.ANGUISHED_FACE);
            this.thenDelay(20);
            this.thenEntityPathParallel(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -31.51 103.00 -456.52 -45.90 -0.90"}
            );
            this.thenDelay(5);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.LINEAR,
               new String[]{"/execute in minecraft:overworld run tp @s -30.19 103.17 -457.36 109.95 -0.75"}
            );
            this.thenDelay(50);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -30.30 104.29 -413.24 -105.90 0.60");
            this.thenClearEntityGoal(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -22.69 103.00 -420.74 41.25 -0.90");
            this.positionEntity(Characters.BLUE_MAN, "/execute in minecraft:overworld run tp @s -27.10 103.00 -414.09 -672.16 -0.15");
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SMILING_FACE_WITH_TEAR);
            this.blackout(20);
            this.thenEntityPathParallel(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -40.09 103.00 -402.94 402.90 -1.20"}
            );
            this.tdCutsceneMoveCamera(
               120,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -38.24 104.29 -405.69 238.65 -1.20"}
            );
            this.thenDelay(15);
            this.thenEntityPathParallel(Characters.BLUE_MAN, new String[]{"/execute in minecraft:overworld run tp @s -39.10 103.00 -404.11 42.75 0.15"});
            this.thenDelay(105);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.CONFOUNDED_FACE);
            this.tdCutsceneMoveCamera(
               30,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -30.85 103.36 -413.12 -109.80 6.60"}
            );
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               120,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -33.30 103.36 -410.29 -118.20 0.30"}
            );
            this.thenDelay(120);
            this.thenEntityPathParallel(Characters.BLUE_MAN, new String[]{"/execute in minecraft:overworld run tp @s -50.46 103.00 -392.23 38.55 -1.50"});
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -34.36 103.36 -409.64 403.80 -2.85"}
            );
            this.thenDelay(40);
            this.tdCutsceneMoveCamera(
               110,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -32.13 103.36 -412.25 42.90 -2.85"}
            );
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.WEARY_FACE);
            this.thenDelay(110);
            this.blackout(60);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.freeEntities(true, new Character[]{Characters.SPIDER_HEAD_LOLOLOSHKA, Characters.BLUE_MAN});
            this.blackout(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -207.94 90.11 -201.25 385.50 -0.15");
            this.positionEntity(Characters.BLUE_MAN, "/execute in minecraft:overworld run tp @s -209.04 89.00 -198.58 41.40 -1.35");
            this.thenDelay(50);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -211.97 90.11 -197.12 25.50 -0.15"}
            );
            this.thenEntityPath(Characters.BLUE_MAN, new String[]{"/execute in minecraft:overworld run tp @s -213.14 89.00 -194.24 402.15 0.90"});
            this.thenDelay(5);
            this.action(Characters.BLUE_MAN, new IAnimationSource[]{Animations.HANDS_POSE_HANDSONSIDES});
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -213.44 89.97 -191.99 -169.05 -1.50");
            this.thenDelay(10);
            this.emotion(
               Characters.BLUE_MAN,
               new ConfiguredAnimation[]{
                  Animations.MADMAN_LOOKING_AROUND.configure().transitionTime(0.4F).mask(ScriptUses.maskNoFace()).loopMode(LoopMode.ONCE)
               }
            );
            this.thenDelay(Animations.MADMAN_LOOKING_AROUND);
            this.action(Characters.BLUE_MAN, new IAnimationSource[]{Animations.SIGH_NEW});
            this.thenDelay(Animations.SIGH_NEW);
            this.thenSpeechMessage(1, "Чёрт, опять ошибка…");
            this.emotion(Characters.BLUE_MAN, new IAnimationSource[]{Animations.EMOTION_EYE_ROLLING_NEW});
            this.thenDelay(Animations.EMOTION_EYE_ROLLING_NEW);
            this.thenSpeechMessage(1, "Дурацкий телепорт.");
            this.clearChat();
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.66 90.63 -191.00 193.05 6.30"}
            );
            this.deleteTelepornGlitchEffect(Characters.BLUE_MAN);
            this.thenDelay(20);
            this.blackout(100);
            this.thenTitle(new Title(Component.literal("Спустя"), Component.literal("много времени…"), null, 20, 40, 20));
            this.tdCutscene("/execute in minecraft:overworld run tp @s -1.01 103.03 -391.54 100.51 -2.85");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -10.74 103.00 -393.07 270.16 0.00");
            this.tdCutsceneMoveCamera(
               150,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -1.01 103.03 -391.54 126.01 -2.25"}
            );
            this.thenEntityPathParallel(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -3.07 103.00 -393.01 -88.19 -1.80"}
            );
            this.thenDelay(20);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SAD_BUT_RELIEVED_FACE);
            this.thenDelay(80);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_EXHALING);
            this.thenDelay(80);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -1.67 103.03 -392.02 129.61 -1.95"}
            );
            this.action(Characters.SPIDER_HEAD_LOLOLOSHKA, new IAnimationSource[]{Animations.SPIDER_HEAD_SIT});
            this.thenDelay(Animations.SPIDER_HEAD_SIT);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.LOUDLY_CRYING_FACE);
            this.thenDelay(60);
            this.saveState("8");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -3.07 103.00 -393.01 -88.19 -1.80");
            this.setSkyEyeLookTarget("player");
            this.timeMarker("12:00", true);
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.thenDelay(60);
            this.thenSpeechMessage(0, "Почему ты так расстроен?");
            this.thenSpeechMessage(0, "Я понимаю, что другие тебе интересны, но…");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.ANOTHER));
            this.thenSpeechMessage(0, "А вдруг это существо тоже было недружелюбным?");
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Ты ведь не знаешь.");
            this.thenSpeechMessage(0, "Может быть, и хорошо, что оно ушло.");
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Понимаю, ты всё равно хотел на него посмотреть.");
            this.thenSpeechMessage(0, "Это потому, что оно так от тебя отличается?");
            this.thenSpeechMessage(0, "Оно такое большое, сильное, у него есть руки и ноги…");
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.addPlayerEmotion(Emotion.LOUDLY_CRYING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Ты тоже хочешь такое тело?");
            this.addPlayerEmotion(Emotion.CONFOUNDED_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Конечно, хочешь.");
            this.thenSpeechMessage(0, "И оно обязательно будет у тебя.");
            this.thenSpeechMessage(0, "Но запасись терпением.");
            this.addPlayerEmotion(Emotion.SAD_BUT_RELIEVED_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Для того, чтобы его получить, тебе придётся потрудиться.");
            this.thenSpeechMessage(0, "Это не так быстро.");
            this.addPlayerEmotion(Emotion.ENRAGED_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Сосредоточься пока на ближайших целях.");
            this.thenSpeechMessage(0, "Ты уверен, что полностью осмотрел свой новый раскрашенный мир?");
            this.addPlayerEmotion(Emotion.EYES);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Вот именно.");
            this.thenSpeechMessage(0, "Сначала займись тем, что можешь сделать прямо сейчас.");
            this.sendWebWarningRed("Вспомни про слова в симуляторе, пока никакие перки не покупай");
            this.saveState("9");
            this.setSkyEyeLookTarget("player");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Я вижу, ты уже начал понемногу обдумывать некоторые важные понятия.");
            this.addPlayerEmotion(Emotion.THINKING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Так держать.");
            this.addPlayerEmotion(Emotion.SLIGHTLY_SMILING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Такой большой прогресс за сегодня, а ещё и полдня не прошло.");
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_SUNGLASSES);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Хочется как-то наградить тебя.");
            this.addPlayerEmotion(Emotion.SMILING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Хм-м… что может быть лучшей наградой, чем возможность дальнейшего роста, верно?");
            this.thenSpeechMessage(0, "Именно её я тебе и предложу.");
            this.addPlayerEmotion(Emotion.THINKING_FACE);
            this.thenDelay(30);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.GROWTH));
            this.thenSpeechMessage(0, "Вижу, ты запутался.");
            this.thenSpeechMessage(0, "Давай по-простому.");
            this.thenSpeechMessage(0, "Каждый переход на следующий уровень чётко фиксирован во времени.");
            this.thenSpeechMessage(0, "Несмотря на то, что у тебя уже есть достаточно эйдосов…");
            this.thenSpeechMessage(0, "Ты не можешь перейти на третий уровень.");
            this.addPlayerEmotion(Emotion.PENSIVE_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Тебе нужно дождаться, пока подойдёт время.");
            this.thenSpeechMessage(0, "И тогда ты сможешь сдать эйдосы и перейти на следующий уровень.");
            this.thenSpeechMessage(0, "Но я вижу, что ты хочешь сейчас, правда?");
            this.addPlayerEmotion(Emotion.FOLDED_HANDS);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Ну конечно.");
            this.thenSpeechMessage(0, "И в этот раз я могу кое-что подтасовать, чтобы тебе помочь.");
            this.clearChat();
            this.dialog(
               new Dialog(3, Characters.SKY_EYE)
                  .setTalkingTo(Characters.SKY_EYE)
                  .addText("Ты согласен?")
                  .addAnswer("d(￣◇￣)b", false)
                  .addAnswer("q(︶︹︺)р", true)
            );
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Прекрасно.");
            this.thenSpeechMessage(0, "Что ж, тогда я забираю у тебя эйдосы…");
            this.addPlayerEmotion(Emotion.SMILING_FACE_WITH_TEAR);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "И перевожу тебя на третий уровень.");
            this.addPlayerEmotion(Emotion.PARTYING_FACE);
            this.thenDelay(30);
            this.thenRun(() -> CurrencyStorage.addCurrency(CurrencySystem.EIDOS_CURRENCY, -30));
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         private void part3(S3 inst) {
            this.saveState("10");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.importBuilding("s3_triangle_statue_no_triangle");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 173.26 232.80 -254.15 217.31 -1.95");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 173.26 232.80 -254.15 217.31 -1.95");
            this.positionEntity(S3.TRIANGLE_STATUE, "/execute in minecraft:overworld run tp @s 211.00 565.00 -320.00 90.00 0.00");
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.blackout(30);
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               100, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s 180.79 241.03 -265.75 217.46 -22.35"}
            );
            this.thenDelay(80);
            this.sendWebWarningOrange("Когда статуя упадет с неба на гору, на монтаже наложить звук грохота");
            this.thenEntityMove(S3.TRIANGLE_STATUE, 15.0, "/execute in minecraft:overworld run tp @s 211.00 238.00 -320.00 90.00 0.00", false);
            this.thenUseEntity(
               S3.TRIANGLE_STATUE,
               e -> {
                  Vec3 center = new Vec3(220.0, 262.0, -320.0);
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                     center.add(-5.0, 20.0, 0.0),
                     400,
                     13.0,
                     15.0,
                     13.0,
                     0.4,
                     30.0F * (0.9F + S3.rng.nextFloat() * 0.2F),
                     50,
                     new Color(90 + S3.rng.nextInt(21) - 10, 90 + S3.rng.nextInt(21) - 10, 90 + S3.rng.nextInt(21) - 10),
                     0.7F
                  );
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.LARGE_SMOKE,
                     center.add(0.0, 0.0, 0.0),
                     500,
                     10.0,
                     20.0,
                     10.0,
                     0.5,
                     10.0F * (0.9F + S3.rng.nextFloat() * 0.2F),
                     120,
                     new Color(60 + S3.rng.nextInt(21) - 10, 60 + S3.rng.nextInt(21) - 10, 60 + S3.rng.nextInt(21) - 10),
                     0.9F
                  );
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.ASH,
                     center.add(10.0, 0.0, 0.0),
                     250,
                     4.0,
                     1.0,
                     4.0,
                     0.7,
                     30.0F * (0.9F + S3.rng.nextFloat() * 0.2F),
                     100,
                     new Color(150 + S3.rng.nextInt(21) - 10, 140 + S3.rng.nextInt(21) - 10, 130 + S3.rng.nextInt(21) - 10),
                     0.3F
                  );
               }
            );
            this.importBuilding("s3_triangle_statue");
            this.freeEntity(S3.TRIANGLE_STATUE, true);
            this.tdCutsceneMoveCamera(
               140,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 153.95 242.26 -318.17 269.51 -24.30"}
            );
            this.thenDelay(170);
            this.saveState("11");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -33.16 103.00 -465.01 -33.74 -22.20");
            this.setSkyEyeLookTarget("player");
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.thenDelay(80);
            this.thenSpeechMessage(0, "Поздравляю!");
            this.thenSpeechMessage(0, "Теперь ты можешь начать обдумывать ещё больше понятий.");
            this.addPlayerEmotion(Emotion.STAR_STRUCK);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Но прежде чем ты этим займёшься, скажи…");
            this.clearChat();
            this.dialog(
               new Dialog(3, Characters.SKY_EYE)
                  .setTalkingTo(Characters.SKY_EYE)
                  .addText("Какое счастье ты желаешь сегодня?")
                  .addAnswer("Понимание", true)
                  .addAnswer("Желание", false)
                  .addAnswer("Прикосновение", true)
            );
            this.thenDelay(20);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.DESIRE));
            this.thenSpeechMessage(0, "Это достойный выбор.");
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Я в тебе не сомневался.");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "А теперь иди.");
            this.thenSpeechMessage(0, "Я знаю, что тебе очень интересно.");
            this.sendWebWarningRed(
               "Посмотри какие у тебя новые слова начали обдумываться + продолжай исследовать разблоченную локацию, досмотри оставшиеся объекты, когда досмотрел, снова проверь слова"
            );
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.thenDelay(40);
            this.saveState("12");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -20.38 103.63 -482.12 135.61 10.20");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -20.38 103.63 -482.12 135.61 10.20");
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -22.25 103.00 -484.07 377.86 -3.45");
            this.setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.blackout(20);
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               100, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -20.38 103.63 -482.12 56.86 9.75"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -22.47 103.00 -479.31 2.56 -1.65"}
            );
            this.blackout(30);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -10.80 103.59 -455.72 -192.59 5.55");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -12.77 103.00 -459.79 -47.69 0.30");
            this.thenDelay(25);
            this.tdCutsceneMoveCamera(
               80, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -10.81 103.91 -454.79 195.01 5.10"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -10.56 103.00 -457.74 -48.14 0.45"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.GRINNING_FACE_WITH_SMILING_EYES);
            this.thenDelay(50);
            this.blackout(30);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 5.06 104.59 -424.35 -116.54 18.30");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 6.98 103.00 -427.37 317.26 -3.15");
            this.thenDelay(25);
            this.tdCutsceneMoveCamera(
               60, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s 5.06 104.59 -424.35 283.81 10.20"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s 9.27 103.00 -425.79 328.36 -13.95"}
            );
            this.action(Characters.SPIDER_HEAD_LOLOLOSHKA, new IAnimationSource[]{Animations.SPIDER_HEAD_10_DEGREE_HEAD});
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.THINKING_FACE);
            this.thenDelay(50);
            this.blackout(30);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.stopAction(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -19.02 103.64 -415.46 -169.11 5.88");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -18.17 103.00 -418.57 50.72 1.50");
            this.thenDelay(25);
            this.tdCutsceneMoveCamera(
               120,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -24.92 103.65 -414.31 258.84 3.33"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SMILING_FACE_WITH_HEART_EYES);
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -23.01 103.00 -414.54 401.19 -5.52"}
            );
            this.blackout(30);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.stopAction(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -47.58 103.51 -383.17 232.73 6.03");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -45.41 103.00 -385.41 752.33 -0.27");
            this.thenDelay(25);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -48.06 103.51 -382.21 238.58 3.03"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -46.56 103.00 -382.77 754.28 -2.07"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.NEUTRAL_FACE);
            this.thenDelay(40);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -44.76 103.22 -384.16 45.98 -3.27");
            this.thenDelay(20);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -48.06 103.00 -381.02 36.98 -1.17"}
            );
            this.saveState("13");
            this.blackout(40);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.freeEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, true);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -115.76 97.20 -334.45 9.68 -0.15");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -115.76 97.20 -334.45 9.68 -0.15");
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -115.92 97.00 -332.66 40.58 -0.72")
                     .setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .tdCutsceneMoveCamera(
                        160,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -118.93 97.53 -331.08 6.98 10.20\n/execute in minecraft:overworld run tp @s -119.81 97.53 -330.10 -11.62 10.05\n"
                     )
                     .thenEntityPath(
                        Characters.SPIDER_HEAD_LOLOLOSHKA,
                        0.2,
                        "/execute in minecraft:overworld run tp @s -119.63 97.00 -328.74 47.93 -1.50\n/execute in minecraft:overworld run tp @s -119.85 97.00 -327.22 374.03 -0.30\n"
                     )
                     .addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SLIGHTLY_SMILING_FACE)
                     .tdCutsceneMoveCamera(
                        60,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -120.96 97.32 -326.21 221.63 2.85"
                     )
                     .thenDelay(80)
                     .tdCutsceneMoveCamera(
                        100, SplineType.LINEAR, InterpolationType.LINEAR, "/execute in minecraft:overworld run tp @s -120.96 97.32 -326.21 -45.52 0.30"
                     )
                     .thenHandleState(inst.createFlag("ants_run"))
                     .clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .thenEntityPathParallel(
                        Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, "/execute in minecraft:overworld run tp @s -117.26 97.00 -321.94 -42.37 0.15"
                     )
                     .thenDelay(60)
                     .addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.CONFOUNDED_FACE)
                     .thenDelay(60)
                     .tdCutscene("/execute in minecraft:overworld run tp @s -115.87 97.34 -321.36 -240.07 6.90")
                     .thenDelay(15)
                     .clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .thenDelay(15)
                     .addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.ENRAGED_FACE)
                     .thenDelay(60)
                     .clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .tdCutsceneMoveCamera(
                        110,
                        SplineType.LINEAR,
                        InterpolationType.LINEAR,
                        "/execute in minecraft:overworld run tp @s -115.87 97.34 -321.36 183.68 6.00\n/execute in minecraft:overworld run tp @s -115.87 97.34 -321.36 122.93 6.45\n"
                     )
                     .thenEntityPath(
                        Characters.SPIDER_HEAD_LOLOLOSHKA,
                        0.2,
                        "/execute in minecraft:overworld run tp @s -115.63 97.00 -324.12 -131.77 -1.80\n/execute in minecraft:overworld run tp @s -117.67 97.00 -322.02 42.83 -1.05\n"
                     )
                     .entityLookAtFromRot(
                        Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -117.13 97.00 -322.83 314.33 -0.30", true
                     )
                     .thenDelay(5)
                     .addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SAD_BUT_RELIEVED_FACE)
                     .thenDelay(60)
                     .clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA)
                     .thenDelay(40)
                     .entityLookAtFromRot(
                        Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -117.13 97.00 -322.83 40.33 -0.50", true
                     )
                     .tdCutsceneMoveCamera(
                        40,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -115.94 97.20 -321.96 67.43 -0.60"
                     )
                     .thenDelay(60)
                     .tdCutsceneMoveCamera(
                        60,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -116.45 96.98 -322.70 46.13 -4.35"
                     )
                     .thenEntityPath(Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, "/execute in minecraft:overworld run tp @s -119.54 97.00 -320.47 44.78 -1.35")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(0), "/execute in minecraft:overworld run tp @s -146.29 97.00 -342.58 -47.17 0.00")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(0), 0.5, "/execute in minecraft:overworld run tp @s -110.15 97.00 -311.70 -52.72 0.75")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(0), 0.4, "/execute in minecraft:overworld run tp @s -75.36 97.00 -286.27 -51.82 -0.30")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(1), "/execute in minecraft:overworld run tp @s -146.94 97.00 -341.75 -44.17 -1.50")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(1), 0.5, "/execute in minecraft:overworld run tp @s -110.77 97.00 -310.46 -54.22 0.60")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(1), 0.4, "/execute in minecraft:overworld run tp @s -75.91 97.00 -285.26 -51.97 -0.30")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(2), "/execute in minecraft:overworld run tp @s -146.48 97.00 -343.53 -43.72 -0.60")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(2), 0.5, "/execute in minecraft:overworld run tp @s -111.16 97.00 -312.07 -52.72 -0.30")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(2), 0.4, "/execute in minecraft:overworld run tp @s -75.81 97.00 -286.79 -51.52 -0.90")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(3), "/execute in minecraft:overworld run tp @s -147.27 97.00 -342.75 -45.97 -1.05")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(3), 0.5, "/execute in minecraft:overworld run tp @s -112.01 97.00 -310.93 -54.37 -1.05")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(3), 0.4, "/execute in minecraft:overworld run tp @s -76.97 97.00 -285.30 -51.97 -0.30")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(4), "/execute in minecraft:overworld run tp @s -147.01 97.00 -343.80 -46.87 -0.30")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(4), 0.5, "/execute in minecraft:overworld run tp @s -111.97 97.00 -312.46 -53.77 -1.35")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(4), 0.4, "/execute in minecraft:overworld run tp @s -76.61 97.00 -286.65 -52.42 -2.40")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .positionEntity(S3.ANTS.get(5), "/execute in minecraft:overworld run tp @s -147.74 97.00 -343.14 -48.37 -1.50")
                     .thenDelay(15)
                     .thenEntityPath(S3.ANTS.get(5), 0.5, "/execute in minecraft:overworld run tp @s -112.92 97.00 -311.39 -55.42 -2.85")
                     .thenWaitForFlag("ants_run")
                     .thenEntityPath(S3.ANTS.get(5), 0.4, "/execute in minecraft:overworld run tp @s -77.52 97.00 -286.07 -52.72 -3.45")
                     .build()
               }
            );
            this.saveState("14");
            this.blackout(50);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.thenUsePlayer(p -> p.setGameMode(GameType.SPECTATOR));
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.freeEntities(
               true,
               new Character[]{
                  Characters.SPIDER_HEAD_LOLOLOSHKA, S3.ANTS.get(0), S3.ANTS.get(1), S3.ANTS.get(2), S3.ANTS.get(3), S3.ANTS.get(4), S3.ANTS.get(5)
               }
            );
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -141.64 89.62 -262.26 184.28 7.65");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -141.64 89.62 -262.26 184.28 7.65");
            this.positionEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -140.80 89.00 -267.11 -315.07 -0.30");
            this.setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.thenDelay(40);
            this.tdCutsceneMoveCamera(
               100,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -141.64 89.62 -262.26 149.63 9.60"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -143.54 89.00 -264.36 44.93 -0.30"}
            );
            this.deleteTelepornGlitchEffect(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.thenDelay(15);
            this.blackout(40);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -219.01 87.09 -189.52 -125.61 -2.40");
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -216.71 89.33 -191.60 240.09 1.35"}
            );
            this.thenDelay(60);
            this.createTelepornGlitchEffect(Characters.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -210.53 89.00 -195.40 401.93 0.30");
            this.setSkyEyeLookTarget(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.19 89.19 -192.19 250.89 1.20"}
            );
            this.thenEntityPath(
               Characters.SPIDER_HEAD_LOLOLOSHKA, 0.2, new String[]{"/execute in minecraft:overworld run tp @s -213.49 89.00 -192.53 -317.32 2.70"}
            );
            this.thenDelay(5);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_EXHALING);
            this.thenDelay(60);
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.overridePredefinedAnimation(
               Characters.SPIDER_HEAD_LOLOLOSHKA, MovementActionType.IDLE, Animations.SPIDER_HEAD_IDLE.configure().transitionTime(0.4F).speed(0.5F)
            );
            this.action(Characters.SPIDER_HEAD_LOLOLOSHKA, new IAnimationSource[]{Animations.SPIDER_HEAD_SIT});
            this.thenDelay(Animations.SPIDER_HEAD_SIT);
            this.thenSpeechMessage(0, "Ты всё ещё грустишь из-за того существа?");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.PENSIVE_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Это нормально.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.35 89.19 -190.92 -173.76 -2.10"}
            );
            this.thenSpeechMessage(0, "Мы смотрим на других и сравниваем себя с ними.");
            this.thenSpeechMessage(0, "И это может дать нам мотивацию трудиться и развиваться.");
            this.thenSpeechMessage(0, "Однако иногда получается всё наоборот.");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.NEUTRAL_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Мы сравниваем себя с другими и впадаем в уныние.");
            this.thenSpeechMessage(0, "У нас опускаются руки.");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.CONFOUNDED_FACE);
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.35 88.84 -191.30 -169.11 -14.70"}
            );
            this.thenSpeechMessage(0, "Подумай: какой смысл сравнивать себя с таким большим существом?");
            this.thenSpeechMessage(0, "Хочешь я скажу тебе, какого оно уровня?");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.EYES);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Пятьдесят первого.");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_SCREAMING_IN_FEAR);
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.29 88.84 -192.33 -72.96 -10.65"}
            );
            this.thenSpeechMessage(0, "Понимаешь, какая огромная между вами разница?");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.WEARY_FACE);
            this.thenDelay(30);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.DIFFERENCE));
            this.thenDelay(80);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -214.82 88.84 -192.56 -99.66 -10.80"}
            );
            this.thenSpeechMessage(0, "Зачем ты сравниваешь себя с ним?");
            this.thenSpeechMessage(0, "Он уже очень, очень давно появился.");
            this.thenSpeechMessage(0, "Конечно, у него было много времени, чтобы дойти до такого высокого уровня.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.33 89.02 -192.57 -99.66 -4.20"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SMILING_FACE_WITH_TEAR);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "А у тебя этого времени не было.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -214.14 89.35 -190.45 -157.71 5.10"}
            );
            this.thenSpeechMessage(
               0,
               () -> Component.literal("Тебе нужно сравнивать себя ")
                     .append(Component.literal("с самим собой").withStyle(ChatFormatting.ITALIC))
                     .append(Component.literal("."))
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.EXPLODING_HEAD);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Да, тебе пока совсем трудно понять такое…");
            this.thenSpeechMessage(0, "Ничего, совсем скоро ты вспомнишь об этом разговоре и всё осознаешь.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.67 88.78 -191.23 -158.76 -15.90"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.ENRAGED_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Не злись.");
            this.thenSpeechMessage(0, "Ты думаешь, это моя вина?");
            this.thenSpeechMessage(0, "Думаешь, в моих силах дать тебе сразу всё?");
            this.thenSpeechMessage(0, "И сильное тело, и блистательный разум, и железную волю?");
            this.thenSpeechMessage(0, "Нет, это совершенно от меня не зависит.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -214.52 88.81 -191.98 -120.21 -15.15"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_WITH_RAISED_EYEBROW);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Я говорю правду: не зависит.");
            this.thenSpeechMessage(0, "Я здесь для того, чтобы присматривать за тобой.");
            this.thenSpeechMessage(0, "И помогать тебе в сложных ситуациях.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.01 89.03 -191.58 -123.36 -4.65"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.FACE_EXHALING);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Не стоит расстраиваться из-за того, что мы с формиками не можем сделать всё за тебя.");
            this.thenSpeechMessage(0, "Невозможно иметь сильное тело, если ты не вложил в него много труда.");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.NEUTRAL_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Невозможно стать проницательным, если не тренировать постоянно свой ум.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -212.63 89.43 -190.77 -192.81 4.65"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.MELTING_FACE);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "И настолько же невозможно проснуться однажды утром существом с железной волей…");
            this.thenSpeechMessage(0, "…Когда ещё вчера тебя повсюду носили формики.");
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.01 88.92 -191.32 178.14 -8.70"}
            );
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.SMILING_FACE_WITH_TEAR);
            this.thenDelay(30);
            this.thenSpeechMessage(0, "Это в принципе невозможно, ни для кого.");
            this.thenSpeechMessage(0, "И это не я придумал.");
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -211.70 89.02 -193.17 53.34 -3.90"}
            );
            this.thenSpeechMessage(0, "Мир, он… таков.");
            this.thenSpeechMessage(0, "И мы ничего не можем с этим сделать.");
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.92 88.63 -194.45 -5.16 -24.90"}
            );
            this.thenDelay(30);
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.PLEADING_FACE);
            this.thenDelay(30);
            this.sendWebWarning("Голос вздыхает");
            this.action(Characters.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SIGH});
            this.thenDelay(Animations.SKYEYE_SIGH);
            this.thenSpeechMessage(0, "Наберись терпения.");
            this.stopAction(Characters.SKY_EYE);
            this.thenSpeechMessage(0, "Не буду врать: тебе понадобится много, очень много терпения.");
            this.thenSpeechMessage(0, "Как и всем остальным.");
            this.addCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA, Emotion.THINKING_FACE);
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -213.65 88.85 -194.41 16.59 -8.25"}
            );
            this.thenDelay(20);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.PATIENCE));
            this.thenDelay(80);
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.07 89.06 -192.92 -87.06 -6.75"}
            );
            this.thenSpeechMessage(0, "Но я уверен, в итоге у тебя всё получится.");
            this.thenSpeechMessage(0, "Не сдавайся.");
            this.thenSpeechMessage(0, "Иди к своей цели каждый день, шаг за шагом.");
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -215.87 89.34 -192.73 -91.11 1.80"}
            );
            this.thenSpeechMessage(0, "Ты и не заметишь, как станешь самым сильным.", false);
            this.thenDelay(20);
            this.thenDelay(30);
            this.thenWaitForF8();
            this.thenDelay(20);
            this.clearChat();
            this.blackout(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -212.71 89.89 -189.36 -184.26 13.20");
            this.action(Characters.SPIDER_HEAD_LOLOLOSHKA, new ConfiguredAnimation[]{Animations.SPIDER_HEAD_TIRED_POSE.configure().transitionTime(0.0F)});
            this.thenDelay(80);
            this.blackout(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -213.41 89.96 -195.82 -345.21 11.25");
            this.thenDelay(60);
            this.tdCutsceneMoveCamera(
               60, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -213.41 89.96 -195.82 5.64 -48.15"}
            );
            this.thenDelay(80);
            this.tdCutsceneMoveCamera(
               200, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -213.45 93.32 -191.68 5.34 -48.15"}
            );
            this.thenDelay(100);
            this.saveState("15");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.endTdCutscene();
            this.clearCharacterEmotion(Characters.SPIDER_HEAD_LOLOLOSHKA);
            this.freeEntity(Characters.SPIDER_HEAD_LOLOLOSHKA, true);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -213.49 89.00 -192.53 -317.32 2.70");
            this.setSkyEyeLookTarget("player");
            this.thenUsePlayer(p -> p.setGameMode(GameType.SURVIVAL));
            this.timeMarker("15:00", true);
            this.sendWebWarning("Скрипт всё");
            this.thenWhenPlayerExitBox(new AABB(-232.0, 86.0, -176.0, -193.0, 98.0, -208.0));
            this.teleportWithGlitchEffect(3, "/execute in minecraft:overworld run tp @s -24.24 103.00 -413.52 -167.64 -1.05");
         }

         private ScriptPerformerBuilder teleportWithGlitchEffect(int id, String location) {
            return this.createTicker("teleport_with_glitch_delete_" + id, p -> {
               if (p != null) {
                  Random random = new Random();
                  EntityDimensions dim = p.getType().getDimensions();
                  Vec3 position = p.position();
                  double expansion = 0.2;
                  double halfWidth = (double)dim.width / 2.0 + expansion;
                  double minX = position.x - halfWidth;
                  double maxX = position.x + halfWidth;
                  double minY = position.y;
                  double maxY = position.y + (double)dim.height + expansion;
                  double minZ = position.z - halfWidth;
                  double maxZ = position.z + halfWidth;

                  for (int i = 0; i < 2; i++) {
                     Location particleLocation = new Location(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY), random.nextDouble(minZ, maxZ));
                     ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
                  }
               }
            }).thenDelay(20).teleportPlayer(location).deleteTicker("teleport_with_glitch_delete_" + id).thenDelay(15);
         }
      })
      .build(this::registerMainPerformer);

   private static IAttackChain<BaseMob> MUD_BOSS_PATTERN() {
      return FightAttacks.<BaseMob>createStagedFight()
         .addAttack(1.0, mudBossAttackNear(10, TargetSelector.playerSelector(), 0.5F))
         .handleEvent(
            EventType.LIVING_HURT_DIRECT,
            (evt, ctx) -> {
               if (evt.getEntity() == ctx.getEntity() && ctx.getEntity().getImpact() <= 0.0F) {
                  ctx.getManager().addFlag("default_flags$STUN");
                  ctx.getManager().cancel();
                  ctx.getManager()
                     .offerAttack(
                        CommonAttacks.stun(60, TargetSelector.playerSelector(), Animations.ZOMBIE_STUN_1, Animations.ZOMBIE_STUN_2, Animations.ZOMBIE_STUN_3)
                     );
               }
            }
         )
         .build();
   }

   public S3(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this)
         .appendBuilder(
            ScriptUses.predownloadStructures(
               "s3_pit_stage_1",
               "s3_pit_stage_2",
               "s3_pit_stage_3",
               "s3_color1",
               "s3_color2",
               "s3_color1_gray",
               "s3_color2_gray",
               "s3_triangle_statue",
               "s3_triangle_statue_just_triangle",
               "s3_triangle_statue_no_triangle"
            )
         )
         .appendBuilder(ScriptUses.createStructureImportSetup())
         .build();
   }

   private void spawnEndRodParticles(Player p, Location loc) {
      if (p.tickCount % 2 == 0) {
         ParticleHelper.spawnParticles(p.level(), ParticleTypes.END_ROD, loc.x, loc.y, loc.z, 2, 0.0, 0.0, 0.0, 0.0);
      }
   }

   private static <T extends BaseMob, U extends LivingEntity> Attack<T> mudBossAttackNear(int cooldown, TargetSelector<T, U> targetSelector, float damage) {
      return Attack.<T>builder(
            "mudBossAttackNear",
            cooldown,
            (manager, context) -> {
               LivingEntity e = TargetSelectorHelper.nearest(context, targetSelector);
               if (e == null) {
                  return true;
               } else {
                  manager.tick();
                  manager.addFlag("default_flags$ATTACK");
                  manager.doOnce(
                     "mud_boss_attack_anim",
                     () -> {
                        context.getState().stopBehaviour(BehaviourType.MOVEMENT);
                        ((BaseMob)context.getEntity()).startAnimation("ANIMATION_9", Animations.ZOMBIE3_ATTACK);
                        context.getState().getData().setLookTarget(new EntityTarget(e));
                        ((BaseMob)context.getEntity())
                           .setAttackAnimAction(
                              new AttackAnimActionBuilder(
                                    Animations.ZOMBIE3_ATTACK.configure(), List.of("LeftArm", "RightArm"), (AnimatedEntityLikeHumanoid)context.getEntity()
                                 )
                                 .withInflateV3(new Vec3(2.0, 3.0, 2.0))
                                 .build()
                           );
                     }
                  );
                  ((BaseMob)context.getEntity()).lookAt(Anchor.EYES, e.position());
                  ((BaseMob)context.getEntity()).lookAt(Anchor.FEET, e.position());
                  if (((BaseMob)context.getEntity()).getAttackAnimAction() != null && ((BaseMob)context.getEntity()).getAttackAnimAction().is_active()) {
                     float animTime = ((BaseMob)context.getEntity()).getAttackAnimAction().getTime_anim();
                     if (animTime >= 15.0F && animTime < 100.0F && !manager.contains("Damage") && Collider.isCollidingBox(context.getEntity(), e)) {
                        e.hurt(
                           new DamageSource(((BaseMob)context.getEntity()).damageSources().mobAttack((LivingEntity)context.getEntity()).typeHolder()), damage
                        );
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
         .addCondition(context -> TargetSelectorHelper.hasBetween(context, targetSelector, 3.0, 0.0))
         .tryCompleteConditions(mudBossSmartMovement(targetSelector, 0.25F, 100))
         .build();
   }

   private static <T extends BaseMob, U extends LivingEntity> AttackAction<T> mudBossSmartMovement(
      TargetSelector<T, U> targetSelector, float speed, int timeTryComplete
   ) {
      return (manager, context) -> {
         T entity;
         manager.tick();
         U nearest = TargetSelectorHelper.nearest(context, targetSelector);
         entity = (T)context.getEntity();
         label41:
         if (nearest != null && !nearest.isSpectator()) {
            if (nearest instanceof Player p && p.isCreative()) {
               break label41;
            }

            double distance = (double)entity.distanceTo(nearest);
            if (distance > 10.0) {
               entity.setTarget(null);
               lookInMoveDirection(entity);
               randomWalk(entity, (double)speed);
               return false;
            }

            if (distance <= 3.0) {
               entity.setTarget(nearest);
            }

            if (entity.getTarget() != null) {
               if (distance <= 10.0 && distance > 1.0) {
                  entity.lookAt(nearest, 360.0F, 360.0F);
                  entity.getNavigation().moveTo(nearest, (double)speed);
                  return false;
               }

               if (distance <= 1.0) {
                  entity.setDeltaMovement(Vec3.ZERO);
                  entity.getNavigation().stop();
                  entity.lookAt(nearest, 360.0F, 360.0F);
                  return true;
               }
            } else {
               lookInMoveDirection(entity);
               randomWalk(entity, (double)speed);
            }

            manager.addTimer("end", timeTryComplete);
            if (manager.containsCompleted("end")) {
               entity.getNavigation().stop();
               entity.setDeltaMovement(Vec3.ZERO);
               return true;
            }

            return false;
         }

         lookInMoveDirection(entity);
         randomWalk(entity, (double)speed);
         return false;
      };
   }

   private static <T extends BaseMob> void lookInMoveDirection(T entity) {
      Vec3 motion = entity.getDeltaMovement();
      if (motion.lengthSqr() > 1.0E-4) {
         Vec3 lookTarget = entity.position().add(motion.normalize().scale(2.0));
         entity.getLookControl().setLookAt(lookTarget.x, entity.getY() + (double)entity.getEyeHeight(), lookTarget.z);
      } else {
         entity.getLookControl()
            .setLookAt(
               entity.getX() + Math.cos(Math.toRadians((double)entity.getYRot())),
               entity.getY() + (double)entity.getEyeHeight(),
               entity.getZ() + Math.sin(Math.toRadians((double)entity.getYRot()))
            );
      }
   }

   private static <T extends BaseMob> void randomWalk(T entity, double speed) {
      if (entity.getNavigation().isDone()) {
         RandomSource r = entity.getRandom();
         double dx = r.nextDouble() * 16.0 - 8.0;
         double dz = r.nextDouble() * 16.0 - 8.0;
         double dy = r.nextDouble() * 3.0 - 1.5;
         double tx = entity.getX() + dx;
         double ty = Math.max(0.0, entity.getY() + dy);
         double tz = entity.getZ() + dz;
         entity.getNavigation().moveTo(tx, ty, tz, speed);
      }

      if (!entity.getNavigation().isDone()) {
         entity.setZza(1.0F);
         entity.setSpeed((float)speed);
      }
   }
}
