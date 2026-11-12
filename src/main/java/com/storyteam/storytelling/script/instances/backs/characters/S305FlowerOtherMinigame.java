package com.storyteam.storytelling.script.instances.backs.characters;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.blocks.JapanLanternBlock;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.base.FlowerOtherMinigameEndEvent;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(
   value = -305,
   description = "Мини-игра Simon (цветок)"
)
public class S305FlowerOtherMinigame extends BackInstance {
   public static final int SCRIPT_ID = -305;
   private static final Character<AnimatedEntity> NEON_FLOWER_OTHER = Character.builder("s305_neon_flower_other", Cast.constant(EntitiesST.NEON_FLOWER_OTHER))
      .name("Цветок")
      .build();
   private static final List<BlockPos> LANTERN_POSITIONS = List.of(
      new BlockPos(-301, 25, -738), new BlockPos(-303, 25, -738), new BlockPos(-305, 25, -738), new BlockPos(-307, 25, -738)
   );
   private int playerStep = 0;
   private List<BlockPos> currentSequence = new ArrayList<>();
   private BlockPos lastClickedBlock = null;
   private static boolean canPlayToday = true;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S305FlowerOtherMinigame inst) {
            this.positionEntity(S305FlowerOtherMinigame.NEON_FLOWER_OTHER, "/execute in minecraft:overworld run tp @s 4.50 103.00 -494.50 0.00 0.00");
            this.entitySit(S305FlowerOtherMinigame.NEON_FLOWER_OTHER);
            this.label("s305_before_playing", false);
            this.thenWaitUntil(() -> S305FlowerOtherMinigame.canPlayToday);
            this.thenAwaitEvent(EventType.ENTITY_CLICK, e -> e.isOf(S305FlowerOtherMinigame.NEON_FLOWER_OTHER));
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         public void part1(S305FlowerOtherMinigame inst) {
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S305FlowerOtherMinigame::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.deleteTicker("teleporn");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -303.50 27.12 -744.70 0.00 26.70");
            this.importBuilding("s1_lamp_game_on");
            this.thenDelay(60);
            this.createTelepornGlitchEffect(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, "/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.thenDelay(30);
            this.blackout(60);
            this.clearChat();
            this.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, true);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -303.50 25.00 -741.50 0.00 0.00");
            this.thenDelay(80);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         public void part2(S305FlowerOtherMinigame inst) {
            this.saveState("round_1");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S305FlowerOtherMinigame.shufflePositions();
               S305FlowerOtherMinigame.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S305FlowerOtherMinigame.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S305FlowerOtherMinigame.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S305FlowerOtherMinigame.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S305FlowerOtherMinigame.this.appendSuccess(b))
                  .thenRun(() -> S305FlowerOtherMinigame.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_1");
                  }
               }).build()
            );
            this.thenDelay(20);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         public void part3(S305FlowerOtherMinigame inst) {
            this.saveState("round_2");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S305FlowerOtherMinigame.shufflePositions();
               S305FlowerOtherMinigame.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S305FlowerOtherMinigame.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S305FlowerOtherMinigame.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S305FlowerOtherMinigame.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S305FlowerOtherMinigame.this.appendSuccess(b))
                  .thenRun(() -> S305FlowerOtherMinigame.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_2");
                  }
               }).build()
            );
            this.thenDelay(20);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(4)
         public void part4(S305FlowerOtherMinigame inst) {
            this.saveState("round_3");
            this.thenDynamicPerformer(b -> {
               List<BlockPos> randomOrder = S305FlowerOtherMinigame.shufflePositions();
               S305FlowerOtherMinigame.this.currentSequence = randomOrder;

               for (BlockPos pos : randomOrder) {
                  b.thenRun(() -> S305FlowerOtherMinigame.this.setLitLantern(pos, true));
                  b.thenDelay(20);
               }
            });
            this.thenRun(() -> S305FlowerOtherMinigame.this.unlitAntUnlockLanternsAndReset());
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst).thenRun(() -> {
                  S305FlowerOtherMinigame.this.litAndLockLantern();
                  S305FlowerOtherMinigame.this.playerStep++;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenAwaitEvent(EventType.BLOCK_CLICK, event -> S305FlowerOtherMinigame.this.handleLanternClick(event));
            this.thenIf(
               () -> S305FlowerOtherMinigame.this.lastClickedBlock != null
                     && S305FlowerOtherMinigame.this.lastClickedBlock
                        .equals(S305FlowerOtherMinigame.this.currentSequence.get(S305FlowerOtherMinigame.this.playerStep)),
               new ScriptPerformerBuilder(inst)
                  .thenRun(() -> S305FlowerOtherMinigame.this.litAndLockLantern())
                  .thenDelay(5)
                  .appendBuilder(b -> S305FlowerOtherMinigame.this.appendSuccess(b))
                  .thenRun(() -> S305FlowerOtherMinigame.this.currentSequence.clear())
                  .build(),
               new ScriptPerformerBuilder(inst).appendBuilder(b -> S305FlowerOtherMinigame.this.appendFail(b)).thenRun(() -> {
                  S305FlowerOtherMinigame.this.playerStep = 0;
                  S305FlowerOtherMinigame.this.lastClickedBlock = null;
               }).thenRun(() -> {
                  if (S305FlowerOtherMinigame.this.getMainPerformer() != null) {
                     S305FlowerOtherMinigame.this.getMainPerformer().gotoSavepoint("round_3");
                  }
               }).build()
            );
            this.thenDelay(20);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(5)
         public void part5(S305FlowerOtherMinigame inst) {
            this.clearChat();
            this.createTicker("teleporn", S305FlowerOtherMinigame::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 1.50 103.00 -494.50 -90.00 0.00");
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenDelay(20);
            this.thenUseEntity(S305FlowerOtherMinigame.NEON_FLOWER_OTHER, e -> {
               Vec3 pos = e.position();
               UtilST.dropEidos(e.level(), pos, 3, true);
            });
            this.thenRun(() -> S305FlowerOtherMinigame.canPlayToday = false);
            this.thenRun(() -> {
               FlowerOtherMinigameEndEvent event = new FlowerOtherMinigameEndEvent(ScriptUtils.getPlayer(), true);
               MinecraftForge.EVENT_BUS.post(event);
               IStoryServer.get().postStoryEvent(EventType.FLOWER_OTHER_MINIGAME_END_EVENT, event);
            });
            this.gotoLabel("s305_before_playing", true);
         }
      })
      .build(this::registerMainPerformer);

   public S305FlowerOtherMinigame(ScriptEnvironment env, int id) {
      super(env, id);
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
}
