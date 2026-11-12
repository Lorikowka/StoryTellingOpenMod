package com.storyteam.storytelling.system.fight.mob_drop;

import com.storyteam.storytelling.util.ScriptUtils;
import java.util.Random;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

public class MobDropData {
   private final ItemStack stack;
   private final float dropChance;
   private final int minCount;
   private final int maxCount;

   public MobDropData(ItemStack stack, float dropChance, int minCount, int maxCount) {
      this.stack = stack;
      this.dropChance = dropChance;
      this.minCount = minCount;
      this.maxCount = maxCount;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public float getDropChance() {
      return this.dropChance;
   }

   public int getMinCount() {
      return this.minCount;
   }

   public int getMaxCount() {
      return this.maxCount;
   }

   public boolean shouldDrop(Random random) {
      float dropChance = this.dropChance;
      if (ScriptUtils.getPlayer() != null && ScriptUtils.getPlayer().hasEffect(MobEffects.LUCK)) {
         dropChance *= 1.8F;
      }

      return random.nextFloat() <= dropChance;
   }

   public int getRandomCount(Random random) {
      int extraCount = 0;
      float dropChance = this.dropChance;
      if (ScriptUtils.getPlayer() != null && ScriptUtils.getPlayer().hasEffect(MobEffects.LUCK)) {
         dropChance *= 1.8F;
      }

      for (int i = 0; i < this.maxCount - this.minCount; i++) {
         if (random.nextFloat() <= dropChance) {
            extraCount++;
         }
      }

      return this.minCount + extraCount;
   }
}
