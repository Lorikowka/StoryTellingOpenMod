package com.storyteam.storytelling.system.simulator.main_page.init;

import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/simulator/main_tab/"
)
public interface MainTabModels {
   @RegistryName("simulator")
   IGeometryContainer SIMULATOR = IGeometryContainer.create();
}
