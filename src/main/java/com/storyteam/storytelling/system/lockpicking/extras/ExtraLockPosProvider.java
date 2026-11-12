package com.storyteam.storytelling.system.lockpicking.extras;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.zeith.hammerlib.util.java.consumers.Consumer4;
import org.zeith.hammerlib.util.java.predicates.Predicate3;

public record ExtraLockPosProvider(Predicate3<ServerLevel, BlockPos, BlockState> test, ExtraLockPosProvider.Func extras) {
   private static final List<ExtraLockPosProvider> REGISTRY = new ArrayList<>();
   public static final ExtraLockPosProvider DOOR_PROVIDER = forBlock(
      DoorBlock.class, (level, pos, state, sink) -> sink.accept(pos.above(state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? 1 : -1))
   );
   public static final ExtraLockPosProvider CHEST_PROVIDER = forBlock(
      ChestBlock.class,
      s -> s.getValue(ChestBlock.TYPE) != ChestType.SINGLE,
      (l, pos, state, sink) -> sink.accept(pos.relative(ChestBlock.getConnectedDirection(state)))
   );

   public ExtraLockPosProvider(Predicate3<ServerLevel, BlockPos, BlockState> test, ExtraLockPosProvider.Func extras) {
      REGISTRY.add(this);
      this.test = test;
      this.extras = extras;
   }

   public static <T extends Block> ExtraLockPosProvider forBlock(Class<T> blockType, ExtraLockPosProvider.Func extras) {
      return new ExtraLockPosProvider((level, pos, state) -> blockType.isInstance(state.getBlock()), extras);
   }

   public static <T extends Block> ExtraLockPosProvider forBlock(Class<T> blockType, Predicate<BlockState> filter, ExtraLockPosProvider.Func extras) {
      return new ExtraLockPosProvider((level, pos, state) -> blockType.isInstance(state.getBlock()) && filter.test(state), extras);
   }

   public static Iterable<ExtraLockPosProvider> all() {
      return REGISTRY;
   }

   public interface Func extends Consumer4<ServerLevel, BlockPos, BlockState, Consumer<BlockPos>> {
      void accept(ServerLevel var1, BlockPos var2, BlockState var3, Consumer<BlockPos> var4);
   }
}
