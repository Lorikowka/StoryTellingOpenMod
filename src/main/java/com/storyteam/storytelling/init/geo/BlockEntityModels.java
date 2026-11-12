package com.storyteam.storytelling.init.geo;

import org.zeith.hammeranims.api.geometry.IGeometryContainer;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "tile/"
)
public interface BlockEntityModels {
   @RegistryName("city_gates")
   IGeometryContainer CITY_GATES = IGeometryContainer.create();
   @RegistryName("pampushka_mushroom")
   IGeometryContainer PAMPUSHKA_MUSHROOM = IGeometryContainer.create();
   @RegistryName("lightflower")
   IGeometryContainer LIGHTFLOWER = IGeometryContainer.create();
   @RegistryName("eidos_shell")
   IGeometryContainer EIDOS_SHELL = IGeometryContainer.create();
   @RegistryName("bottleflower")
   IGeometryContainer BOTTLEFLOWER = IGeometryContainer.create();
   @RegistryName("geyser")
   IGeometryContainer GEYSER = IGeometryContainer.create();
}
