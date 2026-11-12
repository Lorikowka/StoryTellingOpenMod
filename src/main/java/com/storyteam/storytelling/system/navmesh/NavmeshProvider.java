package com.storyteam.storytelling.system.navmesh;

import com.storyteam.storytelling.St;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NavmeshProvider implements ICapabilitySerializable<CompoundTag> {
   public static final Capability<Navmesh> MESH = CapabilityManager.get(new CapabilityToken<Navmesh>() {
   });
   public final Navmesh mesh;
   private final LazyOptional<Navmesh> lazy;

   public NavmeshProvider(LevelChunk chunk) {
      this.mesh = new Navmesh(chunk);
      this.lazy = LazyOptional.of(() -> this.mesh);
   }

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
      return cap == MESH ? MESH.orEmpty(cap, this.lazy) : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      return this.mesh.serializeNBT();
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.mesh.deserializeNBT(nbt);
   }

   @SubscribeEvent
   public static void addCap(AttachCapabilitiesEvent<LevelChunk> e) {
      e.addCapability(St.id("navmesh"), new NavmeshProvider((LevelChunk)e.getObject()));
   }
}
