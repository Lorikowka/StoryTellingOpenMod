package com.storyteam.storytelling.events.base;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class RenderToroHealthEvent extends LivingEvent {
   public RenderToroHealthEvent(LivingEntity entity) {
      super(entity);
   }
}
