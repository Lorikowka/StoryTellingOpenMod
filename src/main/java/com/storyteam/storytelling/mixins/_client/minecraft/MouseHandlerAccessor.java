package com.storyteam.storytelling.mixins._client.minecraft;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MouseHandler.class})
public interface MouseHandlerAccessor {
   @Accessor
   double getXpos();

   @Accessor
   void setXpos(double var1);

   @Accessor
   double getYpos();

   @Accessor
   void setYpos(double var1);
}
