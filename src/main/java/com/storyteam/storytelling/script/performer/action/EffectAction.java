package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectAction extends ScriptPerformerAction {
   private final MobEffectInstance effect;

   public EffectAction(ScriptPerformer performer, MobEffectInstance effect) {
      super(performer);
      this.effect = effect;
   }

   @Override
   public void tick() {
      this.forceAddEffect();
   }

   @Override
   public void fastForwardToStage() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (this.effect.getDuration() >= 32767) {
         this.forceAddEffect();
      } else {
         player.removeEffect(this.effect.getEffect());
      }
   }

   @Override
   public void rewindToStage() {
      this.removeEffect();
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("effect", this.effect).build();
   }

   private void removeEffect() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         player.removeEffect(this.effect.getEffect());
      }
   }

   private void forceAddEffect() {
      ServerPlayer player = ScriptUtils.getPlayer();
      if (player != null) {
         player.forceAddEffect(new MobEffectInstance(this.effect), player);
      }
   }
}
