package com.storyteam.storytelling.system.simulator.me_tab.attributes.net;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeData;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class AttributeConvertPartsToValuePacket implements IPacket {
   AttributeType type;

   private AttributeConvertPartsToValuePacket() {
   }

   public AttributeConvertPartsToValuePacket(AttributeType type) {
      this.type = type;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeUtf(this.type.toString());
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.type = AttributeType.valueOf(buf.readUtf());
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      AttributeData data = PlayerStats.getAttribute(this.type);
      data.convertPartsToValue();
   }
}
