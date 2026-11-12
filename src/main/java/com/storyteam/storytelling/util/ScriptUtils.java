package com.storyteam.storytelling.util;

import com.mojang.datafixers.util.Pair;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.particle.circleTint.CircleTintData;
import com.storyteam.storytelling.content.entities.AnimatedNoGazeHumanoid;
import com.storyteam.storytelling.content.entities.ChairEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.mixins.minecraft.ItemEntityAccessor;
import com.storyteam.storytelling.net.SetServerPlayerSpeed;
import com.storyteam.storytelling.net.core.BlackoutPacket;
import com.storyteam.storytelling.net.core.BlinkPacket;
import com.storyteam.storytelling.net.core.DaylightCyclePacket;
import com.storyteam.storytelling.script.performer.state.SpeechEntity;
import com.storyteam.storytelling.script.story.EntityRepository;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import com.storyteam.storytelling.util.world.Location;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.mcf.Resources;

@EventBusSubscriber
public class ScriptUtils {
   public static final ResourceKey<Level> THEABYSS = ResourceKey.create(Registries.DIMENSION, Resources.location("theabyss:the_abyss"));
   public static final ResourceKey<Level> WHITE = ResourceKey.create(Registries.DIMENSION, St.id("white"));

   public static MinecraftServer server() {
      return ServerLifecycleHooks.getCurrentServer();
   }

   @Nullable
   public static ServerPlayer getPlayer() {
      return getPlayer(server());
   }

   @Nullable
   public static ServerPlayer getPlayer(MinecraftServer server) {
      return Optional.ofNullable(server)
         .map(MinecraftServer::getPlayerList)
         .map(PlayerList::getPlayers)
         .stream()
         .flatMap(Collection::stream)
         .findFirst()
         .orElse(null);
   }

   public static ItemStack withAmount(ItemStack item, int count) {
      item.setCount(count);
      return item;
   }

   public static ServerLevel getLevel() {
      return getPlayer() == null ? server().overworld() : getPlayer().serverLevel();
   }

