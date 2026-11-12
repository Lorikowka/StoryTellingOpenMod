package com.storyteam.storytelling.api.entity;

import net.minecraft.world.entity.item.ItemEntity;
import org.zeith.hammerlib.util.java.Cast;

public interface IStoryItemEntity {
   String UNMERGEABLE_TAG_KEY = "Storytelling_UnmergeableItem";

   static IStoryItemEntity of(ItemEntity ie) {
      return (IStoryItemEntity)Cast.cast(ie);
   }

   default IStoryItemEntity setUnmergeable(boolean unmergeable) {
      this.noStory().getPersistentData().putBoolean("Storytelling_UnmergeableItem", unmergeable);
      return this;
   }

   default boolean isUnmergeable() {
      return this.noStory().getPersistentData().getBoolean("Storytelling_UnmergeableItem");
   }

   default ItemEntity noStory() {
      return (ItemEntity)Cast.cast(this);
   }
}
