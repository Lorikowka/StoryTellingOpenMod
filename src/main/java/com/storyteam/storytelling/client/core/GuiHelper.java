package com.storyteam.storytelling.client.core;

import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiTooltipObject;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;

public class GuiHelper {
   public static GuiTooltipObject dynSizeTooltip(Tooltip tooltip) {
      GuiTooltipObject gto = new GuiTooltipObject("$tooltip").tooltip(tooltip);
      gto.onPreRender((partialTime, mouse) -> {
         GuiObject obj = gto.getParent();
         if (obj != null) {
            gto.size(obj.getUnscaledWidth(), obj.getUnscaledHeight());
         }
      });
      return gto;
   }
}
