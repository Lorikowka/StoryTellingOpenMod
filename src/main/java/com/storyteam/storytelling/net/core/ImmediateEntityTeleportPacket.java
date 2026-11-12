package com.storyteam.storytelling.net.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class ImmediateEntityTeleportPacket implements IPacket {
   public Entity entity;
   public Vec3 pos;
   private float xRot;
   private float yRot;
   private float yHeadRot;

   public ImmediateEntityTeleportPacket(Entity entity, Vec3 pos) {
      this.entity = entity;
      this.pos = pos;
      this.xRot = entity.getXRot();
      this.yRot = entity.getYRot();
      this.yHeadRot = entity.getYHeadRot();
   }

   public ImmediateEntityTeleportPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.entity.getId());
      buf.writeDouble(this.pos.x);
      buf.writeDouble(this.pos.y);
      buf.writeDouble(this.pos.z);
      buf.writeFloat(this.xRot);
      buf.writeFloat(this.yRot);
      buf.writeFloat(this.yHeadRot);
   }

   public void read(FriendlyByteBuf buf) {
      this.entity = Minecraft.getInstance().level.getEntity(buf.readInt());
      this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
      this.xRot = buf.readFloat();
      this.yRot = buf.readFloat();
      this.yHeadRot = buf.readFloat();
   }

   public void clientExecute(PacketContext ctx) {
      this.entity.setPos(this.pos);
      this.entity.setXRot(this.xRot);
      this.entity.setYRot(this.yRot);
      this.entity.setYHeadRot(this.yHeadRot);
   }
}
