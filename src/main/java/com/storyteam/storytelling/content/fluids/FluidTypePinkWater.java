package com.storyteam.storytelling.content.fluids;

import com.storyteam.storytelling.St;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidType.Properties;

public class FluidTypePinkWater extends FluidType {
   public static final ResourceLocation STILL = St.id("block/water_pink_still");
   public static final ResourceLocation FLOW = St.id("block/water_pink_flow");

   public static FluidType create() {
      return new FluidTypePinkWater(
         Properties.create()
            .canDrown(true)
            .canSwim(false)
            .density(1000)
            .viscosity(1000)
            .motionScale(0.014)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
      );
   }

   protected FluidTypePinkWater(Properties properties) {
      super(properties);
   }

   public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
      return false;
   }

   public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
      consumer.accept(new IClientFluidTypeExtensions() {
         public ResourceLocation getStillTexture() {
            return FluidTypePinkWater.STILL;
         }

         public ResourceLocation getFlowingTexture() {
            return FluidTypePinkWater.FLOW;
         }
      });
   }

   public static class PinkWaterBlock extends LiquidBlock {
      public PinkWaterBlock(Supplier<? extends FlowingFluid> fluid) {
         super(
            fluid,
            net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
               .replaceable()
               .noCollission()
               .strength(100.0F)
               .pushReaction(PushReaction.DESTROY)
               .noLootTable()
               .liquid()
               .mapColor(MapColor.COLOR_PINK)
               .sound(SoundType.EMPTY)
         );
      }
   }
}
