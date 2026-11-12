package com.storyteam.storytelling.util.data.buffered;

import org.zeith.hammeranims.api.animsys.ConfiguredAnimation;

public class AnimationBuffer extends BufferedData<ConfiguredAnimation> {
   public AnimationBuffer() {
      super(ConfiguredAnimation.noAnimation());
   }
}
