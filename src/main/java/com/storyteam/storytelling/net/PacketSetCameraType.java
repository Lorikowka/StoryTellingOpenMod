package com.storyteam.storytelling.net;

import net.minecraft.client.CameraType;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetCameraType implements IPacket {
   CameraType camera;
   boolean surfing;

   public PacketSetCameraType(CameraType camera, boolean surfing) {
      this.camera = camera;
      this.surfing = surfing;
   }

   public PacketSetCameraType() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeEnum(this.camera);
      buf.writeBoolean(this.surfing);
   }

   public void read(FriendlyByteBuf buf) {
      this.camera = (CameraType)buf.readEnum(CameraType.class);
      this.surfing = buf.readBoolean();
   }

   public void clientExecute(PacketContext ctx) {
      net.minecraft.client.Minecraft.getInstance().options.setCameraType(this.camera);
   }
}
