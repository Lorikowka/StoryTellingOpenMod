package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.types.Script;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class ScriptUpdatePacket extends BasePacket {
   private Script script;

   public ScriptUpdatePacket() {
      super("scriptUpdate");
   }

   public ScriptUpdatePacket(Script script) {
      this();
      this.script = script;
   }

   public Script getScript() {
      return this.script;
   }
}
