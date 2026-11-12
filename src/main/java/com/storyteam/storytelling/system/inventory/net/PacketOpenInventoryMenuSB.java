package com.storyteam.storytelling.system.inventory.net;

import com.storyteam.storytelling.system.inventory.client.screen.FakeInventoryMenu;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketOpenInventoryMenuSB implements IPacket {
   public void serverExecute(PacketContext ctx) {
      ctx.getSender().openMenu(FakeInventoryMenu.createProvider());
   }
}
