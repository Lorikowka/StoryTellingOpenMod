package com.storyteam.storytelling.util;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.TargetSelector;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetSelectorHelper {
   @Nullable
   public static <T extends AnimatedEntity, U extends Entity> U nearest(EntityContext<T> context, TargetSelector<T, U> selector) {
      List<U> list = selector.select(context).stream().filter(e -> e != context.getEntity() && e.isAlive()).toList();
      return list.isEmpty() ? null : list.stream().min(Comparator.comparingDouble(e -> (double)((AnimatedEntity)context.getEntity()).distanceTo(e))).get();
   }

   @NotNull
   public static <T extends AnimatedEntity, U extends Entity> List<U> inRange(EntityContext<T> context, TargetSelector<T, U> selector, double distance) {
      List<U> list = selector.select(context).stream().filter(e -> e != context.getEntity() && e.isAlive()).toList();
      return (List<U>)(list.isEmpty()
         ? new ArrayList<>()
         : list.stream().filter(e -> (double)e.distanceTo(context.getEntity()) <= distance).collect(Collectors.toList()));
   }

   @NotNull
   public static <T extends AnimatedEntity, U extends Entity> List<U> between(EntityContext<T> context, TargetSelector<T, U> selector, double max, double min) {
      List<U> list = selector.select(context).stream().filter(e -> e != context.getEntity() && e.isAlive()).toList();
      return (List<U>)(list.isEmpty()
         ? new ArrayList<>()
         : list.stream()
            .filter(e -> (double)e.distanceTo(context.getEntity()) <= max && (double)e.distanceTo(context.getEntity()) >= min)
            .collect(Collectors.toList()));
   }

   public static <T extends AnimatedEntity, U extends Entity> boolean hasInRange(EntityContext<T> context, TargetSelector<T, U> selector, double distance) {
      return !inRange(context, selector, distance).isEmpty();
   }

   public static <T extends AnimatedEntity, U extends Entity> boolean doesntHaveInRange(
      EntityContext<T> context, TargetSelector<T, U> selector, double distance
   ) {
      return inRange(context, selector, distance).isEmpty();
   }

   public static <T extends AnimatedEntity, U extends Entity> boolean hasBetween(
      EntityContext<T> context, TargetSelector<T, U> selector, double max, double min
   ) {
      return !between(context, selector, max, min).isEmpty();
   }
}
