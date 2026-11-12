package com.storyteam.storytelling.mixins._client.minecraft;

import com.storyteam.storytelling.system.inventory.net.PacketOpenInventoryMenuSB;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.zeith.hammerlib.net.Network;

@Mixin({Minecraft.class})
public abstract class MinecraftMixin {
   @Shadow
   @Nullable
   public LocalPlayer player;

   @Redirect(
      method = {"handleKeybinds"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;sendOpenInventory()V"
      )
   )
   private void sendOpenInventoryRedirect(LocalPlayer instance) {
      if (!instance.isCreative() && !instance.isSpectator()) {
         Network.sendToServer(new PacketOpenInventoryMenuSB());
      } else {
         instance.sendOpenInventory();
      }
   }

   @Redirect(
      method = {"handleKeybinds"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
      )
   )
   private void setScreenRedirect(Minecraft instance, Screen screen) {
      if (this.player != null) {
         if (screen instanceof InventoryScreen && !this.player.isCreative() && !this.player.isSpectator()) {
            Network.sendToServer(new PacketOpenInventoryMenuSB());
         } else {
            instance.setScreen(screen);
         }
      }
   }
}
