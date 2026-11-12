package com.storyteam.storytelling.system.fight.mob_drop;

import com.storyteam.storytelling.util.ScriptUtils;
import java.util.List;

public class MobDropST {
   public static List<MobDropData> DRONE_DROP_1 = List.of(
      new MobDropData(ScriptUtils.getItem("immersiveengineering:component_electronic").getDefaultInstance(), 0.5F, 1, 2),
      new MobDropData(ScriptUtils.getItem("immersiveengineering:component_electronic_adv").getDefaultInstance(), 0.2F, 1, 1),
      new MobDropData(ScriptUtils.getItem("moremekanismprocessing:crystal_electrotine").getDefaultInstance(), 0.02F, 1, 1),
      new MobDropData(ScriptUtils.getItem("enderio:iron_gear").getDefaultInstance(), 0.2F, 1, 3),
      new MobDropData(ScriptUtils.getItem("chemlib:platinum_nugget").getDefaultInstance(), 0.5F, 2, 10),
      new MobDropData(ScriptUtils.getItem("chemlib:osmium_nugget").getDefaultInstance(), 0.5F, 2, 10),
      new MobDropData(ScriptUtils.getItem("chemlib:cobalt_nugget").getDefaultInstance(), 0.5F, 2, 10),
      new MobDropData(ScriptUtils.getItem("mekanism:dust_gold").getDefaultInstance(), 0.5F, 2, 10),
      new MobDropData(ScriptUtils.getItem("immersiveengineering:nugget_lead").getDefaultInstance(), 0.5F, 2, 10)
   );
}
