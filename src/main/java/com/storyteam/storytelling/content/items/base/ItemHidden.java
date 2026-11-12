package com.storyteam.storytelling.content.items.base;

import com.storyteam.storytelling.api.itf.IHiddenFromJeiItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ItemHidden extends Item implements IHiddenFromJeiItem {
   public ItemHidden(Properties pProperties) {
      super(pProperties);
   }
}
