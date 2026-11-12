package com.storyteam.storytelling.system.riddles;

import com.storyteam.storytelling.init.system.RiddlesST;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.riddles.command.RiddlesSubCommand;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("riddles")
public class RiddlesSystem implements ISystem {
   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(RiddlesST.class);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(RiddlesSubCommand.class);
   }
}
