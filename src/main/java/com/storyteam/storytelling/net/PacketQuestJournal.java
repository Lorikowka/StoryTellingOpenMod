package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.screen.QuestJournalScreen;
import com.storyteam.storytelling.script.story.QuestJournal;
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
   private static final int MAX_QUESTS = 128;
   private static final int MAX_TASKS = 64;
   private List<QuestJournal.Entry> entries = List.of();

   public PacketQuestJournal() { }
   public PacketQuestJournal(List<QuestJournal.Entry> entries) { this.entries = List.copyOf(entries); }

   @Override public void write(FriendlyByteBuf buf) {
      int count = Math.min(entries.size(), MAX_QUESTS);
      buf.writeVarInt(count);
      for (int i = 0; i < count; i++) {
         QuestJournal.Entry entry = entries.get(i);
         writeText(buf, entry.id(), 128);
         writeText(buf, entry.title(), 256);
         writeText(buf, entry.description(), 1024);
         buf.writeEnum(entry.status());
         int taskCount = Math.min(entry.tasks().size(), MAX_TASKS);
         buf.writeVarInt(taskCount);
         for (int j = 0; j < taskCount; j++) {
            QuestJournal.TaskEntry task = entry.tasks().get(j);
            writeText(buf, task.title(), 256);
            buf.writeBoolean(task.done());
         }
      }
   }

   @Override public void read(FriendlyByteBuf buf) {
      int count = readCount(buf, MAX_QUESTS, "quests");
      List<QuestJournal.Entry> received = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
         String id = buf.readUtf(128);
         String title = buf.readUtf(256);
         String description = buf.readUtf(1024);
         QuestJournal.Status status = buf.readEnum(QuestJournal.Status.class);
         int taskCount = readCount(buf, MAX_TASKS, "quest tasks");
         List<QuestJournal.TaskEntry> tasks = new ArrayList<>(taskCount);
         for (int j = 0; j < taskCount; j++) {
            tasks.add(new QuestJournal.TaskEntry(buf.readUtf(256), buf.readBoolean()));
         }
         received.add(new QuestJournal.Entry(id, title, description, status, List.copyOf(tasks)));
      }
      entries = List.copyOf(received);
   }

   private static int readCount(FriendlyByteBuf buf, int maximum, String label) {
      int count = buf.readVarInt();
      if (count < 0 || count > maximum) throw new IllegalArgumentException("Invalid " + label + " count " + count);
      return count;
   }

   private static void writeText(FriendlyByteBuf buf, String text, int maximum) {
      buf.writeUtf(text.length() <= maximum ? text : text.substring(0, maximum), maximum);
   }

   @OnlyIn(Dist.CLIENT)
   @Override public void clientExecute(PacketContext ctx) {
      Minecraft.getInstance().setScreen(new QuestJournalScreen(entries));
   }
}
