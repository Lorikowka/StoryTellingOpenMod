package com.storyteam.storytelling.ai;

import com.storyteam.storytelling.system.navmesh.Navmesh;
import com.storyteam.storytelling.util.world.LevelObtainer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomWalkNodeEvalutor extends WalkNodeEvaluator {
   private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125;
   private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap();
   private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();

   public int getNeighbors(Node[] pOutputArray, Node pNode) {
      int i = 0;
      int j = 0;
      BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, pNode.x, pNode.y + 1, pNode.z);
      BlockPathTypes blockpathtypes1 = this.getCachedBlockType(this.mob, pNode.x, pNode.y, pNode.z);
      if (this.mob.getPathfindingMalus(blockpathtypes) >= 0.0F && blockpathtypes1 != BlockPathTypes.STICKY_HONEY) {
         j = Mth.floor(Math.max(1.0F, this.mob.getStepHeight()));
      }

      float mobWidth = this.mob.getBbWidth();
      float maxDistanceToWaypoint;
      if (mobWidth % 2.0F != 0.0F && !(mobWidth > 1.0F)) {
         maxDistanceToWaypoint = mobWidth / 2.0F;
      } else {
         maxDistanceToWaypoint = mobWidth;
      }

      int horizontalChecks = Math.max(1, Mth.ceil(maxDistanceToWaypoint));
      double d0 = this.getFloorLevel(new BlockPos(pNode.x, pNode.y, pNode.z));
      Node node = this.findAcceptedNode(pNode.x, pNode.y, pNode.z + horizontalChecks, j, d0, Direction.SOUTH, blockpathtypes1);
      if (this.isNeighborValid(node, pNode)) {
         pOutputArray[i++] = node;
      }

      Node node1 = this.findAcceptedNode(pNode.x - horizontalChecks, pNode.y, pNode.z, j, d0, Direction.WEST, blockpathtypes1);
      if (this.isNeighborValid(node1, pNode)) {
         pOutputArray[i++] = node1;
      }

      Node node2 = this.findAcceptedNode(pNode.x + horizontalChecks, pNode.y, pNode.z, j, d0, Direction.EAST, blockpathtypes1);
      if (this.isNeighborValid(node2, pNode)) {
         pOutputArray[i++] = node2;
      }

      Node node3 = this.findAcceptedNode(pNode.x, pNode.y, pNode.z - horizontalChecks, j, d0, Direction.NORTH, blockpathtypes1);
      if (this.isNeighborValid(node3, pNode)) {
         pOutputArray[i++] = node3;
      }

      Node node4 = this.findAcceptedNode(pNode.x - horizontalChecks, pNode.y, pNode.z - horizontalChecks, j, d0, Direction.NORTH, blockpathtypes1);
      if (this.isDiagonalValid(pNode, node1, node3, node4)) {
         pOutputArray[i++] = node4;
      }

      Node node5 = this.findAcceptedNode(pNode.x + horizontalChecks, pNode.y, pNode.z - horizontalChecks, j, d0, Direction.NORTH, blockpathtypes1);
      if (this.isDiagonalValid(pNode, node2, node3, node5)) {
         pOutputArray[i++] = node5;
      }

      Node node6 = this.findAcceptedNode(pNode.x - horizontalChecks, pNode.y, pNode.z + horizontalChecks, j, d0, Direction.SOUTH, blockpathtypes1);
      if (this.isDiagonalValid(pNode, node1, node, node6)) {
         pOutputArray[i++] = node6;
      }

      Node node7 = this.findAcceptedNode(pNode.x + horizontalChecks, pNode.y, pNode.z + horizontalChecks, j, d0, Direction.SOUTH, blockpathtypes1);
      if (this.isDiagonalValid(pNode, node2, node, node7)) {
         pOutputArray[i++] = node7;
      }

      return i;
   }

   @Nullable
   protected Node findAcceptedNode(int pX, int pY, int pZ, int pVerticalDeltaLimit, double pNodeFloorLevel, Direction pDirection, BlockPathTypes pPathType) {
      Node node = null;
      MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();
      double d0 = this.getFloorLevel(blockpos$mutableblockpos.set(pX, pY, pZ));
      if (d0 - pNodeFloorLevel > this.getMobJumpHeight()) {
         return null;
      } else {
         BlockPathTypes blockpathtypes = this.getCachedBlockType(this.mob, pX, pY, pZ);
         float f = this.mob.getPathfindingMalus(blockpathtypes);
         double d1 = (double)this.mob.getBbWidth() / 2.0;
         if (f >= 0.0F) {
            node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, f);
         }

         if (doesBlockHavePartialCollision(pPathType) && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
            node = null;
         }

         if (this.isEntityNearby(pX, pY, pZ)) {
            return null;
         } else if ((blockpathtypes == BlockPathTypes.WALKABLE || this.isAmphibious() && blockpathtypes == BlockPathTypes.WATER)
            && !this.isEntityNearby(pX, pY, pZ)) {
            return node;
         } else {
            if ((node == null || node.costMalus < 0.0F)
               && pVerticalDeltaLimit > 0
               && (blockpathtypes != BlockPathTypes.FENCE || this.canWalkOverFences())
               && blockpathtypes != BlockPathTypes.UNPASSABLE_RAIL
               && blockpathtypes != BlockPathTypes.TRAPDOOR
               && blockpathtypes != BlockPathTypes.POWDER_SNOW) {
               node = this.findAcceptedNode(pX, pY + 1, pZ, pVerticalDeltaLimit - 1, pNodeFloorLevel, pDirection, pPathType);
               if (node != null && (node.type == BlockPathTypes.OPEN || node.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
                  double d2 = (double)(pX - pDirection.getStepX()) + 0.5;
                  double d3 = (double)(pZ - pDirection.getStepZ()) + 0.5;
                  AABB aabb = new AABB(
                     d2 - d1,
                     this.getFloorLevel(blockpos$mutableblockpos.set(d2, (double)(pY + 1), d3)) + 0.001,
                     d3 - d1,
                     d2 + d1,
                     (double)this.mob.getBbHeight() + this.getFloorLevel(blockpos$mutableblockpos.set((double)node.x, (double)node.y, (double)node.z)) - 0.002,
                     d3 + d1
                  );
                  if (this.hasCollisions(aabb)) {
                     node = null;
                  }
               }
            }

            if (!this.isAmphibious() && blockpathtypes == BlockPathTypes.WATER && !this.canFloat()) {
               if (this.getCachedBlockType(this.mob, pX, pY - 1, pZ) != BlockPathTypes.WATER) {
                  return node;
               }

               while (pY > this.mob.level().getMinBuildHeight()) {
                  blockpathtypes = this.getCachedBlockType(this.mob, pX, --pY, pZ);
                  if (blockpathtypes != BlockPathTypes.WATER) {
                     return node;
                  }

                  node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, this.mob.getPathfindingMalus(blockpathtypes));
               }
            }

            if (blockpathtypes == BlockPathTypes.OPEN) {
               int j = 0;
               int i = pY;

               while (blockpathtypes == BlockPathTypes.OPEN) {
                  if (--pY < this.mob.level().getMinBuildHeight()) {
                     return this.getBlockedNode(pX, i, pZ);
                  }

                  if (j++ >= this.mob.getMaxFallDistance()) {
                     return this.getBlockedNode(pX, pY, pZ);
                  }

                  blockpathtypes = this.getCachedBlockType(this.mob, pX, pY, pZ);
                  f = this.mob.getPathfindingMalus(blockpathtypes);
                  if (blockpathtypes != BlockPathTypes.OPEN && f >= 0.0F) {
                     node = this.getNodeAndUpdateCostToMax(pX, pY, pZ, blockpathtypes, f);
                     break;
                  }

                  if (f < 0.0F) {
                     return this.getBlockedNode(pX, pY, pZ);
                  }
               }
            }

            if (doesBlockHavePartialCollision(blockpathtypes) && node == null) {
               node = this.getNode(pX, pY, pZ);
               node.closed = true;
               node.type = blockpathtypes;
               node.costMalus = blockpathtypes.getMalus();
            }

            return node;
         }
      }
   }

   private boolean canReachWithoutCollision(Node pNode) {
      AABB aabb = this.mob.getBoundingBox();
      Vec3 vec3 = new Vec3(
         (double)pNode.x - this.mob.getX() + aabb.getXsize() / 2.0,
         (double)pNode.y - this.mob.getY() + aabb.getYsize() / 2.0,
         (double)pNode.z - this.mob.getZ() + aabb.getZsize() / 2.0
      );
      int i = Mth.ceil(vec3.length() / aabb.getSize());
      vec3 = vec3.scale((double)(1.0F / (float)i));

      for (int j = 1; j <= i; j++) {
         aabb = aabb.move(vec3);
         if (this.hasCollisions(aabb)) {
            return false;
         }
      }

      return true;
   }

   private static boolean doesBlockHavePartialCollision(BlockPathTypes pBlockPathType) {
      return pBlockPathType == BlockPathTypes.FENCE || pBlockPathType == BlockPathTypes.DOOR_WOOD_CLOSED || pBlockPathType == BlockPathTypes.DOOR_IRON_CLOSED;
   }

   private double getMobJumpHeight() {
      return Math.max(1.125, (double)this.mob.getStepHeight());
   }

   private Node getNodeAndUpdateCostToMax(int pX, int pY, int pZ, BlockPathTypes pType, float pCostMalus) {
      Node node = this.getNode(pX, pY, pZ);
      node.type = pType;
      node.costMalus = Math.max(node.costMalus, pCostMalus);
      return node;
   }

   private Node getBlockedNode(int pX, int pY, int pZ) {
      Node node = this.getNode(pX, pY, pZ);
      node.type = BlockPathTypes.BLOCKED;
      node.costMalus = -1.0F;
      return node;
   }

   private boolean hasCollisions(AABB pBoundingBox) {
      return this.collisionCache.computeIfAbsent(pBoundingBox, p_192973_ -> !this.level.noCollision(this.mob, pBoundingBox));
   }

   private boolean isEntityNearby(int x, int y, int z) {
      BlockPos position = new BlockPos(x, y, z);
      List<LivingEntity> livingEntityList = this.mob
         .level()
         .getEntitiesOfClass(LivingEntity.class, new AABB(position))
         .stream()
         .filter(e -> e != this.mob && !(e instanceof Player))
         .toList();
      return !livingEntityList.isEmpty();
   }

   @NotNull
   public BlockPathTypes getBlockPathType(BlockGetter pLevel, int pX, int pY, int pZ) {
      BlockPos pos = new BlockPos(pX, pY, pZ);
      Level level = LevelObtainer.fromBlockGetter(pLevel);
      if (level != null && Navmesh.isPositionBlockedForNavigation(level, pos)) {
         return BlockPathTypes.BLOCKED;
      } else {
         BlockState state = pLevel.getBlockState(pos);
         BlockPathTypes blockPathTypes = !(state.getBlock() instanceof FlowerBlock) && !(state.getBlock() instanceof TallFlowerBlock)
            ? (
               state.getBlock() instanceof ScaffoldingBlock
                  ? BlockPathTypes.OPEN
                  : (state.getBlock() instanceof FenceGateBlock ? BlockPathTypes.WALKABLE_DOOR : super.getBlockPathType(pLevel, pX, pY, pZ))
            )
            : BlockPathTypes.BREACH;
         if (blockPathTypes == BlockPathTypes.DOOR_OPEN && this.mob.getBbWidth() > 1.0F) {
            blockPathTypes = BlockPathTypes.BLOCKED;
         }

         return blockPathTypes;
      }
   }

   public boolean canPassDoors() {
      return true;
   }

   public boolean canOpenDoors() {
      return false;
   }
}
