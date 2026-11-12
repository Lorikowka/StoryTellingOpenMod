package com.storyteam.storytelling.system.simulator.me_tab.fragment.net;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentInstanceManager;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentInstance;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class CheckConditionsFragmentPacket implements IPacket {
   String id;

   private CheckConditionsFragmentPacket() {
   }

   public CheckConditionsFragmentPacket(String id) {
      this.id = id;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeUtf(this.id);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.id = buf.readUtf();
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(this.id);
      if (data != null) {
         FragmentInstance instance = FragmentInstanceManager.getActive(data);
         if (instance != null) {
            boolean check = data.checkDescribedConditions(ctx.getSender());
            if (check != instance.isConditionsPassed()) {
               instance.setConditionsPassed(check);
            }
         }
      }
   }
}
