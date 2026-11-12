package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class SkipScriptPacket extends BasePacket {
   private int script;

   public SkipScriptPacket() {
      super("skipScriptStorytelling");
   }

   public SkipScriptPacket(String id, int script) {
      this();
      this.script = script;
   }

   public int getScript() {
      return this.script;
   }
}
