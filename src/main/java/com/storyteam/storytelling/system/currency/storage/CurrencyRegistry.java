package com.storyteam.storytelling.system.currency.storage;

import com.storyteam.storytelling.system.currency.Currency;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRegistry {
   private static final List<Currency> currencies = new ArrayList<>();

   public static <T extends Currency> T register(T currency) {
      currencies.add(currency);
      return currency;
   }

   public static Currency get(String id) {
      return currencies.stream().filter(currency -> currency.getId().equals(id)).findFirst().orElse(null);
   }

   public static List<Currency> getAll() {
      return new ArrayList<>(currencies);
   }
}
