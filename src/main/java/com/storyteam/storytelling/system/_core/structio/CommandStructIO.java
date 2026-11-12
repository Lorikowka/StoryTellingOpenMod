package com.storyteam.storytelling.system._core.structio;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.content.commands.arguments.CloudStructureFileArgument;
import com.storyteam.storytelling.content.commands.arguments.StructureFileArgument;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system._core.cloud.CIOMessages;
import com.storyteam.storytelling.system._core.structio.repo.StructureRepository;
import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import com.storyteam.storytelling.util.IntegerLocalizer;
import com.storyteam.storytelling.util.world.Location;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

@EventBusSubscriber
public class CommandStructIO {
   public static final List<AABB> EXCLUSION_ZONES = new ArrayList<>();

   @SubscribeEvent
   public static void serverShutDayn(ServerStoppingEvent e) {
      EXCLUSION_ZONES.clear();
   }

   public static boolean allowedByExclusions(BlockPos pos) {
      return EXCLUSION_ZONES.stream().noneMatch(r -> r.contains(Vec3.atCenterOf(pos)));
   }

   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static Component createCloudUploadComponent(String name) {
      return CIOMessages.command("Загрузить структуру в облако..", "Нажми, что бы выгрузить структуру в облако.", "/st structure cloud_upload " + name);
   }

   public static Component createExportOverrideComponentPositional(String name, BlockPos from, BlockPos to) {
      return CIOMessages.command(
         "Перезаписать?",
         "Нажми, что бы перезаписать существующую структуру.",
         "/st structure export "
            + from.getX()
            + " "
            + from.getY()
            + " "
            + from.getZ()
            + " "
            + to.getX()
            + " "
            + to.getY()
            + " "
            + to.getZ()
            + " "
            + name
            + " true"
      );
   }

   public static int importStructure(CommandContext<CommandSourceStack> cs, boolean async) {
      MinecraftServer server = ((CommandSourceStack)cs.getSource()).getServer();
      String name = StringArgumentType.getString(cs, "name");
      ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Импорт файла структуры §7\"" + name + ".dat\"§r..."), true);
      Runnable sendFinish = () -> ((CommandSourceStack)cs.getSource())
            .sendSuccess(CIOMessages.info("Импорт файла структуры §7\"" + name + ".dat\"§r завершён!"), true);
      if (async) {
         Tuple3<AABB, ServerLevel, CompletableFuture<Void>> tuple = AsyncStructIO.importBuilding(server, name);
         ((CompletableFuture)tuple.c()).thenRun(sendFinish);
      } else {
         StructIO.importBuilding(server, name);
         sendFinish.run();
      }

      return 1;
   }

   public static int exportStructure(CommandContext<CommandSourceStack> cs, BlockPos from, BlockPos to, boolean override) {
      String name = StringArgumentType.getString(cs, "name");
      if (Files.isRegularFile(StructIO.getStructurePath(name)) && !override) {
         ((CommandSourceStack)cs.getSource())
            .sendSuccess(
               CIOMessages.info(
                  "Файл структуры §7\"" + name + ".dat\"§r уже существует. ", c -> c.append(createExportOverrideComponentPositional(name, from, to))
               ),
               true
            );
         return 0;
      } else {
         ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Экспорт файла структуры §7\"" + name + ".dat\"§r..."), true);
         StructIO.exportBuilding(((CommandSourceStack)cs.getSource()).getLevel(), from, to, name, p -> {
         }, CommandStructIO::allowedByExclusions);
         Component uploadComp = createCloudUploadComponent(name);
         ((CommandSourceStack)cs.getSource())
            .sendSuccess(CIOMessages.info("Экспорт файла структуры §7\"" + name + ".dat\"§r завершён! ", c -> c.append(uploadComp)), true);
         return 1;
      }
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                          "structure"
                                       )
                                       .then(
                                          Commands.literal("import")
                                             .then(
                                                ((RequiredArgumentBuilder)Commands.argument("name", StructureFileArgument.structure())
                                                      .then(Commands.argument("async", BoolArgumentType.bool()).executes(cs -> {
                                                         boolean async = BoolArgumentType.getBool(cs, "async");
                                                         return importStructure(cs, async);
                                                      })))
                                                   .executes(cs -> importStructure(cs, false))
                                             )
                                       ))
                                                                         .then(((LiteralArgumentBuilder)Commands.literal("excludes")
                                        .then(Commands.literal("add")
                                           .then(Commands.argument("from", BlockPosArgument.blockPos())
                                              .then(Commands.argument("to", BlockPosArgument.blockPos()).executes(cs -> {
                                                 BlockPos from = BlockPosArgument.getLoadedBlockPos(cs, "from");
                                                 BlockPos to = BlockPosArgument.getLoadedBlockPos(cs, "to");
                                                 EXCLUSION_ZONES.add(new AABB(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()) + 1, Math.max(from.getY(), to.getY()) + 1, Math.max(from.getZ(), to.getZ()) + 1));
                                                 ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Добавлена маска-исключение №" + EXCLUSION_ZONES.size()), true);
                                                 return 1;
                                              }))))
                                        .then(Commands.literal("clear").executes(cs -> {
                                           int count = EXCLUSION_ZONES.size();
                                           EXCLUSION_ZONES.clear();
                                           ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Очистка маски: удалено " + count + " зон."), true);
                                           return count;
                                        })))
