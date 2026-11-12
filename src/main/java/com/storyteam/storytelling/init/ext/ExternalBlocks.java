package com.storyteam.storytelling.init.ext;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.util.mcf.Resources;

public class ExternalBlocks {
   public static Block WASHING_MACHINE;

   public static void setup() {
      WASHING_MACHINE = getBlock("yuushya:washing_machine");
   }

   private static Block getBlock(String id) {
      return (Block)ForgeRegistries.BLOCKS.getValue(Resources.location(id));
   }

   public static void bind() {
   }
}
