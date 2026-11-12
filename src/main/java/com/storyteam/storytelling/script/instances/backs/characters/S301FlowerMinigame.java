package com.storyteam.storytelling.script.instances.backs.characters;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.base.FlowerMinigameEndEvent;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.system._core.st_cutscene.InterpolationType;
import com.storyteam.storytelling.system._core.st_cutscene.SplineType;
import com.storyteam.storytelling.system.emotions.Emotion;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.PositionParser;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(
   value = -301,
   description = "Мини-игра с толканием блоков (цветок)"
)
public class S301FlowerMinigame extends BackInstance {
   public static final int SCRIPT_ID = -301;
   private static final Character<AnimatedEntity> NEON_FLOWER = Character.builder("s301_neon_flower", Cast.constant(EntitiesST.NEON_FLOWER))
      .name("Цветок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_GREEN = Character.builder("s301_block_green", Cast.constant(EntitiesST.BLOCK_GREEN))
      .name("Зеленый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_ORANGE = Character.builder("s301_block_orange", Cast.constant(EntitiesST.BLOCK_ORANGE))
      .name("Оранжевый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_WHITE = Character.builder("s301_block_white", Cast.constant(EntitiesST.BLOCK_WHITE))
      .name("Белый блок")
      .build();
   private static final Character<AnimatedEntity> BLOCK_YELLOW = Character.builder("s301_block_yellow", Cast.constant(EntitiesST.BLOCK_YELLOW))
      .name("Желтый блок")
      .build();
   private static final List<String> START_POSITIONS = List.of(
      "/execute in minecraft:overworld run tp @s -345.50 25.00 -736.50 0.00 0.00",
      "/execute in minecraft:overworld run tp @s -341.50 25.00 -736.50 0.00 0.00",
      "/execute in minecraft:overworld run tp @s -339.50 25.00 -736.50 0.00 0.00",
      "/execute in minecraft:overworld run tp @s -343.50 25.00 -736.50 0.00 0.00"
   );
   private static final List<String> TARGET_POSITIONS = List.of(
      "/setblock -340 25 -745", "/setblock -342 25 -745", "/setblock -344 25 -745", "/setblock -346 25 -745"
   );
   private List<S301FlowerMinigame.MinigameData> BLOCKS = new ArrayList<>();
   private static boolean canPlayToday = true;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S301FlowerMinigame inst) {
            this.positionEntity(S301FlowerMinigame.NEON_FLOWER, "/execute in minecraft:overworld run tp @s -5.50 103.00 -446.50 0.00 0.00");
            this.entitySit(S301FlowerMinigame.NEON_FLOWER);
            this.label("s301_before_playing", false);
            this.thenWaitUntil(() -> S301FlowerMinigame.canPlayToday);
            this.thenAwaitEvent(EventType.ENTITY_CLICK, e -> e.isOf(S301FlowerMinigame.NEON_FLOWER));
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         public void part1(S301FlowerMinigame inst) {
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S301FlowerMinigame::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.deleteTicker("teleporn");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 0.00 0.00");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 33.00 -740.50 0.00 90.00");
            this.thenDynamicPerformer(b -> {
               for (S301FlowerMinigame.MinigameData data : S301FlowerMinigame.this.BLOCKS) {
                  b.positionEntity(data.character(), data.startPos());
                  b.thenRun(() -> ScriptUtils.getLevel().setBlockAndUpdate(data.targetPos(), data.platform().defaultBlockState()));
               }
            });
            this.thenDelay(60);
            this.tdCutsceneMoveCamera(
               800, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -342.50 31.00 -740.50 0.00 90.00"}
            );
            this.thenDelay(20);
            this.createTelepornGlitchEffect(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 0.00 0.00");
            this.thenDelay(60);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         public void part2(S301FlowerMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 0.00 0.00");
            this.thenDynamicPerformer(
               b -> {
                  List<ScriptPerformer> performers = new ArrayList<>();

                  for (S301FlowerMinigame.MinigameData data : S301FlowerMinigame.this.BLOCKS) {
                     performers.add(
                        ScriptPerformerBuilder.builder(inst)
                           .thenWaitUntil(data::isEntityAboveBlock)
                           .appendBuilder(builder -> S301FlowerMinigame.this.appendSuccess(inst, builder, data))
                           .build()
                     );
                  }

                  b.thenAllPerformers(performers.toArray(new ScriptPerformer[0]));
               }
            );
            this.thenDelay(40);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         public void part3(S301FlowerMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.clearPlayerEmotion();
            this.tdCutscene("/execute in minecraft:overworld run tp @s -345.50 25.00 -743.50 -180.00 15.00");
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -342.50 25.00 -740.50 180.00 0.00");
            this.thenDelay(60);
            this.tdCutsceneMoveCamera(
               80, SplineType.LINEAR, InterpolationType.LINEAR, new String[]{"/execute in minecraft:overworld run tp @s -339.50 25.00 -743.50 -180.00 15.00"}
            );
            this.thenDelay(90);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 25.30 -742.50 0.00 10.00");
            this.thenDelay(5);
            this.tdCutsceneMoveCamera(
               20,
               SplineType.CATMULLROM,
               InterpolationType.EASE_IN_SINE,
               new String[]{"/execute in minecraft:overworld run tp @s -342.50 25.30 -742.50 0.00 0.00"}
            );
            this.thenDelay(15);
            this.characterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, new Emotion[]{Emotion.GRINNING_FACE_WITH_SMILING_EYES});
            this.thenDelay(40);
            this.tdCutscene("/execute in minecraft:overworld run tp @s -342.50 26.00 -738.50 -180.00 25.00");
            this.clearCharacterEmotion(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.deleteTelepornGlitchEffect(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            this.thenDelay(30);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(4)
         public void part4(S301FlowerMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.createTicker("teleporn", S301FlowerMinigame::drawPlayerGlitchParticles);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -8.50 103.00 -446.50 -90.00 0.00");
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenDelay(20);
            this.thenUseEntity(S301FlowerMinigame.NEON_FLOWER, e -> {
               Vec3 pos = e.position();
               UtilST.dropEidos(e.level(), pos, 3, true);
            });
            this.thenRun(() -> S301FlowerMinigame.canPlayToday = false);
            this.thenRun(() -> {
               FlowerMinigameEndEvent event = new FlowerMinigameEndEvent(ScriptUtils.getPlayer(), true);
               MinecraftForge.EVENT_BUS.post(event);
               IStoryServer.get().postStoryEvent(EventType.FLOWER_MINIGAME_END_EVENT, event);
            });
            this.gotoLabel("s301_before_playing", true);
         }
      })
      .build(this::registerMainPerformer);

   private List<S301FlowerMinigame.MinigameData> generateBlocks() {
      List<String> start = new ArrayList<>(START_POSITIONS);
      List<BlockPos> target = new ArrayList<>(TARGET_POSITIONS.stream().map(PositionParser::parseBlockPos).filter(Objects::nonNull).toList());
      Collections.shuffle(start);
      Collections.shuffle(target);
      return List.of(
         new S301FlowerMinigame.MinigameData(BLOCK_ORANGE, start.get(0), target.get(0), BlocksST.PLATFORM_WITH_BLOCK_ORANGE, BlocksST.PLATFORM_ORANGE),
         new S301FlowerMinigame.MinigameData(BLOCK_YELLOW, start.get(1), target.get(1), BlocksST.PLATFORM_WITH_BLOCK_YELLOW, BlocksST.PLATFORM_YELLOW),
         new S301FlowerMinigame.MinigameData(BLOCK_GREEN, start.get(2), target.get(2), BlocksST.PLATFORM_WITH_BLOCK_GREEN, BlocksST.PLATFORM_GREEN),
         new S301FlowerMinigame.MinigameData(BLOCK_WHITE, start.get(3), target.get(3), BlocksST.PLATFORM_WITH_BLOCK_WHITE, BlocksST.PLATFORM_WHITE)
      );
   }

   public S301FlowerMinigame(ScriptEnvironment env, int id) {
      super(env, id);
      this.BLOCKS = this.generateBlocks();
   }

   @SubscribeEvent
   public void updatePlayOnNewDay(NewDayStartEvent event) {
      canPlayToday = true;
   }

   private static void drawPlayerGlitchParticles(ServerPlayer player) {
      Random random = new Random();
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
         Location particleLocation = new Location(random.nextDouble(minX, maxX), random.nextDouble(minY, maxY), random.nextDouble(minZ, maxZ));
         ParticleHelper.spawnDirectedParticle(ScriptUtils.getLevel(), ParticleTypesST.GLITCH, particleLocation, Vec3.ZERO);
      }
   }

   private void appendSuccess(ScriptInstance inst, ScriptPerformerBuilder builder, S301FlowerMinigame.MinigameData data) {
      builder.thenAllPerformers(
            new Performer[]{
               new ScriptPerformerBuilder(inst).deleteTelepornGlitchEffect(data.character()).build(),
               new ScriptPerformerBuilder(inst)
                  .thenDelay(10)
                  .thenRun(() -> ScriptUtils.getLevel().setBlockAndUpdate(data.targetPos(), data.platformWithBlock().defaultBlockState()))
                  .build()
            }
         )
         .playerEmotion(Emotion.FACE_EXHALING);
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
