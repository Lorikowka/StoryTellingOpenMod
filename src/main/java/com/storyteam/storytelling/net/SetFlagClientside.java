package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class SetFlagClientside implements IPacket {
   private String flag;
   private int id;
   private boolean remove;

   public SetFlagClientside(int id, String flag, boolean remove) {
      this.flag = flag;
      this.id = id;
      this.remove = remove;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeUtf(this.flag);
      buf.writeInt(this.id);
      buf.writeBoolean(this.remove);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.flag = buf.readUtf();
      this.id = buf.readInt();
      this.remove = buf.readBoolean();
   }

   public void serverExecute(PacketContext ctx) {
      IPacket.super.serverExecute(ctx);
      ScriptEnvironment e = IStoryServer.get().getScriptEnv();
      if (e.getScript(this.id) != null) {
         if (!this.remove) {
            e.getScript(this.id).flags.set(this.flag);
         } else {
            e.getScript(this.id).flags.remove(this.flag);
         }
      }
   }
}
