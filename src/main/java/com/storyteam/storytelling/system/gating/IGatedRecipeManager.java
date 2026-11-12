package com.storyteam.storytelling.system.gating;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

public interface IGatedRecipeManager {
   @Nullable
   Recipe<?> byKeyBypassingLock(ResourceLocation var1);

   static Recipe<?> byId(RecipeManager recipes, ResourceLocation id) {
      return ((IGatedRecipeManager)recipes).byKeyBypassingLock(id);
   }
}
