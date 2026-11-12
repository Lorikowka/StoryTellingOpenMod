package com.storyteam.storytelling.system.simulator.me_tab.attributes.net;

import com.storyteam.storytelling.system.inventory.client.screen.FakeInventoryMenuScreen;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.client.flowgui.GuiAttributeWidget;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;
import com.storyteam.storytelling.system.simulator.me_tab.client.MeTabScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class MeTabScreenAnimatedAttributePacket implements IPacket {
   AttributeType type;

   private MeTabScreenAnimatedAttributePacket() {
   }

   public MeTabScreenAnimatedAttributePacket(AttributeType type) {
      this.type = type;
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      buf.writeUtf(this.type.toString());
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.type = AttributeType.valueOf(buf.readUtf());
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      Screen screen = Minecraft.getInstance().screen;
      if (screen instanceof MeTabScreen meTabScreen) {
         GuiAttributeWidget guiAttributeWidget = meTabScreen.attributeWidgetMap.get(this.type);
         guiAttributeWidget.wrapped.setAnimatedStat(true);
      }

      if (screen instanceof FakeInventoryMenuScreen fakeInventoryMenuScreen) {
         GuiAttributeWidget guiAttributeWidget = fakeInventoryMenuScreen.attributeWidgetMap.get(this.type);
         guiAttributeWidget.wrapped.setAnimatedStat(true);
      }
   }
}
