package com.storyteam.storytelling.mixins.hammeranims;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.zeith.hammeranims.core.client.ClientHammerHooks;

@Mixin({ClientHammerHooks.class})
public class ClientHammerHooksMixin {
   @Redirect(
      method = {"lambda$clientTick$0"},
      at = @At(
         value = "INVOKE",
         target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"
      )
   )
   private static void warnRedirect(Logger logger, String message, Object p0) {
   }
}
