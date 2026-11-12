package com.storyteam.storytelling.script.story.characters.mobs;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.system.fight.mob_drop.MobDropData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.mcf.Resources;

public class SpecialCharactersFactory<T extends AnimatedEntity> {
   private static final Random RANDOM = new Random();
   private final HashMap<EntityType<T>, List<ResourceLocation>> textureBinds;
   private final HashMap<EntityType<T>, Consumer<T>> spawnRules;
   private Supplier<Outfit> outfit;
   private List<MobDropData> mobDropList = new ArrayList<>();
   @Nullable
   private final String defaultName;

   public Stream<SpecialCharacter<T>> stream(String refId) {
      return this.stream(refId, new Random());
   }

   public Stream<SpecialCharacter<T>> stream(String refId, Random rng) {
      AtomicInteger i = new AtomicInteger();
      return Stream.generate(() -> this.createSpecialCharacter(refId + i.getAndIncrement(), rng));
   }

   public Stream<SpecialCharacter<T>> stream(String name, String refId) {
      return this.stream(name, refId, new Random());
   }

   public Stream<SpecialCharacter<T>> stream(String name, String refId, Random rng) {
      AtomicInteger i = new AtomicInteger();
      return Stream.generate(() -> this.createSpecialCharacter(refId + i.getAndIncrement(), name, rng));
   }

   public Stream<SpecialCharacter<T>> stream(String name, String refId, EntityType<T> type) {
      AtomicInteger i = new AtomicInteger();
      return Stream.generate(() -> this.createSpecialCharacter(refId + i.getAndIncrement(), name, type));
   }

   public Stream<SpecialCharacter<T>> stream(String name, String refId, EntityType<T> type, Consumer<T> onCreate) {
      AtomicInteger i = new AtomicInteger();
      return Stream.generate(() -> this.createSpecialCharacter(refId + i.getAndIncrement(), name, type, onCreate));
   }

   private SpecialCharactersFactory(
      HashMap<EntityType<T>, List<ResourceLocation>> textureBinds,
      HashMap<EntityType<T>, Consumer<T>> spawnRules,
      @Nullable String defaultName,
      @NotNull Supplier<Outfit> outfit,
      List<MobDropData> mobDropList
   ) {
      this.textureBinds = textureBinds;
      this.spawnRules = spawnRules;
      this.defaultName = defaultName;
      this.outfit = outfit;
      this.mobDropList = mobDropList;
   }

   public static <U extends AnimatedEntity> SpecialCharactersFactory.SpecialCharactersFactoryBuilder<U> builder() {
      return new SpecialCharactersFactory.SpecialCharactersFactoryBuilder<>();
   }

   public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> derive() {
      SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> builder = new SpecialCharactersFactory.SpecialCharactersFactoryBuilder<>();
      this.textureBinds.forEach((k, v) -> builder.setTexturesForType((EntityType<T>)k, v.toArray(ResourceLocation[]::new)));
      this.spawnRules.forEach(builder::addRuleOnFreshCreate);
      return builder.setDefaultName(this.defaultName).setOutfit(this.outfit).setMobDrop(this.mobDropList);
   }

   private EntityType<T> getRandomType(Random rng) {
      Set<EntityType<T>> types = this.textureBinds.keySet();
      int currIdx = 0;
      int randIdx = rng.nextInt(types.size());

      for (EntityType<T> type : types) {
         if (currIdx == randIdx) {
            return type;
         }

         currIdx++;
      }

      return null;
   }

   public SpecialCharactersFactory<T> merge(SpecialCharactersFactory<T> factory) {
      HashMap<EntityType<T>, List<ResourceLocation>> mergedTextureBinds = new HashMap<>();
      HashMap<EntityType<T>, Consumer<T>> mergedSpawnRules = new HashMap<>();

      for (EntityType<T> key : this.textureBinds.keySet()) {
         mergedTextureBinds.put(key, this.textureBinds.get(key));
         factory.textureBinds.forEach((type, textures) -> {
            if (mergedTextureBinds.containsKey(type)) {
               mergedTextureBinds.get(type).addAll(textures);
            } else {
               mergedTextureBinds.put((EntityType<T>)type, (List<ResourceLocation>)textures);
            }
         });
      }

      for (EntityType<T> key : this.spawnRules.keySet()) {
         mergedSpawnRules.put(key, this.spawnRules.get(key));
         factory.spawnRules.forEach((type, action) -> {
            if (mergedSpawnRules.containsKey(type)) {
               mergedSpawnRules.put((EntityType<T>)type, e -> {
                  mergedSpawnRules.get(type).accept(e);
                  action.accept(e);
               });
            } else {
               mergedSpawnRules.put((EntityType<T>)type, (Consumer<T>)action);
            }
         });
      }

      return new SpecialCharactersFactory<>(
         mergedTextureBinds, mergedSpawnRules, this.defaultName, this.outfit == null ? factory.outfit : this.outfit, this.mobDropList
      );
   }

   public SpecialCharacterList<T> createSpecialCharacterList(String refId, int size) {
      return this.createSpecialCharacterList(refId, size, new Random());
   }

