package com.storyteam.storytelling.system.clothes.item;

import com.storyteam.storytelling.system.clothes.ArmorGeometryConfiguration;
import com.storyteam.storytelling.system.clothes.VanitySet;
import com.storyteam.storytelling.system.clothes.entity.VanitySlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item.Properties;

public interface IVanityArmorItemFactory {
   IVanityArmorItemFactory DEFAULT = ItemVanityArmor::new;

   ItemVanityArmor create(VanitySet var1, ArmorMaterial var2, VanitySlot var3, Properties var4, String var5, ArmorGeometryConfiguration var6);
}
