package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.ai.CustomWalkNodeEvalutorHuman;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.content.entities.api.CustomMoveControl;
import com.storyteam.storytelling.content.entities.configurations.AnimConfigurationWrapper;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.ILivingAnimationEntity;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.init.EntityDataSerializersST;
import com.storyteam.storytelling.net.PacketSyncForgeData;
import com.storyteam.storytelling.net.core.PacketAnimatedLeadEntity;
import com.storyteam.storytelling.script.data.EntityController;
import com.storyteam.storytelling.script.performer.entity.EntityState;
import com.storyteam.storytelling.script.performer.entity.behaviour.BehaviourType;
import com.storyteam.storytelling.script.performer.entity.behaviour.movement.ISpeedyBehavior;
import com.storyteam.storytelling.system.clothes.entity.VanityInvDataSerializer;
import com.storyteam.storytelling.system.clothes.entity.VanityInventory;
import com.storyteam.storytelling.util.LayersList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammeranims.api.animation.IAnimationSource;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.AnimationSystem.Builder;
import org.zeith.hammeranims.core.init.DefaultsHA;
import org.zeith.hammerlib.net.Network;

public class AnimatedEntity extends PathfinderMob implements ILivingAnimationEntity {
   private static final EntityDataAccessor<Vec3> DATA_PASSENGER_OFFSET = SynchedEntityData.defineId(AnimatedEntity.class, EntityDataSerializersST.VEC3);
   private static final EntityDataAccessor<Boolean> ANIMATIONS_PAUSED = SynchedEntityData.defineId(AnimatedEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<VanityInventory> DATA_VANITY = SynchedEntityData.defineId(AnimatedEntity.class, VanityInvDataSerializer.INSTANCE);
   protected int animationPauseTime;
   protected final AnimationSystem animationSystem = AnimationSystem.create(this);
   protected AnimConfigurationWrapper wrapper;
   public ResourceLocation textureOverride;
   public boolean canDie;
   public static final String LAYER_WALKING = "ambient";
   public static final String LAYER_ACTION = "action";
   public boolean rotateModel180 = false;
   public boolean allowRotate = false;
   public boolean canOpenDoors = false;
   public Set<GlobalPos> openedDoors = new HashSet<>();
   public boolean onLead;
   public Vec3 connectPos;
   private static final EntityDataAccessor<Float> ALPHA_FADEABLE = SynchedEntityData.defineId(AnimatedEntity.class, EntityDataSerializers.FLOAT);
   private static final UUID MAX_HEALTH_UUID = UUID.randomUUID();

   public float getAlphaFadeable() {
      return (Float)this.entityData.get(ALPHA_FADEABLE);
   }

   public void setAlphaFadeable(float alpha_fadeable) {
      this.entityData.set(ALPHA_FADEABLE, alpha_fadeable);
   }

   public AnimatedEntity(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
      this.wrapper = new AnimConfigurationWrapper(null);
      this.moveControl = new CustomMoveControl(this);
      this.setMaxUpStep(1.125F);
   }

   public static AnimatedEntity create(EntityType<? extends AnimatedEntity> type, Level world, @Nullable IDefaultAnimConfiguration configuration) {
      AnimatedEntity animated = new AnimatedEntity(type, world);
      animated.wrapper = new AnimConfigurationWrapper(configuration);
      return animated;
   }

   public boolean isPaused() {
      return (Boolean)this.entityData.get(ANIMATIONS_PAUSED);
   }

   public void pauseFor(int ticks) {
      this.animationPauseTime = ticks;
   }

   @NotNull
   protected BodyRotationControl createBodyControl() {
      return new RestrictiveBodyController(this);
   }

   public void tick() {
      super.tick();
      if (!this.isPaused()) {
         this.animationSystem.tick();
      }

      if (!this.level().isClientSide) {
         if (this.wrapper != null) {
            this.wrapper.setup(this, 0.1);
         }

         this.entityData.set(ANIMATIONS_PAUSED, this.animationPauseTime > 0);
         if (this.animationPauseTime > 0) {
            this.animationPauseTime--;
         }

         if (StoryData.of(this) != null
            && StoryData.of(this).traversalNavigation.getBoolean()
            && this.getNavigation().getPath() != null
            && !this.getNavigation().getPath().isDone()) {
            Path path = this.getNavigation().getPath();
            Node node = path.getNextNode();
            BlockPos pos = new BlockPos(node.asBlockPos());
            if (this.isEntityNearby(pos)) {
               this.getNavigation().recomputePath();
            }
         }
      }
   }

   private boolean isEntityNearby(BlockPos position) {
      List<LivingEntity> livingEntityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(position)).stream().filter(e -> e != this).toList();
      return !livingEntityList.isEmpty();
   }

