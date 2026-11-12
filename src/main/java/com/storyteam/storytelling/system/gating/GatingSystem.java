package com.storyteam.storytelling.system.gating;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.gating.command.SubcommandContentGating;
import com.storyteam.storytelling.system.gating.io.LockedEntriesDB;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("gating")
public class GatingSystem implements ISystem {
   public static GatingSystem INSTANCE;

   public GatingSystem() {
      INSTANCE = this;
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(SubcommandContentGating.class);
      gameBus.register(LockedEntriesDB.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(LockEvents.class);
   }
}
