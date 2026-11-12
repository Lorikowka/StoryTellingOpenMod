package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.blocks.base.RotateBlockEntity;
import com.storyteam.storytelling.content.items.base.CommonCustomBlockItem;
import com.storyteam.storytelling.content.tiles.LightFlowerTile;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import lombok.Generated;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

public class LightFlowerBlock extends RotateBlockEntity<LightFlowerTile> implements ICustomBlockItem {
   public static final ResourceLocation COLORED_TEXTURE = St.tx("tile/lightflower");
   public static final ResourceLocation GRAY_TEXTURE = St.tx("tile/lightflower_gray");
   private final LightFlowerBlock.BlockType type;

   public LightFlowerBlock(Properties pProperties, LightFlowerBlock.BlockType type) {
      super(pProperties, true, () -> TilesST.LIGHTFLOWER);
      this.type = type;
   }

   public BlockItem createBlockItem() {
      return (BlockItem)CreativeTabAdapter.bindTab(
         new CommonCustomBlockItem(
            this,
            BlockEntityModels.LIGHTFLOWER,
            this.type == LightFlowerBlock.BlockType.COLORED ? COLORED_TEXTURE : GRAY_TEXTURE,
            0.45F,
            new Vec3(0.0, -0.5, 0.0),
            true
         ),
         new CreativeTab[]{TabsST.BLOCKITEMS}
      );
   }

   @Generated
   public LightFlowerBlock.BlockType getType() {
      return this.type;
   }

   public static enum BlockType {
      COLORED,
      GRAY;
   }
}
