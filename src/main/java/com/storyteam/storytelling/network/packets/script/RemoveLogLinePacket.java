package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class RemoveLogLinePacket extends BasePacket {
   private String id;
   private Integer script;

   public RemoveLogLinePacket() {
      super("removeLogLine");
   }

   public RemoveLogLinePacket(String id, int script) {
      this();
      this.id = id;
      this.script = script;
   }

   public String getId() {
      return this.id;
   }

   public int getScript() {
      return this.script;
   }
}
