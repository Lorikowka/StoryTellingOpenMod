package com.storyteam.storytelling.system.shop.init;

import com.storyteam.storytelling.init.ItemsST;
import com.storyteam.storytelling.script.story.characters.Characters;
import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.ShopData;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import com.storyteam.storytelling.util.UtilST;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.annotations.RegistryName;

public class ShopsST {
   private static final AtomicBoolean INITIALIZED = new AtomicBoolean();
   public static final Map<String, ShopInfo> SHOPS = new LinkedHashMap<>();
   public static final UnaryOperator<Style> DARK_GRAY_ITALIC = s -> s.withItalic(true).withColor(ChatFormatting.DARK_GRAY);
   @RegistryName("test")
   public static final ShopInfo TEST = ShopInfo.builder()
      .character(Characters.ZEITHERON)
      .lot(
         LotInfo.builder()
            .cost(100)
            .count(2)
            .stack((Supplier<ItemStack>)(() -> ItemsST.STORYTELLING.getDefaultInstance()))
            .additiveMultiplier(1.3F)
            .type(LotType.BUY)
            .build()
      )
      .buyLot("<item:storytelling:debug>", 1, 1500)
      .build();

   public static ItemStack antiquify(String item, String name, String description) {
      ItemStack antique = UtilST.parseCraftTweakerItem(item + ".withTag({display:{Lore:['[{\"text\":\"" + description + "\",\"color\":\"#aa9248\"}]']}})");
      antique.setHoverName(Component.literal(name).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(15190119)).withItalic(false)));
      return antique;
   }

   public static ItemStack rename(String item, String name, String description) {
      ItemStack antique = UtilST.parseCraftTweakerItem(item + ".withTag({display:{Lore:['[{\"text\":\"" + description + "\",\"color\":\"#aa9248\"}]']}})");
      antique.setHoverName(Component.literal(name).withStyle(Style.EMPTY.withItalic(false)));
      return antique;
   }

   public static void init() {
      try {
         if (!INITIALIZED.getAndSet(true)) {
            for (Field f : ShopsST.class.getDeclaredFields()) {
               if (f.isAnnotationPresent(RegistryName.class)) {
                  String name = ((RegistryName)f.getAnnotation(RegistryName.class)).value();
                  ShopInfo shop = (ShopInfo)f.get(null);
                  shop.setId(name);
                  SHOPS.put(shop.getId(), shop);
                  ShopStorage.SHOPS.put(shop.getId(), new ShopData(shop));
               }
            }
         }
      } catch (Throwable var6) {
         if (var6 instanceof RuntimeException runtime) throw runtime;
         if (var6 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var6);
      }
   }
}
