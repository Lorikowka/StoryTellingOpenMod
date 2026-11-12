package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.annotations.Setup;

public interface PreferencesST {
   static boolean disks(ItemStack stack) {
      return stack.is(ItemTags.MUSIC_DISCS);
   }

   static boolean paper(ItemStack stack) {
      return stack.getItem().equals(Items.PAPER);
   }

   static boolean maps(ItemStack stack) {
      return stack.getItem().equals(Items.FILLED_MAP) || stack.getItem().equals(Items.MAP);
   }

   static boolean compass(ItemStack stack) {
      return stack.is(ItemTags.COMPASSES);
   }

   static boolean seeds(ItemStack stack) {
      return stack.is(net.minecraftforge.common.Tags.Items.SEEDS);
   }

   static boolean sapling(ItemStack stack) {
      return stack.is(ItemTags.SAPLINGS);
   }

   static boolean cookedMeat(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems(
         "candlelight:lasagne,candlelight:chicken,beachparty:cooked_pelican,meadow:cooked_bear_meat,nethersdelight:warped_moldy_meat,nethersdelight:grilled_strider,nethersdelight:plate_of_stuffed_hoglin_roast,nethersdelight:plate_of_stuffed_hoglin_ham,abnormals_delight:cooked_perch_slice,abnormals_delight:perch_slice,abnormals_delight:cooked_pike_slice,abnormals_delight:pike_slice,farmersdelight:shepherds_pie,farmersdelight:rice_roll_medley_block,farmersdelight:roast_chicken,farmersdelight:grilled_salmon,farmersdelight:ratatouille,delightful:venison_chops,delightful:cooked_venison_chops,delightful:deluxe_cheeseburger,delightful:cheeseburger,abnormals_delight:pike_with_beetroot,abnormals_delight:perch_with_mushrooms,candlelight:roastbeef_carrots,candlelight:beef_wellington,candlelight:pork_ribs,candlelight:broccoli_beef,candlelight:chicken_alfredo,candlelight:cooked_beef,candlelight:chicken_teriyaki,alexsmobs:cooked_moose_ribs,nethersdelight:plate_of_stuffed_hoglin_ham,nethersdelight:strider_moss_stew,nethersdelight:plate_of_stuffed_hoglin_snout,farmersdelight:shepherds_pie,farmersdelight:honey_glazed_ham,farmersdelight:roast_chicken,farmersdelight:steak_and_potatoes,farmersdelight:roasted_mutton_chops,farmersdelight:pasta_with_mutton_chop,farmersdelight:pasta_with_meatballs,farmersdelight:bacon_and_eggs,berry_good:sweet_berry_meatballs,alexsmobs:cooked_kangaroo_meat"
            .split(",")
      );
      items.add(Items.COOKED_BEEF);
      items.add(Items.COOKED_CHICKEN);
      items.add(Items.COOKED_COD);
      items.add(Items.COOKED_MUTTON);
      items.add(Items.COOKED_PORKCHOP);
      items.add(Items.COOKED_RABBIT);
      items.add(Items.COOKED_SALMON);
      return items.contains(stack.getItem());
   }

   static boolean bookAndFeather(ItemStack stack) {
      Set<Item> items = Set.of(Items.BOOK, Items.FEATHER, Items.WRITABLE_BOOK);
      return items.contains(stack.getItem());
   }

   static Function<ItemStack, Boolean> item(Item i) {
      return s -> s.getItem() == i;
   }

   static boolean tools(ItemStack stack) {
      return stack.getItem() instanceof DiggerItem;
   }

   static boolean bow(ItemStack stack) {
      return stack.getItem() instanceof BowItem;
   }

   static boolean sword(ItemStack stack) {
      return stack.getItem() instanceof SwordItem;
   }

   static boolean paintings(ItemStack stack) {
      return stack.getItem().equals(Items.PAINTING);
   }

   static boolean melon(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems("minecraft:melon".split(","));
      return items.contains(stack.getItem());
   }

   static boolean apples(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems("minecraft:apple,minecraft:golden_apple,minecraft:enchanted_golden_apple".split(","));
      return items.contains(stack.getItem());
   }

