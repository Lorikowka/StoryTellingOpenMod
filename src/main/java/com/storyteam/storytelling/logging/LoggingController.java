package com.storyteam.storytelling.logging;

import com.google.common.collect.Lists;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.network.StoryServer;
import com.storyteam.storytelling.network.packets.DiscordLogPacket;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.util.Color;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.mcf.ModHelper;

public class LoggingController {
   public static final String buildMark = null;
   @Nullable
   public static UUID uuid;
   @Nullable
   public static String nickname;
   public static final List<Integer> whitelistedScripts = Lists.newArrayList();
   public static UUID loloUUID = UUID.fromString("060c397d-3b53-4a87-aca1-dd06388bd4d0");
   private static LoggingController instance;
   boolean isLololowka;

   public LoggingController() {
      instance = this;
      this.isLololowka = Objects.equals(uuid, loloUUID) && FMLEnvironment.production;
      this.send(
         new WebhookEmbed()
            .setColor(new Color(0, 247, 255).getRGB())
            .setTitle("Запуск клиента")
            .addField(new EmbedField(true, "Версия", ModHelper.getModVersion("storytelling")))
            .addField(new EmbedField(true, "Имя", Storytelling.PROXY.getIngameName()))
      );
   }

   public static boolean isAllowedToLog(ScriptInstance script) {
      return script.getId() <= -2000 || script.getId() > -1000 || whitelistedScripts.contains(script.getId());
   }

   @Nullable
   public static String resolveName() {
      if (uuid == null) {
         return null;
      } else if (ModHelper.inDev()) {
         return System.getProperty("discord.name.override", System.getProperty("user.name"));
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append(nickname);
         if (buildMark != null) {
            sb.append("(Билд для ").append(buildMark).append(")");
         }

         return sb.toString();
      }
   }

   public void send(WebhookEmbed messageBuilder) {
      if (!Boolean.parseBoolean(System.getenv("NoJDA"))) {
         StoryServer.getInstance().sendPacket(new DiscordLogPacket(messageBuilder));
      }
   }

