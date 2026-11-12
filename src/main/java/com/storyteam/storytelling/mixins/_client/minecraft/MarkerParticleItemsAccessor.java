package com.storyteam.storytelling.mixins._client.minecraft;

import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientLevel.class})
public interface MarkerParticleItemsAccessor {
   @Accessor("MARKER_PARTICLE_ITEMS")
   Set<Item> getMarkerParticles();
}
