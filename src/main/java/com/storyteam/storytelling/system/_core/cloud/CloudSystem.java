package com.storyteam.storytelling.system._core.cloud;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("cloud")
public class CloudSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(CommandCloud.class);
   }
}
