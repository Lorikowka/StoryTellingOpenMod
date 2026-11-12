package com.storyteam.storytelling.system.ico;

public enum Ico {
   EIDOS("\ue100"),
   BODY("\ue101"),
   MIND("\ue102"),
   WILLPOWER("\ue103");

   private final String symbol;

   private Ico(String symbol) {
      this.symbol = symbol;
   }

   public String getSymbol() {
      return this.symbol;
   }
}
