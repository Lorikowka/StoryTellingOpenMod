package com.storyteam.storytelling.system.fight.mob_drop;

import java.util.List;
import java.util.Random;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MobDropUtil {
   private static final Random random = new Random();

   public static void handleDrops(Level level, Vec3 pos, List<MobDropData> mobDropList) {
      if (mobDropList != null) {
         for (MobDropData drop : mobDropList) {
            if (drop.shouldDrop(random)) {
               int count = drop.getRandomCount(random);
               ItemStack stack = drop.getStack().copy();
               stack.setCount(count);
               ItemEntity itemEntity = new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack);
               itemEntity.setDeltaMovement(
                  (random.nextDouble() * 0.2F - 0.1F) * 2.0, random.nextDouble() * 0.2 * 2.0, (random.nextDouble() * 0.2F - 0.1F) * 2.0
               );
               itemEntity.setPickUpDelay(5);
               level.addFreshEntity(new ItemEntity(level, pos.x(), pos.y(), pos.z(), stack));
            }
         }
      }
   }
}
