package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.system.gating.LockedEntry;
import com.storyteam.storytelling.util.ScriptUtils;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.util.mcf.Resources;

@SimplyRegister
public interface LockedEntriesST {
   @RegistryName("satchel")
   LockedEntry SATCHEL = LockedEntry.builder().item(ScriptUtils.getItemLike("thermal:satchel")).recipe(Resources.location("thermal", "tools/satchel")).build();
}
