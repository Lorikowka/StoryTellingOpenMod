package com.storyteam.storytelling.script.story.characters;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
import com.storyteam.storytelling.init.EntitiesST;
import com.storyteam.storytelling.system.clothes.Outfit;
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
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class Characters implements CharactersST8 {
   public static final String PLAYER = "player";
   public static Character<HumanoidEntity> ZEITHERON = Character.builder("zeitheron", Cast.constant(EntitiesST.ZEITHERON)).name("Zeith").build();
   public static Character<HumanoidEntity> ARCHMAGE = Character.builder("archmage_sword", Cast.constant(EntitiesST.ARCHMAGE)).name("ArchmageEternal").build();
   public static Character<HumanoidEntity> EDIT = Character.builder("edit", Cast.constant(EntitiesST.EDIT)).name("Edit").build();

   public static final class CharactersFactory<T extends AnimatedEntity> {
      private static final Random RANDOM = new Random();
      private final HashMap<EntityType<T>, List<ResourceLocation>> textureBinds;
      private final HashMap<EntityType<T>, Consumer<T>> spawnRules;
      private Supplier<Outfit> outfit;
      @Nullable
      private final String defaultName;

      public Stream<Character<T>> stream(String refId) {
         return this.stream(refId, new Random());
      }

      public Stream<Character<T>> stream(String refId, Random rng) {
         AtomicInteger i = new AtomicInteger();
         return Stream.generate(() -> this.createCharacter(refId + i.getAndIncrement(), rng));
      }

      public Stream<Character<T>> stream(String name, String refId) {
         return this.stream(name, refId, new Random());
      }

      public Stream<Character<T>> stream(String name, String refId, Random rng) {
         AtomicInteger i = new AtomicInteger();
         return Stream.generate(() -> this.createCharacter(refId + i.getAndIncrement(), name, rng));
      }

      public Stream<Character<T>> stream(String name, String refId, EntityType<T> type) {
         AtomicInteger i = new AtomicInteger();
         return Stream.generate(() -> this.createCharacter(refId + i.getAndIncrement(), name, type));
      }

      public Stream<Character<T>> stream(String name, String refId, EntityType<T> type, Consumer<T> onCreate) {
         AtomicInteger i = new AtomicInteger();
         return Stream.generate(() -> this.createCharacter(refId + i.getAndIncrement(), name, type, onCreate));
      }

      private CharactersFactory(
         HashMap<EntityType<T>, List<ResourceLocation>> textureBinds,
         HashMap<EntityType<T>, Consumer<T>> spawnRules,
         @Nullable String defaultName,
         @NotNull Supplier<Outfit> outfit
      ) {
         this.textureBinds = textureBinds;
         this.spawnRules = spawnRules;
         this.defaultName = defaultName;
         this.outfit = outfit;
      }

      public static <U extends AnimatedEntity> Characters.CharactersFactory.CharactersFactoryBuilder<U> builder() {
         return new Characters.CharactersFactory.CharactersFactoryBuilder<>();
      }

      public Characters.CharactersFactory.CharactersFactoryBuilder<T> derive() {
         Characters.CharactersFactory.CharactersFactoryBuilder<T> builder = new Characters.CharactersFactory.CharactersFactoryBuilder<>();
         this.textureBinds.forEach((k, v) -> builder.setTexturesForType((EntityType<T>)k, v.toArray(ResourceLocation[]::new)));
         this.spawnRules.forEach(builder::addRuleOnFreshCreate);
         return builder.setDefaultName(this.defaultName).setOutfit(this.outfit);
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

      public Characters.CharactersFactory<T> merge(Characters.CharactersFactory<T> factory) {
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

         return new Characters.CharactersFactory<>(mergedTextureBinds, mergedSpawnRules, this.defaultName, this.outfit == null ? factory.outfit : this.outfit);
      }

      public CharacterList<T> createCharacterList(String refId, int size) {
         return this.createCharacterList(refId, size, new Random());
      }

      public CharacterList<T> createCharacterList(String refId, int size, String name) {
         return this.createCharacterList(refId, size, new Random(), name);
      }

      public CharacterList<T> createCharacterList(String refId, int size, Random rng) {
         ArrayList<Character<T>> list = new ArrayList<>();

         for (int i = 0; i < size; i++) {
            list.add(this.createCharacter(refId + "_list_" + i, rng));
         }

         return new CharacterList<>(list);
      }

      public CharacterList<T> createCharacterList(String refId, int size, String seed, boolean randomize) {
         ArrayList<Character<T>> list = new ArrayList<>();

         for (int i = 0; i < size; i++) {
            list.add(this.createCharacter(refId + "_list_" + i, randomize ? new Random((long)(seed + i).hashCode()) : new Random((long)seed.hashCode())));
         }

         return new CharacterList<>(list);
      }

      public CharacterList<T> createCharacterList(String refId, int size, Random rng, String name) {
         ArrayList<Character<T>> list = new ArrayList<>();

         for (int i = 0; i < size; i++) {
            list.add(this.createCharacter(refId + "_list_" + i, name, rng));
         }

         return new CharacterList<>(list);
      }

      public Character<T> createCharacter(String refId) {
         return this.createCharacter(refId, new Random());
      }

      public Character<T> createCharacter(String refId, Random rng) {
         EntityType<T> type = this.getRandomType(rng);
         if (this.defaultName == null) {
            Storytelling.LOG.error(String.format("Default name is missed while creating character using %s.", this.getClass().getSimpleName()));
         }

         return this.createCharacter(refId, this.defaultName, type, rng);
      }

      public Character<T> createCharacter(String refId, String name) {
         return this.createCharacter(refId, name, new Random());
      }

      public Character<T> createCharacter(String refId, String name, Random rng) {
         EntityType<T> type = this.getRandomType(rng);
         return this.createCharacter(refId, name, type);
      }

      public Character<T> createCharacter(String refId, String name, EntityType<T> type) {
         return this.createCharacter(refId, name, type, RANDOM);
      }

      public Character<T> createCharacter(String refId, String name, EntityType<T> type, Random rng) {
         List<ResourceLocation> textures = this.textureBinds.get(type);
         return this.spawnRules.containsKey(type)
            ? this.createCharacter(refId, name, type, this.spawnRules.get(type))
            : Character.builder(refId, (Supplier<EntityType<T>>)(() -> type))
               .textured(textures.get(rng.nextInt(textures.size())))
               .name(name)
               .outfit(this.outfit.get())
               .build();
      }

      public Character<T> createCharacter(String refId, String name, EntityType<T> type, Consumer<T> onCreate) {
         List<ResourceLocation> textures = this.textureBinds.get(type);
         return Character.builder(refId, (Supplier<EntityType<T>>)(() -> type))
            .textured(textures.get(RANDOM.nextInt(textures.size())))
            .name(name)
            .onCreatedFresh(onCreate)
            .outfit(this.outfit.get())
            .build();
      }

      public static class CharactersFactoryBuilder<T extends AnimatedEntity> {
         protected final HashMap<EntityType<T>, List<ResourceLocation>> textureBinds = new HashMap<>();
         protected final HashMap<EntityType<T>, Consumer<T>> spawnRules = new HashMap<>();
         protected String defaultName;
         protected Supplier<Outfit> outfit = () -> Outfit.builder().build();
         private static final String PREFIX = "textures/entity/";

         protected CharactersFactoryBuilder() {
         }

         public Characters.CharactersFactory.CharactersFactoryBuilder<T> setDefaultName(String name) {
            this.defaultName = name;
            return this;
         }

         public Characters.CharactersFactory.CharactersFactoryBuilder<T> setTexturesForType(EntityType<T> type, String... textures) {
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

         public Characters.CharactersFactory.CharactersFactoryBuilder<T> setOutfit(Supplier<Outfit> outfit) {
            this.outfit = outfit;
            return this;
         }

         public Characters.CharactersFactory.CharactersFactoryBuilder<T> setTexturesForType(EntityType<T> type, ResourceLocation... textures) {
            this.textureBinds.put(type, new ArrayList<>(List.of(textures)));
            return this;
         }

         public Characters.CharactersFactory.CharactersFactoryBuilder<T> addRuleOnFreshCreate(EntityType<T> type, Consumer<T> onCreate) {
            this.spawnRules.put(type, onCreate);
            return this;
         }

         public Characters.CharactersFactory<T> build() {
            return new Characters.CharactersFactory<>(this.textureBinds, this.spawnRules, this.defaultName, this.outfit);
         }
      }
   }
}
