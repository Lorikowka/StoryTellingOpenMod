package com.storyteam.storytelling.system.simulator.world_tab;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.storyteam.storytelling.events.base.GatherSTSubcommandsEvent;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegion;
import com.storyteam.storytelling.system.simulator.world_tab.data.WorldMapRegionsStorage;
import java.util.Arrays;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldTabCommand {
   @SubscribeEvent
   public static void register(GatherSTSubcommandsEvent e) {
      e.register(create(e.getContext()));
   }

   public static LiteralArgumentBuilder<CommandSourceStack> create(CommandBuildContext ctx) {
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("world_tab_region")
         .then(
            ((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", StringArgumentType.string())
                     .suggests((c, b) -> SharedSuggestionProvider.suggest(WorldMapRegionsStorage.WORLD_MAP_REGIONS.keySet(), b))
                     .then(Commands.literal("setState").then(Commands.argument("state", IntegerArgumentType.integer(0, 2)).executes(cs -> {
                        String name = StringArgumentType.getString(cs, "name");
                        byte state = (byte)IntegerArgumentType.getInteger(cs, "state");
                        WorldTabSystem.setRegionState(name, state).run();
                        return 1;
                     }))))
                  .then(
                     Commands.literal("setButton")
                        .then(
                           Commands.argument("button", StringArgumentType.string())
                              .suggests((c, b) -> SharedSuggestionProvider.suggest(WorldMapRegionsStorage.WORLD_MAP_BUTTONS.keySet(), b))
                              .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(cs -> {
                                 String name = StringArgumentType.getString(cs, "name");
                                 String buttonName = StringArgumentType.getString(cs, "button");
                                 boolean enabled = BoolArgumentType.getBool(cs, "enabled");
                                 WorldTabSystem.setRegionButtonEnabled(name, buttonName, enabled).run();
                                 return 1;
                              }))
                        )
                  ))
               .then(
                  Commands.literal("getData")
                     .executes(
                        cs -> {
                           String name = StringArgumentType.getString(cs, "name");
                           WorldMapRegion region = WorldMapRegionsStorage.WORLD_MAP_REGIONS.get(name);
                           ((CommandSourceStack)cs.getSource())
                              .sendSystemMessage(
                                 Component.literal(
                                    "\n§2"
                                       + region.getId().toString()
                                       + "§7 ----\n§6state: §8"
                                       + region.getState()
                                       + "\n§exPos: §7"
                                       + region.getXPos()
                                       + "\n§6yPos: §8"
                                       + region.getYPos()
                                       + "\n§ewidth: §7"
                                       + region.getWidth()
                                       + "\n§6height: §8"
                                       + region.getHeight()
                                       + "\n§etexture: §7"
                                       + region.getTexture().toString()
                                       + "\n\n§9info §7----\n    §5name: §8"
                                       + region.getWorldMapRegionInfo().getName()
                                       + "\n    §ddescription: §7"
                                       + region.getWorldMapRegionInfo().getDescription()
                                       + "\n    §5image: §8"
                                       + region.getWorldMapRegionInfo().getImage().toString()
                                       + "\n    §dteleport: §7"
                                       + region.getWorldMapRegionInfo().getTeleportLocation().toCommandString()
                                       + "\n    §5buttons: §8"
                                       + Arrays.toString(
                                          region.getWorldMapRegionInfo()
                                             .getButtons()
                                             .stream()
                                             .map(x -> "§f§8" + x.getName() + " = " + x.isEnabled() + " §l(" + x.getPrice() + " эйдиков)§f§8")
                                             .toArray()
                                       )
                                       + "\n\n§4icon §7----\n    §3xPos: §8"
                                       + region.getWorldMapRegionIcon().XPos()
                                       + "\n    §byPos: §7"
                                       + region.getWorldMapRegionIcon().YPos()
                                       + "\n    §3width: §8"
                                       + region.getWorldMapRegionIcon().width()
                                       + "\n    §bheight: §7"
                                       + region.getWorldMapRegionIcon().height()
                                       + "\n    §3texture: §8"
                                       + region.getWorldMapRegionIcon().iconTexture().toString()
                                 )
                              );
                           return 1;
                        }
                     )
               )
         );
   }
}
