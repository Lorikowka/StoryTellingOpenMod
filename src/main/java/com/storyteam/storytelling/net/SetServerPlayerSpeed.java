package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class SetServerPlayerSpeed implements IPacket {
   private Vec3 vec;

   public SetServerPlayerSpeed(Vec3 speed) {
      this.vec = speed;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeDouble(this.vec.x);
      buf.writeDouble(this.vec.y);
      buf.writeDouble(this.vec.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.vec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
   }

   public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().player.setDeltaMovement(this.vec);
   }
}
