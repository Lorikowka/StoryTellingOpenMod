package com.storyteam.storytelling.system._sysofsys;

import com.storyteam.storytelling.St;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

public interface ISystem {
   default void subscribe(IEventBus gameBus, IEventBus modBus) {
   }

   default void subscribeClient(IEventBus gameBus, IEventBus modBus) {
   }

   default void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
   }

   default ResourceLocation id(String path) {
      return St.id("system/" + this.getSystemName() + "/" + path);
   }

   default ResourceLocation categoryId(String category, String path) {
      return St.id(category + "/system/" + this.getSystemName() + "/" + path);
   }

   default ResourceLocation pngTex(String path) {
      return St.tx("system/" + this.getSystemName() + "/" + path);
   }

   default ResourceLocation guiTex(String path) {
      return this.categoryTex("gui", path);
   }

   default ResourceLocation categoryTex(String category, String path) {
      return St.tx(category + "/system/" + this.getSystemName() + "/" + path);
   }

   default String getSystemName() {
      SystemRoot rt = this.getClass().getAnnotation(SystemRoot.class);
      return rt.value();
   }
}
