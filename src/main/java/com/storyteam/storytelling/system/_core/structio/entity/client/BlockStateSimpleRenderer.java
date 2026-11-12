package com.storyteam.storytelling.system._core.structio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;

public class BlockStateSimpleRenderer {
   static final ThreadLocal<BlockStateSimpleRenderer.Cache> CACHE = ThreadLocal.withInitial(BlockStateSimpleRenderer.Cache::new);
   static final Direction[] DIRECTIONS = Direction.values();
   private final BlockModelShaper models;

   public BlockStateSimpleRenderer(Minecraft mc) {
      this.models = mc.getBlockRenderer().getBlockModelShaper();
   }

   public void renderBatched(
      BlockState pState,
      BlockPos pPos,
      BlockGetter pLevel,
      PoseStack pPoseStack,
      MultiBufferSource pConsumer,
      boolean pCheckSides,
      RandomSource pRandom,
      ModelData modelData
   ) {
      try {
         RenderShape rendershape = pState.getRenderShape();
         if (rendershape == RenderShape.MODEL) {
            this.tesselateBlock(
               pLevel,
               this.models.getBlockModel(pState),
               pState,
               pPos,
               pPoseStack,
               pConsumer,
               pCheckSides,
               pRandom,
               pState.getSeed(pPos),
               OverlayTexture.NO_OVERLAY,
               modelData
            );
         }
      } catch (Throwable var12) {
         CrashReport crashreport = CrashReport.forThrowable(var12, "Tesselating block in world");
         CrashReportCategory crashreportcategory = crashreport.addCategory("Block being tesselated");
         CrashReportCategory.populateBlockDetails(crashreportcategory, pLevel, pPos, pState);
         throw new ReportedException(crashreport);
      }
   }

   public void tesselateBlock(
      BlockGetter level,
      BakedModel model,
      BlockState state,
      BlockPos pos,
      PoseStack pose,
      MultiBufferSource p_111053_,
      boolean pCheckSides,
      RandomSource p_111055_,
      long p_111056_,
      int p_111057_,
      ModelData modelData
   ) {
      boolean flag = Minecraft.useAmbientOcclusion() && state.getLightEmission(level, pos) == 0 && model.useAmbientOcclusion(state);
      Vec3 vec3 = state.getOffset(level, pos);
      pose.translate(vec3.x, vec3.y, vec3.z);

      try {
         if (flag) {
            this.tesselateWithAO(level, model, state, pos, pose, p_111053_, pCheckSides, p_111055_, p_111056_, p_111057_, modelData);
         } else {
            this.tesselateWithoutAO(level, model, state, pos, pose, p_111053_, pCheckSides, p_111055_, p_111056_, p_111057_, modelData);
         }
      } catch (Throwable var18) {
         CrashReport crashreport = CrashReport.forThrowable(var18, "Tesselating block model");
         CrashReportCategory crashreportcategory = crashreport.addCategory("Block model being tesselated");
         CrashReportCategory.populateBlockDetails(crashreportcategory, level, pos, state);
         throw new ReportedException(crashreport);
      }
   }

   public void tesselateWithAO(
      BlockGetter level,
      BakedModel model,
      BlockState state,
      BlockPos pos,
      PoseStack pose,
      MultiBufferSource bufs,
      boolean pCheckSides,
      RandomSource rng,
      long seed,
      int p_111088_,
      ModelData modelData
   ) {
      float[] afloat = new float[DIRECTIONS.length * 2];
      BitSet bitset = new BitSet(3);
      BlockStateSimpleRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new BlockStateSimpleRenderer.AmbientOcclusionFace();
      MutableBlockPos blockpos$mutableblockpos = pos.mutable();
      rng.setSeed(seed);
      ChunkRenderTypeSet types = model.getRenderTypes(state, rng, modelData);

      for (Direction direction : DIRECTIONS) {
         rng.setSeed(seed);

         for (RenderType renderType : types) {
            List<BakedQuad> list = model.getQuads(state, direction, rng, modelData, renderType);
            if (!list.isEmpty()) {
               blockpos$mutableblockpos.setWithOffset(pos, direction);
               if (!pCheckSides || Block.shouldRenderFace(state, level, pos, direction, blockpos$mutableblockpos)) {
                  this.renderModelFaceAO(
                     level, state, pos, pose, bufs.getBuffer(renderType), list, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_
                  );
               }
            }
         }
      }

      for (RenderType renderTypex : types) {
         rng.setSeed(seed);
         List<BakedQuad> list1 = model.getQuads(state, null, rng, modelData, renderTypex);
         if (!list1.isEmpty()) {
            this.renderModelFaceAO(
               level, state, pos, pose, bufs.getBuffer(renderTypex), list1, afloat, bitset, modelblockrenderer$ambientocclusionface, p_111088_
            );
         }
      }
   }

