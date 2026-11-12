package com.storyteam.storytelling.system.rts.buildings.api;

import com.mojang.datafixers.util.Pair;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.storyteam.storytelling.system.rts.client.BuildZonesHandler;
import com.storyteam.storytelling.system.rts.storage.Identifiable;
import com.storyteam.storytelling.system.rts.storage.StorageType;
import com.storyteam.storytelling.system.rts.storage.Storages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import lombok.Generated;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class RTSBuildingConfig implements Identifiable<ResourceLocation>, ICustomRegistrar {
   private ResourceLocation id;
   private final Function<RTSBuildingConfig, LevelingBuilding> mkBuilding;
   private final String name;
   private final List<RTSBuildingLevelConfig> levelConfigs;
   private final ResourceLocation iconTx;
   private final ResourceLocation portraitIconTx;
   private final Keybinding keybinding;
   private final Supplier<Boolean> hideBuyButton;
   private final Supplier<List<FormattedCharSequence>> tooltip;
   private final List<Pair<ProductionItem, Keybinding>> productionItems;
   private final Block[] startingBlockTypes;
   private final float buildTimeModifier;
   private final float repairTimeModifier;
   private final boolean isCapitol;
   private final boolean editableByUnits;
   private final boolean acceptResources;
   private final Predicate<AABB> buildAllowedPredicate;
   private LevelingBuilding _building = null;

   public LevelingBuilding asBuilding() {
      if (this._building == null) {
         this._building = this.mkBuilding.apply(this);
      }

      return this._building;
   }

   public void performRegister(RegisterEvent event, ResourceLocation id) {
      event.register(
         Registries.ITEM, registerHelper -> Storages.<RTSBuildingConfig, ResourceLocation>get(RTSBuildingConfig.class, StorageType.MEMORY).register(id, this)
      );
   }

   public static RTSBuildingConfig.Builder builder() {
      return new RTSBuildingConfig.Builder();
   }

   @Generated
   public ResourceLocation getId() {
      return this.id;
   }

   @Generated
   public Function<RTSBuildingConfig, LevelingBuilding> getMkBuilding() {
      return this.mkBuilding;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public List<RTSBuildingLevelConfig> getLevelConfigs() {
      return this.levelConfigs;
   }

   @Generated
   public ResourceLocation getIconTx() {
      return this.iconTx;
   }

   @Generated
   public ResourceLocation getPortraitIconTx() {
      return this.portraitIconTx;
   }

   @Generated
   public Keybinding getKeybinding() {
      return this.keybinding;
   }

   @Generated
   public Supplier<Boolean> getHideBuyButton() {
      return this.hideBuyButton;
   }

   @Generated
   public Supplier<List<FormattedCharSequence>> getTooltip() {
      return this.tooltip;
   }

   @Generated
   public List<Pair<ProductionItem, Keybinding>> getProductionItems() {
      return this.productionItems;
   }

   @Generated
   public Block[] getStartingBlockTypes() {
      return this.startingBlockTypes;
   }

   @Generated
   public float getBuildTimeModifier() {
      return this.buildTimeModifier;
   }

   @Generated
   public float getRepairTimeModifier() {
      return this.repairTimeModifier;
   }

   @Generated
   public boolean isCapitol() {
      return this.isCapitol;
   }

   @Generated
   public boolean isEditableByUnits() {
      return this.editableByUnits;
   }

   @Generated
   public boolean isAcceptResources() {
      return this.acceptResources;
   }

   @Generated
   public Predicate<AABB> getBuildAllowedPredicate() {
      return this.buildAllowedPredicate;
   }

   @Generated
   public RTSBuildingConfig(
      Function<RTSBuildingConfig, LevelingBuilding> mkBuilding,
      String name,
      List<RTSBuildingLevelConfig> levelConfigs,
      ResourceLocation iconTx,
      ResourceLocation portraitIconTx,
      Keybinding keybinding,
      Supplier<Boolean> hideBuyButton,
      Supplier<List<FormattedCharSequence>> tooltip,
      List<Pair<ProductionItem, Keybinding>> productionItems,
      Block[] startingBlockTypes,
      float buildTimeModifier,
      float repairTimeModifier,
      boolean isCapitol,
      boolean editableByUnits,
      boolean acceptResources,
      Predicate<AABB> buildAllowedPredicate
   ) {
      this.mkBuilding = mkBuilding;
      this.name = name;
      this.levelConfigs = levelConfigs;
      this.iconTx = iconTx;
      this.portraitIconTx = portraitIconTx;
      this.keybinding = keybinding;
      this.hideBuyButton = hideBuyButton;
      this.tooltip = tooltip;
      this.productionItems = productionItems;
      this.startingBlockTypes = startingBlockTypes;
      this.buildTimeModifier = buildTimeModifier;
      this.repairTimeModifier = repairTimeModifier;
      this.isCapitol = isCapitol;
      this.editableByUnits = editableByUnits;
      this.acceptResources = acceptResources;
      this.buildAllowedPredicate = buildAllowedPredicate;
   }

   @Generated
   public void setId(ResourceLocation id) {
      this.id = id;
   }

   public static class Builder {
      private Function<RTSBuildingConfig, LevelingBuilding> building;
      private String name;
      private Keybinding keybinding;
      private ResourceLocation iconTx;
      private ResourceLocation portraitIconTx;
      private final List<RTSBuildingLevelConfig> levelConfigs = new ArrayList<>();
      private Block[] startingBlockTypes = new Block[0];
      private Supplier<List<FormattedCharSequence>> tooltip = Collections::emptyList;
      private final List<Pair<ProductionItem, Keybinding>> productionItems = new ArrayList<>();
      private Supplier<Boolean> hideBuyButton = () -> false;
      private float buildTimeModifier = 1.0F;
      private float repairTimeModifier = 1.25F;
      private boolean editableByUnits = false;
      private boolean acceptResources = false;
      private boolean capitol = false;
      private Predicate<AABB> buildAllowedPredicate = BuildZonesHandler::isBuildingAllowed;

      public RTSBuildingConfig.Builder building(Function<RTSBuildingConfig, LevelingBuilding> building) {
         this.building = building;
         return this;
      }

      public RTSBuildingConfig.Builder name(String name) {
         this.name = name;
         return this;
      }

      public RTSBuildingConfig.Builder level(UnaryOperator<RTSBuildingLevelConfig.Builder> level) {
         this.levelConfigs.add(level.apply(RTSBuildingLevelConfig.builder(this.levelConfigs.size())).build());
         return this;
      }

      public RTSBuildingConfig.Builder iconTx(ResourceLocation iconTx) {
         this.iconTx = iconTx;
         return this;
      }

      public RTSBuildingConfig.Builder portraitIconTx(ResourceLocation portraitIconTx) {
         this.portraitIconTx = portraitIconTx;
         return this;
      }

      public RTSBuildingConfig.Builder keybinding(Keybinding keybinding) {
         this.keybinding = keybinding;
         return this;
      }

      public RTSBuildingConfig.Builder hideBuyButton(Supplier<Boolean> hideBuyButton) {
         this.hideBuyButton = hideBuyButton;
         return this;
      }

      public RTSBuildingConfig.Builder tooltip(Supplier<List<FormattedCharSequence>> tooltip) {
         this.tooltip = tooltip;
         return this;
      }

      public RTSBuildingConfig.Builder produces(ProductionItem productionItem, Keybinding keybinding) {
         this.productionItems.add(Pair.of(productionItem, keybinding));
         return this;
      }

      public RTSBuildingConfig.Builder startingBlockTypes(Block... startingBlockTypes) {
         this.startingBlockTypes = startingBlockTypes;
         return this;
      }

      public RTSBuildingConfig.Builder buildTimeModifier(float buildTimeModifier) {
         this.buildTimeModifier = buildTimeModifier;
         return this;
      }

      public RTSBuildingConfig.Builder repairTimeModifier(float repairTimeModifier) {
         this.repairTimeModifier = repairTimeModifier;
         return this;
      }

      public RTSBuildingConfig.Builder editableByUnits() {
         this.editableByUnits = true;
         return this;
      }

      public RTSBuildingConfig.Builder acceptResources() {
         this.acceptResources = true;
         return this;
      }

      public RTSBuildingConfig.Builder capitol() {
         this.capitol = true;
         return this;
      }

      public RTSBuildingConfig.Builder buildAllowedPredicate(Predicate<AABB> buildAllowedPredicate) {
         this.buildAllowedPredicate = buildAllowedPredicate;
         return this;
      }

      public RTSBuildingConfig.Builder buildAllowedPredicateWithDefault(Predicate<AABB> buildAllowedPredicate) {
         this.buildAllowedPredicate = this.buildAllowedPredicate.and(buildAllowedPredicate);
         return this;
      }

      public RTSBuildingConfig build() {
         if (this.building != null && this.name != null && this.iconTx != null && this.keybinding != null) {
            return new RTSBuildingConfig(
               this.building,
               this.name,
               this.levelConfigs,
               this.iconTx,
               this.portraitIconTx,
               this.keybinding,
               this.hideBuyButton,
               this.tooltip,
               this.productionItems,
               this.startingBlockTypes,
               this.buildTimeModifier,
               this.repairTimeModifier,
               this.capitol,
               this.editableByUnits,
               this.acceptResources,
               this.buildAllowedPredicate
            );
         } else {
            throw new IllegalStateException(RTSBuildingConfig.class.getSimpleName() + ": required builder params are not set");
         }
      }
   }
}
