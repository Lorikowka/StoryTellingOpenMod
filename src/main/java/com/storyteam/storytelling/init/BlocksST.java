package com.storyteam.storytelling.init;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.blocks.BottleFlowerBlock;
import com.storyteam.storytelling.content.blocks.DotPackmanBlock;
import com.storyteam.storytelling.content.blocks.EidosShellBlock;
import com.storyteam.storytelling.content.blocks.EtherLeavesBlock;
import com.storyteam.storytelling.content.blocks.EtherLogBlock;
import com.storyteam.storytelling.content.blocks.EtherealFlashBlock;
import com.storyteam.storytelling.content.blocks.FallingEtherLogBlock;
import com.storyteam.storytelling.content.blocks.GeyserEidosBlock;
import com.storyteam.storytelling.content.blocks.GeyserEtherBlock;
import com.storyteam.storytelling.content.blocks.HalfTransparentSlabBlock;
import com.storyteam.storytelling.content.blocks.HalfTransparentStairBlock;
import com.storyteam.storytelling.content.blocks.JapanLanternBlock;
import com.storyteam.storytelling.content.blocks.LevitationDustBlock;
import com.storyteam.storytelling.content.blocks.LightFlowerBlock;
import com.storyteam.storytelling.content.blocks.MusicCrystalBlock;
import com.storyteam.storytelling.content.blocks.PampushkaMushroomBlock;
import com.storyteam.storytelling.content.blocks.PinkLiquidBlock;
import com.storyteam.storytelling.content.blocks.PlatformBlock;
import com.storyteam.storytelling.content.blocks.PlatformWithBlock;
import com.storyteam.storytelling.content.blocks.RandomGlitchBlock;
import com.storyteam.storytelling.content.blocks.ReplacedBlock;
import com.storyteam.storytelling.content.blocks.TrapChestBlock;
import com.storyteam.storytelling.content.blocks.VersiformNuggetBlock;
import com.storyteam.storytelling.content.blocks.base.RotateBlock;
import com.storyteam.storytelling.content.blocks.base.RotateBlockWithCustomShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.zeith.hammerlib.annotations.Ref;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   creativeTabs = {@Ref(
      value = TabsST.class,
      field = "BLOCKITEMS"
   )}
)
public interface BlocksST {
   @RegistryName("boxes")
   Block BOXES = new RotateBlock(Properties.copy(Blocks.OAK_PLANKS).noOcclusion().noCollission());
   @RegistryName("gray_dirt")
   Block GRAY_DIRT = new Block(Properties.copy(Blocks.DIRT));
   @RegistryName("gray_grass_block")
   Block GRAY_GRASS_BLOCK = new Block(Properties.copy(Blocks.GRASS_BLOCK).mapColor(MapColor.STONE));
   @RegistryName("gray_grass")
   Block GRAY_GRASS = new TallGrassBlock(Properties.copy(Blocks.GRASS).mapColor(MapColor.COLOR_LIGHT_GRAY));
   @RegistryName("gray_tall_grass")
   Block GRAY_TALL_GRASS = new DoublePlantBlock(Properties.copy(Blocks.TALL_GRASS).mapColor(MapColor.COLOR_LIGHT_GRAY));
   @RegistryName("gray_leaves")
   Block GRAY_LEAVES = new Block(Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_LIGHT_GRAY));
   @RegistryName("gray_oak_log")
   Block GRAY_OAK_LOG = new RotatedPillarBlock(Properties.copy(Blocks.OAK_LOG));
   @RegistryName("gray_oak_fence")
   Block GRAY_OAK_FENCE = new FenceBlock(Properties.copy(Blocks.OAK_FENCE));
   @RegistryName("gray_oak_fence_gate")
   Block GRAY_OAK_FENCE_GATE = new FenceGateBlock(Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.DARK_OAK);
   @RegistryName("gray_oak_slab")
   Block GRAY_OAK_SLAB = new SlabBlock(Properties.copy(Blocks.OAK_SLAB));
   @RegistryName("gray_oak_stairs")
   Block GRAY_OAK_STAIRS = new StairBlock(GRAY_OAK_LOG::defaultBlockState, Properties.copy(Blocks.OAK_STAIRS));
   @RegistryName("gray_stone")
   Block GRAY_STONE = new Block(Properties.copy(Blocks.STONE));
   @RegistryName("gray_stone_wall")
   Block GRAY_STONE_WALL = new WallBlock(Properties.copy(Blocks.COBBLESTONE_WALL));
   @RegistryName("gray_stone_slab")
   Block GRAY_STONE_SLAB = new SlabBlock(Properties.copy(Blocks.STONE_SLAB));
   @RegistryName("gray_stone_stairs")
   Block GRAY_STONE_STAIRS = new StairBlock(GRAY_STONE::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS));
   @RegistryName("gray_dirt_wall")
   Block GRAY_DIRT_WALL = new WallBlock(Properties.copy(Blocks.COBBLESTONE_WALL));
   @RegistryName("gray_dirt_slab")
   Block GRAY_DIRT_SLAB = new SlabBlock(Properties.copy(Blocks.STONE_SLAB));
   @RegistryName("gray_dirt_stairs")
   Block GRAY_DIRT_STAIRS = new StairBlock(GRAY_DIRT::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS));
   @RegistryName("versiform_ore_block")
   Block VERSIFORM_ORE = new ReplacedBlock(GRAY_STONE, Properties.copy(Blocks.IRON_ORE));
   @RegistryName("stone_versiform_ore_block")
   Block STONE_VERSIFORM_ORE = new ReplacedBlock(Blocks.COBBLESTONE, Properties.copy(Blocks.IRON_ORE));
   @RegistryName("gray_sand")
   Block GRAY_SAND = new SandBlock(14012376, Properties.copy(Blocks.SAND));
   @RegistryName("glitch_blue")
   Block GLITCH_BLUE = new Block(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_light_blue")
   Block GLITCH_LIGHT_BLUE = new Block(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_pink")
   Block GLITCH_PINK = new Block(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_red")
   Block GLITCH_RED = new Block(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_peach")
   Block GLITCH_PEACH = new Block(Properties.copy(Blocks.STONE).noOcclusion().dynamicShape().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_lilac")
   Block GLITCH_LILAC = new Block(Properties.copy(Blocks.STONE).noOcclusion().dynamicShape().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true));
   @RegistryName("glitch_blue_trans")
   Block GLITCH_BLUE_TRANS = new HalfTransparentBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("glitch_light_blue_trans")
   Block GLITCH_LIGHT_BLUE_TRANS = new HalfTransparentBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("glitch_pink_trans")
   Block GLITCH_PINK_TRANS = new HalfTransparentBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("glitch_red_trans")
   Block GLITCH_RED_TRANS = new HalfTransparentBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("aerogel_log")
   Block ETHER_LOG = new EtherLogBlock(
      Properties.copy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_PINK).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("aerogel_log_blue")
   Block ETHER_LOG_BLUE = new EtherLogBlock(
      Properties.copy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("falling_aerogel_log")
   Block FALLING_ETHER_LOG = new FallingEtherLogBlock(
      Properties.copy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_PINK).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("falling_aerogel_log_blue")
   Block FALLING_ETHER_LOG_BLUE = new FallingEtherLogBlock(
      Properties.copy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("aerogel_leaves")
   Block ETHER_LEAVES = new EtherLeavesBlock(
      Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_PINK).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("aerogel_leaves_blue")
   Block ETHER_LEAVES_BLUE = new EtherLeavesBlock(
      Properties.copy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion().lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true)
   );
   @RegistryName("neon/neon_blue")
   Block NEON_BLUE = new HalfTransparentBlock(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion());
   @RegistryName("neon/neon_blue_slab")
   Block NEON_BLUE_SLAB = new HalfTransparentSlabBlock(
      Properties.copy(Blocks.STONE_SLAB).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_blue_stairs")
   Block NEON_BLUE_STAIRS = new HalfTransparentStairBlock(
      NEON_BLUE::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_light_blue")
   Block NEON_LIGHT_BLUE = new HalfTransparentBlock(
      Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_light_blue_slab")
   Block NEON_LIGHT_BLUE_SLAB = new HalfTransparentSlabBlock(
      Properties.copy(Blocks.STONE_SLAB).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_light_blue_stairs")
   Block NEON_LIGHT_BLUE_STAIRS = new HalfTransparentStairBlock(
      NEON_BLUE::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_pink")
   Block NEON_PINK = new HalfTransparentBlock(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion());
   @RegistryName("neon/neon_pink_slab")
   Block NEON_PINK_SLAB = new HalfTransparentSlabBlock(
      Properties.copy(Blocks.STONE_SLAB).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_pink_stairs")
   Block NEON_PINK_STAIRS = new HalfTransparentStairBlock(
      NEON_BLUE::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_red")
   Block NEON_RED = new HalfTransparentBlock(Properties.copy(Blocks.STONE).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion());
   @RegistryName("neon/neon_red_slab")
   Block NEON_RED_SLAB = new HalfTransparentSlabBlock(
      Properties.copy(Blocks.STONE_SLAB).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("neon/neon_red_stairs")
   Block NEON_RED_STAIRS = new HalfTransparentStairBlock(
      NEON_BLUE::defaultBlockState, Properties.copy(Blocks.STONE_STAIRS).lightLevel(b -> 1).emissiveRendering((state, level, pos) -> true).noOcclusion()
   );
   @RegistryName("platform/platform_green")
   Block PLATFORM_GREEN = new PlatformBlock();
   @RegistryName("platform/platform_orange")
   Block PLATFORM_ORANGE = new PlatformBlock();
   @RegistryName("platform/platform_white")
   Block PLATFORM_WHITE = new PlatformBlock();
   @RegistryName("platform/platform_yellow")
   Block PLATFORM_YELLOW = new PlatformBlock();
   @RegistryName("platform/platform_with_block_green")
   Block PLATFORM_WITH_BLOCK_GREEN = new PlatformWithBlock();
   @RegistryName("platform/platform_with_block_orange")
   Block PLATFORM_WITH_BLOCK_ORANGE = new PlatformWithBlock();
   @RegistryName("platform/platform_with_block_white")
   Block PLATFORM_WITH_BLOCK_WHITE = new PlatformWithBlock();
   @RegistryName("platform/platform_with_block_yellow")
   Block PLATFORM_WITH_BLOCK_YELLOW = new PlatformWithBlock();
   @RegistryName("dot_packman")
   Block DOT_PACKMAN = new DotPackmanBlock();
   @RegistryName("boot")
   Block BOOT = new RotateBlockWithCustomShape(Properties.copy(Blocks.STONE).noOcclusion(), Block.box(5.0, 0.0, 6.0, 11.0, 6.5, 12.0));
   @RegistryName("wild_rice")
   Block WILD_RICE = new Block(Properties.copy(Blocks.OAK_LEAVES));
   @RegistryName("wild_rice_gray")
   Block WILD_RICE_GRAY = new Block(Properties.copy(Blocks.OAK_LEAVES));
   @RegistryName("pampushka_mushroom")
   Block PAMPUSHKA_MUSHROOM = new PampushkaMushroomBlock(Properties.copy(Blocks.GRASS_BLOCK).noOcclusion(), PampushkaMushroomBlock.BlockType.COLORED);
   @RegistryName("pampushka_mushroom_gray")
   Block PAMPUSHKA_MUSHROOM_GRAY = new PampushkaMushroomBlock(Properties.copy(Blocks.GRASS_BLOCK).noOcclusion(), PampushkaMushroomBlock.BlockType.GRAY);
   @RegistryName("eidos_shell")
   Block EIDOS_SHELL = new EidosShellBlock(Properties.copy(Blocks.GRASS).noOcclusion().noCollission(), EidosShellBlock.BlockType.COLORED);
   @RegistryName("eidos_shell_gray")
   Block EIDOS_SHELL_GRAY = new EidosShellBlock(Properties.copy(Blocks.GRASS).noOcclusion().noCollission(), EidosShellBlock.BlockType.GRAY);
   @RegistryName("lightflower")
   Block LIGHTFLOWER = new LightFlowerBlock(Properties.copy(Blocks.GRASS).noOcclusion(), LightFlowerBlock.BlockType.COLORED);
   @RegistryName("lightflower_gray")
   Block LIGHTFLOWER_GRAY = new LightFlowerBlock(Properties.copy(Blocks.GRASS).noOcclusion(), LightFlowerBlock.BlockType.GRAY);
   @RegistryName("music_crystal")
   Block MUSIC_CRYSTAL = new MusicCrystalBlock(Properties.copy(Blocks.AMETHYST_BLOCK).noOcclusion());
   @RegistryName("random_glitch")
   Block RANDOM_GLITH = new RandomGlitchBlock(Properties.copy(Blocks.BARRIER).noOcclusion());
   @RegistryName("bottleflower/red_colored")
   Block BOTTLEFLOWER_RED_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.RED, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/red_gray")
   Block BOTTLEFLOWER_RED_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.RED, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/yellow_colored")
   Block BOTTLEFLOWER_YELLOW_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.YELLOW, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/yellow_gray")
   Block BOTTLEFLOWER_YELLOW_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.YELLOW, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/green_colored")
   Block BOTTLEFLOWER_GREEN_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.GREEN, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/green_gray")
   Block BOTTLEFLOWER_GREEN_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.GREEN, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/black_colored")
   Block BOTTLEFLOWER_BLACK_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.BLACK, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/black_gray")
   Block BOTTLEFLOWER_BLACK_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.BLACK, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/blue_colored")
   Block BOTTLEFLOWER_BLUE_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.BLUE, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/blue_gray")
   Block BOTTLEFLOWER_BLUE_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.BLUE, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/cyan_colored")
   Block BOTTLEFLOWER_CYAN_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.CYAN, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/cyan_gray")
   Block BOTTLEFLOWER_CYAN_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.CYAN, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("bottleflower/violet_colored")
   Block BOTTLEFLOWER_VIOLET_COLORED = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.VIOLET, BottleFlowerBlock.FlowerType.COLORED);
   @RegistryName("bottleflower/violet_gray")
   Block BOTTLEFLOWER_VIOLET_GRAY = new BottleFlowerBlock(BottleFlowerBlock.FlowerColor.VIOLET, BottleFlowerBlock.FlowerType.GRAY);
   @RegistryName("versiform_nugget")
   Block VERSIFORM_NUGGET = new VersiformNuggetBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("ethereal_flash")
   Block ETHEREAL_FLASH = new EtherealFlashBlock(Properties.copy(Blocks.BARRIER).noCollission());
   @RegistryName("pen")
   Block PEN = new Block(Properties.copy(Blocks.OAK_WOOD).noOcclusion());
   @RegistryName("levitation_dust")
   Block LEVITATION_DUST = new LevitationDustBlock(Properties.copy(Blocks.BARRIER).noCollission());
   @RegistryName("pink_liquid")
   Block PINK_LIQUID = new PinkLiquidBlock();
   @RegistryName("trap_chest")
   Block TRAP_CHEST = new TrapChestBlock(Properties.copy(Blocks.CHEST), () -> TilesST.TRAP_CHEST);
   @RegistryName("geyser/geyser_eidos_1")
   Block GEYSER_EIDOS_1 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_eidos_2")
   Block GEYSER_EIDOS_2 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_eidos_3")
   Block GEYSER_EIDOS_3 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_ether_1")
   Block GEYSER_ETHER_1 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_ether_2")
   Block GEYSER_ETHER_2 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_ether_3")
   Block GEYSER_ETHER_3 = new RotateBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_eidos_main")
   Block GEYSER_EIDOS_MAIN = new GeyserEidosBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("geyser/geyser_ether_main")
   Block GEYSER_ETHER_MAIN = new GeyserEtherBlock(Properties.copy(Blocks.STONE).noOcclusion());
   @RegistryName("part_bridge")
   Block PART_BRIDGE = new RotateBlockWithCustomShape(Properties.copy(Blocks.STONE).noOcclusion(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0));
   @RegistryName("japan_lantern/japan_lantern")
   Block JAPAN_LANTERN = new JapanLanternBlock();
   @RegistryName("japan_lantern/japan_lantern_red")
   Block JAPAN_LANTERN_RED = new JapanLanternBlock();
}
