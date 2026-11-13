package com.storyteam.storytelling.script.data;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IMob;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.AI;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.world.Location;
import com.storyteam.storytelling.util.world.MobNavigator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.Resources;

public class EntityController<T extends Entity> implements IAutoNBTSerializable {
   PriorityQueue<EntityState<T>> deq = new PriorityQueue<>();
   private EntityType<? extends T> type;
   private final String storyKey;
   @NBTSerializable("Bound")
   public UUID entityDataId;
   @NBTSerializable("Pos")
   private Location lastRegisteredPos;
   @NBTSerializable
   public boolean goals;
   @NBTSerializable
   public final MobNavigator navigator = new MobNavigator();
   private T reportedEntity;

   public EntityController(String storyKey, EntityType<T> type) {
      this.storyKey = storyKey;
      this.type = type;
   }

   public boolean update() {
      if (this.deq.isEmpty()) {
         return true;
      } else {
         EntityState<T> state = this.deq.peek();
         if (state.getEntityTypeOverride() != null) {
            this.type = state.getEntityTypeOverride();
         }

         if (!state.isValid() && this.reportedEntity == null) {
            return this.removeState();
         } else {
            T entity = this.getSpawnedEntity(state);
            if (entity == null) {
               return false;
            } else {
               return state.tick(entity, this) ? this.removeState() : false;
            }
         }
      }
   }

   private boolean removeState() {
      this.deq.poll();
      this.updateStateDeque(true);
      return this.deq.isEmpty();
   }

   @Nullable
   private T getSpawnedEntity(EntityState<T> state) {
      if (!state.isMarkedForReposition()) {
         return this.reportedEntity;
      } else {
         if (!this.getOrCreateNewEntity(state) && this.reportedEntity != null) {
            state.doReposition(this.reportedEntity);
            this.lastRegisteredPos = state.getPos();
         } else {
            state.markForReposition(false);
         }

         return this.reportedEntity;
      }
   }

