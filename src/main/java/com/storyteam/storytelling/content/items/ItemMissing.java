package com.storyteam.storytelling.content.items;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.api.itf.IHiddenFromJeiItem;
import com.storyteam.storytelling.init.ItemsST;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import org.zeith.hammerlib.api.items.ITabItem;

public class ItemMissing extends Item implements IHiddenFromJeiItem, ITabItem {
   private static final Set<ResourceLocation> missingIds = new HashSet<>();

   public ItemMissing(Properties props) {
      super(props);
   }

   public void fillItemCategory(CreativeModeTab tab, Set<ItemStack> items) {
      if (tab != CreativeModeTabs.searchTab() && this.allowedIn(tab)) {
         List<ResourceLocation> rls = new ArrayList<>(missingIds);
         rls.sort(Comparator.comparing(ResourceLocation::toString));

         for (ResourceLocation id : rls) {
            items.add(createMissing(id, 1));
         }
      }
   }

   @Override
   public void getHiddenItems(Consumer<ItemStack> items) {
      List<ResourceLocation> rls = new ArrayList<>(missingIds);
      rls.sort(Comparator.comparing(ResourceLocation::toString));

      for (ResourceLocation id : rls) {
         items.accept(createMissing(id, 1));
      }
   }

   public static ItemStack createMissing(ResourceLocation id, int count) {
      missingIds.add(id);
      ItemStack s = new ItemStack(ItemsST.MISSING, count);
      s.getOrCreateTag().putString("Id", Util.makeDescriptionId("item", id));
      return s;
   }

   public Component getName(ItemStack s) {
      return (Component)(s.hasTag() && s.getTag().contains("Id", 8) ? Component.translatable(s.getTag().getString("Id")) : super.getName(s));
   }

   public CreativeModeTab getItemCategory() {
      return TabsST.ITEM_GROUP.tab();
   }
}
