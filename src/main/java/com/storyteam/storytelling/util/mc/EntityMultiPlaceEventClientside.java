package com.storyteam.storytelling.util.mc;

import java.util.List;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class EntityMultiPlaceEventClientside extends EntityPlaceEventClientside {
   private final List<BlockSnapshot> blockSnapshots;

   public EntityMultiPlaceEventClientside(@NotNull List<BlockSnapshot> blockSnapshots, @NotNull BlockState placedAgainst, @Nullable Entity entity) {
      super(blockSnapshots.get(0), placedAgainst, entity);
      this.blockSnapshots = blockSnapshots;
   }

   @Generated
   public List<BlockSnapshot> getBlockSnapshots() {
      return this.blockSnapshots;
   }
}
