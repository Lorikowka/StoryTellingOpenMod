package com.storyteam.storytelling.init;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.api.annotations.client.WithScreen;
import com.storyteam.storytelling.content.items.ItemMissing;
import com.storyteam.storytelling.content.items.base.ItemHidden;
import com.storyteam.storytelling.content.items.base.ItemWithGui;
import com.storyteam.storytelling.system.simulator.world_tab.ui.WorldMainTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import org.zeith.hammerlib.annotations.Ref;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   creativeTabs = {@Ref(
      value = TabsST.class,
      field = "ITEM_GROUP"
   )}
)
public interface ItemsST {
   @RegistryName("missing")
   ItemMissing MISSING = new ItemMissing(props());
   @RegistryName("storytelling")
   Item STORYTELLING = hiddenMaterial();
   @RegistryName("world_map")
   @WithScreen(WorldMainTab.class)
   Item WORLD_MAP = new ItemWithGui(props(), Component.empty());
   @RegistryName("key")
   Item KEY_ITEM = new Item(props());
   @RegistryName("versiform")
   Item VERSIFORM_RESOURCE = new Item(props().stacksTo(200));
   @RegistryName("ether")
   Item ETHER_RESOURCE = new Item(props().stacksTo(200));
   @RegistryName("rice")
   Item RICE_RESOURCE = new Item(props().stacksTo(200));
   @RegistryName("eidos_item")
   Item EIDOS_RESOURCE = new Item(props().stacksTo(1));

   static Properties props() {
      return new Properties();
   }

   static Item material() {
      return new Item(props());
   }

   static Item hiddenMaterial() {
      return new ItemHidden(props());
   }
}
