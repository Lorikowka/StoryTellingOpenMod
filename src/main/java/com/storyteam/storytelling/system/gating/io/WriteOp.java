package com.storyteam.storytelling.system.gating.io;

import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public record WriteOp(Consumer<Consumer<Set<ResourceLocation>>> KNOWN_ENTRIES, Consumer<Consumer<Set<ResourceLocation>>> UNLOCKED_ENTRIES) {
   public WriteOp modifyKnownEntries(Consumer<Set<ResourceLocation>> op) {
      this.KNOWN_ENTRIES.accept(op);
      return this;
   }

   public WriteOp modifyUnlockedEntries(Consumer<Set<ResourceLocation>> op) {
      this.UNLOCKED_ENTRIES.accept(op);
      return this;
   }
}
