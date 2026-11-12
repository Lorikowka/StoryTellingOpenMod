package com.storyteam.storytelling.system.rts.production;

import com.solegendary.reignofnether.building.production.ProductionItem;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import lombok.Generated;
import net.minecraft.world.level.Level;

public abstract class CustomProductionItem extends ProductionItem {
   protected ResourceCostST defaultCostSt;

   public CustomProductionItem(ResourceCostST defaultCostSt) {
      super(defaultCostSt.asResourceCost());
      this.defaultCostSt = defaultCostSt;
      this.onStart = (l, p, ownerName) -> this.getCostSt().trySubtract(ownerName);
   }

   public ResourceCostST getCostSt() {
      return this.defaultCostSt;
   }

   public boolean canAfford(Level level, String ownerName) {
      return this.getCostSt().canAfford(ownerName) && this.canAffordPopulation(level, ownerName);
   }

   @Generated
   public ResourceCostST getDefaultCostSt() {
      return this.defaultCostSt;
   }
}
