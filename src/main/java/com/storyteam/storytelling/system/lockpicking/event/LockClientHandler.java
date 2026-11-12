package com.storyteam.storytelling.system.lockpicking.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.storyteam.storytelling.system.lockpicking.data.LockRegistryEntry;
import com.storyteam.storytelling.system.lockpicking.data.LockStorage;
import com.storyteam.storytelling.util.ScriptUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LockClientHandler {
   public static final List<LockRegistryEntry> RENDERED_ENTRIES = new ArrayList<>();
   static Level level;
   private static final Map<Class<? extends Block>, BiConsumer<LockRegistryEntry, RenderLevelStageEvent>> RENDERERS = Map.ofEntries(
      Map.entry(DoorBlock.class, LockClientHandler::doorRender)
   );

   @SubscribeEvent
   public static void rend(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         RENDERED_ENTRIES.clear();
         Minecraft mc = Minecraft.getInstance();
         LocalPlayer player = mc.player;
         int fov = (Integer)mc.options.fov().get() + 30;
         boolean firstPerson = mc.options.getCameraType().isFirstPerson();
         if (player == null) {
            level = null;
         } else {
            level = player.level();
            ResourceKey<Level> dim = level.dimension();
            Vec3 ppos = player.position();

            for (Entry<GlobalPos, LockRegistryEntry> e : LockStorage.REGISTRY.entrySet()) {
               GlobalPos gpos = e.getKey();
               if (!e.getValue().isUnlocked()
                  && dim.equals(gpos.dimension())
                  && !(gpos.pos().distToCenterSqr(ppos) > 400.0)
                  && (!firstPerson || !(ScriptUtils.getAngle(player, gpos.pos().getCenter()) > (double)fov))) {
                  RENDERED_ENTRIES.add(e.getValue());
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void rend(RenderLevelStageEvent event) {
      if (event.getStage() == Stage.AFTER_CUTOUT_BLOCKS) {
         label27:
         for (LockRegistryEntry data : RENDERED_ENTRIES) {
            BlockState state = level.getBlockState(data.getPos().pos());

            for (Entry<Class<? extends Block>, BiConsumer<LockRegistryEntry, RenderLevelStageEvent>> entry : RENDERERS.entrySet()) {
               if (entry.getKey().isInstance(state.getBlock())) {
                  entry.getValue().accept(data, event);
                  continue label27;
               }
            }

            genericRender(data, event);
         }
      }
   }

   private static void genericRender(LockRegistryEntry data, RenderLevelStageEvent event) {
      AABB bounce = data.entry.getLockBoundary();
      Vec3 center = bounce.getCenter();
      PoseStack matrices = event.getPoseStack();
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      Vec3 cp = event.getCamera().getPosition();
      Vec3 p = new Vec3(center.x, bounce.maxY + 0.35, center.z);
      p = p.subtract(cp);
      float delta = ((float)event.getRenderTick() + event.getPartialTick()) * 5.0F;
      matrices.pushPose();
      Vec3 off = data.entry.getOffset();
      if (off != null) {
         matrices.translate(off.x, off.y, off.z);
      }

      matrices.translate(p.x, p.y + (double)(Mth.sin((float)Math.toRadians((double)delta)) / 10.0F), p.z);
      matrices.scale(0.5F, 0.5F, 0.5F);
      matrices.mulPose(Axis.YP.rotationDegrees(delta / 2.5F));
      renderLock(itemRenderer, data, matrices);
      matrices.popPose();
   }

   private static void doorRender(LockRegistryEntry data, RenderLevelStageEvent event) {
      Level level = event.getCamera().getEntity().level();
      if (!(Boolean)level.getBlockState(data.getPos().pos()).getValue(DoorBlock.OPEN)) {
         AABB bounce = data.entry.getLockBoundary();
         Vec3 center = bounce.getCenter();
         Direction direction = (Direction)level.getBlockState(data.getPos().pos()).getValue(DoorBlock.FACING);
         net.minecraft.core.Direction.Axis axis = direction.getAxis();
         PoseStack poseStack = event.getPoseStack();
         ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
         Vec3 cp = event.getCamera().getPosition();
         Vec3 p = new Vec3(center.x, center.y, center.z);
         p = p.subtract(cp);
         poseStack.pushPose();
         poseStack.translate(p.x, p.y, p.z);
         poseStack.scale(0.5F, 0.5F, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(axis == net.minecraft.core.Direction.Axis.X ? 90.0F : 0.0F));
         poseStack.translate(0.0, 0.0, 0.6);
         if (direction == Direction.SOUTH || direction == Direction.EAST) {
            event.getPoseStack().translate(0.0, 0.0, -1.625);
         }

         renderLock(itemRenderer, data, event.getPoseStack());
         poseStack.translate(0.0, 0.0, 0.425);
         renderLock(itemRenderer, data, event.getPoseStack());
         poseStack.popPose();
      }
   }

   private static void safeRender(LockRegistryEntry data, RenderLevelStageEvent event) {
      Level level = event.getCamera().getEntity().level();
      AABB bounce = data.entry.getLockBoundary();
      Vec3 center = bounce.getCenter();
      if (level.getBlockState(data.getPos().pos()).hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
         Direction direction = (Direction)level.getBlockState(data.getPos().pos()).getValue(BlockStateProperties.HORIZONTAL_FACING);
         net.minecraft.core.Direction.Axis axis = direction.getAxis();
         PoseStack poseStack = event.getPoseStack();
         ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
         Vec3 cp = event.getCamera().getPosition();
         Vec3 p = new Vec3(center.x, center.y, center.z);
         p = p.subtract(cp);
         poseStack.pushPose();
         poseStack.translate(p.x, p.y, p.z);
         poseStack.scale(0.5F, 0.5F, 0.5F);
         poseStack.mulPose(Axis.YP.rotationDegrees(axis == net.minecraft.core.Direction.Axis.X ? 90.0F : 0.0F));
         if (direction == Direction.SOUTH || direction == Direction.EAST) {
            event.getPoseStack().translate(0.0, 0.0, -1.625);
         }

         poseStack.translate(0.0, 0.0, 0.8);
         renderLock(itemRenderer, data, event.getPoseStack());
         poseStack.popPose();
      }
   }

   private static void renderLock(ItemRenderer renderer, LockRegistryEntry data, PoseStack poseStack) {
      renderer.renderStatic(
         data.entry.getComplexity().display.get().getDefaultInstance(),
         ItemDisplayContext.GUI,
         15728880,
         OverlayTexture.NO_OVERLAY,
         poseStack,
         Minecraft.getInstance().renderBuffers().bufferSource(),
         Minecraft.getInstance().level,
         0
      );
   }
}