   static boolean drinks(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems(
         "beachparty:chocolate_milkshake,beachparty:coconut_milkshake,beachparty:sweetberry_milkshake,beachparty:melon_cocktail,beachparty:honey_cocktail,beachparty:pumpkin_cocktail,beachparty:cocoa_cocktail,beachparty:sweetberries_cocktail,beachparty:coconut_cocktail,vinery:jungle_white_grapejuice_bottle,vinery:jungle_red_grapejuice_bottle,vinery:taiga_white_grapejuice_bottle,vinery:taiga_red_grapejuice_bottle,vinery:savanna_white_grapejuice_bottle,vinery:savanna_red_grapejuice_bottle,vinery:red_grapejuice_wine_bottle,vinery:white_grapejuice_wine_bottle,farmersdelight:hot_cocoa,delightful:berry_matcha_latte,delightful:matcha_latte,delightful:ender_nectar,delightful:prickly_pear_juice,delightful:azalea_tea,delightful:lavender_tea,farmersdelight:melon_juice"
            .split(",")
      );
      return items.contains(stack.getItem());
   }

   static boolean alcohol(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems(
         "vinery:white_grapejuice_wine_bottle,vinery:red_grapejuice_wine_bottle,vinery:king_danis_wine,vinery:mellohi_wine,vinery:jo_special_mixture,vinery:cristel_wine,vinery:glowing_wine,vinery:creepers_crush,vinery:bottle_mojang_noir,vinery:villagers_fright,vinery:mead,vinery:cherry_wine,vinery:clark_wine,vinery:clark_wine,vinery:jellie_wine,vinery:apple_cider,vinery:apple_wine,vinery:red_wine,vinery:praetorian_wine,vinery:chenet_wine,vinery:stal_wine,vinery:kelp_cider,vinery:strad_wine,vinery:magnetic_wine,vinery:chorus_wine,vinery:aegis_wine,vinery:solaris_wine,vinery:noir_wine,vinery:bolvar_wine,vinery:eiswein,farmersdelight:apple_cider"
            .split(",")
      );
      return items.contains(stack.getItem());
   }

   static boolean ingots(ItemStack stack) {
      ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
      return loc.getPath().contains("ingot");
   }

   static boolean wool(ItemStack stack) {
      ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
      return loc.getPath().contains("wool");
   }

   static boolean flowersAndPlants(ItemStack stack) {
      return stack.getItem() instanceof BlockItem b ? b.getBlock() instanceof IPlantable : false;
   }

   static boolean cloth(ItemStack stack) {
      return stack.getItem() instanceof ArmorItem;
   }

   static boolean book(ItemStack stack) {
      return stack.getItem().equals(Items.BOOK)
         || stack.getItem().equals(Items.ENCHANTED_BOOK)
         || stack.getItem().equals(Items.WRITABLE_BOOK)
         || stack.getItem().equals(Items.WRITTEN_BOOK)
         || stack.getItem().equals(ScriptUtils.getItem("alexsmobs:animal_dictionary"))
         || stack.getItem().equals(ScriptUtils.getItem("solcarrot:food_book"));
   }

   static boolean armor(ItemStack stack) {
      return stack.getItem() instanceof ArmorItem;
   }

   static boolean weapon(ItemStack stack) {
      return stack.getItem() instanceof SwordItem || sword(stack);
   }

   static boolean weapons_without_wooden(ItemStack stack) {
      Set<Item> items = ScriptUtils.getItems(
         "minecraft:stone_sword,minecraft:golden_sword,minecraft:iron_sword,minecraft:diamond_sword,minecraft:netherite_sword,minecraft:stone_axe,minecraft:netherite_axe,minecraft:diamond_axe,minecraft:iron_axe,minecraft:golden_axe,farmersdelight:netherite_knife,farmersdelight:golden_knife,farmersdelight:diamond_knife,farmersdelight:iron_knife,nethersdelight:diamond_machete,nethersdelight:iron_machete,nethersdelight:netherite_machete,nethersdelight:golden_machete"
            .split(",")
      );
      return items.contains(stack.getItem());
   }

   @Setup
   static void setup() {
      Storytelling.LOG.info("Preference registry has set up.");
   }
}