   private boolean getOrCreateNewEntity(EntityState<T> state) {
      if (state.getPos() == null) {
         return false;
      } else {
         Location loc = state.getPos();
         ServerLevel level = IStoryServer.get().getOrLoadDimension(loc.getDimension());
         if (level == null) {
            return false;
         } else {
            IStoryServer server = IStoryServer.get();
            if ((this.reportedEntity == null || !this.reportedEntity.isAlive() || !level.isPositionEntityTicking(this.reportedEntity.blockPosition()))
               && this.createFreshEntity(level, loc) != null) {
               if (level.isPositionEntityTicking(this.reportedEntity.blockPosition())) {
                  server.postStoryEvent(
                     EventType.ENTITY_REPORTED, new EntityContext<>(this, this.getActiveState(), this.reportedEntity, Character.getById(this.storyKey))
                  );
               }

               if (state.getData().getShouldUpdateChunk().get()) {
                  server.getChunkLoader().entityStartTracking(level, this.storyKey, this.reportedEntity);
               } else {
                  server.getChunkLoader().entityStopTracking(level, this.storyKey);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void submitEntityState(EntityState<T> state) {
      if (!this.deq.contains(state)) {
         EntityState<T> previous = this.deq.peek();
         this.deq.add(state);
         boolean wasChanged = this.deq.peek() == state;
         if (wasChanged && previous != null && this.reportedEntity != null) {
            previous.onStop(new EntityContext<>(this, previous, this.reportedEntity, Character.getById(this.storyKey)));
         }

         this.updateStateDeque(wasChanged);
      }
   }

   private void updateStateDeque(boolean doRepos) {
      EntityState<T> state = this.deq.peek();
      if (state != null) {
         if (doRepos) {
            state.markForReposition();
            this.navigator.refresh();
            if (this.reportedEntity != null) {
               IStoryServer.get().postStoryEvent(EventType.ENTITY_REPORTED, new EntityContext<>(this, state, this.reportedEntity, state.getCharacter()));
            }
         }

         Iterator<EntityState<T>> iterator = this.deq.iterator();
         iterator.next();
         iterator.forEachRemaining(entry -> {
            if (entry.isValid()) {
               entry.markForWaiting();
            } else {
               iterator.remove();
            }
         });
         if (state.isValid()) {
            state.markAlive();
         }
      }
   }

   public T createFreshEntity(Level world, Location pos) {
      T e = (T)this.type.create(world);
      if (e == null) {
         Storytelling.LOG
            .warn(
               "Failed to create fresh entity of type "
                  + ForgeRegistries.ENTITY_TYPES.getKey(this.type)
                  + "! Something went horribly wrong. (EntityReference<"
                  + this.storyKey
                  + ">)"
            );
         return null;
      } else {
         CompoundTag nbt = new CompoundTag();
         nbt.putUUID("RefId", this.entityDataId = e.getUUID());
         nbt.putString("Id", this.storyKey);
         nbt.putBoolean("Vulnerable", false);
         e.getPersistentData().put("Storytelling", nbt);
         e.setPos(pos.x, pos.y, pos.z);
         if (e instanceof LivingEntity entity) {
            float f = pos.getPitch() * (float) (Math.PI / 180.0);
            float f1 = -pos.getYaw() * (float) (Math.PI / 180.0);
            float f2 = Mth.cos(f1);
            float f3 = Mth.sin(f1);
            float f4 = Mth.cos(f);
            float f5 = Mth.sin(f);
            Location vec = new Location((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
            Location target = pos.add(vec.scale(100.0));
            entity.lookAt(Anchor.EYES, target);
         }

         this.lastRegisteredPos = new Location(world.dimension(), pos.x, pos.y, pos.z, pos.getYaw(), pos.getPitch());
         if (!this.goals && e instanceof Mob) {
            ((IMob)e).clearGoalsIME();
         }

         if (this.reportedEntity != null) {
            this.reportedEntity.discard();
         }

         this.reportedEntity = e;
         world.addFreshEntity(e);
         Character<T> character = Character.getById(this.storyKey);
         if (character != null) {
            character.onCreatedFreshEntity(e);
         }

         return this.reportedEntity;
      }
   }

   public void reportEntity(Entity e) {
      if (e.getType() != this.type) {
         e.discard();
      } else if (!Objects.equals(this.entityDataId, e.getUUID())) {
         e.discard();
      } else {
         this.reportedEntity = (T)Cast.cast(e);
         this.lastRegisteredPos = new Location(e);
         if (!this.deq.isEmpty()) {
            EntityState<T> state = this.deq.peek();
            state.getData().exchangeData((T)Cast.cast(e), this);
         }

         if (e instanceof Mob mob) {
            mob.setPersistenceRequired();
            if (!this.goals) {
               AI.noGoals(mob);
            } else if (mob.goalSelector.getAvailableGoals().isEmpty()) {
               ((IMob)e).registerGoalsIME();
            }

            this.navigator.update(mob);
         }
      }
   }

   public <E> void handleEvent(EventType<E> type, E event) {
      EntityState<T> state = this.getActiveState();
      if (state != null && this.reportedEntity != null) {
         state.handleEvent(type, event, this.reportedEntity, this);
      }
   }

   @Nullable
   public EntityState<T> getActiveState() {
      return this.deq.isEmpty() ? null : this.deq.peek();
   }

   public boolean isStateActive(EntityState<?> state) {
      return this.deq.peek() == state;
   }

   @Nullable
   public T getEntity() {
      return this.reportedEntity;
   }

   @Nonnull
   public Optional<T> optionally() {
      return Optional.ofNullable(this.reportedEntity);
   }

   public CompoundTag serializeNBT() {
      if (this.reportedEntity != null && this.reportedEntity.isAlive()) {
         this.lastRegisteredPos = new Location(this.reportedEntity);
      }
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      nbt.putString("EntityType", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.type)).toString());
      return nbt;
   }

   public static EntityController<?> read(String name, CompoundTag nbt) {
      EntityType<?> type = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(Resources.location(nbt.getString("EntityType")));
      if (type == null) {
         return null;
      } else {
         EntityController<?> ref = new EntityController(name, type);
         ref.deserializeNBT(nbt);
         return ref;
      }
   }

   public boolean isOf(Character<?> character) {
      return character.getReferenceId().equals(this.getStoryKey());
   }

   public boolean isOf(Character<?>... character) {
      return Arrays.stream(character).anyMatch(e -> e.getReferenceId().equals(this.getStoryKey()));
   }

   public <E extends Entity> boolean isOf(Collection<Character<? extends E>> character) {
      return character.stream().anyMatch(e -> e.getReferenceId().equals(this.getStoryKey()));
   }

   @Generated
   public EntityType<? extends T> getType() {
      return this.type;
   }

   @Generated
   public String getStoryKey() {
      return this.storyKey;
   }

   @Generated
   public Location getLastRegisteredPos() {
      return this.lastRegisteredPos;
   }
}
