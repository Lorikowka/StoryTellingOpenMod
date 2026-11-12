package com.storyteam.storytelling.system.gating;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public record LockedContents(boolean mutable, Set<ItemStack> items, Set<FluidStack> fluids, Set<ResourceLocation> recipes) {
   public static LockedContents NONE = new LockedContents(false, Set.of(), Set.of(), Set.of());

   public boolean isEmpty() {
      return this == NONE || this.items.isEmpty() && this.fluids.isEmpty() && this.recipes.isEmpty();
   }

   public LockedContents immutable() {
      if (this.isEmpty()) {
         return NONE;
      } else {
         return !this.mutable() ? this : new LockedContents(false, Set.copyOf(this.items), Set.copyOf(this.fluids), Set.copyOf(this.recipes));
      }
   }

   public void cleanupEmpty() {
      this.items.removeIf(ItemStack::isEmpty);
      this.fluids.removeIf(FluidStack::isEmpty);
      this.recipes.removeIf(Objects::isNull);
   }

   public static LockedContents createMutable() {
      return new LockedContents(true, new HashSet<>(), new HashSet<>(), new HashSet<>());
   }
}
