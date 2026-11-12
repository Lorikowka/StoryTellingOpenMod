package com.storyteam.storytelling.util.splines;

import com.storyteam.storytelling.util.world.Location;

public abstract class Spline {
   public Location[] points;

   public Spline(Location... locations) {
      this.points = locations;
   }

   public abstract Location getPoint(float var1);

   public abstract Location getLocalPoint(int var1, float var2);

   public abstract Location getVelocity(float var1);

   public abstract Location getLocalVelocity(int var1, float var2);

   public int getPointAmount() {
      return this.points.length;
   }
}
