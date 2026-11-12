package com.storyteam.storytelling.mixins.minecraft;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({GiveCommand.class})
public interface GiveCommandAccessor {
   @Invoker("giveItem")
   static int giveItem(CommandSourceStack pSource, ItemInput pItem, Collection<ServerPlayer> pTargets, int pCount) throws CommandSyntaxException {
      throw new UnsupportedOperationException();
   }
}
