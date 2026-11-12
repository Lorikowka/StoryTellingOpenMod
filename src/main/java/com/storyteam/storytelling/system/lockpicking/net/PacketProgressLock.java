package com.storyteam.storytelling.system.lockpicking.net;

import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockStorage;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketProgressLock extends PacketGlopos {
   public int index;

   public PacketProgressLock(GlobalPos pos, int index) {
      super(pos);
      this.index = index;
   }

   public PacketProgressLock() {
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      super.write(buf);
      buf.writeVarInt(this.index);
   }

   @Override
   public void read(FriendlyByteBuf buf) {
      super.read(buf);
      this.index = buf.readVarInt();
   }

   public void serverExecute(PacketContext ctx) {
      LockRegistryEntry lp = LockStorage.getLock(this.pos);
      if (lp != null) {
         lp.progress(ctx.getSender(), this.index);
      }
   }
}
