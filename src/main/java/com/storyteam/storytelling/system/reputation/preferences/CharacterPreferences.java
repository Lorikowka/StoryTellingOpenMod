package com.storyteam.storytelling.system.reputation.preferences;

import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.system.reputation.events.SelectGiftPerformerEvent;
import com.storyteam.storytelling.system.reputation.gift.GiftCategory;
import com.storyteam.storytelling.system.reputation.gift.GiftInfo;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;

public class CharacterPreferences implements ICustomRegistrar {
   private List<Function<ItemStack, GiftInfo>> giftPredicates;
   private Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> giftReplies;
   private List<Consumer<SelectGiftPerformerEvent>> giftSelectorListeners;
   private boolean isHidden;

   public void performRegister(RegisterEvent registerEvent, ResourceLocation resourceLocation) {
   }

   @Generated
   private static List<Function<ItemStack, GiftInfo>> $default$giftPredicates() {
      return null;
   }

   @Generated
   private static Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> $default$giftReplies() {
      return null;
   }

   @Generated
   private static List<Consumer<SelectGiftPerformerEvent>> $default$giftSelectorListeners() {
      return List.of();
   }

   @Generated
   private static boolean $default$isHidden() {
      return false;
   }

   @Generated
   CharacterPreferences(
      List<Function<ItemStack, GiftInfo>> giftPredicates,
      Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> giftReplies,
      List<Consumer<SelectGiftPerformerEvent>> giftSelectorListeners,
      boolean isHidden
   ) {
      this.giftPredicates = giftPredicates;
      this.giftReplies = giftReplies;
      this.giftSelectorListeners = giftSelectorListeners;
      this.isHidden = isHidden;
   }

   @Generated
   public static CharacterPreferences.PreferencesBuilder builder() {
      return new CharacterPreferences.PreferencesBuilder();
   }

   @Generated
   public List<Function<ItemStack, GiftInfo>> getGiftPredicates() {
      return this.giftPredicates;
   }

   @Generated
   public Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> getGiftReplies() {
      return this.giftReplies;
   }

   @Generated
   public List<Consumer<SelectGiftPerformerEvent>> getGiftSelectorListeners() {
      return this.giftSelectorListeners;
   }

   @Generated
   public boolean isHidden() {
      return this.isHidden;
   }

   public static class PreferencesBuilder {
      @Generated
      private boolean giftPredicates$set;
      @Generated
      private List<Function<ItemStack, GiftInfo>> giftPredicates$value;
      @Generated
      private boolean giftReplies$set;
      @Generated
      private Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> giftReplies$value;
      @Generated
      private boolean giftSelectorListeners$set;
      @Generated
      private List<Consumer<SelectGiftPerformerEvent>> giftSelectorListeners$value;
      @Generated
      private boolean isHidden$set;
      @Generated
      private boolean isHidden$value;

      public CharacterPreferences.PreferencesBuilder hide() {
         this.isHidden(true);
         return this;
      }

      private List<Function<ItemStack, GiftInfo>> getGiftPredicates() {
         if (!this.giftPredicates$set || this.giftPredicates$value == null) {
            this.giftPredicates(new ArrayList<>());
         }

         return this.giftPredicates$value;
      }

      private Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> getGiftReplies() {
         if (!this.giftReplies$set || this.giftReplies$value == null) {
            this.giftReplies(new HashMap<>());
         }

         return this.giftReplies$value;
      }

      private List<Consumer<SelectGiftPerformerEvent>> getGiftSelectorListeners() {
         if (!this.giftSelectorListeners$set || this.giftSelectorListeners$value == null) {
            this.giftSelectorListeners(new ArrayList<>());
         }

         return this.giftSelectorListeners$value;
      }

      public CharacterPreferences.PreferencesBuilder giftSelectorListener(Predicate<ItemStack> filter, String... replyMessageChain) {
         return this.giftSelectorListener(filter, sp -> {
            int i = 0;

            for (int l = replyMessageChain.length; i < l; i++) {
               String m = replyMessageChain[i];
               sp.thenSpeechMessage(0, m, i != l - 1);
            }
         });
      }

      public CharacterPreferences.PreferencesBuilder giftSelectorListener(Predicate<ItemStack> filter, Consumer<ScriptPerformerBuilder> builder) {
         return this.giftSelectorListener(evt -> {
            if (filter.test(evt.getStack())) {
               evt.setPerformer(builder);
            }
         });
      }

