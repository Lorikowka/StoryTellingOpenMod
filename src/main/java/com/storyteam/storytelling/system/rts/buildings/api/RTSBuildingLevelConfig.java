package com.storyteam.storytelling.system.rts.buildings.api;

import com.solegendary.reignofnether.building.BuildingServerboundPacket;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.production.CustomProductionItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class RTSBuildingLevelConfig extends CustomProductionItem {
   private final int levelId;
   private final String structure;
   private final Supplier<Boolean> enableUpgradeButton;
   private final Supplier<List<FormattedCharSequence>> tooltip;

   public RTSBuildingLevelConfig(
      int levelId, ResourceCostST cost, String structure, Supplier<Boolean> enableUpgradeButton, Supplier<List<FormattedCharSequence>> tooltip
   ) {
      super(cost);
      this.levelId = levelId;
      this.structure = structure;
      this.enableUpgradeButton = enableUpgradeButton;
      this.tooltip = tooltip;
      this.onComplete = (level, placement) -> {
         LevelingPlacement lPlacement = (LevelingPlacement)placement;
         lPlacement.updateStructure(++lPlacement.upgradeLevel);
      };
   }

   public String getItemName() {
      return "Улучшение до " + (this.levelId + 1) + " уровня";
   }

   public Button getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
      return new Button(
         this.structure,
         14,
         new ResourceLocation("minecraft", "textures/block/iron_block.png"),
         new ResourceLocation("reignofnether", "textures/hud/pink_button_transparent.png"),
         hotkey,
         () -> false,
         () -> {
            if (this.itemIsBeingProducedAt(prodBuilding)) {
               return true;
            } else {
               if (prodBuilding instanceof LevelingPlacement p && p.getUpgradeLevel() != this.levelId - 1) {
                  return true;
               }

               return false;
            }
         },
         this.enableUpgradeButton,
         () -> BuildingServerboundPacket.startProduction(prodBuilding.originPos, this),
         null,
         new ArrayList<FormattedCharSequence>() {
            {
               this.add(FormattedCharSequence.forward("Улучшить до " + (RTSBuildingLevelConfig.this.levelId + 1) + " уровня ", Style.EMPTY.withBold(true)));
               this.add(RTSBuildingLevelConfig.this.getCostSt().renderCost());
               this.add(RTSBuildingLevelConfig.this.getCostSt().renderPopulationAndTime());
               this.add(FormattedCharSequence.forward("", Style.EMPTY));
               this.addAll(RTSBuildingLevelConfig.this.tooltip.get());
            }
         }
      );
   }

   public Button getCancelButton(ProductionPlacement prodBuilding, boolean first) {
      return new Button(
         this.structure,
         14,
         new ResourceLocation("minecraft", "textures/block/barrier.png"),
         new ResourceLocation("reignofnether", "textures/hud/pink_button_transparent.png"),
         null,
         () -> false,
         () -> false,
         () -> true,
         () -> BuildingServerboundPacket.cancelProduction(prodBuilding.minCorner, this, first),
         null,
         null
      );
   }

   public static RTSBuildingLevelConfig.Builder builder(int levelId) {
      return new RTSBuildingLevelConfig.Builder(levelId);
   }

   @Generated
   public int getLevelId() {
      return this.levelId;
   }

   @Generated
   public String getStructure() {
      return this.structure;
   }

   @Generated
   public Supplier<Boolean> getEnableUpgradeButton() {
      return this.enableUpgradeButton;
   }

   @Generated
   public Supplier<List<FormattedCharSequence>> getTooltip() {
      return this.tooltip;
   }

   public static class Builder {
      private final int levelId;
      private ResourceCostST cost;
      private String structure;
      private Supplier<Boolean> isEnabled = () -> true;
      private Supplier<List<FormattedCharSequence>> tooltip = Collections::emptyList;

      public RTSBuildingLevelConfig.Builder cost(ResourceCostST cost) {
         this.cost = cost;
         return this;
      }

      public RTSBuildingLevelConfig.Builder structure(String structure) {
         this.structure = structure;
         return this;
      }

      public RTSBuildingLevelConfig.Builder isEnabled(Supplier<Boolean> isEnabled) {
         this.isEnabled = isEnabled;
         return this;
      }

      public RTSBuildingLevelConfig.Builder tooltip(Supplier<List<FormattedCharSequence>> tooltip) {
         this.tooltip = tooltip;
         return this;
      }

      public RTSBuildingLevelConfig build() {
         return new RTSBuildingLevelConfig(this.levelId, this.cost, this.structure, this.isEnabled, this.tooltip);
      }

      @Generated
      public Builder(int levelId) {
         this.levelId = levelId;
      }
   }
}
