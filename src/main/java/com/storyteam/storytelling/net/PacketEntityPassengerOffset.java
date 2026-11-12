package com.storyteam.storytelling.net;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
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
public class PacketEntityPassengerOffset implements IPacket {
   private int id;
   private float xo;
   private float yo;
   private float zo;

   public PacketEntityPassengerOffset(int id, float x, float y, float z) {
      this.id = id;
      this.xo = x;
      this.yo = y;
      this.zo = z;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeFloat(this.xo);
      buf.writeFloat(this.yo);
      buf.writeFloat(this.zo);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      this.xo = buf.readFloat();
      this.yo = buf.readFloat();
      this.zo = buf.readFloat();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.id);
         if (entity instanceof AnimatedEntity) {
            ((AnimatedEntity)entity).setPassengerOffset((double)this.xo, (double)this.yo, (double)this.zo);
         }
      }
   }
}
