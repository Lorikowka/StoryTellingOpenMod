package com.storyteam.storytelling.compat.jei;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.itf.IHiddenFromJeiItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class STJeiPlugin implements IModPlugin {
   public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
      IIngredientManager ings = jeiRuntime.getIngredientManager();
      ings.removeIngredientsAtRuntime(
         VanillaTypes.ITEM_STACK, ForgeRegistries.ITEMS.getValues().stream().map(IHiddenFromJeiItem::get).mapMulti(IHiddenFromJeiItem::getHiddenItems).toList()
      );
   }

   public ResourceLocation getPluginUid() {
      return St.id("plugin");
   }
}