   public static void sendMessageToPlayers(SpeechEntity entity, Component text) {
      if (entity == null) {
         throw new NullPointerException(
            "Cannot invoke \"com.storyteam.storytelling.script.performer.state.SpeechEntity.getName()\" because \"entity\" is null. Attempted to send message "
               + text
               + " to chat."
         );
      } else {
         sendMessageToPlayers(
            Component.literal("")
               .append(Component.literal("[" + entity.getName() + "] ").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(entity.getColor()))))
               .append(text)
         );
      }
   }

   public static Supplier<Component> textWithPrefix(String prefix, String text) {
      return () -> Component.literal(String.format("[%s] ", prefix))
            .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16750070)))
            .append(Component.literal(text).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
   }

   public static Supplier<Component> coloredText(String text) {
      return () -> Component.literal(String.format("[%s] ", text)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16750070)));
   }

   public static void sendMessageToPlayers(String entity, Component text) {
      sendMessageToPlayers(
         Component.literal("").append(Component.literal("[" + entity + "] ").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16750070)))).append(text)
      );
   }

   public static void sendMessageToPlayers(SpeechEntity entity, String text) {
      sendMessageToPlayers(entity, Component.literal(text));
   }

   public static void sendMessageToPlayers(String text) {
      sendMessageToPlayers(Component.literal(text));
   }

   public static void sendMessageToPlayers(Component text) {
      MinecraftServer server = server();
      if (server != null) {
         for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.displayClientMessage(text, false);
         }
      }
   }

   public static double getAngle(Entity source, Entity target) {
      Vec3 look = source.getLookAngle().normalize();
      Vec3 entCenter = target.getBoundingBox().getCenter();
      Vec3 playerEye = source.getEyePosition(1.0F);
      Vec3 dir = entCenter.subtract(playerEye);
      return Math.toDegrees(Math.acos(dir.normalize().dot(look)));
   }

   public static double getAngle(Entity source, Vec3 target) {
      Vec3 look = source.getLookAngle().normalize();
      Vec3 playerEye = source.getEyePosition(1.0F);
      Vec3 dir = target.subtract(playerEye);
      return Math.toDegrees(Math.acos(dir.normalize().dot(look)));
   }

   public static Vec3 vec3FromBone(String bone, HumanoidEntity e, Vec3 offset) {
      Vec3 vec3 = Vec3.ZERO;
      Optional<Matrix4d> body_Transform = e.getTransforms(bone, false);
      if (body_Transform.isPresent()) {
         Matrix4d body_Matrix = body_Transform.get();
         Vector3d basePos = new Vector3d(offset.x(), offset.y(), offset.z());
         Vector3d body_3d = body_Matrix.transformPosition(basePos);
         Optional<Vec3> body_opt = Optional.of(new Vec3(body_3d.x(), body_3d.y(), body_3d.z()));
         vec3 = body_opt.get();
      }

      return vec3;
   }

   public static Vec3 vec3FromBone(String bone, BaseMob e, Vec3 offset) {
      Vec3 vec3 = Vec3.ZERO;
      Optional<Matrix4d> body_Transform = e.getTransforms(bone, false);
      if (body_Transform.isPresent()) {
         Matrix4d body_Matrix = body_Transform.get();
         Vector3d basePos = new Vector3d(offset.x(), offset.y(), offset.z());
         Vector3d body_3d = body_Matrix.transformPosition(basePos);
         Optional<Vec3> body_opt = Optional.of(new Vec3(body_3d.x(), body_3d.y(), body_3d.z()));
         vec3 = body_opt.get();
      }

      return vec3;
   }

   public static Vec3 vec3FromBone(String bone, AnimatedNoGazeHumanoid e, Vec3 offset) {
      Vec3 vec3 = Vec3.ZERO;
      Optional<Matrix4d> body_Transform = e.getTransforms(bone, false);
      if (body_Transform.isPresent()) {
         Matrix4d body_Matrix = body_Transform.get();
         Vector3d basePos = new Vector3d(offset.x(), offset.y(), offset.z());
         Vector3d body_3d = body_Matrix.transformPosition(basePos);
         Optional<Vec3> body_opt = Optional.of(new Vec3(body_3d.x(), body_3d.y(), body_3d.z()));
         vec3 = body_opt.get();
      }

      return vec3;
   }

   public static ItemStack name(ItemStack stack, String text) {
      stack.setHoverName(Component.literal(text));
      return stack;
   }

   public static Outfit randomOutfitUniversal(Random rng, List<VanitySet> outfits) {
      return Outfit.builder()
         .set(VanitySlot.HAIR, randomOutfitPartUniversal(VanitySlot.HAIR, rng, outfits))
         .set(VanitySlot.BODY, randomOutfitPartUniversal(VanitySlot.BODY, rng, outfits))
         .set(VanitySlot.LEGS, randomOutfitPartUniversal(VanitySlot.LEGS, rng, outfits))
         .set(VanitySlot.FEET, randomOutfitPartUniversal(VanitySlot.FEET, rng, outfits))
         .build();
   }

   public static ItemStack randomOutfitPartUniversal(VanitySlot equipmentSlot, Random random, List<VanitySet> outfits) {
      if (outfits.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         for (int i = 0; i < 10; i++) {
            ItemStack stack = outfits.get(random.nextInt(outfits.size())).get(equipmentSlot).getDefaultInstance();
            if (!stack.isEmpty()) {
               return stack;
            }
         }

         return ItemStack.EMPTY;
      }
   }

   public static Outfit randomOutfitPartOutfit(Random random, List<VanitySet> outfits) {
      if (outfits.isEmpty()) {
         return Outfit.builder().build();
      } else {
         VanitySet vanitySet = outfits.get(random.nextInt(outfits.size()));
         return Outfit.of(vanitySet);
      }
   }

   public static ItemStack randomOutfitPartUniversalNotOverkill(VanitySlot equipmentSlot, Random random, List<VanitySet> outfits) {
      return outfits.isEmpty() ? ItemStack.EMPTY : outfits.get(random.nextInt(outfits.size())).get(equipmentSlot).getDefaultInstance();
   }

   public static double getAngle(Entity source, Vec2 target) {
      Vec3 look = source.getLookAngle().normalize();
      Vec3 playerEye = source.getEyePosition(1.0F);
      look = new Vec3(look.x, 0.0, look.z);
      playerEye = new Vec3(playerEye.x, 0.0, playerEye.z);
      Vec3 dir = new Vec3((double)target.x, 0.0, (double)target.y).subtract(playerEye);
      return Math.toDegrees(Math.acos(dir.normalize().dot(look)));
   }

   public static double getAngle(Vec3 eye, Vec3 look, Vec3 target) {
      Vec3 dir = target.subtract(eye);
      return Math.toDegrees(Math.acos(dir.normalize().dot(look)));
   }

   public static double getAnglePosition(Entity source, Vec3 target) {
      Vec3 look = source.getLookAngle().normalize();
      Vec3 playerPos = source.position();
      Vec3 dir = target.subtract(playerPos);
      return Math.toDegrees(Math.acos(dir.normalize().dot(look)));
   }

   public static Item getItem(String id) {
      ResourceLocation rel = Resources.location(id);
      return ForgeRegistries.ITEMS.containsKey(rel) ? (Item)ForgeRegistries.ITEMS.getValue(rel) : Items.SPONGE;
   }

   public static ItemLike getItemLike(String id) {
      return () -> getItem(id);
   }

   public static Block getBlock(String id) {
      ResourceLocation rel = Resources.location(id);
      return ForgeRegistries.BLOCKS.containsKey(rel) ? (Block)ForgeRegistries.BLOCKS.getValue(rel) : Blocks.SPONGE;
   }

   public static Set<Item> getItems(String... id) {
      return Arrays.stream(id).map(el -> (Item)ForgeRegistries.ITEMS.getValue(Resources.location(el.trim()))).collect(Collectors.toSet());
   }

   public static Block getBlockItem(Item item) {
      return Item.BY_BLOCK.containsValue(item)
         ? Item.BY_BLOCK.entrySet().stream().filter(el -> ((Item)el.getValue()).equals(item)).findFirst().map(Entry::getKey).orElse(Blocks.AIR)
         : Blocks.AIR;
   }

   public static int pickUpItemMax(LivingEntity entity, int maxAmount, float grow, Predicate<ItemStack> filter) {
      return pickUpItemMax(entity, maxAmount, grow, filter, false);
   }

   public static int pickUpItemMax(LivingEntity entity, int maxAmount, float grow, Predicate<ItemStack> filter, boolean ignoreDelay) {
      if (entity == null) {
         return 0;
      } else {
         List<ItemEntity> items = entity.level().getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate((double)grow));
         int picked = 0;

         for (ItemEntity item : items) {
            if (item.isAlive() && (ignoreDelay || !item.hasPickUpDelay()) && !Objects.equals(((ItemEntityAccessor)item).getThrower(), entity.getUUID())) {
               ItemStack stack = item.getItem();
               if (!stack.isEmpty() && filter.test(stack)) {
                  int amtPick = Math.min(maxAmount - picked, stack.getCount());
                  entity.take(item, amtPick);
                  stack.shrink(amtPick);
                  picked += amtPick;
                  item.setItem(stack);
                  if (stack.isEmpty()) {
                     item.discard();
                  }

                  item.setDefaultPickUpDelay();
                  if (picked >= maxAmount) {
                     return picked;
                  }
               }
            }
         }

         return picked;
      }
   }

   public static void setBlackout(int color, int inTime, int time, int outTime, boolean belowChat) {
      Network.sendTo(getPlayer(), new BlackoutPacket(color, inTime, time, outTime, belowChat));
   }

   public static void setBlackout(int color, int inTime, int time, int outTime) {
      Network.sendTo(getPlayer(), new BlackoutPacket(color, inTime, time, outTime, false));
   }

   public static void setBlink(int color, int inTime, int time, int outTime) {
      Network.sendTo(getPlayer(), new BlinkPacket(color, inTime, time, outTime));
   }

   public static void setServerPlayerSpeed(ServerPlayer player, Vec3 movement) {
      player.setDeltaMovement(movement);
      Network.sendTo(player, new SetServerPlayerSpeed(movement));
   }

   public static ItemStack findEquippedCurio(Entity entity, Item item) {
      return ItemStack.EMPTY; // Curios integration disabled.
   }

   public static void clientLockTime(boolean lockTime) {
      Network.sendToServer(new DaylightCyclePacket(!lockTime));
   }

   public static void serverLockTime(boolean lockTime) {
      ((BooleanValue)server().getGameRules().getRule(GameRules.RULE_DAYLIGHT)).set(!lockTime, server());
   }

   public static void doorState(String pos, boolean open) {
      String[] r = pos.split(" ");
      BlockPos p = new BlockPos(Integer.parseInt(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]));
      if (getLevel().getBlockState(p).getBlock() instanceof DoorBlock doorBlock) {
         doorBlock.setOpen(null, getLevel(), getLevel().getBlockState(p), p, open);
      }

      if (getLevel().getBlockState(p).getBlock() instanceof FenceGateBlock) {
         BlockState state = (BlockState)getLevel().getBlockState(p).setValue(FenceGateBlock.OPEN, open);
         getLevel().setBlock(p, state, 10);
      }

      if (getLevel().getBlockState(p).getBlock() instanceof TrapDoorBlock) {
         BlockState state = (BlockState)getLevel().getBlockState(p).setValue(TrapDoorBlock.OPEN, open);
         getLevel().setBlock(p, state, 10);
      }
   }

   public static void createChairAtPosAndSit(Vec3 pos) {
      ChairEntity chair = (ChairEntity)EntitiesST.CHAIR.create(getLevel());
      chair.setPos(pos.x, pos.y, pos.z);
      getLevel().addFreshEntity(chair);
      getPlayer().startRiding(chair);
   }

   public static AABB aabb(String cord1, String cord2) {
      Location loc = new Location(cord1);
      Location loc2 = new Location(cord2);
      return new AABB(loc, loc2);
   }

   public static <T extends Entity> void killAllEntitiesInBox(AABB box, Class<T> entityClass, Predicate<T> entityCondition) {
      killAllEntitiesInBox(getLevel(), box, entityClass, entityCondition);
   }

   public static <T extends Entity> void killAllEntitiesInBox(Level level, AABB box, Class<T> entityClass, Predicate<T> entityCondition) {
      for (T entity : level.getEntitiesOfClass(entityClass, box, entityCondition)) {
         entity.discard();
      }
   }

   public static <T extends Entity> void killAllEntitiesInRadius(Vec3 pos, int radius, Class<T> entityClass, Predicate<T> entityCondition) {
      killAllEntitiesInRadius(getLevel(), pos, radius, entityClass, entityCondition);
   }

   public static <T extends Entity> void killAllEntitiesInRadius(Level level, Vec3 pos, int radius, Class<T> entityClass, Predicate<T> entityCondition) {
      AABB box = new AABB(pos, pos).inflate((double)radius);
      killAllEntitiesInBox(level, box, entityClass, entityCondition);
   }

   public static void killAllItemsInRadius(Vec3 pos, int radius, Predicate<ItemEntity> itemEntityCondition) {
      killAllEntitiesInRadius(pos, radius, ItemEntity.class, itemEntityCondition);
   }

   public static void killAllItemsInRadius(Vec3 pos, int radius, ItemLike itemToKill) {
      killAllItemsInRadius(pos, radius, (Predicate<ItemEntity>)(itemEntity -> itemEntity.getItem().is(itemToKill.asItem())));
   }

   public static void killAllItemsInBox(AABB box) {
      killAllEntitiesInBox(box, ItemEntity.class, itemEntity -> true);
   }

   public static void killAllItemsInRadius(Vec3 pos, int radius) {
      killAllItemsInRadius(pos, radius, (Predicate<ItemEntity>)(itemEntity -> true));
   }

   public static ItemStack applyCustomMapToItemStack(ItemStack map, String mapId) {
      map.getTag().putString("custom_map", mapId);
      return map;
   }

   public static ItemStack applyLoreToItem(ItemStack item, String lore) {
      item.getOrCreateTag().putString("st_lore", lore);
      return item;
   }

   public static void moveEntityToPlayer(LivingEntity e, Player player, double speed) {
      if (e != null) {
         e.setDeltaMovement(player.position().subtract(e.position()).normalize().multiply(speed, speed, speed));
      }
   }

   public static void moveEntityToEntity(LivingEntity e, LivingEntity target, double speed) {
      if (e != null && target != null) {
         e.setDeltaMovement(target.position().subtract(e.position()).normalize().multiply(speed, speed, speed));
      }
   }

   public static void moveEntityFromPlayer(LivingEntity e, Player player, double speed) {
      if (e != null) {
         e.setDeltaMovement(e.position().subtract(player.position()).normalize().multiply(speed, speed, speed));
      }
   }

   public static void moveEntityFromEntity(LivingEntity e, LivingEntity target, double speed) {
      if (e != null && target != null) {
         e.setDeltaMovement(e.position().subtract(target.position()).normalize().multiply(speed, speed, speed));
      }
   }

   public static boolean doCharacterSeePlayer(HumanoidEntity entity, LivingEntity entityToSee, float radius, float radiusOfSight) {
      if (entity == null) {
         return false;
      } else {
         if (entityToSee instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
         }

         if (entity.position().multiply(1.0, 0.0, 1.0).subtract(entityToSee.position().multiply(1.0, 0.0, 1.0)).length() > (double)radius) {
            return false;
         } else if (Math.abs(entity.position().y - entityToSee.position().y) > 5.0) {
            return false;
         } else {
            Vec3 lookVec = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
            Vec3 entityPos = entityToSee.position().multiply(1.0, 0.0, 1.0);
            Vec3 between = entityPos.subtract(entity.position().multiply(1.0, 0.0, 1.0)).normalize();
            double scalar = lookVec.x * between.x + lookVec.y * between.y + lookVec.z * between.z;
            double modulo = between.length() * lookVec.length();
            double angle = Math.toDegrees(Math.acos(scalar / modulo));
            return angle < (double)radiusOfSight;
         }
      }
   }

   public static <T extends Event> void addEventHandler(EventPriority priority, Consumer<T> consumer) {
      MinecraftForge.EVENT_BUS.addListener(priority, consumer);
   }

   public static <T extends GenericEvent<? extends F>, F> void addGenericEventHandler(Class<F> geneticType, EventPriority priority, Consumer<T> consumer) {
      MinecraftForge.EVENT_BUS.addGenericListener(geneticType, priority, consumer);
   }

   public static ItemStack makePapper(String[] text) {
      ItemStack st = Items.PAPER.getDefaultInstance();
      st.setHoverName(Component.literal("Письмо").withStyle(ChatFormatting.WHITE));
      ListTag lore = new ListTag();

      for (String s : text) {
         lore.add(StringTag.valueOf(Serializer.toJson(Component.literal(s))));
      }

      st.getOrCreateTagElement("display").put("Lore", lore);
      return st;
   }

   public static boolean hasItem(Item item) {
      return getPlayer().getInventory().hasAnyOf(Set.of(item));
   }

   public static boolean isLeftHandDisabled() {
      return ScriptIO.WORLD_EXTRA_NBT.getBoolean("left_hand_disabled");
   }

   public static void setLeftHandDisabled(boolean state) {
      ScriptIO.WORLD_EXTRA_NBT.putBoolean("left_hand_disabled", state);
   }

   public static void setDoorState(Level world, String pos, boolean state) {
      BlockPos bpos = PositionParser.parseBlockPos(pos);
      BlockState door = world.getBlockState(bpos);
      if (door.getBlock() instanceof DoorBlock doorBlock) {
         doorBlock.setOpen(null, world, door, bpos, state);
      } else if (door.getBlock() instanceof FenceGateBlock fg) {
         world.setBlockAndUpdate(bpos, (BlockState)door.setValue(FenceGateBlock.OPEN, state));
      }
   }

   public static void setDoorState(Level world, BlockPos bpos, boolean state) {
      BlockState door = world.getBlockState(bpos);
      if (door.getBlock() instanceof DoorBlock doorBlock) {
         doorBlock.setOpen(null, world, door, bpos, state);
      } else if (door.getBlock() instanceof FenceGateBlock fg) {
         world.setBlockAndUpdate(bpos, (BlockState)door.setValue(FenceGateBlock.OPEN, state));
      }
   }

   public static void updateTile(BlockEntity tile) {
      if (tile.getLevel() != null) {
         tile.setChanged();
         BlockState state = tile.getLevel().getBlockState(tile.getBlockPos());
         tile.getLevel().sendBlockUpdated(tile.getBlockPos(), state, state, 3);
      }
   }

   public static void setGameMode(GameType type) {
      getPlayer().setGameMode(type);
   }

   public static void setBrotherhoodPageState(boolean state) {
      ScriptIO.WORLD_EXTRA_NBT.putBoolean("brotherhood_page", state);
   }

   public static boolean isBrotherhoodPageOpened() {
      return ScriptIO.WORLD_EXTRA_NBT.getBoolean("brotherhood_page");
   }

   public static void summonStealthParticles(HumanoidEntity entity, Color color, float circleRadius, float sightRadius) {
      Vec3 lookAngle = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
      int particleCount = (int)((double)(circleRadius * 2.0F) * Math.PI / 0.25);
      double angle = (Math.PI * 2) / (double)particleCount;
      Vec3 mAngle = lookAngle.yRot(-((float)Math.toRadians((double)sightRadius)));

      for (double i = 0.0; i < Math.toRadians((double)(sightRadius - 1.0F)) * 2.0; i += angle) {
         mAngle = mAngle.yRot((float)angle);
         double x = mAngle.x * (double)circleRadius;
         double z = mAngle.z * (double)circleRadius;
         entity.level()
            .addParticle(
               new CircleTintData(color, 0.25F, 1, 1.0F, false, false),
               entity.position().x + x,
               entity.position().y + 0.1,
               entity.position().z + z,
               0.0,
               0.0,
               0.0
            );
      }

      for (float i = 1.0F; i < circleRadius; i += 0.5F) {
         Vec3 left = lookAngle.yRot((float)Math.toRadians((double)sightRadius)).multiply((double)i, 0.0, (double)i);
         Vec3 right = lookAngle.yRot(-((float)Math.toRadians((double)sightRadius))).multiply((double)i, 0.0, (double)i);
         entity.level()
            .addParticle(
               new CircleTintData(color, 0.25F, 1, 1.0F, false, false),
               entity.position().x + left.x,
               entity.position().y + 0.1F,
               entity.position().z + left.z,
               0.0,
               0.0,
               0.0
            );
         entity.level()
            .addParticle(
               new CircleTintData(color, 0.25F, 1, 1.0F, false, false),
               entity.position().x + right.x,
               entity.position().y + 0.1F,
               entity.position().z + right.z,
               0.0,
               0.0,
               0.0
            );
      }
   }

   public static void summonStealthParticles(LivingEntity entity, Color color, Boolean renderParticles, float circleRadius, float sightRadius, Runnable caught) {
      Vec3 lookAngle = entity.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
      int particleCount = (int)((double)(circleRadius * 2.0F) * Math.PI / 0.25);
      double angle = (Math.PI * 2) / (double)particleCount;
      Vec3 mAngle = lookAngle.yRot(-((float)Math.toRadians((double)sightRadius)));
      if (renderParticles) {
         for (double i = 0.0; i < Math.toRadians((double)(sightRadius - 1.0F)) * 2.0; i += angle) {
            mAngle = mAngle.yRot((float)angle);
            double x = mAngle.x * (double)circleRadius;
            double z = mAngle.z * (double)circleRadius;
            ParticleHelper.spawnDirectedParticle(
               entity.level(),
               new CircleTintData(color, 0.25F, 8, 1.0F, false, false),
               entity.position().x + x,
               entity.position().y + 0.1,
               entity.position().z + z,
               0.0,
               0.0,
               0.0
            );
         }

         for (float i = 1.0F; i < circleRadius; i += 0.5F) {
            Vec3 left = lookAngle.yRot((float)Math.toRadians((double)sightRadius)).multiply((double)i, 0.0, (double)i);
            Vec3 right = lookAngle.yRot(-((float)Math.toRadians((double)sightRadius))).multiply((double)i, 0.0, (double)i);
            ParticleHelper.spawnDirectedParticle(
               entity.level(),
               new CircleTintData(color, 0.25F, 1, 1.0F, false, false),
               entity.position().x + left.x,
               entity.position().y + 0.1F,
               entity.position().z + left.z,
               0.0,
               0.0,
               0.0
            );
            ParticleHelper.spawnDirectedParticle(
               entity.level(),
               new CircleTintData(color, 0.25F, 1, 1.0F, false, false),
               entity.position().x + right.x,
               entity.position().y + 0.1F,
               entity.position().z + right.z,
               0.0,
               0.0,
               0.0
            );
         }
      }

      if (getAnglePosition(entity, getPlayer().position()) <= (double)sightRadius && getPlayer().distanceTo(entity) <= circleRadius) {
         caught.run();
      }
   }

   public static void summonStealthParticles(LivingEntity entity, Color color, float circleRadius, float sightRadius, Runnable caught) {
      summonStealthParticles(entity, color, true, circleRadius, sightRadius, caught);
   }

   @Nullable
   public static Entity getEntityFromCharacter(String id) {
      EntityRepository rep = IStoryServer.get().getEntityRepository();
      Character<?> ch = Character.getById(id);
      return ch == null ? null : rep.getEntity((Character<Entity>)ch);
   }

   public static CompoundTag getExtraDataTag(String id) {
      CompoundTag extraNbt = ScriptIO.WORLD_EXTRA_NBT;
      CompoundTag tag;
      if (extraNbt.contains(id)) {
         tag = extraNbt.getCompound(id);
      } else {
         tag = new CompoundTag();
         extraNbt.put(id, tag);
      }

      return tag;
   }

   public static CompoundTag getTagFromTag(CompoundTag tag, String tagName) {
      if (!tag.contains(tagName)) {
         tag.put(tagName, new CompoundTag());
      }

      return tag.getCompound(tagName);
   }

   public static AABB clipBoxesAndReturnClosest(Player player, List<AABB> boxes) {
      double reach = player.getAttributeValue((Attribute)ForgeMod.BLOCK_REACH.get());
      Vec3 begin = player.position().add(0.0, (double)player.getEyeHeight(), 0.0);
      return clipBoxesAndReturnClosest(begin, begin.add(player.getLookAngle().multiply(reach, reach, reach)), boxes);
   }

   public static AABB clipBoxesAndReturnClosest(Vec3 begin, Vec3 end, List<AABB> boxes) {
      AABB minBox = null;
      double dist = 2.147483647E9;

      for (AABB box : boxes) {
         Optional<Vec3> p = box.clip(begin, end);
         if (p.isPresent()) {
            double d = begin.subtract(p.get()).length();
            if (d < dist) {
               minBox = box;
               dist = d;
            }
         }
      }

      return minBox;
   }

   public static boolean isVectorTouchPlayerWithWallCollisions(Vec3 start, Vec3 end) {
      Vec3 delta = end.subtract(start);
      Vec3 dir = delta.normalize();
      double len = delta.length();
      double d = 0.1;

      for (double cur = 0.0; cur < len; cur += d) {
         Vec3 pos = new Vec3(start.x + dir.x * cur, start.y + dir.y * cur, start.z + dir.z * cur);
         if (!getLevel().getBlockState(BlockPos.containing(pos)).getBlock().equals(Blocks.AIR)) {
            return false;
         }

         if (pos.distanceTo(getPlayer().position().add(0.0, 1.5, 0.0)) <= 1.0 || pos.distanceTo(getPlayer().position().add(0.0, 0.5, 0.0)) <= 1.0) {
            return true;
         }
      }

      return false;
   }

   public static void addAttributeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
      AttributeMap map = entity.getAttributes();
      if (map.hasAttribute(attribute)) {
         AttributeInstance inst = map.getInstance(attribute);
         if (!inst.hasModifier(modifier)) {
            inst.addTransientModifier(modifier);
         }
      }
   }

   public static void replaceAttributeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
      AttributeMap map = entity.getAttributes();
      if (map.hasAttribute(attribute)) {
         AttributeInstance inst = map.getInstance(attribute);
         inst.removeModifier(modifier.getId());
         inst.addTransientModifier(modifier);
      }
   }

   public static void removeAttributeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
      AttributeMap map = entity.getAttributes();
      if (map.hasAttribute(attribute)) {
         AttributeInstance inst = map.getInstance(attribute);
         if (inst.hasModifier(modifier)) {
            inst.removeModifier(modifier);
         }
      }
   }

   public static void removeAttributeModifier(LivingEntity entity, Attribute attribute, UUID modifierId) {
      AttributeMap map = entity.getAttributes();
      if (map.hasAttribute(attribute)) {
         AttributeInstance inst = map.getInstance(attribute);
         inst.removeModifier(modifierId);
      }
   }

   public static boolean atTickRate(long tickCount, int interval) {
      return tickCount % (long)interval == 0L;
   }

   public static void setItemToDrainExclusions(ItemStack... stacks) {
      for (ItemStack stack : stacks) {
         stack.getOrCreateTag().putBoolean("drain_exclusion", true);
      }
   }

   public static void removeItemFromDrainExclusions(ItemStack... stacks) {
      for (ItemStack stack : stacks) {
         stack.getOrCreateTag().putBoolean("drain_exclusion", false);
      }
   }

   public static Pair<Float, Float> getPitchYaw(Vec3 eyes, Vec3 target) {
      double dx = target.x - eyes.x;
      double dy = target.y - eyes.y;
      double dz = target.z - eyes.z;
      double yawRadians = Math.atan2(-dx, dz);
      float yawDegrees = (float)Math.toDegrees(yawRadians);
      if (yawDegrees < -180.0F) {
         yawDegrees += 360.0F;
      } else if (yawDegrees > 180.0F) {
         yawDegrees -= 360.0F;
      }

      double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
      double pitchRadians = Math.atan2(dy, horizontalDistance);
      float pitchDegrees = (float)Math.toDegrees(pitchRadians);
      return new Pair(pitchDegrees, yawDegrees);
   }
}
