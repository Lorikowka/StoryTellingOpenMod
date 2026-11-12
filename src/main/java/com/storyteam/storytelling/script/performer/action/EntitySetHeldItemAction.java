package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.story.characters.Character;
import com.storyteam.storytelling.util.ToStringBuilder;
import java.util.function.Supplier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntitySetHeldItemAction<T extends Entity> extends EntityPerformerAction<T> {
   private final InteractionHand hand;
   private final Supplier<ItemStack> item;

   public EntitySetHeldItemAction(ScriptPerformer performer, @NotNull Character<T> character, InteractionHand hand, Supplier<ItemStack> item) {
      super(performer, character);
      this.hand = hand;
      this.item = item;
   }

   @Override
   public void tick() {
      this.getEntity().getData().setHeldItem(this.hand, this.item.get());
   }

   @Override
   public void fastForwardToStage() {
      this.getEntity().getData().setHeldItem(this.hand, this.item.get());
   }

   @Override
   public void rewindToStage() {
      this.getEntity().getData().setHeldItem(this.hand, this.item.get());
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("character", this.character).append("hand", this.hand.name()).append("item", this.item.get()).build();
   }
}
