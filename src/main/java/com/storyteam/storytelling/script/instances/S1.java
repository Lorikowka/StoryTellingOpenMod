package com.storyteam.storytelling.script.instances;

import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.Trigger;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.animations.AnimationsST2;
import com.storyteam.storytelling.client.animations.AnimationsST6;
import com.storyteam.storytelling.client.animations.AnimationsST7;
import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.particle.st_particle.STParticleData;
import com.storyteam.storytelling.client.particle.st_particle.STParticleDataBuilder;
import com.storyteam.storytelling.client.particle.st_particle.options.STDefaultOptions;
import com.storyteam.storytelling.content.blocks.JapanLanternBlock;
import com.storyteam.storytelling.content.blocks.PlatformWithBlock;
import com.storyteam.storytelling.content.blocks.VersiformNuggetBlock;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.AntEntity;
import com.storyteam.storytelling.content.entities.AntGroupEntity;
import com.storyteam.storytelling.content.entities.HeadLololoshkaAntEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.init.system.TributeOffersST;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system._core.structio.entity.StructioContent;
import com.storyteam.storytelling.system._core.structio.entity.StructureEntity;
import com.storyteam.storytelling.system.currency.content.entities.EidosGuiEntity;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.system.dialog.Dialog;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.simulator.main_page.MainTabType;
import com.storyteam.storytelling.system.simulator.main_page.client.gui.MainTabScreen;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.system.tribute.storage.TributeOfferStorage;
import com.storyteam.storytelling.util.Color;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.PositionParser;
import com.storyteam.storytelling.util.ScriptUses;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammerlib.util.java.Cast;

