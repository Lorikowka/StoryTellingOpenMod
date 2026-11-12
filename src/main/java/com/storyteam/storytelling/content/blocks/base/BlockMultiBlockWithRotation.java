package com.storyteam.storytelling.content.blocks.base;

import com.storyteam.storytelling.content.tiles.TileLinkable;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.zeith.hammerlib.util.mcf.shapes.ShapeUtils;

public abstract class BlockMultiBlockWithRotation<TILE extends TileLinkable> extends BlockMultiBlock<TILE> {
   protected List<VoxelShape> staticShapes = List.of();
   protected boolean oppositeFaceForPlacement;

   protected BlockMultiBlockWithRotation(Properties pProperties, boolean doesTick, Supplier<BlockEntityType<TILE>> blockEntityTypeSupplier) {
      super(pProperties, doesTick, blockEntityTypeSupplier);
   }

   protected void updateStaticShapes() {
      this.staticShapes = List.of(
         this.getStaticShape(Direction.SOUTH), this.getStaticShape(Direction.WEST), this.getStaticShape(Direction.NORTH), this.getStaticShape(Direction.EAST)
      );
   }

   protected abstract VoxelShape getNorthShape();

   protected VoxelShape getStaticShape(Direction face) {
      return ShapeUtils.rotateNorthShape(this.getNorthShape(), face);
   }

   protected VoxelShape getMultiblockShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      if (this.staticShapes.isEmpty()) {
         this.updateStaticShapes();
      }

      return this.staticShapes.get(((Direction)pState.getValue(BlockStateProperties.HORIZONTAL_FACING)).get2DDataValue());
   }

   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      BlockPos main = this.getMainPos(pLevel, pPos);
      if (main == null) {
         return Shapes.empty();
      } else {
         VoxelShape box = this.getMultiblockShape(pState, pLevel, pPos, pContext);
         if (main.equals(pPos)) {
            return box;
         } else {
            BlockPos off = main.subtract(pPos);
            return box.move((double)off.getX(), (double)off.getY(), (double)off.getZ());
         }
      }
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
      pBuilder.add(new Property[]{BlockStateProperties.HORIZONTAL_FACING});
      super.createBlockStateDefinition(pBuilder);
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      return (BlockState)super.getStateForPlacement(pContext)
         .setValue(
            BlockStateProperties.HORIZONTAL_FACING,
            this.oppositeFaceForPlacement ? pContext.getHorizontalDirection().getOpposite() : pContext.getHorizontalDirection()
         );
   }
}
