package com.storyteam.storytelling.init.system;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.api.animations.AnimationWithCondition;
import com.storyteam.storytelling.client.Animations;
import com.storyteam.storytelling.client.texture.Material;
import com.storyteam.storytelling.init.geo.EntityModelsST;
import com.storyteam.storytelling.init.seasonal.MorphsST8;
import com.storyteam.storytelling.system._core.morph.MorphSizeHandler;
import com.storyteam.storytelling.system._core.morph.PlayerMorph;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface MorphsST extends MorphsST8 {
   @RegistryName("none")
   PlayerMorph NONE = PlayerMorph.builder().build();
   @RegistryName("zeith")
   PlayerMorph ZEITH = PlayerMorph.builder()
      .disableVanillaAnimations()
      .renderLayers(false)
      .model(EntityModelsST.ZEITHERON_SERGAL)
      .entitySize(
         MorphSizeHandler.useState(
            props -> EntityDimensions.scalable(0.6F, props.get(MorphSizeHandler.K_SHIFTING) ? 1.5F : 1.8F), List.of(MorphSizeHandler.PLAYER_SNEAKING)
         )
      )
      .withAnimations(
         b -> b.animation("ambient", AnimationWithCondition.always(Animations.HUMANOID_IDLE))
               .addAnimation(AnimationWithCondition.walking(Animations.WALK_NEW, 0.05, 0.15))
               .addAnimation(AnimationWithCondition.running(Animations.RUN_NEW))
               .addAnimation(AnimationWithCondition.sneaking(Animations.HUMANOID_SNEAK))
               .addAnimation(AnimationWithCondition.climbing(Animations.HUMANOID_CLIMB))
               .addAnimation(AnimationWithCondition.jumpStart(Animations.JUMP_START))
               .animation("TailIdle", AnimationWithCondition.always(Animations.ZEITH_TAIL_IDLE.reconfigure(c -> c.speed(0.25F))))
               .addAnimation(
                  AnimationWithCondition.<Player>builder()
                     .activeWhile(Entity::isShiftKeyDown)
                     .animation(Animations.ZEITH_TAIL_IDLE.reconfigure(c -> c.speed(2.0F).transitionTime(0.5F)))
                     .build()
               )
      )
      .material(Material.builder().albedo(St.tx("entity/st_team/zeitheron_sergal")).emission(St.tx("entity/st_team/zeitheron_sergal_emissive")).build())
      .build();
}