@Script(1)
public class S1 extends ScriptInstance {
   private static final Random RANDOM = new Random((long)"S1".hashCode());
   private static final Character<AnimatedEntity> NEON_FLOWER = Character.builder("s1_neon_flower", Cast.constant(EntitiesST.NEON_FLOWER))
      .name("Цветок")
      .build();
   private static final Character<AnimatedEntity> NEON_MUSHROOM = Character.builder("s1_neon_mushroom", Cast.constant(EntitiesST.NEON_MUSHROOM))
      .name("Гриб")
      .build();
   private static final Character<AnimatedEntity> NEON_GRASS = Character.builder("s1_neon_grass", Cast.constant(EntitiesST.NEON_GRASS)).name("Трава").build();
   private static final Character<AnimatedEntity> NEON_FLOWER_BLACK = Character.builder("s1_neon_flower_black", Cast.constant(EntitiesST.NEON_FLOWER_BLACK))
      .name("Цветок")
      .build();
   private static final Character<AnimatedEntity> NEON_FLOWER_OTHER = Character.builder("s1_neon_flower_other", Cast.constant(EntitiesST.NEON_FLOWER_OTHER))
      .name("Цветок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_GREEN = Character.builder("s1_block_green", Cast.constant(EntitiesST.BLOCK_GREEN))
      .name("Зеленый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_ORANGE = Character.builder("s1_block_orange", Cast.constant(EntitiesST.BLOCK_ORANGE))
      .name("Оранжевый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_WHITE = Character.builder("s1_block_white", Cast.constant(EntitiesST.BLOCK_WHITE))
      .name("Белый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_YELLOW = Character.builder("s1_block_yellow", Cast.constant(EntitiesST.BLOCK_YELLOW))
      .name("Желтый блок")
      .build();
   private static final List<S1.MinigameData> BLOCKS = List.of(
      new S1.MinigameData(
         BLOCK_ORANGE,
         "/execute in minecraft:overworld run tp @s -345.50 25.00 -736.50 0.00 0.00",
         PositionParser.parseBlockPos("/setblock -340 25 -745"),
         BlocksST.PLATFORM_WITH_BLOCK_ORANGE,
         BlocksST.PLATFORM_ORANGE
      ),
      new S1.MinigameData(
         BLOCK_YELLOW,
         "/execute in minecraft:overworld run tp @s -341.50 25.00 -736.50 0.00 0.00",
         PositionParser.parseBlockPos("/setblock -342 25 -745"),
         BlocksST.PLATFORM_WITH_BLOCK_YELLOW,
         BlocksST.PLATFORM_YELLOW
      ),
      new S1.MinigameData(
         BLOCK_GREEN,
         "/execute in minecraft:overworld run tp @s -339.50 25.00 -736.50 0.00 0.00",
         PositionParser.parseBlockPos("/setblock -344 25 -745"),
         BlocksST.PLATFORM_WITH_BLOCK_GREEN,
         BlocksST.PLATFORM_GREEN
      ),
      new S1.MinigameData(
         BLOCK_WHITE,
         "/execute in minecraft:overworld run tp @s -343.50 25.00 -736.50 0.00 0.00",
         PositionParser.parseBlockPos("/setblock -346 25 -745"),
         BlocksST.PLATFORM_WITH_BLOCK_WHITE,
         BlocksST.PLATFORM_WHITE
      )
   );
   private static final List<BlockPos> BLOCKS_AREA = List.of(
      new BlockPos(-340, 25, -745), new BlockPos(-342, 25, -745), new BlockPos(-344, 25, -745), new BlockPos(-346, 25, -745)
   );
   private static final List<BlockPos> LANTERN_POSITIONS = List.of(
      new BlockPos(-301, 25, -738), new BlockPos(-303, 25, -738), new BlockPos(-305, 25, -738), new BlockPos(-307, 25, -738)
   );
   private int playerStep = 0;
   private List<BlockPos> currentSequence = new ArrayList<>();
   private BlockPos lastClickedBlock = null;
   private static final Character<AntGroupEntity> ANT_PILLAR = Character.builder("s1_ant_pillar", Cast.constant(EntitiesST.ANT_PILLAR)).name("Формик").build();
   private static final Character<AntGroupEntity> ANT_TRIANGLE = Character.builder("s1_ant_triangle", Cast.constant(EntitiesST.ANT_TRIANGLE))
      .name("Формик")
      .build();
   private static final Character<AntGroupEntity> ANT_HEART = Character.builder("s1_ant_heart", Cast.constant(EntitiesST.ANT_HEART)).name("Формик").build();
   private static final Character<AntGroupEntity> ANT_HEAD = Character.builder("s1_ant_head", Cast.constant(EntitiesST.ANT_HEAD)).name("Формик").build();
   private static final Character<AntGroupEntity> ANT_FLOWER = Character.builder("s1_ant_flower", Cast.constant(EntitiesST.ANT_FLOWER)).name("Формик").build();
   private static final CharacterList<AntEntity> ANTS = CharactersST8.ANT_FAC.createCharacterList("s1_ants", 4);
   private static final Character<HeadLololoshkaAntEntity> HEAD_LOLOLOSHKA_ANT_TALL = Character.builder(
         "s1_head_lololoshka_ant_tall", Cast.constant(EntitiesST.HEAD_LOLOLOSHKA_ANT_TALL)
      )
      .name("Lololowka")
      .build();
   private static final Character<StructureEntity> CUBE_STATUE = (Character<StructureEntity>)Cast.cast(
      Character.builder("s1_cube_statue", (Supplier<net.minecraft.world.entity.EntityType<StructureEntity>>)(() -> StructioContent.STRUCTURE_ENTITY_ENTITY_TYPE)).name("Cтатуя").onCreatedFresh(e -> {
         e.setStructure("s1_cube_statue_just_cube");
         e.setNoGravity(true);
         e.noPhysics = true;
      }).build()
   );
   private static final Character<EidosGuiEntity> EIDOS = Character.builder("s1_eidos", Cast.constant(EntitiesST.EIDOS)).name("Эйдос").build();
   private boolean isFirstInteractionDone = false;
   private String firstInteractionEntityName = null;
   private boolean mushroomInteractionDone = false;
   private boolean grassInteractionDone = false;
   private boolean statueInteractionDone = false;
   private boolean drownInteractionDone = false;
   private boolean oldShoeInteractionDone = false;
   private boolean eidosInteractionDone = false;
   private boolean versiformInteractionDone = false;
   private boolean keyInteractionDone = false;
   public ScriptPerformer MAIN = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0() {
            this.ensureScriptActive(new int[]{-101, -102, -302});
            this.ensureScriptInactive(new int[]{-301, -305});
            this.thenSetSpeechEntity(0, "???", "sky_eye");
            this.setTime(0, "12:00");
            this.thenRun(() -> CurrencyStorage.setEidos(0));
            this.positionEntity(S1.NEON_FLOWER, "/execute in minecraft:overworld run tp @s -5.50 103.00 -446.50 0.00 0.00");
            this.entitySit(S1.NEON_FLOWER);
            this.positionEntity(S1.NEON_MUSHROOM, "/execute in minecraft:overworld run tp @s 17.50 105.00 -374.50 0.00 0.00");
            this.entitySit(S1.NEON_MUSHROOM);
            this.positionEntity(S1.NEON_GRASS, "/execute in minecraft:overworld run tp @s -5.50 103.00 -408.50 0.00 0.00");
            this.entitySit(S1.NEON_GRASS);
            this.blackout(60);
            this.sendWebWarningOrange(
               "Вначале, во время чэ, на монтаже наложить экран загрузки в эстетике, как весь остальной интерфейс — с пиксельными разноцветными глитчами, которые появляются и исчезают. Бар загрузки постепенно прогружается и, когда доходит до 100%, надпись меняется на “ЗАГРУЗКА ЗАВЕРШЕНА”"
            );
            this.thenDelay(40);
            this.setSimulatorBlocking(MainTabType.MIND, true);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         public void part1(S1 inst) {
            this.saveState("1");
            this.screenColor(16777215, 0, 60, 20);
            this.clearChat();
            this.sendWebWarningOrange("На белом экране тоже наложить глитчи");
            this.thenRunMorph(MorphsST8.SPOODER);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -8.00 103.00 -515.00 225.50 10.00");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_CLOSE});
            this.tdCutscene("/execute in minecraft:overworld run tp @s -8.00 103.00 -515.00 225.50 10.00");
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -6.00 103.00 -517.00 45.20 0.00");
            this.thenDelay(50);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -11.73 104.13 -525.10 -9.26 -11.25");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -7.91 102.64 -516.93 288.65 -10.55");
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -7.09 102.90 -515.90 -134.50 10.00");
            this.tdCutsceneMoveCamera(60, new String[]{"/execute in minecraft:overworld run tp @s -6.67 102.80 -516.31 -134.50 7.00"});
            this.thenDelay(50);
            this.animation(
               CharactersST8.HEAD_LOLOLOSHKA, 0, new ConfiguredAnimation[]{AnimationsST6.GLASSES_SLIPPING.configure().speed(0.8F).transitionTime(0.4F)}
            );
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               80,
               SplineType.LINEAR,
               InterpolationType.LINEAR,
               new String[]{
                  "/execute in minecraft:overworld run tp @s -6.67 102.80 -516.31 -144.50 7.00\n/execute in minecraft:overworld run tp @s -6.67 102.80 -516.31 -124.50 7.00\n/execute in minecraft:overworld run tp @s -6.67 102.80 -516.31 -134.50 7.00\n"
               }
            );
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA,
               new ConfiguredAnimation[]{AnimationsST7.CHECKING_AROUND_LOOK_BASE.configure().mask(ScriptUses.maskNoHead()).speed(0.6F).transitionTime(0.4F)}
            );
            this.thenDelay(100);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -5.12 103.00 -512.96 141.95 -3.50");
            this.stopAnimation(CharactersST8.HEAD_LOLOLOSHKA, 0, 0.3F);
            this.thenDelay(40);
            this.sendWebWarningOrange("Когда Ло будет показан в кадре общим планом и будет дуть цифровой ветер, на монтаже наложить звук этого цифрового ветра");
            this.createTicker(
               "glitch_wind",
               p -> {
                  Entity e = CharactersST8.HEAD_LOLOLOSHKA.repositoryEntity();
                  if (e != null) {
                     Vec3 look = e.getLookAngle().normalize().scale(0.2);
                     Color[] colors = new Color[]{
                        new Color(0, 241, 255, 85), new Color(0, 111, 166, 85), new Color(250, 18, 249, 85), new Color(142, 0, 211, 85), new Color(0, 0, 0, 85)
                     };

                     for (int i = 0; i < 30; i++) {
                        double x = e.getX() + S1.RANDOM.nextDouble(-8.0, 8.0);
                        double y = e.getY() + S1.RANDOM.nextDouble(0.0, 8.0);
                        double z = e.getZ() + S1.RANDOM.nextDouble(-8.0, 8.0);
                        Color randomColor = colors[S1.RANDOM.nextInt(colors.length)];
                        STParticleData quad = new STParticleDataBuilder(
                              new STDefaultOptions(0.1F * (1.0F + S1.RANDOM.nextFloat()), 30, randomColor, 1.0F, false, true, true, true),
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
            this.thenDelay(20);
            this.action(CharactersST8.HEAD_LOLOLOSHKA, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_START_SPIN});
            this.thenDelay(AnimationsST8.HEAD_LOLOLOSHKA_START_SPIN);
            this.action(CharactersST8.HEAD_LOLOLOSHKA, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_SPIN});
            this.thenAnyPerformer(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .thenEntityMove(CharactersST8.HEAD_LOLOLOSHKA, 0.15F, "/execute in minecraft:overworld run tp @s -12.42 103.00 -510.62 45.20 0.00")
                     .build(),
                  new ScriptPerformerBuilder(inst).thenDelay(50).build()
               }
            );
            this.thenClearEntityGoal(CharactersST8.HEAD_LOLOLOSHKA);
            this.thenDelay(1);
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -28.50 103.00 -440.00 0.00 0.00");
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -28.50 103.00 -440.00 0.00 0.00", true);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -30.50 103.00 -440.00 -90.00 5.00");
            this.tdCutsceneMoveCamera(
               95, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -30.50 103.00 -427.50 -90.00 5.00"}
            );
            this.thenDelay(5);
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .thenEntityMove(CharactersST8.HEAD_LOLOLOSHKA, 0.15F, "/execute in minecraft:overworld run tp @s -28.50 103.00 -427.50 0.00 0.00")
                     .build(),
                  new ScriptPerformerBuilder(inst).thenDelay(55).deleteTicker("glitch_wind").thenDelay(10).build()
               }
            );
            this.tdCutscene("/execute in minecraft:overworld run tp @s -22.64 105.25 -414.59 155.08 0.90");
            this.action(CharactersST8.HEAD_LOLOLOSHKA, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_END_SPIN});
            this.thenDelay(60);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         public void part2(S1 inst) {
            this.saveState("2");
            this.blackout(60);
            this.clearChat();
            this.endTdCutscene();
            this.clearPlayerEmotion();
            this.freeEntity(CharactersST8.HEAD_LOLOLOSHKA, true);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -28.50 103.00 -427.50 0.00 0.00");
            this.thenRunMorph(MorphsST8.HEAD);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_CLOSE});
            this.sendWebWarning(
               "После катсцены, осмотрись и прокомментируй обстановку. Как закончишь, нажми F8. По левую сторону от тебя будут пробегать формики"
            );
            this.thenWaitForF8();
            this.positionEntity(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s 10.50 105.00 -416.50 -180.00 0.00");
            this.positionEntity(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s 9.50 103.00 -413.50 90.00 0.00");
            this.positionEntity(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 14.50 103.00 -423.50 -180.00 0.00");
            this.thenEntityPathParallel(
               S1.ANTS.get(0),
               0.5,
               new String[]{
                  "/execute in minecraft:overworld run tp @s 10.48 105.00 -418.33 -261.90 6.90\n/execute in minecraft:overworld run tp @s 7.48 103.00 -420.58 93.60 18.90\n/execute in minecraft:overworld run tp @s -12.89 103.00 -418.47 82.20 13.05\n/execute in minecraft:overworld run tp @s -42.50 103.00 -424.02 286.95 12.90\n/execute in minecraft:overworld run tp @s -73.28 103.00 -430.57 104.85 11.55\n/execute in minecraft:overworld run tp @s -80.00 103.00 -433.50 0.00 0.00\n"
               }
            );
            this.thenEntityPathParallel(
               S1.ANTS.get(1),
               0.5,
               new String[]{
                  "/execute in minecraft:overworld run tp @s 4.76 103.00 -413.53 90.75 23.10\n/execute in minecraft:overworld run tp @s -9.66 103.00 -425.39 108.15 13.35\n/execute in minecraft:overworld run tp @s -28.81 103.00 -432.85 96.45 4.80\n/execute in minecraft:overworld run tp @s -78.43 103.00 -437.29 89.40 20.25\n/execute in minecraft:overworld run tp @s -78.50 103.00 -435.50 0.00 0.00\n"
               }
            );
            this.thenEntityPathParallel(
               S1.ANTS.get(2),
               0.5,
               new String[]{
                  "/execute in minecraft:overworld run tp @s 14.45 103.00 -425.39 -265.20 2.10\n/execute in minecraft:overworld run tp @s 0.60 103.00 -427.44 -271.95 17.25\n/execute in minecraft:overworld run tp @s -25.37 103.00 -417.17 84.00 10.50\n/execute in minecraft:overworld run tp @s -25.03 103.00 -417.66 86.10 3.00\n/execute in minecraft:overworld run tp @s -41.97 103.00 -417.11 101.70 3.30\n/execute in minecraft:overworld run tp @s -81.83 103.00 -428.37 -235.95 8.70\n/execute in minecraft:overworld run tp @s -82.50 103.00 -431.50 0.00 0.00\n"
               }
            );
            this.thenDelay(200);
            this.playerEmotion(new Emotion[]{Emotion.FEARFUL_FACE});
            this.thenDelay(100);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_OPEN});
            this.thenDelay(AnimationsST8.SKYEYE_OPEN);
            this.sendWebWarningRed(
               "Голос читать низко, чтобы он производил впечатление, как будто бог говорит из-за кадра, но с такими добродушными интонациями (не преувеличенно добренькими и слащавыми, а чтобы это прям правдоподобно звучало); главное, чтобы это не звучало, как взрослый, который разговаривает с ребёнком. голос всё же должен говорить очень спокойно и немного отстранённо, хотя и добродушно и с таким типа лёгким любопытством по отношению к герою"
            );
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SURPRISE});
            this.thenSpeechMessage(0, "О, а это у нас кто?");
            this.sendWebWarningRed("Формики — ударение на фОрмики");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Формики, подойдите сюда.");
            this.thenSpeechMessage(0, "Посмотрите, какой маленький.");
            this.freeEntities(true, new Character[]{S1.ANTS.get(0), S1.ANTS.get(1), S1.ANTS.get(2), S1.ANTS.get(3)});
            this.playerEmotion(new Emotion[]{Emotion.FEARFUL_FACE});
            this.thenDelay(25);
            this.sendWebWarningRed("Когда глаз будет смеяться, озвучить его добродушным смехом");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_LAUGH});
            this.thenDelay(AnimationsST8.SKYEYE_LAUGH);
            this.thenSpeechMessage(0, "Не бойся.");
            this.thenSpeechMessage(0, "Здесь никто не причинит тебе вреда.");
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Да, конечно, пока тебе трудно понять…");
            this.thenSpeechMessage(0, "Это значит, что ты в безопасности.");
            this.thenSpeechMessage(0, "Мы присмотрим за тобой.", false);
            this.positionEntity(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s -78.50 103.00 -435.50 0.00 0.00");
            this.positionEntity(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s -82.50 103.00 -431.50 0.00 0.00");
            this.positionEntity(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 9.50 103.00 -413.50 90.00 0.00");
            this.positionEntity(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s 14.50 103.00 -423.50 -180.00 0.00");
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(
                        S1.ANTS.get(0),
                        0.5,
                        "/execute in minecraft:overworld run tp @s -78.43 103.00 -437.29 -80.70 3.30\n/execute in minecraft:overworld run tp @s -62.89 103.00 -435.78 -84.60 4.05\n/execute in minecraft:overworld run tp @s -30.07 103.00 -428.29 -67.95 0.00\n"
                     )
                     .entityLookAt(S1.ANTS.get(0), "player")
                     .action(
                        S1.ANTS.get(0),
                        AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(
                        S1.ANTS.get(1),
                        0.5,
                        "/execute in minecraft:overworld run tp @s -81.83 103.00 -428.37 271.20 0.60\n/execute in minecraft:overworld run tp @s -64.30 103.00 -423.03 271.95 4.35\n/execute in minecraft:overworld run tp @s -29.89 103.00 -426.35 233.25 0.00\n"
                     )
                     .entityLookAt(S1.ANTS.get(1), "player")
                     .action(
                        S1.ANTS.get(1),
                        AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(30)
                     .thenEntityPath(
                        S1.ANTS.get(2),
                        0.5,
                        "/execute in minecraft:overworld run tp @s 4.76 103.00 -413.53 90.75 23.10\n/execute in minecraft:overworld run tp @s -27.00 103.00 -426.72 115.95 0.00\n"
                     )
                     .entityLookAt(S1.ANTS.get(2), "player")
                     .action(
                        S1.ANTS.get(2),
                        AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F))
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(30)
                     .thenEntityPath(
                        S1.ANTS.get(3),
                        0.5,
                        "/execute in minecraft:overworld run tp @s 14.45 103.00 -425.39 -265.20 2.10\n/execute in minecraft:overworld run tp @s 0.07 103.00 -431.14 85.65 4.35\n/execute in minecraft:overworld run tp @s -27.37 103.00 -428.64 45.60 0.00\n"
                     )
                     .entityLookAt(S1.ANTS.get(3), "player")
                     .action(
                        S1.ANTS.get(3),
                        AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F))
                     )
                     .build()
               }
            );
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Это формики, мои подмастерья.");
            this.thenDynamicPerformer(b -> {
               for (Character<AntEntity> ant : S1.ANTS) {
                  b.stopAction(ant, 0.43F);
               }
            });
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Проще говоря — помощники.");
            this.thenSpeechMessage(0, "Они умеют гораздо больше, чем может показаться на первый взгляд…");
            this.thenDynamicPerformer(
               b -> {
                  List<ScriptPerformer> list = new ArrayList<>();

                  for (Character<AntEntity> ant : S1.ANTS) {
                     list.add(
                        ScriptPerformerBuilder.builder(inst)
                           .action(ant, AnimationsST8.ANT_NOD_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F)))
                           .thenDelayRandom(30, 40, S1.RANDOM)
                           .stopAction(ant, S1.RANDOM.nextFloat(0.35F, 0.45F))
                           .build()
                     );
                  }

                  b.thenAllPerformers(list.toArray(new ScriptPerformer[0]));
               }
            );
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "А ты что умеешь?");
            this.thenDynamicPerformer(b -> {
               for (Character<AntEntity> ant : S1.ANTS) {
                  b.action(ant, AnimationsST8.ANT_BUGEYED_BLINK.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.85F, 1.0F)));
               }
            });
            this.thenSpeechMessage(0, "Ты умеешь ходить?");
            this.playerEmotion(new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Или ползать?", false);
            this.sendWebWarning("После фразы \"Или ползать?\" попробуй подвигаться");
            this.thenDelay(100);
            this.clearChat();
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.sendWebWarningOrange("Когда на лице формиков будет беспокойство, на монтаже наложить звук, как будто они тихо пищат");
            this.thenDynamicPerformer(b -> {
               for (Character<AntEntity> ant : S1.ANTS) {
                  b.action(ant, AnimationsST8.ANT_SAD.configure().transitionTime(S1.RANDOM.nextFloat(0.35F, 0.45F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F)));
               }
            });
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Не огорчайся…");
            this.thenSpeechMessage(0, "Ты всему научишься.");
            this.thenSpeechMessage(0, "А пока — тебе помогут… правда, друзья?");
            this.thenDynamicPerformer(
               b -> {
                  List<ScriptPerformer> list = new ArrayList<>();

                  for (Character<AntEntity> ant : S1.ANTS) {
                     list.add(
                        ScriptPerformerBuilder.builder(inst)
                           .thenDelayRandom(1, 4, S1.RANDOM)
                           .stopAction(ant)
                           .entityLookAt(ant, CharactersST8.SKY_EYE, true)
                           .action(ant, AnimationsST8.ANT_NOD_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F)))
                           .build()
                     );
                  }

                  b.thenAllPerformers(list.toArray(new ScriptPerformer[0]));
               }
            );
            this.thenDelay(40);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         public void part3(S1 inst) {
            this.saveState("3");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -28.47 103.00 -424.45 179.85 21.30");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -27.31 103.26 -425.95 143.55 18.15");
            this.thenDynamicPerformer(b -> {
               for (Character<AntEntity> ant : S1.ANTS) {
                  b.freeEntity(ant);
               }
            });
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -28.50 103.00 -427.50 0.00 0.00");
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT, new ConfiguredAnimation[]{AnimationsST8.CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_HEAD.configure().freezeAt(0.01F)}
            );
            this.setSkyEyePosition("/execute in minecraft:overworld run tp @s -230.90 500.00 140.26 -163.75 30.75");
            this.setSkyEyeLookTarget(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT,
               new ConfiguredAnimation[]{AnimationsST8.CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_HEAD.configure().transitionTime(0.0F)}
            );
            this.thenDelay(25);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -30.33 103.85 -424.75 213.90 9.60");
            this.thenDelay(20);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(40);
            this.sendWebWarningOrange("Когда формики подхватят тебя, на монтаже наложить звук, как они пищат");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -27.62 103.00 -429.03 387.44 -9.90");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Формики маленькие, но очень сильные.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Покажите нашему новому другу, что вы умеете.");
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -26.81 103.66 -425.37 141.90 8.55");
            this.action(CharactersST8.HEAD_LOLOLOSHKA_ANT, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_STAGGER});
            this.thenDelay(30);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.GRINNING_FACE_WITH_SMILING_EYES});
            this.thenDelay(10);
            this.thenSpeechMessage(0, "Видишь?");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -27.62 103.00 -429.03 387.44 -9.90");
            this.thenSpeechMessage(0, "Они могут отнести тебя куда угодно.");
            this.thenSpeechMessage(0, "Куда бы ты хотел отправиться?");
            this.clearChat();
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .entityLookAtFromRot(
                        CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -28.50 103.00 -427.50 180.00 0.00", true
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .tdCutsceneMoveCamera(
                        30,
                        SplineType.CATMULLROM,
                        InterpolationType.EASE_IN_SINE,
                        "/execute in minecraft:overworld run tp @s -29.80 103.30 -429.51 -34.21 9.60"
                     )
                     .build()
               }
            );
            this.thenDelay(20);
            this.animation(CharactersST8.HEAD_LOLOLOSHKA_ANT, 0, new IAnimationSource[]{AnimationsST6.GLASSES_SLIPPING});
            this.thenDelay(10);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -28.50 103.09 -428.50 0.00 5.00");
            this.thenDelay(5);
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT,
               new ConfiguredAnimation[]{AnimationsST7.CHECKING_AROUND_LOOK_BASE.configure().mask(ScriptUses.maskNoHead()).speed(0.8F).transitionTime(0.4F)}
            );
            this.thenDelay(AnimationsST7.CHECKING_AROUND_LOOK_BASE.configure().speed(0.8F));
            this.stopAnimation(CharactersST8.HEAD_LOLOLOSHKA_ANT, 0);
            this.entityLookAt(CharactersST8.HEAD_LOLOLOSHKA_ANT, S1.NEON_FLOWER, true);
            this.thenDelay(10);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -30.67 103.51 -426.93 239.85 1.85");
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.EYES});
            this.thenDelay(40);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -8.66 103.00 -447.25 283.65 -7.45");
            this.thenSpeechMessage(0, "А, цветок привлёк твоё внимание?");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -27.25 102.90 -428.91 40.50 -10.45");
            this.thenSpeechMessage(0, "Формики, вы слышали.");
            this.thenSpeechMessage(0, "Вперёд.");
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -29.74 103.57 -426.45 229.20 13.70");
            this.tdCutsceneMoveCamera(
               120, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -22.42 103.57 -433.69 232.95 15.80"}
            );
            this.thenEntityPathParallel(
               CharactersST8.HEAD_LOLOLOSHKA_ANT, new String[]{"/execute in minecraft:overworld run tp @s -7.44 103.00 -445.20 -123.45 -1.00"}
            );
            this.thenDelay(100);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -3.87 103.73 -447.13 70.05 4.40");
            this.freeEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, true);
            this.thenDelay(1);
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -14.50 103.00 -442.50 247.50 0.00");
            this.thenEntityPathParallel(
               CharactersST8.HEAD_LOLOLOSHKA_ANT, new String[]{"/execute in minecraft:overworld run tp @s -7.44 103.00 -445.20 -123.45 0.00"}
            );
            this.setSkyEyeLookTarget(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               70,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -4.60 103.59 -445.85 84.30 11.15"}
            );
            this.thenDelay(80);
            this.entityLookAt(CharactersST8.HEAD_LOLOLOSHKA_ANT, S1.NEON_FLOWER);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.SMILING_FACE_WITH_HEART_EYES});
            this.thenDelay(30);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -5.72 103.20 -448.61 17.55 -7.15");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Красивый цветок, правда?");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "У него есть маленький секрет.");
            this.thenSpeechMessage(0, "Покажите.");
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -5.86 103.51 -442.94 149.38 6.10");
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -5.86 103.51 -442.94 175.78 4.90"}
            );
            this.thenDelay(5);
            this.thenEntityPath(CharactersST8.HEAD_LOLOLOSHKA_ANT, new String[]{"/execute in minecraft:overworld run tp @s -6.23 103.00 -446.09 242.23 0.00"});
            this.thenDelay(10);
            this.deleteTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.thenDelay(10);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(4)
         public void part4(S1 inst) {
            this.saveState("4");
            this.blackout(60);
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -342.50 25.00 -737.92 -180.15 24.30");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 25.00 -738.50 -180.00 2.55");
            this.thenDynamicPerformer(b -> {
               for (S1.MinigameData data : S1.BLOCKS) {
                  b.positionEntity(data.character(), data.startPos());
                  b.thenRun(() -> ScriptUtils.getLevel().setBlockAndUpdate(data.targetPos(), data.platform().defaultBlockState()));
               }
            });
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы продолжить катсцену");
            this.thenWaitForF8();
            this.createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 0.00 0.00");
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -346.07 28.37 -733.45 206.55 27.15");
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.FEARFUL_FACE});
            this.thenDelay(25);
            this.thenSpeechMessage(0, "Не бойся, это всего лишь игра.");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.GAME));
            this.tdCutscene("/execute in minecraft:overworld run tp @s -341.62 25.22 -738.32 158.10 5.20");
            this.thenSpeechMessage(0, "Ты ведь любишь играть?");
            this.clearChat();
            this.tdCutsceneMoveCamera(
               20,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -341.62 25.22 -738.32 158.10 -5.20"}
            );
            this.thenDelay(15);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.STAR_STRUCK});
            this.thenDelay(20);
            this.sendWebWarningRed("На фразе \"Конечно, любишь…\" — озвучить, как глаз добродушно смеется");
            this.thenSpeechMessage(0, "Конечно, любишь…");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.tdCutsceneMoveCamera(
               20,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -341.94 25.05 -739.11 155.40 11.65"}
            );
            this.thenSpeechMessage(0, "Давай посмотрим, справишься ли ты.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(5)
         public void part5(S1 inst) {
            this.saveState("5");
            this.blackout(30);
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 25.17 -738.30 -180.00 11.25");
            this.thenDelay(50);
            this.tdCutsceneMoveCamera(20, new String[]{"/execute in minecraft:overworld run tp @s -342.50 24.89 -739.00 -179.00 11.10"});
            this.thenDelay(5);
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT, new ConfiguredAnimation[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER.configure().speed(0.7F)}
            );
            this.thenDelay(15);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 28.00 -740.50 0.00 90.00");
            this.thenDelay(40);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(6)
         public void part6(S1 inst) {
            this.saveState("6");
            this.blackout(60);
            this.clearChat();
            this.freeEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, true);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 0.00 0.00");
            this.thenDynamicPerformer(b -> {
               for (S1.MinigameData data : S1.BLOCKS) {
                  b.positionEntity(data.character(), data.startPos());
                  b.thenRun(() -> ScriptUtils.getLevel().setBlockAndUpdate(data.targetPos(), data.platform().defaultBlockState()));
               }
            });
            this.sendWebWarning(
               "После мини-катсцены ты сможешь управлять своим персонажем и тебе нужно сыграть в мини-игру. Твоя задача — передвигать кубики определенных цветов на такого же цвета платформы. Когда все кубики будут стоять на своих местах, тебя телепортирует назад и скрипт продолжится. В эту мини-игру ты сможешь играть каждый новый внутриигровой день. Чтобы начать новый внутриигровой день, можно подождать 00:00 по внутриигровым часам или прописать команду /st time new_day_start"
            );
            this.createParallelPerformer(
               "move_blocks",
               ScriptPerformerBuilder.builder(inst)
                  .thenDynamicPerformer(
                     b -> {
                        List<ScriptPerformer> performers = new ArrayList<>();

                        for (S1.MinigameData data : S1.BLOCKS) {
                           performers.add(
                              ScriptPerformerBuilder.builder(inst)
                                 .thenWaitUntil(data::isEntityAboveBlock)
                                 .appendBuilder(builder -> S1.this.appendBlockPlacedSuccessful(inst, builder, data))
                                 .build()
                           );
                        }

                        b.thenAllPerformers(performers.toArray(new ScriptPerformer[0]));
                     }
                  )
                  .build()
            );
            this.thenWaitUntil(() -> S1.this.hasAtLeastBlocks(1));
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Тяжело, да.");
            this.thenSpeechMessage(0, "Но ты всё делаешь правильно.", false);
            this.thenWaitUntil(() -> S1.this.hasAtLeastBlocks(2));
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Так держать.", false);
            this.thenWaitUntil(() -> S1.this.hasAtLeastBlocks(3));
            this.playerEmotion(new Emotion[]{Emotion.FACE_EXHALING});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Давай, ещё немного…", false);
            this.thenWaitUntil(() -> S1.this.hasAtLeastBlocks(4));
            this.playerEmotion(new Emotion[]{Emotion.GRINNING_FACE_WITH_SMILING_EYES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ничего себе!");
            this.thenSpeechMessage(0, "Такой маленький, а справился…");
            this.playerEmotion(new Emotion[]{Emotion.PARTYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Поздравляю: тебя ждёт награда.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(7)
         public void part7(S1 inst) {
            this.saveState("7");
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S1::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.removeParallelPerformer("move_blocks");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -8.50 103.00 -446.50 -90.00 0.00");
            this.thenRun(() -> CurrencyStorage.setEidos(0));
            this.setSkyEyeLookTarget("player");
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenDelay(20);
            this.thenUseEntity(S1.NEON_FLOWER, e -> {
               Vec3 pos = e.position();
               UtilST.dropEidos(e.level(), pos, 3, true);
            });
            this.thenSpeechMessage(0, "Ты заработал свои первые эйдосы.");
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Собери их обязательно, они тебе очень пригодятся.");
            this.playerEmotion(new Emotion[]{Emotion.OK_HAND});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Ты быстро учишься…");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Что ж, пожалуй, уже пришло время показать тебе нечто важное.");
            this.playerEmotion(new Emotion[]{Emotion.FOLDED_HANDS});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Тебе не терпится?");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Формики, покажите ему.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(8)
         public void part8(S1 inst) {
            this.saveState("8");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -11.23 103.00 -444.92 -117.45 5.25");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -10.01 103.00 -445.52 -124.50 6.15");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -8.50 103.00 -446.50 90.00 0.00");
            this.setSkyEyeLookTarget(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.entityLookAtFromRot(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -8.50 103.00 -446.50 140.00 0.00");
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -9.20 102.98 -449.15 352.50 -9.15");
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.EYES});
            this.thenDelay(15);
            this.entityLookAtFromRot(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -8.50 103.00 -446.50 70.00 0.00", true);
            this.thenDelay(15);
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -11.00 103.42 -446.50 -90.00 14.10");
            this.thenDelay(15);
            this.positionEntity(CharactersST8.ANT, "/execute in minecraft:overworld run tp @s -15.87 103.00 -453.30 -49.20 0.30");
            this.entityLookAt(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, CharactersST8.ANT);
            this.thenEntityPathParallel(CharactersST8.ANT, 0.43, new String[]{"/execute in minecraft:overworld run tp @s -9.47 103.00 -446.76 281.85 3.00"});
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               50,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -9.98 103.11 -448.06 324.30 10.05"}
            );
            this.entityLookAt(CharactersST8.ANT, CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true);
            this.action(CharactersST8.ANT, new IAnimationSource[]{AnimationsST8.ANT_HAPPY_JUMP_CYCLE});
            this.thenDelay(AnimationsST8.ANT_HAPPY_JUMP_CYCLE);
            this.thenDelay(AnimationsST8.ANT_HAPPY_JUMP_CYCLE);
            this.stopAction(CharactersST8.ANT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -7.14 103.20 -445.95 493.19 11.10");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .thenEntityPath(
                        CharactersST8.ANT,
                        0.5,
                        "/execute in minecraft:overworld run tp @s -12.45 103.00 -455.13 154.34 6.90\n/execute in minecraft:overworld run tp @s -21.63 103.00 -472.31 555.45 0.00\n"
                     )
                     .entityLookAt(CharactersST8.ANT, CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true, true)
                     .action(CharactersST8.ANT, AnimationsST8.ANT_HAPPY_JUMP_CYCLE)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(40)
                     .thenEntityPath(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -19.57 103.00 -471.44 519.59 -1.20")
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenDelay(100)
                     .tdCutscene("/execute in minecraft:overworld run tp @s -23.02 103.00 -472.72 314.09 7.50")
                     .thenDelay(70)
                     .thenClearEntityGoal(CharactersST8.SPIDER_HEAD_LOLOLOSHKA)
                     .entityLookAtFromRot(CharactersST8.ANT, "/execute in minecraft:overworld run tp @s -21.58 103.00 -472.24 -22.43 -1.13")
                     .build()
               }
            );
            this.tdCutscene("/execute in minecraft:overworld run tp @s -21.36 102.79 -471.23 169.04 1.05");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -19.91 103.00 -463.81 -179.63 5.62");
            this.thenDelay(20);
            this.tdCutsceneMoveCamera(
               90,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -20.92 104.29 -466.96 183.89 -0.45"}
            );
            this.entityLookAt(CharactersST8.ANT, CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.thenEntityPath(
               CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new String[]{"/execute in minecraft:overworld run tp @s -19.57 103.00 -471.44 519.59 -1.20"}
            );
            this.stopAction(CharactersST8.ANT, 0.5F);
            this.thenDelay(10);
            this.entityLookAtFromRot(CharactersST8.ANT, "/execute in minecraft:overworld run tp @s -21.63 103.00 -472.31 -165.15 -24.15", true, false);
            this.thenDelay(30);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(9)
         public void part9(S1 inst) {
            this.saveState("9");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -19.57 103.00 -471.44 158.99 -15.45");
            this.thenUseEntity(CharactersST8.ANT, e -> e.setDisableIdleHeadRotateAnimation(false));
            this.setSkyEyeLookTarget("player");
            this.thenDelay(80);
            this.entityLookAt(CharactersST8.ANT, "player", true);
            this.thenDelay(10);
            this.deleteTelepornGlitchEffect(CharactersST8.ANT);
            this.sendWebWarning("Открой симулятор");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(40);
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Ах да, верно, я совсем забыл — он заблокирован.");
            this.thenSpeechMessage(0, "Ничего, это не беда.");
            this.thenSpeechMessage(0, "Подумай немного: что можно сделать, чтобы разблокировать его?");
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.sendWebWarning(
               "После фразы \"Подумай немного: что можно сделать, чтобы разблокировать его?\" подойди к светящемуся камню, который находится по левую сторону от симулятора (/execute in minecraft:overworld run tp @s -61.50 103.00 -469.50 -90.00 0.00)"
            );
            this.thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -61.50 103.00 -469.50 -90.00 0.00", 8.0);
            this.thenSpeechMessage(0, "А ты сообразителен — это хорошо.", false);
            this.sendWebWarning(
               "После фразы \"А ты сообразителен — это хорошо.\", нажми ПКМ по камню — и тебя телепортирует в следующую мини-игру. Этой игрой будет пакман. Здесь не будет призраков, но тебе нужно собрать все доты, чтобы перейти на следующий уровень. Всего уровней будет три. В эту мини-игру ты также можешь играть каждый новый внутриигровой день"
            );
            this.thenAwaitEvent(EventType.STONE_MINIGAME_END_EVENT);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(10)
         public void part10(S1 inst) {
            this.saveState("10");
            this.playerEmotion(new Emotion[]{Emotion.PARTYING_FACE});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Неплохо, очень неплохо.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Есть у тебя голова на плечах.");
            this.sendWebWarningRed("После фразы \"Есть у тебя голова на плечах.\", голос должен ржать");
            this.playerEmotion(new Emotion[]{Emotion.NEUTRAL_FACE});
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_LAUGH});
            this.thenDelay(AnimationsST8.SKYEYE_LAUGH);
            this.thenSpeechMessage(0, "Я просто пошутил, не обижайся.");
            this.thenSpeechMessage(0, "А теперь что?");
            this.thenSpeechMessage(0, "Как поступишь дальше?");
            this.sendWebWarning("После фразы \"Как поступишь дальше?\" подходи к симулятору и заплати эйдосы, чтобы разблокировать его");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.playerEmotion(new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Не спеши.");
            this.thenSpeechMessage(0, "Нельзя получить всё сразу.");
            this.thenSpeechMessage(0, "Формики, развеселите нашего нового друга.");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(11)
         public void part11(S1 inst) {
            this.saveState("11");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -23.53 103.00 -467.80 179.70 9.60");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -23.10 103.07 -468.74 165.75 11.70");
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 0.00 0.00");
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT, new ConfiguredAnimation[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER.configure().freezeAt(10.0F)}
            );
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.action(
               CharactersST8.HEAD_LOLOLOSHKA_ANT,
               new ConfiguredAnimation[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER.configure().reversed().speed(0.8F).transitionTime(0.0F)}
            );
            this.thenDelay(AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER);
            this.action(CharactersST8.HEAD_LOLOLOSHKA_ANT, new IAnimationSource[]{AnimationsST8.CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_OFF_HEAD});
            this.thenDelay(10);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -24.54 103.47 -466.36 195.75 10.95");
            this.thenDelay(60);
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 0.00 0.00")
                     .entitySit(CharactersST8.HEAD_LOLOLOSHKA)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s -24.25 103.00 -471.19 -56.10 0.00")
                     .entityLookAt(S1.ANTS.get(0), CharactersST8.HEAD_LOLOLOSHKA)
                     .entitySit(S1.ANTS.get(0))
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s -22.77 103.00 -471.21 46.80 0.00")
                     .entityLookAt(S1.ANTS.get(1), CharactersST8.HEAD_LOLOLOSHKA)
                     .entitySit(S1.ANTS.get(1))
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s -22.76 103.00 -469.77 134.55 0.00")
                     .entityLookAt(S1.ANTS.get(2), CharactersST8.HEAD_LOLOLOSHKA)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s -24.28 103.00 -469.72 224.55 0.00")
                     .entityLookAt(S1.ANTS.get(3), CharactersST8.HEAD_LOLOLOSHKA)
                     .build()
               }
            );
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.CONFOUNDED_FACE});
            this.action(
               S1.ANTS.get(2),
               new ConfiguredAnimation[]{
                  AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(0.9F, 1.15F)).weight(S1.RANDOM.nextFloat(0.8F, 1.0F))
               }
            );
            this.action(
               S1.ANTS.get(3),
               new ConfiguredAnimation[]{
                  AnimationsST8.ANT_HAPPY_JUMP_CYCLE.configure().speed(S1.RANDOM.nextFloat(0.9F, 1.15F)).weight(S1.RANDOM.nextFloat(0.8F, 1.0F))
               }
            );
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .overridePredefinedAnimation(S1.ANTS.get(0), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .thenEntityPath(
                        S1.ANTS.get(0),
                        0.4F,
                        "/execute in minecraft:overworld run tp @s -25.40 103.00 -470.25 264.45 14.85\n/execute in minecraft:overworld run tp @s -24.77 103.00 -468.77 217.50 14.10\n/execute in minecraft:overworld run tp @s -24.07 103.00 -469.03 201.00 12.30\n"
                     )
                     .entityLookAt(S1.ANTS.get(0), CharactersST8.HEAD_LOLOLOSHKA, true, true)
                     .action(S1.ANTS.get(0), AnimationsST8.ANT_HAPPY_JUMP_CYCLE)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .overridePredefinedAnimation(S1.ANTS.get(1), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .thenEntityPath(
                        S1.ANTS.get(1),
                        0.4F,
                        "/execute in minecraft:overworld run tp @s -21.64 103.00 -470.43 94.35 8.70\n/execute in minecraft:overworld run tp @s -21.88 103.00 -469.07 122.25 7.95\n/execute in minecraft:overworld run tp @s -23.29 103.00 -468.90 171.00 10.50\n"
                     )
                     .entityLookAt(S1.ANTS.get(1), CharactersST8.HEAD_LOLOLOSHKA, true, true)
                     .action(S1.ANTS.get(1), AnimationsST8.ANT_HAPPY_JUMP_CYCLE)
                     .build()
               }
            );
            this.thenDelay(30);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -23.50 102.91 -468.42 -180.00 -3.90");
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 43.43 0.00", true);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(40);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -23.53 103.44 -470.74 -2.55 35.85");
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 -45.01 0.00");
            this.sendWebWarningOrange(
               "Когда формики начнут танцевать танец мурашек, на монтаже наложить звук, как будто они пищат какую-то песенку с таким вот примерно сказочным вайбом: https://www.youtube.com/watch?v=tRJ531rrUKg&ab_channel=VFTraducciones%26AMV"
            );
            this.thenDynamicPerformer(
               b -> {
                  List<ScriptPerformer> list = new ArrayList<>();

                  for (Character<AntEntity> ant : S1.ANTS) {
                     list.add(
                        ScriptPerformerBuilder.builder(inst)
                           .action(ant, AnimationsST8.ANT_DANCE.configure().speed(S1.RANDOM.nextFloat(0.9F, 1.1F)).weight(S1.RANDOM.nextFloat(0.8F, 1.0F)))
                           .build()
                     );
                  }

                  b.thenAllPerformers(list.toArray(new ScriptPerformer[0]));
               }
            );
            this.thenDelay(80);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -23.50 102.91 -467.73 176.10 -3.90");
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 0.00 0.00", true);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SMILING_FACE});
            this.thenDelay(50);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(12)
         public void part12(S1 inst) {
            this.saveState("12");
            this.blackout(60);
            this.clearChat();
            this.clearCharacterEmotion(S1.HEAD_LOLOLOSHKA_ANT_TALL);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -24.25 103.68 -468.21 -518.69 13.35");
            this.freeEntities(true, new Character[]{CharactersST8.HEAD_LOLOLOSHKA, S1.ANTS.get(0), S1.ANTS.get(1), S1.ANTS.get(2), S1.ANTS.get(3)});
            this.positionEntity(S1.HEAD_LOLOLOSHKA_ANT_TALL, "/execute in minecraft:overworld run tp @s -23.50 103.00 -470.50 0.00 0.00");
            this.setSkyEyeLookTarget(S1.HEAD_LOLOLOSHKA_ANT_TALL);
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы продолжить катсцену");
            this.thenWaitForF8();
            this.action(S1.HEAD_LOLOLOSHKA_ANT_TALL, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_LONG_JUMP});
            this.thenDelay(70);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -21.34 103.18 -472.35 -682.66 -10.05");
            this.thenDelay(30);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -23.02 105.23 -467.84 -550.94 18.75");
            this.tdCutsceneMoveCamera(40, new String[]{"/execute in minecraft:overworld run tp @s -23.24 104.54 -468.87 172.21 -9.15"});
            this.thenDelay(30);
            this.characterEmotion(S1.HEAD_LOLOLOSHKA_ANT_TALL, new Emotion[]{Emotion.GRINNING_FACE_WITH_SMILING_EYES});
            this.thenDelay(30);
            this.clearCharacterEmotion(S1.HEAD_LOLOLOSHKA_ANT_TALL);
            this.action(S1.HEAD_LOLOLOSHKA_ANT_TALL, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_LONG_LAND});
            this.thenDelay(8);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(13)
         public void part13(S1 inst) {
            this.saveState("13");
            this.blackout(60);
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.freeEntity(S1.HEAD_LOLOLOSHKA_ANT_TALL, true);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 22.50");
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 4.50 104.00 -464.50 90.00 0.00");
            this.entitySit(CharactersST8.HEAD_LOLOLOSHKA);
            this.positionEntity(S1.ANT_PILLAR, "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 0.00");
            this.appendBuilder(b -> S1.this.appendAntsIdle(inst, b, S1.ANT_PILLAR));
            this.action(S1.ANT_PILLAR, new ConfiguredAnimation[]{AnimationsST8.CUTSCENE_ANT_PILLAR_JUMP.configure().freezeAt(0.01F)});
            this.setSkyEyeLookTarget(CharactersST8.HEAD_LOLOLOSHKA);
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы продолжить катсцену");
            this.thenWaitForF8();
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 7.65"}
            );
            this.action(S1.ANT_PILLAR, new ConfiguredAnimation[]{AnimationsST8.CUTSCENE_ANT_PILLAR_JUMP.configure().transitionTime(0.0F)});
            this.thenDelay(AnimationsST8.CUTSCENE_ANT_PILLAR_JUMP);
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.80 104.03 -463.81 247.35 -3.60"}
            );
            this.thenDelay(40);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.THUMBS_UP});
            this.thenDelay(25);
            this.thenSpeechMessage(0, "Как радостно быть с друзьями, правда?");
            this.thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.JOYFULLY));
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 7.65");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANT_PILLAR).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANT_TRIANGLE, "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 0.00")
                     .appendBuilder(b -> S1.this.appendAntsIdle(inst, b, S1.ANT_TRIANGLE))
                     .build()
               }
            );
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.80 104.03 -463.81 247.35 -3.60");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.CLAPPING_HANDS});
            this.thenDelay(25);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 7.65");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANT_TRIANGLE).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANT_HEART, "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 0.00")
                     .appendBuilder(b -> S1.this.appendAntsIdle(inst, b, S1.ANT_HEART))
                     .build()
               }
            );
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.80 104.03 -463.81 247.35 -3.60");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SMILING_FACE_WITH_HEART_EYES});
            this.thenDelay(25);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 1.83 103.03 -464.49 90.00 15.00");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANT_HEART).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANT_HEAD, "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 0.00")
                     .appendBuilder(b -> S1.this.appendAntsIdle(inst, b, S1.ANT_HEAD))
                     .build()
               }
            );
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -0.89 103.53 -463.68 260.85 7.65");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FACE_WITH_TEARS_OF_JOY});
            this.thenDelay(25);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 7.65");
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANT_HEAD).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANT_FLOWER, "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 0.00")
                     .appendBuilder(b -> S1.this.appendAntsIdle(inst, b, S1.ANT_FLOWER))
                     .build()
               }
            );
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.80 104.03 -463.81 247.35 -3.60"}
            );
            this.thenDelay(40);
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 4.50 104.00 -464.50 112.35 0.00", true);
            this.thenDelay(10);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 5.94 104.61 -464.88 101.55 13.35");
            this.thenDelay(10);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(25);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.42 103.68 -463.53 114.30 7.65");
            this.tdCutsceneMoveCamera(
               40,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.07 103.28 -464.04 102.45 23.25"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANT_FLOWER).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s 0.50 103.00 -464.50 -90.00 -14.44")
                     .entityLookAt(S1.ANTS.get(0), CharactersST8.HEAD_LOLOLOSHKA)
                     .thenDelayRandom(1, 3, S1.RANDOM)
                     .action(
                        S1.ANTS.get(0),
                        AnimationsST8.ANT_SAD.configure().transitionTime(S1.RANDOM.nextFloat(0.37F, 0.47F)).weight(S1.RANDOM.nextFloat(0.85F, 1.1F))
                     )
                     .overridePredefinedAnimation(S1.ANTS.get(0), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s 0.89 103.00 -465.17 -79.49 -15.64")
                     .entityLookAt(S1.ANTS.get(1), CharactersST8.HEAD_LOLOLOSHKA)
                     .thenDelayRandom(1, 3, S1.RANDOM)
                     .action(
                        S1.ANTS.get(1),
                        AnimationsST8.ANT_SAD.configure().transitionTime(S1.RANDOM.nextFloat(0.37F, 0.47F)).weight(S1.RANDOM.nextFloat(0.85F, 1.1F))
                     )
                     .overridePredefinedAnimation(S1.ANTS.get(1), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 0.01 103.00 -463.95 -96.97 -12.83")
                     .entityLookAt(S1.ANTS.get(2), CharactersST8.HEAD_LOLOLOSHKA)
                     .thenDelayRandom(1, 3, S1.RANDOM)
                     .action(
                        S1.ANTS.get(2),
                        AnimationsST8.ANT_SAD.configure().transitionTime(S1.RANDOM.nextFloat(0.37F, 0.47F)).weight(S1.RANDOM.nextFloat(0.85F, 1.1F))
                     )
                     .overridePredefinedAnimation(S1.ANTS.get(2), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s 0.79 103.00 -463.67 -102.60 -15.16")
                     .entityLookAt(S1.ANTS.get(3), CharactersST8.HEAD_LOLOLOSHKA)
                     .thenDelayRandom(1, 3, S1.RANDOM)
                     .action(
                        S1.ANTS.get(3),
                        AnimationsST8.ANT_SAD.configure().transitionTime(S1.RANDOM.nextFloat(0.37F, 0.47F)).weight(S1.RANDOM.nextFloat(0.85F, 1.1F))
                     )
                     .overridePredefinedAnimation(S1.ANTS.get(3), MovementActionType.FAST_WALKING, AnimationsST8.ANT_RUN)
                     .build()
               }
            );
            this.thenDelay(20);
            this.thenEntityPathParallel(S1.ANTS.get(0), 0.37, new String[]{"/execute in minecraft:overworld run tp @s 3.76 104.00 -464.50 -89.99 -14.34"});
            this.thenEntityPathParallel(S1.ANTS.get(1), 0.37, new String[]{"/execute in minecraft:overworld run tp @s 3.58 104.00 -465.17 -90.00 -15.66"});
            this.thenEntityPathParallel(S1.ANTS.get(2), 0.37, new String[]{"/execute in minecraft:overworld run tp @s 3.20 104.00 -463.95 -90.00 -12.87"});
            this.thenEntityPathParallel(S1.ANTS.get(3), 0.37, new String[]{"/execute in minecraft:overworld run tp @s 4.57 104.00 -463.67 -90.00 -15.20"});
            this.thenDelay(20);
            this.thenClearEntityGoal(S1.ANTS.get(0));
            this.thenClearEntityGoal(S1.ANTS.get(1));
            this.thenClearEntityGoal(S1.ANTS.get(2));
            this.thenClearEntityGoal(S1.ANTS.get(3));
            this.positionEntity(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s 4.08 104.00 -463.53 -156.61 -1.80");
            this.positionEntity(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s 4.87 104.00 -463.69 155.44 -2.51");
            this.positionEntity(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 5.35 104.00 -464.83 68.87 -2.25");
            this.positionEntity(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s 4.57 104.00 -465.63 3.56 -1.98");
            this.entityLookAt(S1.ANTS.get(0), CharactersST8.HEAD_LOLOLOSHKA, true, false);
            this.entityLookAt(S1.ANTS.get(1), CharactersST8.HEAD_LOLOLOSHKA, true, false);
            this.entityLookAt(S1.ANTS.get(2), CharactersST8.HEAD_LOLOLOSHKA, true, false);
            this.entityLookAt(S1.ANTS.get(3), CharactersST8.HEAD_LOLOLOSHKA, true, false);
            this.thenDelay(10);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.23 104.07 -465.33 289.29 4.42");
            this.thenSpeechMessage(0, "Я вижу, тебе очень любопытно.");
            this.thenDynamicPerformer(b -> {
               for (Character<AntEntity> ant : S1.ANTS) {
                  b.stopAction(ant);
                  b.thenUseEntity(ant, e -> e.setDisableIdleHeadRotateAnimation(true));
               }
            });
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.LOUDLY_CRYING_FACE, Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "А что ещё ты хочешь посмотреть?");
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -0.76 105.28 -462.72 252.24 14.17");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.EYES});
            this.action(CharactersST8.HEAD_LOLOLOSHKA, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ROTATE});
            this.thenDelay(AnimationsST8.HEAD_LOLOLOSHKA_ROTATE);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 4.41 103.88 -466.94 365.04 -9.23");
            this.sendWebWarningRed("Когда глаз будет смеяться, озвучить его добродушным смехом");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_LAUGH});
            this.thenDelay(AnimationsST8.SKYEYE_LAUGH);
            this.thenSpeechMessage(0, "Разумеется.");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.23 104.07 -465.33 -71.16 11.62");
            this.thenSpeechMessage(0, "Формики, вы знаете, что делать.");
            this.clearChat();
            this.tdCutsceneMoveCamera(
               100,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 1.58 104.60 -467.24 313.74 9.37"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .thenEntityPath(S1.ANTS.get(3), 0.35, "/execute in minecraft:overworld run tp @s 4.01 104.00 -465.25 320.79 0.00")
                     .entityLookAt(S1.ANTS.get(3), CharactersST8.HEAD_LOLOLOSHKA, false, true)
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenEntityPath(S1.ANTS.get(0), 0.35, "/execute in minecraft:overworld run tp @s 3.61 104.00 -464.46 267.84 15.37")
                     .entityLookAt(S1.ANTS.get(0), CharactersST8.HEAD_LOLOLOSHKA, false, true)
                     .build()
               }
            );
            this.thenDelay(5);
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA).build(),
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(0)).build(),
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(1)).build(),
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(2)).build(),
                  ScriptPerformerBuilder.builder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(3)).build(),
                  ScriptPerformerBuilder.builder(inst)
                     .createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s 4.50 104.00 -464.50 112.35 0.00")
                     .build()
               }
            );
            this.action(CharactersST8.HEAD_LOLOLOSHKA_ANT, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER});
            this.thenDelay(AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(30);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(14)
         public void part14(S1 inst) {
            this.saveState("14");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 4.50 104.00 -464.50 112.35 0.00");
            this.positionEntity(S1.EIDOS, "/execute in minecraft:overworld run tp @s -10.55 103.00 -464.54 0.00 0.00");
            this.entitySit(S1.EIDOS);
            this.setSkyEyeLookTarget("player");
            this.thenRun(() -> {
               S1.this.isFirstInteractionDone = false;
               S1.this.mushroomInteractionDone = false;
               S1.this.grassInteractionDone = false;
               S1.this.statueInteractionDone = false;
               S1.this.drownInteractionDone = false;
               S1.this.oldShoeInteractionDone = false;
               S1.this.eidosInteractionDone = false;
               S1.this.versiformInteractionDone = false;
               S1.this.keyInteractionDone = false;
               CurrencyStorage.setEidos(3);
            });
            this.importBuilding("s1_ruda");
            this.importBuilding("s1_key");
            this.importBuilding("s1_sapog");
            this.thenDelay(80);
            this.thenSpeechMessage(0, "Иди, осмотрись, исследуй территорию.");
            this.playerEmotion(new Emotion[]{Emotion.STAR_STRUCK});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Я понимаю: всё кажется новым и непонятным.");
            this.thenSpeechMessage(0, "Ступай, я подожду.", false);
            this.sendWebWarning(
               "После фразы \"Ступай, я подожду.\" ты можешь пойти погулять. При приближении/взаимодействии на ПКМ с разными объектами будут проигрываться разные фразы и эмоции. Тебе также нужно попробовать утонуть в воде. Объекты, с которыми нужно по взаимодействовать:"
            );
            this.sendWebWarning("1. Гриб — /execute in minecraft:overworld run tp @s 17.50 105.00 -374.50 0.00 0.00");
            this.sendWebWarning("2. Трава — /execute in minecraft:overworld run tp @s -5.50 103.00 -408.50 0.00 0.00");
            this.sendWebWarning("3. Статуя — /execute in minecraft:overworld run tp @s 23.00 111.00 -527.00 0.00 0.00");
            this.sendWebWarning("4. Вода — рядом с грибом и травой");
            this.sendWebWarning("5. Старый ботинок — /execute in minecraft:overworld run tp @s -91.50 103.50 -379.50 0.00 0.00");
            this.sendWebWarning("6. Эйдос — /execute in minecraft:overworld run tp @s -10.55 103.00 -464.54 0.00 0.00");
            this.sendWebWarning("7. Кусок версиформа — /execute in minecraft:overworld run tp @s -3.50 103.50 -394.50 0.00 0.00");
            this.sendWebWarning("8. Резной ключик — /execute in minecraft:overworld run tp @s -64.50 106.50 -505.50 0.00 0.00");
            this.sendWebWarning(
               "Если что-то пошло не так или ты хочешь закончить эту часть, ты можешь прописать /script trigger 1 completeAllInteractions — это выполнит все интеракции, и скрипт продолжится дальше"
            );
            this.thenAllPerformers(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .thenAwaitEvent(EventType.ENTITY_CLICK, e -> {
                        if (!e.isOf(S1.NEON_MUSHROOM) && !e.isOf(S1.NEON_GRASS)) {
                           return false;
                        } else if (S1.this.isFirstInteractionDone) {
                           return false;
                        } else {
                           S1.this.firstInteractionEntityName = e.storyTarget.getStoryName();
                           return true;
                        }
                     })
                     .thenIfDynamic(
                        () -> !S1.this.isFirstInteractionDone,
                        new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFirstInteraction(inst, b)).thenRun(() -> {
                           S1.this.isFirstInteractionDone = true;
                           if (S1.NEON_MUSHROOM.getReferenceId().equals(S1.this.firstInteractionEntityName)) {
                              S1.this.mushroomInteractionDone = true;
                           }

                           if (S1.NEON_GRASS.getReferenceId().equals(S1.this.firstInteractionEntityName)) {
                              S1.this.grassInteractionDone = true;
                           }
                        }).build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenAwaitEvent(EventType.ENTITY_CLICK, e -> {
                        if (!e.isOf(S1.NEON_MUSHROOM) && !e.isOf(S1.NEON_GRASS)) {
                           return false;
                        } else {
                           return !S1.this.isFirstInteractionDone ? false : !e.storyTarget.getStoryName().equals(S1.this.firstInteractionEntityName);
                        }
                     })
                     .thenIfDynamic(
                        () -> S1.this.isFirstInteractionDone,
                        new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendSecondInteraction(inst, b)).thenRun(() -> {
                           if (S1.NEON_MUSHROOM.getReferenceId().equals(S1.this.firstInteractionEntityName)) {
                              S1.this.mushroomInteractionDone = true;
                           }

                           if (S1.NEON_GRASS.getReferenceId().equals(S1.this.firstInteractionEntityName)) {
                              S1.this.grassInteractionDone = true;
                           }
                        }).build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.statueInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s 23.00 111.00 -527.00 0.00 0.00", 5.0)
                           .appendBuilder(b -> S1.this.appendStatueInteraction(inst, b))
                           .thenRun(() -> S1.this.statueInteractionDone = true)
                           .build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.drownInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenWaitForFlagAndConsume("drown_death")
                           .appendBuilder(b -> S1.this.appendDrown(inst, b))
                           .thenRun(() -> S1.this.drownInteractionDone = true)
                           .build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.oldShoeInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenAwaitEvent(EventType.BLOCK_CLICK, e -> e.getHitVec().getBlockPos().equals(new BlockPos(-92, 103, -380)))
                           .appendBuilder(b -> S1.this.appendOldShoeInteraction(inst, b))
                           .thenRun(() -> S1.this.oldShoeInteractionDone = true)
                           .build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.eidosInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenAwaitEvent(EventType.ENTITY_CLICK, e -> e.isOf(S1.EIDOS))
                           .deleteTelepornGlitchEffect(S1.EIDOS)
                           .thenUsePlayer(player -> {
                              Vec3 loc = new Location("/execute in minecraft:overworld run tp @s -10.55 103.00 -464.54 0.00 0.00");
                              UtilST.dropEidos(player.level(), loc, 1, true);
                           })
                           .thenDelay(30)
                           .appendBuilder(b -> S1.this.appendEidosInteraction(inst, b))
                           .thenRun(() -> S1.this.eidosInteractionDone = true)
                           .build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.versiformInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenRun(() -> VersiformNuggetBlock.setLocked(ScriptUtils.getLevel(), new BlockPos(-4, 103, -395), true))
                           .thenAwaitEvent(EventType.BLOCK_CLICK, e -> e.getHitVec().getBlockPos().equals(new BlockPos(-4, 103, -395)))
                           .appendBuilder(b -> S1.this.appendVersiformInteraction(inst, b))
                           .thenRun(() -> S1.this.versiformInteractionDone = true)
                           .build()
                     )
                     .build(),
                  ScriptPerformerBuilder.builder(inst)
                     .thenIfDynamic(
                        () -> !S1.this.keyInteractionDone,
                        new ScriptPerformerBuilder(inst)
                           .thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -64.50 106.00 -505.50 0.00 0.00", 3.0)
                           .appendBuilder(b -> S1.this.appendKeyInteraction(inst, b))
                           .thenRun(() -> S1.this.keyInteractionDone = true)
                           .build()
                     )
                     .build()
               }
            );
            this.saveState("skip");
            this.clearChat();
            this.clearPlayerEmotion();
            this.sendWebWarning("После взаимодействия со всеми объектами (8 шт), нажми F8, чтобы продолжить — будет катсцена");
            this.thenWaitForF8();
         }

         @ScriptPerformerBuilderReflective.ScriptPart(15)
         public void part15(S1 inst) {
            this.saveState("15");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 3.81 103.00 -489.33 67.06 2.92");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.83 103.30 -490.90 55.21 5.17");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 3.50 103.00 -492.85 52.95 0.00");
            this.setSkyEyeLookTarget(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.thenEntityPathParallel(
               CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new String[]{"/execute in minecraft:overworld run tp @s 0.94 103.00 -490.92 52.95 -0.15"}
            );
            this.thenDelay(10);
            this.tdCutsceneMoveCamera(
               50,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.83 103.30 -490.90 70.21 10.12"}
            );
            this.thenDelay(70);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.42 103.30 -488.52 200.56 2.77");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(30);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.58 102.85 -492.75 360.45 -10.88");
            this.thenSpeechMessage(0, "Что ж, вижу, тебе действительно не терпится.");
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.LOUDLY_CRYING_FACE, Emotion.LOUDLY_CRYING_FACE});
            this.thenDelay(20);
            this.clearChat();
            this.sendWebWarningRed("После фразы: \"Что ж, вижу, тебе действительно не терпится.\" — озвучить, как голос вздыхает");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SIGH});
            this.thenDelay(AnimationsST8.SKYEYE_SIGH);
            this.thenSpeechMessage(0, "Очень хорошо.");
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -0.97 103.21 -489.87 -111.75 1.87");
            this.characterEmotion(
               CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.LOUDLY_CRYING_FACE, Emotion.LOUDLY_CRYING_FACE, Emotion.LOUDLY_CRYING_FACE}
            );
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Формики, ваш выход.");
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.tdCutsceneMoveCamera(
               70,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -0.05 103.07 -489.52 224.70 12.07"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(CharactersST8.SPIDER_HEAD_LOLOLOSHKA).build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 53.10 0.00")
                     .build()
               }
            );
            this.action(CharactersST8.HEAD_LOLOLOSHKA_ANT, new IAnimationSource[]{AnimationsST8.CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_OFF_HEAD});
            this.thenDelay(AnimationsST8.CUTSCENE_HEAD_LOLOLOSHKA_ANT_TAKE_OFF_HEAD);
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA_ANT).build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 53.10 0.00")
                     .entitySit(CharactersST8.HEAD_LOLOLOSHKA)
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s 1.24 103.00 -490.13 191.84 0.00")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s 0.51 103.00 -491.02 272.84 0.00")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 1.54 103.00 -491.69 379.94 0.00")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s 2.19 103.00 -490.80 460.33 0.00")
                     .build()
               }
            );
            this.setSkyEyeLookTarget(CharactersST8.HEAD_LOLOLOSHKA);
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 24.03 0.00", true);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -0.86 104.97 -488.69 228.15 41.32");
            this.thenDynamicPerformer(
               b -> {
                  List<ScriptPerformer> list = new ArrayList<>();

                  for (Character<AntEntity> ant : S1.ANTS) {
                     list.add(
                        ScriptPerformerBuilder.builder(inst)
                           .entityLookAt(ant, CharactersST8.SKY_EYE, true)
                           .action(ant, AnimationsST8.ANT_NOD_CYCLE.configure().speed(S1.RANDOM.nextFloat(1.0F, 1.2F)).weight(S1.RANDOM.nextFloat(0.7F, 1.0F)))
                           .thenDelayRandom(30, 40, S1.RANDOM)
                           .stopAction(ant, S1.RANDOM.nextFloat(0.35F, 0.45F))
                           .build()
                     );
                  }

                  b.thenAllPerformers(list.toArray(new ScriptPerformer[0]));
               }
            );
            this.tdCutsceneMoveCamera(
               70,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 2.91 103.44 -493.34 232.35 24.67"}
            );
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(
                        S1.ANTS.get(0),
                        0.46,
                        "/execute in minecraft:overworld run tp @s 2.40 103.00 -489.07 -149.67 5.76\n/execute in minecraft:overworld run tp @s 6.27 103.00 -492.64 147.17 15.96\n/execute in minecraft:overworld run tp @s 5.05 103.00 -494.43 100.37 0.00\n"
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenEntityPath(
                        S1.ANTS.get(1),
                        0.46,
                        "/execute in minecraft:overworld run tp @s -0.04 103.00 -492.29 -118.78 10.11\n/execute in minecraft:overworld run tp @s 1.03 103.00 -495.67 -91.03 8.91\n/execute in minecraft:overworld run tp @s 4.86 103.00 -495.19 17.27 0.00\n"
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .entityLookAtFromRot(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s 1.04 103.00 -491.72 225.49 0.00", true, true)
                     .thenEntityPath(
                        S1.ANTS.get(2),
                        0.42,
                        "/execute in minecraft:overworld run tp @s 1.82 103.00 -493.02 -123.13 9.51\n/execute in minecraft:overworld run tp @s 4.14 103.00 -494.66 262.38 0.00\n"
                     )
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .entityLookAtFromRot(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s 2.08 103.00 -491.25 579.19 6.30", true, true)
                     .thenEntityPath(S1.ANTS.get(3), 0.42, "/execute in minecraft:overworld run tp @s 4.39 103.00 -494.29 190.97 0.00")
                     .build()
               }
            );
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(0)).build(),
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(1)).build(),
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(2)).build(),
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(S1.ANTS.get(3)).build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.NEON_FLOWER_BLACK, "/execute in minecraft:overworld run tp @s 4.50 103.00 -494.50 0.00 0.00")
                     .entitySit(S1.NEON_FLOWER_BLACK)
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(10)
                     .positionEntity(S1.NEON_FLOWER_OTHER, "/execute in minecraft:overworld run tp @s 4.50 103.00 -494.50 0.00 0.00")
                     .entitySit(S1.NEON_FLOWER_OTHER)
                     .action(S1.NEON_FLOWER_OTHER, AnimationsST2.ZERO_SCALE.configure().freezeAt(10.0F).transitionTime(0.0F))
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .thenDelay(15)
                     .tdCutscene("/execute in minecraft:overworld run tp @s 1.29 103.12 -489.63 568.35 8.62")
                     .entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 218.43 0.00")
                     .build()
               }
            );
            this.thenDelay(20);
            this.action(S1.NEON_FLOWER_BLACK, new IAnimationSource[]{AnimationsST2.ZERO_SCALE});
            this.thenDelay(30);
            this.stopAction(S1.NEON_FLOWER_OTHER);
            this.thenDelay(20);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 4.35 103.02 -497.03 19.95 -7.58");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Я ведь говорил: мои помощники умеют многое.");
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 24.03 0.00");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Формики могут принять любую форму.");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.79 102.94 -488.82 194.70 -3.23");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Я вижу, ты запутался — это нормально.");
            this.thenSpeechMessage(0, "Скоро ты всё узнаешь.");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.88 102.85 -492.42 4.95 -12.23");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "А пока… что ты будешь делать теперь, когда у тебя не осталось формиков?");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FLUSHED_FACE});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Именно.");
            this.thenSpeechMessage(0, "Нужно очень хорошо думать, прежде чем–...");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s 4.35 103.02 -497.03 19.95 -7.58");
            this.sendWebWarningRed("После фразы: \"Нужно очень хорошо думать, прежде чем–..\" — озвучить, как голос вздыхает");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SIGH});
            this.thenDelay(AnimationsST8.SKYEYE_SIGH);
            this.thenSpeechMessage(0, "Не волнуйся.");
            this.thenSpeechMessage(0, "Сейчас мы кого-нибудь найдём.");
            this.thenSpeechMessage(0, "Кто-нибудь непременно сможет тебе помочь.");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FOLDED_HANDS});
            this.thenDelay(20);
            this.sendWebWarningRed("После того как глаз посмотрит вправо-влево — озвучь, как он прокашливаться");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_LOOK_RIGHT_TO_LEFT});
            this.thenDelay(AnimationsST8.SKYEYE_LOOK_RIGHT_TO_LEFT);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.88 102.85 -492.42 4.95 -12.23");
            this.thenSpeechMessage(0, "Эй, формики!");
            this.thenSpeechMessage(0, "Здесь есть ещё кто-нибудь?");
            this.clearChat();
            this.tdCutscene("/execute in minecraft:overworld run tp @s 1.47 104.89 -486.16 -182.10 18.67");
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.EYES});
            this.action(CharactersST8.HEAD_LOLOLOSHKA, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ROTATE});
            this.thenDelay(AnimationsST8.HEAD_LOLOLOSHKA_ROTATE);
            this.thenSpeechMessage(0, "…");
            this.thenSpeechMessage(0, "Формики, отзовитесь!");
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 2.13 103.32 -490.02 120.90 12.97");
            this.createTelepornGlitchEffect(CharactersST8.ANT, "/execute in minecraft:overworld run tp @s -6.51 103.00 -494.39 -89.37 0.00");
            this.entityLookAt(CharactersST8.ANT, CharactersST8.HEAD_LOLOLOSHKA, true);
            this.thenSpeechMessage(0, "Вот видишь, кто-то всё-таки нашёлся.", false);
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 115.98 0.00");
            this.tdCutsceneMoveCamera(
               60,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 0.46 103.14 -488.98 190.35 2.77"}
            );
            this.thenEntityPathParallel(CharactersST8.ANT, 0.426, new String[]{"/execute in minecraft:overworld run tp @s 0.45 103.00 -491.39 -59.67 0.00"});
            this.thenDelay(40);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FACE_EXHALING});
            this.sendWebWarningOrange("Когда формик подбежит к тебе и будет прыгать — на монтаже наложить звук, как она пищит");
            this.entityLookAt(CharactersST8.ANT, CharactersST8.HEAD_LOLOLOSHKA, true);
            this.action(CharactersST8.ANT, new IAnimationSource[]{AnimationsST8.ANT_HAPPY_JUMP_CYCLE});
            this.thenDelay(40);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA, new Emotion[]{Emotion.SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Друг мой, помоги ему.");
            this.stopAction(CharactersST8.ANT, 0.5F);
            this.entityLookAt(CharactersST8.ANT, CharactersST8.SKY_EYE, true);
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 0.37 102.96 -490.70 171.30 19.12");
            this.action(CharactersST8.ANT, new IAnimationSource[]{AnimationsST8.ANT_NOD_CYCLE});
            this.thenDelay(AnimationsST8.ANT_NOD_CYCLE);
            this.thenDelay(AnimationsST8.ANT_NOD_CYCLE);
            this.thenDelay(AnimationsST8.ANT_NOD_CYCLE);
            this.stopAction(CharactersST8.ANT);
            this.clearChat();
            this.entityLookAt(CharactersST8.ANT, CharactersST8.HEAD_LOLOLOSHKA, true);
            this.tdCutsceneMoveCamera(
               70,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 1.35 103.54 -488.96 180.75 23.62"}
            );
            this.action(CharactersST8.ANT, new IAnimationSource[]{AnimationsST8.ANT_HAPPY_JUMP_CYCLE});
            this.thenDelay(AnimationsST8.ANT_HAPPY_JUMP_CYCLE);
            this.thenDelay(AnimationsST8.ANT_HAPPY_JUMP_CYCLE);
            this.stopAction(CharactersST8.ANT);
            this.entityLookAtFromRot(CharactersST8.HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s 1.36 103.00 -490.93 218.43 0.00");
            this.thenEntityPath(
               CharactersST8.ANT,
               new String[]{
                  "/execute in minecraft:overworld run tp @s 0.15 103.00 -490.29 -118.02 13.36\n/execute in minecraft:overworld run tp @s 1.37 103.00 -490.40 214.52 0.00\n"
               }
            );
            this.entityLookAtFromRot(CharactersST8.ANT, "/execute in minecraft:overworld run tp @s 1.37 103.00 -490.40 214.52 0.00", true);
            this.action(CharactersST8.ANT, new IAnimationSource[]{AnimationsST8.ANT_HARD_WALK_PUSH});
            this.tdCutsceneMoveCamera(
               140,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s 3.62 103.03 -491.72 182.55 -0.38"}
            );
            this.thenAnyPerformer(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .overridePredefinedAnimation(CharactersST8.ANT, MovementActionType.WALKING, Animations.NULL)
                     .thenEntityMove(CharactersST8.ANT, 0.02, "/execute in minecraft:overworld run tp @s 3.61 103.00 -493.60 578.87 0.00", false)
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .entityStopSit(CharactersST8.HEAD_LOLOLOSHKA)
                     .thenEntityMove(CharactersST8.HEAD_LOLOLOSHKA, 0.02, "/execute in minecraft:overworld run tp @s 3.38 103.00 -493.49 578.42 0.00", false)
                     .build()
               }
            );
            this.thenClearEntityGoal(CharactersST8.ANT);
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(CharactersST8.ANT).build(),
                  new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(CharactersST8.HEAD_LOLOLOSHKA).build()
               }
            );
            this.thenDelay(10);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(16)
         public void part16(S1 inst) {
            this.saveState("16");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S1::drawPlayerGlitchParticles);
            this.freeEntities(true, new Character[]{CharactersST8.HEAD_LOLOLOSHKA, CharactersST8.ANT, S1.NEON_FLOWER_BLACK});
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.thenRunMorph(MorphsST8.HEAD);
            this.positionEntity(S1.ANTS.get(0), "/execute in minecraft:overworld run tp @s -303.54 25.00 -742.50 -0.45 0.00");
            this.importBuilding("s1_lamp_game_on", true);
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenEntityPath(
               S1.ANTS.get(0),
               new String[]{
                  "/execute in minecraft:overworld run tp @s -302.38 25.00 -742.26 6.00 9.00\n/execute in minecraft:overworld run tp @s -302.48 25.00 -740.51 0.75 0.00\n"
               }
            );
            this.entityLookAt(S1.ANTS.get(0), "player", true);
            this.sendWebWarning(
               "После того как тебя телепортирует в персиковое пространство, нужно будет решить три последовательности головоломки. Суть: фонарики будут мигать в определенном порядке, его нужно запомнить и повторить на ПКМ. В эту мини-игру тоже можно будет играть каждый новый внутриигровой день."
            );
            this.thenSpeechMessage(0, "Так ты решить головоломку не сможешь.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Не печалься, сейчас мы позовём ещё формиков.");
            this.thenSpeechMessage(0, "Эй, формики!");
            this.thenAllPerformers(
               new Performer[]{
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s -306.34 25.00 -740.55 -103.20 0.00")
                     .thenEntityPath(S1.ANTS.get(1), "/execute in minecraft:overworld run tp @s -304.01 25.00 -740.90 -139.95 0.00")
                     .entityLookAt(S1.ANTS.get(1), "player")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s -306.89 25.00 -742.67 -71.70 0.00")
                     .thenEntityPath(S1.ANTS.get(2), "/execute in minecraft:overworld run tp @s -303.93 25.00 -742.08 -32.85 0.00")
                     .entityLookAt(S1.ANTS.get(2), "player")
                     .build(),
                  new ScriptPerformerBuilder(inst)
                     .createTelepornGlitchEffect(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s -300.54 25.00 -742.50 74.55 5.70")
                     .thenEntityPath(S1.ANTS.get(3), "/execute in minecraft:overworld run tp @s -303.01 25.00 -742.00 49.35 0.00")
                     .entityLookAt(S1.ANTS.get(3), "player")
                     .build()
               }
            );
            this.thenDelay(70);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(17)
         public void part17(S1 inst) {
            this.saveState("17");
            this.blackout(30);
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT);
            this.freeEntities(new Character[]{S1.ANTS.get(0), S1.ANTS.get(1), S1.ANTS.get(2), S1.ANTS.get(3)});
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -303.11 25.00 -739.56 174.30 8.10");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -303.04 25.58 -738.35 173.10 14.85");
            this.positionEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, "/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.thenDelay(30);
            this.action(CharactersST8.HEAD_LOLOLOSHKA_ANT, new IAnimationSource[]{AnimationsST8.HEAD_LOLOLOSHKA_ANT_TRANSFORM_TO_SPIDER});
            this.thenDelay(10);
            this.tdCutsceneMoveCamera(35, new String[]{"/execute in minecraft:overworld run tp @s -303.04 25.58 -738.35 173.10 3.00"});
            this.thenDelay(20);
            this.characterEmotion(CharactersST8.HEAD_LOLOLOSHKA_ANT, new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(40);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(18)
         public void part18(S1 inst) {
            this.saveState("18");
            this.blackout(60);
            this.clearChat();
            this.freeEntity(CharactersST8.HEAD_LOLOLOSHKA_ANT, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.thenRunMorph(MorphsST8.SPOODER);
            this.thenDelay(100);
            this.saveState("round_1");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S1.shufflePositions();
               S1.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S1.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S1.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S1.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S1.this.appendSuccess(b))
                  .thenRun(() -> S1.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenDelay(20);
            this.saveState("round_2");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S1.shufflePositions();
               S1.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S1.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S1.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S1.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S1.this.appendSuccess(b))
                  .thenRun(() -> S1.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenDelay(20);
            this.saveState("round_3");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S1.shufflePositions();
               S1.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S1.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S1.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S1.this.litAndLockLantern();
                  S1.this.playerStep++;
                  S1.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S1.this.handleLanternClick(event));
            this.thenIf(
               () -> S1.this.lastClickedBlock != null && S1.this.lastClickedBlock.equals(S1.this.currentSequence.get(S1.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S1.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S1.this.appendSuccess(b))
                  .thenRun(() -> S1.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S1.this.appendFail(b)).thenRun(() -> {
                  S1.this.playerStep = 0;
                  S1.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S1.this.getMainPerformer() != null) {
                     S1.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenDelay(20);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(19)
         public void part19(S1 inst) {
            this.saveState("19");
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S1::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 1.50 103.00 -494.50 -90.00 0.00");
            this.setSkyEyeLookTarget("player");
            this.thenRun(() -> CurrencyStorage.setEidos(4));
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenDelay(20);
            this.thenUseEntity(S1.NEON_FLOWER_OTHER, e -> {
               Vec3 pos = e.position();
               UtilST.dropEidos(e.level(), pos, 3, true);
            });
            this.setSimulatorBlocking(MainTabType.MIND, false);
            this.thenSpeechMessage(0, "Удивительно: ты и с этим справился…");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Иди, посмотри, какая у тебя в этот раз награда.");
            this.playerEmotion(new Emotion[]{Emotion.THINKING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "В этот раз это не только эйдосы…", false);
            this.sendWebWarning("После фразы: \"В этот раз это не только эйдосы…\" — зайди в симулятор, посмотри раздел “Познание” и прокомментируй ситуацию");
            this.thenWaitUntil(() -> Minecraft.getInstance().screen instanceof MainTabScreen);
            this.thenWaitUntil(() -> Minecraft.getInstance().screen == null);
            this.playerEmotion(new Emotion[]{Emotion.STAR_STRUCK});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Теперь ты можешь его использовать… частично.");
            this.thenSpeechMessage(0, "И ты сам его разблокировал.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Молодец, молодец.");
            this.playerEmotion(new Emotion[]{Emotion.SMILING_FACE_WITH_SUNGLASSES});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Раз ты так сообразителен, я переведу тебя на первый уровень.");
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Совершенно бесплатно.");
            this.thenSpeechMessage(0, "Но только в этот раз, хорошо?");
         }

         @ScriptPerformerBuilderReflective.ScriptPart(20)
         public void part20(S1 inst) {
            this.saveState("20");
            this.blackout(60);
            this.clearChat();
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.importBuilding("s1_cube_statue_no_cube");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 188.39 254.00 -395.57 223.95 -2.40");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 149.77 272.71 -421.09 289.50 20.40");
            this.positionEntity(S1.CUBE_STATUE, "/execute in minecraft:overworld run tp @s 201.50 565.00 -402.50 0.00 0.00");
            this.thenDelay(60);
            this.sendWebWarning("Нажми F8, чтобы начать катсцену");
            this.thenWaitForF8();
            this.tdCutsceneMoveCamera(
               300, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s 158.97 272.71 -417.85 289.05 24.45"}
            );
            this.thenDelay(60);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 178.03 253.61 -387.90 239.82 -44.81");
            this.sendWebWarningOrange("Когда статуя упадет с неба на гору, на монтаже наложить звук грохота");
            this.thenEntityMove(S1.CUBE_STATUE, 15.0, "/execute in minecraft:overworld run tp @s 201.50 365.00 -402.50 0.00 0.00");
            this.thenDelay(13);
            this.thenUseEntity(
               S1.CUBE_STATUE,
               e -> {
                  Vec3 center = new Vec3(201.0, 265.0, -403.0);
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                     new Vec3(201.0, 300.0, -401.0),
                     400,
                     13.0,
                     15.0,
                     13.0,
                     0.4,
                     30.0F * (0.9F + S1.RANDOM.nextFloat() * 0.2F),
                     50,
                     new Color(90 + S1.RANDOM.nextInt(21) - 10, 90 + S1.RANDOM.nextInt(21) - 10, 90 + S1.RANDOM.nextInt(21) - 10),
                     0.7F
                  );
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.LARGE_SMOKE,
                     center,
                     500,
                     10.0,
                     20.0,
                     10.0,
                     0.5,
                     10.0F * (0.9F + S1.RANDOM.nextFloat() * 0.2F),
                     120,
                     new Color(60 + S1.RANDOM.nextInt(21) - 10, 60 + S1.RANDOM.nextInt(21) - 10, 60 + S1.RANDOM.nextInt(21) - 10),
                     0.9F
                  );
                  ParticleHelper.spawnEnginedParticles(
                     e.level(),
                     ParticleTypes.ASH,
                     center,
                     250,
                     4.0,
                     1.0,
                     4.0,
                     0.7,
                     30.0F * (0.9F + S1.RANDOM.nextFloat() * 0.2F),
                     100,
                     new Color(150 + S1.RANDOM.nextInt(21) - 10, 140 + S1.RANDOM.nextInt(21) - 10, 130 + S1.RANDOM.nextInt(21) - 10),
                     0.3F
                  );
               }
            );
            this.importBuilding("s1_cube_statue");
            this.freeEntity(S1.CUBE_STATUE);
            this.thenDelay(40);
            this.tdCutscene("/execute in minecraft:overworld run tp @s 135.59 276.10 -389.60 260.67 -0.26");
            this.tdCutsceneMoveCamera(
               300, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s 122.14 276.10 -387.39 260.67 -0.26"}
            );
            this.thenDelay(60);
            this.blackout(30);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -23.68 103.00 -480.23 271.01 1.35");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -21.70 102.79 -481.22 -79.99 -23.25");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -22.85 103.00 -482.27 289.31 -37.50");
            this.thenDelay(30);
            this.tdCutsceneMoveCamera(
               80,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -24.76 102.79 -481.73 -88.09 -17.10"}
            );
            this.thenDelay(80);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.FACE_SCREAMING_IN_FEAR});
            this.thenDelay(40);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(21)
         public void part21(S1 inst) {
            this.saveState("21");
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -22.85 103.00 -482.27 289.31 -37.50");
            this.thenRun(() -> TributeOfferStorage.setOffer(TributeOffersST.FIRST, 0));
            this.thenDelay(80);
            this.thenSpeechMessage(0, "А теперь, друг мой, позволь спросить…");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Какое счастье ты желаешь сегодня?");
            this.playerEmotion(new Emotion[]{Emotion.EYES});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Хочешь стать разумнее?");
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Или, может быть, сильнее?");
            this.playerEmotion(new Emotion[]{Emotion.FOLDED_HANDS});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Или же предпочитаешь воспитать характер?..");
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Выбери что-то одно.");
            this.playerEmotion(new Emotion[]{Emotion.PLEADING_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Всё сразу получить нельзя.");
            this.thenSpeechMessage(0, "Не волнуйся, у нас счастья на всех хватит.");
            this.thenSpeechMessage(0, "Так что ты ещё успеешь стать и умным, и сильным, и волевым.");
            this.clearChat();
            this.clearPlayerEmotion();
            this.dialog(
               new Dialog(1, CharactersST8.SKY_EYE)
                  .setTalkingTo(CharactersST8.SKY_EYE)
                  .addText("Но сейчас… какое счастье ты желаешь сегодня?")
                  .addAnswer(Component.literal("[Разум]").withStyle(ChatFormatting.BLUE), false)
                  .addAnswer(Component.literal("[Тело]").withStyle(ChatFormatting.LIGHT_PURPLE), true)
                  .addAnswer(Component.literal("[Воля]").withStyle(ChatFormatting.DARK_GREEN), true)
            );
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_INTEREST});
            this.thenSpeechMessage(0, "Разум?");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Что ж, замечательно…");
            this.thenSpeechMessage(0, "Поздравляю.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_SATISFACTION});
            this.thenSpeechMessage(0, "Это весьма похвальный выбор.");
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_BASE});
            this.thenSpeechMessage(0, "Что ж, на этом я вынужден тебя покинуть.");
            this.playerEmotion(new Emotion[]{Emotion.ANGUISHED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "У меня много других дел.");
            this.playerEmotion(new Emotion[]{Emotion.WEARY_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Но я обязательно вернусь.");
            this.playerEmotion(new Emotion[]{Emotion.SAD_BUT_RELIEVED_FACE});
            this.thenDelay(20);
            this.thenSpeechMessage(0, "Твои новые друзья формики останутся с тобой.");
            this.clearChat();
            this.playerEmotion(new Emotion[]{Emotion.SLIGHTLY_SMILING_FACE});
            this.thenDelay(20);
            this.action(CharactersST8.SKY_EYE, new IAnimationSource[]{AnimationsST8.SKYEYE_CLOSE});
            this.sendWebWarning("После того как глаз закроется, можешь прокомментировать ситуацию, походить туда-сюда, но больше ничего происходить не будет");
            this.sendWebWarning("Скрипт все!");
            this.thenInfiniteSleep();
            this.timeMarker("12:00");
         }
      })
      .build(this::registerMainPerformer);

   public S1(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @Override
   public void updateAsPlayer(ServerPlayer player) {
      super.updateAsPlayer(player);
   }

   @Override
   public void handleStop() {
      this.getRepository().removeAll();
      super.handleStop();
   }

   @Override
   public void handleStart() {
      super.handleStart();
   }

   @Override
   public ScriptPerformer handleSetup() {
      return new ScriptPerformerBuilder(this)
         .appendBuilder(
            ScriptUses.predownloadStructures(
               "s1_cube_statue_just_cube",
               "s1_cube_statue_no_cube",
               "s1_cube_statue",
               "s1_pacman_bot",
               "s1_dorone",
               "s1_red_packman",
               "s1_ruda",
               "s1_key",
               "s1_sapog"
            )
         )
         .appendBuilder(
            ScriptUses.createStructureImportSetup("s1_lololowka_head", "s1_lilac_room", "s1_pacman", "s1_peach_room", "s1_lamp_game_on", "s1_simulyator")
         )
         .build();
   }

   @SubscribeEvent
   public void onPlayerHurt(LivingHurtEvent event) {
      if (event.getEntity() instanceof Player player) {
         DamageSource var4 = event.getSource();
         if ("drown".equals(var4.getMsgId())) {
            this.flags.set("drown_death");
         }
      }
   }

   @Trigger("completeAllInteraction")
   public void completeAllInteractions() {
      this.isFirstInteractionDone = true;
      this.mushroomInteractionDone = true;
      this.grassInteractionDone = true;
      this.statueInteractionDone = true;
      this.drownInteractionDone = true;
      this.oldShoeInteractionDone = true;
      this.eidosInteractionDone = true;
      this.versiformInteractionDone = true;
      this.keyInteractionDone = true;
      if (this.getMainPerformer() != null) {
         this.getMainPerformer().gotoSavepoint("skip");
      }
   }

   private static void drawPlayerGlitchParticles(ServerPlayer player) {
      EntityDimensions dim = player.getType().getDimensions();
      Vec3 position = player.position();
      double expansion = 0.9;
      double halfWidth = (double)dim.width / 2.0 + expansion;
      double minX = position.x - halfWidth;
      double maxX = position.x + halfWidth;
      double minY = position.y;
      double maxY = position.y + (double)dim.height + expansion;
      double minZ = position.z - halfWidth;
      double maxZ = position.z + halfWidth;

      for (int i = 0; i < 8; i++) {
         Location particleLocation = new Location(RANDOM.nextDouble(minX, maxX), RANDOM.nextDouble(minY, maxY), RANDOM.nextDouble(minZ, maxZ));
         ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
      }
   }

   private void appendAntsIdle(ScriptInstance inst, ScriptPerformerBuilder builder, Character<AntGroupEntity> entity) {
      builder.thenDynamicPerformer(
         b -> {
            IAnimationSource[] anim = new IAnimationSource[]{
               AnimationsST8.ANT_IDLE_MODEL2,
               AnimationsST8.ANT_IDLE_MODEL3,
               AnimationsST8.ANT_IDLE_MODEL4,
               AnimationsST8.ANT_IDLE_MODEL5,
               AnimationsST8.ANT_IDLE_MODEL6,
               AnimationsST8.ANT_IDLE_MODEL7,
               AnimationsST8.ANT_IDLE_MODEL8,
               AnimationsST8.ANT_IDLE_MODEL9
            };

            for (int i = 0; i < anim.length; i++) {
               b.animation(entity, i + 2, anim[i].configure().speed(RANDOM.nextFloat(0.7F, 1.0F)).weight(RANDOM.nextFloat(0.6F, 0.8F)));
            }
         }
      );
   }

   private void appendFirstInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.sendWebWarningRed("На фразе: \"Сюда тебе пока нельзя.\" — голос озвучивать назидательно")
         .thenSpeechMessage(0, "Сюда тебе пока нельзя.")
         .playerEmotion(Emotion.WEARY_FACE)
         .thenDelay(20)
         .thenSpeechMessage(0, "Всему своё время.")
         .thenSpeechMessage(0, "Погуляй ещё немного.", false)
         .thenDelay(50)
         .playerEmotion(Emotion.FACE_EXHALING);
   }

   private void appendSecondInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.thenSpeechMessage(0, "И сюда тоже пока нельзя, друг мой, ты уж прости.")
         .addPlayerEmotion(Emotion.PENSIVE_FACE)
         .thenDelay(20)
         .thenSpeechMessage(0, "Пока не получится: не время.")
         .thenSpeechMessage(0, "Иди, иди, посмотри, что ещё тут есть интересного.", false);
   }

   private void appendStatueInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.thenSpeechMessage(0, "Вижу, ты нашёл её.")
         .playerEmotion(Emotion.EYES)
         .thenDelay(20)
         .thenSpeechMessage(0, "Я понимаю, тебе интересно.")
         .thenSpeechMessage(0, "Но нужно потрудиться, чтобы она предстала перед тобой во всём своём великолепии.")
         .playerEmotion(Emotion.THINKING_FACE);
   }

   private void appendDrown(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.action(CharactersST8.SKY_EYE, AnimationsST8.SKYEYE_FEAR)
         .thenSpeechMessage(0, "Подожди, стой, куда ты?!..", false)
         .thenWhenPlayerCloseTo("/execute in minecraft:overworld run tp @s -28.50 103.00 -488.50 0.00 0.00", 5.0)
         .thenAwaitEvent(EventType.PLAYER_UPDATE, e -> !e.isInvulnerable())
         .thenRun(() -> FragmentInstanceManager.addActive(FragmentsST.DANGEROUS))
         .playerEmotion(Emotion.FACE_WITH_PEEKING_EYE)
         .thenDelay(20)
         .sendWebWarningRed("Когда ты утонешь — фразы голоса читать с легким недовольством")
         .action(CharactersST8.SKY_EYE, AnimationsST8.SKYEYE_SLIGHT_ANGRY)
         .thenSpeechMessage(0, "Да, всё правильно: это опасно.")
         .thenSpeechMessage(0, "Ты ведь не умеешь плавать.")
         .action(CharactersST8.SKY_EYE, AnimationsST8.SKYEYE_BASE)
         .thenSpeechMessage(0, "Будь осторожнее.")
         .thenSpeechMessage(0, "Мы за тобой присматриваем, но и ты учись.", false);
   }

   private void appendOldShoeInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.playerEmotion(Emotion.SAD_BUT_RELIEVED_FACE)
         .thenDelay(20)
         .thenSpeechMessage(0, "Да, так себе находка.")
         .thenSpeechMessage(0, "Хотя, может быть, это сейчас так кажется.")
         .thenSpeechMessage(0, "Никогда не знаешь, что может пригодиться.", false);
   }

   private void appendEidosInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.playerEmotion(Emotion.SLIGHTLY_SMILING_FACE)
         .thenDelay(20)
         .thenSpeechMessage(0, "Вижу, тебе повезло.")
         .thenSpeechMessage(0, "Исследовать окружающий мир бывает полезно.", false);
   }

   private void appendVersiformInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.playerEmotion(Emotion.EYES)
         .thenDelay(20)
         .thenSpeechMessage(0, "Что это у тебя?..")
         .thenSpeechMessage(0, "А-а, версиформ.")
         .thenSpeechMessage(0, "Полезная, очень полезная штука.", false);
   }

   private void appendKeyInteraction(ScriptInstance inst, ScriptPerformerBuilder builder) {
      builder.playerEmotion(Emotion.SMILING_FACE_WITH_HEART_EYES)
         .thenDelay(20)
         .thenSpeechMessage(0, "Какой красивый ты ключ нашёл.")
         .thenSpeechMessage(0, "Интересно, от чего он?..", false);
   }

   private void appendBlockPlacedSuccessful(ScriptInstance inst, ScriptPerformerBuilder builder, S1.MinigameData data) {
      builder.thenAllPerformers(
         new Performer[]{
            new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(data.character()).build(),
            new ScriptPerformerBuilder(inst)
               .thenDelay(10)
               .thenRun(() -> ScriptUtils.getLevel().setBlockAndUpdate(data.targetPos(), data.platformWithBlock().defaultBlockState()))
               .build()
         }
      );
   }

   private boolean hasAtLeastBlocks(int targetCount) {
      int count = 0;

      for (BlockPos pos : BLOCKS_AREA) {
         Block block = ScriptUtils.getLevel().getBlockState(pos).getBlock();
         if (block instanceof PlatformWithBlock) {
            count++;
         }
      }

      return count >= targetCount;
   }

   public static List<BlockPos> shufflePositions() {
      List<BlockPos> shuffled = new ArrayList<>(LANTERN_POSITIONS);
      Collections.shuffle(shuffled);
      return shuffled;
   }

   private void unlitAntUnlockLanternsAndReset() {
      this.playerStep = 0;
      this.lastClickedBlock = null;

      for (BlockPos pos : LANTERN_POSITIONS) {
         this.setLitLantern(pos, false);
         this.setLockedLantern(pos, false);
      }
   }

   private void litAndLockLantern() {
      this.setLitLantern(this.lastClickedBlock, true);
      this.setLockedLantern(this.lastClickedBlock, true);
   }

   private boolean handleLanternClick(RightClickBlock event) {
      BlockPos clickedPos = event.getHitVec().getBlockPos();
      Level level = event.getLevel();
      BlockState state = level.getBlockState(clickedPos);
      if (!(state.getBlock() instanceof JapanLanternBlock)) {
         return false;
      } else {
         this.lastClickedBlock = clickedPos;
         return true;
      }
   }

   private void appendFail(ScriptPerformerBuilder builder) {
      builder.thenDynamicPerformer(b -> {
         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLockedLantern(pos, true));
         }

         for (int i = 0; i < 3; i++) {
            for (BlockPos pos : LANTERN_POSITIONS) {
               b.thenRun(() -> this.setLitLantern(pos, true));
            }

            b.thenDelay(10);

            for (BlockPos pos : LANTERN_POSITIONS) {
               b.thenRun(() -> this.setLitLantern(pos, false));
            }

            b.thenDelay(10);
         }
      });
   }

   private void appendSuccess(ScriptPerformerBuilder builder) {
      builder.thenDynamicPerformer(b -> {
         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> {
               this.setLockedLantern(pos, true);
               this.setLitLantern(pos, false);
            });
         }

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, true));
            b.thenDelay(5);
         }

         b.thenDelay(5);

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, false));
         }

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, true));
            b.thenDelay(5);
         }

         b.thenDelay(5);

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, false));
         }

         b.thenDelay(10);

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, true));
         }

         b.thenDelay(10);

         for (BlockPos pos : LANTERN_POSITIONS) {
            b.thenRun(() -> this.setLitLantern(pos, false));
         }
      });
   }

   private void setLitLantern(BlockPos pos, boolean lit) {
      JapanLanternBlock.setLit(ScriptUtils.getLevel(), pos, lit);
   }

   private void setLockedLantern(BlockPos pos, boolean locked) {
      JapanLanternBlock.setLocked(ScriptUtils.getLevel(), pos, locked);
   }

   static record MinigameData(Character<AnimatedEntity> character, String startPos, BlockPos targetPos, Block platformWithBlock, Block platform) {
      boolean isEntityAboveBlock() {
         Entity e = this.character.repositoryEntity();
         if (e == null) {
            return false;
         } else {
            Vec3 center = e.position();
            Vec3 targetCenter = this.targetPos.getCenter();
            double tolerance = 0.1;
            boolean aboveX = Math.abs(center.x - targetCenter.x) < tolerance;
            boolean aboveZ = Math.abs(center.z - targetCenter.z) < tolerance;
            return aboveX && aboveZ;
         }
      }
   }
}
