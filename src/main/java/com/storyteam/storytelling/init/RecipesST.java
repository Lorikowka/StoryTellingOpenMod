package com.storyteam.storytelling.init;

import com.storyteam.storytelling.St;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.core.adapter.recipe.ShapedRecipeBuilder;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.event.recipe.SpoofRecipesEvent;

@ProvideRecipes
public class RecipesST implements IRecipeProvider {
   public static ResourceLocation TEST_RECIPE = St.id("test_recipe");

   public void provideRecipes(RegisterRecipesEvent event) {
      ((ShapedRecipeBuilder)((ShapedRecipeBuilder)event.shaped().id(TEST_RECIPE))
            .shape(new String[]{"grt", "sks", "grt"})
            .map('g', Items.GOLD_NUGGET)
            .map('r', Items.REDSTONE)
            .map('t', Blocks.TORCH.asItem())
            .map('s', Blocks.GLASS.asItem())
            .map('k', Blocks.STONE.asItem())
            .result(new ItemStack(Blocks.GLOWSTONE.asItem(), 3)))
         .registerIf(() -> false);
   }

   public void spoofRecipes(SpoofRecipesEvent event) {
   }
}
