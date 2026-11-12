package com.storyteam.storytelling.content.commands.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.storyteam.storytelling.init.FluidsST;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.fluid.FluidFactory;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.api.PartEntity;
import org.zeith.multipart.api.WorldPartComponents;
import org.zeith.multipart.api.placement.PartPlacement;
import org.zeith.multipart.api.placement.PlacedPartConfiguration;
import org.zeith.multipart.init.PartRegistries;
import org.zeith.multipart.microblocks.HammerMicroblocks;
import org.zeith.multipart.microblocks.api.MicroblockType;
import org.zeith.multipart.microblocks.api.tile.MicroblockState;
import org.zeith.multipart.microblocks.contents.multipart.MicroblockPartDefinition.MicroblockConfiguration;
import org.zeith.multipart.microblocks.init.PartDefinitionsHM;

public class ReplaceWithMicroblock {
   public static LiteralArgumentBuilder<CommandSourceStack> create(CommandBuildContext ctx) {
      Command<CommandSourceStack> code = cs -> {
         String fluid = null;

         try {
            fluid = StringArgumentType.getString(cs, "fluid");
         } catch (RuntimeException var14) {
         }

         int range = IntegerArgumentType.getInteger(cs, "range");
         BlockInput filter = BlockStateArgument.getBlock(cs, "source-block");
         ItemStack target = ItemArgument.getItem(cs, "target-material").createItemStack(1, false);
         MicroblockType type = (MicroblockType)HammerMicroblocks.microblockTypes().getValue(ResourceLocationArgument.getId(cs, "microblock-type"));
         PartPlacement placement = (PartPlacement)PartRegistries.partPlacements().getValue(ResourceLocationArgument.getId(cs, "microblock-placement"));
         MicroblockState mbs = new MicroblockState().setType(type, null).setMaterial(target);
         PlacedPartConfiguration cfg = new PlacedPartConfiguration(PartDefinitionsHM.MICROBLOCK, new MicroblockConfiguration(mbs), placement);
         FluidFactory fluidFactory = (FluidFactory)FluidsST.Ids.getBindings().get(fluid);
         int flId = fluidFactory != null ? FluidsST.Ids.getId(fluidFactory.getType()) : 0;
         AtomicInteger counter = new AtomicInteger();
         ServerLevel level = ((CommandSourceStack)cs.getSource()).getLevel();
         Vec3 center = ((CommandSourceStack)cs.getSource()).getPosition();
         BlockPos.betweenClosedStream(new AABB(center, center).inflate((double)range)).forEach(pos -> {
            if (filter.test(new BlockInWorld(level, pos, true))) {
               PartContainer pc = WorldPartComponents.createFragile(level, pos);
               if (pc == null) {
                  return;
               }

               PartEntity pe = cfg.placer().create(pc, cfg.placement());
               pc.setPartAt(cfg.placement(), pe, false);
               BlockState state = level.getBlockState(pos);
               if (state.hasProperty(FluidsST.ST_FLUID_PROPERTY)) {
                  level.setBlockAndUpdate(pos, (BlockState)state.setValue(FluidsST.ST_FLUID_PROPERTY, flId));
               }

               counter.incrementAndGet();
            }
         });
         ((CommandSourceStack)cs.getSource())
            .sendSuccess(Cast.constant(Component.literal(ChatFormatting.LIGHT_PURPLE.toString() + counter.get() + " blocks have been replaced.")), false);
         return counter.get();
      };
      return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("replace-microblock")
         .then(
            Commands.argument("range", IntegerArgumentType.integer(1, 128))
               .then(
                  Commands.argument("source-block", BlockStateArgument.block(ctx))
                     .then(
                        Commands.argument("target-material", ItemArgument.item(ctx))
                           .then(
                              Commands.argument("microblock-type", ResourceLocationArgument.id())
                                 .suggests(
                                    (context, builder) -> SharedSuggestionProvider.suggestResource(HammerMicroblocks.microblockTypes().getKeys(), builder)
                                 )
                                 .then(
                                    ((RequiredArgumentBuilder)Commands.argument("microblock-placement", ResourceLocationArgument.id())
                                          .suggests(
                                             (context, builder) -> SharedSuggestionProvider.suggestResource(PartRegistries.partPlacements().getKeys(), builder)
                                          )
                                          .executes(code))
                                       .then(
                                          Commands.argument("fluid", StringArgumentType.string())
                                             .suggests((context, builder) -> SharedSuggestionProvider.suggest(FluidsST.Ids.getBindings().keySet(), builder))
                                             .executes(code)
                                       )
                                 )
                           )
                     )
               )
         );
   }
}
