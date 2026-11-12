package com.storyteam.storytelling.net;

import com.storyteam.storytelling.api.itf.IStoryEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSyncEntityAdditionalData implements IPacket {
   int entityId;
   CompoundTag nbt;

   public PacketSyncEntityAdditionalData(Entity entity) {
      this.entityId = entity.getId();
      this.nbt = IStoryEntity.of(entity).gatherAdditionalNbtData_ST();
   }

   public PacketSyncEntityAdditionalData() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.entityId);
      buf.writeNbt(this.nbt);
   }

   public void read(FriendlyByteBuf buf) {
      this.entityId = buf.readInt();
      this.nbt = buf.readNbt();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity ent = level.getEntity(this.entityId);
         if (ent != null) {
            IStoryEntity.of(ent).applyAdditionalNbtData_ST(this.nbt);
         }
      }
   }
}
