package com.storyteam.storytelling.content.caps;

import com.storyteam.storytelling.compat.hl.src.StoryDataSourceType;
import com.storyteam.storytelling.init.CapsST;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.SendPropertiesPacket;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;
import org.zeith.hammerlib.net.properties.IProperty;
import org.zeith.hammerlib.net.properties.PropertyBool;
import org.zeith.hammerlib.net.properties.PropertyDispatcher;
import org.zeith.hammerlib.net.properties.PropertyFloat;
import org.zeith.hammerlib.net.properties.PropertyResourceLocation;
import org.zeith.hammerlib.util.mcf.Resources;

@AutoRegisterCapability
public class StoryData implements IAutoNBTSerializable, IBasePropertyHolder {
   protected final PropertyDispatcher properties;
   public final IProperty<ResourceLocation> texture;
   public final PropertyBool blockBody;
   public final PropertyBool lockHead;
   public final PropertyBool dontRotateOnInteraction;
   public final PropertyBool ignoreNavmesh;
   public final PropertyBool blockAnimRotateWalking;
   public final PropertyBool traversalNavigation;
   public final PropertyBool freeSight;
   public final PropertyFloat alphaFadeable;
   private final Entity entity;

   public StoryData(Entity entity) {
      this.entity = entity;
      this.properties = new PropertyDispatcher(StoryDataSourceType.create(this), () -> {
      });
      this.properties.registerProperty("texture", this.texture = new PropertyResourceLocation());
      this.properties.registerProperty("block_body", this.blockBody = new PropertyBool());
      this.properties.registerProperty("lock_head", this.lockHead = new PropertyBool());
      this.properties.registerProperty("dont_rotate_on_interaction", this.dontRotateOnInteraction = new PropertyBool());
      this.properties.registerProperty("ignore_navmesh", this.ignoreNavmesh = new PropertyBool());
      this.properties.registerProperty("block_anim_rotate_walking", this.blockAnimRotateWalking = new PropertyBool());
      this.properties.registerProperty("alphaFadeable", this.alphaFadeable = new PropertyFloat());
      this.properties.registerProperty("traversal_navigation", this.traversalNavigation = new PropertyBool());
      this.properties.registerProperty("freeSight", this.freeSight = new PropertyBool());
   }

   public CompoundTag serializeNBT() {
      CompoundTag nbt = IAutoNBTSerializable.super.serializeNBT();
      ResourceLocation tx = (ResourceLocation)this.texture.get();
      nbt.putString("Texture", tx != null ? tx.toString() : "");
      nbt.putBoolean("BlockBody", this.blockBody.getBoolean());
      nbt.putBoolean("LockHead", this.lockHead.getBoolean());
      nbt.putBoolean("NoRotateOnInteraction", this.dontRotateOnInteraction.getBoolean());
      nbt.putBoolean("IgnoreNavmesh", this.ignoreNavmesh.getBoolean());
      nbt.putBoolean("BlockAnimRotateWalking", this.blockAnimRotateWalking.getBoolean());
      nbt.putFloat("alphaFadeable", this.alphaFadeable.getFloat());
      nbt.putBoolean("TraversalNavigation", this.traversalNavigation.getBoolean());
      nbt.putBoolean("freeSight", this.freeSight.getBoolean());
      return nbt;
   }

   public void deserializeNBT(CompoundTag nbt) {
      String tx = nbt.getString("Texture");
      if (!tx.isBlank()) {
         this.texture.set(Resources.location(tx));
      } else {
         this.texture.set(null);
      }

      this.blockBody.setBool(nbt.getBoolean("BlockBody"));
      this.lockHead.setBool(nbt.getBoolean("LockHead"));
      this.dontRotateOnInteraction.setBool(nbt.getBoolean("NoRotateOnInteraction"));
      this.ignoreNavmesh.setBool(nbt.getBoolean("IgnoreNavmesh"));
      this.blockAnimRotateWalking.setBool(nbt.getBoolean("BlockAnimRotateWalking"));
      this.alphaFadeable.setFloat(nbt.getFloat("alphaFadeable"));
      this.traversalNavigation.setBool(nbt.getBoolean("TraversalNavigation"));
      this.freeSight.setBool(nbt.getBoolean("freeSight"));
      IAutoNBTSerializable.super.deserializeNBT(nbt);
   }

   public void detectAndSendChanges() {
      if (!this.entity.level().isClientSide()) {
         SendPropertiesPacket pkt = this.properties.detectAndGenerateChanges(true);
         if (pkt != null) {
            Network.sendToTracking(this.entity, pkt);
         }
      }
   }

   @Nullable
   public static StoryData of(Entity entity) {
      return entity != null ? (StoryData)entity.getCapability(CapsST.STORY_DATA).orElse(null) : null;
   }

   public static Optional<StoryData> optionalOf(Entity entity) {
      return entity.getCapability(CapsST.STORY_DATA).resolve();
   }

   public void tick(Level level) {
      this.properties.tick(level);
      this.detectAndSendChanges();
   }

   public void syncProperties() {
      this.detectAndSendChanges();
   }

   public void syncPropertiesNow() {
      this.detectAndSendChanges();
   }

   public PropertyDispatcher getProperties() {
      return this.properties;
   }

   @Generated
   public IProperty<ResourceLocation> getTexture() {
      return this.texture;
   }

   @Generated
   public PropertyBool getBlockBody() {
      return this.blockBody;
   }

   @Generated
   public PropertyBool getLockHead() {
      return this.lockHead;
   }

   @Generated
   public PropertyBool getDontRotateOnInteraction() {
      return this.dontRotateOnInteraction;
   }

   @Generated
   public PropertyBool getIgnoreNavmesh() {
      return this.ignoreNavmesh;
   }

   @Generated
   public PropertyBool getBlockAnimRotateWalking() {
      return this.blockAnimRotateWalking;
   }

   @Generated
   public PropertyBool getTraversalNavigation() {
      return this.traversalNavigation;
   }

   @Generated
   public PropertyBool getFreeSight() {
      return this.freeSight;
   }

   @Generated
   public PropertyFloat getAlphaFadeable() {
      return this.alphaFadeable;
   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }

   public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
      protected final StoryData std;
      protected final LazyOptional<StoryData> data;

      public Provider(Entity entity) {
         this.std = new StoryData(entity);
         this.data = LazyOptional.of(() -> this.std);
      }

      @NotNull
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CapsST.STORY_DATA ? this.data.cast() : LazyOptional.empty();
      }

      public CompoundTag serializeNBT() {
         return this.std.serializeNBT();
      }

      public void deserializeNBT(CompoundTag nbt) {
         this.std.deserializeNBT(nbt);
      }
   }
}
