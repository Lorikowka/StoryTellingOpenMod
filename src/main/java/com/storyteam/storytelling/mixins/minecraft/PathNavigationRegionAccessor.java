package com.storyteam.storytelling.mixins.minecraft;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({PathNavigationRegion.class})
public interface PathNavigationRegionAccessor {
   @Accessor
   Level getLevel();
}
