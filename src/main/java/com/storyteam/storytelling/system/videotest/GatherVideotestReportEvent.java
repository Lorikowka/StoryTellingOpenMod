package com.storyteam.storytelling.system.videotest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class GatherVideotestReportEvent extends PlayerEvent {
   private final SortedSet<GatherVideotestReportEvent.PrioritizedReport> set = new TreeSet<>(
      Comparator.comparingDouble(GatherVideotestReportEvent.PrioritizedReport::priority)
   );
   private final boolean expandedInfo;

   public GatherVideotestReportEvent(Player player) {
      super(player);
      this.expandedInfo = player.isShiftKeyDown();
   }

   public void reportInfo(double priority, String text) {
      this.reportInfo(priority, Component.literal(text));
   }

   public void reportInfo(double priority, Component text) {
      this.set.add(new GatherVideotestReportEvent.PrioritizedReport(priority, text));
   }

   public void reportCategory(double priority, String... components) {
      this.reportCategory(priority, Arrays.stream(components).map(Component::literal).toArray(Component[]::new));
   }

   public void reportCategory(double priority, Component... components) {
      if (!this.set.isEmpty()) {
         this.reportInfo(priority, Component.empty());
         priority = Math.nextUp(priority);
      }

      for (Component com : components) {
         this.reportInfo(priority, com);
         priority = Math.nextUp(priority);
      }
   }

   public Stream<Component> components() {
      return this.set.stream().map(GatherVideotestReportEvent.PrioritizedReport::text);
   }

   @Generated
   public boolean isExpandedInfo() {
      return this.expandedInfo;
   }

   private static record PrioritizedReport(double priority, Component text) {
   }
}
