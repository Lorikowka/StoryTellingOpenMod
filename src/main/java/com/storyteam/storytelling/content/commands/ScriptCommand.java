package com.storyteam.storytelling.content.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.events.EventType;
import com.storyteam.storytelling.api.itf.IStoryServer;
import com.storyteam.storytelling.content.commands.arguments.ActiveScriptArgument;
import com.storyteam.storytelling.content.commands.arguments.ScriptIdArgument;
import com.storyteam.storytelling.content.commands.arguments.ScriptTriggerArgument;
import com.storyteam.storytelling.events.base.NextDayEvent;
import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.init.ScriptCategory;
import com.storyteam.storytelling.script.ScriptInstance;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.action.PerformerAction;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.script.story.ScriptEnvironment;
import com.storyteam.storytelling.util.ServerScheduler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ModHelper;

public class ScriptCommand {
   private static final DynamicCommandExceptionType ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER = new DynamicCommandExceptionType(
      id -> Component.literal("Скрипт " + id + " не имеет главного исполнителя.").withStyle(ChatFormatting.RED)
   );
   private static final Dynamic2CommandExceptionType ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER_LABEL = new Dynamic2CommandExceptionType(
      (id, state) -> Component.literal("Скрипт " + id + " не имеет метки главного исполнителя \"" + state + "\".").withStyle(ChatFormatting.RED)
   );

   public static void sendSuccess(CommandContext<CommandSourceStack> ctx, Component com, boolean allowLogging) {
      ((CommandSourceStack)ctx.getSource()).sendSuccess(Cast.constant(com), allowLogging);
   }

