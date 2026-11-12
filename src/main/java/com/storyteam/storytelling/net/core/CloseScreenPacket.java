package com.storyteam.storytelling.net.core;

import net.minecraft.client.Minecraft;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class CloseScreenPacket implements IPacket {
   public void clientExecute(PacketContext ctx) {
      if (Minecraft.getInstance().screen != null) {
         Minecraft.getInstance().screen.onClose();
      }

      Minecraft.getInstance().setScreen(null);
   }
}
