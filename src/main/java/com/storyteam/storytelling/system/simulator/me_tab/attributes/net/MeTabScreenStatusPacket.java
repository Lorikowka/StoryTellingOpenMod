package com.storyteam.storytelling.system.simulator.me_tab.attributes.net;

import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class MeTabScreenStatusPacket implements IPacket {
   boolean open;

   public MeTabScreenStatusPacket() {
   }

   public MeTabScreenStatusPacket(boolean open) {
      this.open = open;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeBoolean(this.open);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.open = buf.readBoolean();
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      PlayerStats.isGuiWithAttributeScreenOpen = this.open;
   }
}
