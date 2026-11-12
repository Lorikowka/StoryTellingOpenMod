package com.storyteam.storytelling.script.performer.entity.behaviour.movement;

import com.storyteam.storytelling.script.performer.entity.behaviour.IStoryBehaviour;
import net.minecraft.world.entity.Entity;

public interface ISpeedyBehavior<T extends Entity> extends IStoryBehaviour<T> {
   double getSpeed();
}
