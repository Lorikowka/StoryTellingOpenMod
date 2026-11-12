package com.storyteam.storytelling.system.videotest;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.videotest.handlers.VideotestHandler;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("videotest")
public class VideotestSystem implements ISystem {
   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(VideotestHandler.class);
   }
}