   public static void register(ICommand.ICommandRegistry dispatcher) {
      ScriptEnvironment env = dispatcher.getStoryServer().getScriptEnv();
      dispatcher.register(
         (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                                            "script"
                                                         )
                                                         .then(Commands.literal("environment").executes(ctx -> {
                                                            String ver = ((IModInfo)ModList.get().getModFileById("storytelling").getMods().get(0))
                                                               .getVersion()
                                                               .toString();
                                                            sendSuccess(
                                                               ctx, Component.literal("Версия Storytelling: " + (ModHelper.inDev() ? "DEV" : ver)), true
                                                            );
                                                            sendSuccess(
                                                               ctx,
                                                               Component.literal("Скриптов загружено: " + IStoryServer.get().getScriptEnv().constructors.size()),
                                                               true
                                                            );
                                                            return 1;
                                                         })))
                                                      .then(Commands.literal("active").executes(cs -> {
                                                         if (env.scriptInstances.isEmpty()) {
                                                            sendSuccess(cs, Component.literal("Активных скриптов нет.").withStyle(ChatFormatting.RED), true);
                                                         } else {
                                                            sendSuccess(cs, Component.literal("Активные скрипты:").withStyle(ChatFormatting.RED), true);
                                                            ObjectIterator var2 = env.scriptInstances.int2ObjectEntrySet().iterator();

                                                            while (var2.hasNext()) {
                                                               Entry<ScriptInstance> entry = (Entry<ScriptInstance>)var2.next();
                                                               String description = ((ScriptInstance)entry.getValue()).getDescription();
                                                               int id = entry.getIntKey();
                                                               String value = "§7Скрипт "
                                                                  + (id >= 0 ? "§e" : "§8")
                                                                  + "#"
                                                                  + id
                                                                  + (description.equals("") ? "" : "§7 [" + description + "]");
                                                               sendSuccess(cs, Component.literal(value), true);
                                                            }
                                                         }

                                                         return 1;
                                                      })))
                                                   .then(Commands.literal("nextDay").executes(cs -> {
                                                      NextDayEvent event = new NextDayEvent();
                                                      Storytelling.BUS.post(event);
                                                      IStoryServer.get().postStoryEvent(EventType.NEXT_DAY, event);
                                                      return 1;
                                                   })))
                                                .then(Commands.literal("setup").then(Commands.argument("script", ScriptIdArgument.script()).executes(cs -> {
                                                   int id = (Integer)cs.getArgument("script", Integer.class);
                                                   ScriptInstance script = env.createInstance(id);
                                                   ScriptPerformer performer = script.handleSetup();
                                                   if (performer.getActionsAmount() > 0) {
                                                      env.scriptSetups.put(id, performer);
                                                   }

                                                   return 1;
                                                }))))
                                             .then(
                                                ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("start")
                                                         .then(
                                                            Commands.literal("all")
                                                               .then(
                                                                  Commands.argument("scripts", StringArgumentType.string())
                                                                     .executes(
                                                                        cs -> {
                                                                           String[] values = ((String)cs.getArgument("scripts", String.class)).split("[ ,;]+");

                                                                           for (String entry : values) {
                                                                              try {
                                                                                 int index = Integer.parseInt(entry);
                                                                                 if (env.getScript(index) == null) {
                                                                                    env.start(index);
                                                                                 }
                                                                              } catch (NumberFormatException var8) {
                                                                                 sendSuccess(
                                                                                    cs,
                                                                                    Component.literal(
                                                                                       "§4§l[§cОшибка§4§l]§c: §rНеправильное форматирование индекса скрипта: §c"
                                                                                          + entry
                                                                                          + "§r!"
                                                                                    ),
                                                                                    true
                                                                                 );
                                                                              }
                                                                           }

                                                                           return 1;
                                                                        }
                                                                     )
                                                               )
                                                         ))
                                                      .then(
                                                         Commands.literal("category")
                                                            .then(
                                                               Commands.argument("scriptCategory", StringArgumentType.string())
                                                                  .suggests((context, builder) -> {
                                                                     for (String category : ScriptCategory.SCRIPTS.keySet()) {
                                                                        builder.suggest(category);
                                                                     }

                                                                     return builder.buildFuture();
                                                                  })
                                                                  .executes(
                                                                     cs -> {
                                                                        String category = (String)cs.getArgument("scriptCategory", String.class);
                                                                        int[] ids = ScriptCategory.SCRIPTS.get(category).getScripts();
                                                                        if (ids != null) {
                                                                           for (int index : ids) {
                                                                              if (env.getScript(index) == null) {
                                                                                 env.start(index);
                                                                              }
                                                                           }
                                                                        } else {
                                                                           sendSuccess(
                                                                              cs,
                                                                              Component.literal(
                                                                                 "§4§l[§cОшибка§4§l]§c: §rНе удалось получить категорию под названием: §c"
                                                                                    + category
                                                                                    + "§r!"
                                                                              ),
                                                                              true
                                                                           );
                                                                        }

                                                                        return 1;
                                                                     }
                                                                  )
                                                            )
                                                      ))
                                                   .then(
                                                      ((RequiredArgumentBuilder)Commands.argument("script", ScriptIdArgument.script())
                                                            .executes(
                                                               cs -> {
                                                                  int num = (Integer)cs.getArgument("script", Integer.class);

                                                                  try {
                                                                     env.start(num);
                                                                  } catch (Exception var4) {
                                                                     var4.printStackTrace();
                                                                     ((CommandSourceStack)cs.getSource())
                                                                        .sendFailure(
                                                                           Component.literal(String.format("Ошибка при загрузке скрипта под номером %d.", num))
                                                                        );
                                                                  }

                                                                  return 1;
                                                               }
                                                            ))
                                                         .then(Commands.argument("label", StringArgumentType.string()).executes(cs -> {
                                                            int num = (Integer)cs.getArgument("script", Integer.class);
                                                            env.start((Integer)cs.getArgument("script", Integer.class));

                                                            try {
                                                               ScriptInstance script = (ScriptInstance)env.scriptInstances.get(num);
                                                               ScriptPerformer mainPerformer = script.getMainPerformer();
                                                               if (mainPerformer == null) {
                                                                  throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER.create(script.getId());
                                                               } else {
                                                                  String label = StringArgumentType.getString(cs, "label");
                                                                  if (mainPerformer.containsLabel(label)) {
                                                                     mainPerformer.gotoLabel(label);
                                                                     return 1;
                                                                  } else {
                                                                     throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER_LABEL.create(script.getId(), label);
                                                                  }
                                                               }
                                                            } catch (RuntimeException var6) {
                                                               var6.printStackTrace();
                                                               throw var6;
                                                            }
                                                         }))
                                                   )
                                             ))
                                          .then(
                                             ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                                               "stop"
                                                            )
                                                            .then(
                                                               Commands.literal("all")
                                                                  .executes(
                                                                     cs -> {
                                                                        for (int script : env.scriptInstances
                                                                           .values()
                                                                           .stream()
                                                                           .map(ScriptInstance::getId)
                                                                           .toList()) {
                                                                           if (env.getScript(script) != null) {
                                                                              try {
                                                                                 env.stop(script);
                                                                              } catch (Exception var5) {
                                                                                 var5.printStackTrace();
                                                                                 env.scriptInstances.remove(script);
                                                                                 ((CommandSourceStack)cs.getSource())
                                                                                    .sendFailure(
                                                                                       Component.literal(
                                                                                          String.format(
                                                                                             "Ошибка во время остановки скрипта под номером %d.", script
                                                                                          )
                                                                                       )
                                                                                    );
                                                                              }
                                                                           }
                                                                        }

                                                                        return 1;
                                                                     }
                                                                  )
                                                            ))
                                                         .then(
                                                            Commands.literal("category")
                                                               .then(
                                                                  Commands.argument("scriptCategory", StringArgumentType.string())
                                                                     .suggests((context, builder) -> {
                                                                        for (String category : ScriptCategory.SCRIPTS.keySet()) {
                                                                           builder.suggest(category);
                                                                        }

                                                                        return builder.buildFuture();
                                                                     })
                                                                     .executes(
                                                                        cs -> {
                                                                           String category = (String)cs.getArgument("scriptCategory", String.class);
                                                                           int[] ids = ScriptCategory.SCRIPTS.get(category).getScripts();
                                                                           if (ids != null) {
                                                                              for (int index : ids) {
                                                                                 try {
                                                                                    env.stop(index);
                                                                                 } catch (Exception var9) {
                                                                                    var9.printStackTrace();
                                                                                    env.scriptInstances.remove(index);
                                                                                    ((CommandSourceStack)cs.getSource())
                                                                                       .sendFailure(
                                                                                          Component.literal(
                                                                                             String.format(
                                                                                                "Ошибка во время остановки скрипта под номером %d.", index
                                                                                             )
                                                                                          )
                                                                                       );
                                                                                 }
                                                                              }
                                                                           } else {
                                                                              sendSuccess(
                                                                                 cs,
                                                                                 Component.literal(
                                                                                    "§4§l[§cОшибка§4§l]§c: §rНе удалось получить категорию под названием: §c"
                                                                                       + category
                                                                                       + "§r!"
                                                                                 ),
                                                                                 true
                                                                              );
                                                                           }

                                                                           return 1;
                                                                        }
                                                                     )
                                                               )
                                                         ))
                                                      .then(
                                                         Commands.literal("scripts")
                                                            .executes(
                                                               cs -> {
                                                                  for (int script : env.scriptInstances
                                                                     .values()
                                                                     .stream()
                                                                     .map(ScriptInstance::getId)
                                                                     .filter(entry -> entry >= 0)
                                                                     .toList()) {
                                                                     if (env.getScript(script) != null) {
                                                                        try {
                                                                           env.stop(script);
                                                                        } catch (Exception var5) {
                                                                           var5.printStackTrace();
                                                                           env.scriptInstances.remove(script);
                                                                           ((CommandSourceStack)cs.getSource())
                                                                              .sendFailure(
                                                                                 Component.literal(
                                                                                    String.format("Ошибка во время остановки скрипта под номером %d.", script)
                                                                                 )
                                                                              );
                                                                        }
                                                                     }
                                                                  }

                                                                  return 1;
                                                               }
                                                            )
                                                      ))
                                                   .then(
                                                      Commands.literal("backscripts")
                                                         .executes(
                                                            cs -> {
                                                               for (int script : env.scriptInstances
                                                                  .values()
                                                                  .stream()
                                                                  .map(ScriptInstance::getId)
                                                                  .filter(entry -> entry < 0)
                                                                  .toList()) {
                                                                  if (env.getScript(script) != null) {
                                                                     try {
                                                                        env.stop(script);
                                                                     } catch (Exception var5) {
                                                                        var5.printStackTrace();
                                                                        env.scriptInstances.remove(script);
                                                                        ((CommandSourceStack)cs.getSource())
                                                                           .sendFailure(
                                                                              Component.literal(
                                                                                 String.format("Ошибка во время остановки скрипта под номером %d.", script)
                                                                              )
                                                                           );
                                                                     }
                                                                  }
                                                               }

                                                               return 1;
                                                            }
                                                         )
                                                   ))
                                                .then(
                                                   Commands.argument("script", new ActiveScriptArgument())
                                                      .executes(
                                                         cs -> {
                                                            int script = (Integer)cs.getArgument("script", Integer.class);

                                                            try {
                                                               env.stop(script);
                                                            } catch (Exception var4) {
                                                               var4.printStackTrace();
                                                               env.scriptInstances.remove(script);
                                                               ((CommandSourceStack)cs.getSource())
                                                                  .sendFailure(
                                                                     Component.literal(
                                                                        String.format("Ошибка во время остановки скрипта под номером %d.", script)
                                                                     )
                                                                  );
                                                            }

                                                            return 1;
                                                         }
                                                      )
                                                )
                                          ))
                                       .then(
                                          ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("export")
                                                   .then(
                                                      Commands.literal("all")
                                                         .executes(
                                                            cs -> {
                                                               String value = Arrays.toString(
                                                                     env.scriptInstances.values().stream().map(ScriptInstance::getId).sorted().toArray()
                                                                  )
                                                                  .replace("[", "")
                                                                  .replace("]", "");
                                                               Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                                                               return 1;
                                                            }
                                                         )
                                                   ))
                                                .then(
                                                   Commands.literal("scripts")
                                                      .executes(
                                                         cs -> {
                                                            String value = Arrays.toString(
                                                                  env.scriptInstances
                                                                     .values()
                                                                     .stream()
                                                                     .map(ScriptInstance::getId)
                                                                     .filter(entry -> entry >= 0)
                                                                     .sorted()
                                                                     .toArray()
                                                               )
                                                               .replace("[", "")
                                                               .replace("]", "");
                                                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                                                            return 1;
                                                         }
                                                      )
                                                ))
                                             .then(
                                                Commands.literal("backscripts")
                                                   .executes(
                                                      cs -> {
                                                         String value = Arrays.toString(
                                                               env.scriptInstances
                                                                  .values()
                                                                  .stream()
                                                                  .map(ScriptInstance::getId)
                                                                  .filter(entry -> entry < 0)
                                                                  .sorted()
                                                                  .toArray()
                                                            )
                                                            .replace("[", "")
                                                            .replace("]", "");
                                                         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
                                                         return 1;
                                                      }
                                                   )
                                             )
                                       ))
                                    .then(Commands.literal("actions").then(Commands.argument("script", ScriptIdArgument.script()).executes(cs -> {
                                       int id = (Integer)cs.getArgument("script", Integer.class);
                                       ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                                       if (script == null) {
                                          script = env.createInstance(id);
                                       }

                                       ScriptPerformer mainPerformer = script.getMainPerformer();
                                       if (mainPerformer == null) {
                                          throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER.create(script.getId());
                                       } else {
                                          Path path = ScriptIO.WORLD_PATH.resolve("actions.txt");
                                          if (!Files.exists(path)) {
                                             try {
                                                Files.createFile(path);
                                             } catch (IOException var12) {
                                                throw new RuntimeException(var12);
                                             }
                                          }

                                          try (Writer writer = Files.newBufferedWriter(path)) {
                                             for (int i = 0; i < mainPerformer.getActions().size(); i++) {
                                                try {
                                                   PerformerAction<?> action = mainPerformer.getActions().get(i);
                                                   writer.write("[" + i + "]: " + action.toString() + "\n\n");
                                                } catch (NullPointerException var11) {
                                                   writer.write("[" + i + "]: Unreadable action{} \n\n");
                                                }
                                             }

                                             writer.flush();
                                          } catch (IOException var14) {
                                             throw new RuntimeException(var14);
                                          }

                                          try {
                                             Desktop.getDesktop().edit(path.toFile());
                                             return 1;
                                          } catch (IOException var10) {
                                             throw new RuntimeException(var10);
                                          }
                                       }
                                    }))))
                                 .then(
                                    Commands.literal("phase")
                                       .then(
                                          Commands.argument("script", new ActiveScriptArgument())
                                             .executes(
                                                cs -> {
                                                   int id = (Integer)cs.getArgument("script", Integer.class);
                                                   ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                                                   if (script == null) {
                                                      ((CommandSourceStack)cs.getSource())
                                                         .sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                                      return 1;
                                                   } else {
                                                      if (script.getMainPerformer() != null) {
                                                         ScriptPerformer performer = script.getMainPerformer();

                                                         for (String entry : Lists.newArrayList(
                                                            new String[]{
                                                               "Стадия скрипта: ["
                                                                  + performer.getActionIndex()
                                                                  + "/"
                                                                  + performer.getActionsAmount()
                                                                  + "] ("
                                                                  + performer.getActions().get(performer.getActionIndex())
                                                                  + ")"
                                                            }
                                                         )) {
                                                            sendSuccess(cs, Component.literal(entry), true);
                                                         }
                                                      } else {
                                                         ((CommandSourceStack)cs.getSource())
                                                            .sendFailure(Component.literal("Главный исполнитель скрипта " + script.getId() + " не обнаружен."));
                                                      }

                                                      return 1;
                                                   }
                                                }
                                             )
                                       )
                                 ))
                              .then(Commands.literal("help").then(Commands.argument("script", new ActiveScriptArgument()).executes(cs -> {
                                 int id = (Integer)cs.getArgument("script", Integer.class);
                                 ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                                 if (script == null) {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                    return 1;
                                 } else {
                                    if (script.getMainPerformer() != null) {
                                       ScriptPerformer performer = script.getMainPerformer();
                                       HelpReportBuilder builder = HelpReportBuilder.builder();
                                       performer.getActions().get(performer.getActionIndex()).gatherHelpReport(builder);
                                       sendSuccess(cs, Component.literal(builder.build()), true);
                                    } else {
                                       ((CommandSourceStack)cs.getSource())
                                          .sendFailure(Component.literal("Главный исполнитель скрипта " + script.getId() + " не обнаружен."));
                                    }

                                    return 1;
                                 }
                              }))))
                           .then(Commands.literal("data").then(Commands.argument("script", new ActiveScriptArgument()).executes(cs -> {
                              int id = (Integer)cs.getArgument("script", Integer.class);
                              ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                              if (script == null) {
                                 ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                 return 1;
                              } else {
                                 sendSuccess(cs, Component.literal(script.serializeNBT().toString()), true);
                                 return 1;
                              }
                           }))))
                        .then(Commands.literal("jump").then(((RequiredArgumentBuilder)Commands.argument("script", new ActiveScriptArgument()).executes(cs -> {
                           int id = (Integer)cs.getArgument("script", Integer.class);
                           ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                           if (script == null) {
                              ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                              return 1;
                           } else {
                              ScriptPerformer mainPerformer = script.getMainPerformer();
                              if (mainPerformer == null) {
                                 throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER.create(script.getId());
                              } else {
                                 mainPerformer.jumpTo(mainPerformer.getActionIndex() + 1, false);
                                 return 1;
                              }
                           }
                        })).then(Commands.argument("index", IntegerArgumentType.integer()).executes(cs -> {
                           ScriptInstance script = (ScriptInstance)env.scriptInstances.get((Integer)cs.getArgument("script", Integer.class));
                           ScriptPerformer mainPerformer = script.getMainPerformer();
                           if (mainPerformer == null) {
                              throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER.create(script.getId());
                           } else {
                              mainPerformer.jumpTo((Integer)cs.getArgument("index", Integer.class), false);
                              return 1;
                           }
                        })))))
                     .then(
                        Commands.literal("goto")
                           .then(
                              Commands.argument("script", new ActiveScriptArgument())
                                 .then(Commands.argument("label", StringArgumentType.string()).executes(cs -> {
                                    int id = (Integer)cs.getArgument("script", Integer.class);
                                    ScriptInstance script = (ScriptInstance)env.scriptInstances.get(id);
                                    if (script == null) {
                                       ((CommandSourceStack)cs.getSource())
                                          .sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                       return 1;
                                    } else {
                                       ScriptPerformer mainPerformer = script.getMainPerformer();
                                       if (mainPerformer == null) {
                                          throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER.create(script.getId());
                                       } else {
                                          String label = StringArgumentType.getString(cs, "label");
                                          if (mainPerformer.containsLabel(label)) {
                                             ServerScheduler.schedule(100, () -> mainPerformer.gotoLabel(label));
                                             return 1;
                                          } else {
                                             throw ERROR_SCRIPT_DOES_NOT_HAVE_MAIN_PERFORMER_LABEL.create(script.getId(), label);
                                          }
                                       }
                                    }
                                 }))
                           )
                     ))
                  .then(
                     Commands.literal("trigger")
                        .then(
                           ((RequiredArgumentBuilder)Commands.argument("script", new ActiveScriptArgument())
                                 .then(
                                    Commands.argument("trigger", ScriptTriggerArgument.trigger())
                                       .executes(
                                          cs -> {
                                             int id = (Integer)cs.getArgument("script", Integer.class);
                                             String trigger = (String)cs.getArgument("trigger", String.class);
                                             ScriptInstance instance = env.getScript(id);
                                             if (instance == null) {
                                                ((CommandSourceStack)cs.getSource())
                                                   .sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                                return 1;
                                             } else {
                                                ScriptInstance.gatherSubcommands(instance, env)
                                                   .stream()
                                                   .filter(scriptSubcommand -> scriptSubcommand.getName().equals(trigger))
                                                   .findFirst()
                                                   .ifPresent(scriptSubcommand -> {
                                                      try {
                                                         scriptSubcommand.run(cs);
                                                      } catch (CommandSyntaxException var3x) {
                                                         var3x.printStackTrace();
                                                      }
                                                   });
                                                return 1;
                                             }
                                          }
                                       )
                                 ))
                              .executes(cs -> {
                                 int id = (Integer)cs.getArgument("script", Integer.class);
                                 ScriptInstance instance = env.getScript(id);
                                 if (instance == null) {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                                    return 1;
                                 } else {
                                    IStoryServer.get().postStoryEvent(EventType.TRIGGER, id);
                                    return 1;
                                 }
                              })
                        )
                  ))
               .then(Commands.literal("pause").then(Commands.argument("script", new ActiveScriptArgument()).executes(cs -> {
                  int id = (Integer)cs.getArgument("script", Integer.class);
                  ScriptInstance instance = env.getScript(id);
                  if (instance == null) {
                     ((CommandSourceStack)cs.getSource()).sendFailure(Component.literal(String.format("Скрипт под номером %d не обнаружен.", id)));
                     return 1;
                  } else {
                     instance.pause();
                     return 1;
                  }
               }))))
            .then(Commands.literal("resume").then(Commands.argument("script", new ActiveScriptArgument()).executes(cs -> {
               int id = (Integer)cs.getArgument("script", Integer.class);
               IStoryServer.get().getScriptEnv().getScript(id).resume();
               return 1;
            })))
      );
      dispatcher.register(Commands.literal("script").then(Commands.literal("reload").executes(ctx -> {
         env.loadScripts();
         sendSuccess(ctx, Component.literal("Сценарии перезагружены: " + env.constructors.size()
            + " (Lua: " + env.luaStories.size() + "). Уже запущенные сценарии продолжат прежнюю версию."), true);
         return env.luaStories.size();
      })));
   }
}
