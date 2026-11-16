package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.content.overlays.StoryHudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

/** A short quest notification or a scene hint for one client. */
@MainThreaded
public class PacketStoryHud implements IPacket {
   private boolean hint;
   private String text;
   private int ticks;

   public PacketStoryHud() { }

   public PacketStoryHud(boolean hint, String text, int ticks) {
      this.hint = hint;
      this.text = text.length() <= 512 ? text : text.substring(0, 512);
      this.ticks = Math.max(20, Math.min(600, ticks));
   }

   @Override public void write(FriendlyByteBuf buf) {
      buf.writeBoolean(hint);
      buf.writeUtf(text, 512);
      buf.writeVarInt(ticks);
   }

   @Override public void read(FriendlyByteBuf buf) {
      hint = buf.readBoolean();
      text = buf.readUtf(512);
      ticks = Math.max(20, Math.min(600, buf.readVarInt()));
   }

   @OnlyIn(Dist.CLIENT)
   @Override public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().execute(() -> StoryHudOverlay.show(hint, text, ticks));
   }
}