   public void tesselateWithoutAO(
      BlockGetter level,
      BakedModel model,
      BlockState state,
      BlockPos pos,
      PoseStack mat,
      MultiBufferSource vc,
      boolean p_111097_,
      RandomSource rng,
      long seed,
      int p_111100_,
      ModelData modelData
   ) {
      BitSet bitset = new BitSet(3);
      MutableBlockPos mutPos = pos.mutable();
      rng.setSeed(seed);
      ChunkRenderTypeSet types = model.getRenderTypes(state, rng, modelData);

      for (Direction direction : DIRECTIONS) {
         rng.setSeed(seed);

         for (RenderType type : types) {
            List<BakedQuad> quads = model.getQuads(state, direction, rng, modelData, type);
            if (!quads.isEmpty()) {
               mutPos.setWithOffset(pos, direction);
               if (!p_111097_ || Block.shouldRenderFace(state, level, pos, direction, mutPos)) {
                  int i = getLightColor(level, state, mutPos);
                  this.renderModelFaceFlat(level, state, pos, i, p_111100_, false, mat, vc.getBuffer(type), quads, bitset);
               }
            }
         }
      }

      for (RenderType typex : types) {
         rng.setSeed(seed);
         List<BakedQuad> list1 = model.getQuads(state, null, rng, modelData, typex);
         if (!list1.isEmpty()) {
            this.renderModelFaceFlat(level, state, pos, -1, p_111100_, true, mat, vc.getBuffer(typex), list1, bitset);
         }
      }
   }

   public static int getLightColor(BlockGetter pLevel, BlockState pState, BlockPos pPos) {
      if (pState.emissiveRendering(pLevel, pPos)) {
         return 15728880;
      } else {
         int i = 15;
         int j = 0;
         int k = pState.getLightEmission(pLevel, pPos);
         if (j < k) {
            j = k;
         }

         return i << 20 | j << 4;
      }
   }

   public static boolean ForgeHooksClient_calculateFaceWithoutAO(
      BlockGetter getter, BlockState state, BlockPos pos, BakedQuad quad, boolean isFaceCubic, float[] brightness, int[] lightmap
   ) {
      if (quad.hasAmbientOcclusion()) {
         return false;
      } else {
         BlockPos lightmapPos = isFaceCubic ? pos.relative(quad.getDirection()) : pos;
         brightness[0] = brightness[1] = brightness[2] = brightness[3] = getShade(quad.getDirection(), quad.isShade());
         lightmap[0] = lightmap[1] = lightmap[2] = lightmap[3] = getLightColor(getter, state, lightmapPos);
         return true;
      }
   }

   private void renderModelFaceAO(
      BlockGetter pLevel,
      BlockState pState,
      BlockPos pPos,
      PoseStack pPoseStack,
      VertexConsumer pConsumer,
      List<BakedQuad> pQuads,
      float[] pShape,
      BitSet pShapeFlags,
      BlockStateSimpleRenderer.AmbientOcclusionFace pAoFace,
      int pPackedOverlay
   ) {
      for (BakedQuad bakedquad : pQuads) {
         this.calculateShape(pLevel, pState, pPos, bakedquad.getVertices(), bakedquad.getDirection(), pShape, pShapeFlags);
         if (!ForgeHooksClient_calculateFaceWithoutAO(pLevel, pState, pPos, bakedquad, pShapeFlags.get(0), pAoFace.brightness, pAoFace.lightmap)) {
            pAoFace.calculate(pLevel, pState, pPos, bakedquad.getDirection(), pShape, pShapeFlags, bakedquad.isShade());
         }

         this.putQuadData(
            pConsumer,
            pPoseStack.last(),
            bakedquad,
            pAoFace.brightness[0],
            pAoFace.brightness[1],
            pAoFace.brightness[2],
            pAoFace.brightness[3],
            pAoFace.lightmap[0],
            pAoFace.lightmap[1],
            pAoFace.lightmap[2],
            pAoFace.lightmap[3],
            pPackedOverlay
         );
      }
   }

