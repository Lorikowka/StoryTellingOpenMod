package com.storyteam.storytelling.system.particle;

import com.storyteam.storytelling.util.Color;

public interface IColoredParticle extends IParticle {
   Color getColor();

   void setColor(Color var1);
}
