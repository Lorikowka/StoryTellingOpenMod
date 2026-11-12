package com.storyteam.storytelling.system.rts.buildings.api;

import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.BuildingServerEvents;
import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.util.Faction;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import org.zeith.hammerlib.api.proxy.IProxy;

public abstract class LevelingBuilding extends ProductionBuilding {
   private final RTSBuildingConfig config;

   public LevelingBuilding(RTSBuildingConfig config) {
      super(config.getLevelConfigs().get(0).getStructure(), config.getLevelConfigs().get(0).getCostSt().asResourceCost(), config.isCapitol());
      this.config = config;
      this.name = config.getName();
      this.icon = config.getIconTx();
      this.portraitIcon = config.getPortraitIconTx();
      this.repairTimeModifier = config.getRepairTimeModifier();
      this.buildTimeModifier = config.getBuildTimeModifier();
      this.startingBlockTypes.addAll(List.of(config.getStartingBlockTypes()));
      config.getProductionItems().forEach(p -> this.productions.add((ProductionItem)p.getFirst(), (Keybinding)p.getSecond()));
      config.getLevelConfigs().forEach(l -> this.productions.add(l, Keybindings.keyU));
      this.canAcceptResources = config.isAcceptResources();
      if (config.isEditableByUnits()) {
         this.explodeChance = 0.0F;
      }

      this.onStart = ownerName -> this.getBuildCost().trySubtract(ownerName);
   }

   public Faction getFaction() {
      return Faction.STORYTELLING;
   }

   private int countBuildingSameType() {
      List<BuildingPlacement> buildings = (List<BuildingPlacement>)IProxy.create(
         () -> BuildingClientEvents::getBuildings, () -> BuildingServerEvents::getBuildings
      );
      return Math.toIntExact(buildings.stream().filter(b -> b.getBuilding() == this).count());
   }

   private ResourceCostST getBuildCost() {
      double multiplier = Math.min(1.0 + (double)this.countBuildingSameType() * 0.15, 3.0);
      return this.config.getLevelConfigs().get(0).getCostSt().multiplyCost(multiplier);
   }

   public AbilityButton getBuildButton() {
      final ResourceCostST buildCost = this.getBuildCost();
      return new AbilityButton(
         this.config.getName(),
         this.config.getIconTx(),
         this.config.getKeybinding(),
         () -> BuildingClientEvents.getBuildingToPlace() == this,
         this.config.getHideBuyButton(),
         this.config.getLevelConfigs().get(0).getEnableUpgradeButton(),
         () -> BuildingClientEvents.setBuildingToPlace(this),
         null,
         new ArrayList<FormattedCharSequence>() {
            {
               this.add(FormattedCharSequence.forward(LevelingBuilding.this.config.getName(), Style.EMPTY.withBold(true)));
               this.add(buildCost.renderCost());
               this.add(buildCost.renderPopulationAndTime());
               this.add(FormattedCharSequence.forward("", Style.EMPTY));
               this.addAll(LevelingBuilding.this.config.getTooltip().get());
            }
         },
         null
      );
   }

   public BuildingPlacement createBuildingPlacement(Level level, BlockPos pos, Rotation rotation, String ownerName) {
      return new LevelingPlacement(
         this,
         level,
         pos,
         rotation,
         ownerName,
         BuildingUtils.getAbsoluteBlockData(this.getRelativeBlockData(level), level, pos, rotation),
         this.config.isCapitol()
      );
   }

   @Deprecated
   public AbilityButton getBuildButton(Keybinding keybinding) {
      return this.getBuildButton();
   }

   public boolean isBuildAllowed(AABB placement) {
      return super.isBuildAllowed(placement);
   }

   @Generated
   public RTSBuildingConfig getConfig() {
      return this.config;
   }
}
