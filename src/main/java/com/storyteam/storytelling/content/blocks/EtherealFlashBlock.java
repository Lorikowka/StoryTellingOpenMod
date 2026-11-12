package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.RotateBlockEntity;
import com.storyteam.storytelling.content.tiles.EtherealFlashTile;
import com.storyteam.storytelling.init.TilesST;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EtherealFlashBlock extends RotateBlockEntity<EtherealFlashTile> {
   public EtherealFlashBlock(Properties pProperties) {
      super(pProperties, true, () -> TilesST.ETHEREAL_FLASH);
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         ItemStack drop = new ItemStack(Blocks.SPONGE);
         if (!player.addItem(drop)) {
            player.drop(drop, false);
         }

         level.removeBlock(pos, false);
         return InteractionResult.CONSUME;
      }
   }

   @NotNull
   public RenderShape getRenderShape(@NotNull BlockState pState) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);
   }
}
