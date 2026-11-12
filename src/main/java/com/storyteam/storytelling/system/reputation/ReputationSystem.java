package com.storyteam.storytelling.system.reputation;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.reputation.gift.GiftingManager;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("reputation")
public class ReputationSystem implements ISystem {
   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(GiftingManager.class);
   }
}
