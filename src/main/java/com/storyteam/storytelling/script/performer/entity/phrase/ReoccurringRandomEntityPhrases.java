package com.storyteam.storytelling.script.performer.entity.phrase;

import java.util.Random;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class ReoccurringRandomEntityPhrases implements EntityPhrases {
   private final String[] phrases;
   @NBTSerializable
   private int index;

   public ReoccurringRandomEntityPhrases(String... phrases) {
      this.phrases = phrases;
   }

   @Override
   public String supplyNextPhrase() {
      String text;
      if (this.index < this.phrases.length) {
         text = this.phrases[this.index++];
      } else {
         text = this.phrases[new Random().nextInt(this.phrases.length)];
      }

      return text;
   }

   @Override
   public void reset() {
      this.index = 0;
   }
}
