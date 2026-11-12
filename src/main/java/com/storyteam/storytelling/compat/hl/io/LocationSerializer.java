package com.storyteam.storytelling.compat.hl.io;

import com.storyteam.storytelling.util.world.Location;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;
import org.zeith.hammerlib.util.mcf.Resources;

@NBTSerializer({Location.class})
public class LocationSerializer implements INBTSerializer<Location> {
   public void serialize(CompoundTag nbt, String key, Location loc) {
      if (loc != null) {
         CompoundTag sub = new CompoundTag();
         sub.putDouble("x", loc.x);
         sub.putDouble("y", loc.y);
         sub.putDouble("z", loc.z);
         if (loc.hasRotation()) {
            sub.putFloat("yaw", loc.getYaw());
            sub.putFloat("pitch", loc.getPitch());
         }

         if (loc.hasDimension()) {
            sub.putString("dim", loc.getDimension().location().toString());
         }

         nbt.put(key, sub);
      }
   }

   public Location deserialize(CompoundTag nbt, String key) {
      if (nbt.contains(key, 10)) {
         CompoundTag sub = nbt.getCompound(key);
         Vec3 pos = new Vec3(sub.getDouble("x"), sub.getDouble("y"), sub.getDouble("z"));
         float yaw = sub.contains("yaw", 5) ? sub.getFloat("yaw") : Float.NaN;
         float pitch = sub.contains("pitch", 5) ? sub.getFloat("pitch") : Float.NaN;
         ResourceKey<Level> dim = sub.contains("dim", 8) ? ResourceKey.create(Registries.DIMENSION, Resources.location(sub.getString("dim"))) : null;
         return new Location(dim, pos.x, pos.y, pos.z, yaw, pitch);
      } else {
         return null;
      }
   }
}
