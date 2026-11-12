package com.storyteam.storytelling.util;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.client.render.entity.def.ModelConfiguration;
import com.storyteam.storytelling.content.items.ItemMissing;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.system.currency.content.entities.EidosEntity;
import com.storyteam.storytelling.util.data.buffered.BufferedData;
import java.text.CompactNumberFormat;
import java.text.NumberFormat;
import java.text.NumberFormat.Style;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3d;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.util.mcf.Resources;

public class UtilST {
   public static Random rng = new Random(12345678L);
   private static final HashMap<Direction, Integer> DIRECTIONS;

   public static <T> Stream<T> concatStreams(Iterable<Stream<T>> streams) {
      Stream<T> r = Stream.empty();

      for (Stream<T> stream : streams) {
         r = Stream.concat(r, stream);
      }

      return r;
   }

   public static Vec3 toVec3(Vec3i vec3i) {
      return new Vec3((double)vec3i.getX(), (double)vec3i.getY(), (double)vec3i.getZ());
   }

   @SafeVarargs
   public static <T, C extends Collection<T>> C concat(C empty, C... collection) {
      return Arrays.stream(collection).reduce(empty, (c1, c2) -> {
         c1.addAll(c2);
         return c1;
      });
   }

   public static ItemStack createStack(ResourceLocation id, int count) {
      Item i = (Item)ForgeRegistries.ITEMS.getValue(id);
      return i != Items.AIR ? new ItemStack(i, count) : ItemMissing.createMissing(id, count);
   }

   public static ItemStack createStack(ResourceLocation id) {
      return createStack(id, 1);
   }

   public static ItemStack createStack(String id, int count) {
      return createStack(Resources.location(id), count);
   }

   public static ItemStack createStack(String id) {
      return createStack(id, 1);
   }

   public static ItemStack createStackOrDefault(String id, Supplier<ItemStack> defaultStack) {
      ItemStack st = createStack(id, 1);
      return !st.isEmpty() && !st.is(ItemsST.MISSING) ? st : defaultStack.get();
   }

   public static <T> Supplier<T> bufferSupplier(Supplier<T> supplier) {
      BufferedData<T> reference = new BufferedData<>();
      return () -> {
         if (!reference.isChange()) {
            reference.set(supplier.get());
         }

         return reference.get();
      };
   }

   public static double getGroundY(Level level, Vec3 pVec) {
      BlockPos blockpos = BlockPos.containing(pVec);
      return level.getBlockState(blockpos.below()).isAir() ? pVec.y : WalkNodeEvaluator.getFloorLevel(level, blockpos);
   }

   public static ResourceKey<Level> getWorld(String id) {
      return ResourceKey.create(Registries.DIMENSION, Resources.location(id));
   }

   public static void server(Level world, Consumer<ServerLevel> handler) {
      if (!world.isClientSide() && world instanceof ServerLevel) {
         handler.accept((ServerLevel)world);
      }
   }

