package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.LuaStory;
import com.storyteam.storytelling.script.story.StoryFileWriter;
import com.storyteam.storytelling.script.story.StoryWorkspace;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

/** Local-world Lua editor. Writes are backed up and replaced atomically when supported. */
public final class LuaEditorScreen extends Screen {
   private static final int MAX_SOURCE = 262_144;
   private static final int EDIT_X = 148;
   private EditBox fileName;
   private EditorBox editor;
   private String draft = "";
   private String selected = "";
   private String status = "Выберите файл или введите новый числовой ID";
   private boolean dirty;
   private int page;
   private List<LineMarker> lineMarkers = List.of();

   public LuaEditorScreen() { super(Component.literal("Редактор сценариев Storytelling")); }

   @Override protected void init() {
      if (minecraft == null || minecraft.getSingleplayerServer() == null) {
         onClose();
         return;
      }
      int editX = EDIT_X;
      int editWidth = Math.max(120, width - editX - 12);
      fileName = addRenderableWidget(new EditBox(font, editX, 31, Math.min(180, editWidth), 20, Component.literal("Файл")));
      fileName.setMaxLength(32);
      fileName.setValue(selected);
      editor = addRenderableWidget(new EditorBox(font, editX, 58, editWidth, Math.max(70, height - 106),
         Component.literal("Lua"), Component.literal("Код Lua")));
      editor.setCharacterLimit(MAX_SOURCE);
      editor.setValue(draft);
      rebuildLineMarkers();
      editor.setValueListener(value -> { draft = value; dirty = true; rebuildLineMarkers(); });
      addRenderableWidget(Button.builder(Component.literal("Проверить"), button -> validateDraft())
         .bounds(Math.max(editX, width - 212), height - 42, 96, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Сохранить"), button -> saveDraft())
         .bounds(Math.max(editX + 100, width - 110), height - 42, 98, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Новый"), button -> {
         if (dirty) { status = "Сначала сохраните текущие изменения"; return; }
         selected = ""; draft = "story(900000)\n\npart(\"main\")\n";
         fileName.setValue(""); editor.setValue(draft); dirty = true;
      }).bounds(8, 31, 100, 20).build());
      refreshList();
   }

   private void refreshList() {
      try {
         Files.createDirectories(StoryWorkspace.scripts());
         List<Path> files;
         try (Stream<Path> stream = Files.list(StoryWorkspace.scripts())) {
            files = stream.filter(Files::isRegularFile)
               .filter(path -> path.getFileName().toString().matches("[0-9]+\\.lua"))
               .sorted(Comparator.comparing(path -> path.getFileName().toString())).toList();
         }
         int pages = Math.max(1, (files.size() + 7) / 8);
         page = Math.max(0, Math.min(page, pages - 1));
         for (int i = page * 8; i < Math.min(files.size(), (page + 1) * 8); i++) {
            String name = files.get(i).getFileName().toString();
            int y = 60 + (i - page * 8) * 23;
            addRenderableWidget(Button.builder(Component.literal(name), button -> openFile(name))
               .bounds(8, y, 100, 20).build());
         }
         addRenderableWidget(Button.builder(Component.literal("<"), button -> { page--; recreateWidgets(); })
            .bounds(8, height - 42, 44, 20).build());
         addRenderableWidget(Button.builder(Component.literal(">"), button -> { page++; recreateWidgets(); })
            .bounds(64, height - 42, 44, 20).build());
      } catch (IOException error) {
         status = "Не удалось прочитать папку: " + error.getMessage();
      }
   }

   private void recreateWidgets() {
      selected = fileName.getValue();
      draft = editor.getValue();
      clearWidgets();
      init();
   }

   private void rebuildLineMarkers() {
      if (editor == null) return;
      List<LineMarker> next = new ArrayList<>();
      int row = 0;
      int number = 1;
      for (String line : editor.getValue().split("\\n", -1)) {
         next.add(new LineMarker(number++, row));
         row += Math.max(1, font.split(Component.literal(line), editor.getWidth() - 8).size());
      }
      lineMarkers = List.copyOf(next);
   }

