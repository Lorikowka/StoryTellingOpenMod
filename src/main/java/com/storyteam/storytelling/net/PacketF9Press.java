package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketF9Press implements IPacket {
   public void serverExecute(PacketContext ctx) {
      IStoryServer.get().postStoryEvent(EventType.F9_PRESS, ctx.getSender());
   }
}
