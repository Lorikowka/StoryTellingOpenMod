package com.storyteam.storytelling.script.performer.entity;

import com.google.common.collect.Maps;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

public class EntityStatesRepository implements IAutoNBTSerializable {
   private final HashMap<Character<?>, EntityState<?>> entities = Maps.newHashMap();
   private final ScriptInstance instance;
   EntityControlPriority priority;

   public EntityStatesRepository(ScriptInstance instance, EntityControlPriority priority) {
      this.instance = instance;
      this.priority = priority;
   }

   public <T extends Entity> Optional<EntityState<T>> optional(@Nonnull Character<T> character) {
      return Optional.ofNullable((EntityState<T>)Cast.cast(this.entities.get(character)));
   }

   @Nonnull
   public <T extends Entity> EntityState<T> getOrCreate(@Nonnull Character<T> character) {
      return this.getOrCreate(character, false);
   }

   @Nonnull
   public <T extends Entity> EntityState<T> getOrCreate(@Nonnull Character<T> character, boolean loadToRepo) {
      return (EntityState<T>)Cast.cast(
         this.entities
            .compute(
               character,
               (c, prev) -> {
                  if (prev == null) {
                     prev = new EntityState<>(this.instance, character, this.priority);
                  }

                  if (loadToRepo) {
                     prev.markAlive();
                     this.instance.env.server.getEntityRepository().submitEntityState(character, (EntityState<T>)Cast.cast(prev));
                  }

                  if (prev.character != character) {
                     throw new IllegalArgumentException(
                        "Character \""
                           + character
                           + "\" has same referenceId as character \""
                           + prev.character
                           + "\". This can cause inconsistency in performers work. Please, use different characters with different referenceId's."
                     );
                  } else {
                     return prev;
                  }
               }
            )
      );
   }

   @Nullable
   public <T extends Entity> EntityState<T> get(@Nonnull Character<T> character) {
      return (EntityState<T>)Cast.cast(this.entities.get(character));
   }

   @Nullable
   public <T extends Entity> EntityState<T> get(@Nonnull String character) {
      return (EntityState<T>)Cast.cast(this.entities.get(Character.getById(character)));
   }

   public HashMap<Character<?>, EntityState<?>> getEntities() {
      return this.entities;
   }

   @Nullable
   public <T extends Entity> EntityState<T> removeEntity(@Nonnull Character<T> character, boolean removeFromRepo) {
      IStoryServer.get().postStoryEvent(EventType.CHARACTER_FREE, character);
      EntityState<T> state = removeFromRepo ? (EntityState)Cast.cast(this.entities.remove(character)) : (EntityState)Cast.cast(this.entities.get(character));
      if (state != null && state.isRepoHost(this)) {
         state.markForDelete();
      }

      return state;
   }

   @Nullable
   public <T extends Entity> EntityState<T> getActiveState(@Nonnull Character<T> character) {
      EntityController<T> controller = this.instance.env.server.getEntityRepository().get(character);
      return controller == null ? null : controller.getActiveState();
   }

   public void removeAll() {
      for (Entry<Character<?>, EntityState<?>> entry : new HashSet<>(this.entities.entrySet())) {
         EntityState<?> state = this.entities.remove(entry.getKey());
         if (state != null) {
            state.markForDelete();
         }
      }
   }

   public CompoundTag serializeNBT() {
      return IAutoNBTSerializable.super.serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
   }

   @Generated
   public EntityControlPriority getPriority() {
      return this.priority;
   }

   @Generated
   public void setPriority(EntityControlPriority priority) {
      this.priority = priority;
   }
}
