package com.storyteam.storytelling.system.rts.init;

import com.solegendary.reignofnether.keybinds.Keybindings;
import com.storyteam.storytelling.system.rts.RTSSystem;
import com.storyteam.storytelling.system.rts.buildings.Library;
import com.storyteam.storytelling.system.rts.buildings.Temple;
import com.storyteam.storytelling.system.rts.buildings.TownHall;
import com.storyteam.storytelling.system.rts.buildings.api.RTSBuildingConfig;
import com.storyteam.storytelling.system.rts.common.ResourceCostST;
import com.storyteam.storytelling.system.rts.common.ResourceST;
import com.storyteam.storytelling.system.rts.helpers.CognitionAccessor;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognitions;
import net.minecraft.world.level.block.Blocks;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/building/"
)
public interface RTSBuildingsST {
   @RegistryName("townhall")
   RTSBuildingConfig TOWNHALL = RTSBuildingConfig.builder()
      .name("Ратуша")
      .building(TownHall::new)
      .iconTx(RTSSystem.iconTx("warehouse"))
      .portraitIconTx(RTSSystem.iconTx("warehouse"))
      .keybinding(Keybindings.keyQ)
      .capitol()
      .acceptResources()
      .startingBlockTypes(Blocks.STONE_BRICKS, Blocks.STONE, Blocks.STONE_BRICK_STAIRS, Blocks.ANDESITE)
      .hideBuyButton(() -> !CognitionAccessor.isCardResearched(Cognitions.CITY_HALL))
      .level(
         l -> l.structure("st_townhall_0")
               .cost(ResourceCostST.create().add(ResourceST.POPULATION, 5).add(ResourceST.RICE, 1).add(ResourceST.ETHER, 1).add(ResourceST.VERSIFORM, 1))
               .isEnabled(() -> CognitionAccessor.isCardLevelReached(Cognitions.CITY_HALL, 1))
      )
      .level(
         l -> l.structure("st_townhall_1")
               .cost(
                  ResourceCostST.create()
                     .add(ResourceST.POPULATION, 5)
                     .add(ResourceST.RICE, 2)
                     .add(ResourceST.ETHER, 2)
                     .add(ResourceST.VERSIFORM, 2)
                     .add(ResourceST.TIME, 5)
               )
               .isEnabled(() -> CognitionAccessor.isCardLevelReached(Cognitions.CITY_HALL, 2))
      )
      .level(
         l -> l.structure("st_townhall_2")
               .cost(
                  ResourceCostST.create()
                     .add(ResourceST.POPULATION, 5)
                     .add(ResourceST.RICE, 2)
                     .add(ResourceST.ETHER, 2)
                     .add(ResourceST.VERSIFORM, 2)
                     .add(ResourceST.TIME, 10)
               )
               .isEnabled(() -> CognitionAccessor.isCardLevelReached(Cognitions.CITY_HALL, 3))
      )
      .build();
   @RegistryName("temple")
   RTSBuildingConfig TEMPLE = RTSBuildingConfig.builder()
      .name("Храм")
      .building(Temple::new)
      .iconTx(RTSSystem.iconTx("warehouse_branch"))
      .portraitIconTx(RTSSystem.iconTx("warehouse_branch"))
      .keybinding(Keybindings.keyM)
      .startingBlockTypes(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, Blocks.SANDSTONE_STAIRS, Blocks.SAND, Blocks.PRISMARINE_BRICK_SLAB)
      .level(l -> l.structure("st_temple").cost(ResourceCostST.create().add(ResourceST.RICE, 1).add(ResourceST.ETHER, 1).add(ResourceST.VERSIFORM, 1)))
      .build();
   @RegistryName("library")
   RTSBuildingConfig LIBRARY = RTSBuildingConfig.builder()
      .name("Библиотека")
      .building(Library::new)
      .iconTx(RTSSystem.iconTx("rice_field"))
      .portraitIconTx(RTSSystem.iconTx("rice_field"))
      .keybinding(Keybindings.keyL)
      .startingBlockTypes(
         Blocks.OAK_PLANKS,
         Blocks.STONE,
         Blocks.STONE_STAIRS,
         Blocks.ANDESITE,
         Blocks.STRIPPED_OAK_WOOD,
         Blocks.STRIPPED_OAK_LOG,
         Blocks.ANDESITE_WALL,
         Blocks.STONE_PRESSURE_PLATE
      )
      .level(l -> l.structure("st_library").cost(ResourceCostST.create().add(ResourceST.RICE, 1).add(ResourceST.ETHER, 1).add(ResourceST.VERSIFORM, 1)))
      .build();
}
