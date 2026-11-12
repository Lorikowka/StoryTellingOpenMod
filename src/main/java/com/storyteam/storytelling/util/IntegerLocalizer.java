package com.storyteam.storytelling.util;

public class IntegerLocalizer {
   public static String localize(int amount, String... values) {
      int f;
      if (amount > 99) {
         String s = String.valueOf(amount);
         f = Integer.parseInt(s.subSequence(s.length() - 2, s.length()).toString());
      } else {
         f = amount;
      }

      if (f > 20) {
         f %= 10;
      }

      return f == 1 ? values[0] : (f >= 2 && f < 5 ? values[1] : values[2]);
   }
}
