package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.mobs.BaseMob;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.script.story.characters.mobs.SpecialCharacter;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.data.EntityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.zeith.hammerlib.util.java.Cast;

public interface TargetSelector<T extends AnimatedEntity, U extends Entity> {
   List<U> select(EntityContext<T> var1);

   default TargetSelector<T, U> filter(Predicate<U> entity) {
      return ctx -> this.select(ctx).stream().filter(entity).toList();
   }

   default TargetSelector<T, U> or(TargetSelector<T, U> other) {
      return ctx -> {
         List<U> all = new ArrayList<>(this.select(ctx));
         all.addAll(other.select(ctx));
         return all;
      };
   }

   static <T extends AnimatedEntity> TargetSelector<T, Player> playerSelector() {
      return a -> List.of(ScriptUtils.getPlayer());
   }

   static <T extends AnimatedEntity> TargetSelector<T, Player> playerNoCarSelector() {
      ArrayList<Player> list = new ArrayList<>();
      if (ScriptUtils.getPlayer() != null && !ScriptUtils.getPlayer().isPassenger()) {
         list.add(ScriptUtils.getPlayer());
      }

      return a -> list;
   }

   static <T extends AnimatedEntity, E extends Entity> TargetSelector<T, E> charSelector(Character<E> character) {
      return a -> (List<E>)Cast.cast(
            ((AnimatedEntity)a.getEntity())
               .level()
               .getEntities(
                  a.getEntity(), new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0), character::is
               )
         );
   }

   static <T extends AnimatedEntity, E extends Entity> TargetSelector<T, Entity> charSelector(List<Character<E>> character) {
      return a -> ((AnimatedEntity)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
               e -> character.stream().anyMatch(c -> c.is(e))
            );
   }

   static <T extends AnimatedEntity, E extends LivingEntity> TargetSelector<T, LivingEntity> charSelectorLiveWithPlayer(List<? extends Character<E>> character) {
      return a -> {
         ArrayList<LivingEntity> list = new ArrayList<>();
         if (((AnimatedEntity)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
               e -> character.stream().anyMatch(c -> c.is(e))
            )
            .stream()
            .map(e -> (LivingEntity)e)
            .toList()
            .isEmpty()) {
            list.add(ScriptUtils.getPlayer());
            return list;
         } else {
            list = new ArrayList<>(
               ((AnimatedEntity)a.getEntity())
                  .level()
                  .getEntities(
                     a.getEntity(),
                     new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
                     e -> character.stream().anyMatch(c -> c.is(e))
                  )
                  .stream()
                  .map(e -> (LivingEntity)e)
                  .toList()
            );
            list.add(ScriptUtils.getPlayer());
            return list;
         }
      };
   }

   static <T extends BaseMob, E extends LivingEntity> TargetSelector<T, LivingEntity> charSelectorBaseMobWithPlayer(List<? extends Character<E>> character) {
      return a -> {
         ArrayList<LivingEntity> list = new ArrayList<>();
         if (((BaseMob)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((BaseMob)a.getEntity()).position(), ((BaseMob)a.getEntity()).position()).inflate(30.0),
               e -> character.stream().anyMatch(c -> c.is(e))
            )
            .stream()
            .map(e -> (LivingEntity)e)
            .toList()
            .isEmpty()) {
            list.add(ScriptUtils.getPlayer());
            return list;
         } else {
            list = new ArrayList<>(
               ((BaseMob)a.getEntity())
                  .level()
                  .getEntities(
                     a.getEntity(),
                     new AABB(((BaseMob)a.getEntity()).position(), ((BaseMob)a.getEntity()).position()).inflate(30.0),
                     e -> character.stream().anyMatch(c -> c.is(e))
                  )
                  .stream()
                  .map(e -> (LivingEntity)e)
                  .toList()
            );
            list.add(ScriptUtils.getPlayer());
            return list;
         }
      };
   }

   static <T extends AnimatedEntity, E extends LivingEntity> TargetSelector<T, LivingEntity> charSelectorLive(List<? extends Character<E>> character) {
      return a -> {
         ArrayList<LivingEntity> list = new ArrayList<>();
         return ((AnimatedEntity)a.getEntity())
               .level()
               .getEntities(
                  a.getEntity(),
                  new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
                  e -> character.stream().anyMatch(c -> c.is(e))
               )
               .stream()
               .map(e -> (LivingEntity)e)
               .toList()
               .isEmpty()
            ? list
            : new ArrayList<>(
               ((AnimatedEntity)a.getEntity())
                  .level()
                  .getEntities(
                     a.getEntity(),
                     new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
                     e -> character.stream().anyMatch(c -> c.is(e))
                  )
                  .stream()
                  .map(e -> (LivingEntity)e)
                  .toList()
            );
      };
   }

   static <T extends BaseMob, E extends LivingEntity> TargetSelector<T, LivingEntity> charSelectorBaseMob(List<? extends Character<E>> character) {
      return a -> new ArrayList<>(
            ((BaseMob)a.getEntity())
               .level()
               .getEntities(
                  a.getEntity(),
                  new AABB(((BaseMob)a.getEntity()).position(), ((BaseMob)a.getEntity()).position()).inflate(50.0),
                  e -> character.stream().anyMatch(c -> c.is(e))
               )
               .stream()
               .map(e -> (LivingEntity)e)
               .toList()
         );
   }

   static <T extends AnimatedEntity, E extends LivingEntity> TargetSelector<T, LivingEntity> spCharSelectorLive(List<SpecialCharacter<E>> character) {
      return a -> ((AnimatedEntity)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(30.0),
               e -> character.stream().anyMatch(c -> c.is(e))
            )
            .stream()
            .map(e -> (LivingEntity)e)
            .toList();
   }

   static <T extends AnimatedEntity, E extends LivingEntity> TargetSelector<T, LivingEntity> spCharSelectorLive(
      List<SpecialCharacter<E>> character, int distance
   ) {
      return a -> ((AnimatedEntity)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate((double)distance),
               e -> character.stream().anyMatch(c -> c.is(e))
            )
            .stream()
            .map(e -> (LivingEntity)e)
            .toList();
   }

   static <T extends AnimatedEntity, E extends LivingEntity> TargetSelector<T, LivingEntity> spCharSelectorLive_1(List<SpecialCharacter<E>> character) {
      return a -> ((AnimatedEntity)a.getEntity())
            .level()
            .getEntities(
               a.getEntity(),
               new AABB(((AnimatedEntity)a.getEntity()).position(), ((AnimatedEntity)a.getEntity()).position()).inflate(5.0),
               e -> character.stream().anyMatch(c -> c.is(e))
            )
            .stream()
            .map(e -> (LivingEntity)e)
            .toList();
   }
}
