package com.storyteam.storytelling.system.reputation;

import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system._core.character_data.CharacterDataManager;
import com.storyteam.storytelling.system.reputation.events.GiftValidationEvent;
import com.storyteam.storytelling.system.reputation.events.ReputationChangedEvent;
import com.storyteam.storytelling.system.reputation.gift.GiftCategory;
import com.storyteam.storytelling.system.reputation.gift.GiftInfo;
import com.storyteam.storytelling.system.reputation.preferences.CharacterPreferences;
import com.storyteam.storytelling.system.reputation.preferences.PreferenceRegistry;
import com.storyteam.storytelling.system.reputation.util.ChangeReason;
import java.util.function.Function;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.util.mcf.Resources;

public class ReputationManager {
   public static final int MAX_REPUTATION = 100;

   public static void deleteForCharacter(Character<?> character) {
      CharacterDataManager.deleteAttachment(character, "reputation");
   }

   public static CharacterReputation getOrEnableForCharacter(Character<?> character) {
      return CharacterDataManager.getOrCreateAttachment(character, "reputation", CharacterReputation::new);
   }

   public static CharacterReputation forCharacter(Character<?> character) {
      return CharacterDataManager.getExistingAttachment(character, "reputation", CharacterReputation::new);
   }

   public static int getReputation(Character<?> character) {
      CharacterReputation data = forCharacter(character);
      return data == null ? -1 : data.getReputation();
   }

   public static int getBaselineReputation(Character<?> character) {
      CharacterReputation data = forCharacter(character);
      return data == null ? -1 : data.getBaseline();
   }

   public static boolean setBaselineReputation(Character<?> character, int amount) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         data.setBaseline(Mth.clamp(amount, 0, 100));
         return true;
      }
   }

   public static boolean addReputation(String modifierId, Character<?> character, int amount) {
      return addReputation(modifierId, character, amount, ChangeReason.EXTERNAL);
   }

   public static boolean revokeReputation(String modifierId, Character<?> character) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         if (data.getReputationModifiers().removeInt(modifierId) != 0) {
            data.setTotalDirty(true);
         }

         return true;
      }
   }

   public static boolean addReputation(String modifierId, Character<?> character, int amount, ChangeReason reason) {
      return addReputation(modifierId, character, amount, reason, true);
   }

   public static boolean addReputation(String modifierId, Character<?> character, int amount, ChangeReason reason, boolean sendNotification) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         ReputationChangedEvent event = new ReputationChangedEvent(amount, reason, modifierId, sendNotification);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCanceled()) {
            return false;
         } else {
            amount = event.getAmount();
            if (amount != 0 && event.shouldSendNotification()) {
               sendNotification(amount, character);
            }

            data.getReputationModifiers().put(modifierId, amount);
            data.setTotalDirty(true);
            return true;
         }
      }
   }

   public static boolean addBaselineReputation(Character<?> character, int amount) {
      return addBaselineReputation(character, amount, ChangeReason.EXTERNAL);
   }

   public static boolean addBaselineReputation(Character<?> character, int amount, ChangeReason reason) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         ReputationChangedEvent event = new ReputationChangedEvent(amount, reason, null, true);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCanceled()) {
            return false;
         } else {
            amount = event.getAmount();
            if (amount != 0) {
               sendNotification(amount, character);
            }

            return setBaselineReputation(character, getBaselineReputation(character) + amount);
         }
      }
   }

   public static void sendNotification(int amount, Character<?> character) {
   }

   public static GiftCategory getGiftCategory(Character<?> character, String item) {
      return getGiftCategory(character, (Item)ForgeRegistries.ITEMS.getValue(Resources.location(item)));
   }

   public static GiftCategory getGiftCategory(Player player, Character<?> character, ItemStack item) {
      try {
         if (item.isEmpty()) {
            return GiftCategory.IGNORED;
         } else {
            GiftCategory cat = getGiftCategory(character, item.getItem());
            GiftValidationEvent e = new GiftValidationEvent(character, item, player, cat);
            MinecraftForge.EVENT_BUS.post(e);
            return e.getCategory();
         }
      } catch (RuntimeException var5) {
         CrashReport report = new CrashReport(
            "Failed to determine gift category for item "
               + ForgeRegistries.ITEMS.getKey(item.getItem())
               + " while interacting with Character "
               + character.getReferenceId()
               + " ("
               + character.getName()
               + ")",
            var5
         );
         Minecraft.getInstance().delayCrash(report);
         throw var5;
      }
   }

   public static GiftCategory getGiftCategory(Character<?> character, Item item) {
      CharacterPreferences info = PreferenceRegistry.get(character);
      GiftCategory category = GiftCategory.NEUTRAL;
      if (info == null) {
         return category;
      } else {
         for (Function<ItemStack, GiftInfo> fun : info.getGiftPredicates()) {
            GiftCategory entry = fun.apply(item.getDefaultInstance()).getCategory();
            if (entry != GiftCategory.NEUTRAL) {
               category = entry;
            }
         }

         return category;
      }
   }

   public static boolean addGift(Character<?> character, Item item) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         data.addGift(item);
         return true;
      }
   }

   public static boolean removeGift(Character<?> character, Item item) {
      CharacterReputation data = forCharacter(character);
      if (data == null) {
         return false;
      } else {
         data.removeGift(item);
         return true;
      }
   }

   public static boolean hasGift(Character<?> character, Item item) {
      CharacterReputation data = forCharacter(character);
      return data == null ? false : data.hasGift(item);
   }
}
