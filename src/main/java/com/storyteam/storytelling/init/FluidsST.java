package com.storyteam.storytelling.init;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.storyteam.storytelling.TabsST;
import com.storyteam.storytelling.content.fluids.FluidTypePinkWater;
import com.storyteam.storytelling.content.fluids.FluidTypeTopiGoo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.fluids.FluidType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.fluid.FluidFactory;
import org.zeith.hammerlib.api.fluid.FluidFactory.Builder;

@SimplyRegister(
   prefix = "fluids/"
)
public interface FluidsST {
   @RegistryName("topi_goo")
   FluidFactory TOPI_GOO = FluidsST.Ids.register(
      "<auto>",
      FluidTypeTopiGoo::create,
      t -> t.withRenderType(() -> RenderType::translucent)
            .propertyModifier(p -> p.tickRate(7))
            .withBlock(FluidTypeTopiGoo.TopiLiquidBlock::new)
            .withBucket()
            .addToTab(TabsST.ITEM_GROUP)
            .addFluidTags(List.of())
   );
   @RegistryName("pink_water")
   FluidFactory PINK_WATER = FluidsST.Ids.register(
      "<auto>",
      FluidTypePinkWater::create,
      t -> t.withRenderType(() -> RenderType::translucent)
            .propertyModifier(p -> p.tickRate(7))
            .withBlock(FluidTypePinkWater.PinkWaterBlock::new)
            .withBucket()
            .addToTab(TabsST.ITEM_GROUP)
            .addFluidTags(List.of(FluidTags.WATER))
   );
   int TOTAL_FLUID_COUNT = FluidsST.Ids.TYPE_REGISTRY.size();
   IntegerProperty ST_FLUID_PROPERTY = IntegerProperty.create("st_fluid", 0, TOTAL_FLUID_COUNT);

   public static class Ids {
      private static final Int2ObjectMap<FluidFactory> TYPE_REGISTRY = new Int2ObjectOpenHashMap();
      private static final Object2IntMap<FluidType> TYPE_REGISTRY_INV = new Object2IntOpenHashMap();
      private static final BiMap<String, FluidFactory> BINDINGS = HashBiMap.create();

      private static FluidFactory register(String auto, Supplier<FluidType> ft, Consumer<Builder> configurator) {
         int id = TYPE_REGISTRY.size() + 1;
         Builder f = FluidFactory.builder(Suppliers.memoize(() -> {
            FluidType r = ft.get();
            TYPE_REGISTRY_INV.put(r, id);
            return r;
         }));
         configurator.accept(f);
         FluidFactory t = f.build();
         TYPE_REGISTRY.put(id, t);
         BINDINGS.put(auto, t);
         return t;
      }

      public static int getId(FluidType type) {
         return TYPE_REGISTRY_INV.getInt(type);
      }

      public static FluidFactory getById(int id) {
         return (FluidFactory)TYPE_REGISTRY.get(id);
      }

      public static BiMap<String, FluidFactory> getBindings() {
         return BINDINGS;
      }
   }
}
