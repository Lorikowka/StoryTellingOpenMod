package com.storyteam.storytelling.client.screen;

import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.script.story.LuaBlockProject;
import com.storyteam.storytelling.script.story.LuaBlockProject.Block;
import com.storyteam.storytelling.script.story.LuaBlockProject.Kind;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

/** A limited visual sequence editor. It exports validated Lua and protects manual edits. */
public final class LuaBlocksScreen extends Screen {
   private LuaBlockProject project;
   private EditBox idBox;
   private EditBox valueBox;
   private int selected = -1;
   private int page;
   private String status = "Выберите блок, задайте значение и сохраните проект";

   public LuaBlocksScreen() { super(Component.literal("Блоки сценария Storytelling")); }

   @Override protected void init() {
      if (minecraft == null || minecraft.getSingleplayerServer() == null) { onClose(); return; }
      if (project == null) try { project = LuaBlockProject.open(900100); }
      catch (IOException | RuntimeException error) { status = "Ошибка проекта: " + error.getMessage(); return; }
      page = Math.max(0, Math.min(page, Math.max(0, (project.blocks().size() - 1) / 8)));
      int rightX = 240;
      int rightWidth = Math.max(130, width - rightX - 12);
      idBox = addRenderableWidget(new EditBox(font, 8, 32, 105, 20, Component.literal("ID")));
      idBox.setMaxLength(9);
      idBox.setValue(Integer.toString(project.id()));
      addRenderableWidget(Button.builder(Component.literal("Открыть ID"), button -> openId())
         .bounds(119, 32, 108, 20).build());
      valueBox = addRenderableWidget(new EditBox(font, rightX, 78, rightWidth, 20, Component.literal("Значение блока")));
      valueBox.setMaxLength(1024);
      if (selected >= 0 && selected < project.blocks().size()) valueBox.setValue(project.blocks().get(selected).value());
      valueBox.setResponder(value -> {
         if (selected >= 0 && selected < project.blocks().size()) {
            Block current = project.blocks().get(selected);
            project.blocks().set(selected, new Block(current.kind(), value));
         }
      });
      Kind[] kinds = Kind.values();
      for (int i = 0; i < kinds.length; i++) {
         Kind kind = kinds[i];
         int x = rightX + (i % 2) * Math.max(100, rightWidth / 2);
         int y = 112 + (i / 2) * 24;
         addRenderableWidget(Button.builder(Component.literal("+ " + label(kind)), button -> addBlock(kind))
            .bounds(x, y, Math.max(96, rightWidth / 2 - 4), 20).build());
      }
      addRenderableWidget(Button.builder(Component.literal("Выше"), button -> move(-1))
         .bounds(rightX, 200, 76, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Ниже"), button -> move(1))
         .bounds(rightX + 80, 200, 76, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Удалить"), button -> remove())
         .bounds(rightX + 160, 200, 76, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Собрать Lua и сохранить"), button -> save())
         .bounds(rightX, height - 43, Math.min(rightWidth, 230), 20).build());
      addRenderableWidget(Button.builder(Component.literal("<"), button -> { page = Math.max(0, page - 1); refresh(); })
         .bounds(8, height - 43, 44, 20).build());
      addRenderableWidget(Button.builder(Component.literal(">"), button -> { page++; refresh(); })
         .bounds(182, height - 43, 44, 20).build());
      int from = page * 8;
      for (int i = from; i < Math.min(project.blocks().size(), from + 8); i++) {
         int index = i;
         Block block = project.blocks().get(i);
         String text = (i + 1) + ". " + label(block.kind());
         addRenderableWidget(Button.builder(Component.literal(text), button -> select(index))
            .bounds(8, 65 + (i - from) * 24, 218, 20).build());
      }
   }

   private static String label(Kind kind) {
      return switch (kind) {
         case SPEECH -> "Реплика";
         case WAIT_F8 -> "Ждать F8";
         case WAIT_TICKS -> "Пауза";
         case ANCHOR -> "Якорь";
         case BLACKOUT -> "Затемнение";
      };
   }

   private void openId() {
      try {
         int id = Integer.parseInt(idBox.getValue());
         project = LuaBlockProject.open(id);
         selected = -1; page = 0;
         status = "Открыт проект " + id;
         refresh();
      } catch (IOException | RuntimeException error) { status = "Ошибка: " + error.getMessage(); }
   }

   private void select(int index) {
      selected = index;
      valueBox.setValue(project.blocks().get(index).value());
      status = "Блок " + (index + 1) + ": " + label(project.blocks().get(index).kind());
   }

   private void addBlock(Kind kind) {
      String value = switch (kind) {
         case SPEECH -> "Новая реплика";
         case WAIT_F8 -> "";
         case WAIT_TICKS, BLACKOUT -> "20";
         case ANCHOR -> "scene_start";
      };
      project.blocks().add(new Block(kind, value));
      selected = project.blocks().size() - 1;
      page = selected / 8;
      refresh();
   }

   private void move(int delta) {
      if (selected < 0 || selected + delta < 0 || selected + delta >= project.blocks().size()) return;
      Block other = project.blocks().get(selected + delta);
      project.blocks().set(selected + delta, project.blocks().get(selected));
      project.blocks().set(selected, other);
      selected += delta; page = selected / 8; refresh();
   }

   private void remove() {
      if (selected < 0 || selected >= project.blocks().size()) return;
      project.blocks().remove(selected);
      selected = Math.min(selected, project.blocks().size() - 1);
      refresh();
   }

   private void refresh() { clearWidgets(); init(); }

   private void save() {
      try {
         project.save();
         String name = project.id() + ".lua";
         status = "Lua и проект сохранены; обновляю сценарий...";
         MinecraftServer server = minecraft.getSingleplayerServer();
         server.execute(() -> {
            String result;
            try { IStoryServer.get(server).getScriptEnv().reloadLuaStory(name); result = "Сценарий " + name + " обновлён"; }
            catch (Exception error) { result = "Файл сохранён, перезагрузка не удалась: " + error.getMessage(); }
            String message = result;
            Minecraft.getInstance().execute(() -> status = message);
         });
      } catch (IOException | RuntimeException error) { status = "Ошибка сохранения: " + error.getMessage(); }
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      graphics.drawCenteredString(font, title, width / 2, 11, 0x42C7FF);
      graphics.drawString(font, "ID проекта", 8, 20, 0xAAAAAA, false);
      graphics.drawString(font, "Значение выбранного блока", 240, 64, 0xAAAAAA, false);
      graphics.drawString(font, status, 240, height - 17, 0xFFCC66, false);
      super.render(graphics, mouseX, mouseY, partialTick);
   }

   @Override public boolean isPauseScreen() { return false; }
}
