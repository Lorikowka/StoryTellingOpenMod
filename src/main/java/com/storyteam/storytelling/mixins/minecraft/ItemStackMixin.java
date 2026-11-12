package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.util.mc.STClientForgeHooks;
import java.util.function.Function;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(
   value = {ItemStack.class},
   remap = false
)
public class ItemStackMixin {
   @ModifyVariable(
      method = {"onItemUse"},
      at = @At("HEAD"),
      index = 2,
      argsOnly = true
   )
   private Function<UseOnContext, InteractionResult> ST_onItemUse(Function<UseOnContext, InteractionResult> value) {
      return ctx -> STClientForgeHooks.onPlaceItemIntoWorld(ctx, value);
   }
}
