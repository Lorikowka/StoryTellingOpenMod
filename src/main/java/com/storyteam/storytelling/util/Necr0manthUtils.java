package com.storyteam.storytelling.util;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.client.animations.AnimationsST3;
import com.storyteam.storytelling.client.animations.AnimationsST4;
import com.storyteam.storytelling.client.animations.AnimationsST5;
import com.storyteam.storytelling.client.animations.AnimationsST7;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.CharacterList;
import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import com.storyteam.storytelling.util.world.Location;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.Cast;

public class Necr0manthUtils {
   public static final Random random = new Random();
   public static final Vec3[] SIDES_OF_BLOCKS = new Vec3[]{
      new Vec3(0.55, 0.0, 0.0),
      new Vec3(-0.55, 0.0, 0.0),
      new Vec3(0.0, 0.55, 0.0),
      new Vec3(0.0, -0.55, 0.0),
      new Vec3(0.0, 0.0, 0.55),
      new Vec3(0.0, 0.0, -0.55)
   };
   public static boolean fuckFixedTime = false;
   public static Map<Entity, Float> noMoveTime = new HashMap<>();
   public static Map<Entity, Location> locations = new HashMap<>();
   public static Map<Entity, Map<String, ConfiguredAnimation>> anims = new HashMap<>();

   public static boolean inventoryHasItems(List<ItemStack> inventory, List<ItemStack> requiredItems) {
      List<Pair<ItemStack, Integer>> requiredCounts = new ArrayList<>();

      for (ItemStack stack : requiredItems) {
         boolean f = true;

         for (Pair<ItemStack, Integer> entry : requiredCounts) {
            if (ItemStack.isSameItemSameTags((ItemStack)entry.first(), stack)) {
               entry.right((Integer)entry.right() + stack.getCount());
               f = false;
               break;
            }
         }

         if (f) {
            requiredCounts.add(new ObjectObjectMutablePair(stack, stack.getCount()));
         }
      }

      List<Pair<ItemStack, Integer>> inventoryCounts = new ArrayList<>();

      for (ItemStack stack : inventory) {
         boolean f = true;

         for (Pair<ItemStack, Integer> entryx : inventoryCounts) {
            if (ItemStack.isSameItemSameTags((ItemStack)entryx.first(), stack)) {
               entryx.right((Integer)entryx.right() + stack.getCount());
               f = false;
               break;
            }
         }

         if (f) {
            inventoryCounts.add(new ObjectObjectMutablePair(stack, stack.getCount()));
         }
      }

      for (Pair<ItemStack, Integer> entryxx : requiredCounts) {
         Integer count = inventoryCounts.stream()
            .filter(e -> ItemStack.isSameItemSameTags((ItemStack)e.first(), (ItemStack)entryxx.first()))
            .findAny()
            .<Integer>map(Pair::right)
            .orElse(0);
         if (count < (Integer)entryxx.right()) {
            return false;
         }
      }

      return true;
   }

   public static void removeItemsFromInventory(List<ItemStack> inventory, List<ItemStack> toRemove) {
      for (ItemStack stack : toRemove) {
         ItemStack stack1 = stack.copy();

         for (ItemStack i : inventory) {
            if (ItemStack.isSameItemSameTags(i, stack1)) {
               int c = Math.min(i.getCount(), stack1.getCount());
               stack1.shrink(c);
               i.shrink(c);
               if (stack1.isEmpty()) {
                  break;
               }
            }
         }
      }
   }

   public static VoxelShape rotateShapeY(int times, VoxelShape shape) {
      VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

      for (int i = 0; i < times; i++) {
         buffer[0]
            .forAllBoxes(
               (minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX))
            );
         buffer[0] = buffer[1];
         buffer[1] = Shapes.empty();
      }

