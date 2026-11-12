package com.storyteam.storytelling.system.reputation.events;

import com.storyteam.storytelling.script.performer.builder.ScriptPerformerBuilder;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.reputation.gift.GiftCategory;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SelectGiftPerformerEvent extends Event {
   private final Character<?> character;
   private final ItemStack stack;
   private final Player player;
   private final GiftCategory category;
   private Consumer<ScriptPerformerBuilder> performer;

   public boolean is(Ingredient ingredient) {
      return ingredient != null && ingredient.test(this.stack);
   }

   @Generated
   public Character<?> getCharacter() {
      return this.character;
   }

   @Generated
   public ItemStack getStack() {
      return this.stack;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public GiftCategory getCategory() {
      return this.category;
   }

   @Generated
   public Consumer<ScriptPerformerBuilder> getPerformer() {
      return this.performer;
   }

   @Generated
   public void setPerformer(Consumer<ScriptPerformerBuilder> performer) {
      this.performer = performer;
   }

   @Generated
   public String toString() {
      return "SelectGiftPerformerEvent(character="
         + this.getCharacter()
         + ", stack="
         + this.getStack()
         + ", player="
         + this.getPlayer()
         + ", category="
         + this.getCategory()
         + ", performer="
         + this.getPerformer()
         + ")";
   }

   @Generated
   public SelectGiftPerformerEvent(Character<?> character, ItemStack stack, Player player, GiftCategory category, Consumer<ScriptPerformerBuilder> performer) {
      this.character = character;
      this.stack = stack;
      this.player = player;
      this.category = category;
      this.performer = performer;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SelectGiftPerformerEvent other)) {
         return false;
      } else if (!other.canEqual(this)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         Object this$character = this.getCharacter();
         Object other$character = other.getCharacter();
         if (this$character == null ? other$character == null : this$character.equals(other$character)) {
            Object this$stack = this.getStack();
            Object other$stack = other.getStack();
            if (this$stack == null ? other$stack == null : this$stack.equals(other$stack)) {
               Object this$player = this.getPlayer();
               Object other$player = other.getPlayer();
               if (this$player == null ? other$player == null : this$player.equals(other$player)) {
                  Object this$category = this.getCategory();
                  Object other$category = other.getCategory();
                  if (this$category == null ? other$category == null : this$category.equals(other$category)) {
                     Object this$performer = this.getPerformer();
                     Object other$performer = other.getPerformer();
                     return this$performer == null ? other$performer == null : this$performer.equals(other$performer);
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof SelectGiftPerformerEvent;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = super.hashCode();
      Object $character = this.getCharacter();
      result = result * 59 + ($character == null ? 43 : $character.hashCode());
      Object $stack = this.getStack();
      result = result * 59 + ($stack == null ? 43 : $stack.hashCode());
      Object $player = this.getPlayer();
      result = result * 59 + ($player == null ? 43 : $player.hashCode());
      Object $category = this.getCategory();
      result = result * 59 + ($category == null ? 43 : $category.hashCode());
      Object $performer = this.getPerformer();
      return result * 59 + ($performer == null ? 43 : $performer.hashCode());
   }
}
