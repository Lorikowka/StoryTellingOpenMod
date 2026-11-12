package com.storyteam.storytelling.script.instances.backs.characters;

import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.blocks.DotPackmanBlock;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.events.base.StoneMinigameEndEvent;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ParticleTypesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.time.event.NewDayStartEvent;
import com.storyteam.storytelling.util.ParticleHelper;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(
   value = -302,
   description = "Мини-игра пакман (камень)"
)
public class S302StoneMinigame extends BackInstance {
   public static final int SCRIPT_ID = -302;
   private static final Character<AnimatedEntity> NEON_STONE = Character.builder("s302_neon_stone", Cast.constant(EntitiesST.NEON_STONE))
      .name("Камень")
      .build();
   private static final Character<AnimatedEntity> SPIDER_HEAD_LOLOLOSHKA_BIG = Character.builder(
         "s302_spider_head_lololoshka_big", Cast.constant(EntitiesST.SPIDER_HEAD_LOLOLOSHKA_BIG)
      )
      .name("Lololowka")
      .build();
   private static final AABB GREEN_MAZE_AREA = new AABB(new Vec3(-21.0, -50.0, -946.0), new Vec3(50.0, -49.0, -895.0));
   private static final AABB BLUE_MAZE_AREA = new AABB(new Vec3(99.0, -50.0, -946.0), new Vec3(170.0, -49.0, -895.0));
   private static final AABB RED_MAZE_AREA = new AABB(new Vec3(-147.0, -50.0, -946.0), new Vec3(-77.0, -49.0, -896.0));
   private static boolean canPlayToday = true;
   private Vec3 currentMovement = Vec3.ZERO;
   private float currentYaw = 0.0F;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S302StoneMinigame inst) {
            this.positionEntity(S302StoneMinigame.NEON_STONE, "/execute in minecraft:overworld run tp @s -61.50 103.00 -469.50 -90.00 0.00");
            this.entitySit(S302StoneMinigame.NEON_STONE);
            this.label("s302_before_playing", false);
            this.thenWaitUntil(() -> S302StoneMinigame.canPlayToday);
            this.thenAwaitEvent(EventType.ENTITY_CLICK, e -> e.isOf(S302StoneMinigame.NEON_STONE));
         }

         @ScriptPerformerBuilderReflective.ScriptPart(1)
         public void part1(S302StoneMinigame inst) {
            this.clearChat();
            this.clearPlayerEmotion();
            this.createTicker("teleporn", S302StoneMinigame::drawPlayerGlitchParticles);
            this.thenDelay(30);
            this.screenColor(0, 20, 60, 20);
            this.thenDelay(20);
            this.deleteTicker("teleporn");
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 134.50 -50.00 -920.50 0.00 0.00");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 134.50 -5.80 -920.50 0.00 90.00");
            this.importBuilding("s1_pacman_bot");
            this.importBuilding("s1_dorone");
            this.importBuilding("s1_red_packman");
            this.thenDelay(70);
            this.createTelepornGlitchEffect(
               S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG, "/execute in minecraft:overworld run tp @s 134.50 -50.00 -920.50 0.00 0.00"
            );
            this.thenHandleState(S302StoneMinigame.this.createFlag("play_game"));
            this.thenWaitUntil(() -> S302StoneMinigame.isAreaCleared(S302StoneMinigame.BLUE_MAZE_AREA));
            this.thenHandleState(S302StoneMinigame.this.clearFlag("play_game"));
            this.thenDelay(20);
            this.deleteTelepornGlitchEffect(S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(2)
         public void part2(S302StoneMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s 14.50 -50.00 -920.50 0.00 0.00");
            this.tdCutscene("/execute in minecraft:overworld run tp @s 14.50 -5.80 -920.50 0.00 90.00");
            this.thenDelay(70);
            this.createTelepornGlitchEffect(
               S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG, "/execute in minecraft:overworld run tp @s 14.50 -50.00 -920.50 0.00 0.00"
            );
            this.thenHandleState(S302StoneMinigame.this.createFlag("play_game"));
            this.thenWaitUntil(() -> S302StoneMinigame.isAreaCleared(S302StoneMinigame.GREEN_MAZE_AREA));
            this.thenHandleState(S302StoneMinigame.this.clearFlag("play_game"));
            this.thenDelay(20);
            this.deleteTelepornGlitchEffect(S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(3)
         public void part3(S302StoneMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -111.50 -50.00 -920.50 0.00 0.00");
            this.tdCutscene("/execute in minecraft:overworld run tp @s -111.50 -5.80 -920.50 0.00 90.00");
            this.thenDelay(70);
            this.createTelepornGlitchEffect(
               S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG, "/execute in minecraft:overworld run tp @s -111.50 -50.00 -920.50 0.00 0.00"
            );
            this.thenHandleState(S302StoneMinigame.this.createFlag("play_game"));
            this.thenWaitUntil(() -> S302StoneMinigame.isAreaCleared(S302StoneMinigame.RED_MAZE_AREA));
            this.thenHandleState(S302StoneMinigame.this.clearFlag("play_game"));
            this.thenDelay(20);
            this.deleteTelepornGlitchEffect(S302StoneMinigame.SPIDER_HEAD_LOLOLOSHKA_BIG);
         }

         @ScriptPerformerBuilderReflective.ScriptPart(4)
         public void part4(S302StoneMinigame inst) {
            this.blackout(60);
            this.clearChat();
            this.createTicker("teleporn", S302StoneMinigame::drawPlayerGlitchParticles);
            this.endTdCutscene();
            this.teleportPlayer("/execute in minecraft:overworld run tp @s -58.50 103.00 -469.50 90.00 0.00");
            this.thenDelay(60);
            this.deleteTicker("teleporn");
            this.thenDelay(20);
            this.thenUseEntity(S302StoneMinigame.NEON_STONE, e -> {
               Vec3 pos = e.position();
               UtilST.dropEidos(e.level(), pos, 10, true);
            });
            this.thenRun(() -> S302StoneMinigame.canPlayToday = false);
            this.thenRun(() -> {
               StoneMinigameEndEvent event = new StoneMinigameEndEvent(ScriptUtils.getPlayer(), true);
               MinecraftForge.EVENT_BUS.post(event);
               IStoryServer.get().postStoryEvent(EventType.STONE_MINIGAME_END_EVENT, event);
            });
            this.gotoLabel("s302_before_playing", true);
         }
      })
      .build(this::registerMainPerformer);

   @SubscribeEvent
   public void updatePlayOnNewDay(NewDayStartEvent event) {
      canPlayToday = true;
   }

   @SubscribeEvent
   public void onKeyPress(Key event) {
      if (this.flags.has("play_game")) {
         int keyCode = event.getKey();
         switch (keyCode) {
            case 65:
               this.currentMovement = new Vec3(0.07, 0.0, 0.0);
               this.currentYaw = -90.0F;
               break;
            case 68:
               this.currentMovement = new Vec3(-0.07, 0.0, 0.0);
               this.currentYaw = 90.0F;
               break;
            case 83:
               this.currentMovement = new Vec3(0.0, 0.0, -0.07);
               this.currentYaw = 180.0F;
               break;
            case 87:
               this.currentMovement = new Vec3(0.0, 0.0, 0.07);
               this.currentYaw = 0.0F;
         }
      }
   }

   @SubscribeEvent
   public void onLivingTick(LivingTickEvent event) {
      if (this.flags.has("play_game")) {
         Entity entity = this.getEntity(SPIDER_HEAD_LOLOLOSHKA_BIG);
         if (entity != null) {
            entity.setDeltaMovement(this.currentMovement);
            entity.move(MoverType.SELF, this.currentMovement);
            if (entity.horizontalCollision) {
               this.currentMovement = Vec3.ZERO;
            }

            entity.setYRot(this.currentYaw);
            entity.setYHeadRot(this.currentYaw);
            entity.yRotO = this.currentYaw;
         }
      }
   }

   public S302StoneMinigame(ScriptEnvironment env, int id) {
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

   private static boolean isAreaCleared(AABB area) {
      Level level = ScriptUtils.getLevel();

      for (int x = (int)area.minX; (double)x < area.maxX; x++) {
         for (int y = (int)area.minY; (double)y < area.maxY; y++) {
            for (int z = (int)area.minZ; (double)z < area.maxZ; z++) {
               BlockPos pos = new BlockPos(x, y, z);
               Block block = level.getBlockState(pos).getBlock();
               if (block instanceof DotPackmanBlock) {
                  return false;
               }
            }
         }
      }

      return true;
   }
}
