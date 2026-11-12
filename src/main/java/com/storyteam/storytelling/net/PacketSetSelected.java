package com.storyteam.storytelling.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketSetSelected implements IPacket {
   protected int slot;

   public PacketSetSelected() {
   }

   public PacketSetSelected(int slot) {
      this.slot = slot;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeInt(this.slot);
   }

   public void read(FriendlyByteBuf buf) {
      this.slot = buf.readInt();
   }

   @OnlyIn(Dist.CLIENT)
   public void clientExecute(PacketContext ctx) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         player.getInventory().selected = this.slot;
      }
   }
}
