package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.story.QuestJournal;
import com.storyteam.storytelling.script.story.StoryFileWriter;
import com.storyteam.storytelling.script.story.StoryWorkspace;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Local display preference; quest definitions and progress remain server-owned. */
public final class QuestJournalSelection {
   private static final String FILE_NAME = "quest-journal.properties";

   private QuestJournalSelection() { }

   public record Value(QuestJournal.Status tab, String questId, int listScroll, int detailScroll) {
      public Value {
         if (tab == null || tab == QuestJournal.Status.AVAILABLE) tab = QuestJournal.Status.ACTIVE;
         if (questId == null) questId = "";
         listScroll = Math.max(0, listScroll);
         detailScroll = Math.max(0, detailScroll);
      }
   }

   public static Value load() {
      try { return read(path()); }
      catch (IOException | IllegalArgumentException error) {
         Storytelling.LOG.warn("Cannot read quest journal selection", error);
         return new Value(QuestJournal.Status.ACTIVE, "", 0, 0);
      }
   }

   public static void save(Value selection) {
      try { write(path(), selection); }
      catch (IOException error) { Storytelling.LOG.warn("Cannot save quest journal selection", error); }
   }

   private static Path path() {
      return StoryWorkspace.root().resolve("client").resolve(FILE_NAME);
   }

   public static Value read(Path file) throws IOException {
      if (!Files.isRegularFile(file)) return new Value(QuestJournal.Status.ACTIVE, "", 0, 0);
      Properties values = new Properties();
      try (var reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) { values.load(reader); }
      QuestJournal.Status tab;
      try { tab = QuestJournal.Status.valueOf(values.getProperty("tab", "ACTIVE")); }
      catch (IllegalArgumentException ignored) { tab = QuestJournal.Status.ACTIVE; }
      return new Value(tab, values.getProperty("quest", ""), number(values, "listScroll"), number(values, "detailScroll"));
   }

   public static void write(Path file, Value selection) throws IOException {
      Path directory = file.toAbsolutePath().normalize().getParent();
      Files.createDirectories(directory);
      Path target = directory.toRealPath().resolve(file.getFileName());
      Properties values = new Properties();
      values.setProperty("tab", selection.tab().name());
      values.setProperty("quest", selection.questId());
      values.setProperty("listScroll", Integer.toString(selection.listScroll()));
      values.setProperty("detailScroll", Integer.toString(selection.detailScroll()));
      StringWriter text = new StringWriter();
      values.store(text, "Storytelling quest journal selection");
      StoryFileWriter.write(target, text.toString(), false);
   }

   private static int number(Properties values, String key) {
      try { return Integer.parseInt(values.getProperty(key, "0")); }
      catch (NumberFormatException ignored) { return 0; }
   }
}