      public CharacterPreferences.PreferencesBuilder giftSelectorListener(Consumer<SelectGiftPerformerEvent> listener) {
         this.getGiftSelectorListeners().add(listener);
         return this;
      }

      public CharacterPreferences.PreferencesBuilder giftReply(GiftCategory category, Consumer<ScriptPerformerBuilder> reply) {
         this.getGiftReplies().computeIfAbsent(category, __ -> new ArrayList<>()).add(reply);
         return this;
      }

      public CharacterPreferences.PreferencesBuilder giftReply(GiftCategory category, String... replyMessageChain) {
         return this.giftReply(category, sp -> {
            int i = 0;

            for (int l = replyMessageChain.length; i < l; i++) {
               String m = replyMessageChain[i];
               sp.thenSpeechMessage(0, m, i != l - 1);
            }
         });
      }

      public CharacterPreferences.PreferencesBuilder giftPredicate(Function<ItemStack, GiftInfo> predicate) {
         this.getGiftPredicates().add(predicate);
         return this;
      }

      public CharacterPreferences.PreferencesBuilder giftPredicate(Function<ItemStack, Boolean> predicate, GiftCategory category) {
         this.getGiftPredicates().add(entry -> new GiftInfo(predicate.apply(entry) ? category : GiftCategory.NEUTRAL));
         return this;
      }

      public CharacterPreferences.PreferencesBuilder giftPredicate(String id, GiftCategory category) {
         this.getGiftPredicates().add(entry -> new GiftInfo(ScriptUtils.getItem(id).equals(entry.getItem()) ? category : GiftCategory.NEUTRAL));
         return this;
      }

      @Generated
      PreferencesBuilder() {
      }

      @Generated
      public CharacterPreferences.PreferencesBuilder giftPredicates(List<Function<ItemStack, GiftInfo>> giftPredicates) {
         this.giftPredicates$value = giftPredicates;
         this.giftPredicates$set = true;
         return this;
      }

      @Generated
      public CharacterPreferences.PreferencesBuilder giftReplies(Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> giftReplies) {
         this.giftReplies$value = giftReplies;
         this.giftReplies$set = true;
         return this;
      }

      @Generated
      public CharacterPreferences.PreferencesBuilder giftSelectorListeners(List<Consumer<SelectGiftPerformerEvent>> giftSelectorListeners) {
         this.giftSelectorListeners$value = giftSelectorListeners;
         this.giftSelectorListeners$set = true;
         return this;
      }

      @Generated
      public CharacterPreferences.PreferencesBuilder isHidden(boolean isHidden) {
         this.isHidden$value = isHidden;
         this.isHidden$set = true;
         return this;
      }

      @Generated
      public CharacterPreferences build() {
         List<Function<ItemStack, GiftInfo>> giftPredicates$value = this.giftPredicates$value;
         if (!this.giftPredicates$set) {
            giftPredicates$value = CharacterPreferences.$default$giftPredicates();
         }

         Map<GiftCategory, List<Consumer<ScriptPerformerBuilder>>> giftReplies$value = this.giftReplies$value;
         if (!this.giftReplies$set) {
            giftReplies$value = CharacterPreferences.$default$giftReplies();
         }

         List<Consumer<SelectGiftPerformerEvent>> giftSelectorListeners$value = this.giftSelectorListeners$value;
         if (!this.giftSelectorListeners$set) {
            giftSelectorListeners$value = CharacterPreferences.$default$giftSelectorListeners();
         }

         boolean isHidden$value = this.isHidden$value;
         if (!this.isHidden$set) {
            isHidden$value = CharacterPreferences.$default$isHidden();
         }

         return new CharacterPreferences(giftPredicates$value, giftReplies$value, giftSelectorListeners$value, isHidden$value);
      }

      @Generated
      @Override
      public String toString() {
         return "CharacterPreferences.PreferencesBuilder(giftPredicates$value="
            + this.giftPredicates$value
            + ", giftReplies$value="
            + this.giftReplies$value
            + ", giftSelectorListeners$value="
            + this.giftSelectorListeners$value
            + ", isHidden$value="
            + this.isHidden$value
            + ")";
      }
   }
}
