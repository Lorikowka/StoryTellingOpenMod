package com.storyteam.storytelling.system._core.st_cutscene;

import com.storyteam.storytelling.util.world.Location;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class STCutsceneData implements IAutoNBTSerializable {
   @NBTSerializable
   public int time;
   public List<Location> locations;
   @NBTSerializable
   public SplineType splineType;
   @NBTSerializable
   public InterpolationType interpolationType;
   @NBTSerializable
   public InterpolationType lookInterpolationType;
   @NBTSerializable
   public boolean cameraOut = true;

   public STCutsceneData(
      int time, List<Location> locations, SplineType splineType, InterpolationType interpolationType, InterpolationType lookInterpolationType
   ) {
      this.time = time;
      this.locations = new ArrayList<>();

      for (Location l : locations) {
         float yaw = Float.NaN;
         float pitch = Float.NaN;
         if (l.hasRotation()) {
            yaw = ClientsideCutsceneHandler.transformYaw(l.getYaw());
            pitch = l.getPitch();
         }

         this.locations.add(new Location(l.getDimension(), l.x, l.y, l.z, yaw, pitch));
      }

      this.splineType = splineType;
      this.interpolationType = interpolationType;
      this.lookInterpolationType = lookInterpolationType;
   }

   public STCutsceneData(Location location, boolean cameraOut) {
      this.time = 1;
      this.locations = List.of(location);
      this.splineType = SplineType.LINEAR;
      this.interpolationType = InterpolationType.LINEAR;
      this.lookInterpolationType = InterpolationType.LINEAR;
      this.cameraOut = cameraOut;
   }

   public STCutsceneData(STCutsceneData data) {
      this.time = data.time;
      this.locations = new ArrayList<>(data.locations);
      this.splineType = data.splineType;
      this.interpolationType = data.interpolationType;
      this.lookInterpolationType = data.lookInterpolationType;
      this.cameraOut = data.cameraOut;
   }

   public STCutsceneData addLocation(Location l) {
      float yaw = Float.NaN;
      float pitch = Float.NaN;
      if (l.hasRotation()) {
         yaw = ClientsideCutsceneHandler.transformYaw(l.getYaw());
         pitch = l.getPitch();
      }

      this.locations.add(new Location(l.getDimension(), l.x, l.y, l.z, yaw, pitch));
      return this;
   }

   public STCutsceneData() {
      this.locations = new ArrayList<>();
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = IAutoNBTSerializable.super.serializeNBT();
      tag.putInt("locationsSize", this.locations.size());
      int i = 0;

      for (Location l : this.locations) {
         CompoundTag loc = l.serialize();
         tag.put("location_" + i++, loc);
      }

      return tag;
   }

   public void deserializeNBT(CompoundTag tag) {
      IAutoNBTSerializable.super.deserializeNBT(tag);
      int size = tag.getInt("locationsSize");
      List<Location> ls = new ArrayList<>();

      for (int i = 0; i < size; i++) {
         CompoundTag t = tag.getCompound("location_" + i);
         Location location = Location.deserialize(t);
         ls.add(location);
      }

      this.locations = ls;
   }
}
