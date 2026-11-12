package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Entity.class})
public interface EntityAccessor {
   @Accessor
   void setEyeHeight(float var1);

   @Accessor
   EntityInLevelCallback getLevelCallback();
}
