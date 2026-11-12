package com.storyteam.storytelling.system._core.recipe_displays;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("recipe_display")
public class RecipeDisplaySystem implements ISystem {
   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      modBus.addListener(ClientEvents::registerComponents);
   }
}
