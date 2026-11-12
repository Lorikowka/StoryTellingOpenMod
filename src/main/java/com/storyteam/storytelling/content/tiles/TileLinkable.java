package com.storyteam.storytelling.content.tiles;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.tiles.TileSyncableTickable;

public class TileLinkable extends TileSyncableTickable {
   @NBTSerializable("Parent")
   public BlockPos parent;
   @NBTSerializable("Children")
   public BlockPos[] children;
   @NBTSerializable
   public UUID structureId;
   protected AABB multiblockBoundaryCache;

   public TileLinkable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public Stream<BlockPos> getAllParts() {
      if (this.children != null && this.children.length > 0) {
         return Stream.concat(Stream.of(this.worldPosition), Arrays.stream(this.children));
      } else {
         return this.level != null
               && this.parent != null
               && !Objects.equals(this.parent, this.worldPosition)
               && this.level.getBlockEntity(this.parent) instanceof TileLinkable tl
               && this.isThisPart(tl)
            ? tl.getAllParts()
            : Stream.empty();
      }
   }

   public AABB getBoundary() {
      if (this.multiblockBoundaryCache != null) {
         return this.multiblockBoundaryCache;
      } else {
         Vector3d min = new Vector3d(Double.MAX_VALUE);
         Vector3d max = new Vector3d(Double.MIN_VALUE);
         boolean counted = false;

         for (Iterator<BlockPos> it = this.getAllParts().iterator(); it.hasNext(); counted = true) {
            BlockPos cord = it.next();
            min.min(new Vector3d((double)cord.getX(), (double)cord.getY(), (double)cord.getZ()));
            max.max(new Vector3d((double)(cord.getX() + 1), (double)(cord.getY() + 1), (double)(cord.getZ() + 1)));
         }

         return !counted ? null : (this.multiblockBoundaryCache = new AABB(min.x, min.y, min.z, max.x, max.y, max.z));
      }
   }

   public boolean isValid() {
      if (this.level == null) {
         return true;
      } else if (this.parent != null && !Objects.equals(this.parent, this.worldPosition)) {
         return this.isThisPart(this.level.getBlockEntity(this.worldPosition));
      } else {
         return this.children != null && this.children.length > 0
            ? Arrays.stream(this.children).<BlockEntity>map(this.level::getBlockEntity).allMatch(this::isThisPart)
            : false;
      }
   }

   public boolean isThisPart(BlockEntity tile) {
      if (tile instanceof TileLinkable tl && Objects.equals(tl.structureId, this.structureId)) {
         return true;
      }

      return false;
   }

   public BlockPos getMain() {
      return this.parent != null ? this.parent : this.worldPosition;
   }
}
