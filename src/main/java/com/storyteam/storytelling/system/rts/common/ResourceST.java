package com.storyteam.storytelling.system.rts.common;

import lombok.Generated;

public enum ResourceST {
   RICE('\ue000'),
   ETHER('\ue001'),
   VERSIFORM('\ue002'),
   EIDOS('\ue009'),
   POPULATION('\ue003'),
   TIME('\ue004');

   private final char iconChar;

   private ResourceST(char iconChar) {
      this.iconChar = iconChar;
   }

   @Generated
   public char getIconChar() {
      return this.iconChar;
   }
}