   public SpecialCharacterList<T> createSpecialCharacterList(String refId, int size, String name) {
      return this.createSpecialCharacterList(refId, size, new Random(), name);
   }

   public SpecialCharacterList<T> createSpecialCharacterList(String refId, int size, Random rng) {
      ArrayList<SpecialCharacter<T>> list = new ArrayList<>();

      for (int i = 0; i < size; i++) {
         list.add(this.createSpecialCharacter(refId + "_list_" + i, rng));
      }

      return new SpecialCharacterList<>(list);
   }

   public SpecialCharacterList<T> createSpecialCharacterList(String refId, int size, String seed, boolean randomize) {
      ArrayList<SpecialCharacter<T>> list = new ArrayList<>();

      for (int i = 0; i < size; i++) {
         list.add(this.createSpecialCharacter(refId + "_list_" + i, randomize ? new Random((long)(seed + i).hashCode()) : new Random((long)seed.hashCode())));
      }

      return new SpecialCharacterList<>(list);
   }

   public SpecialCharacterList<T> createSpecialCharacterList(String refId, int size, Random rng, String name) {
      ArrayList<SpecialCharacter<T>> list = new ArrayList<>();

      for (int i = 0; i < size; i++) {
         list.add(this.createSpecialCharacter(refId + "_list_" + i, name, rng));
      }

      return new SpecialCharacterList<>(list);
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId) {
      return this.createSpecialCharacter(refId, new Random());
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, Random rng) {
      EntityType<T> type = this.getRandomType(rng);
      if (this.defaultName == null) {
         Storytelling.LOG.error(String.format("Default name is missed while creating character using %s.", this.getClass().getSimpleName()));
      }

      return this.createSpecialCharacter(refId, this.defaultName, type, rng);
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, String name) {
      return this.createSpecialCharacter(refId, name, new Random());
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, String name, Random rng) {
      EntityType<T> type = this.getRandomType(rng);
      return this.createSpecialCharacter(refId, name, type);
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, String name, EntityType<T> type) {
      return this.createSpecialCharacter(refId, name, type, RANDOM);
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, String name, EntityType<T> type, Random rng) {
      List<ResourceLocation> textures = this.textureBinds.get(type);
      return this.spawnRules.containsKey(type)
         ? this.createSpecialCharacter(refId, name, type, this.spawnRules.get(type))
         : SpecialCharacter.specialCharacterBuilder(refId, (Supplier<EntityType<T>>)(() -> type))
            .textured(textures.get(rng.nextInt(textures.size())))
            .name(name)
            .outfit(this.outfit.get())
            .mobDropList(this.mobDropList)
            .build();
   }

   public SpecialCharacter<T> createSpecialCharacter(String refId, String name, EntityType<T> type, Consumer<T> onCreate) {
      List<ResourceLocation> textures = this.textureBinds.get(type);
      return SpecialCharacter.specialCharacterBuilder(refId, (Supplier<EntityType<T>>)(() -> type))
         .textured(textures.get(RANDOM.nextInt(textures.size())))
         .name(name)
         .onCreatedFresh(onCreate)
         .outfit(this.outfit.get())
         .mobDropList(this.mobDropList)
         .build();
   }

   public static class SpecialCharactersFactoryBuilder<T extends AnimatedEntity> {
      protected final HashMap<EntityType<T>, List<ResourceLocation>> textureBinds = new HashMap<>();
      protected final HashMap<EntityType<T>, Consumer<T>> spawnRules = new HashMap<>();
      protected String defaultName;
      protected Supplier<Outfit> outfit = () -> Outfit.builder().build();
      protected List<MobDropData> mobDropList = new ArrayList<>();
      private static final String PREFIX = "textures/entity/";

      protected SpecialCharactersFactoryBuilder() {
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> setDefaultName(String name) {
         this.defaultName = name;
         return this;
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> setTexturesForType(EntityType<T> type, String... textures) {
         this.textureBinds
            .put(
               type,
               Arrays.stream(textures)
                  .map(
                     texture -> !texture.startsWith("storytelling") && !texture.startsWith("minecraft:")
                           ? (texture.startsWith("textures/entity/") ? St.id(texture) : St.id("textures/entity/" + texture))
                           : Resources.location(texture)
                  )
                  .toList()
            );
         return this;
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> setOutfit(Supplier<Outfit> outfit) {
         this.outfit = outfit;
         return this;
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> setTexturesForType(EntityType<T> type, ResourceLocation... textures) {
         this.textureBinds.put(type, Arrays.asList(textures));
         return this;
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> addRuleOnFreshCreate(EntityType<T> type, Consumer<T> onCreate) {
         this.spawnRules.put(type, onCreate);
         return this;
      }

      public SpecialCharactersFactory.SpecialCharactersFactoryBuilder<T> setMobDrop(List<MobDropData> mobDropList) {
         this.mobDropList = mobDropList;
         return this;
      }

      public SpecialCharactersFactory<T> build() {
         return new SpecialCharactersFactory<>(this.textureBinds, this.spawnRules, this.defaultName, this.outfit, this.mobDropList);
      }
   }
}
