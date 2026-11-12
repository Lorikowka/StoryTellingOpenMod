package com.storyteam.storytelling.system.containertier;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.containertier.command.ContainerTierCommand;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("container_tier")
public class ContainerTierSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(ContainerTierCommand.class);
   }
}