   private void renderModelFaceFlat(
      BlockGetter pLevel,
      BlockState pState,
      BlockPos pPos,
      int pPackedLight,
      int pPackedOverlay,
      boolean pRepackLight,
      PoseStack pPoseStack,
      VertexConsumer pConsumer,
      List<BakedQuad> pQuads,
      BitSet pShapeFlags
   ) {
      for (BakedQuad bakedquad : pQuads) {
         if (pRepackLight) {
            this.calculateShape(pLevel, pState, pPos, bakedquad.getVertices(), bakedquad.getDirection(), null, pShapeFlags);
         }

         float f = getShade(bakedquad.getDirection(), bakedquad.isShade());
         this.putQuadData(pConsumer, pPoseStack.last(), bakedquad, f, f, f, f, pPackedLight, pPackedLight, pPackedLight, pPackedLight, pPackedOverlay);
      }
   }

   public static float getShade(Direction pDirection, boolean pShade) {
      boolean flag = true;
      if (!pShade) {
         return 0.9F;
      } else {
         return switch (pDirection) {
            case DOWN, UP -> 0.9F;
            case NORTH, SOUTH -> 0.8F;
            case WEST, EAST -> 0.6F;
            default -> 1.0F;
         };
      }
   }

   private void putQuadData(
      VertexConsumer pConsumer,
      Pose pPose,
      BakedQuad pQuad,
      float pBrightness0,
      float pBrightness1,
      float pBrightness2,
      float pBrightness3,
      int pLightmap0,
      int pLightmap1,
      int pLightmap2,
      int pLightmap3,
      int pPackedOverlay
   ) {
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      pConsumer.putBulkData(
         pPose,
         pQuad,
         new float[]{pBrightness0, pBrightness1, pBrightness2, pBrightness3},
         r,
         g,
         b,
         new int[]{pLightmap0, pLightmap1, pLightmap2, pLightmap3},
         pPackedOverlay,
         true
      );
   }

   private void calculateShape(
      BlockGetter pLevel, BlockState pState, BlockPos pPos, int[] pVertices, Direction pDirection, @Nullable float[] pShape, BitSet pShapeFlags
   ) {
      float f = 32.0F;
      float f1 = 32.0F;
      float f2 = 32.0F;
      float f3 = -32.0F;
      float f4 = -32.0F;
      float f5 = -32.0F;

      for (int i = 0; i < 4; i++) {
         float f6 = Float.intBitsToFloat(pVertices[i * 8]);
         float f7 = Float.intBitsToFloat(pVertices[i * 8 + 1]);
         float f8 = Float.intBitsToFloat(pVertices[i * 8 + 2]);
         f = Math.min(f, f6);
         f1 = Math.min(f1, f7);
         f2 = Math.min(f2, f8);
         f3 = Math.max(f3, f6);
         f4 = Math.max(f4, f7);
         f5 = Math.max(f5, f8);
      }

      if (pShape != null) {
         pShape[Direction.WEST.get3DDataValue()] = f;
         pShape[Direction.EAST.get3DDataValue()] = f3;
         pShape[Direction.DOWN.get3DDataValue()] = f1;
         pShape[Direction.UP.get3DDataValue()] = f4;
         pShape[Direction.NORTH.get3DDataValue()] = f2;
         pShape[Direction.SOUTH.get3DDataValue()] = f5;
         int j = DIRECTIONS.length;
         pShape[Direction.WEST.get3DDataValue() + j] = 1.0F - f;
         pShape[Direction.EAST.get3DDataValue() + j] = 1.0F - f3;
         pShape[Direction.DOWN.get3DDataValue() + j] = 1.0F - f1;
         pShape[Direction.UP.get3DDataValue() + j] = 1.0F - f4;
         pShape[Direction.NORTH.get3DDataValue() + j] = 1.0F - f2;
         pShape[Direction.SOUTH.get3DDataValue() + j] = 1.0F - f5;
      }

      float f9 = 1.0E-4F;
      float f10 = 0.9999F;
      switch (pDirection) {
         case DOWN:
            pShapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
            pShapeFlags.set(0, f1 == f4 && (f1 < 1.0E-4F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
            break;
         case UP:
            pShapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
            pShapeFlags.set(0, f1 == f4 && (f4 > 0.9999F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
            break;
         case NORTH:
            pShapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
            pShapeFlags.set(0, f2 == f5 && (f2 < 1.0E-4F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
            break;
         case SOUTH:
            pShapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
            pShapeFlags.set(0, f2 == f5 && (f5 > 0.9999F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
            break;
         case WEST:
            pShapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
            pShapeFlags.set(0, f == f3 && (f < 1.0E-4F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
            break;
         case EAST:
            pShapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
            pShapeFlags.set(0, f == f3 && (f3 > 0.9999F || pState.isCollisionShapeFullBlock(pLevel, pPos)));
      }
   }

   public static void clearCache() {
      CACHE.get().disable();
   }

   public static void enableCaching() {
      CACHE.get().enable();
   }

   @OnlyIn(Dist.CLIENT)
   protected static enum AdjacencyInfo {
      DOWN(
         new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH},
         0.5F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         }
      ),
      UP(
         new Direction[]{Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH},
         1.0F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         }
      ),
      NORTH(
         new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST},
         0.8F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST
         }
      ),
      SOUTH(
         new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP},
         0.8F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.WEST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_WEST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.WEST,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.WEST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.EAST
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_EAST,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.EAST,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.EAST
         }
      ),
      WEST(
         new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH},
         0.6F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         }
      ),
      EAST(
         new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH},
         0.6F,
         true,
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.DOWN,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.NORTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_NORTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.NORTH
         },
         new BlockStateSimpleRenderer.SizeInfo[]{
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.SOUTH,
            BlockStateSimpleRenderer.SizeInfo.FLIP_UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.FLIP_SOUTH,
            BlockStateSimpleRenderer.SizeInfo.UP,
            BlockStateSimpleRenderer.SizeInfo.SOUTH
         }
      );

