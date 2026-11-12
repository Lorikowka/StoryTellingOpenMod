package com.storyteam.storytelling.network.packets.script;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;
import org.jetbrains.annotations.Nullable;

@RegisterSTPacket
public class ClearLogLinesPacket extends BasePacket {
   public Integer script = null;

   public ClearLogLinesPacket() {
      super("clearLogLines");
   }

   public ClearLogLinesPacket(@Nullable Integer script) {
      super("clearLogLines");
      this.script = script;
   }

   @Nullable
   public Integer getScript() {
      return this.script;
   }
}
