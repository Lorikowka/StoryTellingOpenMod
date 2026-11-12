package com.storyteam.storytelling.init.seasonal;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.texture.Material;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.system._core.morph.MorphSizeHandler;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import java.util.List;
import net.minecraft.world.entity.EntityDimensions;
import org.zeith.hammeranims.api.animation.LoopMode;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface MorphsST8 {
   @RegistryName("spooder")
   PlayerMorph SPOODER = PlayerMorph.builder()
      .disableVanillaAnimations()
      .renderLayers(false)
      .model(EntityModelsST.SPIDER_HEAD_LOLOLOSHKA)
      .entitySize(
         MorphSizeHandler.useState(
            props -> EntityDimensions.scalable(0.6F, props.get(MorphSizeHandler.K_SHIFTING) ? 0.7F : 0.9F), List.of(MorphSizeHandler.PLAYER_SNEAKING)
         )
      )
      .eyeHeight((player, pose) -> {
         float var10000;
         if (false) {
            var10000 = 1.62F;
         } else {
            switch (pose) {
               case SWIMMING:
               case FALL_FLYING:
               case SPIN_ATTACK:
                  var10000 = 0.4F;
                  break;
               case CROUCHING:
                  var10000 = 0.41F;
                  break;
               default:
                  var10000 = 0.52F;
            }
         }

         return var10000;
      })
      .withAnimations(
         b -> b.animation("ambient", AnimationWithCondition.idle(Animations.SPIDER_HEAD_IDLE))
               .addAnimation(AnimationWithCondition.walking(Animations.SPIDER_HEAD_WALK.reconfigure(c -> c.speed(0.4F)), 0.01, 0.08))
               .addAnimation(AnimationWithCondition.walking(Animations.SPIDER_HEAD_WALK.reconfigure(c -> c.speed(0.9F)), 0.08, 0.13))
               .addAnimation(AnimationWithCondition.walking(Animations.SPIDER_HEAD_WALK.reconfigure(c -> c.speed(1.1F)), 0.13, 10.0))
               .addAnimation(AnimationWithCondition.running(Animations.SPIDER_HEAD_WALK.reconfigure(c -> c.speed(1.5F))))
               .addAnimation(AnimationWithCondition.sneaking(Animations.SPIDER_HEAD_SHIFT))
               .addAnimation(AnimationWithCondition.jumpStart(Animations.SPIDER_HEAD_JUMP))
               .addAnimation(AnimationWithCondition.jumpEnd(Animations.SPIDER_HEAD_LAND))
               .addAnimation(AnimationWithCondition.swing(Animations.SPIDER_HEAD_ATTACK.reconfigure(c -> c.transitionTime(0.05F).loopMode(LoopMode.ONCE))))
               .addAnimation(AnimationWithCondition.sneaking(Animations.SPIDER_HEAD_SHIFT))
               .addAnimation(AnimationWithCondition.death(Animations.SPIDER_HEAD_DEATH))
               .addAnimation(AnimationWithCondition.hurt(Animations.SPIDER_HEAD_DAMAGE))
      )
      .material(Material.builder().albedo(St.tx("entity/spider_head_lololoshka")).build())
      .build();
   @RegistryName("head")
   PlayerMorph HEAD = PlayerMorph.builder()
      .disableVanillaAnimations()
      .renderLayers(false)
      .model(EntityModelsST.HEAD_LOLOLOSHKA_PLAYER)
      .entitySize(MorphSizeHandler.of(0.6F, 0.5F))
      .eyeHeight((player, pose) -> 0.43F)
      .material(Material.builder().albedo(St.tx("entity/head_lololoshka")).build())
      .build();
}
