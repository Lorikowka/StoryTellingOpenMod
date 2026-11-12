package com.storyteam.storytelling.net;

import com.storyteam.storytelling.client.CameraHandler;
import com.storyteam.storytelling.util.world.Location;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

@MainThreaded
public class StartCameraPathPacket implements IPacket {
   private Location start;
   private Location end;
   private boolean slowCameraStart;
   private boolean slowCameraEnd;
   private float speed;

   public StartCameraPathPacket(Location start, Location end, boolean slowCameraStart, boolean slowCameraEnd, float speed) {
      this.start = start;
      this.end = end;
      this.slowCameraStart = slowCameraStart;
      this.slowCameraEnd = slowCameraEnd;
      this.speed = speed;
   }

   public Location writeLocation(@NotNull Location loc, @NotNull FriendlyByteBuf buf) {
      buf.writeBoolean(loc.hasDimension());
      if (loc.hasDimension()) {
         buf.writeResourceKey(loc.getDimension());
      }

      buf.writeDouble(loc.x());
      buf.writeDouble(loc.y());
      buf.writeDouble(loc.z());
      buf.writeBoolean(loc.hasRotation());
      if (loc.hasRotation()) {
         buf.writeFloat(loc.getYaw());
      }

      if (loc.hasRotation()) {
         buf.writeFloat(loc.getPitch());
      }

      return loc;
   }

   public Location readLocation(@NotNull FriendlyByteBuf buf) {
      ResourceKey<Level> dimension = buf.readBoolean() ? buf.readResourceKey(Registries.DIMENSION) : Level.OVERWORLD;
      double x = buf.readDouble();
      double y = buf.readDouble();
      double z = buf.readDouble();
      boolean hasRotation = buf.readBoolean();
      float yaw = hasRotation ? buf.readFloat() : 0.0F;
      float pitch = hasRotation ? buf.readFloat() : 0.0F;
      return new Location(dimension, x, y, z, yaw, pitch);
   }

   public void write(FriendlyByteBuf buf) {
      IPacket.super.write(buf);
      this.writeLocation(this.start, buf);
      this.writeLocation(this.end, buf);
      buf.writeBoolean(this.slowCameraStart);
      buf.writeBoolean(this.slowCameraEnd);
      buf.writeFloat(this.speed);
   }

   public void read(FriendlyByteBuf buf) {
      IPacket.super.read(buf);
      this.start = this.readLocation(buf);
      this.end = this.readLocation(buf);
      this.slowCameraStart = buf.readBoolean();
      this.slowCameraEnd = buf.readBoolean();
      this.speed = buf.readFloat();
   }

   public void clientExecute(PacketContext ctx) {
      IPacket.super.clientExecute(ctx);
      CameraHandler.progress0 = 0.0F;
      CameraHandler.slowCameraStart = this.slowCameraStart;
      CameraHandler.slowCameraEnd = this.slowCameraEnd;
      CameraHandler.speed = this.speed;
      CameraHandler.startPos = this.start;
      CameraHandler.targetPos = this.end;
   }
}
