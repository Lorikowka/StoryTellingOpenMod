package com.storyteam.storytelling.script.performer.entity.phrase;

import java.util.function.Function;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;

public interface EntityPhrases extends IAutoNBTSerializable {
   String supplyNextPhrase();

   default void reset() {
   }

   public static enum Type {
      RANDOM(RandomEntityPhrases::new),
      CYCLING(CyclingEntityPhrases::new),
      STORYTELLING(ReoccurringRandomEntityPhrases::new);

      private final Function<String[], EntityPhrases> factory;

      private Type(Function<String[], EntityPhrases> factory) {
         this.factory = factory;
      }

      public EntityPhrases create(String... phrases) {
         return this.factory.apply(phrases);
      }
   }
}
