package com.storyteam.storytelling.system.shop.net;

import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.init.SoundsST;
import com.storyteam.storytelling.system.shop.data.LotData;
import com.storyteam.storytelling.system.shop.data.LotInfo;
import com.storyteam.storytelling.system.shop.data.ShopInfo;
import com.storyteam.storytelling.system.shop.data.utils.LotType;
import com.storyteam.storytelling.system.shop.events.ShopExchangeEvent;
import com.storyteam.storytelling.system.shop.init.ShopsST;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import com.storyteam.storytelling.system.shop.utils.ShopUtils;
import com.storyteam.storytelling.system.sounds.SoundUtils;
import com.storyteam.storytelling.util.ScriptUses;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class ShopExchangePacket implements IPacket {
   private String shopID;
   private String tabID;
   private String lotID;
   private int count;

   public ShopExchangePacket(String shopID, String tabID, String lotID, int count) {
      this.shopID = shopID;
      this.tabID = tabID;
      this.lotID = lotID;
      this.count = count;
   }

   public ShopExchangePacket() {
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.shopID);
      buf.writeUtf(this.tabID);
      buf.writeUtf(this.lotID);
      buf.writeInt(this.count);
      IPacket.super.write(buf);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.shopID = buf.readUtf();
      this.tabID = buf.readUtf();
      this.lotID = buf.readUtf();
      this.count = buf.readInt();
   }

   public void serverExecute(PacketContext ctx) {
      ShopInfo shopInfo = ShopsST.SHOPS.get(this.shopID);
      LotInfo lotInfo = shopInfo.getTabs().get(this.tabID).getLots().get(this.lotID);
      LotData lotData = ShopStorage.SHOPS.get(this.shopID).getLots().get(this.lotID);
      ShopExchangeEvent event = new ShopExchangeEvent(shopInfo, lotInfo, this.count, false);
      IStoryServer.get().postStoryEvent(EventType.SHOP_EXCHANGE_EVENT, event);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         this.count = event.getCount();
         int cost = lotInfo.getFinalCost() * event.getCount();
         ServerPlayer player = ctx.getSender();
         ItemStack result = lotInfo.getStack().copy();
         boolean mayContinue = false;
         if (lotInfo.getType() == LotType.BUY) {
            SoundUtils.playVolumedSound(player, SoundsST.SHOP_BUY);
            result.setCount(lotInfo.getCount());

            for (ItemStack stack : lotInfo.getTradeStack()) {
               ScriptUses.removeItem(player, stack.getItem(), stack.getCount() * this.count);
            }

            lotData.setTimes_bought(lotData.getTimes_bought() + 1);
            if (!event.isForfeitItems()) {
               for (int i = 0; i < this.count; i++) {
                  if (!player.addItem(result.copy())) {
                     ScriptUses.dropItem(player, result.copy(), true, true);
                  }
               }
            }

            mayContinue = true;
         } else if (ShopUtils.takeAll(lotInfo, this.count, player)) {
            SoundUtils.playVolumedSound(player, SoundsST.SHOP_SELL);
            mayContinue = true;
         }

         if (mayContinue && lotData.getStock() > 0) {
            lotData.setStock(Math.max(0, lotData.getStock() - this.count));
         }
      }
   }
}