   public float getWalkTargetValue(@NotNull BlockPos pPos, @NotNull LevelReader pLevel) {
      float value = super.getWalkTargetValue(pPos, pLevel);
      if (pLevel.getBlockState(pPos).getBlock() instanceof IPlantable) {
         value -= 2.0F;
      }

      if (pLevel.getBlockState(pPos).getFluidState().isEmpty()) {
         value += 3.0F;
      }

      return value;
   }

   public VanityInventory getInventory() {
      return (VanityInventory)this.entityData.get(DATA_VANITY);
   }

   public void setInventory(VanityInventory inventory) {
      this.entityData.set(DATA_VANITY, inventory);
   }

   public boolean canOpenDoors() {
      return this.canOpenDoors;
   }

   public void setVulnerable(boolean vulnerable) {
      this.getPersistentData().getCompound("Storytelling").putBoolean("Vulnerable", vulnerable);
      Network.sendToTracking(this, new PacketSyncForgeData(this.getId(), this.getPersistentData()));
      Storytelling.LOG
         .info(
            "Vulnerable "
               + this.getPersistentData().getCompound("Storytelling").getBoolean("Vulnerable")
               + " id: "
               + this.getPersistentData().getCompound("Storytelling").getString("Id")
         );
   }

   public boolean isInvulnerableTo(@NotNull DamageSource source) {
      IStoryEntity se = IStoryEntity.of(this);
      if (this.canDie) {
         return false;
      } else if (source == this.level().damageSources().fellOutOfWorld() || source == this.damageSources().genericKill()) {
         return false;
      } else {
         return !se.isSEVulnerable()
            ? true
            : this.getHealth() <= 1.0F || source == this.level().damageSources().fall() || source == this.level().damageSources().inWall();
      }
   }

   protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
      if (!this.isInvulnerableTo(pDamageSource)) {
         pDamageAmount = ForgeHooks.onLivingHurt(this, pDamageSource, pDamageAmount);
         if (!(pDamageAmount <= 0.0F)) {
            pDamageAmount = this.getDamageAfterArmorAbsorb(pDamageSource, pDamageAmount);
            pDamageAmount = this.getDamageAfterMagicAbsorb(pDamageSource, pDamageAmount);
            float f2 = Math.max(pDamageAmount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (pDamageAmount - f2));
            float f = pDamageAmount - f2;
            if (f > 0.0F && f < 3.4028235E37F && pDamageSource.getEntity() instanceof ServerPlayer) {
               ((ServerPlayer)pDamageSource.getEntity()).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_DEALT_ABSORBED), Math.round(f * 10.0F));
            }

