package com.storyteam.storytelling.system.gating.jei;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.network.ReflectionUtil;
import com.storyteam.storytelling.system.gating.LockedContents;
import com.storyteam.storytelling.system.gating.event.NotifyHiddenItemsEvent;
import com.storyteam.storytelling.system.gating.event.RequestLockedItemUpdateEvent;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.library.recipes.RecipeManager;
import mezz.jei.library.recipes.RecipeManagerInternal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

@JeiPlugin
public class JeiCompat implements IModPlugin, IJeiContext {
   public static final ResourceLocation ID = St.id("hidden_stuff");
   protected IJeiRuntime runtime;
   protected LockedContents lockedStuff = LockedContents.NONE;

   public JeiCompat() {
      Storytelling.BUS.addListener(this::updateHiddenList);
      IJeiContext.Instance.context = this;
   }

   public ResourceLocation getPluginUid() {
      return ID;
   }

   public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
      this.runtime = jeiRuntime;
      RequestLockedItemUpdateEvent.request();
   }

   public void onRuntimeUnavailable() {
      this.runtime = null;
      this.lockedStuff = LockedContents.NONE;
   }

   private void updateHiddenList(NotifyHiddenItemsEvent e) {
      if (this.runtime != null) {
         LockedContents prev = this.lockedStuff;
         LockedContents cur = e.getCurrent();
         this.updateHidden(VanillaTypes.ITEM_STACK, prev.items(), cur.items());
         this.updateHidden(ForgeTypes.FLUID_STACK, prev.fluids(), cur.fluids());
         this.runtime.getJeiHelpers().getAllRecipeTypes().forEach(type -> this.updateHiddenRecipes(type, prev.recipes(), cur.recipes()));
         this.lockedStuff = cur;
      }
   }

   private <T> void updateHidden(IIngredientType<T> type, Collection<T> unhide, Collection<T> hide) {
      IIngredientManager mgr = this.runtime.getIngredientManager();
      if (!unhide.isEmpty()) {
         mgr.addIngredientsAtRuntime(type, unhide);
      }

      if (!hide.isEmpty()) {
         mgr.removeIngredientsAtRuntime(type, hide);
      }
   }

   private <T> void updateHiddenRecipes(RecipeType<T> type, Set<ResourceLocation> unhide, Collection<ResourceLocation> hide) {
      if (Recipe.class.isAssignableFrom(type.getRecipeClass())) {
         IRecipeManager manager = this.runtime.getRecipeManager();
         Map<ResourceLocation, T> recipes = new HashMap<>();
         this.gatherAndOperateOnRecipes(type, true, all -> all.forEach(r -> recipes.put(((Recipe)r).getId(), (T)r)));
         List<T> unhideRecipes = unhide.stream().map(recipes::get).filter(Objects::nonNull).toList();
         if (!unhideRecipes.isEmpty()) {
            manager.unhideRecipes(type, unhideRecipes);
         }

         List<T> hideRecipes = hide.stream().map(recipes::get).filter(Objects::nonNull).toList();
         if (!hideRecipes.isEmpty()) {
            manager.hideRecipes(type, hideRecipes);
         }
      }
   }

   private <T> void gatherAndOperateOnRecipes(RecipeType<T> type, boolean includeHidden, Consumer<Stream<T>> handler) {
      try {
         Field internals = ReflectionUtil.lookupField(RecipeManager.class, RecipeManagerInternal.class);
         RecipeManagerInternal o = (RecipeManagerInternal)internals.get(this.runtime.getRecipeManager());
         handler.accept(o.getRecipesStream(type, this.runtime.getJeiHelpers().getFocusFactory().getEmptyFocusGroup(), includeHidden));
      } catch (Throwable var6) {
         throw var6;
      }
   }
}