   public static void captureThreadsDump() {
      StringBuilder stackString = new StringBuilder();
      Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
      stackString.append(String.format("%-15s \t %-15s \t %-15s \t %s\n", "Name", "State", "Priority", "isDaemon"));

      for (Thread t : map.keySet()) {
         stackString.append(String.format("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon()));
      }

      stackString.append("\n\n");

      for (Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
         stackString.append(String.format("Thread Name: " + entry.getKey().getName()));
         StackTraceElement[] stackTraceElements = entry.getValue();

         for (StackTraceElement stackTraceElement : stackTraceElements) {
            stackString.append("\n").append(stackTraceElement);
         }

         stackString.append("\n\n");
      }
   }

   public static void captureSaveState(@NotNull ScriptInstance scriptInstance, String name, @Nullable String performer) {
      if (isAllowedToLog(scriptInstance)) {
         if (instance != null) {
            instance.send(
               new WebhookEmbed()
                  .setColor(new Color(42, 250, 42).getRGB())
                  .setTitle("Достигнуто сохранение")
                  .addField(new EmbedField(true, "Скрипт", String.valueOf(scriptInstance.getId())))
                  .addField(new EmbedField(true, "Сохранение", name))
                  .addField(new EmbedField(true, "Исполнитель", performer == null ? "неизвестный" : performer))
            );
         }
      }
   }

   public static void capturePerformerLabel(@NotNull ScriptInstance scriptInstance, String name, @Nullable String performer) {
      if (isAllowedToLog(scriptInstance)) {
         if (instance != null) {
            instance.send(
               new WebhookEmbed()
                  .setColor(new Color(42, 250, 42).getRGB())
                  .setTitle("Достигнута метка")
                  .addField(new EmbedField(true, "Скрипт", String.valueOf(scriptInstance.getId())))
                  .addField(new EmbedField(true, "Метка", name))
                  .addField(new EmbedField(true, "Исполнитель", performer == null ? "неизвестный" : performer))
            );
         }
      }
   }

   public static void captureCommand(String text) {
      if (instance != null) {
         instance.send(new WebhookEmbed().setColor(new Color(255, 91, 0).getRGB()).setTitle("Введена команда").setDescription(text));
      }
   }

   public static void captureChat(String text) {
      if (instance != null) {
         instance.send(new WebhookEmbed().setColor(new Color(210, 16, 229).getRGB()).setTitle("Отправлено сообщение").setDescription(text));
      }
   }

   public static void captureLoad(Int2ObjectArrayMap<ScriptInstance> scripts) {
      if (instance != null) {
         instance.send(
            new WebhookEmbed()
               .setColor(new Color(0, 0, 0).getRGB())
               .setTitle("Вход в мир")
               .addField(
                  new EmbedField(
                     true,
                     "Активные скрипты",
                     scripts.keySet().stream().filter(id -> id > -1000).sorted().map(String::valueOf).collect(Collectors.joining(", "))
                  )
               )
               .addField(
                  new EmbedField(
                     true,
                     "Активные фоновые скрипты",
                     scripts.keySet()
                        .stream()
                        .filter(id -> id <= -1000)
                        .sorted(Comparator.reverseOrder())
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                  )
               )
         );
      }
   }

   public static void captureHelp(Int2ObjectArrayMap<ScriptInstance> scripts) {
      if (instance != null) {
         StringBuilder msgBuilder = new StringBuilder();
         msgBuilder.append("**__Активные скрипты__:**\n\n");
         scripts.values().stream().filter(i -> i.getId() > -1000).sorted(Comparator.comparingInt(i -> i.getId())).forEach(active -> {
            ScriptPerformer mainPerformer = active.getMainPerformer();
            String report;
            if (mainPerformer == null) {
               report = "Отсутствует главный исполнитель.";
            } else {
               report = mainPerformer.gatherHelpReport();
            }

            msgBuilder.append("**Скрипт** `").append(active.getId()).append("` => ").append(report).append("\n\n");
         });
         msgBuilder.append("**__Фоновые скрипты:__**\n\n");
         scripts.values().stream().filter(i -> i.getId() <= -1000).sorted(Comparator.comparingInt(i -> i.getId())).forEach(bg -> {
            ScriptPerformer mainPerformer = bg.getMainPerformer();
            String report;
            if (mainPerformer == null) {
               report = "Отсутствует главный исполнитель.";
            } else {
               report = mainPerformer.gatherHelpReport();
            }

            msgBuilder.append("**Скрипт** `").append(bg.getId()).append("` => ").append(report).append("\n\n");
         });
         String msgBody = msgBuilder.toString();
         boolean title = false;
         String[] lns = msgBody.split("\n");
         StringBuilder jdaBuilder = new StringBuilder();

         for (String ln : lns) {
            if (jdaBuilder.length() + ln.length() >= 4000) {
               WebhookEmbed builder = new WebhookEmbed().setColor(new Color(50, 180, 190).getRGB());
               if (!title) {
                  title = true;
                  builder = builder.setTitle("Вызвана вспомогательная команда статуса активных скриптов");
               }

               builder = builder.setDescription(jdaBuilder.toString());
               instance.send(builder);
               jdaBuilder = new StringBuilder();
            }

            jdaBuilder.append(ln + "\n");
         }

         if (jdaBuilder.length() > 0) {
            WebhookEmbed builder = new WebhookEmbed().setColor(new Color(50, 180, 190).getRGB());
            if (!title) {
               builder = builder.setTitle("Вызвана вспомогательная команда статуса активных скриптов");
            }

            builder = builder.setDescription(jdaBuilder.toString());
            instance.send(builder);
         }
      }
   }

   public static void captureUnload() {
      if (instance != null) {
         instance.send(new WebhookEmbed().setColor(new Color(255, 255, 255).getRGB()).setTitle("Выход из мира"));
      }
   }

   public static void captureDebug(String text) {
      if (instance != null) {
         instance.send(new WebhookEmbed().setColor(new Color(137, 167, 31).getRGB()).setTitle(text));
      }
   }
}
