package com.storyteam.storytelling.system.tribute.storage.registry;

import com.storyteam.storytelling.system.tribute.data.medallion.MedallionStage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedallionStageRegistry {
   private static final List<MedallionStage> registered = new ArrayList<>();

   public static MedallionStage register(MedallionStage registryEntry) {
      registered.add(registryEntry);
      return registryEntry;
   }

   public static List<MedallionStage> getAll() {
      return new ArrayList<>(registered);
   }

   public static Optional<MedallionStage> get(String id) {
      return registered.stream().filter(medallionStage -> medallionStage.getIdPath().endsWith(id)).findFirst();
   }
}
