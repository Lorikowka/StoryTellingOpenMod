package com.storyteam.storytelling.system.reputation.events;

import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.system.reputation.gift.GiftCategory;
import lombok.Generated;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class GiftValidationEvent extends Event {
   private final Character<?> character;
   private final ItemStack stack;
   private final Player player;
   private GiftCategory category;

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
   public void setCategory(GiftCategory category) {
      this.category = category;
   }

   @Generated
   public String toString() {
      return "GiftValidationEvent(character="
         + this.getCharacter()
         + ", stack="
         + this.getStack()
         + ", player="
         + this.getPlayer()
         + ", category="
         + this.getCategory()
         + ")";
   }

   @Generated
   public GiftValidationEvent(Character<?> character, ItemStack stack, Player player, GiftCategory category) {
      this.character = character;
      this.stack = stack;
      this.player = player;
      this.category = category;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GiftValidationEvent other)) {
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
                  return this$category == null ? other$category == null : this$category.equals(other$category);
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
      return other instanceof GiftValidationEvent;
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
      return result * 59 + ($category == null ? 43 : $category.hashCode());
   }
}
