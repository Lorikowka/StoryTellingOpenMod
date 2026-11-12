package com.storyteam.storytelling.system._core.structio.structure;

import com.storyteam.storytelling.system._core.structio.BlockPalette;
import com.storyteam.storytelling.system._core.structio.structure.v3.AttachmentRegistry;
import com.storyteam.storytelling.system._core.structio.structure.v3.IAttachmentProcessor;
import com.storyteam.storytelling.system._core.structio.structure.v3.StructureAttachments;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StructureST {
   public static final int LATEST_VERSION = 200;
   protected final StructureMetadata metadata = new StructureMetadata();
   protected final BlockPalette palette = new BlockPalette();
   protected final StructureSection data = new StructureSection();
   protected final StructureAttachments attachments = new StructureAttachments();

   public void prepare(ServerLevel level, BlockPos min, BlockPos max) {
      this.metadata.prepare(level.getServer());
      this.data.prepare(level.dimension(), min, max);
   }

   public void capture(MinecraftServer server, Predicate<BlockPos> exportPosition) {
      this.sections().forEach(s -> s.capture(this.palette, server, exportPosition));
      ServerLevel level = server.getLevel(this.data.dimension);

      for (Entry<String, IAttachmentProcessor> e : AttachmentRegistry.getRegistry().entrySet()) {
         e.getValue().gather(level, this, this.attachments.getOrCreate(e.getKey()));
      }
   }

   public void place(MinecraftServer server, String name) {
      AtomicInteger section = new AtomicInteger();
      ServerLevel level = server.getLevel(this.data.dimension);
      Set<Entry<String, IAttachmentProcessor>> reg = AttachmentRegistry.getRegistry().entrySet();

      for (Entry<String, IAttachmentProcessor> e : reg) {
         e.getValue().prePlace(level, this, this.attachments.getOrCreate(e.getKey()));
      }

      this.sections().forEach(s -> s.place(this.palette, server, name + "[" + section.getAndIncrement() + "]"));

      for (Entry<String, IAttachmentProcessor> e : reg) {
         e.getValue().place(level, this, this.attachments.getOrCreate(e.getKey()));
      }
   }

   public Stream<StructureSection> sections() {
      return Stream.of(this.data);
   }

   public ServerLevel getLevel(MinecraftServer server) {
      return server.getLevel(this.data.dimension);
   }

   public AABB boundary() {
      return this.data.boundary();
   }

   public VoxelShape computeShape(BlockPos min) {
      return this.data.computeShape(this.palette, min);
   }

   public BlockPos getCenterPos() {
      return BlockPos.containing(this.boundary().getCenter());
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.put("metadata", this.metadata.serializeNBT());
      tag.put("palette", this.palette.serializeNBT());
      tag.put("data", this.data.serializeNBT());
      tag.put("attachments", this.attachments.serializeNBT());
      return tag;
   }

   public void deserializeNBT(RegistryAccess access, CompoundTag nbt) {
      this.metadata.deserializeNBT(nbt.getCompound("metadata"));
      this.palette.deserializeNBT(access, nbt.getList("palette", 10));
      this.data.deserializeNBT(nbt.getCompound("data"));
      this.attachments.deserializeNBT(nbt.getCompound("attachments"));
   }

   @Generated
   public StructureMetadata getMetadata() {
      return this.metadata;
   }

   @Generated
   public BlockPalette getPalette() {
      return this.palette;
   }

   @Generated
   public StructureSection getData() {
      return this.data;
   }

   @Generated
   public StructureAttachments getAttachments() {
      return this.attachments;
   }
}
