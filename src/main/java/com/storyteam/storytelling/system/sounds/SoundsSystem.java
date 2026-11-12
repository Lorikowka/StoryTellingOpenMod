package com.storyteam.storytelling.system.sounds;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("sounds")
public class SoundsSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(SoundSubcommand.class);
   }
}
