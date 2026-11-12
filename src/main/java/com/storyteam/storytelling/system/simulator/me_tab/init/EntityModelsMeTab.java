package com.storyteam.storytelling.system.simulator.me_tab.init;

import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/simulator/me_tab/"
)
public interface EntityModelsMeTab {
   @RegistryName("lololowka_metab")
   IGeometryContainer LOLOLOSHKA_ME_TAB = IGeometryContainer.create();
}
