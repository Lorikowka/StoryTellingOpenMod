package com.storyteam.storytelling.system.simulator.me_tab.fragment.data;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DescribedCondition {
   private final Predicate<ServerPlayer> condition;
   private final Component description;

   public DescribedCondition(Predicate<ServerPlayer> condition, Component description) {
      this.condition = condition;
      this.description = description;
   }

   public DescribedCondition(Predicate<ServerPlayer> condition, String description) {
      this(condition, Component.literal(description));
   }

   public boolean test(ServerPlayer player) {
      return this.condition.test(player);
   }

   public static DescribedCondition level(int lvl) {
      return new DescribedCondition(p -> PlayerStats.getLevel() >= lvl, "Требуется уровень: " + lvl);
   }

   @Generated
   public Predicate<ServerPlayer> getCondition() {
      return this.condition;
   }

   @Generated
   public Component getDescription() {
      return this.description;
   }
}
