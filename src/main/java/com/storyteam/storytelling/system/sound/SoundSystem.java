package com.storyteam.storytelling.system.sound;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.sound.handler.SoundHandler;
import com.storyteam.storytelling.system.sound.init.UnitSoundsST;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("sound")
public class SoundSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(SoundHandler.class);
   }

   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(UnitSoundsST.class);
   }
}
