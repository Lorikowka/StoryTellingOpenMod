package com.storyteam.storytelling.system.gating.io;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public record LockedContentCache(Set<Item> hiddenItems, Set<ResourceLocation> hiddenRecipes) {
}
