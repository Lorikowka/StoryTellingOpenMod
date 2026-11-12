package com.storyteam.storytelling.system.simulator.cognition_tab;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public class CognitionHolder {
   public static final CognitionHolder INSTANCE = new CognitionHolder();
   private final Map<ResourceLocation, Cognition> cognitions = new HashMap<>();

   public Collection<Cognition> getCognitions() {
      return this.cognitions.values();
   }

   @Nonnull
   public Cognition getCognition(ResourceLocation location) {
      return this.cognitions.getOrDefault(location, Cognition.EMPTY);
   }

   public Cognition register(Cognition cognition) {
      this.cognitions.put(cognition.id(), cognition);
      return cognition;
   }
}
