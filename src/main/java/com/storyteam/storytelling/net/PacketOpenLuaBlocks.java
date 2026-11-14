package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.screen.LuaBlocksScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketOpenLuaBlocks implements IPacket {
   @Override public void write(FriendlyByteBuf buf) { }
   @Override public void read(FriendlyByteBuf buf) { }

   @OnlyIn(Dist.CLIENT)
   @Override public void clientExecute(PacketContext ctx) {
      Minecraft client = Minecraft.getInstance();
      if (client.getSingleplayerServer() != null) client.setScreen(new LuaBlocksScreen());
   }
}
