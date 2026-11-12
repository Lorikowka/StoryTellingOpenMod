package com.storyteam.storytelling.system.lockpicking.net;

import com.storyteam.storytelling.system.lockpicking.data.LockEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.gui.LockScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketUpdateLockpicking extends PacketGlopos {
   protected LockRegistryEntry entry;

   public PacketUpdateLockpicking(GlobalPos pos, LockRegistryEntry entry) {
      super(pos);
      this.entry = entry;
   }

   public PacketUpdateLockpicking() {
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      super.write(buf);
      buf.writeNbt(this.entry.serializeNBT());
      buf.writeNbt(this.entry.entry.serializeNBT());
   }

   @Override
   public void read(FriendlyByteBuf buf) {
      super.read(buf);
      this.entry = new LockRegistryEntry(buf.readNbt());
      this.entry.entry = new LockEntry();
      this.entry.entry.deserializeNBT(buf.readNbt());
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      if (this.pos != null) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.screen instanceof LockScreen lock) {
            lock.display = this.entry;
            lock.regenBrokenNotches();
            lock.rebuildWidgets();
         }
      }
   }
}
