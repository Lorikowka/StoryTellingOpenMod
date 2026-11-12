package com.storyteam.storytelling.system.simulator.main_page;

import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabBlocks;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabModels;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabTiles;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SystemRoot("simulator/main_tab")
public class MainTabSystem implements ISystem {
   public static MainTabSystem INSTANCE;

   public MainTabSystem() {
      INSTANCE = this;
   }

   @Override
   public void gatherSimplyRegisters(Consumer<Class<?>> registrar, BiConsumer<String, Class<?>> registrarWithPrefix) {
      registrar.accept(MainTabBlocks.class);
      registrar.accept(MainTabTiles.class);
      registrar.accept(MainTabModels.class);
   }
}
