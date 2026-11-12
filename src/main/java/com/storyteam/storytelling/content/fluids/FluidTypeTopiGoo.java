package com.storyteam.storytelling.content.fluids;

import com.storyteam.storytelling.St;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidType.Properties;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FluidTypeTopiGoo extends FluidType {
   public static final ResourceLocation STILL = St.id("block/topi_goo_still");
   public static final ResourceLocation FLOW = St.id("block/topi_goo_flow");

   public static FluidType create() {
      return new FluidTypeTopiGoo(
         Properties.create()
            .canDrown(true)
            .canSwim(false)
            .density(5000)
            .viscosity(8000)
            .motionScale(0.014)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
      );
   }

   protected FluidTypeTopiGoo(Properties properties) {
      super(properties);
   }

   public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
      return false;
   }

   @SubscribeEvent
   public static void livingTickEvent(LivingTickEvent e) {
      LivingEntity le = e.getEntity();
      if (le.level().isClientSide() || le.isSpectator()) {
         ;
      }
   }

   public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
      consumer.accept(new IClientFluidTypeExtensions() {
         public ResourceLocation getStillTexture() {
            return FluidTypeTopiGoo.STILL;
         }

         public ResourceLocation getFlowingTexture() {
            return FluidTypeTopiGoo.FLOW;
         }
      });
   }

   public static class TopiLiquidBlock extends LiquidBlock {
      public TopiLiquidBlock(Supplier<? extends FlowingFluid> fluid) {
         super(
            fluid,
            net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
               .replaceable()
               .noCollission()
               .strength(100.0F)
               .pushReaction(PushReaction.DESTROY)
               .noLootTable()
               .liquid()
               .sound(SoundType.EMPTY)
         );
      }
   }
}
