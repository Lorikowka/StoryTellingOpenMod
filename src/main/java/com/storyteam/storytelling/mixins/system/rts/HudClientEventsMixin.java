package com.storyteam.storytelling.mixins.system.rts;

import com.solegendary.reignofnether.hud.HudClientEvents;
import com.storyteam.storytelling.client.content.overlays.MessageOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent.Render.Post;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HudClientEvents.class})
public class HudClientEventsMixin {
   @Inject(
      method = {"onDrawScreen"},
      at = {@At("RETURN")},
      remap = false
   )
   private static void onDrawScreen(Post evt, CallbackInfo ci) {
      if (Minecraft.getInstance().gui instanceof ForgeGui forgeGui) {
         MessageOverlay.renderMessageOverlay(forgeGui, evt.getGuiGraphics(), evt.getPartialTick(), evt.getScreen().width, evt.getScreen().height);
      }
   }
}
