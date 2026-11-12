package com.storyteam.storytelling.system.emotions.events;

import com.storyteam.storytelling.system.emotions.EmotionRenderData;
import com.storyteam.storytelling.system.emotions.RenderHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class EmotionsOverlay implements IGuiOverlay {
   public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      for (EmotionRenderData renderData : RenderHolder.getRenderedMap()) {
         if (renderData.getUuid().equals("player")) {
            if (renderData.getY().length == 0) {
               float[] y = new float[]{(float)screenHeight, (float)screenHeight, (float)screenHeight};
               renderData.setY(y);
            }

            renderData.render(gui, guiGraphics, partialTick, screenWidth, screenHeight);
         }
      }
   }
}
