package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class StopScriptPacket extends BasePacket {
   private int script;

   public StopScriptPacket() {
      super("stopScriptStorytelling");
   }

   public StopScriptPacket(int script) {
      this();
      this.script = script;
   }

   public int getScript() {
      return this.script;
   }
}
