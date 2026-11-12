package com.storyteam.storytelling.system.inventory.client.screen;

import com.storyteam.storytelling.init.MenuTypesST;
import com.storyteam.storytelling.system.inventory.InventorySystem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.api.inv.IScreenContainer;

public class FakeInventoryMenu extends AbstractContainerMenu implements IScreenContainer {
   public FakeInventoryMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
      this(windowId, playerInventory);
   }

   public FakeInventoryMenu(int windowId, Inventory playerInventory) {
      super(MenuTypesST.FAKE_INV_MENU, windowId);

      for (int i = 0; i < 9; i++) {
         this.addSlot(new Slot(playerInventory, i, 8 + 18 * i, 142));
      }

      if (InventorySystem.isSecondHandAvailable(playerInventory.player)) {
         this.addSlot(new Slot(playerInventory, 40, -10, 142));
      }
   }

   public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
      return null;
   }

   public boolean stillValid(Player player) {
      return true;
   }

   @OnlyIn(Dist.CLIENT)
   public Screen openScreen(Inventory inventory, Component label) {
      return new FakeInventoryMenuScreen(this, inventory, label);
   }

   public static SimpleMenuProvider createProvider() {
      return new SimpleMenuProvider((id, inventory, pl) -> new FakeInventoryMenu(id, inventory), Component.empty());
   }
}
