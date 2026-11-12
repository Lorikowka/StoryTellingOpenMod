package com.storyteam.storytelling.mixins.forge;

import net.minecraftforge.registries.MissingMappingsEvent.Action;
import net.minecraftforge.registries.MissingMappingsEvent.Mapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Mapping.class})
public interface MissingMappingsEventEntryAccessor<T> {
   @Accessor
   T getTarget();

   @Accessor
   Action getAction();
}
