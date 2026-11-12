package com.storyteam.storytelling.system._core.cloud;

import java.io.File;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import org.zeith.hammerlib.util.java.Cast;

public class CIOMessages {
   public static MutableComponent infor(String text) {
      return Component.literal("§6§l[§e§l>§6§l] §r" + text);
   }

   public static Supplier<Component> info(String text) {
      return Cast.constant(Component.literal("§6§l[§e§l>§6§l] §r" + text));
   }

   public static Supplier<Component> info(String text, UnaryOperator<MutableComponent> op) {
      return Cast.constant((Component)op.apply(Component.literal("§6§l[§e§l>§6§l] §r" + text)));
   }

   public static MutableComponent open(File target) {
      return open(null, target);
   }

   public static MutableComponent open(String name, File target) {
      return Component.literal(name != null ? name : target.getName())
         .withStyle(
            Style.EMPTY
               .withHoverEvent(
                  new HoverEvent(Action.SHOW_TEXT, Component.literal("Нажмите, что бы открыть ").append(target.isDirectory() ? "папку" : "файл").append("."))
               )
               .withClickEvent(new ClickEvent(net.minecraft.network.chat.ClickEvent.Action.OPEN_FILE, target.getAbsolutePath()))
               .withColor(ChatFormatting.BLUE)
               .withUnderlined(true)
         );
   }

   public static MutableComponent command(String title, String tooltip, String command) {
      if (!command.startsWith("/")) {
         command = "/" + command;
      }

      HoverEvent he = tooltip != null ? new HoverEvent(Action.SHOW_TEXT, Component.literal(tooltip)) : null;
      return Component.literal(title)
         .setStyle(
            Style.EMPTY
               .withUnderlined(true)
               .withColor(ChatFormatting.BLUE)
               .withClickEvent(new ClickEvent(net.minecraft.network.chat.ClickEvent.Action.RUN_COMMAND, command))
               .withHoverEvent(he)
         );
   }

   public static MutableComponent url(String uri) {
      return Component.literal(uri)
         .withStyle(
            Style.EMPTY
               .withHoverEvent(new HoverEvent(Action.SHOW_TEXT, Component.literal("Нажми, что бы открыть URL")))
               .withClickEvent(new ClickEvent(net.minecraft.network.chat.ClickEvent.Action.OPEN_URL, uri))
               .withColor(ChatFormatting.BLUE)
               .withUnderlined(true)
         );
   }
}
