package com.storyteam.storytelling.system.simulator.me_tab.net;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class FragmentAcceptRejectActionPacket implements IPacket {
   boolean accept;
   String idData;

   private FragmentAcceptRejectActionPacket() {
   }

   public FragmentAcceptRejectActionPacket(boolean accept, String idData) {
      this.idData = idData;
      this.accept = accept;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeBoolean(this.accept);
      buf.writeUtf(this.idData);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.accept = buf.readBoolean();
      this.idData = buf.readUtf();
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(this.idData);
      if (data != null) {
         FragmentInstance instance = FragmentInstanceManager.getActive(data);
         if (instance != null) {
            instance.applyServerSideAcceptReject(this.accept, ctx.getSender());
         }
      }
   }
}
