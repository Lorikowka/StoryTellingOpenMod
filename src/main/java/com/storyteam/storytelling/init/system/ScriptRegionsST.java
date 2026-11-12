package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.regions.ScriptRegion;
import com.storyteam.storytelling.util.LevelAABB;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface ScriptRegionsST {
   @RegistryName("test")
   ScriptRegion TEST = ScriptRegion.builder().area(LevelAABB.of(Level.OVERWORLD, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0)).build();
}
