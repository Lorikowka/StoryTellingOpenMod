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
   private int sidebarWidth;
   private int editX;
   private int editY;
   private int listRows;
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
      sidebarWidth = Math.max(104, Math.min(176, width / 4));
      editX = sidebarWidth + 34;
      editY = 58;
      listRows = Math.max(2, (height - 112) / 23);
      int editWidth = Math.max(80, width - editX - 10);
      fileName = addRenderableWidget(new EditBox(font, editX, 31, Math.min(180, editWidth), 20, Component.literal("Файл")));
      fileName.setMaxLength(32);
      fileName.setValue(selected);
      editor = addRenderableWidget(new EditorBox(font, editX, editY, editWidth, Math.max(60, height - editY - 49),
         Component.literal("Lua"), Component.literal("Код Lua")));
      editor.setCharacterLimit(MAX_SOURCE);
      editor.setValue(draft);
      rebuildLineMarkers();
      editor.setValueListener(value -> { draft = value; dirty = true; rebuildLineMarkers(); });
      addRenderableWidget(Button.builder(Component.literal("Проверить"), button -> validateDraft())
         .bounds(Math.max(editX, width - 212), height - 43, 96, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Сохранить"), button -> saveDraft())
         .bounds(Math.max(editX + 100, width - 110), height - 43, 98, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Новый"), button -> {
         if (dirty) { status = "Сначала сохраните текущие изменения"; return; }
         selected = ""; draft = "story(900000)\n\npart(\"main\")\n";
         fileName.setValue(""); editor.setValue(draft); dirty = true;
      }).bounds(8, 31, sidebarWidth - 16, 20).build());
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
         int pages = Math.max(1, (files.size() + listRows - 1) / listRows);
         page = Math.max(0, Math.min(page, pages - 1));
         for (int i = page * listRows; i < Math.min(files.size(), (page + 1) * listRows); i++) {
            String name = files.get(i).getFileName().toString();
            int y = 67 + (i - page * listRows) * 23;
            addRenderableWidget(Button.builder(Component.literal(name), button -> openFile(name))
               .bounds(8, y, sidebarWidth - 16, 20).build());
         }
         addRenderableWidget(Button.builder(Component.literal("<"), button -> { page--; recreateWidgets(); })
            .bounds(8, height - 43, 44, 20).build());
         addRenderableWidget(Button.builder(Component.literal(">"), button -> { page++; recreateWidgets(); })
            .bounds(sidebarWidth - 52, height - 43, 44, 20).build());
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
      graphics.fill(0, 0, width, 25, 0xEE111827);
      graphics.fill(0, 25, sidebarWidth, height - 21, 0xDD17222E);
      graphics.fill(sidebarWidth, 25, width, height - 21, 0xDD0D141C);
      graphics.fill(0, height - 21, width, height, 0xEE102939);
      graphics.drawString(font, "ST  /  LUA", 9, 9, 0x42C7FF, false);
      graphics.drawString(font, "СЦЕНАРИИ", 9, 56, 0x9BB5C5, false);
      graphics.drawString(font, (dirty ? "● " : "") + (selected.isBlank() ? "Новый файл" : selected),
         editX, 18, dirty ? 0xFFCC66 : 0xDDEEFF, false);
      graphics.drawString(font, "ФАЙЛ", editX - 32, 37, 0x9BB5C5, false);
      graphics.drawString(font, "Lua", editX, editY - 10, 0x62C8EA, false);
      if (editor != null) {
         graphics.fill(sidebarWidth + 1, editY, editX - 2, height - 49, 0xAA17222E);
         graphics.enableScissor(sidebarWidth + 2, editY, editX - 3, height - 49);
         for (LineMarker marker : lineMarkers) {
            int y = editY + 4 + marker.visualRow() * 9 - (int) editor.scroll();
            if (y >= editY && y < height - 49) graphics.drawString(font, Integer.toString(marker.number()),
               editX - 8 - font.width(Integer.toString(marker.number())), y, 0x718899, false);
         }
         graphics.disableScissor();
      }
      graphics.drawString(font, font.plainSubstrByWidth(status, width - 16), 8, height - 15,
         dirty ? 0xFFCC66 : 0xB7D3E4, false);
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
