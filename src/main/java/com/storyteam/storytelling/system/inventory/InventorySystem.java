package com.storyteam.storytelling.system.inventory;

import com.storyteam.storytelling.init.MenuTypesST;
import com.storyteam.storytelling.init.system.MorphsST;
import com.storyteam.storytelling.system._core.morph.IAnimatedPlayer;
import com.storyteam.storytelling.system._sysofsys.ISystem;
import com.storyteam.storytelling.system._sysofsys.SystemRoot;
import com.storyteam.storytelling.system.inventory.client.screen.FakeInventoryMenuScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;

public class InventorySystem implements ISystem {
   public static InventorySystem INSTANCE;
   private static int HOTBAR_SIZE = 1;
   private static OffhandState OFFHAND_STATE = OffhandState.AUTOMATIC;

   public InventorySystem() {
      INSTANCE = this;
   }

   public static int getHotBarSize(Player player) {
      return isOverrideLogicDisabled(player) ? 9 : HOTBAR_SIZE;
   }

   public static boolean isSecondHandAvailable(Player player) {
      switch (OFFHAND_STATE) {
         case AUTOMATIC:
            return player == null || ((IAnimatedPlayer)player).getMorph().equals(MorphsST.NONE) || player.isCreative() || player.isSpectator();
         case DISABLED:
            return false;
         case ACTIVE:
            return true;
         default:
            return false;
      }
   }

   public static void setHotBarSize(int size) {
      if (size >= 1 && size <= 9) {
         HOTBAR_SIZE = size;
      } else {
         throw new IllegalArgumentException("Hotbar size must be between 1 and 9");
      }
   }

   public static void setOffhandState(OffhandState state) {
      OFFHAND_STATE = state;
   }

   public static boolean isOverrideLogicDisabled(Player player) {
      return player != null && (player.isCreative() || player.isSpectator());
   }

   @Override
   public void subscribeClient(IEventBus gameBus, IEventBus modBus) {
      modBus.addListener((net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent e) -> e.enqueueWork(() -> MenuScreens.register(MenuTypesST.FAKE_INV_MENU, FakeInventoryMenuScreen::new)));
   }
}
