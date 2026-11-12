package com.storyteam.storytelling.system._core.structio.structure;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.api.itf.IStoryEntity;
import com.storyteam.storytelling.system._core.structio.BlockPalette;
import com.storyteam.storytelling.system.navmesh.Navmesh;
import com.storyteam.storytelling.system.navmesh.NavmeshProvider;
import com.storyteam.storytelling.util.ServerScheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.tiles.TileSyncable;
import org.zeith.hammerlib.util.java.consumers.Consumer2;
import org.zeith.hammerlib.util.mcf.Resources;

public class StructureSection implements IAutoNBTSerializable {
   @Generated
   private static final Logger log = LoggerFactory.getLogger(StructureSection.class);
   private static final Map<String, String> ENTITY_SUBSTITUTES = (Map<String, String>)Util.make(
      new HashMap(), m -> m.put("quark:glass_frame", "minecraft:item_frame")
   );
   private static final ResourceLocation FALLBACK_ENTITY_TYPE = Resources.location("pig");
   private static final EntityTypeTest<Entity, ?> NON_PLAYER_ENTITY = new EntityTypeTest<Entity, Entity>() {
      @Nullable
      public Entity tryCast(@NotNull Entity pEntity) {
         return pEntity instanceof Player ? null : pEntity;
      }

      @NotNull
      public Class<? extends Entity> getBaseClass() {
         return Entity.class;
      }
   };
   @NBTSerializable
   public BlockPos min;
   @NBTSerializable
   public BlockPos max;
   @NBTSerializable
   public int[] blocks;
   @NBTSerializable
   public byte[] navmesh;
   @NBTSerializable
   public final StructureTiles tiles = new StructureTiles();
   @NBTSerializable
   public final StructureTagList entities = new StructureTagList();
   protected ResourceKey<Level> dimension;

   public void prepare(ResourceKey<Level> dimension, BlockPos min, BlockPos max) {
      this.dimension = dimension;
      this.min = new BlockPos(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
      this.max = new BlockPos(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));
      int xSize = this.max.getX() - this.min.getX() + 1;
      int ySize = this.max.getY() - this.min.getY() + 1;
      int zSize = this.max.getZ() - this.min.getZ() + 1;
      this.blocks = new int[xSize * ySize * zSize];
      this.navmesh = new byte[(int)Math.ceil((double)((float)(xSize * ySize * zSize) / 8.0F))];
   }

   public void forEachPosNamed(final String toString, final Consumer2<BlockPos, Integer> visitor) {
      this.forEachPos(new Consumer2<BlockPos, Integer>() {
         public void accept(BlockPos pos, Integer integer) {
            visitor.accept(pos, integer);
         }

         @Override
         public String toString() {
            return toString;
         }
      });
   }

   public void forEachPos(Consumer2<BlockPos, Integer> visitor) {
      int xSize = this.max.getX() - this.min.getX() + 1;
      int ySize = this.max.getY() - this.min.getY() + 1;
      int zSize = this.max.getZ() - this.min.getZ() + 1;
      int total = xSize * ySize * zSize;
      long lastUpd = System.currentTimeMillis();
      int i = -1;
      int j = 0;

      for (int x = 0; x < xSize; x++) {
         for (int y = 0; y < ySize; y++) {
            for (int z = 0; z < zSize; z++) {
               i++;
               BlockPos pos = this.min.offset(x, y, z);
               visitor.accept(pos, i);
               long now;
               if (++j >= 100 && (now = System.currentTimeMillis()) - lastUpd > 500L) {
                  j = 0;
                  lastUpd = now;
                  Storytelling.LOG.info("Waiting on: {} ({}/{} blocks, {}%)", visitor, i, total, (long)i * 100L / (long)total);
               }
            }
         }
      }
   }

   public void capture(BlockPalette palette, MinecraftServer server, Predicate<BlockPos> exportPosition) {
      this.entities.clear();
      this.tiles.clear();
      ServerLevel level = server.getLevel(this.dimension);
      if (level != null) {
         List<? extends Entity> es = level.getEntities(NON_PLAYER_ENTITY, new AABB(this.min, this.max.offset(1, 1, 1)), Entity::isAlive);
         this.entities.prepare(es.size());

         for (Entity e : es) {
            this.entities.add(e);
         }

         Map<ChunkPos, Navmesh> meshes = new HashMap<>();
         Function<ChunkPos, Navmesh> meshObtainer = pos -> (Navmesh)level.getChunk(pos.x, pos.z)
               .getCapability(NavmeshProvider.MESH)
               .orElseThrow(NoSuchElementException::new);
         ChunkPos[] prevPos = new ChunkPos[1];
         Navmesh[] prevNm = new Navmesh[1];
         this.forEachPosNamed("Capture blocks", (pos, i) -> {
            this.blocks[i] = exportPosition.test(pos) ? palette.register(level.getBlockState(pos)) : -1;
            ChunkPos cp = new ChunkPos(pos);
            if (!Objects.equals(prevPos[0], cp)) {
               prevNm[0] = meshes.computeIfAbsent(prevPos[0] = new ChunkPos(pos), meshObtainer);
            }

            boolean canNavigate = prevNm[0].canNavigate(pos);
            if (canNavigate) {
               byte[] var10000 = this.navmesh;
               int var10001 = i / 8;
               var10000[var10001] = (byte)(var10000[var10001] | 1 << i % 8);
            } else {
               byte[] var12 = this.navmesh;
               int var13 = i / 8;
               var12[var13] = (byte)(var12[var13] & ~(1 << i % 8));
            }

            this.tiles.set(level.getBlockEntity(pos));
         });
      }
   }

