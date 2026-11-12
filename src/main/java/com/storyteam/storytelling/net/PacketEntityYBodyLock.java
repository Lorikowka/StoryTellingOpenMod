package com.storyteam.storytelling.net;

import com.storyteam.storytelling.content.entities.HumanoidEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketEntityYBodyLock implements IPacket {
   private int id;
   private boolean lockBody;
   private float y;

   public PacketEntityYBodyLock() {
   }

   public PacketEntityYBodyLock(int id, boolean lockBody, float y) {
      this.id = id;
      this.lockBody = lockBody;
      this.y = y;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeBoolean(this.lockBody);
      buf.writeFloat(this.y);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      this.lockBody = buf.readBoolean();
      this.y = buf.readFloat();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null && level.getEntity(this.id) instanceof HumanoidEntity he) {
         he.yBody = this.y;
         he.lockYBody = this.lockBody;
      }
   }
}