   private void openFile(String name) {
      if (dirty) { status = "Сначала сохраните текущие изменения"; return; }
      try {
         Path root = StoryWorkspace.scripts().toRealPath();
         Path path = root.resolve(name);
         if (Files.isSymbolicLink(path)) throw new IOException("Ссылки вместо Lua-файла запрещены");
         String content = Files.readString(path, StandardCharsets.UTF_8);
         if (content.length() > MAX_SOURCE) throw new IOException("Файл слишком большой для редактора");
         selected = name;
         fileName.setValue(name);
         editor.setValue(content);
         draft = content;
         dirty = false;
         status = "Открыт " + name;
      } catch (IOException error) { status = "Ошибка чтения: " + error.getMessage(); }
   }

   private String checkedName() {
      String name = fileName.getValue().trim();
      if (!name.matches("[0-9]+\\.lua")) throw new IllegalArgumentException("Имя должно быть числовым: 900000.lua");
      return name;
   }

   private void validateDraft() {
      try {
         String name = checkedName();
         LuaStory story = LuaStory.parse(editor.getValue(), name);
         if (!name.equals(story.id() + ".lua")) throw new IllegalArgumentException("Имя файла не совпадает с story(" + story.id() + ")");
         status = "Синтаксис верный, строк: " + editor.getValue().lines().count();
      } catch (RuntimeException error) { status = "Ошибка: " + error.getMessage(); }
   }

   private void saveDraft() {
      if (minecraft == null || minecraft.getSingleplayerServer() == null) return;
      String name;
      try {
         name = checkedName();
         LuaStory story = LuaStory.parse(editor.getValue(), name);
         if (!name.equals(story.id() + ".lua")) throw new IllegalArgumentException("Имя файла не совпадает с story(" + story.id() + ")");
      } catch (RuntimeException error) { status = "Ошибка: " + error.getMessage(); return; }
      try {
         Path root = StoryWorkspace.scripts().toRealPath();
         Path target = root.resolve(name);
         StoryFileWriter.write(target, editor.getValue(), true);
         selected = name;
         dirty = false;
         status = "Сохранён " + name + "; обновляю сценарий...";
         MinecraftServer server = minecraft.getSingleplayerServer();
         server.execute(() -> {
            String result;
            try { IStoryServer.get(server).getScriptEnv().reloadLuaStory(name); result = "Сценарий " + name + " обновлён"; }
            catch (Exception error) { result = "Файл сохранён, перезагрузка не удалась: " + error.getMessage(); }
            String message = result;
            Minecraft.getInstance().execute(() -> status = message);
         });
      } catch (IOException error) { status = "Ошибка сохранения: " + error.getMessage(); }
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      graphics.drawCenteredString(font, title, width / 2, 12, 0x42C7FF);
      if (editor != null) {
         graphics.enableScissor(118, 60, EDIT_X - 4, height - 48);
         for (LineMarker marker : lineMarkers) {
            int y = 62 + marker.visualRow() * 9 - (int) editor.scroll();
            if (y >= 58 && y < height - 48) graphics.drawString(font, Integer.toString(marker.number()), 120, y, 0x888888, false);
         }
         graphics.disableScissor();
      }
      graphics.drawString(font, status, EDIT_X, height - 17, dirty ? 0xFFCC66 : 0xAAAAAA, false);
      super.render(graphics, mouseX, mouseY, partialTick);
   }

   private static final class EditorBox extends MultiLineEditBox {
      private EditorBox(net.minecraft.client.gui.Font font, int x, int y, int width, int height,
                        Component placeholder, Component label) {
         super(font, x, y, width, height, placeholder, label);
      }
      private double scroll() { return scrollAmount(); }
   }

   private record LineMarker(int number, int visualRow) { }

   @Override public boolean isPauseScreen() { return false; }
}
