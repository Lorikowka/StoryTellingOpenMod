package com.storyteam.storytelling.system._core.morph.client;

import lombok.Generated;
import net.minecraft.client.player.Input;

public class BlockedInput extends Input {
   private final Input backing;

   public void tick(boolean p_234115_, float p_234116_) {
      this.leftImpulse = 0.0F;
      this.forwardImpulse = 0.0F;
      this.up = false;
      this.down = false;
      this.left = false;
      this.right = false;
      this.jumping = false;
      this.shiftKeyDown = false;
   }

   @Generated
   public Input getBacking() {
      return this.backing;
   }

   @Generated
   public BlockedInput(Input backing) {
      this.backing = backing;
   }
}
