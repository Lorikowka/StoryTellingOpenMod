package com.storyteam.storytelling.system.currency;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.currency.command.CurrencySubCommand;
import com.storyteam.storytelling.system.currency.handler.EidosAnimationTickHandler;
import com.storyteam.storytelling.system.currency.storage.CurrencyRegistry;
import com.storyteam.storytelling.system.currency.type.EidosCurrency;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("currency")
public class CurrencySystem implements ISystem {
   public static EidosCurrency EIDOS_CURRENCY = CurrencyRegistry.register(new EidosCurrency());
   private static CurrencySystem instance;

   public CurrencySystem() {
      instance = this;
   }

   public static ResourceLocation tx(String path) {
      return getInstance().pngTex(path);
   }

   public static ResourceLocation category(String category, String path) {
      return getInstance().categoryId(category, path);
   }

   public static ResourceLocation guiTx(String path) {
      return getInstance().guiTex(path);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(CurrencySubCommand.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(EidosAnimationTickHandler.class);
   }

   @Generated
   private static CurrencySystem getInstance() {
      return instance;
   }
}
