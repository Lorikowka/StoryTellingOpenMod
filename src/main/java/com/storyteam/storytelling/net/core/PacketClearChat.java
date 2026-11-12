package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.client.content.overlays.MessageOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketClearChat implements IPacket {
   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().gui.getChat().clearMessages(false);
      MessageOverlay.ticker = 0;
      MessageOverlay.currentMessage = null;
   }
}
