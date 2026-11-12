package com.storyteam.storytelling.system.rts;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.rts.client.BuildZonesHandler;
import com.storyteam.storytelling.system.rts.handlers.BuildingSoundHandler;
import com.storyteam.storytelling.system.rts.handlers.DataHandler;
import com.storyteam.storytelling.system.rts.handlers.JEIHandler;
import com.storyteam.storytelling.system.rts.handlers.RTSCommonEventsHandler;
import com.storyteam.storytelling.system.rts.handlers.RegistrationClientHandler;
import com.storyteam.storytelling.system.rts.handlers.RegistrationHandler;
import com.storyteam.storytelling.system.rts.handlers.TickersHandler;
import com.storyteam.storytelling.system.rts.init.RTSAttributesST;
import com.storyteam.storytelling.system.rts.init.RTSBuildingsST;
import com.storyteam.storytelling.system.rts.init.RTSEntityDataSerializersST;
import com.storyteam.storytelling.system.rts.init.RTSProductionItemsST;
import com.storyteam.storytelling.system.rts.init.RTSSoundsST;
import com.storyteam.storytelling.system.rts.init.RTSUnitEntityTypesST;
import com.storyteam.storytelling.system.rts.init.RTSUnitsST;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("rts")
public class RTSSystem implements ISystem {
   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(RTSSoundsST.class);
      registrar.accept(RTSAttributesST.class);
      registrar.accept(RTSEntityDataSerializersST.class);
      registrar.accept(RTSUnitEntityTypesST.class);
      registrar.accept(RTSUnitsST.class);
      registrar.accept(RTSBuildingsST.class);
      registrar.accept(RTSProductionItemsST.class);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(DataHandler.class);
      gameBus.register(TickersHandler.class);
      gameBus.register(JEIHandler.class);
      gameBus.register(BuildingSoundHandler.class);
      gameBus.register(RTSCommonEventsHandler.class);
      modBus.register(RegistrationHandler.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(BuildZonesHandler.class);
      modBus.register(RegistrationClientHandler.class);
   }

   public static ResourceLocation iconTx(String iconName) {
      return St.tx("system/rts/icons/" + iconName);
   }
}
