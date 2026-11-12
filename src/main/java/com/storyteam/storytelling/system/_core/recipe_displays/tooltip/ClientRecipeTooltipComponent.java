package com.storyteam.storytelling.system._core.recipe_displays.tooltip;

import com.storyteam.storytelling.system._core.recipe_displays.RecipeDrawManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.util.math.Point;

public class ClientRecipeTooltipComponent implements ClientTooltipComponent {
   final GuiObject gui;
   final Component text;

   public ClientRecipeTooltipComponent(RecipeTooltipComponent component) {
      GuiObject gui = RecipeDrawManager.createRecipeView("recipe_" + component.id(), component.layout(), component.recipe());
      this.gui = gui != null ? gui : new GuiObject("recipe_" + component.id());
      this.text = component.text();
   }

   public int getHeight() {
      return (int)Math.ceil((double)this.gui.getScaledHeight());
   }

   public int getWidth(Font pFont) {
      return (int)Math.ceil((double)this.gui.getScaledWidth());
   }

   public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
      this.gui.pos((float)pX + Math.max(0.0F, ((float)pFont.width(this.text) - this.gui.getScaledWidth()) / 2.0F), (float)pY);
      this.gui.renderObject(new Graphics(pGuiGraphics, Minecraft.getInstance(), Minecraft.getInstance().getPartialTick(), false), Point.ZERO);
   }
}
