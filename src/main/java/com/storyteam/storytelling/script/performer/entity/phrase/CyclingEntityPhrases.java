package com.storyteam.storytelling.script.performer.entity.phrase;

import org.zeith.hammerlib.api.io.NBTSerializable;

public class CyclingEntityPhrases implements EntityPhrases {
   private final String[] phrases;
   @NBTSerializable
   private int index;

   public CyclingEntityPhrases(String... phrases) {
      this.phrases = phrases;
   }

   @Override
   public String supplyNextPhrase() {
      return this.phrases[this.index++ % this.phrases.length];
   }

   @Override
   public void reset() {
      this.index = 0;
   }
}
