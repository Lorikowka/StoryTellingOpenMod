package com.storyteam.storytelling.system._core.structio;

import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import com.storyteam.storytelling.system._core.structio.structure.StructureSection;
import com.storyteam.storytelling.util.ServerScheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.consumers.Consumer2;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

public class AsyncStructIO {
   public static List<AsyncStructIO.Task> runningTasks = new ArrayList<>();

   @Nullable
   public static Tuple3<AABB, ServerLevel, CompletableFuture<Void>> importBuilding(MinecraftServer server, String name) {
      StructureST data = StructIO.readStructure(server.registryAccess(), name);
      if (data != null) {
         AsyncStructIO.Task task = new AsyncStructIO.Task(name, 0);
         runningTasks.add(task);
         CompletableFuture<Void> future = importRange(server, data, task);
         AABB aabb = data.boundary();
         ServerLevel world = data.getLevel(server);
         return Tuples.immutable(aabb, world, future.thenApply(__ -> {
            runningTasks.remove(task);
            return null;
         }));
      } else {
         return null;
      }
   }

   public static CompletableFuture<Tuple2<AABB, ServerLevel>> importBuilding(MinecraftServer server, Consumer2<AABB, ServerLevel> beforeImport, String name) {
      StructureST str = StructIO.readStructure(server.registryAccess(), name);
      if (str != null) {
         AABB aabb = str.boundary();
         ServerLevel world = str.getLevel(server);
         beforeImport.accept(aabb, world);
         AsyncStructIO.Task task = new AsyncStructIO.Task(name, 0);
         runningTasks.add(task);
         CompletableFuture<Void> future = importRange(server, str, task);
         return future.thenApply(__ -> {
            runningTasks.remove(task);
            return Tuples.immutable(aabb, world);
         });
      } else {
         return CompletableFuture.completedFuture(null);
      }
   }

   public static CompletableFuture<Void> importRange(MinecraftServer server, StructureST data, AsyncStructIO.Task task) {
      CompletableFuture<Void> future = new CompletableFuture<>();
      ServerLevel world = data.getLevel(server);
      if (world == null) {
         future.complete(null);
         return future;
      } else {
         int blockCount = data.sections().mapToInt(s -> s.blocks.length).sum();
         HashMap<Integer, Map<BlockPos, Tuple2<BlockState, CompoundTag>>> blockUpdates = new HashMap<>();
         BlockPalette palette = data.getPalette();
         Iterator<StructureSection> sections = data.sections().iterator();

         while (sections.hasNext()) {
            StructureSection sec = sections.next();
            int[] blocks = sec.blocks;
            int xSize = sec.max.getX() - sec.min.getX() + 1;
            int ySize = sec.max.getY() - sec.min.getY() + 1;
            int zSize = sec.max.getZ() - sec.min.getZ() + 1;
            int i = -1;

            for (int x = 0; x < xSize; x++) {
               for (int y = 0; y < ySize; y++) {
                  for (int z = 0; z < zSize; z++) {
                     i++;
                     BlockPos pos = sec.min.offset(x, y, z);
                     blockUpdates.putIfAbsent(i / 50000, new HashMap<>());
                     blockUpdates.get(i / 50000).put(pos, Tuples.immutable(palette.fromId(blocks[i]), sec.tiles.entries.get(pos)));
                  }
               }
            }
         }

         for (Entry<Integer, Map<BlockPos, Tuple2<BlockState, CompoundTag>>> entry : blockUpdates.entrySet()) {
            ServerScheduler.schedule(entry.getKey() * 5, () -> {
               for (Entry<BlockPos, Tuple2<BlockState, CompoundTag>> tag : entry.getValue().entrySet()) {
                  BlockPos posx = tag.getKey();
                  BlockState state = (BlockState)tag.getValue().a();
                  world.setBlockAndUpdate(posx, state);
                  if (state.hasBlockEntity()) {
                     CompoundTag te = (CompoundTag)tag.getValue().b();
                     if (te != null) {
                        BlockEntity tile = BlockEntity.loadStatic(posx, state, te);
                        if (tile != null) {
                           world.setBlockEntity(tile);
                        }
                     }
                  }
               }

               task.setPercentage(entry.getKey() * 5000000 / blockCount);
            });
         }

         int delay = blockUpdates.size() * 5 + 5;
         sections = data.sections().iterator();

         while (sections.hasNext()) {
            StructureSection sec = sections.next();
            ServerScheduler.schedule(delay, () -> {
               sec.placeEntities(server);
               future.complete(null);
            });
         }

         return future;
      }
   }

   public static final class Task {
      private final String name;
      private int percentage;

      public Task(String name, int percentage) {
         this.name = name;
         this.percentage = percentage;
      }

      public String name() {
         return this.name;
      }

      public int percentage() {
         return this.percentage;
      }

      public void setPercentage(int percentage) {
         this.percentage = percentage;
      }

      @Override
      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (obj != null && obj.getClass() == this.getClass()) {
            AsyncStructIO.Task that = (AsyncStructIO.Task)obj;
            return Objects.equals(this.name, that.name) && this.percentage == that.percentage;
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Objects.hash(this.name, this.percentage);
      }

      @Override
      public String toString() {
         return "Task[name=" + this.name + ", percentage=" + this.percentage + "]";
      }
   }
}
