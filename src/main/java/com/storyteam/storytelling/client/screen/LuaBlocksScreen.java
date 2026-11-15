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

/** A linear block workspace. Blocks are selected and reordered directly on the canvas. */
public final class LuaBlocksScreen extends Screen {
   private static final int CARD_HEIGHT = 34;
   private LuaBlockProject project;
   private EditBox idBox;
   private EditBox valueBox;
   private int selected = -1;
   private int dragging = -1;
   private int page;
   private int paletteWidth;
   private int canvasX;
   private int canvasWidth;
   private int inspectorX;
   private int inspectorWidth;
   private int visibleRows;
   private String status = "Добавьте блок слева и соберите последовательность в центре";

   public LuaBlocksScreen() { super(Component.literal("Блоки сценария Storytelling")); }

   @Override protected void init() {
      if (minecraft == null || minecraft.getSingleplayerServer() == null) { onClose(); return; }
      if (project == null) try { project = LuaBlockProject.open(900100); }
      catch (IOException | RuntimeException error) { status = "Ошибка проекта: " + error.getMessage(); return; }

      paletteWidth = Math.max(102, Math.min(148, width / 5));
      inspectorWidth = Math.max(124, Math.min(190, width / 4));
      inspectorX = width - inspectorWidth;
      canvasX = paletteWidth + 8;
      canvasWidth = Math.max(60, inspectorX - canvasX - 8);
      visibleRows = Math.max(2, (height - 132) / CARD_HEIGHT);
      page = Math.max(0, Math.min(page, maxPage()));

      idBox = addRenderableWidget(new EditBox(font, 9, 31, paletteWidth - 18, 20, Component.literal("ID")));
      idBox.setMaxLength(9);
      idBox.setValue(Integer.toString(project.id()));
      addRenderableWidget(Button.builder(Component.literal("Открыть ID"), button -> openId())
         .bounds(canvasX, 31, Math.min(94, canvasWidth), 20).build());

      int paletteY = 82;
      for (Kind kind : Kind.values()) {
         addRenderableWidget(Button.builder(Component.literal("+ " + label(kind)), button -> addBlock(kind))
            .bounds(9, paletteY, paletteWidth - 18, 20).build());
         paletteY += 24;
      }

      valueBox = addRenderableWidget(new EditBox(font, inspectorX + 8, 89, inspectorWidth - 16, 20,
         Component.literal("Значение блока")));
      valueBox.setMaxLength(1024);
      valueBox.active = selected >= 0 && selected < project.blocks().size();
      if (valueBox.active) valueBox.setValue(project.blocks().get(selected).value());
      valueBox.setResponder(value -> {
         if (selected >= 0 && selected < project.blocks().size()) {
            Block current = project.blocks().get(selected);
            project.blocks().set(selected, new Block(current.kind(), value));
         }
      });
      int controlWidth = (inspectorWidth - 20) / 2;
      addRenderableWidget(Button.builder(Component.literal("Выше"), button -> move(-1))
         .bounds(inspectorX + 8, 119, controlWidth, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Ниже"), button -> move(1))
         .bounds(inspectorX + 12 + controlWidth, 119, controlWidth, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Удалить блок"), button -> remove())
         .bounds(inspectorX + 8, 144, inspectorWidth - 16, 20).build());
      addRenderableWidget(Button.builder(Component.literal("Сохранить Lua"), button -> save())
         .bounds(inspectorX + 8, height - 47, inspectorWidth - 16, 20).build());

      addRenderableWidget(Button.builder(Component.literal("<"), button -> { page = Math.max(0, page - 1); refresh(); })
         .bounds(canvasX, height - 47, 28, 20).build());
      addRenderableWidget(Button.builder(Component.literal(">"), button -> { page = Math.min(maxPage(), page + 1); refresh(); })
         .bounds(canvasX + canvasWidth - 28, height - 47, 28, 20).build());
   }

   private int maxPage() { return Math.max(0, (project.blocks().size() - 1) / visibleRows); }

   private static String label(Kind kind) {
      return switch (kind) {
         case SPEECH -> "Реплика";
         case WAIT_F8 -> "Ждать F8";
         case WAIT_TICKS -> "Пауза";
         case ANCHOR -> "Якорь";
         case BLACKOUT -> "Затемнение";
      };
   }

   private static int accent(Kind kind) {
      return switch (kind) {
         case SPEECH -> 0xFF4BB6E8;
         case WAIT_F8, WAIT_TICKS -> 0xFFF2BA61;
         case ANCHOR -> 0xFF91CE8D;
         case BLACKOUT -> 0xFFC1A0DA;
      };
   }

   private void openId() {
      try {
         int id = Integer.parseInt(idBox.getValue());
         project = LuaBlockProject.open(id);
         selected = -1;
         page = 0;
         status = "Открыт проект " + id;
         refresh();
      } catch (IOException | RuntimeException error) { status = "Ошибка: " + error.getMessage(); }
   }

   private void select(int index) {
      selected = index;
      valueBox.active = true;
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
      page = selected / visibleRows;
      refresh();
   }

   private void move(int delta) {
      if (selected < 0 || selected + delta < 0 || selected + delta >= project.blocks().size()) return;
      Block other = project.blocks().get(selected + delta);
      project.blocks().set(selected + delta, project.blocks().get(selected));
      project.blocks().set(selected, other);
      selected += delta;
      page = selected / visibleRows;
      refresh();
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

   private int cardAt(double mouseX, double mouseY) {
      if (mouseX < canvasX || mouseX >= canvasX + canvasWidth || mouseY < 75) return -1;
      int row = ((int) mouseY - 75) / CARD_HEIGHT;
      if (row < 0 || row >= visibleRows) return -1;
      int index = page * visibleRows + row;
      return index < project.blocks().size() ? index : -1;
   }

   @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         int index = cardAt(mouseX, mouseY);
         if (index >= 0) { select(index); dragging = index; return true; }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (button == 0 && dragging >= 0) {
         int target = cardAt(mouseX, mouseY);
         if (target >= 0 && target != dragging) {
            Block moved = project.blocks().remove(dragging);
            project.blocks().add(target, moved);
            selected = target;
            status = "Блок перемещён на позицию " + (target + 1);
            refresh();
         }
         dragging = -1;
         return true;
      }
      return super.mouseReleased(mouseX, mouseY, button);
   }

   @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      renderBackground(graphics);
      if (project == null) {
         graphics.drawCenteredString(font, status, width / 2, height / 2, 0xFFCC66);
         return;
      }
      graphics.fill(0, 0, width, 25, 0xEE111827);
      graphics.fill(0, 25, paletteWidth, height - 21, 0xDD17222E);
      graphics.fill(paletteWidth, 25, inspectorX, height - 21, 0xDD0D141C);
      graphics.fill(inspectorX, 25, width, height - 21, 0xDD17222E);
      graphics.fill(0, height - 21, width, height, 0xEE102939);
      graphics.drawString(font, "ST  /  БЛОКИ", 9, 9, 0x42C7FF, false);
      graphics.drawString(font, "Сценарий " + project.id(), canvasX, 9, 0xDDEEFF, false);
      graphics.drawString(font, "БИБЛИОТЕКА", 9, 65, 0x9BB5C5, false);
      graphics.drawString(font, "ПОСЛЕДОВАТЕЛЬНОСТЬ", canvasX, 61, 0x9BB5C5, false);
      graphics.drawString(font, "СВОЙСТВА", inspectorX + 8, 62, 0x9BB5C5, false);
      graphics.drawString(font, "Значение", inspectorX + 8, 77, 0xB7D3E4, false);
      int start = page * visibleRows;
      if (project.blocks().isEmpty()) {
         graphics.drawString(font, "Добавьте первый блок слева", canvasX + 8, 92, 0x718899, false);
      }
      for (int row = 0; row < visibleRows && start + row < project.blocks().size(); row++) {
         int index = start + row;
         Block block = project.blocks().get(index);
         int y = 75 + row * CARD_HEIGHT;
         graphics.fill(canvasX, y, canvasX + canvasWidth, y + 29,
            index == selected ? 0xEE2B4152 : 0xDD1B2B36);
         graphics.fill(canvasX, y, canvasX + 3, y + 29, accent(block.kind()));
         String heading = (index + 1) + ". " + label(block.kind());
         graphics.drawString(font, font.plainSubstrByWidth(heading, canvasWidth - 13),
            canvasX + 8, y + 4, accent(block.kind()), false);
         String detail = block.value().isBlank() ? "Продолжить по F8" : block.value();
         graphics.drawString(font, font.plainSubstrByWidth(detail, canvasWidth - 13),
            canvasX + 8, y + 16, 0xC0CBD1, false);
         if (index + 1 < project.blocks().size() && row + 1 < visibleRows)
            graphics.fill(canvasX + canvasWidth / 2, y + 29, canvasX + canvasWidth / 2 + 1, y + CARD_HEIGHT, 0xFF557388);
      }
      graphics.drawCenteredString(font, "Страница " + (page + 1) + "/" + (maxPage() + 1),
         canvasX + canvasWidth / 2, height - 41, 0xA5B8C5);
      if (selected < 0) graphics.drawString(font, "Выберите блок на поле", inspectorX + 8, 177, 0x8299AA, false);
      else graphics.drawString(font, "Перетащите блок для смены порядка", inspectorX + 8, 177, 0x8299AA, false);
      graphics.drawString(font, font.plainSubstrByWidth(status, width - 16), 8, height - 15,
         0xB7D3E4, false);
      super.render(graphics, mouseX, mouseY, partialTick);
   }

   @Override public boolean isPauseScreen() { return false; }
}
