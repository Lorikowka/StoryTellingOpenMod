package com.storyteam.storytelling.net;

import com.storyteam.storytelling.system._core.st_cutscene.ServersideCutsceneHandler;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class StopTDCutscenePacket implements IPacket {
   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      ServersideCutsceneHandler.stopCamera();
   }
}
