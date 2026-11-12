package com.storyteam.storytelling.system.containertier.storage;

import com.storyteam.storytelling.system.containertier.data.ContainerTier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public class ContainerTierStorage {
   private static final Map<String, ContainerTier> CONTAINER_TIERS = new HashMap<>();

   public static void addTier(ContainerTier tier, ResourceLocation id) {
      CONTAINER_TIERS.put(id.getPath(), tier);
   }

   public static ContainerTier getTier(String id) {
      return CONTAINER_TIERS.get(id);
   }

   public static Set<String> getTiersKeys() {
      return CONTAINER_TIERS.keySet();
   }
}
