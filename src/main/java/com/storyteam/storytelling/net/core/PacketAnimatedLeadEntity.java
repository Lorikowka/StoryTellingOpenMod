package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketAnimatedLeadEntity implements IPacket {
   private int id;
   private boolean onLead;
   private double x;
   private double y;
   private double z;

   public PacketAnimatedLeadEntity(int id, boolean onWire, Vec3 connectPos) {
      this.id = id;
      this.onLead = onWire;
      this.x = connectPos.x();
      this.y = connectPos.y();
      this.z = connectPos.z();
   }

   public PacketAnimatedLeadEntity() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeBoolean(this.onLead);
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      this.onLead = buf.readBoolean();
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null && level.getEntity(this.id) instanceof AnimatedEntity e) {
         e.onLead = this.onLead;
         e.connectPos = new Vec3(this.x, this.y, this.z);
      }
   }
}
