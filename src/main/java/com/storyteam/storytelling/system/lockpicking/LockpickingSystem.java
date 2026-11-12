package com.storyteam.storytelling.system.lockpicking;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.lockpicking.data.LockStorage;
import com.storyteam.storytelling.system.lockpicking.event.LockClientHandler;
import com.storyteam.storytelling.system.lockpicking.event.LockCommonHandler;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("lockpicking")
public class LockpickingSystem implements ISystem {
   public static LockpickingSystem INSTANCE;

   public LockpickingSystem() {
      INSTANCE = this;
   }

   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(InitLockPicking.class);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(LockStorage.class);
      gameBus.register(CommandLock.class);
      gameBus.register(LockCommonHandler.class);
      modBus.addListener(InitLockPicking::recipes);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(LockClientHandler.class);
   }
}
