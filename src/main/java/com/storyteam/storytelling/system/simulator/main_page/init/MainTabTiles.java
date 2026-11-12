package com.storyteam.storytelling.system.simulator.main_page.init;

import com.storyteam.storytelling.system.simulator.main_page.client.renderers.SimulatorTESR;
import com.storyteam.storytelling.system.simulator.main_page.tiles.SimulatorTile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.forge.BlockAPI;

@SimplyRegister(
   prefix = "system/simulator/main_tab/"
)
public interface MainTabTiles {
   @RegistryName("sumulator")
   @TileRenderer(SimulatorTESR.class)
   BlockEntityType<SimulatorTile> SIMULATOR_TILE = BlockAPI.createBlockEntityType(SimulatorTile::new, new Block[]{MainTabBlocks.SIMULATOR});
}
