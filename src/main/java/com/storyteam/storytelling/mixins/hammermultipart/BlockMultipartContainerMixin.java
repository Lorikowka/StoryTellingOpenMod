package com.storyteam.storytelling.mixins.hammermultipart;

import com.storyteam.storytelling.init.FluidsST;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.api.fluid.FluidFactory;
import org.zeith.multipart.api.PartContainer;
import org.zeith.multipart.blocks.BlockMultipartContainer;

@Mixin({BlockMultipartContainer.class})
public abstract class BlockMultipartContainerMixin {
   @Shadow
   @Final
   public static BooleanProperty WATERLOGGED;
   @Unique
   private static final IntegerProperty ST$FLUID = FluidsST.ST_FLUID_PROPERTY;

   @Shadow
   public static PartContainer pc(BlockGetter get, BlockPos pos) {
      return null;
   }

   @Inject(
      method = {"createBlockStateDefinition"},
      at = {@At("TAIL")}
   )
   private void ST_createBlockStateDefinition(Builder<Block, BlockState> b, CallbackInfo ci) {
      b.add(new Property[]{ST$FLUID});
   }

   @Inject(
      method = {"getFluidState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ST_getFluidState(BlockState state, CallbackInfoReturnable<FluidState> cir) {
      int stf = (Integer)state.getValue(ST$FLUID);
      if (stf > 0) {
         FluidFactory ft = FluidsST.Ids.getById(stf);
         if (ft != null) {
            cir.setReturnValue(ft.getSource().getSource(false));
         }
      }
   }

   @Inject(
      method = {"placeLiquid"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void ST_placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState, CallbackInfoReturnable<Boolean> cir) {
      int id = FluidsST.Ids.getId(pFluidState.getFluidType());
      int stf = (Integer)pState.getValue(ST$FLUID);
      if (!(Boolean)pState.getValue(BlockStateProperties.WATERLOGGED) && stf == 0 && id > 0) {
         if (!pLevel.isClientSide()) {
            pLevel.setBlock(pPos, (BlockState)pState.setValue(ST$FLUID, id), 3);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
         }

         PartContainer pc = pc(pLevel, pPos);
         if (pc != null) {
            pc.neighborChanged(Direction.UP, pPos, pState, pLevel.getBlockState(pPos).getOptionalValue(WATERLOGGED).orElse(false));
         }

         cir.setReturnValue(true);
      }
   }

   @Inject(
      method = {"updateShape"},
      at = {@At("HEAD")}
   )
   private void ST_updateShape(
      BlockState ourState,
      Direction direction,
      BlockState neigborState,
      LevelAccessor accessor,
      BlockPos pos,
      BlockPos neigborPos,
      CallbackInfoReturnable<BlockState> cir
   ) {
      int stf = (Integer)ourState.getValue(ST$FLUID);
      if (stf > 0) {
         FluidFactory ft = FluidsST.Ids.getById(stf);
         if (ft != null) {
            accessor.scheduleTick(pos, ft.getSource(), ft.getSource().getTickDelay(accessor));
         }
      }
   }

   @Inject(
      method = {"defaultBlockState"},
      at = {@At("RETURN")},
      cancellable = true,
      remap = false
   )
   private void ST_defaultBlockState(BlockGetter level, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
      FluidState fs = level.getFluidState(pos);
      int id;
      if (!fs.isEmpty() && (id = FluidsST.Ids.getId(fs.getFluidType())) > 0) {
         cir.setReturnValue((BlockState)((BlockState)cir.getReturnValue()).setValue(ST$FLUID, id));
      }
   }
}
