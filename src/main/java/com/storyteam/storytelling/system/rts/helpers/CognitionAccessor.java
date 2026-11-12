package com.storyteam.storytelling.system.rts.helpers;

import com.storyteam.storytelling.system.simulator.cognition_tab.Cognition;
import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CognitionAccessor {
   private static boolean player() {
      return Minecraft.getInstance().player != null;
   }

   public static boolean isCardResearched(Cognition cognition) {
      return player() && CognitionHelper.isCardResearched(Minecraft.getInstance().player, cognition);
   }

   public static boolean isCardLevelReached(Cognition cognition, int level) {
      return player() && CognitionHelper.getTier(Minecraft.getInstance().player, cognition) >= level;
   }
}
