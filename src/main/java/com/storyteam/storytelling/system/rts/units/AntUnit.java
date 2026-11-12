package com.storyteam.storytelling.system.rts.units;

import com.solegendary.reignofnether.ability.abilities.CallToArmsUnit;
import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.faction.FactionRegistrar;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchServerEvents;
import com.solegendary.reignofnether.unit.goals.AbstractMeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.BuildRepairGoal;
import com.solegendary.reignofnether.unit.goals.CallToArmsGoal;
import com.solegendary.reignofnether.unit.goals.MeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.RandomLookAroundUnitGoal;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.ConvertableUnit;
import com.solegendary.reignofnether.unit.interfaces.WorkerUnit;
import com.solegendary.reignofnether.util.Faction;
import com.storyteam.storytelling.api.entity.INoNameplate;
import com.storyteam.storytelling.client.render.entity.SimpleBedrockModel;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.configurations.IDynamicModelEntity;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.common.ResourceST;
import com.storyteam.storytelling.system.rts.goals.GatherResourcesGoalSt;
import com.storyteam.storytelling.system.rts.init.RTSAttributesST;
import com.storyteam.storytelling.system.rts.init.RTSEntityDataSerializersST;
import com.storyteam.storytelling.system.rts.units.api.AnimatedUnit;
import com.storyteam.storytelling.system.rts.units.api.data.AntUnitData;
import com.storyteam.storytelling.system.rts.units.api.data.FormikProfession;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammeranims.api.geometry.IGeometryContainer;

public class AntUnit extends AnimatedUnit implements WorkerUnit, AttackerUnit, ConvertableUnit, IDynamicModelEntity<AntUnit>, INoNameplate {
   public CallToArmsGoal callToArmsGoal;
   private IGeometryContainer lastGeometry;
   private SimpleBedrockModel<AntUnit> model;
   public BuildRepairGoal buildRepairGoal;
   public GatherResourcesGoalSt gatherResourceGoal;
   private AbstractMeleeAttackUnitGoal attackGoal;
   private BlockPos attackMoveTarget = null;
   public boolean converted = false;
   private boolean shouldDiscard = false;
   private static final EntityDataAccessor<AntUnitData> UNIT_DATA = SynchedEntityData.defineId(AntUnit.class, RTSEntityDataSerializersST.ANT_UNIT_DATA);

   public AntUnit(EntityType<? extends AnimatedEntity> entityType, Level level) {
      super(entityType, level);
      this.abilities.add(new CallToArmsUnit(level));
      this.updateAbilityButtons();
   }

   public String getUnitName() {
      return this.getUnitData().getProfession().getUnitName();
   }

   @Override
   public void tick() {
      if (this.shouldDiscard) {
         this.discard();
      } else {
         this.setCanPickUpLoot(true);
         super.tick();
         AttackerUnit.tick(this);
         WorkerUnit.tick(this);
         this.callToArmsGoal.tick();
      }
   }

   public void updateAbilityButtons() {
      if (this.level().isClientSide()) {
         this.abilityButtons.clear();
         this.abilityButtons.addAll((Collection<? extends AbilityButton>)FactionRegistrar.get(Faction.STORYTELLING).buildingButtons.get());
         this.abilityButtons.add(this.abilities.get(0).getButton(Keybindings.keyV));
      }
   }

   @Override
   public ResourceCostST getCostSt() {
      return ResourceCostST.create().set(ResourceST.POPULATION, 1);
   }

   public void resetBehaviours() {
      this.callToArmsGoal.stop();
   }

