package com.storyteam.storytelling.mixins.journeymap;

import com.storyteam.storytelling.events.base.MinimapTPEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.mcf.Resources;

@Pseudo
@Mixin(
   targets = {"journeymap/client/command/CmdTeleportWaypoint"},
   remap = false
)
public class CmdTeleportWaypointMixin {
   @Inject(
      method = {"isPermitted"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void isPermitted(Minecraft mc, CallbackInfoReturnable<Boolean> cir) {
      MinimapTPEvent event = new MinimapTPEvent();
      MinecraftForge.EVENT_BUS.post(event);
      if (event.isCanceled()) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = {"teleport(DIDLjava/lang/String;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void teleport_storytelling(double x, int y, double z, String dim, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (MinecraftForge.EVENT_BUS.post(new MinimapTPEvent.ToLocation(mc.player, Resources.location(dim), x, (double)y, z))) {
         ci.cancel();
      }
   }
}
