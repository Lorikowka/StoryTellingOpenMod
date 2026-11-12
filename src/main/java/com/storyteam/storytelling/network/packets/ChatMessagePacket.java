package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class ChatMessagePacket extends BasePacket {
   public String text;

   public ChatMessagePacket(String text) {
      super("chatMessage");
      this.text = text;
   }

   public ChatMessagePacket() {
      super("chatMessage");
   }
}
