package com.storyteam.storytelling.script.performer.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.net.PacketSyncEntityPosition;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourManager;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import com.storyteam.storytelling.script.performer.entity.behaviour.utils.ISightBehaviour;
import com.storyteam.storytelling.script.performer.entity.look.targets.PositionTarget;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.clothes.Outfit;
import com.storyteam.storytelling.util.UtilST;
import com.storyteam.storytelling.util.data.EntityContext;
import com.storyteam.storytelling.util.data.IEntityInfoPack;
import com.storyteam.storytelling.util.world.Location;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammeranims.api.tile.IAnimatedEntity;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.Cast;

public class EntityState<T extends Entity> implements Comparable<EntityState<?>>, IAutoNBTSerializable, Cloneable {
   final ScriptInstance script;
   boolean markedForReposition;
   @NotNull
   EntityStatePhase phase;
   @Nullable
   private EntityType<? extends T> entityTypeOverride;
   protected final Character<T> character;
   private final BehaviourManager<T> manager = new BehaviourManager<>();
   @NBTSerializable
   final EntityStateData<T> data = new EntityStateData<>();

   public EntityState(ScriptInstance script, Character<T> character, EntityControlPriority priority) {
      this.script = script;
      this.character = character;
      this.data.setMoveSpeed(character.getStandardMoveSpeed());
      this.data.priority = priority;
      this.phase = EntityStatePhase.ALIVE;
      Outfit outfit = character.getOutfit();
      if (outfit != null) {
         this.data.setVanity(this.data.getVanity().apply(outfit));
      }
   }

   public void setEntityTypeOverride(@Nullable EntityType<? extends T> entityTypeOverride) {
      this.entityTypeOverride = entityTypeOverride;
   }

   public void markForReposition(boolean doRepos) {
      if (this.isMarkedForReposition() != doRepos) {
         this.markedForReposition = doRepos;
      }
   }

   public void markForReposition() {
      if (!this.isMarkedForReposition()) {
         this.markedForReposition = true;
      }
   }

   public void markForDelete() {
      EntityController<T> controller = this.script.env.server.getEntityRepository().get(this.character);
      if (controller != null && controller.isStateActive(this)) {
         this.phase = EntityStatePhase.MARKED_FOR_DELETE;
      } else {
         this.phase = EntityStatePhase.QUEUED_FOR_DELETE;
      }
   }

   public void markForWaiting() {
      if (this.phase.shouldNotDead()) {
         this.phase = EntityStatePhase.WAITING;
      }
   }

   public void markAlive() {
      this.phase = EntityStatePhase.ALIVE;
   }

   public void setPosition(Location pos, boolean setRotation) {
      if (setRotation && pos != null && pos.hasRotation()) {
         this.data.setLookTarget(new PositionTarget<>(new Location(Vec3.directionFromRotation(pos.getPitch(), pos.getYaw())), true));
      }

      this.data.setPos(pos);
   }

   public void setPosition(Location pos) {
      this.setPosition(pos, false);
   }

   public void interruptBehaviours(@NotNull IStoryBehaviour<T> interrupter) {
      this.manager.interruptBehaviours(interrupter);
   }

   public void continueBehaviours(@NotNull IStoryBehaviour<T> interrupter) {
      this.manager.continueBehaviours(interrupter);
   }

   public void stopBehaviour(BehaviourType type) {
      this.manager.stopBehaviour(type);
   }

   public void addBehaviour(@NotNull IStoryBehaviour<T> behaviour) {
      this.manager.addBehaviour(behaviour);
   }

   public void clearBehaviours() {
      this.manager.clearBehaviours();
   }

