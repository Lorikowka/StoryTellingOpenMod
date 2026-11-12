package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.net.PacketEntityEyeBlinkAnimation;
import com.storyteam.storytelling.util.LayersList;
import java.util.Random;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammerlib.net.Network;

public class AnimatedEntityWithBlink extends AnimatedEntity {
   private boolean disableEyeBlinkAnimation = false;
   private static final Random BLINK_RANDOM = new Random();
   private int blinkTick = 0;

   public AnimatedEntityWithBlink(EntityType<? extends AnimatedEntity> type, Level world) {
      super(type, world);
   }

   public void setDisableEyeBlinkAnimation(boolean disableEyeBlinkAnimation) {
      this.disableEyeBlinkAnimation = disableEyeBlinkAnimation;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketEntityEyeBlinkAnimation(this.getId(), disableEyeBlinkAnimation));
      }
   }

   @Override
   public void tick() {
      super.tick();
      this.manageEyes();
   }

   @Override
   public void addLayers(LayersList layers) {
      layers.addLast("EYES", BlendMode.ADD, 1.0F);
   }

   private void manageEyes() {
      AnimationSystem anims = this.getAnimationSystem();
      if (!this.level().isClientSide) {
         if (this.disableEyeBlinkAnimation || anims.getLayer("EYES") == null) {
            anims.stopAnimation("EYES");
         } else if (this.blinkTick == 0) {
            anims.startAnimationAt("EYES", Animations.BLINK_ENDANIMATED.configure().transitionTime(0.0F).important());
            this.blinkTick = 50 + BLINK_RANDOM.nextInt(4) * 20;
         } else {
            this.blinkTick--;
         }
      }
   }
}
