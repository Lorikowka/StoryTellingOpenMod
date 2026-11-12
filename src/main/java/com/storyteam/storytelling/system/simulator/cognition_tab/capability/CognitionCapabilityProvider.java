package com.storyteam.storytelling.system.simulator.cognition_tab.capability;

import com.storyteam.storytelling.init.CapsST;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class CognitionCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
   private final LazyOptional<CognitionCapability> optional;
   private final CognitionCapability capInstance;

   public CognitionCapabilityProvider(Player player) {
      this.capInstance = new CognitionCapability(player);
      this.optional = LazyOptional.of(() -> this.capInstance);
   }

   @NotNull
   public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
      return capability == CapsST.COG_CAP ? this.optional.cast() : LazyOptional.empty();
   }

   public CompoundTag serializeNBT() {
      return this.capInstance.serializeNBT();
   }

   public void deserializeNBT(CompoundTag compoundTag) {
      this.capInstance.deserializeNBT(compoundTag);
   }
}
