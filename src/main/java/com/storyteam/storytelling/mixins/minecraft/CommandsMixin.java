package com.storyteam.storytelling.mixins.minecraft;

import com.mojang.brigadier.ParseResults;
import com.storyteam.storytelling.network.StoryServerHooks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Commands.class})
public class CommandsMixin {
   @Inject(
      method = {"performCommand"},
      at = {@At("HEAD")}
   )
   private void onPerformCommand(ParseResults<CommandSourceStack> pParseResults, String pCommand, CallbackInfoReturnable<Integer> cir) {
      StoryServerHooks.onCommandRun(pCommand);
   }
}
