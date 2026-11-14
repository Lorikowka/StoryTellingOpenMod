package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.screen.QuestJournalScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class PacketQuestJournal implements IPacket {
   private List<String> lines = List.of();

   public PacketQuestJournal() { }
   public PacketQuestJournal(List<String> lines) { this.lines = List.copyOf(lines); }

   @Override public void write(FriendlyByteBuf buf) {
      int count = Math.min(lines.size(), 256);
      buf.writeVarInt(count);
      for (int i = 0; i < count; i++) {
         String line = lines.get(i);
         buf.writeUtf(line.length() <= 512 ? line : line.substring(0, 512), 512);
      }
   }

   @Override public void read(FriendlyByteBuf buf) {
      int count = buf.readVarInt();
      if (count < 0 || count > 256) throw new IllegalArgumentException("Invalid quest journal size " + count);
      List<String> received = new ArrayList<>(count);
      for (int i = 0; i < count; i++) received.add(buf.readUtf(512));
      lines = List.copyOf(received);
   }

   @OnlyIn(Dist.CLIENT)
   @Override public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().setScreen(new QuestJournalScreen(lines));
   }
}