            f2 = ForgeHooks.onLivingDamage(this, pDamageSource, f2);
            if (f2 != 0.0F) {
               float f1 = this.getHealth();
               this.getCombatTracker().recordDamage(pDamageSource, f2);
               this.setHealth(
                  pDamageSource != this.level().damageSources().fellOutOfWorld() && pDamageSource != this.damageSources().genericKill() && !this.canDie
                     ? Math.max(1.0F, f1 - f2)
                     : f1 - f2
               );
               this.setAbsorptionAmount(this.getAbsorptionAmount() - f2);
               this.gameEvent(GameEvent.ENTITY_DAMAGE);
            }
         }
      }
   }

   public void setupSystem(Builder builder) {
      LayersList list = new LayersList();
      list.addLast("action", BlendMode.OVERRIDE, 1.0F);
      list.addLast("ambient", BlendMode.ADD, 1.0F);
      list.addLast("ROTATE_WALKING", BlendMode.ADD, 1.0F);

      for (int i = 0; i < 21; i++) {
         list.addLast("ANIMATION_" + i, BlendMode.ADD, 1.0F);
      }

      this.addLayers(list);
      builder.addLayers(list.getLayers().toArray(org.zeith.hammeranims.api.animsys.layer.AnimationLayer.Builder[]::new));
      builder.autoSync(true);
   }

   public ConfiguredAnimation getIdleAnimation() {
      return Animations.HUMANOID_IDLE.configure();
   }

   public SidedAnimationSet getRotateWalkingAnimation() {
      return Animations.ROTATE_WALKING;
   }

   public void addLayers(LayersList layers) {
   }

   public void startAnimation(String layer, ConfiguredAnimation cfg) {
      this.animationSystem.startAnimationAt(layer, cfg);
   }

   public void stopAnimation(String layer, float transitionTime) {
      this.startAnimation(layer, DefaultsHA.NULL_ANIM.configure().transitionTime(transitionTime));
   }

   public final void startAnimation(String layer, IAnimationSource animation) {
      this.startAnimation(layer, animation.configure().transitionTime(0.3F));
   }

   public final void stopAnimation(String layer) {
      this.stopAnimation(layer, 0.4F);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_PASSENGER_OFFSET, Vec3.ZERO);
      this.entityData.define(ANIMATIONS_PAUSED, false);
      this.entityData.define(DATA_VANITY, new VanityInventory());
      this.entityData.define(ALPHA_FADEABLE, 0.0F);
   }

   public void setPassengerOffset(double x, double y, double z) {
      this.setPassengerOffset(new Vec3(x, y, z));
   }

   public void setPassengerOffset(Vec3 vec) {
      this.entityData.set(DATA_PASSENGER_OFFSET, vec);
   }

   public Vec3 getPassengerOffset() {
      return (Vec3)this.entityData.get(DATA_PASSENGER_OFFSET);
   }

   protected void customServerAiStep() {
      super.customServerAiStep();
      this.goalSelector.tick();
      this.setYRot(this.getYHeadRot());
   }

   public void positionRider(@NotNull Entity pPassenger, MoveFunction pCallback) {
      super.positionRider(pPassenger, pCallback);
      Vec3 offset = this.getPassengerOffset();
      Vec3 pos = new Vec3(this.getX(), this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset(), this.getZ()).add(offset);
      Vec3 e = pos.subtract(this.position().multiply(1.0, 0.0, 1.0));
      Vec3 finalPos = this.position().add(e.yRot((float)Math.toRadians((double)(-this.yBodyRot))));
      pPassenger.setPos(finalPos.x, pos.y, finalPos.z);
      pPassenger.setYRot(this.getYRot());
      pPassenger.setXRot(this.getXRot());
   }

   public void checkDespawn() {
      this.noActionTime = 0;
   }

   @Override
   public float getWalkingSpeed() {
      double moved = new Vec3(this.xo, this.yo, this.zo).distanceTo(this.position());
      boolean posChanged = Math.abs(this.position().x - this.xo) >= 0.00390625 || Math.abs(this.position().z - this.zo) >= 0.00390625;
      if (posChanged) {
         EntityController<?> ref = IStoryServer.get().getEntityRepository().get(IStoryEntity.of(this).getStoryName());
         if (ref != null) {
            double speed = Optional.ofNullable(ref.getActiveState())
               .map(EntityState::getBehaviours)
               .filter(b -> b.get(BehaviourType.MOVEMENT) instanceof ISpeedyBehavior)
               .map(b -> (ISpeedyBehavior)b.get(BehaviourType.MOVEMENT))
               .map(ISpeedyBehavior::getSpeed)
               .orElse(moved);
            if (ref.navigator.currentJob != null && !ref.navigator.paused) {
               speed = (double)ref.navigator.currentJob.followSpeed;
            }

            return (float)speed;
         }
      }

      return 0.0F;
   }

   public void setMaxHealth(float maxHealth) {
      String name = "ST Max Health";
      AttributeInstance instance = this.getAttributes().getInstance(Attributes.MAX_HEALTH);
      instance.removeModifier(MAX_HEALTH_UUID);
      float before = this.getMaxHealth();
      instance.addPermanentModifier(new AttributeModifier(MAX_HEALTH_UUID, "ST Max Health", (double)(maxHealth - before), Operation.ADDITION));
      this.setHealth(maxHealth);
      this.heal(maxHealth);
   }

   public void addMaxHealth(String modifierName, int health) {
      this.getAttributes().getInstance(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(modifierName, (double)health, Operation.ADDITION));
      this.setHealth(this.getHealth());
   }

   public AnimationSystem getAnimationSystem() {
      return this.animationSystem;
   }

   public void readAdditionalSaveData(CompoundTag pCompound) {
      this.animationSystem.deserializeNBT(pCompound.getCompound("Animations"));
      this.animationPauseTime = pCompound.getInt("animationPauseTime");
      this.setAlphaFadeable((float)pCompound.getInt("alpha_fadeable"));
      super.readAdditionalSaveData(pCompound);
   }

   public void addAdditionalSaveData(CompoundTag pCompound) {
      pCompound.put("Animations", this.animationSystem.serializeNBT());
      pCompound.putInt("animationPauseTime", this.animationPauseTime);
      pCompound.putFloat("alpha_fadeable", this.getAlphaFadeable());
      super.addAdditionalSaveData(pCompound);
   }

   @NotNull
   @Override
   public String getIdleLayer() {
      return "ambient";
   }

   @Override
   public void setAnimationOverrideAll(@Nullable ConfiguredAnimation animation) {
      this.wrapper.setOverrideAll(animation);
   }

   @Override
   public void setAnimationOverride(@NotNull MovementActionType movementType, @Nullable ConfiguredAnimation animation) {
      this.wrapper.setOverride(movementType, animation);
   }

   @Nullable
   @Override
   public ConfiguredAnimation getAnimationOverride() {
      return this.wrapper.getOverrideAll();
   }

   public boolean alwaysAccepts() {
      return super.alwaysAccepts();
   }

   public void setConnectPos(Vec3 connectPos) {
      this.connectPos = connectPos;
      this.onLead = true;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketAnimatedLeadEntity(this.getId(), this.onLead, connectPos));
      }
   }

   public void removeConnect() {
      this.onLead = false;
      this.connectPos = Vec3.ZERO;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketAnimatedLeadEntity(this.getId(), this.onLead, this.connectPos));
      }
   }

   @NotNull
   protected PathNavigation createNavigation(@NotNull Level pLevel) {
      return new HumanoidEntity.HumanoidNavigation(this, pLevel);
   }

   @Generated
   public void setWrapper(AnimConfigurationWrapper wrapper) {
      this.wrapper = wrapper;
   }

   @Generated
   public AnimConfigurationWrapper getWrapper() {
      return this.wrapper;
   }

   @Generated
   public ResourceLocation getTextureOverride() {
      return this.textureOverride;
   }

   @Generated
   public void setTextureOverride(ResourceLocation textureOverride) {
      this.textureOverride = textureOverride;
   }

   static class HumanoidNavigation extends GroundPathNavigation {
      public HumanoidNavigation(Mob p_33379_, Level p_33380_) {
         super(p_33379_, p_33380_);
      }

      @NotNull
      protected PathFinder createPathFinder(int p_33382_) {
         this.nodeEvaluator = new CustomWalkNodeEvalutorHuman();
         return new PathFinder(this.nodeEvaluator, p_33382_);
      }

      @Nullable
      public Path createPath(BlockPos pPos, int pRegionOffset, int pAccuracy) {
         return super.createPath(pPos, pRegionOffset, pAccuracy);
      }
   }
}
