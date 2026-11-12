package com.storyteam.storytelling.script.story.characters.mobs;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.util.UtilST;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

public class SpecialCharacterList<T extends AnimatedEntity> extends ArrayList<SpecialCharacter<T>> {
   public SpecialCharacterList() {
   }

   public SpecialCharacterList(@NotNull Collection<? extends SpecialCharacter<T>> c) {
      super(c);
   }

   private SpecialCharacterList(SpecialCharacter<T>[] characters) {
      super(Arrays.asList(characters));
   }

   public Consumer<ScriptPerformerBuilder> toAll(TriConsumer<ScriptPerformerBuilder, SpecialCharacter<T>, Integer> builderConsumer) {
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

   public Consumer<ScriptPerformerBuilder> toAll(BiConsumer<ScriptPerformerBuilder, SpecialCharacter<T>> builderConsumer) {
      return b -> {
         ScriptPerformer[] performers = this.stream().map(c -> {
            ScriptPerformerBuilder builder = new ScriptPerformerBuilder(b.getPerformer().script);
            builderConsumer.accept(builder, (SpecialCharacter<T>)c);
            return builder.build();
         }).toArray(ScriptPerformer[]::new);
         b.thenAllPerformers(performers);
      };
   }

   @SafeVarargs
   public final SpecialCharacterList<T> concat(SpecialCharacterList<T>... toAdd) {
      SpecialCharacterList<T> list = new SpecialCharacterList<>();
      list.addAll(this);
      return UtilST.concat(list, toAdd);
   }

   public SpecialCharacterList<T> shuffle() {
      Collections.shuffle(this);
      return this;
   }

   public static Integer indexOf(SpecialCharacter<?> character) {
      String refId = character.getReferenceId();
      if (!refId.contains("_list_")) {
         return -1;
      } else {
         String[] parts = refId.split("_list_");
         return Integer.parseInt(parts[parts.length - 1]);
      }
   }

   @SafeVarargs
   public static <T extends AnimatedEntity> SpecialCharacterList<T> of(SpecialCharacter<T>... characters) {
      return new SpecialCharacterList<>(characters);
   }
}
