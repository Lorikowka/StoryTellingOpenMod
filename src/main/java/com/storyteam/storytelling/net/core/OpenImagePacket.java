package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.client.screen.ImageScreen;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class OpenImagePacket implements IPacket {
   protected ResourceLocation tx;

   public OpenImagePacket() {
   }

   public OpenImagePacket(ResourceLocation tx) {
      this.tx = tx;
   }

   public static void open(Player player, ResourceLocation texture) {
      if (player instanceof ServerPlayer sp) {
         Network.sendTo(sp, new OpenImagePacket(texture));
      }
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeResourceLocation(this.tx);
   }

   public void read(FriendlyByteBuf buf) {
      this.tx = buf.readResourceLocation();
   }

   public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().setScreen(new ImageScreen(this.tx));
   }

   @MainThreaded
   public static class ImageScreenClosedPacket extends Event implements IPacket {
      protected ResourceLocation tx;

      public ImageScreenClosedPacket() {
      }

      public ImageScreenClosedPacket(ResourceLocation tx) {
         this.tx = tx;
      }

      public void write(FriendlyByteBuf buf) {
         buf.writeResourceLocation(this.tx);
      }

      public void read(FriendlyByteBuf buf) {
         this.tx = buf.readResourceLocation();
      }

      public void serverExecute(PacketContext ctx) {
         Storytelling.BUS.post(this);
      }

      @Generated
      public ResourceLocation getTx() {
         return this.tx;
      }
   }
}
