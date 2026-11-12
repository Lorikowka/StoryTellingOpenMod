package com.storyteam.storytelling.util;

import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Map;

public class Duration {
   private static final Map<ChronoUnit, String[]> ENDINGS = new EnumMap<>(ChronoUnit.class);
   private static final ChronoUnit[] ORDER = new ChronoUnit[]{ChronoUnit.YEARS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS};
   private final EnumMap<ChronoUnit, Long> time = new EnumMap<>(ChronoUnit.class);

   public Duration(long seconds) {
      long s = seconds;
      long m = seconds / 60L;
      long h = m / 60L;
      long d = h / 24L;
      long y = d / 365L;
      if (m != 0L) {
         s = seconds % (m * 60L);
      }

      if (h != 0L) {
         m %= h * 60L;
      }

      if (d != 0L) {
         h %= d * 24L;
      }

      if (y != 0L) {
         d %= y * 365L;
      }

      this.time.put(ChronoUnit.SECONDS, s);
      this.time.put(ChronoUnit.MINUTES, m);
      this.time.put(ChronoUnit.HOURS, h);
      this.time.put(ChronoUnit.DAYS, d);
      this.time.put(ChronoUnit.YEARS, y);
   }

   public Long get(ChronoUnit unit) {
      if (!ENDINGS.containsKey(unit)) {
         throw new UnsupportedOperationException("Unsupported unit: " + unit.name());
      } else {
         return this.time.get(unit);
      }
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      boolean needsComma = false;

      for (int i = 0; i < ORDER.length; i++) {
         ChronoUnit unit = ORDER[i];
         Long val = this.get(unit);
         if (val != null && val != 0L) {
            if (needsComma) {
               builder.append(", ");
            }

            builder.append(val).append(" ");
            if (val % 100L >= 11L && val % 100L <= 19L) {
               builder.append(ENDINGS.get(unit)[2]);
            } else if (val != 1L && val % 10L != 1L) {
               if ((val < 2L || val > 4L) && val % 10L != 2L) {
                  builder.append(ENDINGS.get(unit)[2]);
               } else {
                  builder.append(ENDINGS.get(unit)[1]);
               }
            } else {
               builder.append(ENDINGS.get(unit)[0]);
            }

            needsComma = true;
         }
      }

      return builder.length() == 0 ? "0 " + ENDINGS.get(ChronoUnit.SECONDS)[2] : builder.toString();
   }

   static {
      ENDINGS.put(ChronoUnit.YEARS, new String[]{"год", "года", "лет"});
      ENDINGS.put(ChronoUnit.DAYS, new String[]{"день", "дня", "дней"});
      ENDINGS.put(ChronoUnit.HOURS, new String[]{"час", "часа", "часов"});
      ENDINGS.put(ChronoUnit.MINUTES, new String[]{"минута", "минуты", "минут"});
      ENDINGS.put(ChronoUnit.SECONDS, new String[]{"секунда", "секунды", "секунд"});
      if (ENDINGS.size() != ORDER.length) {
         throw new IllegalStateException("Should be same");
      }
   }
}
