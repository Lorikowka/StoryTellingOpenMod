package com.storyteam.storytelling.util.mc;

import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class EntityPlaceEventClientside extends BlockEvent {
   private final Entity entity;
   private final BlockSnapshot blockSnapshot;
   private final BlockState placedBlock;
   private final BlockState placedAgainst;

   public EntityPlaceEventClientside(@NotNull BlockSnapshot blockSnapshot, @NotNull BlockState placedAgainst, @Nullable Entity entity) {
      super(blockSnapshot.getLevel(), blockSnapshot.getPos(), !(entity instanceof Player) ? blockSnapshot.getReplacedBlock() : blockSnapshot.getCurrentBlock());
      this.entity = entity;
      this.blockSnapshot = blockSnapshot;
      this.placedBlock = !(entity instanceof Player) ? blockSnapshot.getReplacedBlock() : blockSnapshot.getCurrentBlock();
      this.placedAgainst = placedAgainst;
   }

   @Nullable
   public Entity getEntity() {
      return this.entity;
   }

   @Generated
   public BlockSnapshot getBlockSnapshot() {
      return this.blockSnapshot;
   }

   @Generated
   public BlockState getPlacedBlock() {
      return this.placedBlock;
   }

   @Generated
   public BlockState getPlacedAgainst() {
      return this.placedAgainst;
   }
}
