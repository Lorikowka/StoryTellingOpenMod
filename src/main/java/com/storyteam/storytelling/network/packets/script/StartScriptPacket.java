package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class StartScriptPacket extends BasePacket {
   private int script;

   public StartScriptPacket() {
      super("startScriptStorytelling");
   }

   public StartScriptPacket(int script) {
      this();
      this.script = script;
   }

   public int getScript() {
      return this.script;
   }
}
