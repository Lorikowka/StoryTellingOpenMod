package com.storyteam.storytelling.net;

import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSyncForgeData implements INBTPacket {
   int entity;
   CompoundTag forgeData;

   public PacketSyncForgeData(int entity, CompoundTag forgeData) {
      this.entity = entity;
      this.forgeData = forgeData;
   }

   public PacketSyncForgeData() {
   }

   public void write(CompoundTag nbt) {
      nbt.putInt("Id", this.entity);
      nbt.put("ForgeData", this.forgeData);
   }

   public void read(CompoundTag nbt) {
      this.entity = nbt.getInt("Id");
      this.forgeData = nbt.getCompound("ForgeData");
   }

   public void clientExecute(PacketContext ctx) {
      Optional<Level> cw = (Optional<Level>)LogicalSidedProvider.CLIENTWORLD.get(ctx.getSide());
      if (cw.isPresent()) {
         Level world = cw.get();
         Entity e = world.getEntity(this.entity);
         if (e != null) {
            CompoundTag pd = e.getPersistentData();
            pd.merge(this.forgeData);
         }
      }
   }
}
