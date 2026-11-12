package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.ScriptUtils;
import com.storyteam.storytelling.util.ToStringBuilder;
import net.minecraft.network.chat.Component;

public class SendMessageAction extends ScriptPerformerAction {
   private final Component text;

   public SendMessageAction(ScriptPerformer performer, Component text) {
      super(performer);
      this.text = text;
   }

   @Override
   public void tick() {
      ScriptUtils.sendMessageToPlayers(this.text);
   }

   @Override
   public String toString() {
      return ToStringBuilder.builder(this).append("text", this.text.getString()).build();
   }
}
