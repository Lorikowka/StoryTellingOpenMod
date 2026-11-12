package com.storyteam.storytelling.net.gaze;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.system.animation.EyeGazeSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketUpdateGazeData implements IPacket {
   public int gazer;
   public EyeGazeSystem.GazeData data;

   public PacketUpdateGazeData(int gazer, EyeGazeSystem.GazeData data) {
      this.gazer = gazer;
      this.data = data;
   }

   public PacketUpdateGazeData() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.gazer);
      buf.writeNbt(this.data.serializeNBT());
   }

   public void read(FriendlyByteBuf buf) {
      this.gazer = buf.readInt();
      this.data = new EyeGazeSystem.GazeData();
      this.data.deserializeNBT(buf.readNbt());
   }

   public void clientExecute(PacketContext ctx) {
      ClientLevel cl = Minecraft.getInstance().level;
      if (cl != null) {
         if (cl.getEntity(this.gazer) instanceof IGazingEntity ge) {
            ge.getGazeSystem().updateWeight(this.data.leftHorizWeight, this.data.rightHorizWeight);
            ge.getGazeSystem().data.copyFrom(this.data);
         }
      }
   }

   public void serverExecute(PacketContext ctx) {
      Level cl = ctx.getSender().level();
      if (cl.getEntity(this.gazer) instanceof IGazingEntity ge) {
         ge.getGazeSystem().updateWeight(this.data.leftHorizWeight, this.data.rightHorizWeight);
         ge.getGazeSystem().data.copyFrom(this.data);
      }
   }
}
