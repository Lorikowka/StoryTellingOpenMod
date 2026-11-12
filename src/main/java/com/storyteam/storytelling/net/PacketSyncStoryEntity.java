package com.storyteam.storytelling.net;

import java.util.UUID;
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
public class PacketSyncStoryEntity implements IPacket {
   private int id;
   private String name;
   private boolean vulnerable;
   private UUID uuid;

   public PacketSyncStoryEntity() {
   }

   public PacketSyncStoryEntity(int id, String name, UUID uuid, boolean vulnerable) {
      this.id = id;
      this.name = name;
      this.vulnerable = vulnerable;
      this.uuid = uuid;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeUtf(this.name);
      buf.writeBoolean(this.vulnerable);
      buf.writeUUID(this.uuid);
   }

   public void read(FriendlyByteBuf buf) {
      this.id = buf.readInt();
      this.name = buf.readUtf(256);
      this.vulnerable = buf.readBoolean();
      this.uuid = buf.readUUID();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      ClientLevel level = Minecraft.getInstance().level;
      if (level != null) {
         Entity entity = level.getEntity(this.id);
         if (entity != null) {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("RefId", this.uuid);
            nbt.putString("Id", this.name);
            nbt.putBoolean("Vulnerable", this.vulnerable);
            entity.getPersistentData().put("Storytelling", nbt);
         }
      }
   }
}
