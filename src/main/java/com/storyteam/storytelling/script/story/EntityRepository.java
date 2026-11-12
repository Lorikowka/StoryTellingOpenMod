package com.storyteam.storytelling.script.story;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;

public class EntityRepository implements IAutoNBTSerializable {
   public final IStoryServer server;
   private final Map<String, EntityController<?>> controllerMap = new ConcurrentHashMap<>();
   @NBTSerializable
   public long ticks;

   public EntityRepository(IStoryServer server) {
      this.server = server;
   }

   @Nonnull
   public <T extends Entity> EntityController<T> create(String id, EntityType<T> type) {
      EntityController<T> ref = new EntityController<>(id, type);
      this.controllerMap.put(id, ref);
      return ref;
   }

   @Nonnull
   public <T extends Entity> EntityController<T> create(Character<T> character) {
      return this.create(character.getReferenceId(), character.getType());
   }

   @Nullable
   public EntityController<?> get(String id) {
      return id == null ? null : this.controllerMap.get(id);
   }

   @Nullable
   public <T extends Entity> T getEntity(Character<T> character) {
      if (character == null) {
         return null;
      } else {
         EntityController<T> e = (EntityController<T>)Cast.cast(this.get(character.getReferenceId()));
         return e == null ? null : e.getEntity();
      }
   }

   @Nullable
   public <T extends Entity> EntityController<T> get(Character<T> character) {
      return character == null ? null : (EntityController)Cast.cast(this.get(character.getReferenceId()));
   }

   public <T extends Entity> boolean isSpawned(Character<T> character) {
      EntityController<T> e = this.get(character);
      return e != null && e.getEntity() != null;
   }

   @Nonnull
   public <T extends Entity> EntityController<T> getOrCreate(String id, EntityType<T> type) {
      EntityController<?> reference = this.controllerMap.get(id);
      if (reference != null && reference.getType() != type) {
         this.delete(id);
         reference = null;
      }

      if (reference == null) {
         reference = this.create(id, type);
      }

      return (EntityController<T>)Cast.cast(reference);
   }

   @Nonnull
   public <T extends Entity> EntityController<T> getOrCreate(Character<T> character) {
      return this.getOrCreate(character.getReferenceId(), character.getType());
   }

   public <T extends Entity> Optional<T> optional(String id) {
      return (Optional<T>)Cast.cast(Optional.ofNullable(this.controllerMap.get(id)).flatMap(EntityController::optionally));
   }

   public <T extends Entity> Optional<T> optional(Character<T> character) {
      return this.optional(character.getReferenceId());
   }

   public <T extends Entity> void submitEntityState(Character<T> character, EntityState<T> state) {
      this.getOrCreate(character).submitEntityState(state);
   }

   public <T extends Entity> boolean isStateActive(EntityState<T> state) {
      return Optional.ofNullable(this.controllerMap.get(state.getCharacter().getReferenceId())).map(c -> c.isStateActive(state)).orElse(false);
   }

   public void reportStoryEntity(Entity entity, CompoundTag storyData) {
      EntityController<?> id = this.controllerMap.get(storyData.getString("Id"));
      if (id != null) {
         id.reportEntity(entity);
      } else {
         entity.discard();
      }
   }

   public <E> boolean postCancellableEventToControllers(EventType<E> type, E event, Predicate<E> breakCondition) {
      for (EntityController<?> controller : this.controllerMap.values()) {
         controller.handleEvent(type, event);
         if (breakCondition.test(event)) {
            return true;
         }
      }

      return false;
   }

   public <E> void postEventToControllers(EventType<E> type, E event) {
      this.controllerMap.values().forEach(c -> c.handleEvent(type, event));
   }

   public void tick() {
      this.ticks++;
      Iterator<Entry<String, EntityController<?>>> iterator = this.controllerMap.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<String, EntityController<?>> entry = iterator.next();
         EntityController<?> controller = entry.getValue();
         boolean delete = controller.update();
         if (delete) {
            iterator.remove();
            this.server.postStoryEvent(EventType.ENTITY_REMOVED, controller);
         }
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      CompoundTag refs = new CompoundTag();

      for (Entry<String, EntityController<?>> entry : this.controllerMap.entrySet()) {
         refs.put(entry.getKey(), entry.getValue().serializeNBT());
      }

      nbt.put("Entities", refs);
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      CompoundTag refs = nbt.getCompound("Entities");
      this.controllerMap.clear();

      for (String name : refs.getAllKeys()) {
         EntityController<?> r = EntityController.read(name, refs.getCompound(name));
         if (r != null) {
            this.controllerMap.put(name, r);
         } else {
            Storytelling.LOG.fatal("Failed to read entity reference " + name + " with data: " + refs.getCompound(name));
         }
      }

      Iterator<Entry<String, EntityController<?>>> iterator = this.controllerMap.entrySet().iterator();
      ArrayList<String> toRemove = new ArrayList<>();

      while (iterator.hasNext()) {
         Entry<String, EntityController<?>> entry = iterator.next();
         String namex = entry.getKey();
         String cutName;
         if (namex.startsWith("bg_")) {
            cutName = namex.substring("bg_".length());
         } else {
            if (!namex.startsWith("nbg_")) {
               continue;
            }

            cutName = namex.substring("nbg_".length());
         }

         System.out.println(namex + " -> " + cutName);
         toRemove.add(cutName);
      }

      toRemove.forEach(this.controllerMap::remove);
   }

   public void delete(String id) {
      EntityController<?> entity = this.get(id);
      if (entity != null) {
         entity.optionally().ifPresent(Entity::discard);
         this.controllerMap.remove(id);
      }
   }

   public void delete(Character<?> character) {
      this.delete(character.getReferenceId());
   }

   public Stream<EntityController<?>> streamReferences() {
      return this.controllerMap.values().stream();
   }
}
