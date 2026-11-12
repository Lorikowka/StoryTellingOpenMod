package com.storyteam.storytelling;

import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.init.items.VanityArmorST;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.api.items.CreativeTab.RegisterTab;

public interface TabsST {
   @RegisterTab
   CreativeTab ITEM_GROUP = new CreativeTab(St.id("root"), b -> b.icon(ItemsST.STORYTELLING::getDefaultInstance));
   @RegisterTab
   CreativeTab BLOCKITEMS = new CreativeTab(St.id("blockitems"), b -> b.icon(BlocksST.BOXES.asItem()::getDefaultInstance));
   @RegisterTab
   CreativeTab VANITY_TAB = new CreativeTab(St.id("clothing"), b -> b.icon(VanityArmorST.LOLOWEAR.get(VanitySlot.HEAD)::getDefaultInstance));
}
