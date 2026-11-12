package com.storyteam.storytelling.system.shaders.net;

import com.storyteam.storytelling.system.shaders.ShaderSystem;
import lombok.Generated;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class EnableShaderSystemPacket implements IPacket {
   public boolean enable;

   public void clientExecute(PacketContext ctx) {
      ShaderSystem.setEnabled(this.enable);
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeBoolean(this.enable);
   }

   public void read(FriendlyByteBuf buf) {
      this.enable = buf.readBoolean();
   }

   @Generated
   public EnableShaderSystemPacket(boolean enable) {
      this.enable = enable;
   }
}
