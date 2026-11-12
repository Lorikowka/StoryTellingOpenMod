package com.storyteam.storytelling.system._core.structio;

import com.mojang.datafixers.util.Either;
import com.storyteam.storytelling.mixins.forge.MissingMappingsEventEntryAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.Generated;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.MissingMappingsEvent.Action;
import net.minecraftforge.registries.MissingMappingsEvent.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.util.mcf.Resources;

public class BlockPalette {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(BlockPalette.class);
   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
   protected final Object2IntMap<BlockState> allStates = new Object2IntOpenHashMap();
   protected final List<BlockState> states;

   public BlockPalette() {
      this.allStates.defaultReturnValue(-1);
      this.states = new ArrayList<>();
   }

   public int getStateId(BlockState state) {
      return this.allStates.getInt(state);
   }

   public BlockState fromId(int id) {
      return id >= 0 && id < this.states.size() ? this.states.get(id) : AIR;
   }

   public int register(BlockState state) {
      return this.allStates.computeIfAbsent(state, s -> {
         int sz = this.states.size();
         this.states.add((BlockState)s);
         return sz;
      });
   }

   public boolean unregister(BlockState state) {
      if (!this.states.remove(state)) {
         return false;
      } else {
         this.allStates.clear();

         for (int i = 0; i < this.states.size(); i++) {
            this.allStates.put(this.states.get(i), i);
         }

         return true;
      }
   }

   public ListTag serializeNBT() {
      ListTag lst = new ListTag();

      for (BlockState state : this.states) {
         lst.add(NbtUtils.writeBlockState(state));
      }

      return lst;
   }

   public void deserializeNBT(RegistryAccess access, ListTag nbt) {
      this.states.clear();
      new HashMap();

      for (Tag tag : nbt) {
         if (tag instanceof CompoundTag ct) {
            this.states.add(readBlockState(access.lookupOrThrow(Registries.BLOCK), ct));
         } else {
            this.states.add(Blocks.AIR.defaultBlockState());
         }
      }
   }

   public static Either<Block, Block> applyRemap(HolderGetter<Block> pBlockGetter, ResourceLocation blockId) {
      Block loadedBlock = pBlockGetter.get(ResourceKey.create(Registries.BLOCK, blockId)).<Block>map(Reference::value).orElse(null);
      if (loadedBlock != null) {
         return Either.left(loadedBlock);
      } else {
         List<Mapping<?>> mappings = List.of(new Mapping(ForgeRegistries.BLOCKS, ForgeRegistries.BLOCKS, blockId, 0));
         MinecraftForge.EVENT_BUS.post(new MissingMappingsEvent(BuiltInRegistries.BLOCK.key(), ForgeRegistries.BLOCKS, mappings));
         MissingMappingsEventEntryAccessor<Block> acc = (MissingMappingsEventEntryAccessor<Block>)mappings.get(0);
         return acc.getAction() == Action.REMAP && acc.getTarget() != null ? Either.right(acc.getTarget()) : Either.left(Blocks.AIR);
      }
   }

   public static BlockState readBlockState(HolderGetter<Block> pBlockGetter, CompoundTag pTag) {
      if (!pTag.contains("Name", 8)) {
         return Blocks.AIR.defaultBlockState();
      } else {
         Block block = (Block)applyRemap(pBlockGetter, Resources.location(pTag.getString("Name"))).map(UnaryOperator.identity(), UnaryOperator.identity());
         if (block == null) {
            return Blocks.AIR.defaultBlockState();
         } else {
            BlockState blockstate = block.defaultBlockState();
            if (pTag.contains("Properties", 10)) {
               CompoundTag compoundtag = pTag.getCompound("Properties");
               StateDefinition<Block, BlockState> statedefinition = block.getStateDefinition();

               for (String s : compoundtag.getAllKeys()) {
                  Property<?> property = statedefinition.getProperty(s);
                  if (property != null) {
                     blockstate = setValueHelper(blockstate, property, s, compoundtag, pTag);
                  }
               }
            }

            return blockstate;
         }
      }
   }

   private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(
      S pStateHolder, Property<T> pProperty, String pPropertyName, CompoundTag pPropertiesTag, CompoundTag pBlockStateTag
   ) {
      Optional<T> optional = pProperty.getValue(pPropertiesTag.getString(pPropertyName));
      if (optional.isPresent()) {
         return (S)pStateHolder.setValue(pProperty, optional.get());
      } else {
         log.warn(
            "Unable to read property: {} with value: {} for blockstate: {}",
            new Object[]{pPropertyName, pPropertiesTag.getString(pPropertyName), pBlockStateTag.toString()}
         );
         return pStateHolder;
      }
   }
}
