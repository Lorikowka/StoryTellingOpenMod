package com.storyteam.storytelling.system._core.st_cutscene;

import com.storyteam.storytelling.util.splines.CatmullRomSpline;
import com.storyteam.storytelling.util.splines.LinearSpline;
import com.storyteam.storytelling.util.splines.Spline;
import com.storyteam.storytelling.util.world.Location;
import java.util.function.Function;

public enum SplineType {
   CATMULLROM(CatmullRomSpline::new),
   LINEAR(LinearSpline::new);

   public Function<Location[], Spline> splineFactory;

   private SplineType(Function<Location[], Spline> splineFactory) {
      this.splineFactory = splineFactory;
   }
}
