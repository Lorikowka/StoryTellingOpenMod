package com.storyteam.storytelling.events.listeners;

import com.storyteam.storytelling.Storytelling;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

@EventBusSubscriber
public class BlockRemapper {
   private static final List<Tuple<Pattern, Block>> RULES = new ArrayList<>();

   private static List<Tuple<Pattern, Block>> getRules() {
      if (!RULES.isEmpty()) {
         return RULES;
      } else {
         map(Pattern.compile(".+stripped_?.+_log$"), Blocks.STRIPPED_OAK_LOG);
         map(Pattern.compile(".+stripped_?.+_wood$"), Blocks.STRIPPED_OAK_WOOD);
         map(Pattern.compile(".+_log$"), Blocks.OAK_LOG);
         map(Pattern.compile(".+_wood$"), Blocks.OAK_WOOD);
         map(Pattern.compile(".+_glass$"), Blocks.GLASS);
         map(Pattern.compile(".+_glass_pane$"), Blocks.GLASS_PANE);
         map(Pattern.compile(".+_door$"), Blocks.OAK_DOOR);
         map(Pattern.compile(".+_wall_torch$"), Blocks.WALL_TORCH);
         map(Pattern.compile(".+_torch$"), Blocks.TORCH);
         map(Pattern.compile(".+_slab$"), Blocks.COBBLESTONE_SLAB);
         map(Pattern.compile(".+_lantern$"), Blocks.LANTERN);
         map(Pattern.compile(".+_barrel$"), Blocks.BARREL);
         map(Pattern.compile(".+_composter$"), Blocks.COMPOSTER);
         map(Pattern.compile(".+_crafting_table$"), Blocks.CRAFTING_TABLE);
         map(Pattern.compile(".+_planks$"), Blocks.OAK_PLANKS);
         map(Pattern.compile(".+_trapped_chest$"), Blocks.TRAPPED_CHEST);
         map(Pattern.compile(".+_chest$"), Blocks.CHEST);
         map(Pattern.compile(".+_stairs$"), Blocks.COBBLESTONE_STAIRS);
         map(Pattern.compile(".+_wall$"), Blocks.COBBLESTONE_WALL);
         map(Pattern.compile(".+_pressure_plate$"), Blocks.STONE_PRESSURE_PLATE);
         map(Pattern.compile(".+_fence$"), Blocks.OAK_FENCE);
         map(Pattern.compile(".+_fence_gate$"), Blocks.OAK_FENCE_GATE);
         map(Pattern.compile(".+_leaves$"), Blocks.OAK_LEAVES);
         map(Pattern.compile(".+_sapling$"), Blocks.OAK_SAPLING);
         map(Pattern.compile(".+_bookshelf$"), Blocks.BOOKSHELF);
         map(Pattern.compile(".+_ladder$"), Blocks.LADDER);
         map(Pattern.compile(".+_button$"), Blocks.STONE_BUTTON);
         map(Pattern.compile(".+_carpet$"), Blocks.WHITE_CARPET);
         map(Pattern.compile(".+_wall_sign$"), Blocks.OAK_WALL_SIGN);
         map(Pattern.compile(".+_sign$"), Blocks.OAK_SIGN);
         map(Pattern.compile(".+_trapdoor$"), Blocks.OAK_TRAPDOOR);
         return RULES;
      }
   }

   private static void map(Pattern pattern, Block block) {
      RULES.add(new Tuple(pattern, block));
   }

   @SubscribeEvent
   public static void handleEvent(MissingMappingsEvent event) {
      if (!FMLEnvironment.production) {
         event.getAllMappings(ForgeRegistries.BLOCKS.getRegistryKey())
            .forEach(
               blockMapping -> {
                  String source = blockMapping.getKey().toString();
                  Block target = getRules()
                     .stream()
                     .filter(t -> ((Pattern)t.getA()).matcher(source).matches())
                     .findFirst()
                     .<Block>map(Tuple::getB)
                     .orElse(Blocks.COBBLESTONE);
                  blockMapping.remap(target);
                  Storytelling.LOG.debug("Successfully remapped block {} to {}!", source, ForgeRegistries.BLOCKS.getKey(target).toString());
               }
            );
         event.getAllMappings(ForgeRegistries.BLOCK_ENTITY_TYPES.getRegistryKey()).forEach(tileMapping -> {
            String source = tileMapping.getKey().toString();
            BlockEntityType<?> target = null;
            if (source.matches(".+_chest$")) {
               target = BlockEntityType.CHEST;
            } else if (source.matches(".+_barrel$")) {
               target = BlockEntityType.BARREL;
            } else if (source.matches(".+_sign$")) {
               target = BlockEntityType.SIGN;
            }

            if (target != null) {
               tileMapping.remap(target);
               Storytelling.LOG.debug("Successfully remapped tile {} to {}!", source, ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(target).toString());
            }
         });
      }
   }
}
