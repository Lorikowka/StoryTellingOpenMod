package com.storyteam.storytelling.system.simulator.me_tab;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.AttributesMeCommand;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.PlayerStats;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.handler.AttributeMeClientHandler;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.handler.AttributeMeHandler;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.FragmentCommand;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.handler.ClientFragmentHandler;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.handler.FragmentHandler;
import com.storyteam.storytelling.system.simulator.me_tab.fragment.storage.FragmentInstanceStorage;
import com.storyteam.storytelling.system.simulator.me_tab.init.EntitiesMeTab;
import com.storyteam.storytelling.system.simulator.me_tab.init.EntityModelsMeTab;
import com.storyteam.storytelling.system.simulator.me_tab.init.FragmentsST;
import com.storyteam.storytelling.system.simulator.me_tab.word.AnimatedWordQueue;
import com.storyteam.storytelling.system.simulator.me_tab.word.WordQueueCommand;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("simulator/me_tab")
public class MeTabSystem implements ISystem {
   public static MeTabSystem INSTANCE;

   public MeTabSystem() {
      INSTANCE = this;
   }

   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(EntityModelsMeTab.class);
      registrar.accept(EntitiesMeTab.class);
      registrar.accept(FragmentsST.class);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(PlayerStats.class);
      gameBus.register(FragmentInstanceStorage.class);
      gameBus.register(FragmentHandler.class);
      gameBus.register(AttributesMeCommand.class);
      gameBus.register(AttributeMeHandler.class);
      gameBus.register(FragmentCommand.class);
      gameBus.register(WordQueueCommand.class);
      gameBus.register(AnimatedWordQueue.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(ClientFragmentHandler.class);
      gameBus.register(AttributeMeClientHandler.class);
   }
}
