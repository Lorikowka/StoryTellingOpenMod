package com.storyteam.storytelling.network.packets;

import com.storyteam.storytelling.logging.WebhookEmbed;
import com.storyteam.storytelling.network.BasePacket;
import com.storyteam.storytelling.network.utils.RegisterSTPacket;

@RegisterSTPacket
public class DiscordLogPacket extends BasePacket {
   public WebhookEmbed embed;

   public DiscordLogPacket(WebhookEmbed embed) {
      super("reportGameLog");
      this.embed = embed;
   }

   public DiscordLogPacket() {
      super("reportGameLog");
   }
}
