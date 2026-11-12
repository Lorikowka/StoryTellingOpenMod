package com.storyteam.storytelling.system._core.cloud;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.util.java.PngSizeInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.util.java.Cast;

public class CommandCloud {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create());
   }

   public static File getGlobalSTDir() {
      File std = FMLPaths.GAMEDIR.get().resolve(".storytelling").toFile();
      if (!std.isDirectory()) {
         std.mkdirs();
      }

      return std;
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create() {
      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("cloud").then(Commands.literal("reload").executes(cs -> {
         CloudIO.createReload().join();
         ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Данные облака перезагружены."), true);
         return 1;
      }))).then(imageSub());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> imageSub() {
      class Uploader {
         static int doUpload(CommandContext<CommandSourceStack> cs, boolean override) {
            String f = StringArgumentType.getString(cs, "file");
            File img = new File(CommandCloud.getGlobalSTDir(), "images" + File.separator + f);
            if (!img.isFile()) {
               ((CommandSourceStack)cs.getSource())
                  .sendFailure(CIOMessages.infor("Файл ").append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true))).append(" не найден."));
               return 0;
            } else if (img.length() >= 100000000L) {
               ((CommandSourceStack)cs.getSource())
                  .sendFailure(CIOMessages.infor("Файл ").append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true))).append(" весит более 100 МБ."));
               return 0;
            } else {
               String fn = img.getName();
               if (!fn.toLowerCase(Locale.ROOT).endsWith(".png")) {
                  ((CommandSourceStack)cs.getSource())
                     .sendFailure(CIOMessages.infor("Файл ").append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true))).append(" не является PNG."));
                  return 0;
               } else {
                  String name = fn.substring(0, fn.length() - 4);
                  RemoteFileInfo rfi = CloudIO.CLOUD_IMAGES.get(name);
                  if (rfi != null && !override) {
                     ((CommandSourceStack)cs.getSource())
                        .sendFailure(
                           CIOMessages.infor("Изображение ")
                              .append(CIOMessages.open(img))
                              .append(" уже существует в облаке. ")
                              .append(
                                 CIOMessages.command(
                                    "Перезаписать?", "Нажми, что бы перезаписать существующее изображение.", "/st cloud image upload " + name + " true"
                                 )
                              )
                        );
                     return 0;
                  } else {
                     PngSizeInfo size;
                     try (InputStream in = new FileInputStream(img)) {
                        size = new PngSizeInfo(in);
                     } catch (IOException var14) {
                        ((CommandSourceStack)cs.getSource())
                           .sendFailure(
                              CIOMessages.infor("Файл ").append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true))).append(" не является PNG.")
                           );
                        Storytelling.LOG.error("Failed to read file data.", var14);
                        return 0;
                     }

                     if (size.getPngWidth() > 4096 || size.getPngHeight() > 4096) {
                        ((CommandSourceStack)cs.getSource())
                           .sendSuccess(
                              CIOMessages.info(
                                 "Изображение ",
                                 c -> c.append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true)))
                                       .append(" является ")
                                       .append(Integer.toUnsignedString(size.getPngWidth()))
                                       .append("x")
                                       .append(Integer.toUnsignedString(size.getPngHeight()))
                                       .append(
                                          ". Это может плохо повлиять на время подгрузки в игре. Советуем уменьшить файл так, что бы наибольшая из сторон была не более 4096 пикселей."
                                       )
                              ),
                              true
                           );
                     }

                     try {
                        CloudIO.CLOUD_IMAGES
                           .upload(
                              name,
                              msg -> ((CommandSourceStack)cs.getSource()).sendSuccess(Cast.constant(Component.literal(msg)), true),
                              Files.readAllBytes(img.toPath())
                           )
                           .thenAccept(
                              inf -> {
                                 if (inf == null) {
                                    ((CommandSourceStack)cs.getSource()).sendFailure(CIOMessages.infor("Не получилось загрузить изображение."));
                                 } else {
                                    ((CommandSourceStack)cs.getSource())
                                       .sendSuccess(CIOMessages.info("Изображение загружено по ссылке: ", c -> c.append(CIOMessages.url(inf.uri()))), true);
                                 }
                              }
                           );
                        return 1;
                     } catch (IOException var12) {
                        ((CommandSourceStack)cs.getSource())
                           .sendFailure(
                              CIOMessages.infor("Файл ")
                                 .append(Component.literal(f).withStyle(Style.EMPTY.withItalic(true)))
                                 .append(" не получилось прочитать.")
                           );
                        Storytelling.LOG.error("Failed to read file data.", var12);
                        return 0;
                     }
                  }
               }
            }
         }
      }

      return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("image")
               .then(
                  Commands.literal("get-url")
                     .then(
                        CloudIO.CLOUD_IMAGES
                           .commandArgument("image")
                           .executes(
                              cs -> {
                                 RemoteFileInfo image = CloudIO.CLOUD_IMAGES.fromCommand(cs, "image");
                                 ((CommandSourceStack)cs.getSource())
                                    .sendSuccess(
                                       CIOMessages.info(
                                          "Изображение ", c -> c.append(image.name()).append(" найдено по URL: ").append(CIOMessages.url(image.uri()))
                                       ),
                                       true
                                    );
                                 return 1;
                              }
                           )
                     )
               ))
            .then(Commands.literal("delete").then(CloudIO.CLOUD_IMAGES.commandArgument("image").executes(cs -> {
               String n = StringArgumentType.getString(cs, "image");
               RemoteFileInfo image = CloudIO.CLOUD_IMAGES.fromCommand(cs, "image");
               if (image == null) {
                  ((CommandSourceStack)cs.getSource()).sendFailure(CIOMessages.infor("Файл ").append(n).append(" не найден в облаке."));
                  return 0;
               } else {
                  CloudIO.CLOUD_IMAGES.delete(image.name()).join();
                  ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Файл ", c -> c.append(n).append(" удалён.")), true);
                  return 1;
               }
            }))))
         .then(
            ((LiteralArgumentBuilder)Commands.literal("upload")
                  .executes(
                     cs -> {
                        File imgs = new File(getGlobalSTDir(), "images");
                        MutableComponent file = CIOMessages.open(".minecraft/.storytelling/images/", imgs);
                        ((CommandSourceStack)cs.getSource())
                           .sendSuccess(CIOMessages.info("Все изображения загружаются из директории ", c -> c.append(file).append(". (кликабельно)")), true);
                        return 1;
                     }
                  ))
               .then(
                  ((RequiredArgumentBuilder)Commands.argument("file", StringArgumentType.greedyString()).suggests((cs, b) -> {
                        File imgs = new File(getGlobalSTDir(), "images");
                        if (!imgs.isDirectory()) {
                           imgs.mkdirs();
                        }

                        File[] fs = imgs.listFiles();
                        if (fs == null) {
                           return Suggestions.empty();
                        } else {
                           for (File file : fs) {
                              if (file.isFile()) {
                                 String fn = file.getName();
                                 if (fn.toLowerCase(Locale.ROOT).endsWith(".png")) {
                                    b.suggest(fn);
                                 }
                              }
                           }

                           return b.buildFuture();
                        }
                     }).executes(cs -> Uploader.doUpload(cs, false)))
                     .then(
                        Commands.argument("override", BoolArgumentType.bool()).executes(cs -> Uploader.doUpload(cs, BoolArgumentType.getBool(cs, "override")))
                     )
               )
         );
   }
}
