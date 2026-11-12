package com.storyteam.storytelling.system.shop;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.shop.handlers.ShopHandler;
import com.storyteam.storytelling.system.shop.storage.ShopStorage;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("shop")
public class ShopSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(ShopHandler.class);
      gameBus.register(ShopStorage.class);
   }
}
