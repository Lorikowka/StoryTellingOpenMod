package com.storyteam.storytelling.system.simulator.world_tab;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.simulator.world_tab.data.WMRButton;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegion;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegionsStorage;
import net.minecraft.util.Mth;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("simulator/world_tab")
public class WorldTabSystem implements ISystem {
   public static WorldTabSystem INSTANCE;

   public WorldTabSystem() {
      INSTANCE = this;
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(WorldTabCommand.class);
      gameBus.register(WorldMapRegionsStorage.class);
   }

   public static Runnable setRegionState(String region, byte state) {
      return () -> WorldMapRegionsStorage.WORLD_MAP_REGIONS.get(region).setState((byte)Mth.clamp(state, 0, 2));
   }

   public static Runnable setRegionButtonEnabled(String region, String button, boolean enabled) {
      return () -> {
         WorldMapRegion reg = WorldMapRegionsStorage.WORLD_MAP_REGIONS.get(region);

         for (WMRButton btn : reg.getWorldMapRegionInfo().getButtons()) {
            if (btn.getName().equals(button)) {
               btn.setEnabled(enabled);
            }
         }
      };
   }
}
