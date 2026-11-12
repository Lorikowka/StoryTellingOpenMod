package com.storyteam.storytelling.util.splines;

import com.storyteam.storytelling.util.world.Location;
import net.minecraft.util.Mth;

public class CatmullRomSpline extends Spline {
   public Location[][] neighbors;

   public CatmullRomSpline(Location... points) {
      super(points);
      this.neighbors = new Location[points.length][2];
      this.initNeighbors();
   }

   @Override
   public Location getPoint(float globalPercentage) {
      globalPercentage = Mth.clamp(globalPercentage, 0.0F, 1.0F);
      float f = globalPercentage * (float)(this.getPointAmount() - 1);
      int id = (int)f;
      return this.getLocalPoint(id, f - (float)id);
   }

   @Override
   public Location getVelocity(float globalPercentage) {
      globalPercentage = Mth.clamp(globalPercentage, 0.0F, 1.0F);
      float f = globalPercentage * (float)(this.getPointAmount() - 1);
      int id = (int)f;
      return this.getLocalVelocity(id, f - (float)id);
   }

   @Override
   public Location getLocalPoint(int pointId, float localPercentage) {
      if (pointId + 1 >= this.points.length) {
         return this.points[this.points.length - 1];
      } else {
         Location p0 = this.points[pointId];
         Location p1 = this.neighbors[pointId][1];
         Location p2 = this.neighbors[pointId + 1][0];
         Location p3 = this.points[pointId + 1];
         float lp2 = localPercentage * localPercentage;
         float lp3 = localPercentage * localPercentage * localPercentage;
         float m0 = -lp3 + 3.0F * lp2 - 3.0F * localPercentage + 1.0F;
         float m1 = 3.0F * lp3 - 6.0F * lp2 + 3.0F * localPercentage;
         float m2 = -3.0F * lp3 + 3.0F * lp2;
         return p0.multiply((double)m0, (double)m0, (double)m0)
            .add(p1.multiply((double)m1, (double)m1, (double)m1))
            .add(p2.multiply((double)m2, (double)m2, (double)m2))
            .add(p3.multiply((double)lp3, (double)lp3, (double)lp3));
      }
   }

   @Override
   public Location getLocalVelocity(int pointId, float localPercentage) {
      if (pointId + 1 >= this.points.length) {
         Location op = this.points[this.points.length - 1];
         Location next = this.neighbors[this.points.length - 1][1];
         return next.subtract(op);
      } else {
         Location p0 = this.points[pointId];
         Location p1 = this.neighbors[pointId][1];
         Location p2 = this.neighbors[pointId + 1][0];
         Location p3 = this.points[pointId + 1];
         float lp2 = localPercentage * localPercentage;
         float m0 = -3.0F * lp2 + 6.0F * localPercentage - 3.0F;
         float m1 = 9.0F * lp2 - 12.0F * localPercentage + 3.0F;
         float m2 = -9.0F * lp2 + 6.0F * localPercentage;
         float pr3 = 3.0F * lp2;
         return p0.multiply((double)m0, (double)m0, (double)m0)
            .add(p1.multiply((double)m1, (double)m1, (double)m1))
            .add(p2.multiply((double)m2, (double)m2, (double)m2))
            .add(p3.multiply((double)pr3, (double)pr3, (double)pr3));
      }
   }

   private void initNeighbors() {
      for (int i = 1; i < this.points.length - 1; i++) {
         Location p = this.points[i - 1];
         Location c = this.points[i];
         Location n = this.points[i + 1];
         Location v = n.subtract(p);
         v = v.multiply(0.16666667F, 0.16666667F, 0.16666667F);
         this.neighbors[i][0] = c.add(v.reverse());
         this.neighbors[i][1] = c.add(v);
      }

      Location iv = this.points[1].subtract(this.points[0]).multiply(0.16666667F, 0.16666667F, 0.16666667F);
      this.neighbors[0] = new Location[]{this.points[0].add(iv.reverse()), this.points[0].add(iv)};
      Location ev = this.points[this.points.length - 2].subtract(this.points[this.points.length - 1]).multiply(0.16666667F, 0.16666667F, 0.16666667F);
      this.neighbors[this.neighbors.length - 1] = new Location[]{
         this.points[this.points.length - 1].add(ev), this.points[this.points.length - 1].add(ev.reverse())
      };
   }

   public Location getEndPoint() {
      return this.points[this.points.length - 1];
   }
}
