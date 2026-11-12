package com.storyteam.storytelling.system.simulator.main_page.init;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.system.simulator.main_page.blocks.SimulatorBlock;
import org.zeith.hammerlib.annotations.Ref;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   creativeTabs = {@Ref(
      value = TabsST.class,
      field = "BLOCKITEMS"
   )},
   prefix = "system/simulator/main_tab/"
)
public interface MainTabBlocks {
   @RegistryName("simulator")
   SimulatorBlock SIMULATOR = new SimulatorBlock();
}
