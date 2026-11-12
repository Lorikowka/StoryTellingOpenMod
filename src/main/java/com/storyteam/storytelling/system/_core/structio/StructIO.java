package com.storyteam.storytelling.system._core.structio;

import com.storyteam.storytelling.events.listeners.ScriptIO;
import com.storyteam.storytelling.system._core.structio.structure.StructureST;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.consumers.Consumer2;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

public class StructIO {
   public static Path getStructurePath(String name) {
      try {
         Path path = ScriptIO.STRUCTURES_PATH.resolve(name + ".dat");
         Files.createDirectories(path.getParent());
         return path;
      } catch (Throwable var2) {
         if (var2 instanceof RuntimeException runtime) throw runtime;
         if (var2 instanceof Error error) throw error;
         throw new IllegalStateException("Operation failed", var2);
      }
   }

   @Nullable
   public static StructureST readStructure(RegistryAccess access, String name) {
      try {
         Path path = getStructurePath(name);
         if (!Files.isRegularFile(path)) {
            return null;
         } else {
            CompoundTag tag = NbtIo.readCompressed(path.toFile());
            StructureST st = new StructureST();
            st.deserializeNBT(access, tag);
            return st;
         }
      } catch (IOException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static void exportBuilding(ServerLevel level, BlockPos from, BlockPos to, String name) {
      exportBuilding(level, from, to, name, c -> {
      });
   }

   public static void exportBuilding(ServerLevel level, BlockPos from, BlockPos to, String name, Consumer<StructureST> preps) {
      exportBuilding(level, from, to, name, preps, p -> true);
   }

   public static void exportBuilding(
      ServerLevel level, BlockPos from, BlockPos to, String name, Consumer<StructureST> preps, Predicate<BlockPos> exportPosition
   ) {
      StructureST struct = new StructureST();
      struct.prepare(level, from, to);
      struct.capture(level.getServer(), exportPosition);
      preps.accept(struct);

      try {
         CompoundTag tag = struct.serializeNBT();
         NbtIo.writeCompressed(tag, getStructurePath(name).toFile());
      } catch (IOException var8) {
         var8.printStackTrace();
      }
   }

   public static void reexportBuilding(ServerLevel level, Stream<Tuple2<BlockPos, BlockPos>> bounds, String name, int newRevision) {
      Tuple2<BlockPos, BlockPos> b1 = bounds.findFirst().orElseThrow();
      exportBuilding(level, (BlockPos)b1.a(), (BlockPos)b1.b(), name, s -> s.getMetadata().revision = newRevision);
   }

   public static boolean importBuilding(MinecraftServer server, String name) {
      StructureST str = readStructure(server.registryAccess(), name);
      if (str != null) {
         try {
            str.place(server, name);
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

         return true;
      } else {
         return false;
      }
   }

   public static CompletableFuture<Tuple2<AABB, ServerLevel>> importBuilding(MinecraftServer server, Consumer2<AABB, ServerLevel> beforeImport, String name) {
      StructureST str = readStructure(server.registryAccess(), name);
      if (str != null) {
         beforeImport.accept(str.boundary(), str.getLevel(server));
         return server.submit(() -> str.place(server, name)).thenApply(__ -> Tuples.immutable(str.boundary(), str.getLevel(server)));
      } else {
         return CompletableFuture.completedFuture(null);
      }
   }

   public static boolean structureExists(String id) {
      File structFile = getStructurePath(id).toFile();
      return structFile.isFile();
   }
}
