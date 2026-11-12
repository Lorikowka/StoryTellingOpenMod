package com.storyteam.storytelling.system.exp.render;

import com.storyteam.storytelling.system._core.st_cutscene.ClientsideCutsceneHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ExpOverlay implements IGuiOverlay {
   public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
      if (!Minecraft.getInstance().options.hideGui) {
         if (ClientsideCutsceneHandler.currentData == null) {
            ;
         }
      }
   }
}