.then(
                                    ((LiteralArgumentBuilder)Commands.literal("export")
                                          .then(
                                          Commands.argument("from", BlockPosArgument.blockPos())
                                             .then(
                                                Commands.argument("to", BlockPosArgument.blockPos())
                                                   .then(((RequiredArgumentBuilder)Commands.argument("name", StringArgumentType.string()).executes(cs -> {
                                                      BlockPos from = BlockPosArgument.getLoadedBlockPos(cs, "from");
                                                      BlockPos to = BlockPosArgument.getLoadedBlockPos(cs, "to");
                                                      return exportStructure(cs, from, to, false);
                                                   })).then(Commands.argument("override", BoolArgumentType.bool()).executes(cs -> {
                                                      BlockPos from = BlockPosArgument.getLoadedBlockPos(cs, "from");
                                                      BlockPos to = BlockPosArgument.getLoadedBlockPos(cs, "to");
                                                      boolean override = BoolArgumentType.getBool(cs, "override");
                                                      return exportStructure(cs, from, to, override);
                                                   })))
                                             )
                                       )
                                 ))
                              .then(
                                 Commands.literal("reexport")
                                    .then(
                                       Commands.argument("name", StructureFileArgument.structure())
                                          .executes(
                                             cs -> {
                                                String name = StringArgumentType.getString(cs, "name");
                                                StructureST tag = StructIO.readStructure(((CommandSourceStack)cs.getSource()).registryAccess(), name);
                                                if (tag == null) {
                                                   return 1;
                                                } else {
                                                   ((CommandSourceStack)cs.getSource())
                                                      .sendSuccess(CIOMessages.info("Обновление файла структуры §7\"" + name + ".dat\"§r..."), true);
                                                   StructIO.reexportBuilding(
                                                      ((CommandSourceStack)cs.getSource()).getLevel(),
                                                      tag.sections().map(s -> Tuples.immutable(s.min, s.max)),
                                                      name,
                                                      tag.getMetadata().revision + 1
                                                   );
                                                   Component uploadComp = createCloudUploadComponent(name);
                                                   ((CommandSourceStack)cs.getSource())
                                                      .sendSuccess(
                                                         CIOMessages.info("Файл структуры §7\"" + name + ".dat\"§r обновлён§r! ", c -> c.append(uploadComp)),
                                                         true
                                                      );
                                                   return 1;
                                                }
                                             }
                                          )
                                    )
                              ))
                           .then(
                              Commands.literal("select")
                                 .then(
                                    Commands.argument("name", StructureFileArgument.structure())
                                       .executes(
                                          cs -> {
                                             String name = StringArgumentType.getString(cs, "name");
                                             StructureST tag = StructIO.readStructure(((CommandSourceStack)cs.getSource()).registryAccess(), name);
                                             if (tag == null) {
                                                return 1;
                                             } else {
                                                AABB boundaries = tag.boundary();
                                                ((CommandSourceStack)cs.getSource())
                                                   .getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      (CommandSourceStack)cs.getSource(),
                                                      "//pos1 %d,%d,%d".formatted((int)boundaries.minX, (int)boundaries.minY, (int)boundaries.minZ)
                                                   );
                                                ((CommandSourceStack)cs.getSource())
                                                   .getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      (CommandSourceStack)cs.getSource(),
                                                      "//pos2 %d,%d,%d".formatted((int)boundaries.maxX, (int)boundaries.maxY, (int)boundaries.maxZ)
                                                   );
                                                ((CommandSourceStack)cs.getSource())
                                                   .sendSuccess(CIOMessages.info("Структура §7\"" + name + ".dat\"§r выделена!"), true);
                                                return 1;
                                             }
                                          }
                                       )
                                 )
                           ))
                        .then(Commands.literal("tp").then(Commands.argument("name", StructureFileArgument.structure()).executes(cs -> {
                           String name = StringArgumentType.getString(cs, "name");
                           StructureST tag = StructIO.readStructure(((CommandSourceStack)cs.getSource()).registryAccess(), name);
                           if (tag == null) {
                              return 1;
                           } else {
                              MinecraftServer server = ((CommandSourceStack)cs.getSource()).getServer();
                              ServerPlayer player = ((CommandSourceStack)cs.getSource()).getPlayerOrException();
                              AABB aabb = tag.boundary();
                              ServerLevel level = Objects.requireNonNull(tag.getLevel(server));
                              Vec3 center = aabb.getCenter();
                              player.teleportTo(level, center.x() + 0.5, center.y() + 0.5, center.z() + 0.5, 0.0F, 0.0F);
                              ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Телепортация к структуре §7\"" + name + ".dat\"§r..."), true);
                              return 1;
                           }
                        }))))
                     .then(
                        Commands.literal("info")
                           .then(
                              Commands.argument("name", StructureFileArgument.structure())
                                 .executes(
                                    cs -> {
                                       String name = StringArgumentType.getString(cs, "name");
                                       StructureST tag = StructIO.readStructure(((CommandSourceStack)cs.getSource()).registryAccess(), name);
                                       if (tag == null) {
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(CIOMessages.info("Структура §7\"" + name + ".dat\"§r не найдена!"), true);
                                          return 1;
                                       } else {
                                          AABB aabb = tag.boundary();
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(CIOMessages.info("Информация о структуре §7\"" + name + ".dat\"§r:"), true);
                                          String stv = Integer.toString(tag.getMetadata().structioVersion).replaceAll("", ".");
                                          stv = stv.substring(1, stv.length() - 1);
                                          Instant exportInstant = Instant.ofEpochMilli(tag.getMetadata().timestamp);
                                          StringBuilder relativeTimeAgo = new StringBuilder();
                                          long passedMinutes = Duration.between(exportInstant, Instant.now()).abs().toMinutes();
                                          long tInt;
                                          if ((tInt = TimeUnit.MINUTES.toDays(passedMinutes)) > 0L) {
                                             relativeTimeAgo.append(tInt).append(" ").append(IntegerLocalizer.localize((int)tInt, "день", "дня", "дней"));
                                             passedMinutes -= TimeUnit.DAYS.toMinutes(tInt);
                                          }

                                          if ((tInt = TimeUnit.MINUTES.toHours(passedMinutes)) > 0L) {
                                             if (!relativeTimeAgo.isEmpty()) {
                                                relativeTimeAgo.append(", ");
                                             }

                                             relativeTimeAgo.append(tInt).append(" ").append(IntegerLocalizer.localize((int)tInt, "час", "часа", "часов"));
                                             passedMinutes -= TimeUnit.HOURS.toMinutes(tInt);
                                          }

                                          if (passedMinutes > 0L) {
                                             if (!relativeTimeAgo.isEmpty()) {
                                                relativeTimeAgo.append(", ");
                                             }

                                             relativeTimeAgo.append(passedMinutes)
                                                .append(" ")
                                                .append(IntegerLocalizer.localize((int)passedMinutes, "минуту", "минуты", "минут"));
                                          }

                                          if (relativeTimeAgo.isEmpty()) {
                                             relativeTimeAgo.append("0 минут");
                                          }

                                          MutableComponent time = Component.literal(
                                                new SimpleDateFormat("dd MMM yyyy, hh:mm:ss").format(Date.from(exportInstant))
                                             )
                                             .setStyle(
                                                Style.EMPTY
                                                   .withUnderlined(true)
                                                   .withColor(ChatFormatting.GRAY)
                                                   .withHoverEvent(
                                                      new HoverEvent(Action.SHOW_TEXT, Component.literal(relativeTimeAgo.append(" назад.").toString()))
                                                   )
                                             );
                                          Vec3 center = aabb.getCenter();
                                          ResourceKey<Level> dim = tag.getData().getDimension();
                                          MutableComponent tp = Component.literal(
                                             new Vec3(aabb.minX, aabb.minY, aabb.minZ) + " -> " + new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ)
                                          );
                                          tp.setStyle(
                                             Style.EMPTY
                                                .withUnderlined(true)
                                                .withColor(TextColor.fromRgb(6268839))
                                                .withClickEvent(
                                                   new ClickEvent(
                                                      net.minecraft.network.chat.ClickEvent.Action.RUN_COMMAND, new Location(dim, center).toCommandString()
                                                   )
                                                )
                                                .withHoverEvent(
                                                   new HoverEvent(Action.SHOW_TEXT, Component.literal("Нажми, что бы телепортироваться к структуре."))
                                                )
                                          );
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rИзмерение: §7\"" + dim.location() + "\"§r")), true);
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rГраницы: §7").append(tp).append("§r")), true);
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rАвтор: §7" + tag.getMetadata().author + "§r")), true);
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rВерсия StructIO: §7" + stv + "§r")), true);
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rВремя создания: §7").append(time).append("§r")), true);
                                          ((CommandSourceStack)cs.getSource())
                                             .sendSuccess(Cast.constant(Component.literal("§6§l- §rРевизия: §7" + tag.getMetadata().revision + "§r")), true);
                                          return 1;
                                       }
                                    }
                                 )
                           )
                     ))
                  .then(Commands.literal("cloud_upload").then(Commands.argument("name", StructureFileArgument.structure()).executes(cs -> {
                     String name = StringArgumentType.getString(cs, "name");
                     ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Началась выгрузка структуры §7\"" + name + ".dat\"§r..."), true);
                     StructureRepository.uploadStructure(name, msg -> {
                        msg = msg.replace(name, "§7\"" + name + ".dat\"§r");
                        ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info(msg), true);
                     });
                     return 1;
                  }))))
               .then(Commands.literal("cloud_download").then(Commands.argument("name", CloudStructureFileArgument.cloudStructure()).executes(cs -> {
                  String name = StringArgumentType.getString(cs, "name");
                  StructureRepository.downloadStructure(((CommandSourceStack)cs.getSource()).registryAccess(), name, msg -> {
                     msg = msg.replace(name, "§7\"" + name + ".dat\"§r");
                     ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info(msg), true);
                  });
                  return 1;
               }))))
            .then(
               Commands.literal("cloud_delete")
                  .then(
                     Commands.argument("name", CloudStructureFileArgument.cloudStructure())
                        .executes(
                           cs -> {
                              String name = StringArgumentType.getString(cs, "name");
                              StructureRepository.deleteStructure(name)
                                 .thenAccept(
                                    t -> {
                                       String msg = t.a() == 404
                                          ? "Структура %DAT% не существует в облаке."
                                          : (
                                             t.a() / 100 == 2
                                                ? "Структура %DAT% успешно удалена с облака."
                                                : "Не удалось удалить структуру %DAT% из облака. (HTTP " + t.a() + ": " + (String)t.b() + ")"
                                          );
                                       msg = msg.replace("%DAT%", "§7\"" + name + ".dat\"§r");
                                       ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info(msg), true);
                                    }
                                 );
                              return 1;
                           }
                        )
                  )
            ))
         .then(Commands.literal("validate_structures").executes(cs -> {
            try {
               Stream<String> stringStream = getAvailableStructures();
               List<String> stringList = stringStream.toList();
               if (!stringList.isEmpty()) {
                  for (String structure : stringList) {
                     MinecraftServer server = ((CommandSourceStack)cs.getSource()).getServer();
                     StructureRepository.downloadStructure(server.registryAccess(), structure, Storytelling.LOG::info).join();
                  }
               }

               ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Все скачанные структуры прошли проверку актуальности."), true);
            } catch (IOException var6) {
               var6.printStackTrace();
            }

            return 1;
         })))));
   }

   private static Stream<String> getAvailableStructures() throws IOException {
      Path dir = ScriptIO.STRUCTURES_PATH;
      return !Files.exists(dir) ? Stream.empty() : Files.list(dir).map(p -> com.google.common.io.Files.getNameWithoutExtension(p.toString()));
   }
}
