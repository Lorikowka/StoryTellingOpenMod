package com.storyteam.storytelling.system.inventory.events;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.inventory.InventorySystem;
import com.storyteam.storytelling.system.inventory.OffhandState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent.Hands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.command.EnumArgument;

public class InventoryTickEvent {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(createSetHotbarSize());
      e.register(createSetHandState());
   }

   public static LiteralArgumentBuilder<CommandSourceStack> createSetHotbarSize() {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("setHotbarSize")
         .then(Commands.argument("size", IntegerArgumentType.integer(1, 9)).executes(cs -> {
            InventorySystem.setHotBarSize(IntegerArgumentType.getInteger(cs, "size"));
            return 1;
         }));
   }

   public static LiteralArgumentBuilder<CommandSourceStack> createSetHandState() {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("setHandState")
         .then(Commands.argument("state", EnumArgument.enumArgument(OffhandState.class)).executes(cs -> {
            InventorySystem.setOffhandState((OffhandState)cs.getArgument("state", OffhandState.class));
            return 1;
         }));
   }

   @SubscribeEvent
   public static void onSwapHand(Hands event) {
      if (event.getEntity() instanceof Player player) {
         if (!InventorySystem.isSecondHandAvailable(player)) {
            event.setCanceled(true);
         }
      }
   }
}
