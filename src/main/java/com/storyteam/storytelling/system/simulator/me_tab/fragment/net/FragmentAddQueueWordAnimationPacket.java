package com.storyteam.storytelling.system.simulator.me_tab.fragment.net;

import com.storyteam.storytelling.system.simulator.me_tab.fragment.data.FragmentData;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentStorage;
import com.storyteam.storytelling.system.simulator.me_tab.word.AnimatedWordQueue;
import com.storyteam.storytelling.system.simulator.me_tab.word.data.AnimatedWordData;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class FragmentAddQueueWordAnimationPacket implements IPacket {
   String id;

   public FragmentAddQueueWordAnimationPacket() {
   }

   public FragmentAddQueueWordAnimationPacket(String id) {
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

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      FragmentData data = FragmentStorage.FRAGMENTS_MAP.get(this.id);
      if (data != null) {
         AnimatedWordQueue.addWord(new AnimatedWordData(data));
      }
   }
}
