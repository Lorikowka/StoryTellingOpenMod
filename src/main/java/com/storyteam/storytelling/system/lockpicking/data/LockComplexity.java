package com.storyteam.storytelling.system.lockpicking.data;

import com.storyteam.storytelling.system.lockpicking.InitLockPicking;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.world.item.Item;

public enum LockComplexity {
   BRONZE("Новичок", 9334346, () -> InitLockPicking.BRONZE_LOCK),
   SILVER("Бывалый", 10860727, () -> InitLockPicking.SILVER_LOCK),
   GOLD("Мастер", 16242770, () -> InitLockPicking.GOLD_LOCK),
   DIAMOND("Эксперт", 4776415, () -> InitLockPicking.DIAMOND_LOCK),
   IMPOSSIBLE("Невозможно", 16737860, () -> InitLockPicking.DIAMOND_LOCK);

   public final String title;
   public final int color;
   public final Supplier<Item> display;

   @Generated
   private LockComplexity(String title, int color, Supplier<Item> display) {
      this.title = title;
      this.color = color;
      this.display = display;
   }
}
