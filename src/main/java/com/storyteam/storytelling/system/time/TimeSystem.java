package com.storyteam.storytelling.system.time;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.time.command.TimeSubCommand;
import com.storyteam.storytelling.system.time.handler.LevelLoadHandler;
import com.storyteam.storytelling.system.time.handler.TimeDebugHandler;
import com.storyteam.storytelling.system.time.handler.TimeWorkHandler;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

@SystemRoot("time")
public class TimeSystem implements ISystem {
   private static TimeSystem instance;

   public TimeSystem() {
      instance = this;
   }

   public static ResourceLocation tx(String path) {
      return getInstance().pngTex(path);
   }

   public static ResourceLocation category(String category, String path) {
      return getInstance().categoryId(category, path);
   }

   public static ResourceLocation guiTx(String path) {
      return getInstance().guiTex(path);
   }

   @Override
   public void subscribe(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(TimeWorkHandler.class);
      gameBus.register(LevelLoadHandler.class);
      gameBus.register(TimeSubCommand.class);
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      gameBus.register(TimeDebugHandler.class);
   }

   @Generated
   private static TimeSystem getInstance() {
      return instance;
   }
}
