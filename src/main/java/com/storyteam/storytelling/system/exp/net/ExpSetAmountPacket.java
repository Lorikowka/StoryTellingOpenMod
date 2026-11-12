package com.storyteam.storytelling.system.exp.net;

import com.storyteam.storytelling.system.exp.ExpStorage;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class ExpSetAmountPacket implements IPacket {
   int amount;

   public ExpSetAmountPacket(int amount) {
      this.amount = amount;
   }

   public ExpSetAmountPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeInt(this.amount);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.amount = buf.readInt();
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      ExpStorage.setAmount(this.amount);
   }
}
