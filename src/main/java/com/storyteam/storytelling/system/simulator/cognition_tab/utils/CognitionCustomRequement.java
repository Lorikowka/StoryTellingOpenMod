package com.storyteam.storytelling.system.simulator.cognition_tab.utils;

import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;

public class CognitionCustomRequement {
   private int id;
   private final String desc;
   private final Predicate<ServerPlayer> predicate;

   public CognitionCustomRequement(String desc, Predicate<ServerPlayer> predicate) {
      this.desc = desc;
      this.predicate = predicate;
   }

   public CognitionCustomRequement id(int id) {
      this.id = id;
      return this;
   }

   public int getId() {
      return this.id;
   }

   public String getDescription() {
      return this.desc;
   }

   public boolean test(ServerPlayer player) {
      return this.predicate.test(player);
   }
}
