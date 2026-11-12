package com.storyteam.storytelling.net;

import com.storyteam.storytelling.content.entities.AnimatedEntity;
import com.storyteam.storytelling.content.entities.HumanoidEntity;
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
public class PacketEntityEyeBlinkAnimation implements IPacket {
   private int id;
   private boolean state;

   public PacketEntityEyeBlinkAnimation() {
   }

   public PacketEntityEyeBlinkAnimation(int id, boolean state) {
      this.id = id;
      this.state = state;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeBoolean(this.state);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      this.state = buf.readBoolean();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.id);
         if (entity instanceof AnimatedEntity) {
            ((HumanoidEntity)entity).setDisableEyeBlinkAnimation(this.state);
         }
      }
   }
}
