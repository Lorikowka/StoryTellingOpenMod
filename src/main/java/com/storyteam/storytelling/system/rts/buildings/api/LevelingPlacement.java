package com.storyteam.storytelling.system.rts.buildings.api;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.BuildingBlockData;
import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.buildings.placements.UpgradablePlacement;
import com.solegendary.reignofnether.resources.ResourceCost;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

public class LevelingPlacement extends ProductionPlacement implements UpgradablePlacement {
   public LevelingPlacement(
      Building building, Level level, BlockPos originPos, Rotation rotation, String ownerName, ArrayList<BuildingBlock> blocks, boolean isCapitol
   ) {
      super(building, level, originPos, rotation, ownerName, blocks, isCapitol);
   }

   public boolean canAfford(String ownerName) {
      return ((LevelingBuilding)this.getBuilding()).getConfig().getLevelConfigs().get(0).getCostSt().canAfford(ownerName);
   }

   public ResourceCost getCost() {
      return ((LevelingBuilding)this.getBuilding()).getConfig().getLevelConfigs().get(this.getUpgradeLevel()).getCostSt().asResourceCost();
   }

   public void updateStructure(int upgradeLevel) {
      String newStructureName = ((LevelingBuilding)this.getBuilding()).getConfig().getLevelConfigs().get(upgradeLevel).getStructure();
      ArrayList<BuildingBlock> newBlocks = BuildingBlockData.getBuildingBlocksFromNbt(newStructureName, this.getLevel());
      this.blocks = BuildingUtils.getAbsoluteBlockData(newBlocks, this.getLevel(), this.originPos, this.rotation);
      super.refreshBlocks();
   }
}
