package com.storyteam.storytelling.script.story.characters;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.util.UtilST;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

public class CharacterList<T extends AnimatedEntity> extends ArrayList<Character<T>> {
   public CharacterList() {
   }

   public CharacterList(@NotNull Collection<? extends Character<T>> c) {
      super(c);
   }

   private CharacterList(Character<T>[] characters) {
      super(Arrays.asList(characters));
   }

   public Consumer<ScriptPerformerBuilder> toAll(TriConsumer<ScriptPerformerBuilder, Character<T>, Integer> builderConsumer) {
      return builder -> {
         AtomicInteger i = new AtomicInteger();
         ScriptPerformer[] performers = this.stream().map(c -> {
            ScriptPerformerBuilder builder1 = new ScriptPerformerBuilder(builder.getPerformer().script);
            builderConsumer.accept(builder1, c, i.getAndIncrement());
            return builder1.build();
         }).toArray(ScriptPerformer[]::new);
         builder.thenAllPerformers(performers);
      };
   }

   public Consumer<ScriptPerformerBuilder> toAll(BiConsumer<ScriptPerformerBuilder, Character<T>> builderConsumer) {
      return b -> {
         ScriptPerformer[] performers = this.stream().map(c -> {
            ScriptPerformerBuilder builder = new ScriptPerformerBuilder(b.getPerformer().script);
            builderConsumer.accept(builder, (Character<T>)c);
            return builder.build();
         }).toArray(ScriptPerformer[]::new);
         b.thenAllPerformers(performers);
      };
   }

   public CharacterList<T> splitFirstHalf() {
      int mid = this.size() / 2;
      return new CharacterList<>(this.subList(0, mid));
   }

   public CharacterList<T> slice(int fromIndex, int toIndex) {
      if (fromIndex >= 0 && toIndex <= this.size() && fromIndex <= toIndex) {
         return new CharacterList<>(this.subList(fromIndex, toIndex));
      } else {
         throw new IndexOutOfBoundsException("Invalid slice range: " + fromIndex + " to " + toIndex);
      }
   }

   @SafeVarargs
   public final CharacterList<T> concat(CharacterList<T>... toAdd) {
      CharacterList<T> list = new CharacterList<>();
      list.addAll(this);
      return UtilST.concat(list, toAdd);
   }

   public CharacterList<T> shuffle() {
      Collections.shuffle(this);
      return this;
   }

   public CharacterList<T> shuffle(Random rng) {
      Collections.shuffle(this, rng);
      return this;
   }

   public CharacterList<T> shuffle(int seed) {
      Random random = new Random((long)seed);

      for (int i = this.size() - 1; i > 0; i--) {
         int j = random.nextInt(i + 1);
         Collections.swap(this, i, j);
      }

      return this;
   }

   public static Integer indexOf(Character<?> character) {
      String refId = character.getReferenceId();
      if (!refId.contains("_list_")) {
         return -1;
      } else {
         String[] parts = refId.split("_list_");
         return Integer.parseInt(parts[parts.length - 1]);
      }
   }

   @SafeVarargs
   public static <T extends AnimatedEntity> CharacterList<T> of(Character<T>... characters) {
      return new CharacterList<>(characters);
   }
}
