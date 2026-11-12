package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.St;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.blocks.base.RotateBlockEntity;
import com.storyteam.storytelling.content.items.base.CommonCustomBlockItem;
import com.storyteam.storytelling.content.tiles.EidosShellTile;
import com.storyteam.storytelling.init.BlocksST;
import com.storyteam.storytelling.init.TilesST;
import com.storyteam.storytelling.init.geo.BlockEntityModels;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

public class EidosShellBlock extends RotateBlockEntity<EidosShellTile> implements ICustomBlockItem {
   public static final ResourceLocation EMISSIVE_TEXTURE = St.tx("tile/eidos_shell/eidos_shell_emissive");
   public static final ResourceLocation COLORED_TEXTURE = St.tx("tile/eidos_shell/eidos_shell");
   public static final ResourceLocation GRAY_TEXTURE = St.tx("tile/eidos_shell/gray_eidos_shell");
   private final EidosShellBlock.BlockType type;

   public EidosShellBlock(Properties pProperties, EidosShellBlock.BlockType type) {
      super(pProperties, true, () -> TilesST.EIDOS_SHELL);
      this.type = type;
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         ItemStack drop = new ItemStack(BlocksST.EIDOS_SHELL, 1);
         if (!player.addItem(drop)) {
            player.drop(drop, false);
         }

         level.removeBlock(pos, false);
         return InteractionResult.CONSUME;
      }
   }

   public BlockItem createBlockItem() {
      return (BlockItem)CreativeTabAdapter.bindTab(
         new CommonCustomBlockItem(
            this,
            BlockEntityModels.EIDOS_SHELL,
            this.type == EidosShellBlock.BlockType.COLORED ? COLORED_TEXTURE : GRAY_TEXTURE,
            0.45F,
            new Vec3(0.0, -0.5, 0.0),
            true
         ),
         new CreativeTab[]{TabsST.BLOCKITEMS}
      );
   }

   public static ResourceLocation getEmissiveTexture() {
      return EMISSIVE_TEXTURE;
   }

   public static EidosShellBlock.BlockType getType(BlockState blockState) {
      return blockState.getBlock() instanceof EidosShellBlock eidosShellBlock ? eidosShellBlock.getType() : EidosShellBlock.BlockType.COLORED;
   }

   public static ResourceLocation getBaseTexture(EidosShellBlock.BlockType type) {
      return type == EidosShellBlock.BlockType.COLORED ? COLORED_TEXTURE : GRAY_TEXTURE;
   }

   @Generated
   public EidosShellBlock.BlockType getType() {
      return this.type;
   }

   public static enum BlockType {
      COLORED,
      GRAY;
   }
}
