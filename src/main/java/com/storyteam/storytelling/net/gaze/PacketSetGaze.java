package com.storyteam.storytelling.net.gaze;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetGaze implements IPacket {
   public int gazer;
   public double x;
   public double y;
   public double z;

   public PacketSetGaze(int gazer, Vec3 target) {
      this.gazer = gazer;
      this.x = target == null ? Double.NaN : target.x;
      this.y = target == null ? Double.NaN : target.y;
      this.z = target == null ? Double.NaN : target.z;
   }

   public PacketSetGaze() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.gazer);
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.gazer = buf.readInt();
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
   }

   public void clientExecute(PacketContext ctx) {
      ClientLevel cl = Minecraft.getInstance().level;
      if (cl != null) {
         if (cl.getEntity(this.gazer) instanceof IGazingEntity ge) {
            ge.getGazeSystem()
               .setGazePosition(!Double.isNaN(this.x) && !Double.isNaN(this.y) && !Double.isNaN(this.z) ? new Vec3(this.x, this.y, this.z) : null);
         }
      }
   }
}
