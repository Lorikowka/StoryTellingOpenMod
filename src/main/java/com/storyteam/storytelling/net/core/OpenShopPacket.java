package com.storyteam.storytelling.net.core;

import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import com.storyteam.storytelling.system.shop.events.ShopOpenEvent;
import com.storyteam.storytelling.system.shop.init.ShopsST;
import com.storyteam.storytelling.system.shop.screen.ShopScreen;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class OpenShopPacket implements IPacket {
   private String id;

   public OpenShopPacket(String id) {
      this.id = id;
   }

   public OpenShopPacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.id);
      IPacket.super.write(buf);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.id = buf.readUtf();
   }

   public void clientExecute(PacketContext ctx) {
      ShopInfo shopInfo = ShopsST.SHOPS.get(this.id);
      ShopOpenEvent event = new ShopOpenEvent(shopInfo);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         shopInfo = event.getShop();
         if (Minecraft.getInstance().player != null) {
            SoundUtils.playVolumedSound(Minecraft.getInstance().player, SoundsST.SHOP_OPEN);
         }

         Minecraft.getInstance().setScreen(new ShopScreen(shopInfo));
      }
   }
}
