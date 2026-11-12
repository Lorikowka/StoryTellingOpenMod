package com.storyteam.storytelling.system.lockpicking.net;

import com.storyteam.storytelling.system.lockpicking.data.LockEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.gui.LockScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketOpenLockpicking extends PacketGlopos {
   protected LockRegistryEntry entry;

   public PacketOpenLockpicking(GlobalPos pos, LockRegistryEntry entry) {
      super(pos);
      this.entry = entry;
   }

   public PacketOpenLockpicking() {
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
         mc.setScreen(new LockScreen(this.pos, this.entry));
         mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.STONE_BUTTON_CLICK_ON, 2.0F, 1.0F));
      }
   }
}
