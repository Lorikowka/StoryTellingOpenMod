package com.storyteam.storytelling.system.health;

import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import net.minecraft.world.entity.player.Player;

@SystemRoot("health")
public class HealthSystem implements ISystem {
   public static HealthSystem INSTANCE;
   private final int maxHealth = 20;
   private final int absoluteMaxHealth = 20;

   public HealthSystem() {
      INSTANCE = this;
   }

   public float getCurrentHealth(Player player) {
      return player.getHealth();
   }

   public int getMaxHealth(Player player) {
      PlayerMorph morph = ((IAnimatedPlayer)player).getMorph();
      return !morph.equals(MorphsST8.SPOODER) && !morph.equals(MorphsST8.HEAD) ? 20 : 7;
   }

   public int getAbsoluteMaxHealth(Player player) {
      return 20;
   }
}
