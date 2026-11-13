package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.screen.LuaChoiceScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

/** Opens or closes the local choice screen; the server validates selections. */
@MainThreaded
public class PacketLuaChoiceScreen implements IPacket {
   private int scriptId;
   private int actionIndex;
   private String prompt;
   private String[] labels;

   public PacketLuaChoiceScreen() { }

   public PacketLuaChoiceScreen(int scriptId, int actionIndex, String prompt, String[] labels) {
      this.scriptId = scriptId;
      this.actionIndex = actionIndex;
      this.prompt = prompt;
      this.labels = labels;
   }

   public static PacketLuaChoiceScreen close(int scriptId, int actionIndex) {
      return new PacketLuaChoiceScreen(scriptId, actionIndex, "", new String[0]);
   }

   @Override public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(scriptId);
      buf.writeVarInt(actionIndex);
      buf.writeUtf(prompt, 256);
      buf.writeVarInt(labels.length);
      for (String label : labels) {
         buf.writeBoolean(label != null);
         if (label != null) buf.writeUtf(label, 256);
      }
   }

   @Override public void read(FriendlyByteBuf buf) {
      scriptId = buf.readVarInt();
      actionIndex = buf.readVarInt();
      prompt = buf.readUtf(256);
      int count = buf.readVarInt();
      if (count < 0 || count > 6) throw new IllegalArgumentException("Invalid Lua choice count " + count);
      labels = new String[count];
      for (int i = 0; i < count; i++) if (buf.readBoolean()) labels[i] = buf.readUtf(256);
   }

   @OnlyIn(Dist.CLIENT)
   @Override public void clientExecute(PacketContext ctx) {
      Minecraft client = Minecraft.getInstance();
      if (labels.length == 0) {
         if (client.screen instanceof LuaChoiceScreen screen && screen.matches(scriptId, actionIndex)) client.setScreen(null);
      } else if (client.screen == null) {
         client.setScreen(new LuaChoiceScreen(scriptId, actionIndex, prompt, labels));
      }
   }
}
