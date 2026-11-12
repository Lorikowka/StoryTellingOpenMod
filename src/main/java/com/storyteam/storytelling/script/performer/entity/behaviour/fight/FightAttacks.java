package com.storyteam.storytelling.script.performer.entity.behaviour.fight;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.script.performer.entity.behaviour.fight.chain.StagedAttackChainBuilder;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FightAttacks {
   public static <T extends AnimatedEntity> StagedAttackChainBuilder<T> createStagedFight() {
      return new StagedAttackChainBuilder<>();
   }

   public static class Flags {
      public static final String VULNERABLE = "default_flags$VULNERABLE";
      public static final String IN_BLOCK = "default_flags$IN_BLOCK";
      public static final String CHARGED = "default_flags$CHARGED";
      public static final String ATTACK = "default_flags$ATTACK";
      public static final String STUN = "default_flags$STUN";
      public static final String BLOCK = "default_flags$BLOCK";
   }
}
