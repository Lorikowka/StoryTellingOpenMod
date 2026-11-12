package com.storyteam.storytelling.init;

import com.storyteam.storytelling.client.render.tile.BottleFlowerTESR;
import com.storyteam.storytelling.client.render.tile.EidosShellTESR;
import com.storyteam.storytelling.client.render.tile.EmptyTileEntityRenderer;
import com.storyteam.storytelling.client.render.tile.GeyserEidosTESR;
import com.storyteam.storytelling.client.render.tile.GeyserEtherTESR;
import com.storyteam.storytelling.client.render.tile.LightFlowerTESR;
import com.storyteam.storytelling.client.render.tile.PampushkaMushroomTESR;
import com.storyteam.storytelling.client.render.tile.TrapChestTESR;
import com.storyteam.storytelling.content.tiles.BottleFlowerTile;
import com.storyteam.storytelling.content.tiles.ColoredLightBlockTile;
import com.storyteam.storytelling.content.tiles.EidosShellTile;
import com.storyteam.storytelling.content.tiles.EtherealFlashTile;
import com.storyteam.storytelling.content.tiles.GeyserEidosTile;
import com.storyteam.storytelling.content.tiles.GeyserEtherTile;
import com.storyteam.storytelling.content.tiles.LevitationDustTile;
import com.storyteam.storytelling.content.tiles.LightFlowerTile;
import com.storyteam.storytelling.content.tiles.PampushkaMushroomTile;
import com.storyteam.storytelling.content.tiles.TileLinkable;
import com.storyteam.storytelling.content.tiles.TrapChestTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.forge.BlockAPI;

@SimplyRegister
public interface TilesST {
   @RegistryName("linkable")
   BlockEntityType<TileLinkable> LINKABLE = BlockAPI.createBlockEntityType(TileLinkable::new, new Block[0]);
   @RegistryName("colored_light")
   BlockEntityType<ColoredLightBlockTile> COLORED_LIGHT = BlockAPI.createBlockEntityType(ColoredLightBlockTile::new, new Block[0]);
   @RegistryName("pampushka_mushroom")
   @TileRenderer(PampushkaMushroomTESR.class)
   BlockEntityType<PampushkaMushroomTile> PAMPUSHKA_MUSHROOM = BlockAPI.createBlockEntityType(
      PampushkaMushroomTile::new, new Block[]{BlocksST.PAMPUSHKA_MUSHROOM, BlocksST.PAMPUSHKA_MUSHROOM_GRAY}
   );
   @RegistryName("lightflower")
   @TileRenderer(LightFlowerTESR.class)
   BlockEntityType<LightFlowerTile> LIGHTFLOWER = BlockAPI.createBlockEntityType(
      LightFlowerTile::new, new Block[]{BlocksST.LIGHTFLOWER, BlocksST.LIGHTFLOWER_GRAY}
   );
   @RegistryName("eidos_shell")
   @TileRenderer(EidosShellTESR.class)
   BlockEntityType<EidosShellTile> EIDOS_SHELL = BlockAPI.createBlockEntityType(
      EidosShellTile::new, new Block[]{BlocksST.EIDOS_SHELL, BlocksST.EIDOS_SHELL_GRAY}
   );
   @RegistryName("bottleflower")
   @TileRenderer(BottleFlowerTESR.class)
   BlockEntityType<BottleFlowerTile> BOTTLEFLOWER = BlockAPI.createBlockEntityType(
      BottleFlowerTile::new,
      new Block[]{
         BlocksST.BOTTLEFLOWER_RED_COLORED,
         BlocksST.BOTTLEFLOWER_RED_GRAY,
         BlocksST.BOTTLEFLOWER_YELLOW_COLORED,
         BlocksST.BOTTLEFLOWER_YELLOW_GRAY,
         BlocksST.BOTTLEFLOWER_GREEN_COLORED,
         BlocksST.BOTTLEFLOWER_GREEN_GRAY,
         BlocksST.BOTTLEFLOWER_BLACK_COLORED,
         BlocksST.BOTTLEFLOWER_BLACK_GRAY,
         BlocksST.BOTTLEFLOWER_BLUE_COLORED,
         BlocksST.BOTTLEFLOWER_BLUE_GRAY,
         BlocksST.BOTTLEFLOWER_CYAN_COLORED,
         BlocksST.BOTTLEFLOWER_CYAN_GRAY,
         BlocksST.BOTTLEFLOWER_VIOLET_COLORED,
         BlocksST.BOTTLEFLOWER_VIOLET_GRAY
      }
   );
   @RegistryName("ethereal_flash")
   @TileRenderer(EmptyTileEntityRenderer.class)
   BlockEntityType<EtherealFlashTile> ETHEREAL_FLASH = BlockAPI.createBlockEntityType(EtherealFlashTile::new, new Block[]{BlocksST.ETHEREAL_FLASH});
   @RegistryName("levitation_dust")
   @TileRenderer(EmptyTileEntityRenderer.class)
   BlockEntityType<LevitationDustTile> LEVITATION_DUST = BlockAPI.createBlockEntityType(LevitationDustTile::new, new Block[]{BlocksST.LEVITATION_DUST});
   @RegistryName("trap_chest")
   @TileRenderer(TrapChestTESR.class)
   BlockEntityType<TrapChestTile> TRAP_CHEST = BlockAPI.createBlockEntityType(TrapChestTile::new, new Block[]{BlocksST.TRAP_CHEST});
   @RegistryName("geyser_eidos")
   @TileRenderer(GeyserEidosTESR.class)
   BlockEntityType<GeyserEidosTile> GEYSER_EIDOS_MAIN = BlockAPI.createBlockEntityType(GeyserEidosTile::new, new Block[]{BlocksST.GEYSER_EIDOS_MAIN});
   @RegistryName("ether_eidos")
   @TileRenderer(GeyserEtherTESR.class)
   BlockEntityType<GeyserEtherTile> GEYSER_ETHER_MAIN = BlockAPI.createBlockEntityType(GeyserEtherTile::new, new Block[]{BlocksST.GEYSER_ETHER_MAIN});
}
