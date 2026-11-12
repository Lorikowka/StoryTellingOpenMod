package com.storyteam.storytelling.system._core.recipe_displays;

import com.storyteam.storytelling.system._core.recipe_displays.layout.RecipeDrawLayout;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.flowgui.GuiObject;

public interface IRecipeDrawer<T extends Recipe<?>> {
   @NotNull
   RecipeType<T> getType();

   boolean accepts(T var1);

   @Nullable
   GuiObject create(String var1, RecipeDrawLayout var2, T var3);
}
