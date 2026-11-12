package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class GotoPacket extends BasePacket {
   private int script;
   private String label;

   public GotoPacket() {
      super("gotoScriptStorytelling");
   }

   public GotoPacket(int script, String label) {
      this();
      this.script = script;
      this.label = label;
   }

   public int getScript() {
      return this.script;
   }

   public String getLabel() {
      return this.label;
   }
}
