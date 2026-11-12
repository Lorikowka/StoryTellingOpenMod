package com.storyteam.storytelling.system.tribute.storage.registry;

import com.storyteam.storytelling.system.tribute.data.offer.TributeOffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TributeOfferRegistry {
   private static final List<TributeOffer> registered = new ArrayList<>();

   public static TributeOffer register(TributeOffer registryEntry) {
      registered.add(registryEntry);
      return registryEntry;
   }

   public static List<TributeOffer> getAll() {
      return new ArrayList<>(registered);
   }

   public static Optional<TributeOffer> get(String id) {
      return registered.stream().filter(tributeOffer -> tributeOffer.getIdPath().endsWith(id)).findFirst();
   }
}
