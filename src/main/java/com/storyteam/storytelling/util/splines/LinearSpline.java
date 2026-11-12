package com.storyteam.storytelling.util.splines;

import com.storyteam.storytelling.util.world.Location;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LinearSpline extends Spline {
   public LinearSpline(Location... locations) {
      super(locations);
   }

   @Override
   public Location getPoint(float globalPercentage) {
      globalPercentage = Mth.clamp(globalPercentage, 0.0F, 1.0F);
      float f = globalPercentage * (float)(this.points.length - 1);
      int id = (int)f;
      return this.getLocalPoint(id, f - (float)id);
   }

   @Override
   public Location getLocalPoint(int pointId, float localPercentage) {
      if (pointId + 1 >= this.points.length) {
         return this.points[this.points.length - 1];
      } else {
         Location cpoint = this.points[pointId];
         Location npoint = this.points[pointId + 1];
         return cpoint.add(npoint.subtract(cpoint).multiply((double)localPercentage, (double)localPercentage, (double)localPercentage));
      }
   }

   @Override
   public Location getVelocity(float globalPercentage) {
      return new Location(Level.OVERWORLD, Vec3.ZERO, 0.0F, 0.0F);
   }

   @Override
   public Location getLocalVelocity(int pointId, float localPercentage) {
      return new Location(Level.OVERWORLD, Vec3.ZERO, 0.0F, 0.0F);
   }
}