   @Override
   public void initialiseGoals() {
      super.initialiseGoals();
      this.attackGoal = new MeleeAttackUnitGoal(this, true);
      this.buildRepairGoal = new BuildRepairGoal(this);
      this.gatherResourceGoal = new GatherResourcesGoalSt(this);
      this.callToArmsGoal = new CallToArmsGoal(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(2, this.attackGoal);
      this.goalSelector.addGoal(2, this.buildRepairGoal);
      this.goalSelector.addGoal(2, this.gatherResourceGoal);
      this.goalSelector.addGoal(4, new RandomLookAroundUnitGoal(this));
   }

   public void setupEquipmentAndUpgradesServer() {
      AttributeInstance maxResourcesAttr = this.getAttributes().getInstance(RTSAttributesST.MAX_RESOURCES);
      if (!maxResourcesAttr.getModifiers().stream().anyMatch(m -> m.getName().equals("resources"))) {
         if (ResearchServerEvents.playerHasResearch(this.getOwnerName(), ProductionItems.RESEARCH_RESOURCE_CAPACITY)) {
            maxResourcesAttr.addTransientModifier(new AttributeModifier("resources", 100.0, Operation.ADDITION));
         }
      }
   }

   @Override
   public ResourceLocation getTexture() {
      return this.getUnitData().getProfession().getTx();
   }

   @Override
   public SimpleBedrockModel<AntUnit> getModel(Function<IGeometryContainer, SimpleBedrockModel<AntUnit>> onNewGeometry) {
      IGeometryContainer curGeometry = this.getUnitData().getProfession().getGeo();
      if (this.lastGeometry != curGeometry) {
         this.setLastGeometry(curGeometry);
         this.model = onNewGeometry.apply(curGeometry);
      }

      return this.model;
   }

   public BlockState getReplantBlockState() {
      return Blocks.WHEAT.defaultBlockState();
   }

   public void setAttackMoveTarget(@Nullable BlockPos bp) {
      this.attackMoveTarget = bp;
   }

   public boolean shouldDiscard() {
      return this.shouldDiscard;
   }

   public void convertToMilitia() {
      if (!this.converted) {
      }
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.VILLAGER_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.VILLAGER_DEATH;
   }

   protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
      return SoundEvents.VILLAGER_HURT;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ownerDataAccessor, "");
      this.entityData.define(UNIT_DATA, new AntUnitData(FormikProfession.WORKER, false));
   }

   public AntUnitData getUnitData() {
      return (AntUnitData)this.entityData.get(UNIT_DATA);
   }

   public void setUnitData(@NotNull AntUnitData data) {
      this.entityData.set(UNIT_DATA, data);
   }

   public void updateUnitData(UnaryOperator<AntUnitData> unaryOperator) {
      this.setUnitData(unaryOperator.apply(this.getUnitData()));
   }

   @Override
   public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
      super.addAdditionalSaveData(pCompound);
      pCompound.put("unitData", this.getUnitData().serializeNBT());
      this.addUnitSaveData(pCompound);
   }

   @Override
   public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
      super.readAdditionalSaveData(pCompound);
      AntUnitData unitData = new AntUnitData();
      unitData.deserializeNBT(pCompound.getCompound("unitData"));
      this.setUnitData(unitData);
      this.readUnitSaveData(pCompound);
   }

   public int getPortraitYOffset() {
      return -37;
   }

   public int getPortraitScale() {
      return -22;
   }

   @Generated
   public IGeometryContainer getLastGeometry() {
      return this.lastGeometry;
   }

   @Generated
   public void setLastGeometry(IGeometryContainer lastGeometry) {
      this.lastGeometry = lastGeometry;
   }

   @Generated
   public SimpleBedrockModel<AntUnit> getModel() {
      return this.model;
   }

   @Generated
   public void setModel(SimpleBedrockModel<AntUnit> model) {
      this.model = model;
   }

   @Generated
   public BuildRepairGoal getBuildRepairGoal() {
      return this.buildRepairGoal;
   }

   @Generated
   public GatherResourcesGoalSt getGatherResourceGoal() {
      return this.gatherResourceGoal;
   }

   @Generated
   public AbstractMeleeAttackUnitGoal getAttackGoal() {
      return this.attackGoal;
   }

   @Generated
   public BlockPos getAttackMoveTarget() {
      return this.attackMoveTarget;
   }

   @Generated
   public void setShouldDiscard(boolean shouldDiscard) {
      this.shouldDiscard = shouldDiscard;
   }
}
