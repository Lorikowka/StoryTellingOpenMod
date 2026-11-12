package com.storyteam.storytelling.system.rts.init;

import com.storyteam.storytelling.system.rts.units.AntUnit;
import com.storyteam.storytelling.system.rts.units.api.RTSUnitConfig;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/unit/"
)
public interface RTSUnitsST {
   @RegistryName("ant")
   RTSUnitConfig<AntUnit> ANT = RTSUnitConfig.<AntUnit>builder().entity(RTSUnitEntityTypesST.ANT).defaultUnit(true).build();
}
