package com.storyteam.storytelling.ai;

import com.storyteam.storytelling.content.caps.StoryData;
import com.storyteam.storytelling.content.entities.AnimatedEntity;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomWalkNodeEvalutorHuman extends WalkNodeEvaluator {
   private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125;
   private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap();
   private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();

   public int getNeighbors(Node[] pOutputArray, Node pNode) {
      if (this.mob instanceof AnimatedEntity e && StoryData.of(this.mob) != null && StoryData.of(this.mob).traversalNavigation.getBoolean()) {
         int i = 0;
         int j = 0;
         Direction forward = this.mob.getDirection();
         Direction right = forward.getClockWise();
         Direction left = forward.getCounterClockWise();
         Direction backward = forward.getOpposite();
         BlockPathTypes above = this.getCachedBlockType(this.mob, pNode.x, pNode.y + 1, pNode.z);
         BlockPathTypes current = this.getCachedBlockType(this.mob, pNode.x, pNode.y, pNode.z);
         if (this.mob.getPathfindingMalus(above) >= 0.0F && current != BlockPathTypes.STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.getStepHeight()));
         }

         double floorLevel = this.getFloorLevel(new BlockPos(pNode.x, pNode.y, pNode.z));
         float width = this.mob.getBbWidth();
         int step = Math.max(1, Mth.ceil(width % 2.0F != 0.0F && !(width > 1.0F) ? width / 2.0F : width));
         Direction[] mainDirs = new Direction[]{forward, left, right, backward};

         for (Direction dir : mainDirs) {
            int dx = pNode.x + dir.getStepX() * step;
            int dz = pNode.z + dir.getStepZ() * step;
            Node node = this.findAcceptedNode(dx, pNode.y, dz, j, floorLevel, dir, current);
            if (this.isNeighborValid(node, pNode)) {
               pOutputArray[i++] = node;
            }
         }

         Direction[][] diagonals = new Direction[][]{{forward, left}, {forward, right}, {backward, left}, {backward, right}};

         for (Direction[] pair : diagonals) {
            Direction dir1 = pair[0];
            Direction dir2 = pair[1];
            int dx = pNode.x + dir1.getStepX() * step + dir2.getStepX() * step;
            int dz = pNode.z + dir1.getStepZ() * step + dir2.getStepZ() * step;
            Node main1 = this.findAcceptedNode(pNode.x + dir1.getStepX() * step, pNode.y, pNode.z + dir1.getStepZ() * step, j, floorLevel, dir1, current);
            Node main2 = this.findAcceptedNode(pNode.x + dir2.getStepX() * step, pNode.y, pNode.z + dir2.getStepZ() * step, j, floorLevel, dir2, current);
            Node diag = this.findAcceptedNode(dx, pNode.y, dz, j, floorLevel, dir1, current);
            if (this.isDiagonalValid(pNode, main2, main1, diag)) {
               pOutputArray[i++] = diag;
            }
         }

         return i;
      }

      return super.getNeighbors(pOutputArray, pNode);
   }

   @Nullable
   protected Node findAcceptedNode(int pX, int pY, int pZ, int pVerticalDeltaLimit, double pNodeFloorLevel, Direction pDirection, BlockPathTypes pPathType) {
      if (this.mob instanceof AnimatedEntity e && StoryData.of(this.mob) != null && StoryData.of(this.mob).traversalNavigation.getBoolean()) {
         Node node = null;
         MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();
         double d0 = this.getFloorLevel(blockpos$mutableblockpos.set(pX, pY, pZ));
         if (d0 - pNodeFloorLevel > this.getMobJumpHeight()) {
            return null;
         }

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
         }

         if ((blockpathtypes == BlockPathTypes.WALKABLE || this.isAmphibious() && blockpathtypes == BlockPathTypes.WATER) && !this.isEntityNearby(pX, pY, pZ)) {
            return node;
         }

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

      return super.findAcceptedNode(pX, pY, pZ, pVerticalDeltaLimit, pNodeFloorLevel, pDirection, pPathType);
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

   public void done() {
      this.mob.onPathfindingDone();
      this.pathTypesByPosCache.clear();
      this.collisionCache.clear();
      super.done();
   }

   protected BlockPathTypes getCachedBlockType(Mob pEntity, int pX, int pY, int pZ) {
      return (BlockPathTypes)this.pathTypesByPosCache
         .computeIfAbsent(BlockPos.asLong(pX, pY, pZ), p_265015_ -> this.getBlockPathType(this.level, pX, pY, pZ, pEntity));
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
         ResourceLocation industrial_iron_catwalk_stairsId = new ResourceLocation("createdeco", "industrial_iron_catwalk_stairs");
         Block industrial_iron_catwalk_stairs = (Block)ForgeRegistries.BLOCKS.getValue(industrial_iron_catwalk_stairsId);
         ResourceLocation xkdecoId = new ResourceLocation("xkdeco", "air_duct");
         Block xkdeco = (Block)ForgeRegistries.BLOCKS.getValue(xkdecoId);
         if (!state.getBlock().equals(industrial_iron_catwalk_stairs) && !state.getBlock().equals(xkdeco)) {
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
         } else {
            return BlockPathTypes.WALKABLE;
         }
      }
   }

   public boolean canPassDoors() {
      return true;
   }

   public boolean canOpenDoors() {
      return false;
   }
}
