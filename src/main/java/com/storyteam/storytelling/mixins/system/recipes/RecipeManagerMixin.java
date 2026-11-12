package com.storyteam.storytelling.mixins.system.recipes;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.datafixers.util.Pair;
import com.storyteam.storytelling.system.gating.IGatedRecipeManager;
import com.storyteam.storytelling.system.gating.io.LockedEntriesDB;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Implements({@Interface(
      iface = IGatedRecipeManager.class,
      prefix = "st$"
   )})
@Mixin({RecipeManager.class})
public class RecipeManagerMixin {
   @Shadow
   private Map<ResourceLocation, Recipe<?>> byName;

   @Inject(
      method = {"byKey"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void Storytelling_byKeyPatch(ResourceLocation pRecipeId, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir) {
      cir.setReturnValue(cir.getReturnValue().filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getId())));
   }

   @Inject(
      method = {"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private <C extends Container, T extends Recipe<C>> void Storytelling_getRecipeFor(
      RecipeType<T> pRecipeType, C pInventory, Level pLevel, ResourceLocation pRecipeId, CallbackInfoReturnable<Optional<Pair<ResourceLocation, T>>> cir
   ) {
      cir.setReturnValue(cir.getReturnValue().filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getFirst())));
   }

   @Inject(
      method = {"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private <C extends Container, T extends Recipe<C>> void Storytelling_getRecipeFor$2(
      RecipeType<T> pRecipeType, C pInventory, Level pLevel, CallbackInfoReturnable<Optional<T>> cir
   ) {
      cir.setReturnValue(cir.getReturnValue().filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getId())));
   }

   @ModifyReceiver(
      method = {"getRecipesFor"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/stream/Stream;sorted(Ljava/util/Comparator;)Ljava/util/stream/Stream;",
         remap = false
      )}
   )
   private <C extends Container, T extends Recipe<C>> Stream<T> Storytelling_getRecipesFor$2(Stream<T> instance, Comparator<? super T> comparator) {
      return instance.filter(r -> !LockedEntriesDB.getContent().hiddenRecipes().contains(r.getId()));
   }

   @Nullable
   public Recipe<?> st$byKeyBypassingLock(ResourceLocation id) {
      return this.byName.get(id);
   }
}
