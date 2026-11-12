package com.storyteam.storytelling.system.lockpicking;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.system.gating.LockedEntry;
import com.storyteam.storytelling.system.lockpicking.items.ItemKey;
import com.storyteam.storytelling.system.lockpicking.items.ItemLockpick;
import com.storyteam.storytelling.util.ScriptUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.zeith.hammerlib.annotations.Ref;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.core.adapter.recipe.ShapedRecipeBuilder;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@SimplyRegister(
   prefix = "system/lockpicking/",
   creativeTabs = {@Ref(
      value = TabsST.class,
      field = "ITEM_GROUP"
   )}
)
public interface InitLockPicking {
   ResourceLocation LOCKPICK_RECIPE = St.id("lockpick_recipe");
   @RegistryName("lockpick")
   Item LOCKPICK = new ItemLockpick(ItemsST.props().stacksTo(16).rarity(Rarity.UNCOMMON));
   @RegistryName("key")
   ItemKey KEY = new ItemKey(ItemsST.props().stacksTo(1).rarity(Rarity.RARE));
   @RegistryName("bronze_lock")
   Item BRONZE_LOCK = ItemsST.material();
   @RegistryName("silver_lock")
   Item SILVER_LOCK = ItemsST.material();
   @RegistryName("gold_lock")
   Item GOLD_LOCK = ItemsST.material();
   @RegistryName("diamond_lock")
   Item DIAMOND_LOCK = ItemsST.material();
   @RegistryName("lockpick")
   LockedEntry LAWCKPEECK = LockedEntry.builder().item(LOCKPICK).recipe(LOCKPICK_RECIPE).unlockedByDefault(false).build();

   static void recipes(RegisterRecipesEvent event) {
      ((ShapedRecipeBuilder)((ShapedRecipeBuilder)event.shaped().id(LOCKPICK_RECIPE))
            .shape(new String[]{"  L", " I ", "I  "})
            .map('I', Items.IRON_INGOT)
            .map('L', ScriptUtils.getItemLike("immersiveengineering:ingot_lead"))
            .result(LOCKPICK, 1))
         .register();
   }
}
