package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ItemFrame.class})
public interface ItemFrameAccessor {
   @Invoker
   void callSetDirection(Direction var1);
}