      final Direction[] corners;
      final boolean doNonCubicWeight;
      final BlockStateSimpleRenderer.SizeInfo[] vert0Weights;
      final BlockStateSimpleRenderer.SizeInfo[] vert1Weights;
      final BlockStateSimpleRenderer.SizeInfo[] vert2Weights;
      final BlockStateSimpleRenderer.SizeInfo[] vert3Weights;
      private static final BlockStateSimpleRenderer.AdjacencyInfo[] BY_FACING = (BlockStateSimpleRenderer.AdjacencyInfo[])Util.make(
         new BlockStateSimpleRenderer.AdjacencyInfo[6], p_111134_ -> {
            p_111134_[Direction.DOWN.get3DDataValue()] = DOWN;
            p_111134_[Direction.UP.get3DDataValue()] = UP;
            p_111134_[Direction.NORTH.get3DDataValue()] = NORTH;
            p_111134_[Direction.SOUTH.get3DDataValue()] = SOUTH;
            p_111134_[Direction.WEST.get3DDataValue()] = WEST;
            p_111134_[Direction.EAST.get3DDataValue()] = EAST;
         }
      );

      private AdjacencyInfo(
         Direction[] pCorners,
         float pShadeBrightness,
         boolean pDoNonCubicWeight,
         BlockStateSimpleRenderer.SizeInfo[] pVert0Weights,
         BlockStateSimpleRenderer.SizeInfo[] pVert1Weights,
         BlockStateSimpleRenderer.SizeInfo[] pVert2Weights,
         BlockStateSimpleRenderer.SizeInfo[] pVert3Weights
      ) {
         this.corners = pCorners;
         this.doNonCubicWeight = pDoNonCubicWeight;
         this.vert0Weights = pVert0Weights;
         this.vert1Weights = pVert1Weights;
         this.vert2Weights = pVert2Weights;
         this.vert3Weights = pVert3Weights;
      }

