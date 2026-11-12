package com.storyteam.storytelling.util.world;

import com.google.common.base.Preconditions;
import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.util.PositionParser;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.util.mcf.Resources;

public class Location extends Vec3 implements IAutoNBTSerializable {
   private final ResourceKey<Level> dimension;
   @NBTSerializable
   private final float yaw;
   @NBTSerializable
   private final float pitch;
   public static final Location ZERO = new Location("/execute in storytelling:desert run tp @s 0.00 0.00 0.00 0.00 0.00");

   public Location(String pos) {
      this(PositionParser.parseLocationWithRotationOrException(pos, new RuntimeException()));
   }

   public Location(Vec3 v) {
      this(v.x, v.y, v.z);
   }

   public Location(Position position) {
      this(position.x(), position.y(), position.z());
   }

   public Location(Entity entity) {
      this(entity.level().dimension(), entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
   }

   public Location(Entity entity, float partialTicks) {
      this(entity.level().dimension(), entity.getPosition(partialTicks), entity.getYRot(), entity.getXRot());
   }

   public Location(Location location) {
      this(location.getDimension(), location.x, location.y, location.z, location.getYaw(), location.getPitch());
   }

   public Location(BlockPos position) {
      this((double)position.getX(), (double)position.getY(), (double)position.getZ());
   }

   public Location(@Nullable ResourceKey<Level> dimension, BlockPos position) {
      this(dimension, (double)position.getX(), (double)position.getY(), (double)position.getZ());
   }

   public Location(@Nullable ResourceKey<Level> dimension, Vec3 position) {
      this(dimension, position.x, position.y, position.z);
   }

   public Location(@Nullable ResourceKey<Level> dimension, Vec3 position, float yaw, float pitch) {
      this(dimension, position.x, position.y, position.z, yaw, pitch);
   }

   public Location(Level dimension, Vec3 position) {
      this(dimension.dimension(), position.x, position.y, position.z);
   }

   public Location(double x, double y, double z) {
      this(Level.OVERWORLD, x, y, z, Float.NaN, Float.NaN);
   }

   public Location(@Nullable ResourceKey<Level> dimension, double x, double y, double z) {
      this(dimension, x, y, z, Float.NaN, Float.NaN);
   }

   public Location(@Nullable ResourceKey<Level> dimension, double x, double y, double z, float yaw, float pitch) {
      super(x, y, z);
      this.dimension = dimension;
      if (!Float.isFinite(yaw) || !Float.isFinite(pitch)) {
         yaw = 0.0F;
         pitch = 0.0F;
      }

      this.yaw = yaw;
      this.pitch = pitch;
   }

   public boolean hasDimension() {
      return this.dimension != null;
   }

   public boolean hasRotation() {
      return Float.isFinite(this.yaw) && Float.isFinite(this.pitch);
   }

   public ResourceKey<Level> getDimension(ResourceKey<Level> fallback) {
      return this.hasDimension() ? this.dimension : fallback;
   }

   @Nullable
   public ResourceKey<Level> getDimension() {
      return this.dimension;
   }

   public float getYaw() {
      Preconditions.checkState(this.hasRotation(), "This location doesn't have rotations.");
      return this.yaw;
   }

   public float getPitch() {
      Preconditions.checkState(this.hasRotation(), "This location doesn't have rotations.");
      return this.pitch;
   }

   public Location lerpPosAndRot(Location towards, float progress) {
      float oPitch = Mth.clamp(this.getPitch(), -90.0F, 90.0F) % 360.0F;
      float nPitch = Mth.clamp(towards.getPitch(), -90.0F, 90.0F) % 360.0F;
      return new Location(
         this.dimension,
         Mth.lerp((double)progress, this.x, towards.x),
         Mth.lerp((double)progress, this.y, towards.y),
         Mth.lerp((double)progress, this.z, towards.z),
         Mth.lerp(progress, this.yaw, towards.yaw),
         Mth.lerp(progress, oPitch, nPitch)
      );
   }

   public BlockPos toBlockPos() {
      return BlockPos.containing(this.x, this.y, this.z);
   }

   public String toCommandString() {
      return String.format(
            "/execute in %s run tp @s %f %f %f %f %f",
            this.dimension == null ? "minecraft:overworld" : this.dimension.location(),
            Math.floor(this.x * 100.0) / 100.0,
            Math.floor(this.y * 100.0) / 100.0,
            Math.floor(this.z * 100.0) / 100.0,
            Math.floor((double)(this.yaw * 100.0F)) / 100.0,
            Math.floor((double)(this.pitch * 100.0F)) / 100.0
         )
         .replace(',', '.');
   }

   @Nonnull
   public String toString() {
      return "Location{"
         + (this.hasDimension() ? "dimension=" + this.dimension + ", " : "")
         + "x="
         + this.x
         + ", y="
         + this.y
         + ", z="
         + this.z
         + (this.hasRotation() ? ", yaw=" + this.yaw + ", pitch=" + this.pitch : "")
         + "}";
   }

   public void teleport(ServerPlayer player) {
      if (player != null) {
         if (this.hasDimension() && this.getDimension(null).compareTo(player.level().dimension()) != 0) {
            ServerLevel world = player.server.getLevel(this.getDimension(null));
            if (world == null) {
               Storytelling.LOG.warn("Не получилось телепортивать игрока на координату " + this.toCommandString() + " - мир не найден.");
               return;
            }

            if (this.hasRotation()) {
               player.teleportTo(world, this.x, this.y, this.z, this.yaw, this.pitch);
            } else {
               player.teleportTo(world, this.x, this.y, this.z, player.getYRot(), player.getXRot());
            }
         } else if (this.hasRotation()) {
            player.connection.teleport(this.x, this.y, this.z, this.yaw, this.pitch);
         } else {
            player.teleportTo(this.x, this.y, this.z);
         }
      }
   }

   @Nonnull
   public Location add(Vec3 vec) {
      return new Location(this.dimension, this.x + vec.x, this.y + vec.y, this.z + vec.z, this.yaw, this.pitch);
   }

   @Nonnull
   public Location add(double x, double y, double z) {
      return new Location(this.dimension, this.x + x, this.y + y, this.z + z, this.yaw, this.pitch);
   }

   public Location subtract(Vec3 vec) {
      return this.subtract(vec.x, vec.y, vec.z);
   }

   public Location subtract(double x, double y, double z) {
      return this.add(-x, -y, -z);
   }

   public Location multiply(Vec3 vec) {
      return this.multiply(vec.x, vec.y, vec.z);
   }

   public Location multiply(double x, double y, double z) {
      return new Location(this.dimension, this.x * x, this.y * y, this.z * z, this.yaw, this.pitch);
   }

   public Location rotateY(float rot) {
      return new Location(this.dimension, this.x, this.y, this.z, this.yaw + rot, this.pitch);
   }

   public Location rotateZ(float rot) {
      return new Location(this.dimension, this.x, this.y, this.z, this.yaw, this.pitch + rot);
   }

   public CompoundTag serialize() {
      CompoundTag tag = new CompoundTag();
      tag.putDouble("x", this.x);
      tag.putDouble("y", this.y);
      tag.putDouble("z", this.z);
      if (this.hasRotation()) {
         tag.putFloat("yaw", this.getYaw());
         tag.putFloat("pitch", this.getPitch());
      }

      if (this.hasDimension()) {
         ResourceKey<Level> dim = this.getDimension();
         tag.putString("dimension", dim.location().toString());
      }

      return tag;
   }

   public static Location deserialize(CompoundTag tag) {
      double x = tag.getDouble("x");
      double y = tag.getDouble("y");
      double z = tag.getDouble("z");
      float yaw;
      float pitch;
      if (tag.contains("yaw") && tag.contains("pitch")) {
         yaw = tag.getFloat("yaw");
         pitch = tag.getFloat("pitch");
      } else {
         yaw = Float.NaN;
         pitch = Float.NaN;
      }

      ResourceKey<Level> dimension = null;
      if (tag.contains("dimension")) {
         dimension = ResourceKey.create(Registries.DIMENSION, Resources.location(tag.getString("dimension")));
      }

      return new Location(dimension, x, y, z, yaw, pitch);
   }

   public boolean closerThan(Entity player, double distance) {
      return player != null && (!this.hasDimension() || player.level().dimension().equals(this.dimension))
         ? this.closerThan(player.position(), distance)
         : false;
   }

   public static List<Location> parseLocations(String... coord) {
      return $parseLocations(coord).toList();
   }

   public static List<String> expandLocations(String... coord) {
      return $rawLocations(coord).toList();
   }

   public static Location[] parseLocationArray(String... coord) {
      return $parseLocations(coord).toArray(Location[]::new);
   }

   private static Stream<Location> $parseLocations(String... coord) {
      return $rawLocations(coord).map(Location::new);
   }

   private static Stream<String> $rawLocations(String... coord) {
      return Arrays.stream(coord).flatMap(String::lines).filter(s -> !s.isBlank());
   }
}
