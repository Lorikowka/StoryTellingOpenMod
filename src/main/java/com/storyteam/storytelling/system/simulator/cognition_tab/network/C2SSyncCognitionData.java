package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionHelper;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class C2SSyncCognitionData implements IPacket {
   public void serverExecute(PacketContext ctx) {
      CognitionHelper.syncAllData(ctx.getSender());
      CognitionHelper.syncCustomReques(ctx.getSender());
   }
}
