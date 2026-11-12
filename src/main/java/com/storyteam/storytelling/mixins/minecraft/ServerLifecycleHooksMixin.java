package com.storyteam.storytelling.mixins.minecraft;

import com.storyteam.storytelling.network.StoryServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {ServerLifecycleHooks.class},
   remap = false
)
public class ServerLifecycleHooksMixin {
   @Inject(
      method = {"handleExit"},
      at = {@At("HEAD")}
   )
   private static void catchCrashReportSummary(int retVal, CallbackInfo ci) {
      StoryServer.getInstance().disconnect();
   }
}
