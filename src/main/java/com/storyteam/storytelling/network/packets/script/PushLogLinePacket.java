package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import java.time.Instant;

@RegisterSTPacket
public class PushLogLinePacket extends BasePacket {
   private String id;
   private Integer script;
   private String text;
   private Boolean debug;
   private Instant time = Instant.now();

   public PushLogLinePacket() {
      super("pushLogLine");
   }

   public PushLogLinePacket(String id, int script, String text, Boolean debug) {
      super("pushLogLine");
      this.id = id;
      this.script = script;
      this.text = text;
      this.debug = debug;
      this.time = Instant.now();
   }

   public PushLogLinePacket(String id, int script, String text, Boolean debug, Instant time) {
      super("pushLogLine");
      this.id = id;
      this.script = script;
      this.text = text;
      this.debug = debug;
      this.time = time;
   }
}
