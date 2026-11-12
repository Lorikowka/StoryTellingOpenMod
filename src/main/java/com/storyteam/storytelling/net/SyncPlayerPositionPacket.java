package com.storyteam.storytelling.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class SyncPlayerPositionPacket implements IPacket {
   Vec3 pos;
   Vec3 move;

   public SyncPlayerPositionPacket(Vec3 pos, Vec3 move) {
      this.pos = pos;
      this.move = move;
   }

   public SyncPlayerPositionPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeDouble(this.pos.x);
      buf.writeDouble(this.pos.y);
      buf.writeDouble(this.pos.z);
      buf.writeDouble(this.move.x);
      buf.writeDouble(this.move.y);
      buf.writeDouble(this.move.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      this.move = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
   }

   public void serverExecute(PacketContext ctx) {
      ctx.getSender().setPos(this.pos);
      ctx.getSender().setDeltaMovement(this.move);
   }
}
