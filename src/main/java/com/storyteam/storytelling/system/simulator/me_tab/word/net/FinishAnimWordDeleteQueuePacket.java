package com.storyteam.storytelling.system.simulator.me_tab.word.net;

import com.storyteam.storytelling.system.simulator.me_tab.word.AnimatedWordQueue;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class FinishAnimWordDeleteQueuePacket implements IPacket {
   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      AnimatedWordQueue.getQueue().poll();
      Network.sendTo(ctx.getSender(), new FinishAnimWordClientPacket());
   }
}
