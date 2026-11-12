package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetCustomTexture implements IPacket {
   private int id;
   private String tex;

   public PacketSetCustomTexture() {
   }

   public PacketSetCustomTexture(int id, String tex) {
      this.id = id;
      this.tex = tex;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeBoolean(this.tex != null);
      if (this.tex != null) {
         buf.writeUtf(this.tex);
      }
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      if (buf.readBoolean()) {
         this.tex = buf.readUtf();
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.id);
         if (entity != null) {
            IStoryEntity.of(entity).setCustomTexture(this.tex);
         }
      }
   }
}