      return buffer[0];
   }

   public static VoxelShape rotateShapeZ(int times, VoxelShape shape) {
      VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

      for (int i = 0; i < 4 - times; i++) {
         buffer[0]
            .forAllBoxes(
               (minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1.0 - maxY, minX, minZ, 1.0 - minY, maxX, maxZ))
            );
         buffer[0] = buffer[1];
         buffer[1] = Shapes.empty();
      }

      return buffer[0];
   }

   public static VoxelShape rotateShapeX(int times, VoxelShape shape) {
      VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

      for (int i = 0; i < 4 - times; i++) {
         buffer[0]
            .forAllBoxes(
               (minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(minX, 1.0 - maxZ, minY, maxX, 1.0 - minZ, maxY))
            );
         buffer[0] = buffer[1];
         buffer[1] = Shapes.empty();
      }

      return buffer[0];
   }

   public static VoxelShape rotateShapeAroundAxis(Axis axis, int times, VoxelShape shape) {
      if (axis == Axis.X) {
         return rotateShapeX(times, shape);
      } else {
         return axis == Axis.Y ? rotateShapeY(times, shape) : rotateShapeZ(times, shape);
      }
   }

   public static VoxelShape rotateShapeAroundAxis(Direction dir, int times, VoxelShape shape) {
      return rotateShapeAroundAxis(dir.getAxis(), dir.getAxisDirection() == AxisDirection.POSITIVE ? times : 4 - times, shape);
   }

   public static void saveConfig(String localPath, String data) {
      Path file = FMLPaths.CONFIGDIR.get().resolve(localPath);

      try {
         if (Files.notExists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
         }

         Files.writeString(file, data);
      } catch (Exception var4) {
         Storytelling.LOG.warn("Не удалось сохранить файл");
      }
   }

   public static String readConfig(String localPath) {
      Path file = FMLPaths.CONFIGDIR.get().resolve(localPath);

      try {
         if (Files.exists(file)) {
            return Files.readString(file);
         }
      } catch (Exception var3) {
         Storytelling.LOG.warn("Не удалось прочитать файл");
      }

      return "";
   }

   public static void saveStConfig(String localPath, String data) {
      saveConfig(Path.of("storytelling").resolve(localPath).toString(), data);
   }

   public static String readStConfig(String path) {
      return readConfig(Path.of("storytelling").resolve(path).toString());
   }

   public static Vec3 randomNormalizedVector() {
      return new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize();
   }

   public static Vec3 randomVectorFromBall() {
      return new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().scale(Math.pow(random.nextDouble(), 0.33333334F));
   }

   public static Vec3 randomNormalized2DVector() {
      return new Vec3(random.nextGaussian(), 0.0, random.nextGaussian()).normalize();
   }

   public static Vec3 random2DVectorFromCircle() {
      return new Vec3(random.nextGaussian(), 0.0, random.nextGaussian()).normalize().scale(Math.pow(random.nextDouble(), 0.5));
   }

   public static Consumer<ScriptPerformerBuilder> awesomeCameraPath(String startPos, String endPos, String center, int dir_, int time, int iterations) {
      return builder -> {
         float eyeHeight = 1.62F;
         List<String> cameraPositions = new ArrayList<>();
         Location p0 = PositionParser.parseLocationWithRotation(startPos).add(0.0, (double)eyeHeight, 0.0);
         Vec3 p1 = PositionParser.parseLocationWithRotation(endPos).add(0.0, (double)eyeHeight, 0.0);
         Vec3 centerPos = PositionParser.parseLocationWithRotation(center);
         float a0 = p0.subtract(centerPos).multiply(1.0, 0.0, 1.0).normalize().toVector3f().angleSigned(0.0F, 0.0F, 1.0F, 0.0F, -1.0F, 0.0F);
         float a1 = p1.subtract(centerPos).multiply(1.0, 0.0, 1.0).normalize().toVector3f().angleSigned(0.0F, 0.0F, 1.0F, 0.0F, -1.0F, 0.0F);
         if (a1 < a0) {
            a1 = (float)((double)a1 + (Math.PI * 2));
         }

         int dir = dir_;
         if (dir_ > 0) {
            dir = dir_ - 1;
         }

         a1 = (float)((double)a1 + (Math.PI * 2) * (double)dir);
         double l0 = p0.distanceTo(centerPos);
         double l1 = p1.distanceTo(centerPos);
         double y0 = p0.y;
         double y1 = p1.y;

         for (int i = 0; i < iterations; i++) {
            float t = (float)i / ((float)iterations - 1.0F);
            float a = MathUtils.lerp(a0, a1, t);
            float l = MathUtils.lerp((float)l0, (float)l1, t);
            float y = MathUtils.lerp((float)y0, (float)y1, t);
            Vec3 p = centerPos.add(new Vec3(0.0, 0.0, 1.0).yRot(a).scale(Math.sqrt((double)(l * l) - ((double)y - centerPos.y) * ((double)y - centerPos.y))))
               .add(0.0, (double)y - centerPos.y, 0.0);
            Vec3 look = centerPos.subtract(p).normalize();
            float yRot = -look.toVector3f().mul(1.0F, 0.0F, 1.0F).angleSigned(0.0F, 0.0F, 1.0F, 0.0F, -1.0F, 0.0F) * (180.0F / (float)Math.PI);
            double xRot = -Math.atan2(look.y, look.multiply(1.0, 0.0, 1.0).length()) * 180.0F / (float)Math.PI;
            cameraPositions.add(
               "/execute in " + p0.getDimension().location() + " run tp @s " + p.x + " " + (p.y - (double)eyeHeight) + " " + p.z + " " + yRot + " " + xRot
            );
         }

         builder.thenIfDynamic(
               () -> ServersideCutsceneHandler.currentData == null || p0.getDimension() != ScriptUtils.getPlayer().level().dimension(),
               new ScriptPerformerBuilder(builder.getPerformer().script).tdCutscene(cameraPositions.get(0)).build(),
               new ScriptPerformer(builder.getPerformer().script)
            )
            .tdCutsceneMoveCamera(time, cameraPositions.toArray(new String[0]));
      };
   }

   public static Consumer<ScriptPerformerBuilder> deleteEntity(Character character) {
      return b -> b.freeEntity(character, true).thenRun(() -> {
            Entity e = character.repositoryEntity();
            if (e != null) {
               e.discard();
            }
         });
   }

   public static <T extends Entity> Consumer<ScriptPerformerBuilder> teleportImmediate(Character<T> character, String pos) {
      return teleportImmediate(character, () -> PositionParser.parseLocationWithRotation(pos));
   }

   public static <T extends Entity> Consumer<ScriptPerformerBuilder> teleportImmediate(Character<T> character, Supplier<Location> pos) {
      return builder -> builder.thenPerform(() -> {
            if (character.repositoryEntity() != null) {
               Location newPos = pos.get();
               builder.getPerformer().script.getRepository().get(character).setPosition(newPos, true);
               builder.getPerformer().script.getRepository().get(character).doReposition(character.repositoryEntity());
               if (character.repositoryEntity() instanceof LivingEntity le) {
                  le.lerpTo(le.getX(), le.getY(), le.getZ(), le.getYRot(), le.getXRot(), 0, false);
                  if (le instanceof HumanoidEntity he) {
                     he.yBody = he.yBodyRot = he.yBodyRotO = newPos.getYaw();
                     he.yHeadRot = he.yHeadRotO = newPos.getYaw();
                  }
               }

               T e = getClientEntity(character);
               if (e != null) {
                  e.setPos(character.repositoryEntity().position());
                  e.xo = e.getX();
                  e.yo = e.getY();
                  e.zo = e.getZ();
                  e.xOld = e.getX();
                  e.yOld = e.getY();
                  e.zOld = e.getZ();
                  e.lerpTo(e.getX(), e.getY(), e.getZ(), e.getYRot(), e.getXRot(), 0, false);
                  e.setYRot(newPos.getYaw());
                  e.yRotO = newPos.getYaw();
                  e.setYBodyRot(newPos.getYaw());
                  e.yOld = (double)newPos.getYaw();
                  e.setXRot(newPos.getPitch());
                  e.xRotO = newPos.getPitch();
                  if (e instanceof HumanoidEntity he) {
                     he.yBody = he.yBodyRot = he.yBodyRotO = newPos.getYaw();
                     he.yHeadRot = he.yHeadRotO = newPos.getYaw();
                  }
               }
            }
         });
   }

   public static <T extends Entity> T getClientEntity(Character<T> character) {
      if (Minecraft.getInstance().level == null) {
         return null;
      } else {
         for (Entity e : Minecraft.getInstance().level.entitiesForRendering()) {
            if (e != null && e.getUUID().equals(character.repositoryEntity().getUUID())) {
               return (T)e;
            }
         }

         return null;
      }
   }

   public static <T extends Entity & IAnimatedEntity> Consumer<ScriptPerformerBuilder> syncAnimationsAndSetPosition(
      Character<T> character, String loc, Map<String, ConfiguredAnimation> anims
   ) {
      return syncAnimationsAndSetPosition(character, Cast.constant(PositionParser.parseLocationWithRotation(loc)), anims);
   }

   public static <T extends Entity & IAnimatedEntity> Consumer<ScriptPerformerBuilder> syncAnimationsAndSetPosition(
      Character<T> character, Supplier<Location> loc, Map<String, ConfiguredAnimation> anims
   ) {
      return builder -> builder.thenRun(() -> {
            Necr0manthUtils.anims.put(character.repositoryEntity(), anims);
            Network.sendToAll(new Necr0manthUtils.ChangeAnimationAndRepositionPacket(character.repositoryEntity(), loc.get(), anims));
         }).thenRun(() -> {
            Runnable h = () -> {
               EntityState<T> entityState = builder.getPerformer().script.getRepository().getOrCreate(character, true);
               entityState.setPosition(loc.get(), true);
               entityState.doReposition(character.repositoryEntity());

               for (Entry<String, ConfiguredAnimation> anim : anims.entrySet()) {
                  entityState.setAnimation(anim.getKey(), anim.getValue());
               }

               if (character.repositoryEntity() instanceof HumanoidEntity he) {
                  he.lerpTo(he.getX(), he.getY(), he.getZ(), he.getYRot(), he.getXRot(), 0, false);
                  he.yBody = he.yBodyRot = he.yBodyRotO = loc.get().getYaw();
                  he.yHeadRot = he.yHeadRotO = loc.get().getYaw();
               }
            };
            ServerScheduler.schedule(2, h);
         });
   }

   public static StoryTicker spawnCameraParticles(Level level, BlockPos pos) {
      return player -> {
         float time = (float)Math.sin((double)((float)player.tickCount / 8.0F));

         for (Vec3 side : SIDES_OF_BLOCKS) {
            ParticleHelper.spawnEnginedParticle(level, ParticleTypes.END_ROD, pos.getCenter().add(side), Vec3.ZERO, time, 1, Color.WHITE, time);
         }
      };
   }

   public static StoryTicker particlePath(Location[] path) {
      return Necr0manthUtils.TickRate.atTickRate(player -> {
         Location location = path[random.nextInt(path.length)];
         ParticleHelper.spawnParticles(ScriptUtils.getLevel(), ParticleTypes.END_ROD, location.x, location.y, location.z, 2, 0.0, 0.0, 0.0, 0.0);
      }, 0.5F * (float)path.length / 33.0F);
   }

   public static <T> Runnable consumerToRunnable(Consumer<T> func, T arg) {
      return () -> func.accept(arg);
   }

   public static <T1, T2> Runnable withArgs(BiConsumer<T1, T2> func, T1 arg1, T2 arg2) {
      return () -> func.accept(arg1, arg2);
   }

   public static <T, R> Consumer<T> toConsumer(Function<T, R> func) {
      return func::apply;
   }

   public static <T1, T2, R> Supplier<R> withArgs(BiFunction<T1, T2, R> func, T1 arg1, T2 arg2) {
      return () -> func.apply(arg1, arg2);
   }

   public static <T1, T2, R> Function<T1, R> with2arg(BiFunction<T1, T2, R> func, T2 arg2) {
      return arg1 -> func.apply(arg1, arg2);
   }

   public static <T1, T2> Consumer<T1> with2arg(BiConsumer<T1, T2> func, T2 arg2) {
      return arg1 -> func.accept(arg1, arg2);
   }

   public static <T2> Function<ScriptPerformerBuilder, ScriptPerformerBuilder> withArg(
      BiFunction<ScriptPerformerBuilder, T2, ScriptPerformerBuilder> func, T2 arg2
   ) {
      return arg1 -> func.apply(arg1, arg2);
   }

   public static <T, R> Supplier<R> withArg(Function<T, R> func, T arg) {
      return () -> func.apply(arg);
   }

   public static <T1, T2, R> Function<T2, R> with1arg(BiFunction<T1, T2, R> func, T1 arg1) {
      return arg2 -> func.apply(arg1, arg2);
   }

   public static Consumer<ScriptPerformerBuilder> thenInRun(ScriptPerformer performer) {
      boolean[] fastForward = new boolean[1];
      return b -> b.thenPerform(() -> fastForward[0] = true).thenRun(() -> fastForward[0] = false).thenIfDynamic(() -> !fastForward[0], performer);
   }

   public static Consumer<ScriptPerformerBuilder> thenInFastForward(ScriptPerformer performer) {
      boolean[] fastForward = new boolean[1];
      return b -> b.thenPerform(() -> fastForward[0] = true).thenRun(() -> fastForward[0] = false).thenIfDynamic(() -> fastForward[0], performer);
   }

   public static ScriptPerformer talk2(ScriptInstance inst, Character<? extends AnimatedEntity> ch1, Character<? extends AnimatedEntity> ch2) {
      return new ScriptPerformerBuilder(inst)
         .label("loop")
         .entityLookAt(ch1, ch2)
         .entityLookAt(ch2, ch1)
         .action(ch1, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(20)
         .emotion(ch2, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(20)
         .nod(ch2, 1)
         .thenDelay(20)
         .emotion(ch2, AnimationsST5.EMOTION_HAPPY_NEW)
         .thenDelay(20)
         .stopEmotion(ch2)
         .action(ch2, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(20)
         .stopAction(ch1)
         .thenDelay(20)
         .emotion(ch1, AnimationsST5.EMOTION_HAPPY_NEW)
         .thenDelay(20)
         .emotion(ch1, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(20)
         .nod(ch1, 1)
         .thenDelay(30)
         .action(ch1, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(10)
         .stopAction(ch2)
         .thenDelay(30)
         .nope(ch2, 1)
         .thenDelay(20)
         .stopAction(ch1)
         .action(ch2, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(30)
         .action(ch2, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(20)
         .emotion(ch1, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(30)
         .stopAction(ch2)
         .gotoLabel("loop")
         .build();
   }

   public static ScriptPerformer talk3(
      ScriptInstance inst, Character<? extends AnimatedEntity> ch1, Character<? extends AnimatedEntity> ch2, Character<? extends AnimatedEntity> ch3
   ) {
      return new ScriptPerformerBuilder(inst)
         .label("loop")
         .entityLookAt(ch1, ch2)
         .entityLookAt(ch3, ch2)
         .action(ch2, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(30)
         .nod(ch3, 1)
         .thenDelay(30)
         .emotion(ch1, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .emotion(ch3, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .stopAction(ch2)
         .entityLookAt(ch1, ch3)
         .action(ch1, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(10)
         .entityLookAt(ch2, ch1)
         .entityLookAt(ch3, ch1)
         .thenDelay(40)
         .emotion(ch3, AnimationsST5.EMOTION_HAPPY_NEW)
         .thenDelay(20)
         .stopEmotion(ch3)
         .action(ch3, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(10)
         .entityLookAt(ch2, ch3)
         .entityLookAt(ch1, ch3)
         .thenDelay(20)
         .nope(ch2, 1)
         .thenDelay(10)
         .action(ch2, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(10)
         .entityLookAt(ch3, ch2)
         .entityLookAt(ch1, ch2)
         .thenDelay(30)
         .stopAction(ch3)
         .thenDelay(20)
         .stopAction(ch2)
         .thenDelay(20)
         .emotion(ch3, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .emotion(ch2, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(40)
         .action(ch1, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(10)
         .entityLookAt(ch1, ch2)
         .entityLookAt(ch2, ch1)
         .entityLookAt(ch3, ch1)
         .thenDelay(20)
         .entityLookAt(ch1, ch3)
         .thenDelay(20)
         .stopAction(ch1)
         .gotoLabel("loop")
         .build();
   }

   public static ScriptPerformer talk4(
      ScriptInstance inst,
      Character<? extends AnimatedEntity> ch1,
      Character<? extends AnimatedEntity> ch2,
      Character<? extends AnimatedEntity> ch3,
      Character<? extends AnimatedEntity> ch4
   ) {
      return new ScriptPerformerBuilder(inst)
         .label("loop")
         .entityLookAt(ch1, ch2)
         .entityLookAt(ch3, ch2)
         .entityLookAt(ch4, ch2)
         .action(ch2, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(30)
         .nod(ch3, 1)
         .thenDelay(30)
         .emotion(ch1, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .emotion(ch3, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .emotion(ch4, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .stopAction(ch2)
         .entityLookAt(ch1, ch3)
         .action(ch1, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(10)
         .entityLookAt(ch2, ch1)
         .entityLookAt(ch3, ch1)
         .entityLookAt(ch4, ch1)
         .thenDelay(40)
         .emotion(ch3, AnimationsST5.EMOTION_HAPPY_NEW)
         .emotion(ch4, AnimationsST5.EMOTION_HAPPY_NEW)
         .thenDelay(20)
         .stopEmotion(ch3)
         .stopEmotion(ch4)
         .action(ch3, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(10)
         .entityLookAt(ch2, ch3)
         .entityLookAt(ch1, ch3)
         .entityLookAt(ch4, ch3)
         .thenDelay(20)
         .nope(ch2, 1)
         .emotion(ch4, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(10)
         .action(ch2, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(10)
         .entityLookAt(ch3, ch2)
         .entityLookAt(ch4, ch2)
         .entityLookAt(ch1, ch2)
         .thenDelay(30)
         .stopAction(ch3)
         .thenDelay(10)
         .nod(ch4, 1)
         .thenDelay(20)
         .stopAction(ch2)
         .action(ch4, AnimationsST4.HUMANOID_TALK_2)
         .entityLookAt(ch2, ch4)
         .entityLookAt(ch3, ch4)
         .thenDelay(20)
         .emotion(ch3, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .emotion(ch2, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .thenDelay(10)
         .entityLookAt(ch3, ch4)
         .emotion(ch4, AnimationsST4.EMOTION_GIGGLE_NEW_NOMOUTH)
         .entityLookAt(ch1, ch4)
         .thenDelay(20)
         .action(ch4, AnimationsST4.HUMANOID_TALK_3_ARGUING)
         .thenDelay(40)
         .action(ch1, AnimationsST3.HUMANOID_TALKING)
         .thenDelay(10)
         .stopAction(ch4)
         .thenDelay(20)
         .entityLookAt(ch1, ch2)
         .thenDelay(20)
         .stopAction(ch1)
         .gotoLabel("loop")
         .build();
   }

   public static Consumer<ScriptPerformerBuilder> simpleSit(Character<? extends AnimatedEntity> ch, String pos) {
      return b -> b.positionEntity(ch, pos).animation(ch, 2, AnimationsST3.HUMANOID_SIT).entitySit(ch).setRestrictedLookController(ch, 10.0, 40.0);
   }

   public static StoryTicker cry(Character<? extends HumanoidEntity> ch, String bone, Vector3d offset) {
      return player -> {
         HumanoidEntity e = (HumanoidEntity)ch.repositoryEntity();
         if (e != null) {
            Vec3 pos = ScriptUses.bonePosition(ch, bone, offset, false).get();
            ParticleHelper.spawnEnginedParticle(
               player.serverLevel(), ParticleTypes.UNDERWATER, pos, new Vec3(0.0, -0.01, 0.0), 1.0F, 10, new Color(0.4F, 0.4F, 0.7F), 0.1F
            );
         }
      };
   }

   public static StoryTicker ПлакатьсяСлева(Character<? extends HumanoidEntity> ch) {
      return cry(ch, "ocular_left", new Vector3d());
   }

   public static StoryTicker ПлакатьсяСправа(Character<? extends HumanoidEntity> ch) {
      return cry(ch, "ocular_right", new Vector3d());
   }

   public static Consumer<ScriptPerformerBuilder> closeWings(CharacterList<? extends AnimatedEntity> list) {
      return b -> {
         for (Character<? extends AnimatedEntity> ch : list) {
            b.animation(ch, 16, AnimationsST7.SKYZERN_CLOSED_WINGS);
         }
      };
   }

   public static double normalizeAngleMinusPiPi(double angle) {
      angle %= Math.PI * 2;
      if (Math.abs(angle) > Math.PI) {
         angle -= (Math.PI * 2) * Math.signum(angle);
      }

      return angle;
   }

   public static float normalizeAngleMinusPiPi(float angle) {
      return (float)normalizeAngleMinusPiPi((double)angle);
   }

   public static Consumer<ScriptPerformerBuilder> parallel(String id, ScriptPerformer performer) {
      return builder -> {
         builder.createParallelPerformer(id, performer);
         builder._fastForwardOverwrite(true, performer);
      };
   }

   public static Quaterniond findQuaternion(Vector3d a, Vector3d b, Vector3d c, Vector3d d) {
      Vector3d aNorm = new Vector3d(a).normalize();
      Vector3d bNorm = new Vector3d(b).normalize();
      Quaterniond q1 = new Quaterniond().rotationTo(aNorm, bNorm);
      Vector3d c1 = new Vector3d();
      q1.transform(c, c1);
      Vector3d projB = new Vector3d(bNorm).mul(c1.dot(bNorm));
      Vector3d p1 = new Vector3d(c1).sub(projB);
      projB = new Vector3d(bNorm).mul(d.dot(bNorm));
      Vector3d p2 = new Vector3d(d).sub(projB);
      Quaterniond q2 = new Quaterniond();
      if (p1.length() > 1.0E-12 && p2.length() > 1.0E-12) {
         p1.normalize();
         p2.normalize();
         double cosTheta = p1.dot(p2);
         Vector3d cross = new Vector3d();
         double sinTheta = p1.cross(p2, cross).dot(bNorm);
         double angle = Math.atan2(sinTheta, cosTheta);
         q2.setAngleAxis(angle, bNorm);
      }

      return q2.mul(q1);
   }

   public static Supplier<Location> lookAtLying(
      Vector3d a, Vector3d b, Vector3d c, Vector3d d, Supplier<Vec3> headPositionSupplier, Supplier<Vec3> target, Character<? extends HumanoidEntity> character
   ) {
      return () -> {
         Vec3 pos = target.get();
         Vec3 headPos = headPositionSupplier.get();
         if (pos != null && headPos != null) {
            Vec3 dir = pos.subtract(headPos);
            return character.repositoryEntity() == null
               ? null
               : new Location(
                  ((HumanoidEntity)character.repositoryEntity()).getEyePosition().add(new Vec3(findQuaternion(a, b, c, d).transformInverse(dir.toVector3f())))
               );
         } else {
            return null;
         }
      };
   }

   public static Consumer<ScriptPerformerBuilder> sit(Character<? extends AnimatedEntity> character, String pos) {
      return builder -> builder.overridePredefinedAnimation(character, (Supplier<ConfiguredAnimation>)(() -> {
               if (character.repositoryEntity() == null) {
                  return null;
               } else {
                  AnimConfigurationWrapper animConfigurationWrapper = ((AnimatedEntity)character.repositoryEntity()).getWrapper();
                  IDefaultAnimConfiguration iDefaultAnimConfiguration = animConfigurationWrapper.getConfiguration();
                  if (iDefaultAnimConfiguration != null) {
                     List<ConfiguredAnimation> idleList = iDefaultAnimConfiguration.getAnimations(MovementActionType.IDLE);
                     if (!idleList.isEmpty()) {
                        Random rnd = new Random();
                        return idleList.get(rnd.nextInt(idleList.size()));
                     }
                  }

                  return null;
               }
            }))
            .blockAnimWalkingBodyRotation(character, true)
            .animation(character, 7, AnimationsST7.STAY_TO_SIT.configure().transitionTime(0.4F).important())
            .thenIfDynamic(
               () -> character.repositoryEntity() == null,
               new ScriptPerformerBuilder(builder.getPerformer().script).positionEntity(character, pos).build(),
               new ScriptPerformerBuilder(builder.getPerformer().script).thenEntityMoveNoPhysics(character, 0.1, pos, false).build()
            )
            .entitySit(character)
            .removeOverridePredefined(character)
            .blockAnimWalkingBodyRotation(character, false);
   }

   public static class ChangeAnimationAndRepositionPacket implements INBTPacket {
      protected IObjectSource<?> source;
      protected Location location;
      protected Map<String, ConfiguredAnimation> anims;

      public ChangeAnimationAndRepositionPacket() {
      }

      public ChangeAnimationAndRepositionPacket(Entity entity, Location location, Map<String, ConfiguredAnimation> anims) {
         this.source = (IObjectSource<?>)IObjectSource.ofEntity(entity).get();
         this.location = location;
         this.anims = anims;
      }

      public void write(CompoundTag nbt) {
         CompoundTag animsTag = new CompoundTag();

         for (Entry<String, ConfiguredAnimation> i : this.anims.entrySet()) {
            animsTag.put(i.getKey(), i.getValue().serializeNBT());
         }

         nbt.put("anims", animsTag);
         nbt.put("Src", IObjectSource.writeSource(this.source));
         nbt.put("loc", new CompoundTag());
         nbt.getCompound("loc").putDouble("x", this.location.x());
         nbt.getCompound("loc").putDouble("y", this.location.y());
         nbt.getCompound("loc").putDouble("z", this.location.z());
         nbt.getCompound("loc").putFloat("yaw", this.location.getYaw());
         nbt.getCompound("loc").putFloat("pitch", this.location.getPitch());
      }

      public void read(CompoundTag nbt) {
         this.anims = new HashMap<>();

         for (String i : nbt.getCompound("anims").getAllKeys()) {
            this.anims.put(i, new ConfiguredAnimation(nbt.getCompound("anims").getCompound(i)));
         }

         this.source = (IObjectSource<?>)IObjectSource.readSource(nbt.getCompound("Src")).orElse(null);
         this.location = new Location(
            Level.OVERWORLD,
            nbt.getCompound("loc").getDouble("x"),
            nbt.getCompound("loc").getDouble("y"),
            nbt.getCompound("loc").getDouble("z"),
            nbt.getCompound("loc").getFloat("yaw"),
            nbt.getCompound("loc").getFloat("pitch")
         );
         this.location.deserializeNBT(nbt.getCompound("loc"));
      }

      @OnlyIn(Dist.CLIENT)
      public void clientExecute(PacketContext ctx) {
         RenderCall renderCall = () -> {
            Entity e = (Entity)this.source.get(Minecraft.getInstance().level);
            if (e != null) {
               for (Entry<String, ConfiguredAnimation> anim : this.anims.entrySet()) {
                  ((IAnimatedEntity)e).getAnimationSystem().startAnimationAt(anim.getKey(), anim.getValue());
               }

               e.setPos(this.location);
               e.xo = e.getX();
               e.yo = e.getY();
               e.zo = e.getZ();
               e.xOld = e.getX();
               e.yOld = e.getY();
               e.zOld = e.getZ();
               e.lerpTo(e.getX(), e.getY(), e.getZ(), e.getYRot(), e.getXRot(), 0, false);
               e.setYRot(this.location.getYaw());
               e.yRotO = this.location.getYaw();
               e.setYBodyRot(this.location.getYaw());
               e.setXRot(this.location.getPitch());
               e.xRotO = this.location.getPitch();
               if (e instanceof HumanoidEntity he) {
                  he.yBody = he.yBodyRot = he.yBodyRotO = this.location.getYaw();
                  he.yHeadRot = he.yHeadRotO = this.location.getYaw();
               }

               Necr0manthUtils.noMoveTime.put(e, 0.2F);
               Necr0manthUtils.locations.put(e, this.location);
            }
         };
         if (RenderSystem.isOnRenderThread()) {
            renderCall.execute();
         } else {
            RenderSystem.recordRenderCall(renderCall);
         }
      }
   }

   @EventBusSubscriber({Dist.CLIENT})
   public static class ClientScheduler {
      private static final Object lock = new Object();
      private static final List<Necr0manthUtils.ClientScheduler.Task> pendingQueue = new ArrayList<>();
      private static final List<Necr0manthUtils.ClientScheduler.Task> activeQueue = new ArrayList<>();

      public static void schedule(int ticks, Runnable task) {
         synchronized (lock) {
            pendingQueue.add(new Necr0manthUtils.ClientScheduler.Task(ticks, task));
         }
      }

      @SubscribeEvent
      public static void clientTick(ClientTickEvent event) {
         if (event.phase != Phase.START) {
            synchronized (lock) {
               if (!pendingQueue.isEmpty()) {
                  activeQueue.addAll(pendingQueue);
                  pendingQueue.clear();
               }
            }

            Iterator<Necr0manthUtils.ClientScheduler.Task> iterator = activeQueue.iterator();

            while (iterator.hasNext()) {
               Necr0manthUtils.ClientScheduler.Task task = iterator.next();
               if (task.tick()) {
                  iterator.remove();
               }
            }
         }
      }

      static class Task {
         int ticks;
         Runnable action;

         boolean tick() {
            if (--this.ticks <= 0) {
               this.action.run();
               return true;
            } else {
               return false;
            }
         }

         @Generated
         public Task(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
         }
      }
   }

   public static class TickRate {
      private final Map<Float, Float> tickRateMap = new HashMap<>();

      @SafeVarargs
      public static <I, T extends I> I atTickRate(T function, float tickRate, @NotNull I... huy) {
         Class<?> interfaceClass = huy.getClass().componentType();
         if (!interfaceClass.isInterface()) {
            throw new RuntimeException("Пиздец");
         } else {
            Necr0manthUtils.TickRate tickRateObj = new Necr0manthUtils.TickRate();
            return (I)interfaceClass.cast(Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, (proxy, method, args) -> {
               Object ret = null;

               for (int i = 0; i < tickRateObj.atTickRate(tickRate); i++) {
                  ret = method.invoke(function, args);
               }

               return ret;
            }));
         }
      }

      public int atTickRate(float tickRate) {
         this.tickRateMap.put(tickRate, this.tickRateMap.computeIfAbsent(tickRate, f -> 0.0F) + tickRate);
         int v = Mth.floor(this.tickRateMap.get(tickRate));
         this.tickRateMap.put(tickRate, this.tickRateMap.get(tickRate) - (float)v);
         return v;
      }
   }
}
