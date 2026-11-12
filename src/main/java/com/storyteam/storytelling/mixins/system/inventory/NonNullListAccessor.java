package com.storyteam.storytelling.mixins.system.inventory;

import java.util.List;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({NonNullList.class})
public interface NonNullListAccessor<E> {
   @Mutable
   @Accessor
   List<E> getList();

   @Mutable
   @Accessor
   void setList(List<E> var1);
}
