package com.storyteam.storytelling.events;

import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class FeedAnimalEvent extends LivingEvent {
   final Player feeder;
   final ItemStack food;

   public FeedAnimalEvent(LivingEntity entity, Player feeder, ItemStack food) {
      super(entity);
      this.feeder = feeder;
      this.food = food;
   }

   @Generated
   public Player getFeeder() {
      return this.feeder;
   }

   @Generated
   public ItemStack getFood() {
      return this.food;
   }
}