      public static BlockStateSimpleRenderer.AdjacencyInfo fromFacing(Direction pFacing) {
         return BY_FACING[pFacing.get3DDataValue()];
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class AmbientOcclusionFace {
      final float[] brightness = new float[4];
      final int[] lightmap = new int[4];

      public AmbientOcclusionFace() {
      }

      public void calculate(BlockGetter pLevel, BlockState pState, BlockPos pPos, Direction pDirection, float[] pShape, BitSet pShapeFlags, boolean pShade) {
         BlockPos blockpos = pShapeFlags.get(0) ? pPos.relative(pDirection) : pPos;
         BlockStateSimpleRenderer.AdjacencyInfo modelblockrenderer$adjacencyinfo = BlockStateSimpleRenderer.AdjacencyInfo.fromFacing(pDirection);
         MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();
         BlockStateSimpleRenderer.Cache modelblockrenderer$cache = BlockStateSimpleRenderer.CACHE.get();
         blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[0]);
         BlockState blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
         int i = modelblockrenderer$cache.getLightColor(blockstate, pLevel, blockpos$mutableblockpos);
         float f = modelblockrenderer$cache.getShadeBrightness(blockstate, pLevel, blockpos$mutableblockpos);
         blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[1]);
         BlockState blockstate1 = pLevel.getBlockState(blockpos$mutableblockpos);
         int j = modelblockrenderer$cache.getLightColor(blockstate1, pLevel, blockpos$mutableblockpos);
         float f1 = modelblockrenderer$cache.getShadeBrightness(blockstate1, pLevel, blockpos$mutableblockpos);
         blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[2]);
         BlockState blockstate2 = pLevel.getBlockState(blockpos$mutableblockpos);
         int k = modelblockrenderer$cache.getLightColor(blockstate2, pLevel, blockpos$mutableblockpos);
         float f2 = modelblockrenderer$cache.getShadeBrightness(blockstate2, pLevel, blockpos$mutableblockpos);
         blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[3]);
         BlockState blockstate3 = pLevel.getBlockState(blockpos$mutableblockpos);
         int l = modelblockrenderer$cache.getLightColor(blockstate3, pLevel, blockpos$mutableblockpos);
         float f3 = modelblockrenderer$cache.getShadeBrightness(blockstate3, pLevel, blockpos$mutableblockpos);
         BlockState blockstate4 = pLevel.getBlockState(
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[0]).move(pDirection)
         );
         boolean flag = !blockstate4.isViewBlocking(pLevel, blockpos$mutableblockpos) || blockstate4.getLightBlock(pLevel, blockpos$mutableblockpos) == 0;
         BlockState blockstate5 = pLevel.getBlockState(
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[1]).move(pDirection)
         );
         boolean flag1 = !blockstate5.isViewBlocking(pLevel, blockpos$mutableblockpos) || blockstate5.getLightBlock(pLevel, blockpos$mutableblockpos) == 0;
         BlockState blockstate6 = pLevel.getBlockState(
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[2]).move(pDirection)
         );
         boolean flag2 = !blockstate6.isViewBlocking(pLevel, blockpos$mutableblockpos) || blockstate6.getLightBlock(pLevel, blockpos$mutableblockpos) == 0;
         BlockState blockstate7 = pLevel.getBlockState(
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[3]).move(pDirection)
         );
         boolean flag3 = !blockstate7.isViewBlocking(pLevel, blockpos$mutableblockpos) || blockstate7.getLightBlock(pLevel, blockpos$mutableblockpos) == 0;
         float f4;
         int i1;
         if (!flag2 && !flag) {
            f4 = f;
            i1 = i;
         } else {
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[0]).move(modelblockrenderer$adjacencyinfo.corners[2]);
            BlockState blockstate8 = pLevel.getBlockState(blockpos$mutableblockpos);
            f4 = modelblockrenderer$cache.getShadeBrightness(blockstate8, pLevel, blockpos$mutableblockpos);
            i1 = modelblockrenderer$cache.getLightColor(blockstate8, pLevel, blockpos$mutableblockpos);
         }

         int j1;
         float f5;
         if (!flag3 && !flag) {
            f5 = f;
            j1 = i;
         } else {
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[0]).move(modelblockrenderer$adjacencyinfo.corners[3]);
            BlockState blockstate10 = pLevel.getBlockState(blockpos$mutableblockpos);
            f5 = modelblockrenderer$cache.getShadeBrightness(blockstate10, pLevel, blockpos$mutableblockpos);
            j1 = modelblockrenderer$cache.getLightColor(blockstate10, pLevel, blockpos$mutableblockpos);
         }

         int k1;
         float f6;
         if (!flag2 && !flag1) {
            f6 = f;
            k1 = i;
         } else {
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[1]).move(modelblockrenderer$adjacencyinfo.corners[2]);
            BlockState blockstate11 = pLevel.getBlockState(blockpos$mutableblockpos);
            f6 = modelblockrenderer$cache.getShadeBrightness(blockstate11, pLevel, blockpos$mutableblockpos);
            k1 = modelblockrenderer$cache.getLightColor(blockstate11, pLevel, blockpos$mutableblockpos);
         }

         int l1;
         float f7;
         if (!flag3 && !flag1) {
            f7 = f;
            l1 = i;
         } else {
            blockpos$mutableblockpos.setWithOffset(blockpos, modelblockrenderer$adjacencyinfo.corners[1]).move(modelblockrenderer$adjacencyinfo.corners[3]);
            BlockState blockstate12 = pLevel.getBlockState(blockpos$mutableblockpos);
            f7 = modelblockrenderer$cache.getShadeBrightness(blockstate12, pLevel, blockpos$mutableblockpos);
            l1 = modelblockrenderer$cache.getLightColor(blockstate12, pLevel, blockpos$mutableblockpos);
         }

         int i3 = modelblockrenderer$cache.getLightColor(pState, pLevel, pPos);
         blockpos$mutableblockpos.setWithOffset(pPos, pDirection);
         BlockState blockstate9 = pLevel.getBlockState(blockpos$mutableblockpos);
         if (pShapeFlags.get(0) || !blockstate9.isSolidRender(pLevel, blockpos$mutableblockpos)) {
            i3 = modelblockrenderer$cache.getLightColor(blockstate9, pLevel, blockpos$mutableblockpos);
         }

         float f8 = pShapeFlags.get(0)
            ? modelblockrenderer$cache.getShadeBrightness(pLevel.getBlockState(blockpos), pLevel, blockpos)
            : modelblockrenderer$cache.getShadeBrightness(pLevel.getBlockState(pPos), pLevel, pPos);
         BlockStateSimpleRenderer.AmbientVertexRemap modelblockrenderer$ambientvertexremap = BlockStateSimpleRenderer.AmbientVertexRemap.fromFacing(pDirection);
         if (pShapeFlags.get(1) && modelblockrenderer$adjacencyinfo.doNonCubicWeight) {
            float f29 = (f3 + f + f5 + f8) * 0.25F;
            float f31 = (f2 + f + f4 + f8) * 0.25F;
            float f32 = (f2 + f1 + f6 + f8) * 0.25F;
            float f33 = (f3 + f1 + f7 + f8) * 0.25F;
            float f13 = pShape[modelblockrenderer$adjacencyinfo.vert0Weights[0].shape] * pShape[modelblockrenderer$adjacencyinfo.vert0Weights[1].shape];
            float f14 = pShape[modelblockrenderer$adjacencyinfo.vert0Weights[2].shape] * pShape[modelblockrenderer$adjacencyinfo.vert0Weights[3].shape];
            float f15 = pShape[modelblockrenderer$adjacencyinfo.vert0Weights[4].shape] * pShape[modelblockrenderer$adjacencyinfo.vert0Weights[5].shape];
            float f16 = pShape[modelblockrenderer$adjacencyinfo.vert0Weights[6].shape] * pShape[modelblockrenderer$adjacencyinfo.vert0Weights[7].shape];
            float f17 = pShape[modelblockrenderer$adjacencyinfo.vert1Weights[0].shape] * pShape[modelblockrenderer$adjacencyinfo.vert1Weights[1].shape];
            float f18 = pShape[modelblockrenderer$adjacencyinfo.vert1Weights[2].shape] * pShape[modelblockrenderer$adjacencyinfo.vert1Weights[3].shape];
            float f19 = pShape[modelblockrenderer$adjacencyinfo.vert1Weights[4].shape] * pShape[modelblockrenderer$adjacencyinfo.vert1Weights[5].shape];
            float f20 = pShape[modelblockrenderer$adjacencyinfo.vert1Weights[6].shape] * pShape[modelblockrenderer$adjacencyinfo.vert1Weights[7].shape];
            float f21 = pShape[modelblockrenderer$adjacencyinfo.vert2Weights[0].shape] * pShape[modelblockrenderer$adjacencyinfo.vert2Weights[1].shape];
            float f22 = pShape[modelblockrenderer$adjacencyinfo.vert2Weights[2].shape] * pShape[modelblockrenderer$adjacencyinfo.vert2Weights[3].shape];
            float f23 = pShape[modelblockrenderer$adjacencyinfo.vert2Weights[4].shape] * pShape[modelblockrenderer$adjacencyinfo.vert2Weights[5].shape];
            float f24 = pShape[modelblockrenderer$adjacencyinfo.vert2Weights[6].shape] * pShape[modelblockrenderer$adjacencyinfo.vert2Weights[7].shape];
            float f25 = pShape[modelblockrenderer$adjacencyinfo.vert3Weights[0].shape] * pShape[modelblockrenderer$adjacencyinfo.vert3Weights[1].shape];
            float f26 = pShape[modelblockrenderer$adjacencyinfo.vert3Weights[2].shape] * pShape[modelblockrenderer$adjacencyinfo.vert3Weights[3].shape];
            float f27 = pShape[modelblockrenderer$adjacencyinfo.vert3Weights[4].shape] * pShape[modelblockrenderer$adjacencyinfo.vert3Weights[5].shape];
            float f28 = pShape[modelblockrenderer$adjacencyinfo.vert3Weights[6].shape] * pShape[modelblockrenderer$adjacencyinfo.vert3Weights[7].shape];
            this.brightness[modelblockrenderer$ambientvertexremap.vert0] = f29 * f13 + f31 * f14 + f32 * f15 + f33 * f16;
            this.brightness[modelblockrenderer$ambientvertexremap.vert1] = f29 * f17 + f31 * f18 + f32 * f19 + f33 * f20;
            this.brightness[modelblockrenderer$ambientvertexremap.vert2] = f29 * f21 + f31 * f22 + f32 * f23 + f33 * f24;
            this.brightness[modelblockrenderer$ambientvertexremap.vert3] = f29 * f25 + f31 * f26 + f32 * f27 + f33 * f28;
            int i2 = this.blend(l, i, j1, i3);
            int j2 = this.blend(k, i, i1, i3);
            int k2 = this.blend(k, j, k1, i3);
            int l2 = this.blend(l, j, l1, i3);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert0] = this.blend(i2, j2, k2, l2, f13, f14, f15, f16);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert1] = this.blend(i2, j2, k2, l2, f17, f18, f19, f20);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert2] = this.blend(i2, j2, k2, l2, f21, f22, f23, f24);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert3] = this.blend(i2, j2, k2, l2, f25, f26, f27, f28);
         } else {
            float f9 = (f3 + f + f5 + f8) * 0.25F;
            float f10 = (f2 + f + f4 + f8) * 0.25F;
            float f11 = (f2 + f1 + f6 + f8) * 0.25F;
            float f12 = (f3 + f1 + f7 + f8) * 0.25F;
            this.lightmap[modelblockrenderer$ambientvertexremap.vert0] = this.blend(l, i, j1, i3);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert1] = this.blend(k, i, i1, i3);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert2] = this.blend(k, j, k1, i3);
            this.lightmap[modelblockrenderer$ambientvertexremap.vert3] = this.blend(l, j, l1, i3);
            this.brightness[modelblockrenderer$ambientvertexremap.vert0] = f9;
            this.brightness[modelblockrenderer$ambientvertexremap.vert1] = f10;
            this.brightness[modelblockrenderer$ambientvertexremap.vert2] = f11;
            this.brightness[modelblockrenderer$ambientvertexremap.vert3] = f12;
         }

         float f30 = BlockStateSimpleRenderer.getShade(pDirection, pShade);

         for (int j3 = 0; j3 < this.brightness.length; j3++) {
            this.brightness[j3] = this.brightness[j3] * f30;
         }
      }

      private int blend(int pLightColor0, int pLightColor1, int pLightColor2, int pLightColor3) {
         if (pLightColor0 == 0) {
            pLightColor0 = pLightColor3;
         }

         if (pLightColor1 == 0) {
            pLightColor1 = pLightColor3;
         }

         if (pLightColor2 == 0) {
            pLightColor2 = pLightColor3;
         }

         return pLightColor0 + pLightColor1 + pLightColor2 + pLightColor3 >> 2 & 16711935;
      }

      private int blend(int pBrightness0, int pBrightness1, int pBrightness2, int pBrightness3, float pWeight0, float pWeight1, float pWeight2, float pWeight3) {
         int i = (int)(
               (float)(pBrightness0 >> 16 & 0xFF) * pWeight0
                  + (float)(pBrightness1 >> 16 & 0xFF) * pWeight1
                  + (float)(pBrightness2 >> 16 & 0xFF) * pWeight2
                  + (float)(pBrightness3 >> 16 & 0xFF) * pWeight3
            )
            & 0xFF;
         int j = (int)(
               (float)(pBrightness0 & 0xFF) * pWeight0
                  + (float)(pBrightness1 & 0xFF) * pWeight1
                  + (float)(pBrightness2 & 0xFF) * pWeight2
                  + (float)(pBrightness3 & 0xFF) * pWeight3
            )
            & 0xFF;
         return i << 16 | j;
      }
   }

   @OnlyIn(Dist.CLIENT)
   static enum AmbientVertexRemap {
      DOWN(0, 1, 2, 3),
      UP(2, 3, 0, 1),
      NORTH(3, 0, 1, 2),
      SOUTH(0, 1, 2, 3),
      WEST(3, 0, 1, 2),
      EAST(1, 2, 3, 0);

      final int vert0;
      final int vert1;
      final int vert2;
      final int vert3;
      private static final BlockStateSimpleRenderer.AmbientVertexRemap[] BY_FACING = (BlockStateSimpleRenderer.AmbientVertexRemap[])Util.make(
         new BlockStateSimpleRenderer.AmbientVertexRemap[6], p_111204_ -> {
            p_111204_[Direction.DOWN.get3DDataValue()] = DOWN;
            p_111204_[Direction.UP.get3DDataValue()] = UP;
            p_111204_[Direction.NORTH.get3DDataValue()] = NORTH;
            p_111204_[Direction.SOUTH.get3DDataValue()] = SOUTH;
            p_111204_[Direction.WEST.get3DDataValue()] = WEST;
            p_111204_[Direction.EAST.get3DDataValue()] = EAST;
         }
      );

      private AmbientVertexRemap(int pVert0, int pVert1, int pVert2, int pVert3) {
         this.vert0 = pVert0;
         this.vert1 = pVert1;
         this.vert2 = pVert2;
         this.vert3 = pVert3;
      }

      public static BlockStateSimpleRenderer.AmbientVertexRemap fromFacing(Direction pFacing) {
         return BY_FACING[pFacing.get3DDataValue()];
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class Cache {
      private boolean enabled;
      private final Long2IntLinkedOpenHashMap colorCache = (Long2IntLinkedOpenHashMap)Util.make(() -> {
         Long2IntLinkedOpenHashMap long2intlinkedopenhashmap = new Long2IntLinkedOpenHashMap(100, 0.25F) {
            protected void rehash(int p_111238_) {
            }
         };
         long2intlinkedopenhashmap.defaultReturnValue(Integer.MAX_VALUE);
         return long2intlinkedopenhashmap;
      });
      private final Long2FloatLinkedOpenHashMap brightnessCache = (Long2FloatLinkedOpenHashMap)Util.make(() -> {
         Long2FloatLinkedOpenHashMap long2floatlinkedopenhashmap = new Long2FloatLinkedOpenHashMap(100, 0.25F) {
            protected void rehash(int p_111245_) {
            }
         };
         long2floatlinkedopenhashmap.defaultReturnValue(Float.NaN);
         return long2floatlinkedopenhashmap;
      });

      private Cache() {
      }

      public void enable() {
         this.enabled = true;
      }

      public void disable() {
         this.enabled = false;
         this.colorCache.clear();
         this.brightnessCache.clear();
      }

      public int getLightColor(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
         long i = pPos.asLong();
         if (this.enabled) {
            int j = this.colorCache.get(i);
            if (j != Integer.MAX_VALUE) {
               return j;
            }
         }

         int k = BlockStateSimpleRenderer.getLightColor(pLevel, pState, pPos);
         if (this.enabled) {
            if (this.colorCache.size() == 100) {
               this.colorCache.removeFirstInt();
            }

            this.colorCache.put(i, k);
         }

         return k;
      }

      public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
         long i = pPos.asLong();
         if (this.enabled) {
            float f = this.brightnessCache.get(i);
            if (!Float.isNaN(f)) {
               return f;
            }
         }

         float f1 = pState.getShadeBrightness(pLevel, pPos);
         if (this.enabled) {
            if (this.brightnessCache.size() == 100) {
               this.brightnessCache.removeFirstFloat();
            }

            this.brightnessCache.put(i, f1);
         }

         return f1;
      }
   }

   @OnlyIn(Dist.CLIENT)
   protected static enum SizeInfo {
      DOWN(Direction.DOWN, false),
      UP(Direction.UP, false),
      NORTH(Direction.NORTH, false),
      SOUTH(Direction.SOUTH, false),
      WEST(Direction.WEST, false),
      EAST(Direction.EAST, false),
      FLIP_DOWN(Direction.DOWN, true),
      FLIP_UP(Direction.UP, true),
      FLIP_NORTH(Direction.NORTH, true),
      FLIP_SOUTH(Direction.SOUTH, true),
      FLIP_WEST(Direction.WEST, true),
      FLIP_EAST(Direction.EAST, true);

      final int shape;

      private SizeInfo(Direction pDirection, boolean pFlip) {
         this.shape = pDirection.get3DDataValue() + (pFlip ? BlockStateSimpleRenderer.DIRECTIONS.length : 0);
      }
   }
}
