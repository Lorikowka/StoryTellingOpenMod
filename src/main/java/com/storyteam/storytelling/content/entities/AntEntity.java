package com.storyteam.storytelling.content.entities;

import com.storyteam.storytelling.client.animations.AnimationsST8;
import com.storyteam.storytelling.client.core.SidedAnimationSet;
import com.storyteam.storytelling.content.entities.configurations.MovementActionType;
import com.storyteam.storytelling.net.st8.PacketEntityIdleHeadRotateAnimation;
import com.storyteam.storytelling.util.LayersList;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.zeith.hammeranims.api.animation.AnimationLocation;
import org.zeith.hammeranims.api.animation.interp.BlendMode;
import org.zeith.hammeranims.api.animsys.AnimationSystem;
import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;
import org.zeith.hammeranims.api.animsys.layer.AnimationLayer;
import org.zeith.hammerlib.net.Network;

public class AntEntity extends AnimatedEntityLikeHumanoid {
   private static final Random RANDOM = new Random();
   private static final String IDLE_HEAD_ROTATE_LAYER = "IDLE_HEAD_ROTATE";
   private int idleHeadRotateTick = 0;
   private boolean disableIdleHeadRotateAnimation = false;

   public AntEntity(EntityType<? extends AnimatedEntityLikeHumanoid> type, Level world) {
      super(type, world);
      this.idleHeadRotateTick = 1600 + RANDOM.nextInt(400);
   }

   @Override
   public SidedAnimationSet getRotateWalkingAnimation() {
      return AnimationsST8.ANT_ROTATE_WALKING;
   }

   @Override
   public void addLayers(LayersList layers) {
      super.addLayers(layers);
      layers.addLast("IDLE_HEAD_ROTATE", BlendMode.ADD, 1.0F);
   }

   @Override
   public void tick() {
      super.tick();
      AnimationSystem anims = this.getAnimationSystem();
      boolean isMoving = MovementActionType.getBySpeed((double)this.getWalkingSpeed()) != MovementActionType.IDLE;
      if (!isMoving
         && !this.disableIdleHeadRotateAnimation
         && !this.hasActiveAnimationExcluding(anims, Set.of("IDLE_HEAD_ROTATE", "EYES", "ROTATE_WALKING", "ambient"))) {
         if (this.idleHeadRotateTick-- <= 0) {
            anims.startAnimationAt("IDLE_HEAD_ROTATE", this.getIdleHeadRotateAnimation());
            this.idleHeadRotateTick = 1600 + RANDOM.nextInt(400);
         }
      } else {
         anims.stopAnimation("IDLE_HEAD_ROTATE");
         this.idleHeadRotateTick = 100 + RANDOM.nextInt(100);
      }
   }

   private boolean hasActiveAnimationExcluding(AnimationSystem anims, Set<String> excludedLayers) {
      for (String layerName : anims.getLayerNames()) {
         if (!excludedLayers.contains(layerName)) {
            AnimationLayer layer = anims.getLayer(layerName);
            if (layer != null) {
               AnimationLocation activeAnim = layer.activeAnimationLocation();
               if (activeAnim != null && !"null!null".equals(activeAnim.toString())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public ConfiguredAnimation getIdleHeadRotateAnimation() {
      return AnimationsST8.ANT_IDLE_HEAD_ROTATE.configure().transitionTime(0.0F).important();
   }

   public void setDisableIdleHeadRotateAnimation(boolean disableIdleHeadRotateAnimation) {
      this.disableIdleHeadRotateAnimation = disableIdleHeadRotateAnimation;
      if (!this.level().isClientSide()) {
         Network.sendToAll(new PacketEntityIdleHeadRotateAnimation(this.getId(), disableIdleHeadRotateAnimation));
      }
   }
}
