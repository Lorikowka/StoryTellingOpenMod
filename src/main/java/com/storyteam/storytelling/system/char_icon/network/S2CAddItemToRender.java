package com.storyteam.storytelling.system.char_icon.network;

import com.storyteam.storytelling.system.char_icon.RenderHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class S2CAddItemToRender implements IPacket {
   private String refId;
   private ItemStack stack;
   private Vec3 offset;

   public S2CAddItemToRender(String refId, ItemStack stack, Vec3 offset) {
      this.refId = refId;
      this.stack = stack;
      this.offset = offset;
   }

   public void write(FriendlyByteBuf buf) {
      buf.writeUtf(this.refId);
      buf.writeItem(this.stack);
      buf.writeDouble(this.offset.x);
      buf.writeDouble(this.offset.y);
      buf.writeDouble(this.offset.z);
   }

   public void read(FriendlyByteBuf buf) {
      this.refId = buf.readUtf();
      this.stack = buf.readItem();
      this.offset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
   }

   public void clientExecute(PacketContext ctx) {
      RenderHolder.addRenderedUUID(this.refId, this.stack, this.offset);
   }
}
