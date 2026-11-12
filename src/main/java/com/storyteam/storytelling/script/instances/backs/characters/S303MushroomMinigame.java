package com.storyteam.storytelling.script.instances.backs.characters;

import com.mojang.blaze3d.platform.Window;
import com.storyteam.storytelling.api.SaveOnClose;
import com.storyteam.storytelling.api.Script;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.events.base.MushroomMinigameEndEvent;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.script.BackInstance;
import com.storyteam.storytelling.script.performer.Performer;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.builder.reflective.ScriptPerformerBuilderReflective;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.script.story.characters.CharactersST8;
import com.storyteam.storytelling.system.currency.storage.CurrencyStorage;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.Title;
import com.storyteam.storytelling.util.world.Location;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.client.event.ViewportEvent.ComputeCameraAngles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

@SaveOnClose
@Script(
   value = -303,
   description = "Мини-игра (гриб)"
)
public class S303MushroomMinigame extends BackInstance {
   public static final int SCRIPT_ID = -303;
   private static final Random random = new Random((long)"щас бы грибов, а не вот это вот всё".hashCode());
   private static final int GAME_TIME_IN_SECONDS = 120;
   private static final int SPAWN_INTERVAL_IN_SECONDS = 10;
   private static final Character<AnimatedEntity> NEON_MUSHROOM = Character.builder("s303_neon_mushroom", Cast.constant(EntitiesST.NEON_MUSHROOM))
      .name("Гриб")
      .build();
   private static final Character<AnimatedEntity> COLLECTIBLE = Character.builder("s303_collectible", Cast.constant(EntitiesST.EIDOS_COLLECTIBLE))
      .name("Эйдос")
      .build();
   private static final CharacterList<HumanoidEntity> ZOMBIES = CharactersST8.ZOMBIE_FAC.createCharacterList("s303_zombie", 24);
   boolean forward;
   boolean back;
   boolean left;
   boolean right;
   Vec3 currentMovement = Vec3.ZERO;
   float currentYaw = 0.0F;
   int currentZombieIndex = 0;
   int currentPickedCollectibles = 0;
   GameType prevoiusGamemode = GameType.SURVIVAL;
   @NBTSerializable
   public final ScriptPerformer mainFlow = (new ScriptPerformerBuilderReflective(this) {
         @ScriptPerformerBuilderReflective.ScriptPart(0)
         private void part0(S303MushroomMinigame inst) {
            this.positionEntity(S303MushroomMinigame.NEON_MUSHROOM, "/execute in minecraft:overworld run tp @s 17.50 105.00 -374.50 0.00 0.00");
            this.entitySit(S303MushroomMinigame.NEON_MUSHROOM);
            this.label("loop");
            this.thenAwaitEntityClick(S303MushroomMinigame.NEON_MUSHROOM, data -> S303MushroomMinigame.this.canStartGame());
            this.thenDynamicPerformer(b -> {
               if (!S303MushroomMinigame.this.canStartGame()) {
                  inst.mainFlow.gotoLabel("loop", true);
                  b.thenDelay(5);
               }
            });
            this.blackout(60);
            this.thenHandleState(S303MushroomMinigame.this.createFlag("game"));
            this.thenUsePlayer(player -> {
               S303MushroomMinigame.this.currentZombieIndex = 0;
               S303MushroomMinigame.this.currentPickedCollectibles = 0;
               S303MushroomMinigame.this.prevoiusGamemode = player.gameMode.getGameModeForPlayer();
               player.setGameMode(GameType.SPECTATOR);
            });
            this.positionEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA, S303MushroomMinigame.this.getPlayerStartLocation().toCommandString());
            this.thenDelay(70);
            this.thenTitle(S303MushroomMinigame.this.getCountTitle("3"));
            this.thenTitle(S303MushroomMinigame.this.getCountTitle("2"));
            this.thenTitle(S303MushroomMinigame.this.getCountTitle("1"));
            this.thenHandleState(S303MushroomMinigame.this.createFlag("game_show_score"));
            this.thenDynamicPerformer(b -> {
               Location spawnLocation = S303MushroomMinigame.this.getRandomArenaLocation(S303MushroomMinigame.this.getCollectibleSpawnDistanceFromPlayer());
               b.positionEntity(S303MushroomMinigame.COLLECTIBLE, spawnLocation.toCommandString());
               b.entitySit(S303MushroomMinigame.COLLECTIBLE);
            });
            this.createParallelPerformer(
               "zombie_spawner",
               ScriptPerformerBuilder.builder(inst)
                  .label("loop")
                  .thenDynamicPerformer(
                     b -> {
                        Location spawnLocation = S303MushroomMinigame.this.getRandomArenaLocation(S303MushroomMinigame.this.getZombieSpawnDistanceFromPlayer());
                        Character<HumanoidEntity> zombie = S303MushroomMinigame.ZOMBIES.get(S303MushroomMinigame.this.currentZombieIndex);
                        if (zombie == null) {
                           S303MushroomMinigame.this.currentZombieIndex = 0;
                        } else {
                           b.positionEntity(S303MushroomMinigame.ZOMBIES.get(S303MushroomMinigame.this.currentZombieIndex), spawnLocation.toCommandString());
                           b.thenEntityFollowEntity(
                              S303MushroomMinigame.ZOMBIES.get(S303MushroomMinigame.this.currentZombieIndex),
                              CharactersST8.SPIDER_HEAD_LOLOLOSHKA,
                              0.28,
                              0.4,
                              false
                           );
                           S303MushroomMinigame.this.currentZombieIndex++;
                        }
                     }
                  )
                  .thenDelayInSeconds((double)S303MushroomMinigame.this.getSpawnIntervalInSeconds())
                  .gotoLabel("loop")
                  .build()
            );
            this.createParallelPerformer("collectible_pick", ScriptPerformerBuilder.builder(inst).label("loop").thenWaitUntil(() -> {
               AnimatedEntity entity = S303MushroomMinigame.this.getEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
               AnimatedEntity collectible = S303MushroomMinigame.this.getEntity(S303MushroomMinigame.COLLECTIBLE);
               if (entity != null && collectible != null) {
                  double distanceToPick = 0.8;
                  return entity.distanceToSqr(collectible) <= distanceToPick * distanceToPick;
               } else {
                  return false;
               }
            }).thenDynamicPerformer(b -> {
               S303MushroomMinigame.this.currentPickedCollectibles++;
               Location spawnLocation = S303MushroomMinigame.this.getRandomArenaLocation(S303MushroomMinigame.this.getCollectibleSpawnDistanceFromPlayer());
               b.positionEntity(S303MushroomMinigame.COLLECTIBLE, spawnLocation.toCommandString());
               b.entitySit(S303MushroomMinigame.COLLECTIBLE);
            }).gotoLabel("loop").build());
            this.thenAnyPerformer(
               new Performer[]{
                  ScriptPerformerBuilder.builder(inst)
                     .thenWaitUntil(() -> S303MushroomMinigame.this.currentPickedCollectibles + CurrencyStorage.getEidos() >= 20)
                     .build(),
                  ScriptPerformerBuilder.builder(inst).thenDelayInSeconds((double)S303MushroomMinigame.this.getGameTimeInSeconds()).build(),
                  ScriptPerformerBuilder.builder(inst).thenWaitUntil(() -> {
                     AnimatedEntity entity = S303MushroomMinigame.this.getEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
                     if (entity == null) {
                        return false;
                     } else {
                        double distanceToEnd = 0.8;

                        for (Character<HumanoidEntity> e : S303MushroomMinigame.ZOMBIES) {
                           HumanoidEntity zombie = S303MushroomMinigame.this.getEntity(e);
                           if (zombie != null) {
                              double dist = entity.distanceToSqr(zombie);
                              if (dist <= distanceToEnd * distanceToEnd) {
                                 return true;
                              }
                           }
                        }

                        return false;
                     }
                  }).thenRun(() -> S303MushroomMinigame.this.currentPickedCollectibles = 0).build()
               }
            );
            this.thenDelay(5);
            this.blackout(60);
            this.thenDynamicPerformer(b -> {
               S303MushroomMinigame.ZOMBIES.forEach(b::freeEntity);
               b.freeEntity(S303MushroomMinigame.COLLECTIBLE);
               b.freeEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
            });
            this.thenRun(() -> CurrencyStorage.addEidos(S303MushroomMinigame.this.currentPickedCollectibles));
            this.thenUsePlayer(player -> player.setGameMode(S303MushroomMinigame.this.prevoiusGamemode));
            this.thenHandleState(S303MushroomMinigame.this.clearFlag("game"));
            this.thenHandleState(S303MushroomMinigame.this.clearFlag("game_show_score"));
            this.endTdCutscene();
            this.teleportPlayer(S303MushroomMinigame.this.getPlayerEndLocation().toCommandString());
            this.removeParallelPerformer("zombie_spawner");
            this.removeParallelPerformer("collectible_pick");
            this.thenDelay(60);
            this.thenUsePlayer(player -> {
               MushroomMinigameEndEvent event = new MushroomMinigameEndEvent(player, true);
               MinecraftForge.EVENT_BUS.post(event);
               IStoryServer.get().postStoryEvent(EventType.MUSHROOM_MINIGAME_END_EVENT, event);
            });
            this.gotoLabel("loop");
         }
      })
      .build(this::registerMainPerformer);

   private int getGameTimeInSeconds() {
      return 120;
   }

   private int getSpawnIntervalInSeconds() {
      return 10;
   }

   private int getZombieSpawnDistanceFromPlayer() {
      return 8;
   }

   private int getCollectibleSpawnDistanceFromPlayer() {
      return 8;
   }

   private AABB getGameArea() {
      return new AABB(
            new Location("/execute in minecraft:overworld run tp @s -24.32 105.00 -417.68 -315.00 8.25"),
            new Location("/execute in minecraft:overworld run tp @s -39.55 103.00 -404.54 -115.65 8.25")
         )
         .deflate(1.0, 0.0, 1.0);
   }

   private Location getPlayerStartLocation() {
      return new Location("/execute in minecraft:overworld run tp @s -31.99 103.00 -411.01 0.60 1.35");
   }

   private Location getPlayerEndLocation() {
      return new Location("/execute in minecraft:overworld run tp @s 15.69 105.00 -376.27 -405.31 3.75");
   }

   private int getSpawnHeight() {
      return 103;
   }

   private Location getRandomArenaLocation(int spawnDistance) {
      Player player = ScriptUtils.getPlayer();
      Vec3 playerLocation = player.position();
      Vec3 arenaPosition = getRandomPointInAABB(this.getGameArea(), playerLocation, (double)spawnDistance, 0.5);
      Vec3 heightedArenaPosition = new Vec3(arenaPosition.x(), (double)this.getSpawnHeight(), arenaPosition.z());
      return new Location(heightedArenaPosition);
   }

   private static Vec3 getRandomPointInAABB(AABB box, Vec3 center, double targetDistance, double tolerance) {
      for (int i = 0; i < 10; i++) {
         double x = Mth.lerp(random.nextDouble(), box.minX, box.maxX);
         double z = Mth.lerp(random.nextDouble(), box.minZ, box.maxZ);
         Vec3 point = new Vec3(x, center.y, z);
         double distXZ = Math.sqrt(Math.pow(point.x - center.x, 2.0) + Math.pow(point.z - center.z, 2.0));
         if (Math.abs(distXZ - targetDistance) <= tolerance) {
            return point;
         }
      }

      double x = Mth.lerp(random.nextDouble(), box.minX, box.maxX);
      double z = Mth.lerp(random.nextDouble(), box.minZ, box.maxZ);
      return new Vec3(x, center.y, z);
   }

   private boolean canStartGame() {
      return CurrencyStorage.getEidos() < 20;
   }

   private Title getCountTitle(String count) {
      return Title.builder(Component.literal(count).setStyle(Style.EMPTY.withColor(15649534))).times(5, 20, 5).build();
   }

   public S303MushroomMinigame(ScriptEnvironment env, int id) {
      super(env, id);
   }

   @SubscribeEvent
   public void onKeyPress(Key event) {
      if (this.flags.has("game")) {
         int key = event.getKey();
         int action = event.getAction();
         boolean pressed = action != 0;
         switch (key) {
            case 65:
               this.left = pressed;
               break;
            case 68:
               this.right = pressed;
               break;
            case 83:
               this.back = pressed;
               break;
            case 87:
               this.forward = pressed;
         }
      }
   }

   @SubscribeEvent
   public void onLivingTick(LivingTickEvent event) {
      if (this.flags.has("game")) {
         Entity entity = this.getEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
         if (entity != null) {
            double x = 0.0;
            double z = 0.0;
            if (this.forward) {
               z++;
            }

            if (this.back) {
               z--;
            }

            if (this.left) {
               x++;
            }

            if (this.right) {
               x--;
            }

            Vec3 dir = new Vec3(x, 0.0, z);
            if (dir.lengthSqr() > 0.0) {
               dir = dir.normalize().scale(0.02);
               this.currentMovement = dir;
               this.currentYaw = (float)Math.toDegrees(Math.atan2(-dir.x, dir.z));
            } else {
               this.currentMovement = Vec3.ZERO;
            }

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

   @SubscribeEvent
   public void onCamera(ComputeCameraAngles event) {
      if (this.flags.has("game")) {
         Level level = Minecraft.getInstance().level;
         LocalPlayer player = Minecraft.getInstance().player;
         if (level == null || player == null) {
            return;
         }

         event.getCamera().setup(level, player, false, false, (float)event.getPartialTick());
         Entity entity = this.getEntity(CharactersST8.SPIDER_HEAD_LOLOLOSHKA);
         if (entity == null) {
            return;
         }

         Vec3 entityPos = entity.position();
         double cameraHeight = 7.0;
         Vec3 cameraPos = new Vec3(entityPos.x, entityPos.y + cameraHeight, entityPos.z);
         Location cameraLocation = new Location(entity.level().dimension(), cameraPos, 0.0F, 90.0F);
         float cameraPitch = cameraLocation.hasRotation() ? cameraLocation.getPitch() : 0.0F;
         float cameraYaw = cameraLocation.hasRotation() ? cameraLocation.getYaw() : 0.0F;
         event.getCamera().position = cameraLocation;
         event.setPitch(cameraPitch);
         event.setYaw(cameraYaw);
      }
   }

   @SubscribeEvent
   public void renderCollectibleCount(RenderGuiEvent event) {
      if (this.flags.has("game_show_score")) {
         GuiGraphics graphics = event.getGuiGraphics();
         Minecraft mc = Minecraft.getInstance();
         Window window = mc.getWindow();
         Font font = mc.font;
         int screenWidth = window.getGuiScaledWidth();
         int screenHeight = window.getGuiScaledHeight();
         int maxScore = 20;
         int currentScore = Math.min(this.currentPickedCollectibles + CurrencyStorage.getEidos(), maxScore);
         String textToRender = "%s / %s".formatted(currentScore, maxScore);
         graphics.drawCenteredString(font, textToRender, screenWidth / 2, screenHeight / 10, 15649534);
      }
   }
}