   public void setAlphaFadeable(float alphaFadeable) {
      this.data.setAlphaFadeable(alphaFadeable);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.alphaFadeable.setFloat(alphaFadeable);
         data.detectAndSendChanges();
      });
   }

   public void blockAnimRotateWalking(boolean blockAnimRotateWalking) {
      this.data.setBlockAnimRotateWalking(blockAnimRotateWalking);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.blockAnimRotateWalking.setBool(blockAnimRotateWalking);
         data.detectAndSendChanges();
      });
   }

   public void blockBody(boolean blockBody) {
      this.data.setBlockBody(blockBody);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.blockBody.setBool(blockBody);
         data.detectAndSendChanges();
      });
   }

   public void lockHead(boolean lockHead) {
      this.data.setLockHead(lockHead);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.lockHead.setBool(lockHead);
         data.detectAndSendChanges();
      });
   }

   public void ignoreNavmesh(boolean ignoreNavmesh) {
      this.data.setIgnoreNavmesh(ignoreNavmesh);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.ignoreNavmesh.setBool(ignoreNavmesh);
         data.detectAndSendChanges();
      });
   }

   public void traversalNavigation(boolean traversalNavigation) {
      this.data.setTraversalNavigation(traversalNavigation);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.traversalNavigation.setBool(traversalNavigation);
         data.detectAndSendChanges();
      });
   }

   private boolean isSightInterrupted(EntityController<T> controller) {
      return this.data.isFreeSight()
         && controller.navigator.currentJob != null
         && !controller.navigator.paused
         && !(this.getBehaviour(BehaviourType.MOVEMENT) instanceof ISightBehaviour);
   }

   public void freeSight(boolean freeSight) {
      this.data.setFreeSight(freeSight);
      this.optionalValidOnly().flatMap(StoryData::optionalOf).ifPresent(data -> {
         data.freeSight.setBool(freeSight);
         data.detectAndSendChanges();
      });
   }

   @Nullable
   public Location getPos() {
      return this.data.getPos();
   }

   @Nullable
   public IStoryBehaviour<T> getBehaviour(BehaviourType type) {
      return this.manager.getBehavior(type);
   }

   @NotNull
   public ImmutableMap<BehaviourType, IStoryBehaviour<T>> getBehaviours() {
      return this.manager.getBehaviours();
   }

   public boolean isValid() {
      return this.script.valid && this.phase.shouldNotDead();
   }

   @Deprecated
   public Optional<T> optionalValidOnly() {
      return this.isValid() && this.script.env.server.getEntityRepository().isStateActive(this) ? this.optional() : Optional.empty();
   }

   @Deprecated(
      forRemoval = true
   )
   public Optional<T> optional() {
      return this.script.env.server.getEntityRepository().optional(this.character);
   }

   public ConfiguredAnimation getAnimation(String layer) {
      return this.getData().getAnimations().get(layer);
   }

   public void setAnimation(String layer, ConfiguredAnimation animation) {
      ConfiguredAnimation lastAnimationData = animation;

      while (lastAnimationData.next != null) {
         lastAnimationData = lastAnimationData.next;
      }

      ConfiguredAnimation starter = new ConfiguredAnimation(lastAnimationData).transitionTime(0.0F);
      if (starter.animation.isNull()) {
         this.getData().getAnimations().remove(layer);
      } else {
         this.getData().getAnimations().put(layer, starter);
      }

      this.optionalValidOnly().ifPresent(e -> {
         if (e.isAlive()) {
            if (e instanceof AnimatedEntity animatedEntity) {
               if (Objects.equals(animation.getLocation(), DefaultsHA.NULL_ANIM.getLocation()) && DefaultsHA.NULL_ANIM.get().configure().same(animation)) {
                  animatedEntity.stopAnimation(layer);
               } else {
                  animatedEntity.startAnimation(layer, animation);
               }
            }
         }
      });
   }

   public boolean isRepoHost(EntityStatesRepository entityStatesRepository) {
      return this.script.getRepository() == entityStatesRepository;
   }

   public void doReposition(T entity) {
      Location loc = this.getPos();
      this.markedForReposition = false;
      if (loc != null) {
         Entity currentEntity = entity;

         do {
            if (loc.hasRotation()) {
               currentEntity.getLookAngle().xRot(loc.getPitch());
               currentEntity.getLookAngle().yRot(loc.getYaw());
            }

            currentEntity.teleportTo(loc.x, loc.y, loc.z);
            Network.sendToTracking(entity, new PacketSyncEntityPosition(entity, loc.x, loc.y, loc.z));
         } while ((currentEntity = currentEntity.getVehicle()) != null);
      }
   }

   public boolean tick(@NotNull T entity, EntityController<T> controller) {
      EntityContext<T> context = new EntityContext<>(controller, this, entity, this.character);
      if (this.phase == EntityStatePhase.MARKED_FOR_DELETE) {
         this.onStop(context);
         return true;
      } else {
         ServerLevel level = (ServerLevel)entity.level();
         this.data
            .getShouldUpdateChunk()
            .handeIfChanged(
               () -> this.script.env.server.getChunkLoader().entityStartTracking(level, this.character.getReferenceId(), entity),
               () -> this.script.env.server.getChunkLoader().entityStopTracking(level, this.character.getReferenceId())
            );
         if (this.script.isInvalid()) {
            this.markForDelete();
         }

         if (!this.phase.isAlive()) {
            return false;
         } else {
            if (controller != null) {
               controller.goals = this.data.isGoalsEnabled();
               if (this.getData().getLookTarget() != null && !this.isSightInterrupted(controller)) {
                  this.getData().getLookController().lookAtTarget(context);
               }

               this.manager.processBehaviourChanges(context);
               this.manager.tick(this.script.env.server, context);
            }

            return false;
         }
      }
   }

   public void onStop(EntityContext<T> context) {
      List<String> layers = Lists.newArrayList(this.getAnimationLayers());
      if (context.getEntity() instanceof AnimatedEntity animated) {
         for (String layer : layers) {
            animated.startAnimation(layer, Animations.NULL.configure());
         }
      }

      IStoryServer.get().getChunkLoader().entityStopTracking((ServerLevel)context.getEntity().level(), this.character.getReferenceId());
      this.manager.onStopBehaviours(this.script.env.server.getScriptEnv(), context);
   }

   public <E> void handleEvent(EventType<E> type, E event, @NotNull T entity, EntityController<T> controller) {
      EntityContext<T> context = new EntityContext<>(controller, this, entity, this.character);
      this.manager.handleEvent(type, event, context);
      if (type == EventType.ENTITY_REPORTED) {
         IEntityInfoPack<?> pack = (IEntityInfoPack<?>)event;
         if (pack.isOf(this.character)) {
            this.reportEntity((T)Cast.cast(pack.getEntity()), controller);
         }
      }
   }

   public void reportEntity(T entity, EntityController<? extends T> controller) {
      if (entity != null && entity.isAddedToWorld() && entity.isAlive()) {
         if (entity instanceof LivingEntity le) {
            le.setItemInHand(InteractionHand.MAIN_HAND, this.data.getMainHandItem());
            le.setItemInHand(InteractionHand.OFF_HAND, this.data.getOffHandItem());
            this.data.getVanity().equip(le);
         }

         if (this.data.getLookTarget() != null) {
            this.data.getLookController().lookAtInstantTarget(new EntityContext<>(controller, this, entity, this.character));
         }

         if (entity instanceof IAnimatedEntity animated) {
            AnimationSystem system = animated.getAnimationSystem();
            HashMap<String, ConfiguredAnimation> allAnims = this.getData().getAnimations();

            for (AnimationLayer anim : system.getLayers()) {
               if (!allAnims.containsKey(anim.name)) {
                  this.setAnimation(anim.name, ConfiguredAnimation.noAnimation());
               }
            }

            allAnims.forEach(system::startAnimationAt);
         }

         String customTexture = this.getData().getTextureOverride() == null ? null : this.getData().getTextureOverride().toString();
         IStoryEntity.of(entity).setCustomTexture(customTexture);
         if (entity instanceof LivingEntity) {
            StoryData data = StoryData.of(entity);
            data.blockBody.setBool(this.data.isBlockBody());
            data.lockHead.setBool(this.data.isLockHead());
            data.ignoreNavmesh.setBool(this.data.isIgnoreNavmesh());
            data.dontRotateOnInteraction.setBool(this.data.isDontRotateOnInteraction());
            data.blockAnimRotateWalking.setBool(this.data.isBlockAnimRotateWalking());
            data.traversalNavigation.setBool(this.data.isTraversalNavigation());
            data.detectAndSendChanges();
         }
      }
   }

   public Set<String> getAnimationLayers() {
      return Set.copyOf(this.getData().getAnimations().keySet());
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      CompoundTag animations = UtilST.serializeMap(this.getData().getAnimations());
      tag.put("Animations", animations);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      CompoundTag tag = nbt.getCompound("Animations");
      this.getData()
         .getAnimations()
         .putAll(tag.getAllKeys().stream().collect(Collectors.toMap(s -> (String)s, s -> new ConfiguredAnimation(tag.getCompound(s)))));
   }

   public int compareTo(@NotNull EntityState<?> o) {
      if (o.phase == EntityStatePhase.MARKED_FOR_DELETE == (this.phase == EntityStatePhase.MARKED_FOR_DELETE)) {
         return this.data.priority.compareTo(o.data.getPriority());
      } else {
         return this.phase == EntityStatePhase.MARKED_FOR_DELETE ? 1 : -1;
      }
   }

   public EntityState<T> clone() {
      try {
         return (EntityState<T>)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   @Generated
   public ScriptInstance getScript() {
      return this.script;
   }

   @Generated
   public boolean isMarkedForReposition() {
      return this.markedForReposition;
   }

   @Nullable
   @Generated
   public EntityType<? extends T> getEntityTypeOverride() {
      return this.entityTypeOverride;
   }

   @Generated
   public Character<T> getCharacter() {
      return this.character;
   }

   @Generated
   public EntityStateData<T> getData() {
      return this.data;
   }
}
