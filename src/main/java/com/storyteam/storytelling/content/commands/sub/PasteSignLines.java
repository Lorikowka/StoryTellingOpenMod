package com.storyteam.storytelling.content.commands.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.regions.Region;
import com.storyteam.storytelling.system._core.cloud.CIOMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

public class PasteSignLines {
   public static LiteralArgumentBuilder<CommandSourceStack> create(CommandBuildContext ctx) {
      Command<CommandSourceStack> code = cs -> {
         String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
         if (clipboard != null && !clipboard.trim().isEmpty()) {
            Region region;
            try {
               region = WorldEdit.getInstance()
                  .getSessionManager()
                  .get(ForgeAdapter.adaptPlayer(((CommandSourceStack)cs.getSource()).getPlayerOrException()))
                  .getRegionSelector(ForgeAdapter.adapt(((CommandSourceStack)cs.getSource()).getLevel()))
                  .getRegion();
            } catch (IncompleteRegionException var14) {
               if (var14 instanceof IncompleteRegionException) {
                  ((CommandSourceStack)cs.getSource()).sendFailure(CIOMessages.infor("Выделение не завершено!"));
               } else {
                  ((CommandSourceStack)cs.getSource()).sendFailure(CIOMessages.infor("Ошибка при получении выделенной области!"));
               }

               return 0;
            }

            BlockPos from = ForgeAdapter.toBlockPos(region.getMaximumPoint());
            BlockPos to = ForgeAdapter.toBlockPos(region.getMinimumPoint());
            ServerLevel level = ((CommandSourceStack)cs.getSource()).getLevel();
            String[] signTexts = clipboard.replace("\r\n", "\n").split("\n");
            BlockPos[] signPoses = BlockPos.betweenClosedStream(from, to)
               .filter(b -> level.getBlockState(b).getBlock() instanceof SignBlock)
               .map(b -> new BlockPos(b.getX(), b.getY(), b.getZ()))
               .toArray(BlockPos[]::new);
            if (signTexts.length != signPoses.length) {
               ((CommandSourceStack)cs.getSource())
                  .sendFailure(
                     CIOMessages.infor("Количество строк (%s) не совпадает с количеством табличек (%s)!".formatted(signTexts.length, signPoses.length))
                  );
               return 0;
            } else {
               for (int i = 0; i < signTexts.length; i++) {
                  BlockPos signPos = signPoses[i];
                  String[] lines = signTexts[i].split("\t");
                  SignText text = new SignText();

                  for (int j = 0; j < lines.length; j++) {
                     text = text.setMessage(j, Component.literal(lines[j]));
                  }

                  if (level.getBlockEntity(signPos) instanceof SignBlockEntity signTileEntity) {
                     signTileEntity.setText(text, true);
                  } else {
                     ((CommandSourceStack)cs.getSource())
                        .sendFailure(
                           CIOMessages.infor(
                              "Ошибка: %d %d %d наследует SignBlock, но не наследует SignBlockEntity!"
                                 .formatted(signPos.getX(), signPos.getY(), signPos.getZ())
                           )
                        );
                  }
               }

               ((CommandSourceStack)cs.getSource()).sendSuccess(CIOMessages.info("Обновлено %s табличек!".formatted(signTexts.length)), true);
               return 1;
            }
         } else {
            ((CommandSourceStack)cs.getSource()).sendFailure(CIOMessages.infor("Буфер обмена пуст!"));
            return 0;
         }
      };
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("paste-sign-lines").executes(code);
   }
}
