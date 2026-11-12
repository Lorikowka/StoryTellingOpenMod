package com.storyteam.storytelling.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.storyteam.storytelling.St;
import com.storyteam.storytelling.content.tiles.TrapChestTile;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner.Combiner;
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class TrapChestTESR implements BlockEntityRenderer<TrapChestTile> {
   private final ModelPart lid;
   private final ModelPart bottom;
   private final ModelPart lock;
   private final ModelPart doubleLeftLid;
   private final ModelPart doubleLeftBottom;
   private final ModelPart doubleLeftLock;
   private final ModelPart doubleRightLid;
   private final ModelPart doubleRightBottom;
   private final ModelPart doubleRightLock;

   public TrapChestTESR(Context pContext) {
      ModelPart modelpart = pContext.bakeLayer(ModelLayers.CHEST);
      this.bottom = modelpart.getChild("bottom");
      this.lid = modelpart.getChild("lid");
      this.lock = modelpart.getChild("lock");
      ModelPart modelpart1 = pContext.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT);
      this.doubleLeftBottom = modelpart1.getChild("bottom");
      this.doubleLeftLid = modelpart1.getChild("lid");
      this.doubleLeftLock = modelpart1.getChild("lock");
      ModelPart modelpart2 = pContext.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT);
      this.doubleRightBottom = modelpart2.getChild("bottom");
      this.doubleRightLid = modelpart2.getChild("lid");
      this.doubleRightLock = modelpart2.getChild("lock");
   }

   public void render(TrapChestTile tile, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
      Level level = tile.getLevel();
      boolean hasLevel = level != null;
      BlockState blockState = hasLevel ? tile.getBlockState() : (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
      ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? (ChestType)blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
      if (blockState.getBlock() instanceof AbstractChestBlock<?> abstractChestBlock) {
         boolean isDouble = chestType != ChestType.SINGLE;
         poseStack.pushPose();
         float rotation = ((Direction)blockState.getValue(ChestBlock.FACING)).toYRot();
         poseStack.translate(0.5F, 0.5F, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
         poseStack.translate(-0.5F, -0.5F, -0.5F);
         NeighborCombineResult<? extends ChestBlockEntity> neighborCombiner = hasLevel
            ? abstractChestBlock.combine(blockState, level, tile.getBlockPos(), true)
            : Combiner::acceptNone;
         float lidAngle = tile.lidProgress;
         int light = ((Int2IntFunction)neighborCombiner.apply(new BrightnessCombiner())).applyAsInt(packedLight);
         Material material = this.getMaterial(tile, chestType);
         VertexConsumer vertexConsumer = material.buffer(buffer, RenderType::entityCutout);
         if (isDouble) {
            if (chestType == ChestType.LEFT) {
               this.render(poseStack, vertexConsumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, lidAngle, light, packedOverlay);
            } else {
               this.render(poseStack, vertexConsumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, lidAngle, light, packedOverlay);
            }
         } else {
            this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, lidAngle, light, packedOverlay);
         }

         poseStack.popPose();
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void render(
      PoseStack poseStack, VertexConsumer consumer, ModelPart lid, ModelPart lock, ModelPart bottom, float lidAngle, int packedLight, int packedOverlay
   ) {
      lid.xRot = -(lidAngle * (float) (Math.PI / 2));
      lock.xRot = lid.xRot;
      lid.render(poseStack, consumer, packedLight, packedOverlay);
      lock.render(poseStack, consumer, packedLight, packedOverlay);
      bottom.render(poseStack, consumer, packedLight, packedOverlay);
   }

   @NotNull
   protected Material getMaterial(@NotNull TrapChestTile blockEntity, @NotNull ChestType chestType) {
      return new Material(Sheets.CHEST_SHEET, St.id("entity/chest/trap"));
   }
}
