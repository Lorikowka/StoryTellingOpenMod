package com.storyteam.storytelling.system.persona;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.persona.command.PersonaCommand;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("persona")
public class PersonaSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(PersonaCommand.class);
   }
}
