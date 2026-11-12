package com.storyteam.storytelling.mixins._client.minecraft;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public abstract class MarkerParticleItemsMixin {
   @Shadow
   @Final
   @Mutable
   private static Set<Item> MARKER_PARTICLE_ITEMS;

   @Inject(
      method = {"<clinit>"},
      at = {@At("TAIL")}
   )
   private static void markerItemsModifiable(CallbackInfo ci) {
      Set<Item> prev = MARKER_PARTICLE_ITEMS;
      MARKER_PARTICLE_ITEMS = ConcurrentHashMap.newKeySet(prev.size());
      MARKER_PARTICLE_ITEMS.addAll(prev);
   }
}
