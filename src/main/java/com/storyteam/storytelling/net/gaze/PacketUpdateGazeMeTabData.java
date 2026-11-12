package com.storyteam.storytelling.net.gaze;

import com.storyteam.storytelling.api.entity.IGazingEntity;
import com.storyteam.storytelling.system.animation.EyeGazeMeTabSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketUpdateGazeMeTabData implements IPacket {
   public int gazer;
   public EyeGazeMeTabSystem.GazeData data;

   public PacketUpdateGazeMeTabData(int gazer, EyeGazeMeTabSystem.GazeData data) {
      this.gazer = gazer;
      this.data = data;
   }

   public PacketUpdateGazeMeTabData() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.gazer);
      buf.writeNbt(this.data.serializeNBT());
   }

   public void read(FriendlyByteBuf buf) {
      this.gazer = buf.readInt();
      this.data = new EyeGazeMeTabSystem.GazeData();
      this.data.deserializeNBT(buf.readNbt());
   }

   public void clientExecute(PacketContext ctx) {
      ClientLevel cl = Minecraft.getInstance().level;
      if (cl != null) {
         if (cl.getEntity(this.gazer) instanceof IGazingEntity ge && ge.getGazeSystem() instanceof EyeGazeMeTabSystem eyeGazeMeTabSystem) {
            eyeGazeMeTabSystem.updateWeight(this.data.leftHorizWeight, this.data.rightHorizWeight, this.data.eyesVertWeight);
            eyeGazeMeTabSystem.data.copyFrom(this.data);
         }
      }
   }

   public void serverExecute(PacketContext ctx) {
      Level cl = ctx.getSender().level();
      if (cl.getEntity(this.gazer) instanceof IGazingEntity ge && ge.getGazeSystem() instanceof EyeGazeMeTabSystem eyeGazeMeTabSystem) {
         eyeGazeMeTabSystem.updateWeight(this.data.leftHorizWeight, this.data.rightHorizWeight, this.data.eyesVertWeight);
         eyeGazeMeTabSystem.data.copyFrom(this.data);
      }
   }
}
