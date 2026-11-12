package com.storyteam.storytelling.system._core.recipe_displays.impl;

import com.storyteam.storytelling.system._core.recipe_displays.IRecipeDrawer;
import com.storyteam.storytelling.system._core.recipe_displays.RecipeDrawer;
import com.storyteam.storytelling.system._core.recipe_displays.layout.RecipeDrawLayout;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiItemObject;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.math.Point;

@RecipeDrawer
class ShapedRecipeDrawer implements IRecipeDrawer<CraftingRecipe> {
   @NotNull
   @Override
   public RecipeType<CraftingRecipe> getType() {
      return RecipeType.CRAFTING;
   }

   public boolean accepts(CraftingRecipe recipe) {
      return recipe instanceof IShapedRecipe;
   }

   @Nullable
   public GuiObject create(String name, RecipeDrawLayout layout, CraftingRecipe recipe) {
      if (!(recipe instanceof IShapedRecipe<?> sr)) {
         return null;
      } else {
         int w = sr.getRecipeWidth();
         int h = sr.getRecipeHeight();
         NonNullList items = sr.getIngredients();
         GuiObject texture = (GuiObject)(layout.background() != null
            ? GuiObject.create(name).fullImage(layout.background(), layout.width(), layout.height())
            : GuiObject.create(name).empty().size(layout.width(), layout.height()));
         ClientPacketListener con = Minecraft.getInstance().getConnection();
         RegistryAccess regs = (RegistryAccess)(con != null ? con.registryAccess() : RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
         Point outPos = layout.outputSlots().get(0);
         texture.addChild(
            new GuiItemObject("output", () -> sr.getResultItem(regs)).hoverable(true).provideIngredient(true).pos(outPos.x(), outPos.y()).pivotAtCenter()
         );
         List<Point> inputs = layout.inputSlots();

         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               int i = x + y * w;
               if (i >= items.size() || i >= inputs.size()) {
                  return texture;
               }

               Ingredient ing = (Ingredient)items.get(i);
               Point inPos = inputs.get(i);
               texture.addChild(
                  new GuiItemObject("input_" + x + "_" + y, () -> RecipeHelper.cycleIngredientStack(ing, 1000L))
                     .hoverable(true)
                     .provideIngredient(true)
                     .pos(inPos.x(), inPos.y())
                     .pivotAtCenter()
               );
            }
         }

         return texture;
      }
   }
}
