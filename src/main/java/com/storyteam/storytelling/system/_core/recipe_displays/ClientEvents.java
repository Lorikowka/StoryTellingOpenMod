package com.storyteam.storytelling.system._core.recipe_displays;

import com.storyteam.storytelling.system._core.recipe_displays.tooltip.ClientRecipeTooltipComponent;
import com.storyteam.storytelling.system._core.recipe_displays.tooltip.RecipeTooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

class ClientEvents {
   static void registerComponents(RegisterClientTooltipComponentFactoriesEvent event) {
      event.register(RecipeTooltipComponent.class, ClientRecipeTooltipComponent::new);
   }
}
