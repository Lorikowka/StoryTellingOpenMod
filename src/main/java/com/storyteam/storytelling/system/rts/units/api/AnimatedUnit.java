package com.storyteam.storytelling.system.rts.units.api;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.Checkpoint;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.goals.UsePortalGoal;
import com.solegendary.reignofnether.unit.interfaces.IPortraitRendererModifier;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.Faction;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.IDefaultAnimConfiguration;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.goals.MoveToTargetBlockGoalSt;
import com.storyteam.storytelling.system.rts.init.RTSAttributesST;
import com.storyteam.storytelling.util.LayersList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public abstract class AnimatedUnit extends AnimatedEntity implements Unit, IPortraitRendererModifier {
   protected final List<AbilityButton> abilityButtons = new ArrayList<>();
   protected final List<Ability> abilities = new ArrayList<>();
   protected final List<ItemStack> items = new ArrayList<>();
   protected final ArrayList<Checkpoint> checkpoints = new ArrayList<>();
   private BlockPos anchorPos = new BlockPos(0, 0, 0);
   private int eatingTicksLeft = 0;
   private LivingEntity followTarget = null;
   private boolean holdPosition = false;
   GarrisonGoal garrisonGoal;
   UsePortalGoal usePortalGoal;
   private MoveToTargetBlockGoal moveGoal;
   private SelectedTargetGoal<? extends LivingEntity> targetGoal;
   private ReturnResourcesGoal returnResourcesGoal;
   public static final EntityDataAccessor<String> ownerDataAccessor = SynchedEntityData.defineId(AnimatedUnit.class, EntityDataSerializers.STRING);

   public AnimatedUnit(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
      this.canDie = true;
   }

   public Faction getFaction() {
      return Faction.STORYTELLING;
   }

   public abstract ResourceCostST getCostSt();

   public ResourceCost getCost() {
      return this.getCostSt().asResourceCost();
   }

   public void setAnchor(BlockPos bp) {
      this.anchorPos = bp;
   }

   public BlockPos getAnchor() {
      return this.anchorPos;
   }

   public boolean getWillRetaliate() {
      return false;
   }

   public boolean getAggressiveWhenIdle() {
      return !this.isVehicle();
   }

   public boolean getHoldPosition() {
      return this.holdPosition;
   }

   public float getMovementSpeed() {
      return (float)this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
   }

   public float getUnitMaxHealth() {
      return (float)this.getAttribute(Attributes.MAX_HEALTH).getValue();
   }

   public float getAttackRange() {
      return (float)this.getAttribute(RTSAttributesST.ATTACK_RANGE).getValue();
   }

   public float getUnitAttackDamage() {
      return (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
   }

   public int getAttackCooldown() {
      return (int)(20.0 / this.getAttribute(RTSAttributesST.ATTACKS_PER_SECOND).getValue());
   }

   public float getAttacksPerSecond() {
      return (float)this.getAttribute(RTSAttributesST.ATTACKS_PER_SECOND).getValue();
   }

   public float getAggroRange() {
      return (float)this.getAttribute(RTSAttributesST.AGGRO_RANGE).getValue();
   }

   public int getMaxResources() {
      return (int)this.getAttribute(RTSAttributesST.MAX_RESOURCES).getValue();
   }

   public boolean canGarrison() {
      return this.getGarrisonGoal() != null;
   }

   public boolean canUsePortal() {
      return this.getUsePortalGoal() != null;
   }

   public boolean canAttackBuildings() {
      return this.getAttackBuildingGoal() != null;
   }

   public Goal getAttackBuildingGoal() {
      return null;
   }

   public void initialiseGoals() {
      this.usePortalGoal = new UsePortalGoal(this);
      this.garrisonGoal = new GarrisonGoal(this);
      this.moveGoal = new MoveToTargetBlockGoalSt(this, false, 0);
      this.targetGoal = new SelectedTargetGoal(this, true, true);
      this.returnResourcesGoal = new ReturnResourcesGoal(this);
   }

   protected void registerGoals() {
      this.initialiseGoals();
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, this.usePortalGoal);
      this.goalSelector.addGoal(2, this.garrisonGoal);
      this.targetSelector.addGoal(2, this.targetGoal);
      this.goalSelector.addGoal(2, this.returnResourcesGoal);
      this.goalSelector.addGoal(3, this.moveGoal);
   }

   public boolean isLeftHanded() {
      return false;
   }

   protected void pickUpItem(ItemEntity pItemEntity) {
   }

   public boolean isPushable() {
      return false;
   }

   public boolean removeWhenFarAway(double d) {
      return false;
   }

   @Nullable
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag
   ) {
      return pSpawnData;
   }

   public String getOwnerName() {
      return (String)this.entityData.get(ownerDataAccessor);
   }

   public void setOwnerName(String name) {
      this.entityData.set(ownerDataAccessor, name);
   }

   @Override
   public void addLayers(LayersList layers) {
      layers.addLast("rts_movement", BlendMode.OVERRIDE, 1.0F);
   }

   @Override
   public void tick() {
      super.tick();
      Unit.tick(this);
      if (!this.level().isClientSide) {
         if (!this.getNavigation().isInProgress()) {
            this.stopAnimation("rts_movement");
         } else if (this.animationSystem.getLayer("rts_movement").activeAnimation().isNull()) {
            IDefaultAnimConfiguration iDefaultAnimConfiguration = this.getWrapper().getConfiguration();
            if (iDefaultAnimConfiguration != null) {
               MovementActionType movementType = MovementActionType.getBySpeed((double)this.getMovementSpeed());
               List<ConfiguredAnimation> movementList = iDefaultAnimConfiguration.getAnimations(movementType);
               if (!movementList.isEmpty()) {
                  ConfiguredAnimation movement = movementList.get(Unit.RANDOM.nextInt(movementList.size()));
                  this.startAnimation("rts_movement", movement);
               }
            }
         }
      }
   }

   @Generated
   public List<AbilityButton> getAbilityButtons() {
      return this.abilityButtons;
   }

   @Generated
   public List<Ability> getAbilities() {
      return this.abilities;
   }

   @Generated
   public List<ItemStack> getItems() {
      return this.items;
   }

   @Generated
   public ArrayList<Checkpoint> getCheckpoints() {
      return this.checkpoints;
   }

   @Generated
   public int getEatingTicksLeft() {
      return this.eatingTicksLeft;
   }

   @Generated
   public void setEatingTicksLeft(int eatingTicksLeft) {
      this.eatingTicksLeft = eatingTicksLeft;
   }

   @Generated
   public LivingEntity getFollowTarget() {
      return this.followTarget;
   }

   @Generated
   public void setFollowTarget(LivingEntity followTarget) {
      this.followTarget = followTarget;
   }

   @Generated
   public void setHoldPosition(boolean holdPosition) {
      this.holdPosition = holdPosition;
   }

   @Generated
   public GarrisonGoal getGarrisonGoal() {
      return this.garrisonGoal;
   }

   @Generated
   public UsePortalGoal getUsePortalGoal() {
      return this.usePortalGoal;
   }

   @Generated
   public MoveToTargetBlockGoal getMoveGoal() {
      return this.moveGoal;
   }

   @Generated
   public SelectedTargetGoal<? extends LivingEntity> getTargetGoal() {
      return this.targetGoal;
   }

   @Generated
   public ReturnResourcesGoal getReturnResourcesGoal() {
      return this.returnResourcesGoal;
   }
}
