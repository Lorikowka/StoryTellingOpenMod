package com.storyteam.storytelling.init;

import com.storyteam.storytelling.system.inventory.client.screen.FakeInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface MenuTypesST {
   @RegistryName("fake_inventory")
   MenuType<FakeInventoryMenu> FAKE_INV_MENU = IForgeMenuType.create(FakeInventoryMenu::new);
}
