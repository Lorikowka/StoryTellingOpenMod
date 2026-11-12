package com.storyteam.storytelling.system._core.recipe_displays;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.storyteam.storytelling.system._core.recipe_displays.layout.RecipeLayoutCollection;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.util.Optional;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.core.scans.base.DataScanner;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.util.java.Cast;

public class RecipeDrawManager {
   private static Multimap<RecipeType<?>, IRecipeDrawer<?>> REGISTRY = ArrayListMultimap.create();

   @Nullable
   public static <T extends Recipe<?>> GuiObject createRecipeView(String name, RecipeLayoutCollection layout, T recipe) {
      return getDrawer(recipe).map(d -> d.create(name, layout.layouts().get(recipe.getType()), recipe)).orElse(null);
   }

   public static <T extends Recipe<?>> Optional<IRecipeDrawer<T>> getDrawer(T recipe) {
      if (recipe == null) {
         return Optional.empty();
      } else {
         RecipeType<?> rt = recipe.getType();
         IRecipeDrawer<T> type = (IRecipeDrawer<T>)Cast.cast(REGISTRY.get(rt).stream().map(d -> (IRecipeDrawer<T>) d).filter(d -> d.accepts(recipe)).findFirst().orElse(null));
         return type != null && type.getType() != rt ? Optional.empty() : Optional.ofNullable(type);
      }
   }

   private static void doScan() {
      DataScanner ds = DataScanner.start();
      ds.add(IAnnotationScanListener.forAnnotation(RecipeDrawer.class, ElementType.TYPE, data -> {
         Class<? extends IRecipeDrawer> c = data.getOwnerClass().asSubclass(IRecipeDrawer.class);

         try {
            Constructor<? extends IRecipeDrawer> con = c.getDeclaredConstructor();
            if (con.trySetAccessible()) {
               IRecipeDrawer<?> drawer = (IRecipeDrawer<?>)con.newInstance();
               REGISTRY.put(drawer.getType(), drawer);
            }
         } catch (ReflectiveOperationException var4) {
            throw new RuntimeException(var4);
         }
      }));
      DataScanner.finish(ds);
   }

   static {
      IProxy.runSided(() -> RecipeDrawManager::doScan, () -> () -> {
         });
   }
}
