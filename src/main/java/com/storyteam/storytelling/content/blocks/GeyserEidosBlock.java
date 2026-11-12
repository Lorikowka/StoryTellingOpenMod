package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.BlockMultiBlock;
import com.storyteam.storytelling.content.tiles.GeyserEidosTile;
import com.storyteam.storytelling.init.TilesST;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeyserEidosBlock extends BlockMultiBlock<GeyserEidosTile> {
   public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public GeyserEidosBlock(Properties properties) {
      super(properties, true, () -> TilesST.GEYSER_EIDOS_MAIN);
   }

   @Override
   protected void getExtraPositions(Level level, BlockPos root, BlockState state, Consumer<BlockPos> positions) {
      Direction dir = (Direction)state.getValue(FACING);
      positions.accept(root.relative(dir, -1));
      positions.accept(root.relative(dir, -1).relative(dir.getClockWise(), -1));
      positions.accept(root.relative(dir.getClockWise(), -1));
   }

   @Override
   protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder_) {
      super.createBlockStateDefinition(builder_);
      builder_.add(new Property[]{FACING});
   }

   @Override
   public RenderShape getRenderShape(BlockState pState) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
      return InteractionResult.SUCCESS;
   }
}
