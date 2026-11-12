package com.storyteam.storytelling.system._core.st_cutscene;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class STCutscenePacket implements IPacket {
   public STCutsceneData data;
   public STCutscenePacket.Action action;

   public STCutscenePacket(STCutscenePacket.Action action, STCutsceneData data) {
      this.action = action;
      this.data = data;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeNbt(this.data.serializeNBT());
      buf.writeEnum(this.action);
   }

   public void read(FriendlyByteBuf buf) {
      STCutsceneData data1 = new STCutsceneData();
      data1.deserializeNBT(buf.readNbt());
      this.data = data1;
      this.action = (STCutscenePacket.Action)buf.readEnum(STCutscenePacket.Action.class);
   }

   public void clientExecute(PacketContext ctx) {
      switch (this.action) {
         case INIT_OR_TP:
            ClientsideCutsceneHandler.initOrTpCamera(this.data);
            break;
         case MOVE:
            ClientsideCutsceneHandler.moveCamera(this.data);
            break;
         case STOP_CAMERA:
            ClientsideCutsceneHandler.stopCamera();
      }
   }

   public static enum Action {
      INIT_OR_TP,
      MOVE,
      STOP_CAMERA;
   }
}
