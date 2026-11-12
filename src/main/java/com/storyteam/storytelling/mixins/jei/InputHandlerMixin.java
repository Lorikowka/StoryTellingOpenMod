package com.storyteam.storytelling.mixins.jei;

import com.storyteam.storytelling.events.base.RenderJEIEvent;
import java.util.Optional;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"mezz.jei.gui.elements.GuiIconButton$UserInputHandler"}
)
public class InputHandlerMixin {
   @Inject(
      method = {"handleUserInput"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private void handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
      if (MinecraftForge.EVENT_BUS.post(new RenderJEIEvent())) {
         cir.setReturnValue(Optional.empty());
      }
   }
}
