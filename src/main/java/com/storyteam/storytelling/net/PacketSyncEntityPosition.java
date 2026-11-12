package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSyncEntityPosition implements IPacket {
   int entityId;
   double x;
   double y;
   double z;

   public PacketSyncEntityPosition(Entity entity, double x, double y, double z) {
      this.entityId = entity.getId();
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public PacketSyncEntityPosition() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
   }

   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.entityId);
         if (entity != null) {
            entity.setPos(this.x, this.y, this.z);
         }
      }
   }
}
