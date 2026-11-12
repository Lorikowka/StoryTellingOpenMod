package com.storyteam.storytelling.system.simulator.cognition_tab.utils;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;

public record CognitionTierInfo(
   String description,
   Map<AttributeType, Integer> types,
   int level,
   int eidos,
   Set<CognitionCustomRequement> customReques,
   Set<Consumer<ServerPlayer>> onUpgrade
) {
   public static CognitionTierInfo.Builder builder() {
      return new CognitionTierInfo.Builder();
   }

   public static class Builder {
      private String desctiption = "";
      private final Map<AttributeType, Integer> types = new HashMap<>();
      private int level = 0;
      private int eidos = 0;
      private final Set<CognitionCustomRequement> customReques = new HashSet<>();
      private final Set<Consumer<ServerPlayer>> onUpgrade = new HashSet<>();

      private Builder() {
      }

      public CognitionTierInfo.Builder desctiption(String desctiption) {
         this.desctiption = desctiption;
         return this;
      }

      public CognitionTierInfo.Builder type(AttributeType type, int amount) {
         this.types.put(type, amount);
         return this;
      }

      public CognitionTierInfo.Builder level(int level) {
         this.level = level;
         return this;
      }

      public CognitionTierInfo.Builder eidos(int eidos) {
         this.eidos = eidos;
         return this;
      }

      public CognitionTierInfo.Builder requirement(String desc, Predicate<ServerPlayer> predicate) {
         CognitionCustomRequement reque = new CognitionCustomRequement(desc, predicate);
         this.customReques.add(reque.id(CustomUpgradeHolder.getUpgradeIndex()));
         return this;
      }

      public CognitionTierInfo.Builder onUpgrade(Consumer<ServerPlayer> action) {
         this.onUpgrade.add(action);
         return this;
      }

      public CognitionTierInfo build() {
         return new CognitionTierInfo(this.desctiption, this.types, this.level, this.eidos, this.customReques, this.onUpgrade);
      }
   }
}
