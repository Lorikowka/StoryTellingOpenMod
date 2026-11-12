package com.storyteam.storytelling.system.simulator.main_page.blocks;

import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.blocks.base.BlockMultiBlock;
import com.storyteam.storytelling.content.items.blockitem.CommonCustomBlockItem;
import com.storyteam.storytelling.system.simulator.main_page.client.gui.MainTabScreen;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabModels;
import com.storyteam.storytelling.system.simulator.main_page.init.MainTabTiles;
import com.storyteam.storytelling.system.simulator.main_page.tiles.SimulatorTile;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

public class SimulatorBlock extends BlockMultiBlock<SimulatorTile> implements EntityBlock, ICustomBlockItem {
   public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public SimulatorBlock() {
      super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), true, () -> MainTabTiles.SIMULATOR_TILE);
   }

   @Override
   protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder_) {
      super.createBlockStateDefinition(builder_);
      builder_.add(new Property[]{FACING});
   }

   @Nullable
   @Override
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
      if (pLevel.isClientSide()) {
         this.openScreen();
      }

      return InteractionResult.SUCCESS;
   }

   @OnlyIn(Dist.CLIENT)
   private void openScreen() {
      Minecraft.getInstance().setScreen(new MainTabScreen());
   }

   @Override
   protected void getExtraPositions(Level level, BlockPos root, BlockState state, Consumer<BlockPos> positions) {
      for (int y = 0; y < 2; y++) {
         positions.accept(root.offset(1, y, 0));
         positions.accept(root.offset(1, y, -1));
         positions.accept(root.offset(0, y, -1));
         positions.accept(root.offset(-1, y, -1));
         positions.accept(root.offset(-1, y, 0));
         positions.accept(root.offset(-1, y, 1));
         positions.accept(root.offset(0, y, 1));
         positions.accept(root.offset(1, y, 1));
      }

      Direction dir = (Direction)state.getValue(FACING);

      for (int y = 2; y < 7; y++) {
         positions.accept(root.offset(0, y, 0).relative(dir.getClockWise(), 1));
         positions.accept(root.offset(0, y, 0));
         positions.accept(root.offset(0, y, 0).relative(dir.getClockWise(), -1));
      }
   }

   @Nullable
   @Override
   public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
      return new SimulatorTile(MainTabTiles.SIMULATOR_TILE, pPos, pState);
   }

   public BlockItem createBlockItem() {
      return (BlockItem)CreativeTabAdapter.bindTab(
         new CommonCustomBlockItem(this, MainTabModels.SIMULATOR, "block/system/simulator/main_tab/simulator", 0.15F, new Vec3(-0.25, -0.5, 0.0)),
         new CreativeTab[]{TabsST.BLOCKITEMS}
      );
   }
}
