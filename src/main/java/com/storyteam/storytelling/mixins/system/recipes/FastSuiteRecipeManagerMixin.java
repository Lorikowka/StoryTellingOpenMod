package com.storyteam.storytelling.mixins.system.recipes;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.storyteam.storytelling.system.gating.io.LockedEntriesDB;
import dev.shadowsoffire.fastsuite.AuxRecipeManager;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AuxRecipeManager.class})
public class FastSuiteRecipeManagerMixin {
   @Inject(
      method = {"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private <C extends Container, T extends Recipe<C>> void Storytelling_getRecipeFor$2(
      RecipeType<T> pRecipeType, C pInventory, Level pLevel, CallbackInfoReturnable<Optional<T>> cir
   ) {
      cir.setReturnValue(((Optional)cir.getReturnValue()).filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getId())));
   }

   @ModifyReceiver(
      method = {"lambda$getRecipesFor$5"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/stream/Stream;sorted(Ljava/util/Comparator;)Ljava/util/stream/Stream;",
         remap = false
      )}
   )
   private <C extends Container, T extends Recipe<C>> Stream<T> Storytelling_getRecipesFor$2(Stream<T> instance, Comparator<? super T> comparator) {
      return instance.filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getId()));
   }
}
