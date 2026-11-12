package com.storyteam.storytelling.system.simulator.me_tab.word.net;

import com.storyteam.storytelling.system.simulator.me_tab.word.client.WordOverlay;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class FinishAnimWordClientPacket implements IPacket {
   public void clientExecute(PacketContext ctx) {
      WordOverlay.clearWordData();
   }
}
