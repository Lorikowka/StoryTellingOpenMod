package com.storyteam.storytelling.system.lockpicking.net;

import com.storyteam.storytelling.system.lockpicking.gui.LockScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketCloseLockpicking implements IPacket {
   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.screen instanceof LockScreen lock) {
         mc.setScreen(null);
      }
   }
}
