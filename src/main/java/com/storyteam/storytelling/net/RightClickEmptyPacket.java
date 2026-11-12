package com.storyteam.storytelling.net;

import com.storyteam.storytelling.events.base.ServerEmptyRightClickEvent;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class RightClickEmptyPacket implements IPacket {
   public void serverExecute(PacketContext ctx) {
      MinecraftForge.EVENT_BUS.post(new ServerEmptyRightClickEvent(ctx.getSender()));
   }
}