   public void place(BlockPalette palette, MinecraftServer server, String name) {
      ServerLevel level = server.getLevel(this.dimension);
      List<Entity> entitiesBeforePlacement = level.getEntities(null, this.boundary().inflate(16.0));
      Map<ChunkPos, Navmesh> meshes = new HashMap<>();
      Function<ChunkPos, Navmesh> meshObtainer = pos -> (Navmesh)level.getChunk(pos.x, pos.z)
            .getCapability(NavmeshProvider.MESH)
            .orElseThrow(NoSuchElementException::new);
      ChunkPos[] prevPos = new ChunkPos[1];
      Navmesh[] prevNm = new Navmesh[1];
      this.forEachPosNamed("Place blocks of structure " + name, (pos, i) -> {
         int id = this.blocks[i];
         if (id >= 0) {
            try {
               BlockState state = palette.fromId(id);
               level.setBlockAndUpdate(pos, state);
               if (state.hasBlockEntity()) {
                  CompoundTag te = this.tiles.entries.get(pos);
                  if (te != null) {
                     BlockEntity tile = BlockEntity.loadStatic(pos, state, te);
                     if (tile != null) {
                        level.setBlockEntity(tile);
                        tile.setChanged();
                        if (tile instanceof TileSyncable ts) {
                           ts.sync();
                        } else {
                           Packet<ClientGamePacketListener> pkt = tile.getUpdatePacket();
                           if (pkt == null) {
                              pkt = ClientboundBlockEntityDataPacket.create(tile);
                           }

                           for (ServerPlayer pl : level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)) {
                              pl.connection.send(pkt);
                           }
                        }
                     }
                  }
               }

               ChunkPos cp = new ChunkPos(pos);
               if (!Objects.equals(prevPos[0], cp)) {
                  prevNm[0] = meshes.computeIfAbsent(prevPos[0] = new ChunkPos(pos), meshObtainer);
               }

               int wordIndex = i / 8;
               prevNm[0].setCanNavigate(pos, wordIndex < this.navmesh.length && ((long)this.navmesh[wordIndex] & 1L << i % 8) != 0L);
            } catch (Exception var17) {
               log.error("Failed to place block @ {}", pos, var17);
            }
         }
      });
      ServerScheduler.schedule(2, () -> {
         List<ItemEntity> entitiesAfterPlacement = level.getEntitiesOfClass(ItemEntity.class, this.boundary().inflate(4.0));
         entitiesAfterPlacement.removeAll(entitiesBeforePlacement);

         for (ItemEntity entity : entitiesAfterPlacement) {
            if (!IStoryEntity.of(entity).isStorytellingEntity()) {
               entity.discard();
            }
         }
      });
      this.placeEntities(server);
   }

   public VoxelShape computeShape(BlockPalette palette, BlockPos min) {
      int xSize = this.max.getX() - this.min.getX() + 1;
      int ySize = this.max.getY() - this.min.getY() + 1;
      int zSize = this.max.getZ() - this.min.getZ() + 1;
      List<VoxelShape> shapes = new ArrayList<>(xSize * ySize * zSize);
      int i = -1;

      for (int x = 0; x < xSize; x++) {
         for (int y = 0; y < ySize; y++) {
            for (int z = 0; z < zSize; z++) {
               if (this.blocks[++i] >= 0) {
                  BlockPos pos = min.offset(x, y, z);
                  BlockState state = palette.fromId(this.blocks[i]);
                  if (state.getBlock() != Blocks.AIR) {
                     shapes.add(state.getShape(EmptyBlockGetter.INSTANCE, pos).move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
                  }
               }
            }
         }
      }

      return Shapes.or(Shapes.empty(), shapes.toArray(VoxelShape[]::new));
   }

   public void placeEntities(MinecraftServer server) {
      ServerLevel level = server.getLevel(this.dimension);

      for (int i = 0; i < this.entities.size(); i++) {
         CompoundTag sub = this.entities.get(i);
         ResourceLocation id = Resources.location(sub.getString("id"));
         EntityType<?> type = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(id);
         if (type == EntityType.PIG && !FALLBACK_ENTITY_TYPE.equals(id)) {
            String subStr = ENTITY_SUBSTITUTES.get(id.toString());
            if (subStr != null) {
               type = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(Resources.location(subStr));
            }
         }

         if (type != null) {
            Entity entity = type.create(level);
            if (entity != null) {
               entity.deserializeNBT(sub);
               entity.setUUID(UUID.randomUUID());
               level.addFreshEntity(entity);
               entity.setPos(entity.position());

               for (Entity e : level.getEntities(entity, entity.getBoundingBox())) {
                  if (e.getType() == type && !IStoryEntity.of(e).isStorytellingEntity()) {
                     e.discard();
                  }
               }
            }
         }
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      if (this.dimension != null) {
         nbt.putString("dimension", this.dimension.location().toString());
      }

      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      IAutoNBTSerializable.super.deserializeNBT(nbt);
      this.dimension = nbt.contains("dimension", 8) ? ResourceKey.create(Registries.DIMENSION, Resources.location(nbt.getString("dimension"))) : null;
   }

   public AABB boundary() {
      return new AABB(this.min, this.max);
   }

   @Generated
   public ResourceKey<Level> getDimension() {
      return this.dimension;
   }
}
