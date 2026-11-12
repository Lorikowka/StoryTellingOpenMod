package com.storyteam.storytelling.system.simulator.cognition_tab.network;

import com.storyteam.storytelling.system.simulator.cognition_tab.CognitionPoseConfig;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CToggleEditorMod implements IPacket {
   public void clientExecute(PacketContext ctx) {
      CognitionPoseConfig.editorMod = !CognitionPoseConfig.editorMod;
   }
}
