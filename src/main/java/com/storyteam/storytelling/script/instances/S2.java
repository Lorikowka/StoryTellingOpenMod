package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.particle.st_particle.STParticleData;
import com.storyteam.storytelling.client.particle.st_particle.STParticleDataBuilder;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.content.blocks.base.RotateBlockWithCustomShape;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.content.entities.GlitchCloudEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import com.storyteam.storytelling.system.simulator.main_page.client.gui.MainTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.system.time.core.TimeController;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.util.java.Cast;

@Script(2)
public class S2 extends ScriptInstance {
   private static final CharacterList<AntEntity> ANTS = CharactersST8.ANT_FAC.createCharacterList("s2_ant", 5);
   private static Location cloudStartLocation = new Location("/execute in minecraft:overworld run tp @s -33.26 106.91 -451.92 0.93 0.19");
   private static final Character<AnimatedEntity> NEON_FLOWER = Character.builder("s2_neon_flower", Cast.constant(EntitiesST.NEON_FLOWER))
      .name("Цветок")
      .build();
   private static final Character<AnimatedEntity> NEON_STONE = Character.builder("s2_neon_stone", Cast.constant(EntitiesST.NEON_STONE)).name("Камень").build();
   private static final Character<AnimatedEntity> NEON_MUSHROOM = Character.builder("s2_neon_mushroom", Cast.constant(EntitiesST.NEON_MUSHROOM))
      .name("Гриб")
      .build();
   private static final Character<AnimatedEntity> NEON_GRASS = Character.builder("s2_neon_grass", Cast.constant(EntitiesST.NEON_GRASS)).name("Трава").build();
   private static final Character<AnimatedEntity> NEON_FLOWER_OTHER = Character.builder("s2_neon_flower_other", Cast.constant(EntitiesST.NEON_FLOWER_OTHER))
      .name("Цветок")
      .build();
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(1)
         private void main1() {
            this.saveState("1");
            this.positionEntity(S2.NEON_FLOWER, "/execute in minecraft:overworld run tp @s -5.50 103.00 -446.50 0.00 0.00");
            this.entitySit(S2.NEON_FLOWER);
            this.positionEntity(S2.NEON_STONE, "/execute in minecraft:overworld run tp @s -61.50 103.00 -469.50 -90.00 0.00");
            this.entitySit(S2.NEON_STONE);
            this.positionEntity(S2.NEON_MUSHROOM, "/execute in minecraft:overworld run tp @s 17.50 105.00 -374.50 0.00 0.00");
            this.entitySit(S2.NEON_MUSHROOM);
            this.positionEntity(S2.NEON_GRASS, "/execute in minecraft:overworld run tp @s -5.50 103.00 -408.50 0.00 0.00");
            this.entitySit(S2.NEON_GRASS);
            this.ensureScriptActive(true, new int[]{-102});
            this.thenSetSpeechEntity(0, "???", "sky_eye");
            this.thenPerform(() -> FragmentInstanceManager.removeActive(FragmentsST.ME));
            this.setTime(0, "12:00");
            this.thenPerform(() -> TimeController.get().lockTime());
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -8.00 103.00 -515.00 -134.50 10.00");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_OPEN});
            this.sendWebWarning("Начало по F8");
            this.thenWaitForF8();
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_CLOSE});
            this.thenDelay(Animations.SKYEYE_CLOSE);
            this.setSkyEyeLookTarget("player");
            this.thenDelay(20);
            this.thenSpeechMessage(0, "И снова здравствуй.");
            this.thenSpeechMessage(0, "Я обещал вернуться — и вернулся.");
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Тебе удалось что-то сделать за время, пока меня не было?");
            this.playerEmotion(new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenSpeechMessage(0, "Ничего?");
            this.stopAction(CharactersST8.SKY_EYE, 0.4F);
            this.thenSpeechMessage(0, "Конечно, самому тебе тяжело.");
            this.thenSpeechMessage(0, "Давай я помогу.");
            this.thenSpeechMessage(0, "Почему бы тебе не исследовать мир вокруг тебя?");
            this.thenSpeechMessage(0, "Я думаю, найдётся что-то новое…");
            this.sendWebWarning("Иди к траве");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -26.57 103.00 -498.54 -534.84 -0.35", 6.0);
            this.thenSpeechMessage(0, "Снова ты проявляешь смекалку…");
            this.thenSpeechMessage(0, "Если и тут справишься, то я тебя награжу.", false);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         public void part2(S2 inst) {
            this.sendWebWarning("Ты можешь повзаимодействовать с травой на ПКМ");
            this.saveState("2");
            this.thenAwaitBlockClick("/setblock -27 103 -499 minecraft:calcite");
            this.saveState("3");
            this.blackout(60);
            this.importBuilding("s2_bridge_raw");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -145.71 31.00 -241.58 -1890.06 -1.49");
            this.thenHandleState(S2.this.createFlag("green_room"));
            this.sendWebWarning("Тебе нужно повернуть все зелёные блоки так, чтобы полоски на них соединились, и мурашки смогли прибежать друг к другу");
            this.positionEntity(S2.ANTS.get(0), "/execute in minecraft:overworld run tp @s -138.51 31.13 -245.19 -2160.08 -1.04");
            this.positionEntity(S2.ANTS.get(1), "/execute in minecraft:overworld run tp @s -138.49 31.13 -237.24 -1980.23 -0.44");
            this.entitySit(S2.ANTS.get(0));
            this.entitySit(S2.ANTS.get(1));
            this.sendWebWarning("Мурашки должны грустно пищать, пока ты не повернул все блоки правильно");
            this.emotion(S2.ANTS.get(0), new IAnimationSource[]{Animations.ANT_SAD});
            this.emotion(S2.ANTS.get(1), new IAnimationSource[]{Animations.ANT_SAD});
            this.thenWaitUntil(() -> {
               AABB box = new AABB(new BlockPos(-139, 30, -239), new BlockPos(-139, 32, -245)).inflate(1.0);
               return S2.this.isGreenBridgeDone(ScriptUtils.getLevel(), box);
            });
            this.sendWebWarning("Мурашки должны пищать на монтаже, когда бегут друг к другу");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .emotion(S2.ANTS.get(0), Animations.EMOTION_HAPPY_NEW)
                     .thenEntityPath(S2.ANTS.get(0), 0.4, "/execute in minecraft:overworld run tp @s -138.40 31.13 -241.97 -0.19 0.60")
                     .action(S2.ANTS.get(0), Animations.BODY_ENDANIMATED_HAPPY_JUMP)
                     .thenDelay(Animations.BODY_ENDANIMATED_HAPPY_JUMP.configure().speed(0.5F))
                     .stopAction(S2.ANTS.get(0))
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .emotion(S2.ANTS.get(1), Animations.EMOTION_HAPPY_NEW)
                     .thenEntityPath(S2.ANTS.get(1), 0.38, "/execute in minecraft:overworld run tp @s -138.49 31.13 -240.99 179.81 -0.15")
                     .action(S2.ANTS.get(1), Animations.BODY_ENDANIMATED_HAPPY_JUMP)
                     .thenDelay(Animations.BODY_ENDANIMATED_HAPPY_JUMP.configure().speed(0.5F))
                     .stopAction(S2.ANTS.get(0))
                     .build()
               }
            );
            this.saveState("4");
            this.blackout(60);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -25.07 103.00 -500.16 37.91 3.15");
            this.thenDelay(60);
            this.thenRun(() -> CurrencyStorage.addEidos(5));
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         public void part3(S2 inst) {
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Неплохо, неплохо.");
            this.thenSpeechMessage(0, "Ты снова заработал эйдосы.");
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Что такое эйдосы?");
            this.stopAction(CharactersST8.SKY_EYE, 0.4F);
            this.thenSpeechMessage(0, "Эйдосы — это то, благодаря чему идеи могут обретать конкретную форму.");
            this.thenSpeechMessage(0, "Но тебе пока такое понять сложно.");
            this.thenSpeechMessage(0, "Давай по-другому: эйдосы — это то, что необходимо для развития твоего мира.");
            this.thenSpeechMessage(0, "И для твоего собственного развития.");
            this.thenSpeechMessage(0, "Благодаря эйдосам ты сможешь раскрасить этот серый мир.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_HEART_EYES});
            this.thenSpeechMessage(0, "А также сам стать большим и сильным.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenSpeechMessage(0, "Эйдосы очень важны.");
            this.thenSpeechMessage(0, "Скоро ты увидишь насколько.");
            this.thenSpeechMessage(0, "А пока… хочешь получить свою особую награду?");
            this.playerEmotion(new Emotion[]{Emotion.STAR_STRUCK});
            this.thenSpeechMessage(0, "Я помню, я обещал.");
            this.thenSpeechMessage(0, "Думаю, мы можем кое-что тебе разблокировать.");
            this.thenSpeechMessage(0, "Но ты должен сам найти, где это.");
            this.saveState("5");
            this.sendWebWarning("Иди к симулятору");
            this.thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -20.52 104.00 -474.59 -1.54 -0.15", 18.0);
            this.thenSpeechMessage(0, "Ты двигаешься в правильном направлении.");
            this.thenSpeechMessage(0, "Что ж, думаю, лучше спросить у тебя самого…");
            this.sendWebWarning("Выбираем вариант “Я”, потому что “А вдруг там можно разблочить тело”");
            this.dialog(
               new Dialog(2, CharactersST8.SKY_EYE)
                  .setTalkingTo("player")
                  .addText("…Какую кнопку ты хочешь разблокировать в этот раз?")
                  .addAnswer("“Я”", false)
                  .addAnswer("“Мир”")
            );
            this.thenSpeechMessage(0, "Я понял твой ход мыслей.");
            this.thenSpeechMessage(0, "Точнее сказать, порыв… мыслей у тебя пока нет.");
            this.thenSpeechMessage(0, "Я готов разблокировать тебе кнопку “Я”.");
            this.thenSpeechMessage(0, "За все эйдосы, которые у тебя есть.");
            this.playerEmotion(new Emotion[]{Emotion.FEARFUL_FACE});
            this.thenSpeechMessage(0, "Да, только так.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenSpeechMessage(0, "Друг мой, прости, но ты должен понять…");
            this.thenSpeechMessage(0, "Мы с формиками всего лишь помогаем тебе.");
            this.thenSpeechMessage(0, "Мы всегда на твоей стороне и всегда протянем руку помощи, да, но…");
            this.thenSpeechMessage(0, "Мы не можем всё делать за тебя.");
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenSpeechMessage(0, "Тебе нужно учиться брать на себя ответственность — постепенно, но нужно.");
            this.thenSpeechMessage(0, "Конечно, ты не знаешь, что такое ответственность.");
            this.thenSpeechMessage(0, "Ничего, скоро поймёшь.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(4)
         public void part4(S2 inst) {
            this.thenSpeechMessage(0, "Итак, ты согласен?");
            this.dialog(
               new Dialog(2, CharactersST8.SKY_EYE)
                  .setTalkingTo("player")
                  .addText("Итак, ты согласен?")
                  .addAnswer("[ d(￣◇￣)b ]", false)
                  .addAnswer("[ q(︶︹︺)р ]")
            );
            this.thenRun(() -> CurrencyStorage.setEidos(0));
            this.setSimulatorBlocking(MainTabType.ME, false);
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Это похвальное решение.");
            this.thenSpeechMessage(0, "Молодец.");
            this.stopAction(CharactersST8.SKY_EYE, 0.4F);
            this.thenSpeechMessage(0, "Я вижу, что ты готов постоянно расти и развиваться.");
            this.thenSpeechMessage(0, "Иди посмотри, что ты разблокировал.");
            this.saveState("6");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.sendWebWarning("Иди к симулятору, тыкни на него и посмотри чо как. По выходу из симулятора скрипт продолжится");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenDelay(20);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.thenSpeechMessage(0, "Ты доволен своей работой?");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenSpeechMessage(0, "Ты должен гордиться собой.");
            this.thenSpeechMessage(0, "Очень приятно, когда твой труд приносит плоды, правда?");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_TEAR});
            this.thenSpeechMessage(0, "Ничего, не печалься — эйдосы ты ещё заработаешь…");
            this.thenSpeechMessage(0, "Смотри, а это что такое?");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(5)
         public void part5(S2 inst) {
            this.saveState("7");
            this.blackout(60);
            this.importBuilding("s2_cloud_puddle_no");
            this.thenUsePlayer(player -> {
               CurrencyStorage.setEidos(0);
               S2.cloudStartLocation = new Location("/execute in minecraft:overworld run tp @s -33.26 106.91 -451.92 0.93 0.19");
            });
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_INTEREST});
            this.tdCutscene("/execute in minecraft:overworld run tp @s -18.39 103.63 -469.61 -127.77 11.44");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -16.21 103.00 -471.21 -340.84 -1.80");
            this.thenDelay(80);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -14.15 103.63 -473.38 32.58 -7.91");
            this.setSkyEyeLookTarget("/execute in minecraft:overworld run tp @s 24.32 111.00 -523.56 -292.32 3.49");
            this.entityLookAt(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, CharactersST8.SKY_EYE);
            this.thenDelay(20);
            this.createTicker("cloud_glitch_create", player -> {
               Random random = new Random();
               Location position = new Location(S2.cloudStartLocation);
               EntityDimensions dim = EntityDimensions.fixed(4.0F, 2.0F);
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
            });
            this.setSkyEyeLookTarget(S2.cloudStartLocation.toCommandString());
            this.tdCutsceneMoveCamera(60, SplineType.CATMULLROM, new String[]{"/execute in minecraft:overworld run tp @s -16.83 103.41 -473.61 22.68 -3.56"});
            this.thenDelay(20);
            this.entityLookAt(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, S2.cloudStartLocation.toCommandString(), true);
            this.thenDelay(50);
            this.deleteTicker("cloud_glitch_create");
            this.positionEntity(CharactersST8.GLITCH_CLOUD, S2.cloudStartLocation.toCommandString());
            this.entitySit(CharactersST8.GLITCH_CLOUD);
            this.thenUseEntity(CharactersST8.GLITCH_CLOUD, cloud -> {
               cloud.setRadialBuffMode(false);
               cloud.setRaining(false);
               cloud.setBuffRadius(12.0);
            });
            this.thenDelay(30);
            this.thenUseEntity(CharactersST8.GLITCH_CLOUD, cloud -> cloud.setRaining(true));
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(10);
            this.setSkyEyeLookTarget("/execute in minecraft:overworld run tp @s -16.21 103.00 -471.21 -340.84 -1.80");
            this.stopAction(CharactersST8.SKY_EYE, 0.4F);
            this.thenSpeechMessage(0, "Какое интересное атмосферное явление.");
            this.tdCutsceneMoveCamera(60, SplineType.CATMULLROM, new String[]{"/execute in minecraft:overworld run tp @s -18.55 103.41 -470.98 -93.12 2.89"});
            this.thenDelay(20);
            this.entityLookAt(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, CharactersST8.SKY_EYE);
            this.thenSpeechMessage(0, "Подойди ближе, не бойся: оно совершенно безопасно.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(6)
         public void part6(S2 inst) {
            this.saveState("8");
            this.blackout(60);
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -16.21 103.00 -471.21 -340.84 -1.80");
            this.setSkyEyeLookTarget("player");
            this.sendWebWarning("Подойди к тучке и встань под неё, тебе начнут начисляться эйдосы");
            this.thenWaitUntil(() -> CurrencyStorage.getEidos() >= 13);
            this.createTicker(
               "glitch_wind",
               p -> {
                  Entity e = CharactersST8.GLITCH_CLOUD.repositoryEntity();
                  if (e != null) {
                     Vec3 look = e.getLookAngle().normalize().scale(0.2);
                     Color[] colors = new Color[]{
                        new Color(0, 241, 255, 85), new Color(0, 111, 166, 85), new Color(250, 18, 249, 85), new Color(142, 0, 211, 85), new Color(0, 0, 0, 85)
                     };

                     for (int i = 0; i < 20; i++) {
                        double x = e.getX() + new Random().nextDouble(-8.0, 8.0);
                        double y = e.getY() - 3.0 + new Random().nextDouble(0.0, 8.0);
                        double z = e.getZ() + new Random().nextDouble(-8.0, 8.0);
                        Color randomColor = colors[new Random().nextInt(colors.length)];
                        STParticleData quad = new STParticleDataBuilder(
                              new STDefaultOptions(0.1F * (1.0F + new Random().nextFloat()), 30, randomColor, 1.0F, false, true, false, false),
                              ParticleTypesST.QUAD_TINT_U
                           )
                           .setScalingOptions(5, 25)
                           .setAlphaOptions(5, 25)
                           .build();
                        ParticleHelper.spawnDirectedParticle(e.level(), quad, x, y, z, look.x, look.y, look.z);
                     }
                  }
               }
            );
            this.sendWebWarning("В момент отлёта тучки на монтаже нужно наложить звук ветра. Беги за тучкой, пока она не пропадёт");
            this.thenDelay(10);
            this.thenUseEntity(CharactersST8.GLITCH_CLOUD, cloud -> cloud.setRadialBuffMode(true));
            this.thenEntityMoveNoPhysics(CharactersST8.GLITCH_CLOUD, 0.25, "/execute in minecraft:overworld run tp @s -33.31 106.91 -420.44 0.93 0.19", false);
            this.thenEntityMoveNoPhysics(CharactersST8.GLITCH_CLOUD, 0.5, "/execute in minecraft:overworld run tp @s -33.34 106.91 -395.36 0.93 0.04", false);
            this.createTicker("cloud_glitch_remove", player -> {
               GlitchCloudEntity cloud = S2.this.getEntity(CharactersST8.GLITCH_CLOUD);
               if (cloud != null) {
                  Random random = new Random();
                  Location position = new Location(cloud.position());
                  EntityDimensions dim = EntityDimensions.fixed(4.0F, 2.0F);
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
            });
            this.thenDelay(10);
            this.deleteTicker("cloud_glitch_remove");
            this.importBuilding("s2_cloud_puddle");
            this.thenUsePlayer(
               player -> {
                  Random random = new Random();
                  int count = 20;
                  AABB box = new AABB(
                     new Location("/execute in minecraft:overworld run tp @s -31.32 105.16 -391.24 138.48 28.65"),
                     new Location("/execute in minecraft:overworld run tp @s -35.36 102.26 -397.28 309.33 9.90")
                  );

                  for (int i = 0; i < count; i++) {
                     double x = Mth.lerp(random.nextDouble(), box.minX, box.maxX);
                     double y = Mth.lerp(random.nextDouble(), box.minY, box.maxY);
                     double z = Mth.lerp(random.nextDouble(), box.minZ, box.maxZ);
                     double motionX = (random.nextDouble() - 0.5) * 0.02;
                     double motionY = -0.05 - random.nextDouble() * 0.02;
                     double motionZ = (random.nextDouble() - 0.5) * 0.02;
                     float scale = 0.6F + random.nextFloat() * 0.4F;
                     int lifetime = 25 + random.nextInt(10);
                     ParticleHelper.spawnEnginedParticle(
                        player.level(), ParticleTypesST.COLORED_DROP, new Vec3(x, y, z), motionX, motionY, motionZ, scale, lifetime, new Color(12360162), 0.8F
                     );
                  }
               }
            );
            this.freeEntity(CharactersST8.GLITCH_CLOUD);
            this.deleteTicker("glitch_wind");
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenSpeechMessage(0, "Улетела всё-таки…");
            this.thenSpeechMessage(0, "Не волнуйся, к тебе обязательно ещё кто-нибудь заглянет.");
            this.thenSpeechMessage(0, "К тому же, твой мир скоро будет населять множество существ.");
            this.setSkyEyePosition("/execute in minecraft:overworld run tp @s 58.57 212.56 -262.02 130.53 43.80");
            this.setSkyEyeLookTarget(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.saveState("9");
            this.blackout(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -30.41 104.34 -394.09 90.63 30.15");
            this.thenDelay(70);
            this.tdCutsceneMoveCamera(90, SplineType.CATMULLROM, new String[]{"/execute in minecraft:overworld run tp @s -31.58 104.12 -394.12 90.03 46.80"});
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -31.79 103.39 -398.32 355.98 -11.25");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -28.62 103.00 -394.00 90.18 11.40");
            this.thenEntityMove(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, 0.05, "/execute in minecraft:overworld run tp @s -31.50 103.00 -394.01 89.73 22.80");
            this.action(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new ConfiguredAnimation[]{Animations.SPIDER_HEAD_10_DEGREE_HEAD.configure().weight(-2.0F)});
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenEntityMove(
               CharactersST8.SPIDER_HEAD_LOLOLOSHKA, 0.08, "/execute in minecraft:overworld run tp @s -30.34 103.00 -394.01 89.73 22.80", false
            );
            this.thenDelay(20);
            this.thenEntityMove(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, 0.05, "/execute in minecraft:overworld run tp @s -31.50 103.00 -394.01 89.73 22.80");
            this.action(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new ConfiguredAnimation[]{Animations.SPIDER_HEAD_10_DEGREE_HEAD.configure().weight(-2.0F)});
            this.thenDelay(20);
            this.thenPerform(() -> FragmentInstanceManager.addActive(FragmentsST.ME));
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.THINKING_FACE});
            this.tdCutsceneMoveCamera(50, SplineType.CATMULLROM, new String[]{"/execute in minecraft:overworld run tp @s -32.18 103.32 -396.39 321.33 -6.15"});
            this.thenEntityMove(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, 0.08, "/execute in minecraft:overworld run tp @s -30.47 103.00 -394.01 90.18 8.85", false);
            this.action(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new IAnimationSource[]{Animations.SPIDER_HEAD_SIT});
         }

         @ScriptPerformerBuilderReflective.ScriptPart(7)
         public void part7(S2 inst) {
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FACE_EXHALING});
            this.thenSpeechMessage(0, "Устал?");
            this.thenSpeechMessage(0, "Отдохни немного.");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.NEUTRAL_FACE});
            this.thenDelay(40);
            this.entityLookAtFromRot(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -31.50 103.54 -395.98 544.98 -4.35", true);
            this.sendWebWarning("Голос должен добродушно смеяться на фразе 'Тебе совсем не сидится.'");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_LAUGH});
            this.sendWebWarning("Глаз добродушно смеётся");
            this.thenSpeechMessage(0, "Тебе совсем не сидится.");
            this.thenSpeechMessage(0, "Ты настолько жаждешь узнать, кто ты и где ты?");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.THINKING_FACE});
            this.thenSpeechMessage(0, "Что ж, могу только похвалить тебя за такую любознательность.");
            this.thenSpeechMessage(0, "Только позволь предупредить тебя…");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SERIOUSNESS});
            this.thenSpeechMessage(0, "Эти эйдосы тебе скоро пригодятся.");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenSpeechMessage(0, "Поэтому если ты хочешь их потратить, то не торопись.");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.tdCutsceneMoveCamera(60, SplineType.CATMULLROM, new String[]{"/execute in minecraft:overworld run tp @s -32.05 103.69 -392.61 199.53 6.45"});
            this.createParallelPerformer(
               "lo_walk",
               ScriptPerformerBuilder.builder(inst)
                  .thenEntityPath(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -16.60 103.00 -448.33 206.45 0.30")
                  .entityLookAt(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, CharactersST8.SKY_EYE)
                  .build()
            );
            this.stopAction(CharactersST8.SKY_EYE, 0.4F);
            this.thenSpeechMessage(0, "Хорошо, делай, как знаешь.");
            this.thenSpeechMessage(0, "Все мы учимся на своих ошибках.");
            this.thenSpeechMessage(0, "Моя задача — убедиться, что эти ошибки тебе никак не навредят, только научат.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(8)
         public void part8(S2 inst) {
            this.removeParallelPerformer("lo_walk");
            this.saveState("10");
            this.blackout(60);
            this.endTdCutscene();
            this.setSkyEyePosition("/execute in minecraft:overworld run tp @s -230.90 500.00 140.26 -255.55 76.80");
            this.setSkyEyeLookTarget("player");
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -23.35 103.00 -424.62 543.65 -0.90");
            this.sendWebWarning("Дойди до симулятора, там у тебя должен появиться перк");
            this.saveState("11");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenDelay(20);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.WEARY_FACE});
            this.thenSpeechMessage(0, "Тебе не хватает ни на что?");
            this.thenSpeechMessage(0, "Я понимаю, почему это тебя расстраивает.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "А ты пробовал поискать, где ещё можно заработать эйдосы?", false);
            this.saveState("12");
            this.sendWebWarning("Пойди поищи эйдосы поблизости. Сначала подойди к цветочку поблизости");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenWhenPlayerCloseTo(S2.NEON_FLOWER, 3.0);
            this.thenSpeechMessage(0, "Нет, этот цветок слишком устал.");
            this.thenSpeechMessage(0, "Дай ему отдохнуть.", false);
            this.thenDelay(20);
            this.playerEmotion(new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.saveState("13");
            this.sendWebWarning("Подойди к камушку");
            this.thenWhenPlayerCloseTo(S2.NEON_STONE, 3.0);
            this.thenSpeechMessage(0, "Тут тебе искать нечего.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Поищи в другом месте.");
            this.saveState("14");
            this.sendWebWarning("Подойди к траве");
            this.thenWhenPlayerCloseTo(S2.NEON_GRASS, 3.0);
            this.thenSpeechMessage(0, "Ты хочешь, чтобы трава снова дала тебе эйдосы?");
            this.thenSpeechMessage(0, "Но ты ведь только что их от неё получил!");
            this.playerEmotion(new Emotion[]{Emotion.FOLDED_HANDS});
         }

         @ScriptPerformerBuilderReflective.ScriptPart(9)
         public void part9(S2 inst) {
            this.sendWebWarning("На фразе 'Нет, нельзя так обращаться с природой.' голос должен говорить назидательно");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SERIOUSNESS});
            this.thenSpeechMessage(0, "Нет, нельзя так обращаться с природой.");
            this.thenSpeechMessage(0, "Нужно уважать растения.");
            this.playerEmotion(new Emotion[]{Emotion.ENRAGED_FACE});
            this.thenSpeechMessage(0, "Я понимаю, что ты хочешь эйдосы.");
            this.thenSpeechMessage(0, "Но подумай и о траве.");
            this.thenSpeechMessage(0, "Ей нужно восстановить силы, чтобы потом она могла дать тебе ещё больше эйдосов.");
            this.thenSpeechMessage(0, "А пока не беспокой её.");
            this.thenSpeechMessage(0, "Нельзя.", false);
            this.saveState("15");
            this.sendWebWarning("Иди к каменной статуе, что упала на гору в первой серии");
            this.thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s 45.51 111.00 -502.72 -594.75 3.75", 5.0);
            this.thenSpeechMessage(0, "Я уже говорил тебе, что не получится.");
            this.thenSpeechMessage(0, "Чтобы разблокировать её, тебе нужно будет хорошенько потрудиться.");
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenSpeechMessage(0, "Мне жаль, друг мой.");
            this.thenSpeechMessage(0, "Но скоро ты поймёшь, что чем больше усилий ты прилагал для достижения результатов твоего труда…");
            this.thenSpeechMessage(0, "…Тем больше эти результаты приносят радости.");
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenSpeechMessage(0, "Ты устал уже от моих непонятных изречений, да?");
            this.sendWebWarning("Голос добродушно смеётся на фразе 'Ничего не поделаешь.'");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_LAUGH});
            this.thenSpeechMessage(0, "Ничего не поделаешь.");
            this.thenSpeechMessage(0, "Чем больше в мире будет непонятного, тем больше ты захочешь исследовать и пытаться понять.", false);
            this.thenDelay(40);
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
         }

         @ScriptPerformerBuilderReflective.ScriptPart(10)
         public void part10(S2 inst) {
            this.saveState("16");
            this.sendWebWarning(
               "Подойди к грибочку, и кликни по нему ПКМ, начнётся мини-игра. Твоя задача - бегать по арене и собирать эйдосы, уворачиваясь от зомби. Игра закончится когда соберёшь 20 эйдосов, или когда пройдёт 2 минуты"
            );
            this.sendWebWarning("Ты сможешь играть в эту мини-игру всегда, когда у тебя на балансе меньше 20 эйдосов");
            this.thenAwaitEvent(EventType.MUSHROOM_MINIGAME_END_EVENT);
            this.thenDelay(20);
            this.playerEmotion(new Emotion[]{Emotion.PENSIVE_FACE});
            this.thenSpeechMessage(0, "Не грусти.");
            this.thenSpeechMessage(0, "Грибу просто нужно восстановиться.");
            this.thenSpeechMessage(0, "Он же не может давать тебе эйдосы постоянно, правда?");
            this.thenSpeechMessage(0, "У тебя и так уже достаточно, можешь не переживать.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(11)
         public void part11(S2 inst) {
            this.thenDelay(80);
            this.thenSpeechMessage(0, "Получается, ты осмотрел всю территорию?");
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenSpeechMessage(0, "Или осталось что-то ещё?");
            this.saveState("17");
            this.sendWebWarning("Иди к краю карты, поднимайся на гору");
            this.thenWaitUntil(() -> ScriptUtils.getPlayer().position().x() >= 118.0);
            this.thenSpeechMessage(0, "Ах, ну конечно, ты ещё не забирался наверх.");
            this.thenSpeechMessage(0, "Иди, иди.");
            this.thenSpeechMessage(0, "Сам увидишь.", false);
            this.thenWaitUntil(() -> ScriptUtils.getPlayer().position().x() >= 215.0);
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenWaitForF8();
            this.thenSpeechMessage(0, "Как видишь, здесь пролегает граница твоего мира.");
            this.thenSpeechMessage(0, "Дальше тебе нельзя.");
            this.playerEmotion(new Emotion[]{Emotion.ENRAGED_FACE});
            this.thenSpeechMessage(0, "Я понимаю, почему ты сердишься.");
            this.thenSpeechMessage(0, "Мне жаль, но пока по-другому нельзя.");
            this.thenSpeechMessage(0, "Поверь, в этом мире ещё есть очень много интересного для тебя.");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenSpeechMessage(0, "Продолжай расти, трудиться и исследовать, и ты всё увидишь.");
            this.thenSpeechMessage(0, "Кстати об этом…");
            this.thenSpeechMessage(0, "Не хочешь ли ты немного вырасти?");
            this.playerEmotion(new Emotion[]{Emotion.STAR_STRUCK});
            this.thenSpeechMessage(0, "Конечно, ты очень хочешь.");
            this.thenSpeechMessage(0, "Но для этого тебе придётся снова заплатить эйдосы.");
            this.playerEmotion(new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.thenSpeechMessage(0, "Знаю, знаю, ты не хочешь их отдавать.");
            this.thenSpeechMessage(0, "Они разноцветные и приятно блестят.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenSpeechMessage(0, "Но без эйдосов рост невозможен, как невозможны и любые другие изменения в этом мире…");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(12)
         public void part12(S2 inst) {
            this.saveState("18");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SIGH});
            this.sendWebWarning("Голос вздыхает на фразе 'Я понимаю, что тебе тяжело с ними расставаться, ведь ты их сам заработал.'");
            this.thenSpeechMessage(0, "Я понимаю, что тебе тяжело с ними расставаться, ведь ты их сам заработал.");
            this.playerEmotion(new Emotion[]{Emotion.DOWNCAST_FACE_WITH_SWEAT});
            this.thenSpeechMessage(0, "Всем тяжело.");
            this.thenSpeechMessage(0, "Но приходится.");
            this.dialog(
               new Dialog(2, CharactersST8.SKY_EYE)
                  .setTalkingTo("player")
                  .addText("Ты готов отдать двадцать эйдосов, чтобы перейти на следующий уровень?")
                  .addAnswer("[ d(￣◇￣)b ]", false)
                  .addAnswer("[ q(︶︹︺)р ]")
            );
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Я знал, что ты примешь правильное решение.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(13)
         public void part13(S2 inst) {
            this.saveState("19");
            this.thenSpeechMessage(0, "Не торопись делать выводы, друг мой.");
            this.thenSpeechMessage(0, "Тебе кажется, что ничего не произошло, поскольку нет никаких видимых изменений.");
            this.thenSpeechMessage(0, "А ты подумай, поищи — и ты найдёшь.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(14)
         public void part14(S2 inst) {
            this.saveState("20");
            this.thenSpeechMessage(0, "Но прежде чем ты займёшься поиском изменений в мире и в себе, скажи…");
            this.dialog(
               new Dialog(2, CharactersST8.SKY_EYE)
                  .setTalkingTo("player")
                  .addText("Какое счастье ты желаешь сегодня?")
                  .addAnswer("[Понимание]")
                  .addAnswer("[Желание]")
                  .addAnswer("[Голова]", false)
            );
            this.thenSpeechMessage(0, "Прекрасно.");
            this.thenSpeechMessage(0, "Теперь тебе есть, над чем подумать.", false);
            this.thenDelay(90);
            this.saveState("21");
            this.sendWebWarning("Иди к симулятору, посмотри вкладку 'Я'");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenDelay(20);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.thenSpeechMessage(0, "Я вижу, в твоей голове кипит работа.");
            this.thenSpeechMessage(0, "Конечно, ты ещё не способен к вербальному или абстрактному мышлению…");
            this.thenSpeechMessage(0, "Но образное тебе уже под силу.");
            this.thenSpeechMessage(0, "Ведь ты перешёл на второй уровень.");
            this.playerEmotion(new Emotion[]{Emotion.PARTYING_FACE});
            this.thenSpeechMessage(0, "Поздравляю!");
            this.thenSpeechMessage(0, "А мне пора.");
            this.thenSpeechMessage(0, "У меня другие дела.");
            this.thenSpeechMessage(0, "Скоро увидимся.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(15)
         public void part15(S2 inst) {
            this.saveState("22");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{Animations.SKYEYE_OPEN});
            this.thenDelay(Animations.SKYEYE_OPEN);
            this.setTime(1, "00:00");
         }
      })
      .build(this::registerMainPerformer);

   public S2(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this)
         .appendBuilder(
            ScriptUses.predownloadStructures(
               "s2_par_statue", "s2_par_statue_just_par", "s2_par_statue_no_par", "s2_cloud_puddle", "s2_cloud_puddle_no", "s2_bridge_raw", "s2_bridge_proceed"
            )
         )
         .appendBuilder(ScriptUses.createStructureImportSetup("s2_grass", "s2_barrier", "s2_barrier2"))
         .build();
   }

   @SubscribeEvent
   public void onRightClickBlock(RightClickBlock event) {
      if (this.flags.has("green_room")) {
         if (event.getHand() == InteractionHand.MAIN_HAND) {
            Level level = event.getLevel();
            BlockPos clickedPos = event.getPos();
            AABB box = new AABB(new BlockPos(-139, 30, -239), new BlockPos(-139, 32, -245)).inflate(1.0);
            if (!clickedPos.equals(new BlockPos(-139, 31, -246)) && !clickedPos.equals(new BlockPos(-139, 31, -238))) {
               if (box.contains(Vec3.atCenterOf(clickedPos))) {
                  BlockState state = level.getBlockState(clickedPos);
                  if (state.getBlock() instanceof RotateBlockWithCustomShape) {
                     if (!level.isClientSide()) {
                        Direction facing = (Direction)state.getValue(HorizontalDirectionalBlock.FACING);
                        Direction newFacing;
                        if (facing == Direction.WEST) {
                           newFacing = Direction.SOUTH;
                        } else {
                           newFacing = Direction.WEST;
                        }

                        level.setBlockAndUpdate(clickedPos, (BlockState)state.setValue(HorizontalDirectionalBlock.FACING, newFacing));
                     }

                     event.setCanceled(true);
                  }
               }
            }
         }
      }
   }

   private boolean isGreenBridgeDone(Level level, AABB box) {
      BlockPos min = new BlockPos((int)box.minX, (int)box.minY, (int)box.minZ);
      BlockPos max = new BlockPos((int)box.maxX, (int)box.maxY, (int)box.maxZ);

      for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
         BlockState state = level.getBlockState(pos);
         if (state.getBlock() instanceof RotateBlockWithCustomShape) {
            Direction facing = (Direction)state.getValue(HorizontalDirectionalBlock.FACING);
            if (facing != Direction.SOUTH && facing != Direction.NORTH) {
               return false;
            }
         }
      }

      return true;
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }
}
