package com.storyteam.storytelling.system._core.recipe_displays.tooltip;

import com.storyteam.storytelling.system._core.recipe_displays.layout.RecipeLayoutCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.Recipe;

public record RecipeTooltipComponent(Recipe<?> recipe, String id, Component text, RecipeLayoutCollection layout) implements TooltipComponent {
}
