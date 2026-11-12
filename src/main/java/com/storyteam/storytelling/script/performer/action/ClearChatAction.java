package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.net.core.PacketClearChat;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ToStringBuilder;
import org.zeith.hammerlib.net.Network;

public class ClearChatAction extends ScriptPerformerAction {
   public ClearChatAction(ScriptPerformer performer) {
      super(performer);
   }

   @Override
   public void tick() {
      Network.sendToAll(new PacketClearChat());
   }

   @Override
   public void reset() {
   }

   @Override
   public void fastForwardToStage() {
      Network.sendToAll(new PacketClearChat());
   }

   @Override
   public void rewindToStage() {
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).build();
   }
}
