package com.storyteam.storytelling.script.performer.entity.phrase;

import java.util.Random;

public class RandomEntityPhrases implements EntityPhrases {
   private final String[] phrases;

   public RandomEntityPhrases(String... phrases) {
      this.phrases = phrases;
   }

   @Override
   public String supplyNextPhrase() {
      return this.phrases[new Random().nextInt(this.phrases.length)];
   }
}
