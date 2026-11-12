package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.events.base.F8PressEvent;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketF8Press implements IPacket {
   public void serverExecute(PacketContext ctx) {
      MinecraftForge.EVENT_BUS.post(new F8PressEvent(ctx.getSender()));
      IStoryServer.get().postStoryEvent(EventType.F8_PRESS, ctx.getSender());
   }
}
