package com.storyteam.storytelling.content.blocks.base;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.tiles.TileLinkable;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.util.java.Cast;

public abstract class BlockMultiBlock<TILE extends TileLinkable> extends Block implements EntityBlock, ICreativeTabBlock {
   public static BooleanProperty OFFSET = BooleanProperty.create("offset");
   protected final Supplier<BlockEntityType<TILE>> typeSupplier;
   protected boolean doesTick;

   protected BlockMultiBlock(Properties pProperties, boolean doesTick, Supplier<BlockEntityType<TILE>> typeSupplier) {
      super(pProperties);
      this.doesTick = doesTick;
      this.typeSupplier = typeSupplier;
   }

   protected abstract void getExtraPositions(Level var1, BlockPos var2, BlockState var3, Consumer<BlockPos> var4);

   @Nullable
   protected AABB getMultiBlockBounds(BlockGetter level, BlockPos pos) {
      return level.getBlockEntity(pos) instanceof TileLinkable tl ? tl.getBoundary() : null;
   }

   @Nullable
   public BlockPos getMainPos(BlockGetter level, BlockPos pos) {
      return level.getBlockEntity(pos) instanceof TileLinkable tl ? tl.getMain() : null;
   }

   public static Stream<BlockPos> getAllPos(BlockGetter level, BlockPos pos) {
      return level.getBlockEntity(pos) instanceof TileLinkable tl ? tl.getAllParts() : Stream.empty();
   }

   public void onPlace(BlockState pState, Level world, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
      super.onPlace(pState, world, pos, pOldState, pIsMoving);
      if (!world.isClientSide && !(Boolean)pState.getValue(OFFSET)) {
         Builder<BlockPos> posBuilder = Stream.builder();
         this.getExtraPositions(world, pos, pState, posBuilder);
         BlockPos[] positions = posBuilder.build().<BlockPos>map(BlockPos::immutable).filter(p0 -> !p0.equals(pos)).toArray(BlockPos[]::new);
         UUID id = UUID.randomUUID();
         TileLinkable tl = (TileLinkable)Cast.cast(world.getBlockEntity(pos), TileLinkable.class);
         if (tl == null) {
            world.destroyBlock(pos, false);
            return;
         }

         tl.structureId = id;
         tl.children = (BlockPos[])positions.clone();

         for (int i = 0; i < positions.length; i++) {
            BlockPos remPos = positions[i];
            world.setBlockAndUpdate(remPos, (BlockState)pState.setValue(OFFSET, true));
            TileLinkable tlr = (TileLinkable)Cast.cast(world.getBlockEntity(remPos), TileLinkable.class);
            if (tlr == null) {
               for (int j = 0; j <= i; j++) {
                  world.destroyBlock(positions[j], false);
               }

               world.destroyBlock(pos, false);
               return;
            }

            tlr.structureId = id;
            tlr.parent = pos.immutable();
         }
      }
   }

   public void onRemove(BlockState pState, Level world, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
      if (!pNewState.is(this)) {
         TileLinkable tl = (TileLinkable)Cast.cast(world.getBlockEntity(pos), TileLinkable.class);
         if (tl != null) {
            tl.getAllParts().forEach(remPos -> {
               if (tl.isThisPart(world.getBlockEntity(remPos))) {
                  world.destroyBlock(remPos, false);
               }
            });
         }

         super.onRemove(pState, world, pos, pNewState, pIsMoving);
      }
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
      return BlockAPI.ticker(pLevel);
   }

   @Nullable
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return this.typeSupplier.get().create(pPos, pState);
   }

   protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
      super.createBlockStateDefinition(pBuilder);
      pBuilder.add(new Property[]{OFFSET});
   }

   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
      return (BlockState)this.defaultBlockState().setValue(OFFSET, false);
   }

   protected RenderShape getRootRenderShape(BlockState pState) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public RenderShape getRenderShape(BlockState pState) {
      return pState.getValue(OFFSET) ? RenderShape.INVISIBLE : this.getRootRenderShape(pState);
   }

   public CreativeTab getCreativeTab() {
      return TabsST.ITEM_GROUP;
   }

   public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
      return ItemStack.EMPTY;
   }

   public static class CustomShapeBlock extends Block {
      VoxelShape shape;

      public CustomShapeBlock(Properties pProperties, VoxelShape shape) {
         super(pProperties);
         this.shape = shape;
      }

      public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
         return this.shape;
      }
   }
}
