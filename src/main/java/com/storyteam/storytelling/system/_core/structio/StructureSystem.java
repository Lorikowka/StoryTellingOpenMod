package com.storyteam.storytelling.system._core.structio;

import com.storyteam.storytelling.system._core.structio.entity.StructioContent;
import com.storyteam.storytelling.system._core.structio.structure.v3.AttachmentRegistry;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("structio")
public class StructureSystem implements ISystem {
   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(StructioContent.class);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      modBus.register(StructioContent.Events.class);
      gameBus.register(AttachmentRegistry.class);
      gameBus.register(CommandStructIO.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      modBus.register(StructioContent.ClientEvents.class);
   }
}
