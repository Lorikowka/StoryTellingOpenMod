package com.storyteam.storytelling.content.blocks;

import com.storyteam.storytelling.content.blocks.base.RotateBlockEntity;
import com.storyteam.storytelling.content.tiles.LevitationDustTile;
import com.storyteam.storytelling.init.TilesST;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LevitationDustBlock extends RotateBlockEntity<LevitationDustTile> {
   public static final IntegerProperty LIFT_HEIGHT = IntegerProperty.create("lift_height", 1, 100);

   public LevitationDustBlock(Properties pProperties) {
      super(pProperties, true, () -> TilesST.LEVITATION_DUST);
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LIFT_HEIGHT, 3));
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{LIFT_HEIGHT});
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!level.isClientSide && player.isCreative()) {
         boolean sneaking = player.isShiftKeyDown();
         float current = (float)((Integer)state.getValue(LIFT_HEIGHT)).intValue();
         float newHeight = sneaking ? current - 1.0F : current + 1.0F;
         newHeight = Mth.clamp(newHeight, 1.0F, 100.0F);
         level.setBlock(pos, (BlockState)state.setValue(LIFT_HEIGHT, (int)newHeight), 3);
         player.displayClientMessage(Component.literal("Новая высота потока: " + String.format("%.1f", newHeight) + " блоков"), true);
         return InteractionResult.CONSUME;
      } else {
         return InteractionResult.SUCCESS;
      }
   }

   @NotNull
   public RenderShape getRenderShape(@NotNull BlockState pState) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Shapes.empty();
   }
}
