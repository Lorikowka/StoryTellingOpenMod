package com.storyteam.storytelling.system.rts.init;

import com.storyteam.storytelling.St;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/sounds/"
)
public interface RTSSoundsST {
   @RegistryName("building_built")
   SoundEvent BUILDING_BUILT = $("building_built");
   @RegistryName("place_building")
   SoundEvent PLACE_BUILDING = $("place_building");

   private static SoundEvent $(String name) {
      return SoundEvent.createVariableRangeEvent(St.id("system/rts/" + name));
   }
}
