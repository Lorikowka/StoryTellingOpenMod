package com.storyteam.storytelling.system.gating.event;

import com.storyteam.storytelling.system.gating.LockedContents;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.FluidStack;

public class GatherHiddenItemsEvent extends PlayerEvent {
   protected final LockedContents hidden = LockedContents.createMutable();

   public GatherHiddenItemsEvent(Player player) {
      super(player);
   }

   public void hide(Item item) {
      this.hidden.items().add(item.getDefaultInstance());
   }

   public void hide(ItemStack item) {
      this.hidden.items().add(item);
   }

   public void hideAllItems(Collection<ItemStack> item) {
      this.hidden.items().addAll(item);
   }

   public void hideRecipe(ResourceLocation item) {
      this.hidden.recipes().add(item);
   }

   public void hideAllRecipes(Collection<ResourceLocation> item) {
      this.hidden.recipes().addAll(item);
   }

   public void hide(Fluid item) {
      this.hidden.fluids().add(new FluidStack(item, 1000));
   }

   public void hide(FluidStack item) {
      this.hidden.fluids().add(item);
   }

   public void hideAllFluids(Collection<FluidStack> item) {
      this.hidden.fluids().addAll(item);
   }

   public static LockedContents gather(Player player) {
      GatherHiddenItemsEvent e = new GatherHiddenItemsEvent(player);
      MinecraftForge.EVENT_BUS.post(e);
      e.hidden.cleanupEmpty();
      return e.hidden.immutable();
   }
}
