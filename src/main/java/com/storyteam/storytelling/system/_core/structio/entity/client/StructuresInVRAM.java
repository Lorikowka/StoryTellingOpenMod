package com.storyteam.storytelling.system._core.structio.entity.client;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.system._core.structio.BlockPalette;
import com.storyteam.storytelling.system._core.structio.StructIO;
import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import com.storyteam.storytelling.system._core.structio.structure.StructureSection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

@EventBusSubscriber({Dist.CLIENT})
public class StructuresInVRAM {
   private static final ConcurrentHashMap<String, VoxelShape> COMPUTED_SHAPE_AT_CENTER = new ConcurrentHashMap<>();
   private static final Function<RenderType, BufferBuilder> BUFFER = Util.memoize(f -> new BufferBuilder(2097152));
   private static final List<StructuresInVRAM.QueuedStructure> QUEUED_STRUCTURES = new ArrayList<>();
   private static final Map<String, CompletableFuture<RenderedStructure>> RENDERED_STRUCTURES = new HashMap<>();
   private static boolean markClear = false;

   public static VoxelShape getComputerShape(String structureId) {
      return COMPUTED_SHAPE_AT_CENTER.get(structureId);
   }

   @SubscribeEvent
   public static void clientTick(ClientTickEvent e) {
      if (markClear) {
         for (CompletableFuture<RenderedStructure> value : RENDERED_STRUCTURES.values()) {
            if (value.isDone()) {
               RenderedStructure rs = value.join();
               rs.close();
            }
         }

         RENDERED_STRUCTURES.clear();
         QUEUED_STRUCTURES.clear();
         COMPUTED_SHAPE_AT_CENTER.clear();
         markClear = false;
      }

      Iterator<StructuresInVRAM.QueuedStructure> it = QUEUED_STRUCTURES.iterator();

      while (it.hasNext()) {
         StructuresInVRAM.QueuedStructure qs = it.next();
         if (qs.read.isDone()) {
            uploadStructure(qs.id, qs.read.join());
            it.remove();
         }
      }
   }

   public static void queueUpload(RegistryAccess access, String id) {
      QUEUED_STRUCTURES.add(new StructuresInVRAM.QueuedStructure(id, CompletableFuture.supplyAsync(() -> StructIO.readStructure(access, id))));
   }

   public static void uploadStructure(String id, StructureST structure) {
      CompletableFuture<RenderedStructure> future = RENDERED_STRUCTURES.computeIfAbsent(id, id_ -> new CompletableFuture<>());
      if (!future.isDone() && !future.isCancelled()) {
         Stopwatch timer = Stopwatch.createStarted();
         Map<BlockPos, BlockState> blocks = new HashMap<>();
         BlockPalette pal = structure.getPalette();
         AABB bounds = structure.boundary();
         BlockPos center = structure.getCenterPos();
         Iterator<StructureSection> it = structure.sections().iterator();

         while (it.hasNext()) {
            StructureSection ss = it.next();
            ss.forEachPos((pos, idx) -> {
               pos = pos.offset(-center.getX(), -((int)structure.boundary().minY), -center.getZ());
               BlockState state = pal.fromId(ss.blocks[idx]);
               if (state.getBlock() != Blocks.AIR) {
                  blocks.put(pos, state);
               }
            });
         }

         CompletableFuture<VoxelShape> shapeFuture = CompletableFuture.supplyAsync(
            () -> structure.computeShape(
                  BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ).offset(-center.getX(), -((int)structure.boundary().minY), -center.getZ())
               )
         );
         shapeFuture.thenAccept(shape -> COMPUTED_SHAPE_AT_CENTER.put(id, shape));
         Minecraft MC = Minecraft.getInstance();
         BlockStateSimpleRenderer renderer = new BlockStateSimpleRenderer(MC);
         RenderSystem.recordRenderCall(() -> {
            Map<RenderType, BufferBuilder> cache = new HashMap<>();
            MultiBufferSource mbs = type -> (VertexConsumer)cache.computeIfAbsent(type, renderType -> {
                  BufferBuilder b = BUFFER.apply(renderType);
                  b.begin(renderType.mode(), renderType.format());
                  return b;
               });
            BlockGetterByMap level = new BlockGetterByMap(blocks);

            for (Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
               BlockState state = entry.getValue();
               BlockPos pos = entry.getKey();
               PoseStack pose = new PoseStack();
               pose.translate((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
               renderer.renderBatched(state, pos, level, pose, mbs, true, RandomSource.create(Mth.getSeed(pos)), ModelData.EMPTY);
            }

            List<Tuple3<RenderType, VertexBuffer, CompletableFuture<?>>> allUploadTasks = cache.entrySet().stream().<Tuple3<RenderType, VertexBuffer, CompletableFuture<?>>>map(entryx -> {
               RenderedBuffer res = ((BufferBuilder)entryx.getValue()).endOrDiscardIfEmpty();
               if (res == null) {
                  return Tuples.immutable((RenderType)entryx.getKey(), null, CompletableFuture.completedFuture(null));
               } else {
                  VertexBuffer buffers = new VertexBuffer(Usage.STATIC);
                  return Tuples.immutable((RenderType)entryx.getKey(), buffers, MC.levelRenderer.getChunkRenderDispatcher().uploadChunkLayer(res, buffers));
               }
            }).toList();
            CompletableFuture.allOf(allUploadTasks.stream().map(Tuple3::c).toArray(CompletableFuture[]::new)).thenRun(() -> {
               future.complete(new RenderedStructure(allUploadTasks.stream().collect(Collectors.toMap(Tuple3::a, Tuple3::b))));
               Storytelling.LOG.info("Uploaded structure {} to GPU took {}.", id, timer.stop());
            });
         });
      }
   }

   public static void reset() {
      markClear = true;
   }

   public static CompletableFuture<RenderedStructure> getOrRender(RegistryAccess access, String id) {
      CompletableFuture<RenderedStructure> rs = RENDERED_STRUCTURES.get(id);
      if (rs != null) {
         return rs;
      } else {
         CompletableFuture<RenderedStructure> cf = new CompletableFuture<>();
         RENDERED_STRUCTURES.put(id, cf);
         queueUpload(access, id);
         return cf;
      }
   }

   private static record QueuedStructure(String id, CompletableFuture<StructureST> read) {
   }
}
