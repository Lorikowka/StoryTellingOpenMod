package com.storyteam.storytelling.system.simulator.me_tab.fragment.utils;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import com.storyteam.storytelling.system.simulator.cognition_tab.Cognitions;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.call_context.ActionContext;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.call_context.CallContext;
import com.storyteam.storytelling.util.ScriptUtils;

public class FragmentUtils {
   public static int fragmentModificationAddEidos(int count) {
      ActionContext ctx = CallContext.get();
      int lvl = CognitionHelper.getTier(ScriptUtils.getPlayer(), Cognitions.CALCULATIVENESS);

      int adjust = switch (lvl) {
         case 1 -> 15;
         case 2, 3 -> 100;
         case 4 -> 200;
         default -> 0;
      };
      if (ctx == ActionContext.FRAGMENT_REJECT_ACTION || ctx == ActionContext.FRAGMENT_ACCEPT_ACTION && lvl >= 3) {
         count += adjust;
      }

      if (ctx == ActionContext.FRAGMENT_ROLLBACK_REJECT_ACTION || ctx == ActionContext.FRAGMENT_ROLLBACK_ACCEPT_ACTION && lvl >= 3) {
         count -= adjust;
      }

      return count;
   }
}