   public static <T> T supplyNullOnException(Supplier<T> supplier) {
      try {
         return supplier.get();
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static <T> T random(Collection<T> coll) {
      int num = (int)(Math.random() * (double)coll.size());

      for (T t : coll) {
         if (--num < 0) {
            return t;
         }
      }

      throw new AssertionError();
   }

   public static <T> T random(List<T> coll) {
      return coll.get(new Random().nextInt(coll.size()));
   }

   public static <T, U, S extends SortedMap<T, U>> S rotatedTree(S tree, T root, boolean reversed, Function<Comparator<T>, S> creator) {
      @SuppressWarnings({"unchecked", "rawtypes"})
      Comparator<T> comparator = (Comparator<T>)(tree.comparator() == null
         ? (reversed ? Comparator.reverseOrder() : Comparator.naturalOrder())
         : (reversed ? tree.comparator().reversed() : tree.comparator()));
      Comparator<T> newComparator = Comparator.<T>comparingInt(t -> (comparator.compare((T)t, root) + 3) % 3).thenComparing(comparator);
      S newTree = (S)creator.apply(newComparator);
      newTree.putAll(tree);
      return newTree;
   }

   public static <T, S extends SortedSet<T>> S rotatedSet(S tree, T root, boolean reversed, Function<Comparator<T>, S> creator) {
      @SuppressWarnings({"unchecked", "rawtypes"})
      Comparator<T> comparator = (Comparator<T>)(tree.comparator() == null
         ? (reversed ? Comparator.reverseOrder() : Comparator.naturalOrder())
         : (reversed ? tree.comparator().reversed() : tree.comparator()));
      Comparator<T> newComparator = Comparator.<T>comparingInt(t -> (comparator.compare((T)t, root) + 3) % 3).thenComparing(comparator);
      S newTree = (S)creator.apply(newComparator);
      newTree.addAll(tree);
      return newTree;
   }

   public static CompoundTag serializeMap(HashMap<String, ? extends INBTSerializable<CompoundTag>> map) {
      CompoundTag tag = new CompoundTag();
      map.forEach((s, nbt) -> tag.put(s, nbt.serializeNBT()));
      return tag;
   }

   public static <T extends INBTSerializable<CompoundTag>> HashMap<String, T> deserializeMap(CompoundTag tag, Supplier<T> creator) {
      HashMap<String, T> map = new HashMap<>();

      for (String key : tag.getAllKeys()) {
         T value = (T)creator.get();
         value.deserializeNBT(tag.getCompound(key));
         map.put(key, value);
      }

      return map;
   }

   public static <T extends INBTSerializable<CompoundTag>> HashMap<String, T> deserialize(CompoundTag tag, Supplier<T> creator) {
      HashMap<String, T> map = new HashMap<>();

      for (String key : tag.getAllKeys()) {
         T value = (T)creator.get();
         value.deserializeNBT(tag.getCompound(key));
         map.put(key, value);
      }

      return map;
   }

   public static <K> CompoundTag serializeMap(HashMap<K, ? extends INBTSerializable<CompoundTag>> map, Function<K, String> parser) {
      CompoundTag tag = new CompoundTag();
      map.forEach((k, nbt) -> tag.put(parser.apply((K)k), nbt.serializeNBT()));
      return tag;
   }

   public static VoxelShape rotateBox(VoxelShape box, Direction direction) {
      AABB aabb = box.bounds();
      if (direction.ordinal() < 2) {
         return box;
      } else {
         double a = Math.toRadians((double)DIRECTIONS.get(direction).intValue());
         Vector3d minCoords = new Vector3d(
            aabb.minX * Math.cos(a) + aabb.minZ * Math.sin(a), aabb.minY, aabb.minX * Math.sin(a) * -1.0 + aabb.minZ * Math.cos(a)
         );
         Vector3d maxCoords = new Vector3d(
            aabb.maxX * Math.cos(a) + aabb.maxZ * Math.sin(a), aabb.maxY, aabb.maxX * Math.sin(a) * -1.0 + aabb.maxZ * Math.cos(a)
         );
         return Block.box(
            Math.min(minCoords.x * 16.0, maxCoords.x * 16.0) + (double)(direction != Direction.EAST && direction != Direction.SOUTH ? 0 : 16),
            minCoords.y * 16.0,
            Math.min(minCoords.z * 16.0, maxCoords.z * 16.0) + (double)(direction != Direction.WEST && direction != Direction.SOUTH ? 0 : 16),
            Math.max(minCoords.x * 16.0, maxCoords.x * 16.0) + (double)(direction != Direction.EAST && direction != Direction.SOUTH ? 0 : 16),
            maxCoords.y * 16.0,
            Math.max(minCoords.z * 16.0, maxCoords.z * 16.0) + (double)(direction != Direction.WEST && direction != Direction.SOUTH ? 0 : 16)
         );
      }
   }

   public static ItemStack parseCraftTweakerItem(String str, int count) {
      ItemStack stack = parseCraftTweakerItem(str);
      stack.setCount(count);
      return stack;
   }

   public static ItemStack parseCraftTweakerItem(String str) {
      try {
         Pattern idDelimiter = Pattern.compile("<item:(.+?)>");
         Pattern nbtDelimiter = Pattern.compile("\\.withTag\\((.+?)\\)");
         Matcher idMatcher = idDelimiter.matcher(str);
         Matcher nbtMatcher = nbtDelimiter.matcher(str);
         idMatcher.find();
         String id = idMatcher.group(1);
         Item item = (Item)ForgeRegistries.ITEMS.getValue(Resources.location(id));
         if (item == null) {
            return ItemsST.MISSING.getDefaultInstance();
         } else {
            ItemStack stack = item.getDefaultInstance();
            if (nbtMatcher.find()) {
               String nbt = nbtMatcher.group(1);
               if (nbt != null) {
                  Pattern primitives = Pattern.compile("(\\w+): (\".+?\"|\\d+(\\.\\d+)?) as (int|string|float|double|byte|short)");
                  String s = primitives.matcher(nbt).replaceAll("\"$1\": $2");
                  Pattern structures = Pattern.compile("(?<=, |\\{)(\\w+)(?=: [{\\[])");
                  s = structures.matcher(s).replaceAll("\"$1\"");
                  stack.setTag((CompoundTag)JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, new JsonParser().parse(s)));
               }
            }

            return stack.isEmpty() ? createStack(id) : stack;
         }
      } catch (Exception var12) {
         Storytelling.LOG.info("Couldn't deserialize itemstack: " + str);
         var12.printStackTrace();
         return ItemsST.MISSING.getDefaultInstance();
      }
   }

   public static Set<ItemStack> parseCraftTweakerItems(String... id) {
      return Arrays.stream(id).map(UtilST::parseCraftTweakerItem).collect(Collectors.toSet());
   }

   public static float map(float old_value, float old_bottom, float old_top, float new_bottom, float new_top) {
      return (old_value - old_bottom) / (old_top - old_bottom) * (new_top - new_bottom) + new_bottom;
   }

   public static void giveItemStack(ServerPlayer player, ItemStack itemStack) {
      if (!player.addItem(itemStack.copy())) {
         ScriptUses.dropItem(player, itemStack.copy(), true, true);
      }
   }

   public static SimpleBedrockModel<?> model(IGeometryContainer container) {
      return new SimpleBedrockModel(new ModelConfiguration(container));
   }

   public static String truncateNumber(float number, int fractionDigits) {
      NumberFormat instance = CompactNumberFormat.getCompactNumberInstance(Locale.ENGLISH, Style.SHORT);
      instance.setMaximumFractionDigits(fractionDigits);
      return instance.format((double)number);
   }

   public static int getMobEffectsOffset() {
      return 35;
   }

   public static void dropEidos(Level level, Vec3 loc, int eidosCount, boolean ignoreModifiers, int lifetime) {
      dropEidos(level, loc, eidosCount, 15, ignoreModifiers, lifetime);
   }

   public static void dropEidos(Level level, Vec3 loc, int eidosCount, boolean ignoreModifiers) {
      dropEidos(level, loc, eidosCount, 15, ignoreModifiers, 1000);
   }

   public static void dropEidos(Level level, Vec3 loc, int eidosCount, int delayUnpickable, boolean ignoreModifiers, int lifetime) {
      int reward = eidosCount;
      if (!ignoreModifiers) {
         float exp = (float)eidosCount;
         int base = (int)exp;
         int extra = rng.nextFloat() < exp - (float)base ? 1 : 0;
         reward = base + extra;
      }

      dropEidosInternal(level, loc, reward, delayUnpickable, lifetime);
   }

   private static void dropEidosInternal(Level level, Vec3 loc, int eidosCount, int delayUnpickable, int lifetime) {
      if (eidosCount > 0) {
         for (int stageIdx = EidosEntity.STAGES.length - 1; stageIdx >= 0; stageIdx--) {
            int stageValue = EidosEntity.STAGES[stageIdx];
            if (eidosCount >= stageValue) {
               int orbCount = eidosCount / stageValue;
               int remainder = eidosCount % stageValue;

               for (int i = 0; i < orbCount; i++) {
                  spawnEidos(level, loc, stageValue, delayUnpickable, lifetime);
               }

               dropEidosInternal(level, loc, remainder, delayUnpickable, lifetime);
               return;
            }

            if (stageIdx == 0 && eidosCount > 0) {
               spawnEidos(level, loc, eidosCount, delayUnpickable, lifetime);
            }
         }
      }
   }

   private static void spawnEidos(Level level, Vec3 loc, int amount, int delayUnpickable, int lifetime) {
      EidosEntity orb = (EidosEntity)EntitiesST.EIDOS_PICKABLE.create(level);
      orb.setAmount(amount);
      orb.setPos(loc);
      orb.setDeltaMovement(rng.nextDouble(-0.2, 0.2), rng.nextDouble(0.1, 0.2), rng.nextDouble(-0.2, 0.2));
      orb.setDelayUnpickable(delayUnpickable);
      EidosEntity.setLifetime(lifetime);
      level.addFreshEntity(orb);
   }

   static {
      HashMap<Direction, Integer> dirs = new HashMap<>();
      dirs.put(Direction.NORTH, 0);
      dirs.put(Direction.WEST, 90);
      dirs.put(Direction.SOUTH, 180);
      dirs.put(Direction.EAST, 270);
      DIRECTIONS = dirs;
   }
}
